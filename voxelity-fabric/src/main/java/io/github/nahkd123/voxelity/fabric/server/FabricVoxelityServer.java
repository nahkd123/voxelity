package io.github.nahkd123.voxelity.fabric.server;

import java.util.HashSet;
import java.util.Set;

import io.github.nahkd123.voxelity.Voxelity;
import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.server.VoxelityServer;
import io.github.nahkd123.voxelity.server.world.ServerWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

public class FabricVoxelityServer implements VoxelityServer {
	private MinecraftServer server;

	public FabricVoxelityServer(MinecraftServer server) {
		this.server = server;
	}

	@Override
	public Voxelity getPlatform() { return VoxelityFabric.getFabricPlatform(); }

	@Override
	public Set<ServerWorld> getWorlds() {
		Set<ServerWorld> world = new HashSet<>();
		for (net.minecraft.server.world.ServerWorld mc : server.getWorlds()) world.add((ServerWorld) mc);
		return world;
	}

	@Override
	public ServerWorld getWorld(String name) {
		Identifier id = Identifier.of(name);
		return (ServerWorld) server.getWorld(RegistryKey.of(RegistryKeys.WORLD, id));
	}
}
