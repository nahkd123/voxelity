package io.github.nahkd123.voxelity.server.editor.history;

import io.github.nahkd123.voxelity.editor.history.Action;
import io.github.nahkd123.voxelity.task.ImmediateTask;
import io.github.nahkd123.voxelity.task.Task;

/**
 * <p>
 * <b>Retaining history</b>: History may be retained in server memory, or it may
 * be retained in storage when user log out of server. In order to retain in
 * storage, the action must be serializable. If the action is not serializable,
 * the history queue might be destroyed upon logging out.
 * </p>
 */
public abstract class ServerAction implements Action {
	private ServerHistoryQueue queue;
	private ServerAction before;
	private ServerAction after;
	private String label;
	private boolean reverted;

	public ServerAction(ServerHistoryQueue queue, ServerAction before, ServerAction after, String label, boolean reverted) {
		this.queue = queue;
		this.before = before;
		this.after = after;
		this.label = label;
		this.reverted = reverted;
	}

	public ServerAction(ServerHistoryQueue queue, ServerAction before, String label) {
		this(queue, before, null, label, false);
	}

	protected abstract Task onUndo();

	protected abstract Task onRedo();

	@Override
	public String getLabel() { return label; }

	@Override
	public ServerAction getBefore() { return before; }

	public void setBefore(ServerAction before) { this.before = before; }

	@Override
	public ServerAction getAfter() { return after; }

	public void setAfter(ServerAction after) { this.after = after; }

	@Override
	public boolean isReverted() { return reverted; }

	public ServerHistoryQueue getQueue() { return queue; }

	public void setQueue(ServerHistoryQueue queue) { this.queue = queue; }

	@Override
	public boolean canUndoOrRedo() {
		return queue != null;
	}

	@Override
	public Task undo() throws IllegalStateException {
		if (queue == null) throw new IllegalStateException("Queue has been cleared");
		if (reverted) return ImmediateTask.FINISHED;
		return after.undo().chainVoidToVoid(this::onUndo);
	}

	@Override
	public Task redo() throws IllegalStateException {
		if (queue == null) throw new IllegalStateException("Queue has been cleared");
		if (!reverted) return ImmediateTask.FINISHED;
		return before.redo().chainVoidToVoid(this::onRedo);
	}
}
