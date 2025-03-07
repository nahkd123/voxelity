package io.github.nahkd123.voxelity.fabric.net.duplex;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record VoxelityHistoryUndoPayload(int position) implements CustomPayload {
	public static final Id<VoxelityHistoryUndoPayload> ID = new Id<>(VoxelityFabric.id("history_undo"));
	public static final PacketCodec<PacketByteBuf, VoxelityHistoryUndoPayload> PACKET_CODEC = PacketCodec
		.tuple(
			PacketCodecs.INTEGER, VoxelityHistoryUndoPayload::position,
			VoxelityHistoryUndoPayload::new)
		.cast();

	@Override
	public Id<VoxelityHistoryUndoPayload> getId() { return ID; }
}
