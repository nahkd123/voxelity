package io.github.nahkd123.voxelity.fabric.net.transactional;

import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloadListener;

public interface TransactionNextPayload<L extends VoxelityPayloadListener, T> extends TransactionalPayload<L, T> {
	@Override
	default T getContext(TransactionsHandler handler) {
		return handler.getContext(token());
	}
}
