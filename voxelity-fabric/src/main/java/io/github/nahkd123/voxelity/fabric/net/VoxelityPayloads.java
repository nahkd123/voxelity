package io.github.nahkd123.voxelity.fabric.net;

import java.util.Collection;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import io.github.nahkd123.voxelity.fabric.net.c2s.VoxelityC2SFeatureGetPayload;
import io.github.nahkd123.voxelity.fabric.net.c2s.VoxelityC2SFeatureSetPayload;
import io.github.nahkd123.voxelity.fabric.net.s2c.VoxelityS2CEditorStatePayload;
import io.github.nahkd123.voxelity.fabric.net.s2c.VoxelityS2CFeaturePayload;
import io.github.nahkd123.voxelity.fabric.net.s2c.VoxelityS2CNotifyPayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

public class VoxelityPayloads {
	public static void initialize() {
		// @formatter:off
		PayloadTypeRegistry.playS2C().register(VoxelityS2CNotifyPayload.ID, VoxelityS2CNotifyPayload.PACKET_CODEC);
		PayloadTypeRegistry.configurationS2C().register(VoxelityS2CNotifyPayload.ID, VoxelityS2CNotifyPayload.PACKET_CODEC);
		PayloadTypeRegistry.playS2C().register(VoxelityS2CFeaturePayload.ID, VoxelityS2CFeaturePayload.PACKET_CODEC);
		PayloadTypeRegistry.configurationS2C().register(VoxelityS2CFeaturePayload.ID, VoxelityS2CFeaturePayload.PACKET_CODEC);
		PayloadTypeRegistry.playS2C().register(VoxelityS2CEditorStatePayload.ID, VoxelityS2CEditorStatePayload.PACKET_CODEC);
		PayloadTypeRegistry.configurationS2C().register(VoxelityS2CEditorStatePayload.ID, VoxelityS2CEditorStatePayload.PACKET_CODEC);

		PayloadTypeRegistry.playC2S().register(VoxelityC2SFeatureGetPayload.ID, VoxelityC2SFeatureGetPayload.PACKET_CODEC);
		PayloadTypeRegistry.configurationC2S().register(VoxelityC2SFeatureGetPayload.ID, VoxelityC2SFeatureGetPayload.PACKET_CODEC);
		PayloadTypeRegistry.playC2S().register(VoxelityC2SFeatureSetPayload.ID, VoxelityC2SFeatureSetPayload.PACKET_CODEC);
		PayloadTypeRegistry.configurationC2S().register(VoxelityC2SFeatureSetPayload.ID, VoxelityC2SFeatureSetPayload.PACKET_CODEC);
		// @formatter:on
	}

	public static <E extends Enum<E>> PacketCodec<PacketByteBuf, E> createEnumCodec(Collection<E> possibleValues) {
		if (possibleValues.size() == 0) throw new IllegalArgumentException("Must have at least 1 value");
		BiMap<String, E> mapping = HashBiMap.create();
		int maxLength = 0;

		for (E value : possibleValues) {
			String name = value.name();
			maxLength = Math.max(maxLength, name.length());
			mapping.put(name, value);
		}

		// TODO throw error when parsed to null
		return PacketCodecs
			.string(maxLength)
			.xmap(mapping::get, mapping.inverse()::get)
			.cast();
	}
}
