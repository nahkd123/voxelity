package io.github.nahkd123.voxelity.fabric.net.s2c;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.net.VoxelityFeature;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record VoxelityS2CFeaturePayload(VoxelityFeature feature, boolean enabled) implements CustomPayload {
	public static final Id<VoxelityS2CFeaturePayload> ID = new Id<>(VoxelityFabric.id("feature"));
	public static final PacketCodec<PacketByteBuf, VoxelityS2CFeaturePayload> PACKET_CODEC = PacketCodec
		.tuple(
			VoxelityFeature.PACKET_CODEC, VoxelityS2CFeaturePayload::feature,
			PacketCodecs.BOOLEAN, VoxelityS2CFeaturePayload::enabled,
			VoxelityS2CFeaturePayload::new)
		.cast();

	@Override
	public Id<VoxelityS2CFeaturePayload> getId() { return ID; }
}
