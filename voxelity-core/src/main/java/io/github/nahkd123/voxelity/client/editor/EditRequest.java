package io.github.nahkd123.voxelity.client.editor;

import io.github.nahkd123.voxelity.editor.history.Action;
import io.github.nahkd123.voxelity.task.ValuedTask;
import io.github.nahkd123.voxelity.voxel.view.MutableVoxelView;
import io.github.nahkd123.voxelity.world.World;

/*
 * Handling time out: In case the edit queue is timed out on server,
 * client will have to send entire edits again. This happens during
 * sync(). The main reason for timing out is to avoid excessive memory
 * usage on server, just in case client abandoned the queue without
 * informing server.
 */
/**
 * <p>
 * <b>About voxel view</b>: {@link EditRequest} voxel view acts like "edit
 * layer" rather than a combination of edit layer + world view. This is because
 * client may request an edit in world that is not client's current world, so it
 * is impossible for client to read target world without teleporting to target
 * world or writing complex protocol for downloading part of that world.
 * </p>
 */
public interface EditRequest extends MutableVoxelView {
	World getTarget();

	/**
	 * <p>
	 * Sync edits with server. Server may modify user's edits in case the permission
	 * manager (something like region management system) denied edits in some or
	 * entire updated volume. If there are modifications from server, client will
	 * receive such modifications when sync.
	 * </p>
	 */
	void sync();

	/**
	 * <p>
	 * Check whether this edit request is still open. Closed edit requests cannot be
	 * submitted.
	 * </p>
	 * 
	 * @return Open state.
	 */
	boolean isOpened();

	/**
	 * <p>
	 * Submit this edit and close this edit request.
	 * </p>
	 * 
	 * @return The task that will resolves into {@link Action}, which can be undo or
	 *         push to history UI.
	 */
	ValuedTask<Action> submit();

	/**
	 * <p>
	 * Cancel this edit and close this edit request. This will signal the server to
	 * also destroy the pending edits, thus free out memory.
	 * </p>
	 * <p>
	 * In case the request is abandoned, server will automatically destroy pending
	 * edit after certain timeout.
	 * </p>
	 */
	void cancel();
}
