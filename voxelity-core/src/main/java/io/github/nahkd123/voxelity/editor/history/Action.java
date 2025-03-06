package io.github.nahkd123.voxelity.editor.history;

import io.github.nahkd123.voxelity.task.Task;

public interface Action {
	/**
	 * <p>
	 * Get the display label for this action. This will be displayed in client UI.
	 * </p>
	 * 
	 * @return The label.
	 */
	String getLabel();

	/**
	 * <p>
	 * Get action that was performed before this.
	 * </p>
	 * 
	 * @return Action or {@code null} if there is no actions before this.
	 */
	Action getBefore();

	/**
	 * <p>
	 * Get action that was performed after this.
	 * </p>
	 * 
	 * @return Action or {@code null} if there is no actions after this.
	 */
	Action getAfter();

	/**
	 * <p>
	 * Whether this action has been reverted.
	 * </p>
	 * 
	 * @return Undo state of this action.
	 */
	boolean isReverted();

	/**
	 * <p>
	 * Whether {@link #undo()} or {@link #redo()} can be used for this action.
	 * </p>
	 * 
	 * @return If this action is still presents in {@link HistoryQueue}, returns
	 *         {@code true}.
	 */
	boolean canUndoOrRedo();

	/**
	 * <p>
	 * Revert this action. If there is another action after this one, the next
	 * action will be reverted first before reverting this action.
	 * </p>
	 * 
	 * @return Reverting task.
	 * @throws IllegalStateException if {@link #canUndoOrRedo()} is false.
	 */
	Task undo() throws IllegalStateException;

	/**
	 * <p>
	 * Cancel revert of this action. If there is a task that is undoing this action,
	 * that task will be canceled. If there is another action before this one, the
	 * previous action will be redone first before redoing this action.
	 * 
	 * @return Redoing task.
	 * @throws IllegalStateException if {@link #canUndoOrRedo()} is false.
	 */
	Task redo() throws IllegalStateException;
}
