package io.github.nahkd123.voxelity.fabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.nahkd123.voxelity.Voxelity;
import io.github.nahkd123.voxelity.fabric.bridge.MinecraftServerBridge;
import io.github.nahkd123.voxelity.fabric.registry.FabricRegistries;
import io.github.nahkd123.voxelity.fabric.server.FabricVoxelityServer;
import io.github.nahkd123.voxelity.fabric.voxel.FabricVoxels;
import io.github.nahkd123.voxelity.registry.Registries;
import io.github.nahkd123.voxelity.server.world.ServerWorld;
import io.github.nahkd123.voxelity.voxel.Voxels;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class VoxelityFabric implements ModInitializer, Voxelity {
	public static final String MODID = "voxelity";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	private static VoxelityFabric platform;
	private FabricRegistries registries = FabricRegistries.REGISTRIES;
	private FabricVoxels voxels = FabricVoxels.VOXELS;

	public static VoxelityFabric getFabricPlatform() { return platform; }

	@Override
	public Registries getRegistries() { return registries; }

	@Override
	public Voxels getVoxels() { return voxels; }

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Fabric platform...");
		platform = this;

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			FabricVoxelityServer voxelity = ((MinecraftServerBridge) server).getVoxelity();
			LOGGER.info("Server started");
			for (ServerWorld world : voxelity.getWorlds()) LOGGER.info("  Server world: {}", world.getName());
		});
	}
}
