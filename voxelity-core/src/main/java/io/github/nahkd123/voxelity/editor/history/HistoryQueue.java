package io.github.nahkd123.voxelity.editor.history;

import java.util.function.Predicate;

import io.github.nahkd123.voxelity.task.Task;

/**
 * <p>
 * Contains a growing sequence of {@link Action}. Depending on connected server,
 * the history queue may or may not have a cap on number of actions.
 * </p>
 * <p>
 * Server may retain user's history queue. When rejoining the server, user may
 * receive history that was previously retained.
 * </p>
 */
public interface HistoryQueue {
	/**
	 * <p>
	 * Get the number of {@link Action} in this queue, regardless its undo state.
	 * </p>
	 * 
	 * @return Number of {@link Action}.
	 */
	int count();

	Action get(int index);

	/**
	 * <p>
	 * Get the last index of action that meet the predicate.
	 * </p>
	 * 
	 * @param predicate The predicate to test.
	 * @return The index of last action matches with predicate, or {@code -1} if
	 *         there is no such action.
	 */
	default int getLastIndex(Predicate<Action> predicate) {
		for (int i = count() - 1; i >= 0; i--) {
			Action action = get(i);
			if (predicate.test(action)) return i;
		}

		return -1;
	}

	/**
	 * <p>
	 * Clear this history queue. Once the queue is cleared, you can no longer undo
	 * or redo, even if you have a reference to {@link Action}. This is because
	 * calling this method will also ask the server to clear the queue.
	 * </p>
	 */
	Task clear();
}
