package io.github.nahkd123.voxelity.fabric.net.c2s;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloads;
import io.github.nahkd123.voxelity.math.Box3i;
import io.github.nahkd123.voxelity.schematic.Schematic;
import io.github.nahkd123.voxelity.voxel.Volume;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

public record VoxelityC2SEditSyncPartialPayload(long requestId, Box3i bounds, Volume mask, Schematic partial) implements CustomPayload {

	public static final Id<VoxelityC2SEditSyncPartialPayload> ID = new Id<>(VoxelityFabric.id("edit_sync_partial"));
	public static final PacketCodec<PacketByteBuf, VoxelityC2SEditSyncPartialPayload> PACKET_CODEC = PacketCodec
		.tuple(
			PacketCodecs.VAR_LONG, VoxelityC2SEditSyncPartialPayload::requestId,
			VoxelityPayloads.BOX3I, VoxelityC2SEditSyncPartialPayload::bounds,
			VoxelityPayloads.VOLUME, VoxelityC2SEditSyncPartialPayload::mask,
			VoxelityPayloads.SCHEMATIC, VoxelityC2SEditSyncPartialPayload::partial,
			VoxelityC2SEditSyncPartialPayload::new)
		.cast();

	@Override
	public Id<VoxelityC2SEditSyncPartialPayload> getId() { return ID; }
}
