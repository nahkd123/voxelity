package io.github.nahkd123.voxelity.fabric.net.c2s;

import java.util.List;

import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloads;
import io.github.nahkd123.voxelity.fabric.permission.Capability;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;

public enum ClientStatus {
	/**
	 * <p>
	 * Notify the editor is opened on client. Ignores if client doesn't have
	 * {@link Capability#EDITOR} permissions.
	 * </p>
	 */
	EDITOR_OPEN,
	/**
	 * <p>
	 * Notify the editor is closed on client. Ignores if client doesn't have
	 * {@link Capability#EDITOR} permissions.
	 * </p>
	 */
	EDITOR_CLOSE;

	public static final PacketCodec<ByteBuf, ClientStatus> PACKET_CODEC = VoxelityPayloads.createEnumCodec(List.of(
		EDITOR_OPEN,
		EDITOR_CLOSE));
}
