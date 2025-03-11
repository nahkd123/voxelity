package io.github.nahkd123.voxelity.fabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.nahkd123.voxelity.Voxelity;
import io.github.nahkd123.voxelity.fabric.bridge.ClientConnectionBridge;
import io.github.nahkd123.voxelity.fabric.bridge.MinecraftServerBridge;
import io.github.nahkd123.voxelity.fabric.bridge.ServerCommonNetworkHandlerBridge;
import io.github.nahkd123.voxelity.fabric.net.c2s.ServerVoxelityPayloadListener;
import io.github.nahkd123.voxelity.fabric.net.impl.ServerVoxelityPayloadHandler;
import io.github.nahkd123.voxelity.fabric.registry.FabricRegistries;
import io.github.nahkd123.voxelity.fabric.server.FabricVoxelityServer;
import io.github.nahkd123.voxelity.fabric.voxel.FabricVoxels;
import io.github.nahkd123.voxelity.registry.Registries;
import io.github.nahkd123.voxelity.server.world.ServerWorld;
import io.github.nahkd123.voxelity.voxel.Voxels;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

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

		ServerVoxelityPayloadListener.registerPayloads();

		ServerConfigurationConnectionEvents.BEFORE_CONFIGURE.register((handler, server) -> {
			var connection = ((ServerCommonNetworkHandlerBridge) handler).getConnection();
			var listener = new ServerVoxelityPayloadHandler(connection);
			((ClientConnectionBridge) connection).voxelity$setServerListener(listener);
		});

		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			LOGGER.info("Development environment detected, expect more logging.");

			ServerLifecycleEvents.SERVER_STARTED.register(server -> {
				FabricVoxelityServer voxelity = ((MinecraftServerBridge) server).voxelity$get();
				LOGGER.info("Server started");
				for (ServerWorld world : voxelity.getWorlds()) LOGGER.info("  Server world: {}", world.getName());
			});
		}
	}

	public static Identifier id(String id) {
		return Identifier.of(MODID, id);
	}

	public static void devlog(String message, Object... args) {
		if (!FabricLoader.getInstance().isDevelopmentEnvironment()) return;
		LOGGER.info(message, args);
	}
}
