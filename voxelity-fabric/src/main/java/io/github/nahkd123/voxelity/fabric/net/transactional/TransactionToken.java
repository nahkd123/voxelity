package io.github.nahkd123.voxelity.fabric.net.transactional;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

/**
 * <p>
 * Transaction tokens are used for negotiating between client and server about
 * various functions, like requesting to submit edit (with optional real-time
 * multiplayer preview) for example.
 * </p>
 */
public record TransactionToken(TransactionInitiator initiator, long tokenId) {
	public static final PacketCodec<ByteBuf, TransactionToken> PACKET_CODEC = PacketCodec.tuple(
		TransactionInitiator.PACKET_CODEC, TransactionToken::initiator,
		PacketCodecs.VAR_LONG, TransactionToken::tokenId,
		TransactionToken::new);
}
