package io.github.nahkd123.voxelity.fabric.net.c2s;

import java.util.List;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloads;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

/**
 * <p>
 * Notify server with simple message.
 * </p>
 */
public enum VoxelityC2SNotifyPayload implements CustomPayload {
	HISTORY_REFRESH;

	public static final Id<VoxelityC2SNotifyPayload> ID = new Id<>(VoxelityFabric.id("notify"));
	public static final PacketCodec<PacketByteBuf, VoxelityC2SNotifyPayload> PACKET_CODEC = VoxelityPayloads
		.createEnumCodec(List.of(values()));

	@Override
	public Id<VoxelityC2SNotifyPayload> getId() { return ID; }
}
