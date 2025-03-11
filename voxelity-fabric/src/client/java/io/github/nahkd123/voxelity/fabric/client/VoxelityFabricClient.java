package io.github.nahkd123.voxelity.fabric.client;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.bridge.ClientConnectionBridge;
import io.github.nahkd123.voxelity.fabric.client.net.ClientVoxelityPayloadHandler;
import io.github.nahkd123.voxelity.fabric.net.s2c.ClientVoxelityPayloadListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.mixin.networking.client.accessor.ClientCommonNetworkHandlerAccessor;

public class VoxelityFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		VoxelityFabric.LOGGER.info("Hello from client!");

		ClientVoxelityPayloadListener.registerPayloads(id -> {
			ClientConfigurationNetworking.registerGlobalReceiver(id, (payload, context) -> {
				var connection = ((ClientCommonNetworkHandlerAccessor) context.networkHandler()).getConnection();
				var listener = ((ClientConnectionBridge) connection).voxelity$getClientListener();
				payload.apply(listener);
			});

			ClientPlayNetworking.registerGlobalReceiver(id, (payload, context) -> {
				// There can be at most 1 play state network handler on client it seems
				var connection = ((ClientCommonNetworkHandlerAccessor) context.client().getNetworkHandler())
					.getConnection();
				var listener = ((ClientConnectionBridge) connection).voxelity$getClientListener();
				payload.apply(listener);
			});
		});

		ClientConfigurationConnectionEvents.INIT.register((handler, client) -> {
			var connection = ((ClientCommonNetworkHandlerAccessor) handler).getConnection();
			var listener = new ClientVoxelityPayloadHandler(connection, client);
			((ClientConnectionBridge) connection).voxelity$setClientListener(listener);
			VoxelityFabric.devlog("Attached Voxelity payloads handler to client connection!");
		});
	}
}
