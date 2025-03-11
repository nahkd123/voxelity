package io.github.nahkd123.voxelity.fabric.net.s2c;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.net.transactional.TransactionCancelPayload;
import io.github.nahkd123.voxelity.fabric.net.transactional.TransactionToken;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

public record CancelTransactionS2CPayload(TransactionToken token) implements TransactionCancelPayload<ClientVoxelityPayloadListener> {
	public static final Id<CancelTransactionS2CPayload> ID = new Id<>(VoxelityFabric.id("cancel_transaction"));
	public static final PacketCodec<PacketByteBuf, CancelTransactionS2CPayload> CODEC = TransactionToken.PACKET_CODEC
		.xmap(CancelTransactionS2CPayload::new, CancelTransactionS2CPayload::token)
		.cast();

	@Override
	public Id<CancelTransactionS2CPayload> getId() { return ID; }
}
