package io.github.nahkd123.voxelity.server.editor.history;

/**
 * <p>
 * Factory for creating new {@link ServerAction}.
 * </p>
 */
@FunctionalInterface
public interface ServerActionEnqueueFactory<T extends ServerAction> {
	T createAction(ServerHistoryQueue queue, ServerAction before);
}
