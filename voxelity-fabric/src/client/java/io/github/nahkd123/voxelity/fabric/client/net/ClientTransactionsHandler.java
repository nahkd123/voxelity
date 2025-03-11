package io.github.nahkd123.voxelity.fabric.client.net;

import io.github.nahkd123.voxelity.fabric.net.c2s.CancelTransactionC2SPayload;
import io.github.nahkd123.voxelity.fabric.net.transactional.TransactionInitiator;
import io.github.nahkd123.voxelity.fabric.net.transactional.TransactionToken;
import io.github.nahkd123.voxelity.fabric.net.transactional.TransactionsHandler;

class ClientTransactionsHandler extends TransactionsHandler {
	private ClientVoxelityPayloadHandler handler;

	public ClientTransactionsHandler(ClientVoxelityPayloadHandler handler) {
		super(TransactionInitiator.CLIENT);
		this.handler = handler;
	}

	@Override
	protected void notifyTransactionCancel(TransactionToken token) {
		handler.send(new CancelTransactionC2SPayload(token));
	}
}
