package io.github.nahkd123.voxelity.fabric.client;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import net.fabricmc.api.ClientModInitializer;

public class VoxelityFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		VoxelityFabric.LOGGER.info("Hello from client!");
	}
}
