package io.github.nahkd123.voxelity.fabric.net.transactional;

import io.github.nahkd123.voxelity.fabric.net.VoxelityPayload;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloadListener;

public interface TransactionCancelPayload<L extends VoxelityPayloadListener> extends VoxelityPayload<L> {
	TransactionToken token();

	@Override
	default void apply(L listener) {
		TransactionsHandler handler = listener.getTransactionsHandler();
		handler.destroyContext(token());
	}
}
