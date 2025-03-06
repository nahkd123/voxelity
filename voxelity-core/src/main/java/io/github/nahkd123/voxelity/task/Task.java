package io.github.nahkd123.voxelity.task;

import java.util.function.Supplier;

public interface Task {
	TaskState getState();

	/**
	 * <p>
	 * Cancel this task. The recipient will be notified that this task has been
	 * canceled by sender.
	 * </p>
	 */
	void cancel();

	/**
	 * <p>
	 * Get the reason why this task is canceled.
	 * </p>
	 * 
	 * @return The reject reason.
	 * @throws IllegalStateException if {@link #getState()} is not
	 *                               {@link TaskState#CANCELED}.
	 */
	Throwable getRejection() throws IllegalStateException;

	Task chainVoidToVoid(Supplier<Task> callback);

	default Task thenVoidToVoid(Runnable callback) {
		return chainVoidToVoid(() -> {
			try {
				callback.run();
				return ImmediateTask.FINISHED;
			} catch (Throwable t) {
				return new ImmediateTask(t);
			}
		});
	}

	<R> ValuedTask<R> chainVoidToValue(Supplier<ValuedTask<R>> callback);

	default <R> ValuedTask<R> thenVoidToValue(Supplier<R> callback) {
		return chainVoidToValue(() -> {
			try {
				return new ImmediateValuedTask<>(callback.get(), null);
			} catch (Throwable t) {
				return new ImmediateValuedTask<>(null, t);
			}
		});
	}

	default void throwIfCanceled() throws Throwable {
		if (getState() == TaskState.CANCELED) throw getRejection();
	}
}
