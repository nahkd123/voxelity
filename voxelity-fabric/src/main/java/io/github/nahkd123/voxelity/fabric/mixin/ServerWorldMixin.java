package io.github.nahkd123.voxelity.fabric.mixin;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements io.github.nahkd123.voxelity.server.world.ServerWorld {
	@SuppressWarnings("resource")
	@Override
	public String getName() {
		ServerWorld mc = (ServerWorld) (Object) this;
		MinecraftServer server = mc.getServer();
		Optional<RegistryKey<World>> optional = server.getWorldRegistryKeys().stream()
			.filter(key -> key.equals(mc.getRegistryKey()))
			.findAny();
		return optional.get().getValue().toString();
	}
}
