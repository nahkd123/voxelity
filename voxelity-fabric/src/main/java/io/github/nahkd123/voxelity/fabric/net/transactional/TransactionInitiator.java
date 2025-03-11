package io.github.nahkd123.voxelity.fabric.net.transactional;

import java.util.List;

import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloads;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;

public enum TransactionInitiator {
	/**
	 * <p>
	 * The transaction was initiated by client. The token ID is controlled by
	 * client.
	 * </p>
	 */
	CLIENT,
	/**
	 * <p>
	 * The transaction was initiated by server. The token ID is controlled by
	 * server.
	 * </p>
	 */
	SERVER;

	public static final PacketCodec<ByteBuf, TransactionInitiator> PACKET_CODEC = VoxelityPayloads
		.createEnumCodec(List.of(
			CLIENT,
			SERVER));
}
