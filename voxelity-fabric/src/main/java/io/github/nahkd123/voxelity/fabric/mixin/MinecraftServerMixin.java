package io.github.nahkd123.voxelity.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import io.github.nahkd123.voxelity.fabric.bridge.MinecraftServerBridge;
import io.github.nahkd123.voxelity.fabric.server.FabricVoxelityServer;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements MinecraftServerBridge {
	@Unique
	private FabricVoxelityServer voxelity = null;

	@Override
	public FabricVoxelityServer getVoxelity() {
		if (voxelity == null) voxelity = new FabricVoxelityServer((MinecraftServer) (Object) this);
		return voxelity;
	}
}
