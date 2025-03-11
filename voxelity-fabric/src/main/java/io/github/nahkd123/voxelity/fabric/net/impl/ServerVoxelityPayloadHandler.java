package io.github.nahkd123.voxelity.fabric.net.impl;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.bridge.ServerCommonNetworkHandlerBridge;
import io.github.nahkd123.voxelity.fabric.id.UserIdentity;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayload;
import io.github.nahkd123.voxelity.fabric.net.c2s.NotifyStatusC2SPayload;
import io.github.nahkd123.voxelity.fabric.net.c2s.ServerVoxelityPayloadListener;
import io.github.nahkd123.voxelity.fabric.net.s2c.ClientVoxelityPayloadListener;
import io.github.nahkd123.voxelity.fabric.net.transactional.TransactionsHandler;
import io.github.nahkd123.voxelity.fabric.permission.Capability;
import io.github.nahkd123.voxelity.fabric.server.FabricVoxelityServer;
import io.github.nahkd123.voxelity.fabric.server.editor.FabricServerVoxelityEditor;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerCommonNetworkHandler;

public class ServerVoxelityPayloadHandler implements ServerVoxelityPayloadListener {
	private ClientConnection connection;
	private ServerTransactionsHandler transactions;
	private FabricServerVoxelityEditor editor = null;

	public ServerVoxelityPayloadHandler(ClientConnection connection) {
		this.connection = connection;
		this.transactions = new ServerTransactionsHandler(this);
	}

	@Override
	public TransactionsHandler getTransactionsHandler() { return transactions; }

	public void send(VoxelityPayload<ClientVoxelityPayloadListener> payload) {
		connection.send(new CustomPayloadS2CPacket(payload));
	}

	public FabricVoxelityServer getServer() {
		return ((ServerCommonNetworkHandlerBridge) connection.getPacketListener()).voxelity$getVoxelityServer();
	}

	public FabricServerVoxelityEditor getEditor() { return editor; }

	@Override
	public void onNotifyStatus(NotifyStatusC2SPayload payload) {
		switch (payload.status()) {
		case EDITOR_OPEN:
			if (!Capability.EDITOR.hasCapability(connection)) return;
			onEditorOpen();
			break;
		case EDITOR_CLOSE:
			if (!Capability.EDITOR.hasCapability(connection)) return;
			onEditorClose();
			break;
		default:
			VoxelityFabric.LOGGER.warn("Client status not implemented: {}", payload.status());
			break;
		};
	}

	public void onEditorOpen() {
		if (editor != null) return;
		var identity = UserIdentity.player((ServerCommonNetworkHandler) connection.getPacketListener());
		editor = new FabricServerVoxelityEditor(getServer(), this, identity);
		editor.loadEditorData();
	}

	public void onEditorClose() {
		if (editor == null) return;
		editor.saveEditorData();
		editor = null;
	}
}
