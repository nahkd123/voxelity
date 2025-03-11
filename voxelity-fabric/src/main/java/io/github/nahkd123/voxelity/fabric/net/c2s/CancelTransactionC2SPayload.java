package io.github.nahkd123.voxelity.fabric.net.c2s;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.net.transactional.TransactionCancelPayload;
import io.github.nahkd123.voxelity.fabric.net.transactional.TransactionToken;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record CancelTransactionC2SPayload(TransactionToken token) implements TransactionCancelPayload<ServerVoxelityPayloadListener> {
	public static final Id<CancelTransactionC2SPayload> ID = new Id<>(VoxelityFabric.id("cancel_transaction"));
	public static final PacketCodec<PacketByteBuf, CancelTransactionC2SPayload> CODEC = TransactionToken.PACKET_CODEC
		.xmap(CancelTransactionC2SPayload::new, CancelTransactionC2SPayload::token)
		.cast();

	@Override
	public Id<CancelTransactionC2SPayload> getId() { return ID; }
}
