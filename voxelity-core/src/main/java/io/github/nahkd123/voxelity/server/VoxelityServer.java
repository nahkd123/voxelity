package io.github.nahkd123.voxelity.server;

import java.util.Set;

import io.github.nahkd123.voxelity.Voxelity;
import io.github.nahkd123.voxelity.server.world.ServerWorld;

/**
 * <p>
 * Represent a live server. In normal usage, each process is a server, but there
 * are some cases where user install such mod that allows multiple integrated
 * servers to run at the same time (see: speedrunning multiseed mod). Therefore,
 * it is not possible to merge server and {@link Voxelity} into an interface
 * (the only exception is Bukkit-like servers, which only permit 1 server
 * instance for each process).
 * </p>
 */
public interface VoxelityServer {
	Voxelity getPlatform();

	Set<ServerWorld> getWorlds();

	ServerWorld getWorld(String name);
}
