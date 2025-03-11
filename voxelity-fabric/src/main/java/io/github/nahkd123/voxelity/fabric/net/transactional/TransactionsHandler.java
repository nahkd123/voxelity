package io.github.nahkd123.voxelity.fabric.net.transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * Handle transaction contexts and tokens generation.
 * </p>
 */
public abstract class TransactionsHandler {
	private Map<TransactionToken, Object> contexts = new HashMap<>();
	private TransactionInitiator side;
	private long generator = 0;

	public TransactionsHandler(TransactionInitiator side) {
		this.side = side;
	}

	private TransactionToken generateToken() {
		long id = generator++;
		TransactionToken token = new TransactionToken(side, id);
		return token;
	}

	@SuppressWarnings("unchecked")
	public <T> T getContext(TransactionToken token) {
		T context = (T) contexts.get(token);
		if (context == null) throw new IllegalArgumentException("Token is not initialized");
		return context;
	}

	public <T> void initContext(TransactionToken token, T context) {
		contexts.put(token, context);
	}

	public <T> TransactionToken initNewContext(T context) {
		TransactionToken token = generateToken();
		contexts.put(token, context);
		return token;
	}

	public void destroyContext(TransactionToken token) {
		contexts.remove(token);
	}

	/**
	 * <p>
	 * Get a collection of transaction tokens. You can use this with
	 * {@link #getContext(TransactionToken)} to obtain the transaction context.
	 * </p>
	 * 
	 * @return A collection of transaction tokens.
	 */
	public Set<TransactionToken> getTokens() { return contexts.keySet(); }

	protected abstract void notifyTransactionCancel(TransactionToken token);

	/**
	 * <p>
	 * Cancel a transaction. This will notify the handler to send
	 * {@link TransactionCancelPayload} to peer and destroy the context.
	 * </p>
	 * 
	 * @param token The transaction token to cancel.
	 */
	public void cancelTransaction(TransactionToken token) {
		Object context = contexts.get(token);
		if (context == null) return;
		notifyTransactionCancel(token);
		destroyContext(token);
	}
}
