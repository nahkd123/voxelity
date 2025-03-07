package io.github.nahkd123.voxelity.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.authlib.GameProfile;

import io.github.nahkd123.voxelity.fabric.bridge.ServerCommonNetworkHandlerBridge;
import net.minecraft.server.network.ServerCommonNetworkHandler;

@Mixin(ServerCommonNetworkHandler.class)
public abstract class ServerCommonNetworkHandlerMixin implements ServerCommonNetworkHandlerBridge {
	@Shadow
	protected abstract GameProfile getProfile();

	@Override
	public GameProfile voxelity$getGameProfile() {
		return getProfile();
	}
}
