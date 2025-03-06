package io.github.nahkd123.voxelity.task;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ValuedTask<T> extends Task {
	/**
	 * <p>
	 * Get the value immediately, or throw if value is not available.
	 * </p>
	 * 
	 * @return The return value if this task is finished.
	 * @throws IllegalStateException if {@link #getState()} is not
	 *                               {@link TaskState#FINISHED}.
	 */
	T get() throws IllegalStateException;

	Task chainValueToVoid(Function<T, Task> callback);

	@Override
	default Task chainVoidToVoid(Supplier<Task> callback) {
		return chainValueToVoid(result -> callback.get());
	}

	default Task thenValueToVoid(Consumer<T> callback) {
		return chainValueToVoid(result -> {
			try {
				callback.accept(result);
				return ImmediateTask.FINISHED;
			} catch (Throwable t) {
				return new ImmediateTask(t);
			}
		});
	}

	<R> ValuedTask<R> chainValueToValue(Function<T, ValuedTask<R>> callback);

	@Override
	default <R> ValuedTask<R> chainVoidToValue(Supplier<ValuedTask<R>> callback) {
		return chainValueToValue(result -> callback.get());
	}

	default <R> ValuedTask<R> thenValueToValue(Function<T, R> callback) {
		return chainValueToValue(result -> {
			try {
				return new ImmediateValuedTask<>(callback.apply(result), null);
			} catch (Throwable t) {
				return new ImmediateValuedTask<>(null, t);
			}
		});
	}
}
