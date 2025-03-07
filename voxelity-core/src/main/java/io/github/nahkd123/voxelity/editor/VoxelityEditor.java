package io.github.nahkd123.voxelity.editor;

import java.util.Set;

import io.github.nahkd123.voxelity.editor.history.HistoryQueue;
import io.github.nahkd123.voxelity.world.World;

/**
 * <p>
 * Represent an editor that user can interact to submit edits. An editor
 * instance will be created when requested by user and will be destroyed when
 * user logout of server. Editor instance must be retained even when user
 * changed world.
 * </p>
 */
public interface VoxelityEditor {
	HistoryQueue getHistory();

	/**
	 * <p>
	 * Get reference to all worlds on the server. Use the reference on methods that
	 * involves editing the world, like {@link #createEditQueue(World)} for example.
	 * </p>
	 * 
	 * @return A collection of world references.
	 */
	Set<? extends World> getWorlds();

	/**
	 * <p>
	 * Create a new edit queue that targets the world you want to edit. Edit queue
	 * only contains the editing information. In order to combine both edit info and
	 * underlying world, the user need to have its editor view inside target world,
	 * then combine it by checking whether the voxel inside edit queue is
	 * {@code null} (replace {@code null} with voxel coming from target world).
	 * </p>
	 * <p>
	 * Upon creating new edit queue on client, it will also send request to create
	 * queue on server. There can only be at most 1 edit queue. You need to either
	 * submit or cancel the existing queue in order to create new one.
	 * </p>
	 * 
	 * @param target The target world for editing.
	 * @return The edit queue.
	 * @see #getCurrentEditQueue()
	 */
	EditQueue createEditQueue(World target);

	/**
	 * <p>
	 * Get current edit queue, or {@code null} if there is no queue created.
	 * </p>
	 * 
	 * @return The current edit queue.
	 */
	EditQueue getCurrentEditQueue();
}
