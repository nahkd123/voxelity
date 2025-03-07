package io.github.nahkd123.voxelity.fabric;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mojang.authlib.GameProfile;

import io.github.nahkd123.voxelity.Voxelity;
import io.github.nahkd123.voxelity.fabric.bridge.MinecraftServerBridge;
import io.github.nahkd123.voxelity.fabric.bridge.ServerCommonNetworkHandlerBridge;
import io.github.nahkd123.voxelity.fabric.hook.permission.PermissionManager;
import io.github.nahkd123.voxelity.fabric.net.VoxelityC2SPayloadHandlers;
import io.github.nahkd123.voxelity.fabric.net.VoxelityFeature;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloads;
import io.github.nahkd123.voxelity.fabric.registry.FabricRegistries;
import io.github.nahkd123.voxelity.fabric.server.FabricVoxelityServer;
import io.github.nahkd123.voxelity.fabric.server.editor.FabricServerVoxelityEditor;
import io.github.nahkd123.voxelity.fabric.voxel.FabricVoxels;
import io.github.nahkd123.voxelity.registry.Registries;
import io.github.nahkd123.voxelity.server.world.ServerWorld;
import io.github.nahkd123.voxelity.voxel.Voxels;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
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

		VoxelityPayloads.initialize();
		VoxelityC2SPayloadHandlers.initialize();

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			var bridge = (ServerCommonNetworkHandlerBridge) handler;
			ClientConnection connection = bridge.getConnection();

			if (VoxelityFeature.EDITOR.isEnabled(connection)) {
				GameProfile profile = bridge.voxelity$getGameProfile();
				FabricServerVoxelityEditor.createEditor(connection, profile);
			}
		});

		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			PlayerManager playerManager = newPlayer.getServer().getPlayerManager();
			GameProfile gameProfile = newPlayer.getGameProfile();
			PermissionManager.recalculatePermissions(playerManager, gameProfile);
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
}
