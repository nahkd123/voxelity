package io.github.nahkd123.voxelity.fabric.net.c2s;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.net.VoxelityFeature;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public record VoxelityC2SFeatureGetPayload(VoxelityFeature feature) implements CustomPayload {
	public static final Id<VoxelityC2SFeatureGetPayload> ID = new Id<>(VoxelityFabric.id("feature_get"));
	public static final PacketCodec<PacketByteBuf, VoxelityC2SFeatureGetPayload> PACKET_CODEC = VoxelityFeature.PACKET_CODEC
		.xmap(VoxelityC2SFeatureGetPayload::new, VoxelityC2SFeatureGetPayload::feature)
		.cast();

	@Override
	public Id<VoxelityC2SFeatureGetPayload> getId() { return ID; }
}
