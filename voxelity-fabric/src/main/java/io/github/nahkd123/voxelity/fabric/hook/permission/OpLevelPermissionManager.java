package io.github.nahkd123.voxelity.fabric.hook.permission;

import com.mojang.authlib.GameProfile;

import io.github.nahkd123.voxelity.fabric.bridge.ServerCommonNetworkHandlerBridge;
import net.minecraft.network.ClientConnection;

public class OpLevelPermissionManager implements PermissionManager {
	public static final int LEVEL_PLAYER = 0;
	public static final int LEVEL_MODERATOR = 1;
	public static final int LEVEL_GAMEMASTER = 2;
	public static final int LEVEL_ADMIN = 3;
	public static final int LEVEL_OWNER = 4;

	public int getPermissionLevel(ClientConnection connection) {
		if (connection.getPacketListener() instanceof ServerCommonNetworkHandlerBridge bridge) {
			GameProfile profile = bridge.voxelity$getGameProfile();
			return bridge.getServer().getPermissionLevel(profile);
		}

		return 0;
	}

	@Override
	public boolean canUseEditor(ClientConnection connection) {
		return getPermissionLevel(connection) >= LEVEL_GAMEMASTER;
	}
}
