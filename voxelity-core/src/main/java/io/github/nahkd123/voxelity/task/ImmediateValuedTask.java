package io.github.nahkd123.voxelity.task;

import java.util.function.Function;

public record ImmediateValuedTask<T>(T result, Throwable rejection) implements ValuedTask<T> {
	public ImmediateValuedTask {
		if (result != null && rejection != null)
			throw new IllegalArgumentException("Result must be null if there is rejection");
		if (result == null && rejection == null)
			throw new IllegalArgumentException("Either result or rejection must be present");
	}

	@Override
	public TaskState getState() { return rejection != null ? TaskState.CANCELED : TaskState.FINISHED; }

	@Override
	public void cancel() {}

	@Override
	public T get() throws IllegalStateException {
		return result();
	}

	@Override
	public Throwable getRejection() throws IllegalStateException {
		if (rejection != null)
			throw new IllegalStateException("State is %s, not %s".formatted(getState(), TaskState.CANCELED));
		return rejection;
	}

	@Override
	public <R> ValuedTask<R> chainValueToValue(Function<T, ValuedTask<R>> callback) {
		if (rejection != null) return new ImmediateValuedTask<>(null, rejection);
		return callback.apply(result);
	}

	@Override
	public Task chainValueToVoid(Function<T, Task> callback) {
		if (rejection != null) return new ImmediateValuedTask<>(null, rejection);
		return callback.apply(result);
	}
}
