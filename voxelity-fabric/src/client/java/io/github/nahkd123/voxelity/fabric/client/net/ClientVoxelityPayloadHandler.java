package io.github.nahkd123.voxelity.fabric.client.net;

import java.util.Set;

import io.github.nahkd123.voxelity.fabric.bridge.ClientConnectionBridge;
import io.github.nahkd123.voxelity.fabric.client.editor.FabricClientVoxelityEditor;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayload;
import io.github.nahkd123.voxelity.fabric.net.c2s.ClientStatus;
import io.github.nahkd123.voxelity.fabric.net.c2s.NotifyStatusC2SPayload;
import io.github.nahkd123.voxelity.fabric.net.c2s.ServerVoxelityPayloadListener;
import io.github.nahkd123.voxelity.fabric.net.s2c.CapabilitiesS2CPayload;
import io.github.nahkd123.voxelity.fabric.net.s2c.ClientVoxelityPayloadListener;
import io.github.nahkd123.voxelity.fabric.net.transactional.TransactionsHandler;
import io.github.nahkd123.voxelity.fabric.permission.Capability;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.c2s.common.CustomPayloadC2SPacket;

public class ClientVoxelityPayloadHandler implements ClientVoxelityPayloadListener {
	private ClientConnection connection;
	private ClientTransactionsHandler transactions;
	private FabricClientVoxelityEditor editor = null;
	private MinecraftClient client;

	public ClientVoxelityPayloadHandler(ClientConnection connection, MinecraftClient client) {
		this.connection = connection;
		this.client = client;
		this.transactions = new ClientTransactionsHandler(this);
	}

	private ClientConnectionBridge connectionBridge() {
		return (ClientConnectionBridge) connection;
	}

	public void send(VoxelityPayload<ServerVoxelityPayloadListener> payload) {
		connection.send(new CustomPayloadC2SPacket(payload));
	}

	@Override
	public TransactionsHandler getTransactionsHandler() { return transactions; }

	public FabricClientVoxelityEditor getEditor() { return editor; }

	@Override
	public void onCapabilities(CapabilitiesS2CPayload payload) {
		Set<Capability> capabilities = connectionBridge().voxelity$getCapabilities();
		Set<Capability> oldSet = Set.copyOf(capabilities);
		Set<Capability> newSet = payload.capabilities();
		capabilities.clear();
		capabilities.addAll(newSet);
		for (Capability c : newSet) { if (!oldSet.contains(c)) onCapabilityChange(c, true); }
		for (Capability c : oldSet) { if (!newSet.contains(c)) onCapabilityChange(c, false); }
	}

	private void onCapabilityChange(Capability capability, boolean state) {
		switch (capability) {
		case EDITOR:
			if (!state && editor != null) {
				// TODO close client UI (or let client automatically close)
				editor = null;
			}
			break;
		default:
			break;
		}
	}

	/**
	 * <p>
	 * Open a new editor session. This will create a session on client and then send
	 * {@link ClientStatus#EDITOR_OPEN} to server to create session on server. This
	 * method does not open editor UI, but rather allocating resources on both
	 * client and server to hold editor session before opening UI.
	 * </p>
	 */
	public void openEditor() {
		if (!Capability.EDITOR.hasCapability(connection))
			throw new IllegalStateException("Client missing editor capability (try setting it from server)");
		if (editor != null) return;
		editor = new FabricClientVoxelityEditor(this, client);
		send(new NotifyStatusC2SPayload(ClientStatus.EDITOR_OPEN));
	}

	/**
	 * <p>
	 * Close an existing editor session. This will unset session on client and then
	 * send {@link ClientStatus#EDITOR_CLOSE} to server to destroy session on
	 * server.
	 * </p>
	 */
	public void closeEditor() {
		if (!Capability.EDITOR.hasCapability(connection))
			throw new IllegalStateException("Client missing editor capability (try setting it from server)");
		if (editor == null) return;
		send(new NotifyStatusC2SPayload(ClientStatus.EDITOR_CLOSE));
		editor = null;
		// TODO close client UI (or let client automatically close)
	}
}
