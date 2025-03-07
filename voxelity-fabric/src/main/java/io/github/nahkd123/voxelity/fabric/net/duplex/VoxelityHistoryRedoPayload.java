package io.github.nahkd123.voxelity.fabric.net.duplex;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record VoxelityHistoryRedoPayload(int position) implements CustomPayload {
	public static final Id<VoxelityHistoryRedoPayload> ID = new Id<>(VoxelityFabric.id("history_redo"));
	public static final PacketCodec<PacketByteBuf, VoxelityHistoryRedoPayload> PACKET_CODEC = PacketCodec
		.tuple(
			PacketCodecs.INTEGER, VoxelityHistoryRedoPayload::position,
			VoxelityHistoryRedoPayload::new)
		.cast();

	@Override
	public Id<VoxelityHistoryRedoPayload> getId() { return ID; }
}
