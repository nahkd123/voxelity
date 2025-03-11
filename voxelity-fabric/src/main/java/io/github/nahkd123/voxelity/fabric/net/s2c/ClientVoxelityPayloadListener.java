package io.github.nahkd123.voxelity.fabric.net.s2c;

import io.github.nahkd123.voxelity.fabric.net.VoxelityPayload;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloadListener;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

/**
 * <p>
 * Payloads listener on client.
 * </p>
 */
public interface ClientVoxelityPayloadListener extends VoxelityPayloadListener {
	void onCapabilities(CapabilitiesS2CPayload payload);

	static void registerPayloads(PayloadHandlerRegisterer handlerRegisterer) {
		// System payloads
		register(CancelTransactionS2CPayload.ID, CancelTransactionS2CPayload.CODEC, handlerRegisterer);

		// All other payloads
		register(CapabilitiesS2CPayload.ID, CapabilitiesS2CPayload.CODEC, handlerRegisterer);
	}

	private static <T extends VoxelityPayload<ClientVoxelityPayloadListener>> void register(CustomPayload.Id<T> id, PacketCodec<PacketByteBuf, T> codec, PayloadHandlerRegisterer handlerRegisterer) {
		PayloadTypeRegistry.configurationS2C().register(id, codec);
		PayloadTypeRegistry.playS2C().register(id, codec);
		handlerRegisterer.registerHandler(id);
	}

	@FunctionalInterface
	interface PayloadHandlerRegisterer {
		void registerHandler(CustomPayload.Id<? extends VoxelityPayload<ClientVoxelityPayloadListener>> id);
	}
}
