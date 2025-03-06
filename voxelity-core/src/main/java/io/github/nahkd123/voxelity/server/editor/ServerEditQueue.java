package io.github.nahkd123.voxelity.server.editor;

import io.github.nahkd123.voxelity.editor.EditQueue;
import io.github.nahkd123.voxelity.editor.history.Action;
import io.github.nahkd123.voxelity.math.Box3i;
import io.github.nahkd123.voxelity.math.Vec3i;
import io.github.nahkd123.voxelity.schematic.PackedArraySchematic;
import io.github.nahkd123.voxelity.schematic.Schematic;
import io.github.nahkd123.voxelity.server.editor.history.ServerEditAction;
import io.github.nahkd123.voxelity.server.world.ServerWorld;
import io.github.nahkd123.voxelity.task.ImmediateTask;
import io.github.nahkd123.voxelity.task.ImmediateValuedTask;
import io.github.nahkd123.voxelity.task.Task;
import io.github.nahkd123.voxelity.task.ValuedTask;
import io.github.nahkd123.voxelity.voxel.Voxel;
import io.github.nahkd123.voxelity.world.VoxelBuffer;

public class ServerEditQueue implements EditQueue {
	private ServerVoxelityEditor editor;
	private ServerWorld target;
	private boolean destroyed = false;
	private VoxelBuffer buffer = new VoxelBuffer();

	public ServerEditQueue(ServerVoxelityEditor editor, ServerWorld target) {
		this.editor = editor;
		this.target = target;
	}

	@Override
	public ServerWorld getTarget() { return target; }

	@Override
	public ValuedTask<Action> submit() {
		if (destroyed) return new ImmediateValuedTask<>(null, new IllegalStateException("Queue already destroyed"));
		Box3i bounds = getBounds();
		Vec3i min = bounds.min();
		Vec3i max = bounds.max();
		PackedArraySchematic original = Schematic.copyFrom(getTarget(), bounds, Vec3i.ZERO);

		for (int y = min.y(); y <= max.y(); y++) {
			for (int z = min.z(); z <= max.z(); z++) {
				for (int x = min.x(); x <= max.x(); x++) {
					Voxel voxel = get(x, y, z);
					if (voxel != null) getTarget().set(x, y, z, voxel);
				}
			}
		}

		PackedArraySchematic modified = Schematic.copyFrom(getTarget(), bounds, Vec3i.ZERO);
		String label = "Modify %d blocks at %s".formatted(
			modified.getSize().x() * modified.getSize().y() * modified.getSize().z(),
			modified.getBoundsMin());
		ServerEditAction action = editor.getHistory()
			.enqueue((queue, before) -> new ServerEditAction(queue, before, label, getTarget(), original, modified));
		destroy();
		return new ImmediateValuedTask<>(action, null);
	}

	@Override
	public Task cancel() {
		if (destroyed) return new ImmediateTask(new IllegalStateException("Queue already destroyed"));
		destroyed = true;
		destroy();
		return ImmediateTask.FINISHED;
	}

	private void destroy() {
		if (editor.currentEditQueue == this) editor.currentEditQueue = null;
		buffer = null;
	}

	@Override
	public boolean isOpen() { return !destroyed; }

	@Override
	public Voxel get(int x, int y, int z) {
		return buffer.get(x, y, z);
	}

	@Override
	public void set(int x, int y, int z, Voxel voxel) {
		buffer.set(x, y, z, voxel);
	}

	@Override
	public Box3i getBounds() { return buffer.getBounds(); }

}
