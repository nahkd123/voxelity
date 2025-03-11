package io.github.nahkd123.voxelity.fabric.net.c2s;

import io.github.nahkd123.voxelity.fabric.bridge.ClientConnectionBridge;
import io.github.nahkd123.voxelity.fabric.bridge.ServerCommonNetworkHandlerBridge;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayload;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloadListener;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public interface ServerVoxelityPayloadListener extends VoxelityPayloadListener {
	void onNotifyStatus(NotifyStatusC2SPayload payload);

	static void registerPayloads() {
		// System payloads
		register(CancelTransactionC2SPayload.ID, CancelTransactionC2SPayload.CODEC);

		// All other payloads
		register(NotifyStatusC2SPayload.ID, NotifyStatusC2SPayload.CODEC);
	}

	private static <T extends VoxelityPayload<ServerVoxelityPayloadListener>> void register(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> codec) {
		PayloadTypeRegistry.configurationS2C().register(id, codec);
		PayloadTypeRegistry.playS2C().register(id, codec);

		ServerConfigurationNetworking.registerGlobalReceiver(id, (payload, context) -> {
			ServerConfigurationNetworkHandler mcHandler = context.networkHandler();
			ClientConnection connection = ((ServerCommonNetworkHandlerBridge) mcHandler).getConnection();
			handle(connection, payload);
		});

		ServerPlayNetworking.registerGlobalReceiver(id, (payload, context) -> {
			ServerPlayNetworkHandler mcHandler = context.player().networkHandler;
			ClientConnection connection = ((ServerCommonNetworkHandlerBridge) mcHandler).getConnection();
			handle(connection, payload);
		});
	}

	private static void handle(ClientConnection connection, VoxelityPayload<ServerVoxelityPayloadListener> payload) {
		ServerVoxelityPayloadListener listener = ((ClientConnectionBridge) connection).voxelity$getServerListener();
		payload.apply(listener);
	}
}
