package io.github.nahkd123.voxelity.fabric.net;

import io.github.nahkd123.voxelity.fabric.net.transactional.TransactionsHandler;

public interface VoxelityPayloadListener {
	TransactionsHandler getTransactionsHandler();
}
