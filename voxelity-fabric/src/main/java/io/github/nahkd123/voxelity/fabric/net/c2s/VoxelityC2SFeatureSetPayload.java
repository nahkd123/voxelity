package io.github.nahkd123.voxelity.fabric.net.c2s;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.net.VoxelityFeature;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record VoxelityC2SFeatureSetPayload(VoxelityFeature feature, boolean enable) implements CustomPayload {
	public static final Id<VoxelityC2SFeatureSetPayload> ID = new Id<>(VoxelityFabric.id("feature_set"));
	public static final PacketCodec<PacketByteBuf, VoxelityC2SFeatureSetPayload> PACKET_CODEC = PacketCodec
		.tuple(
			VoxelityFeature.PACKET_CODEC, VoxelityC2SFeatureSetPayload::feature,
			PacketCodecs.BOOLEAN, VoxelityC2SFeatureSetPayload::enable,
			VoxelityC2SFeatureSetPayload::new)
		.cast();

	@Override
	public Id<VoxelityC2SFeatureSetPayload> getId() { return ID; }
}
