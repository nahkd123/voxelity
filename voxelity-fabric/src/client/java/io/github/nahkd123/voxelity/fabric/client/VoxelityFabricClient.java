package io.github.nahkd123.voxelity.fabric.client;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.client.bridge.MinecraftClientBridge;
import io.github.nahkd123.voxelity.fabric.client.command.VoxelityClientCommand;
import io.github.nahkd123.voxelity.fabric.client.net.VoxelityS2CPayloadHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

public class VoxelityFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		VoxelityFabric.LOGGER.info("Hello from client!");

		VoxelityS2CPayloadHandlers.initialize();

		ClientLoginConnectionEvents.INIT.register((handler, client) -> resetClientStates(client));
		ClientConfigurationConnectionEvents.DISCONNECT.register((handler, client) -> resetClientStates(client));
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> resetClientStates(client));

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(VoxelityClientCommand.ROOT);
		});
	}

	private void resetClientStates(MinecraftClient client) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment())
			VoxelityFabric.LOGGER.info("Resetting Voxelity client states...");
		((MinecraftClientBridge) client).voxelity$reset();
	}
}
