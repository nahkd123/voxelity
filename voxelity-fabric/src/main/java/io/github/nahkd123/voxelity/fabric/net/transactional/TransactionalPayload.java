package io.github.nahkd123.voxelity.fabric.net.transactional;

import io.github.nahkd123.voxelity.fabric.net.VoxelityPayload;
import io.github.nahkd123.voxelity.fabric.net.VoxelityPayloadListener;

/**
 * <p>
 * Transactions are used to differentiate between multiple objects. In the case
 * of Voxelity, transactions are used to handle edit requests independently.
 * That means each client can open (basically) unlimited number of edit requests
 * and keep them alive.
 * </p>
 * 
 * @param <L> The type of listener, usually payload handler.
 * @param <T> The type of transaction context. The context will be created by
 *            {@link TransactionInitPayload}.
 */
public interface TransactionalPayload<L extends VoxelityPayloadListener, T> extends VoxelityPayload<L> {
	TransactionToken token();

	@Override
	default void apply(L listener) {
		TransactionsHandler handler = listener.getTransactionsHandler();
		T context = getContext(handler);
		apply(listener, context);
	}

	T getContext(TransactionsHandler handler);

	void apply(L listener, T context);
}
