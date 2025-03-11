package io.github.nahkd123.voxelity.fabric.server.editor;

import java.io.IOException;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.bridge.ClientConnectionBridge;
import io.github.nahkd123.voxelity.fabric.bridge.ServerCommonNetworkHandlerBridge;
import io.github.nahkd123.voxelity.fabric.id.UserIdentity;
import io.github.nahkd123.voxelity.fabric.net.impl.ServerVoxelityPayloadHandler;
import io.github.nahkd123.voxelity.fabric.server.FabricVoxelityServer;
import io.github.nahkd123.voxelity.server.VoxelityServer;
import io.github.nahkd123.voxelity.server.editor.ServerVoxelityEditor;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ServerPlayerEntity;

public class FabricServerVoxelityEditor extends ServerVoxelityEditor {
	private FabricVoxelityServer server;
	private ServerVoxelityPayloadHandler handler;
	private UserIdentity identity;

	public FabricServerVoxelityEditor(FabricVoxelityServer server, ServerVoxelityPayloadHandler handler, UserIdentity identity) {
		this.server = server;
		this.handler = handler;
		this.identity = identity;
	}

	/**
	 * <p>
	 * Create a new server editor session that is completely detached from any
	 * connection. The main use for this is to be used on other mods. Because it
	 * does not attached to connection, it can't be controlled by Voxelity client.
	 * Data will not be loaded automatically; use {@link #loadEditorData()} or
	 * {@link #loadEditorData(io.github.nahkd123.voxelity.server.editor.EditorPaths)}
	 * to load editor data.
	 * </p>
	 * 
	 * @param server   The server access.
	 * @param identity The identity of user. This will be used for loading data.
	 * @return The editor session.
	 */
	public static FabricServerVoxelityEditor createSession(FabricVoxelityServer server, UserIdentity identity) {
		return new FabricServerVoxelityEditor(server, null, identity);
	}

	/**
	 * <p>
	 * Create a new server editor session, using server's identity. Data will not be
	 * loaded automatically; use {@link #loadEditorData()} or
	 * {@link #loadEditorData(io.github.nahkd123.voxelity.server.editor.EditorPaths)}
	 * to load editor data.
	 * </p>
	 * 
	 * @param server The server access.
	 * @return The editor session.
	 */
	public static FabricServerVoxelityEditor createServerSession(FabricVoxelityServer server) {
		return new FabricServerVoxelityEditor(server, null, UserIdentity.server());
	}

	/**
	 * <p>
	 * Get editor session from user's connection. Returns {@code null} if user
	 * haven't made request to create editor session on server yet.
	 * </p>
	 * 
	 * @param connection The connection to user.
	 * @return The editor session.
	 */
	public static FabricServerVoxelityEditor getSessionOf(ClientConnection connection) {
		var listener = ((ClientConnectionBridge) connection).voxelity$getServerListener();
		return ((ServerVoxelityPayloadHandler) listener).getEditor();
	}

	/**
	 * <p>
	 * Get editor session from player. Returns {@code null} if player haven't made
	 * request to create editor session on server yet.
	 * </p>
	 * 
	 * @param player The player.
	 * @return The editor session.
	 */
	public static FabricServerVoxelityEditor getSessionOf(ServerPlayerEntity player) {
		return getSessionOf(((ServerCommonNetworkHandlerBridge) player.networkHandler).getConnection());
	}

	@Override
	public VoxelityServer getServer() { return server; }

	public ServerVoxelityPayloadHandler getPayloadHandler() { return handler; }

	public UserIdentity getIdentity() { return identity; }

	public void loadEditorData() {
		try {
			loadEditorData(server.getEditorPaths(getIdentity()));
		} catch (IOException e) {
			e.printStackTrace();
			VoxelityFabric.LOGGER.warn("Failed to load editor data for {}", getIdentity());
		}
	}

	public void saveEditorData() {
		try {
			saveEditorData(server.getEditorPaths(getIdentity()));
		} catch (IOException e) {
			e.printStackTrace();
			VoxelityFabric.LOGGER.warn("Failed to save editor data for {}", getIdentity());
		}
	}
}
