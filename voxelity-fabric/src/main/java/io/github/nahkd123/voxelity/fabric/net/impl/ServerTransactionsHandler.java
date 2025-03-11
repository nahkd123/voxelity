package io.github.nahkd123.voxelity.fabric.net.impl;

import io.github.nahkd123.voxelity.fabric.net.s2c.CancelTransactionS2CPayload;
import io.github.nahkd123.voxelity.fabric.net.transactional.TransactionInitiator;
import io.github.nahkd123.voxelity.fabric.net.transactional.TransactionToken;
import io.github.nahkd123.voxelity.fabric.net.transactional.TransactionsHandler;

class ServerTransactionsHandler extends TransactionsHandler {
	private ServerVoxelityPayloadHandler handler;

	public ServerTransactionsHandler(ServerVoxelityPayloadHandler handler) {
		super(TransactionInitiator.SERVER);
		this.handler = handler;
	}

	@Override
	protected void notifyTransactionCancel(TransactionToken token) {
		handler.send(new CancelTransactionS2CPayload(token));
	}
}
