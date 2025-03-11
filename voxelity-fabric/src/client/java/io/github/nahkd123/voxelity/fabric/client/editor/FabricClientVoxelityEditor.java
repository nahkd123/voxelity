package io.github.nahkd123.voxelity.fabric.client.editor;

import java.util.Set;

import io.github.nahkd123.voxelity.client.editor.ClientVoxelityEditor;
import io.github.nahkd123.voxelity.client.editor.EditRequest;
import io.github.nahkd123.voxelity.editor.history.HistoryQueue;
import io.github.nahkd123.voxelity.editor.view.EditorView;
import io.github.nahkd123.voxelity.fabric.bridge.ClientConnectionBridge;
import io.github.nahkd123.voxelity.fabric.client.net.ClientVoxelityPayloadHandler;
import io.github.nahkd123.voxelity.world.World;
import net.fabricmc.fabric.mixin.networking.client.accessor.ClientCommonNetworkHandlerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.ClientConnection;

public class FabricClientVoxelityEditor implements ClientVoxelityEditor {
	private ClientVoxelityPayloadHandler handler;
	private MinecraftClient client;

	public FabricClientVoxelityEditor(ClientVoxelityPayloadHandler handler, MinecraftClient client) {
		this.handler = handler;
		this.client = client;
	}

	public static FabricClientVoxelityEditor getSessionOf(ClientConnection connection) {
		var listener = ((ClientConnectionBridge) connection).voxelity$getClientListener();
		return ((ClientVoxelityPayloadHandler) listener).getEditor();
	}

	public static FabricClientVoxelityEditor getSessionOf(ClientPlayerEntity player) {
		return getSessionOf(((ClientCommonNetworkHandlerAccessor) player.networkHandler).getConnection());
	}

	public static FabricClientVoxelityEditor getSessionOf(MinecraftClient client) {
		return getSessionOf(((ClientCommonNetworkHandlerAccessor) client.getNetworkHandler()).getConnection());
	}

	public static FabricClientVoxelityEditor getSession() { return getSessionOf(MinecraftClient.getInstance()); }

	public ClientVoxelityPayloadHandler getHandler() { return handler; }

	public MinecraftClient getClient() { return client; }

	@Override
	public HistoryQueue getHistory() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<? extends World> getWorlds() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public EditorView getView() { // TODO Auto-generated method stub
		return null;
	}

	@Override
	public EditRequest createEditRequest(World target) {
		// TODO Auto-generated method stub
		return null;
	}
}
