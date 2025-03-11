package io.github.nahkd123.voxelity.fabric.server;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.id.UserIdentity;
import io.github.nahkd123.voxelity.server.VoxelityServer;
import io.github.nahkd123.voxelity.server.editor.EditorPaths;
import io.github.nahkd123.voxelity.server.world.ServerWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;

public class FabricVoxelityServer implements VoxelityServer {
	private MinecraftServer server;
	private Path voxelityRoot;

	public FabricVoxelityServer(MinecraftServer server) {
		this.server = server;
		this.voxelityRoot = server.getSavePath(WorldSavePath.ROOT).resolve("voxelity");
	}

	@Override
	public VoxelityFabric getPlatform() { return VoxelityFabric.getFabricPlatform(); }

	public MinecraftServer getMinecraftServer() { return server; }

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

	public Path getVoxelityRoot() { return voxelityRoot; }

	public EditorPaths getEditorPaths(UserIdentity identity) {
		return EditorPaths.ofRoot(getVoxelityRoot().resolve(identity.getDatabaseKey()));
	}
}
