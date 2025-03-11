package io.github.nahkd123.voxelity.fabric.net.transactional;

import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloadListener;

public interface TransactionInitPayload<L extends VoxelityPayloadListener, T> extends TransactionalPayload<L, T> {
	@Override
	default T getContext(TransactionsHandler handler) {
		T context = getNewContext();
		handler.initContext(token(), context);
		return context;
	}

	T getNewContext();
}
