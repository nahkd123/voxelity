package io.github.nahkd123.voxelity.fabric.net;

import java.util.List;

import com.mojang.authlib.GameProfile;

import io.github.nahkd123.voxelity.fabric.bridge.ClientConnectionBridge;
import io.github.nahkd123.voxelity.fabric.bridge.ServerCommonNetworkHandlerBridge;
import io.github.nahkd123.voxelity.fabric.hook.permission.PermissionManager;
import io.github.nahkd123.voxelity.fabric.server.editor.FabricServerVoxelityEditor;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.text.Text;

public enum VoxelityFeature {
	EDITOR(
		Text.translatable("voxelity.feature.editor"),
		Text.translatable("voxelity.feature.editor.enabled"),
		Text.translatable("voxelity.feature.editor.disabled")) {
		@Override
		protected void onFeatureEnable(ClientConnection connection) {
			if (connection.getPacketListener() instanceof ServerCommonNetworkHandlerBridge bridge) {
				GameProfile profile = bridge.voxelity$getGameProfile();
				FabricServerVoxelityEditor.createEditor(connection, profile);
			}
		}

		@Override
		protected void onFeatureDisable(ClientConnection connection) {
			FabricServerVoxelityEditor editor = FabricServerVoxelityEditor.getEditor(connection);
			if (editor != null) editor.closeSession();
		}

		@Override
		public boolean hasPermission(ClientConnection connection, PermissionManager permissions) {
			return permissions.canUseEditor(connection);
		}
	};

	private Text label;
	private Text enabledMessage;
	private Text disabledMessage;

	private VoxelityFeature(Text label, Text enabledMessage, Text disabledMessage) {
		this.label = label;
		this.enabledMessage = enabledMessage;
		this.disabledMessage = disabledMessage;
	}

	public Text getLabel() { return label; }

	public Text getEnabledMessage() { return enabledMessage; }

	public Text getDisabledMessage() { return disabledMessage; }

	protected void onFeatureEnable(ClientConnection connection) {}

	protected void onFeatureDisable(ClientConnection connection) {}

	public boolean isEnabled(ClientConnection connection) {
		ClientConnectionBridge bridge = (ClientConnectionBridge) connection;
		return bridge.voxelity$isFeatureEnabled(this);
	}

	/**
	 * <p>
	 * Set the feature enable state for specific client connection. This does not
	 * automatically send feature state update to client; you have to send it
	 * manually.
	 * </p>
	 * 
	 * @param connection The connection to enable feature.
	 * @param state      Feature enable state.
	 */
	public void setEnable(ClientConnection connection, boolean state) {
		ClientConnectionBridge bridge = (ClientConnectionBridge) connection;
		boolean lastState = bridge.voxelity$isFeatureEnabled(this);
		bridge.voxelity$setFeature(this, state);

		if (connection.getSide() == NetworkSide.SERVERBOUND && (lastState ^ state)) {
			if (state) onFeatureEnable(connection);
			else onFeatureDisable(connection);
		}
	}

	public abstract boolean hasPermission(ClientConnection connection, PermissionManager permissions);

	public static final PacketCodec<PacketByteBuf, VoxelityFeature> PACKET_CODEC = VoxelityPayloads
		.createEnumCodec(List.of(VoxelityFeature.values()));
}
