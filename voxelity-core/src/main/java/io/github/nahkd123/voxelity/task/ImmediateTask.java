package io.github.nahkd123.voxelity.task;

import java.util.function.Supplier;

public record ImmediateTask(Throwable rejection) implements Task {
	public static final ImmediateTask FINISHED = new ImmediateTask(null);

	@Override
	public TaskState getState() { return rejection != null ? TaskState.CANCELED : TaskState.FINISHED; }

	@Override
	public void cancel() {}

	@Override
	public Throwable getRejection() throws IllegalStateException {
		if (rejection != null)
			throw new IllegalStateException("State is %s, not %s".formatted(getState(), TaskState.CANCELED));
		return rejection;
	}

	@Override
	public <R> ValuedTask<R> chainVoidToValue(Supplier<ValuedTask<R>> callback) {
		if (rejection != null) return new ImmediateValuedTask<>(null, rejection);
		return callback.get();
	}

	@Override
	public Task chainVoidToVoid(Supplier<Task> callback) {
		if (rejection != null) return new ImmediateTask(rejection);
		return ImmediateTask.FINISHED;
	}
}
