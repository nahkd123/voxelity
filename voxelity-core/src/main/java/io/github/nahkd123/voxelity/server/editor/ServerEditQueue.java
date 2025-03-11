package io.github.nahkd123.voxelity.server.editor;

import io.github.nahkd123.voxelity.editor.EditQueue;
import io.github.nahkd123.voxelity.editor.history.Action;
import io.github.nahkd123.voxelity.math.Box3i;
import io.github.nahkd123.voxelity.server.world.ServerWorld;
import io.github.nahkd123.voxelity.task.ImmediateTask;
import io.github.nahkd123.voxelity.task.Task;
import io.github.nahkd123.voxelity.task.ValuedTask;
import io.github.nahkd123.voxelity.voxel.Voxel;

public class ServerEditQueue implements EditQueue {
	private boolean opened = true;
	private ServerWorld target;
	private Box3i bounds = null;

	public ServerEditQueue(ServerWorld target) {
		this.target = target;
	}

	@Override
	public ServerWorld getTarget() { return target; }

	@Override
	public ValuedTask<Action> submit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task cancel() {
		if (!opened) return ImmediateTask.FINISHED;
		return null;
	}

	@Override
	public boolean isOpen() { return opened; }

	@Override
	public Voxel get(int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set(int x, int y, int z, Voxel voxel) {
		// TODO Auto-generated method stub
	}

	@Override
	public Box3i getBounds() { return bounds; }
}
