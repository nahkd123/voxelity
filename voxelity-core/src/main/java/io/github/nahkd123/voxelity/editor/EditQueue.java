package io.github.nahkd123.voxelity.editor;

import io.github.nahkd123.voxelity.editor.history.Action;
import io.github.nahkd123.voxelity.math.Box3i;
import io.github.nahkd123.voxelity.task.Task;
import io.github.nahkd123.voxelity.task.ValuedTask;
import io.github.nahkd123.voxelity.voxel.Voxel;
import io.github.nahkd123.voxelity.voxel.view.MutableVoxelView;
import io.github.nahkd123.voxelity.world.World;

/**
 * <p>
 * Represent a queue of edit commands. This extends {@link MutableVoxelView}
 * interface, but it only contains the changes to apply on target world, rather
 * than a combination of target world and edit layer.
 * </p>
 * <p>
 * Note that all edit queues will be automatically canceled when user's
 * permission is revoked.
 * </p>
 */
public interface EditQueue extends MutableVoxelView {
	World getTarget();

	/**
	 * <p>
	 * Submit the edits queued in this queue. Once you call this method, no other
	 * edit commands can be queued to this queue.
	 * </p>
	 * 
	 * @return The task that will perform the edits and return {@link Action}, which
	 *         can be undo.
	 */
	ValuedTask<Action> submit();

	/**
	 * <p>
	 * Discard the edits queued in this queue. Once you call this method, no other
	 * edit commands can be queued to this queue. If live preview is enabled on
	 * server, this will also discard the preview on server, which is why the return
	 * value is a task.
	 * </p>
	 * 
	 * @return The task for canceling edit.
	 */
	Task cancel();

	/**
	 * <p>
	 * Whether this queue is still opened. If the queue is closed, you cannot submit
	 * queue commands.
	 * </p>
	 * 
	 * @return The open state of this queue.
	 */
	boolean isOpen();

	/**
	 * <p>
	 * Get edit at specified coordinates. The returned value will be {@code null} if
	 * there is no changes to apply at given coordinates.
	 * </p>
	 */
	@Override
	Voxel get(int x, int y, int z);

	/**
	 * <p>
	 * Set edit at specified coordinates.
	 * </p>
	 * 
	 * @param x     X coordinate.
	 * @param y     Y coordinate.
	 * @param z     Z coordinate.
	 * @param voxel The edit to voxel at specified coordinates. Use {@code null} to
	 *              mark the voxel as unchanged.
	 */
	@Override
	void set(int x, int y, int z, Voxel voxel);

	/**
	 * <p>
	 * Get the current editing bounds. The bounds is controlled by subsequence calls
	 * to {@link #set(int, int, int, Voxel)}, which expands the bounding box when
	 * you set a voxel outside it. If you have not made any calls to
	 * {@link #set(int, int, int, Voxel)} yet, this method returns {@code null}.
	 * </p>
	 * <p>
	 * Editing bounds are used for storing edits to history file.
	 * </p>
	 * 
	 * @return The bounds or {@code null}.
	 */
	Box3i getBounds();

	default void fillCuboid(Box3i box, Voxel voxel) {
		for (int y = 0; y < box.sizeY(); y++) {
			for (int z = 0; z < box.sizeZ(); z++) {
				for (int x = 0; x < box.sizeX(); x++) set(box.x() + x, box.y() + y, box.z() + z, voxel);
			}
		}
	}
}
