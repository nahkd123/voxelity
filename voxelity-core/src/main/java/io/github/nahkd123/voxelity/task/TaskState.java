package io.github.nahkd123.voxelity.task;

public enum TaskState {
	/**
	 * <p>
	 * The task is waiting to be processed.
	 * </p>
	 */
	WAITING,
	/**
	 * <p>
	 * The task is being processed.
	 * </p>
	 */
	PROCESSING,
	/**
	 * <p>
	 * The task is canceled by any means (requested by self or rejected by peer).
	 * </p>
	 */
	CANCELED,
	/**
	 * <p>
	 * The task is finished.
	 * </p>
	 */
	FINISHED;
}
