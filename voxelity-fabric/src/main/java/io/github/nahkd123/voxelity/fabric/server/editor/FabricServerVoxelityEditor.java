package io.github.nahkd123.voxelity.fabric.server.editor;

import java.io.IOException;

import com.mojang.authlib.GameProfile;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.bridge.ClientConnectionBridge;
import io.github.nahkd123.voxelity.fabric.bridge.MinecraftServerBridge;
import io.github.nahkd123.voxelity.fabric.net.s2c.VoxelityS2CEditorStatePayload;
import io.github.nahkd123.voxelity.fabric.net.s2c.VoxelityS2CHistoryRefreshPayload;
import io.github.nahkd123.voxelity.fabric.net.s2c.VoxelityS2CNotifyPayload;
import io.github.nahkd123.voxelity.fabric.server.FabricVoxelityServer;
import io.github.nahkd123.voxelity.server.editor.EditRequestHandler;
import io.github.nahkd123.voxelity.server.editor.EditorPaths;
import io.github.nahkd123.voxelity.server.editor.ServerVoxelityEditor;
import io.github.nahkd123.voxelity.server.world.ServerWorld;
import net.fabricmc.fabric.mixin.networking.accessor.ServerCommonNetworkHandlerAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class FabricServerVoxelityEditor extends ServerVoxelityEditor {
	private boolean destroyed = false;
	private FabricVoxelityServer server;
	private ClientConnection connection;
	private GameProfile profile;

	private FabricServerVoxelityEditor(FabricVoxelityServer server, ClientConnection connection, GameProfile profile) {
		this.server = server;

		if (connection != null) {
			if (connection.getSide() != NetworkSide.SERVERBOUND)
				throw new IllegalArgumentException("Connection is not server-side");
			if (profile == null)
				throw new IllegalArgumentException("Connection and identity (profile) must present");
			this.connection = connection;
			this.profile = profile;
		}
	}

	/**
	 * <p>
	 * Get server-side editor session from server-side client connection.
	 * </p>
	 * 
	 * @param connection The server-side object of client connection.
	 * @return The editor session, or {@code null} if editor session is not created
	 *         yet.
	 */
	public static FabricServerVoxelityEditor getEditor(ClientConnection connection) {
		return ((ClientConnectionBridge) connection).voxelity$getServerEditor();
	}

	/**
	 * <p>
	 * Create new server-side editor session from server-side client connection. The
	 * server will automatically notify client and a new client-side session will be
	 * created.
	 * </p>
	 * <p>
	 * Both connection and editor's identity (represented as {@link GameProfile})
	 * must be present. To create a new editor that is not attached to any identity,
	 * use {@link #createEditor(FabricVoxelityServer)}.
	 * </p>
	 * 
	 * @param connection The server-side object of client connection.
	 * @param profile    The identity of editor.
	 * @return The editor session.
	 */
	public static FabricServerVoxelityEditor createEditor(ClientConnection connection, GameProfile profile) {
		FabricServerVoxelityEditor session = getEditor(connection);
		if (session != null) return session;

		// Create new editor with identity
		if (!(connection.getPacketListener() instanceof ServerCommonNetworkHandler networkHandler))
			throw new RuntimeException("Server-side editor can only be created from server-side object");
		MinecraftServer mc = ((ServerCommonNetworkHandlerAccessor) networkHandler).getServer();
		FabricVoxelityServer server = ((MinecraftServerBridge) mc).voxelity$get();
		session = new FabricServerVoxelityEditor(server, connection, profile);
		((ClientConnectionBridge) connection).voxelity$setServerEditor(session);
		connection.send(new CustomPayloadS2CPacket(VoxelityS2CNotifyPayload.EDITOR_SESSION_CREATE));

		if (FabricLoader.getInstance().isDevelopmentEnvironment())
			VoxelityFabric.LOGGER.info("Created editor session for {}", profile);

		try {
			EditorPaths editorPaths = session.getServer().getEditorPaths(profile);
			session.loadEditorData(editorPaths);
		} catch (IOException e) {
			e.printStackTrace();
			VoxelityFabric.LOGGER.warn("Failed to load editor data for {}", profile);
		}

		// Sync editor states
		if (FabricLoader.getInstance().isDevelopmentEnvironment())
			VoxelityFabric.LOGGER.info("Sync editor states with {}...", profile);
		connection.send(new CustomPayloadS2CPacket(VoxelityS2CEditorStatePayload.fromEditor(session)));
		connection.send(new CustomPayloadS2CPacket(VoxelityS2CHistoryRefreshPayload.fromHistory(session.getHistory())));

		return session;
	}

	/**
	 * <p>
	 * Create a new identity-free editor. Identity-free editors are meant to be used
	 * by mods to automate editing.
	 * </p>
	 * 
	 * @param server The server.
	 * @return The identity-free editor session.
	 */
	public static FabricServerVoxelityEditor createEditor(FabricVoxelityServer server) {
		return new FabricServerVoxelityEditor(server, null, null);
	}

	private void ensureNotDestroyed() {
		if (destroyed) throw new IllegalStateException("Editor destroyed");
	}

	public GameProfile getProfile() { return profile; }

	@Override
	public FabricVoxelityServer getServer() { return server; }

	public ServerWorld getCurrentWorld() {
		if (connection.getPacketListener() instanceof ServerPlayNetworkHandler playHandler) {
			ServerPlayerEntity player = playHandler.getPlayer();
			if (player == null) return (ServerWorld) server.getMinecraftServer().getOverworld();
			return (ServerWorld) player.getServerWorld();
		}

		return (ServerWorld) server.getMinecraftServer().getOverworld();
	}

	@Override
	public EditRequestHandler createEditRequestHandler(ServerWorld target) {
		ensureNotDestroyed();
		// TODO Hook with region manager
		return () -> target;
	}

	/**
	 * <p>
	 * Close this editor session. The server will notify client that the editor
	 * session has been close and client-side session will be destroyed.
	 * </p>
	 */
	public void closeSession() {
		if (destroyed) return;

		if (connection != null) {
			if (FabricLoader.getInstance().isDevelopmentEnvironment())
				VoxelityFabric.LOGGER.info("Destroying editor session for {}", profile);
			connection.send(new CustomPayloadS2CPacket(VoxelityS2CNotifyPayload.EDITOR_SESSION_DESTROY));
			((ClientConnectionBridge) connection).voxelity$setServerEditor(null);
			connection = null;
			profile = null;
		}

		server = null;
	}
}
