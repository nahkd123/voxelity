package io.github.nahkd123.voxelity.fabric.hook.permission;

import com.mojang.authlib.GameProfile;

import io.github.nahkd123.voxelity.fabric.bridge.ServerCommonNetworkHandlerBridge;
import io.github.nahkd123.voxelity.fabric.net.VoxelityFeature;
import io.github.nahkd123.voxelity.fabric.net.s2c.VoxelityS2CFeaturePayload;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * <p>
 * Manage permissions for using some of Voxelity features.
 * </p>
 */
public interface PermissionManager {
	/**
	 * <p>
	 * Use permission level of player as permission manager for Voxelity.
	 * </p>
	 */
	static OpLevelPermissionManager OP_LEVEL = new OpLevelPermissionManager();

	boolean canUseEditor(ClientConnection connection);

	static void recalculatePermissions(PlayerManager manager, GameProfile profile) {
		ServerPlayerEntity player = manager.getPlayer(profile.getId());
		if (player == null) return; // TODO delay until player respawn
		var bridge = (ServerCommonNetworkHandlerBridge) player.networkHandler;
		PermissionManager permissions = bridge.voxelity$getVoxelityServer().getPermissionManager();
		ClientConnection connection = bridge.getConnection();

		for (VoxelityFeature feature : VoxelityFeature.values()) {
			if (feature.isEnabled(connection) && !feature.hasPermission(connection, permissions)) {
				feature.setEnable(connection, false);
				var payload = new VoxelityS2CFeaturePayload(feature, feature.isEnabled(connection));
				connection.send(new CustomPayloadS2CPacket(payload));
			}
		}
	}
}
