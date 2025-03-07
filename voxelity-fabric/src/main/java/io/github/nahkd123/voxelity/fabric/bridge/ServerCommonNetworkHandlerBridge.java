package io.github.nahkd123.voxelity.fabric.bridge;

import com.mojang.authlib.GameProfile;

import io.github.nahkd123.voxelity.fabric.server.FabricVoxelityServer;
import net.fabricmc.fabric.mixin.networking.accessor.ServerCommonNetworkHandlerAccessor;

public interface ServerCommonNetworkHandlerBridge extends ServerCommonNetworkHandlerAccessor {
	GameProfile voxelity$getGameProfile();

	default FabricVoxelityServer voxelity$getVoxelityServer() {
		return ((MinecraftServerBridge) getServer()).voxelity$get();
	}
}
