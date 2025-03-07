package io.github.nahkd123.voxelity.fabric.net.s2c;

import java.util.List;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloads;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

/**
 * <p>
 * Notify client with simple message.
 * </p>
 */
public enum VoxelityS2CNotifyPayload implements CustomPayload {
	/**
	 * <p>
	 * Notify client that new editor session was created. Client receiving this
	 * payload may create client-side session and start sending editor commands.
	 * </p>
	 */
	EDITOR_SESSION_CREATE,
	/**
	 * <p>
	 * Notify client that the current editor session was destroyed. Client must not
	 * send any editor commands after receiving this payload.
	 * </p>
	 */
	EDITOR_SESSION_DESTROY;

	public static final Id<VoxelityS2CNotifyPayload> ID = new Id<>(VoxelityFabric.id("notify"));
	public static final PacketCodec<PacketByteBuf, VoxelityS2CNotifyPayload> PACKET_CODEC = VoxelityPayloads
		.createEnumCodec(List.of(values()))
		.cast();

	@Override
	public Id<VoxelityS2CNotifyPayload> getId() { return ID; }
}
