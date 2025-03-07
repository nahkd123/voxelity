package io.github.nahkd123.voxelity.fabric.net.c2s;

import java.util.List;

import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloads;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record VoxelityC2SEditRequestNotifyPayload(long requestId, Type type) {
	public static enum Type {
		/**
		 * <p>
		 * Keep the edit request alive on server. After an amount of time without info
		 * from client, the edit request will be destroyed on server to free memory.
		 * </p>
		 */
		KEEP_ALIVE,
		/**
		 * <p>
		 * Confirm the edit.
		 * </p>
		 */
		SUBMIT,
		/**
		 * <p>
		 * Cancel the edit.
		 * </p>
		 */
		CANCEL;

		public static final PacketCodec<ByteBuf, Type> PACKET_CODEC = VoxelityPayloads
			.createEnumCodec(List.of(values()));
	}
}
