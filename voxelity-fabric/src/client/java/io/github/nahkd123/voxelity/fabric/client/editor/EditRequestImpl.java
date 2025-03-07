package io.github.nahkd123.voxelity.fabric.client.editor;

import io.github.nahkd123.voxelity.client.editor.EditRequest;
import io.github.nahkd123.voxelity.editor.history.Action;
import io.github.nahkd123.voxelity.fabric.world.WorldReference;
import io.github.nahkd123.voxelity.math.Box3i;
import io.github.nahkd123.voxelity.math.Vec3i;
import io.github.nahkd123.voxelity.schematic.PackedArraySchematic;
import io.github.nahkd123.voxelity.schematic.Schematic;
import io.github.nahkd123.voxelity.task.ImmediateValuedTask;
import io.github.nahkd123.voxelity.task.ValuedTask;
import io.github.nahkd123.voxelity.voxel.Volume;
import io.github.nahkd123.voxelity.voxel.Voxel;
import io.github.nahkd123.voxelity.voxel.view.VoxelBuffer;

public class EditRequestImpl implements EditRequest {
	// TODO grab new requestId from somewhere
	// options:
	// 1. both server and client agree on sequential ID protocol (inc 1 on every
	// full sync)
	// 2. client request new ID from server
	private long requestId = -1;
	private FabricClientVoxelityEditor editor;
	private WorldReference target;
	private boolean opened = true;

	// Entire edit layer
	private VoxelBuffer editLayer = new VoxelBuffer();

	// Partial, waiting to sync with server
	private Box3i syncRegion = null;
	private Volume syncMask = null;

	public EditRequestImpl(FabricClientVoxelityEditor editor, WorldReference target) {
		this.editor = editor;
		this.target = target;
	}

	@Override
	public void set(int x, int y, int z, Voxel voxel) {
		editLayer.set(x, y, z, voxel);
		syncRegion = syncRegion != null ? syncRegion.expandToIntersect(x, y, z) : new Box3i(x, y, z, 1, 1, 1);
		if (syncMask == null) syncMask = new Volume();
		syncMask.set(x, y, z, true);
	}

	@Override
	public Voxel get(int x, int y, int z) {
		return editLayer.get(x, y, z);
	}

	@Override
	public WorldReference getTarget() { return target; }

	@Override
	public void sync() {
		ensureOpened();
		if (requestId == -1L) syncAll();
		else syncPartial();
	}

	private void syncAll() {
		PackedArraySchematic schematic = Schematic.copyFrom(editLayer, editLayer.getBounds(), Vec3i.ZERO, null);
		// TODO send schematic to server
	}

	private void syncPartial() {
		if (syncMask == null || syncRegion == null) return;
		PackedArraySchematic partial = Schematic.copyFrom(editLayer, syncRegion, Vec3i.ZERO, syncMask);
		// TODO send partial, syncMask and syncRegion to server

		syncMask = null;
		syncRegion = null;
	}

	public void handleServerSyncResponse(Schematic fixed, Volume affectedMask) {
		fixed.pasteTo(editLayer, Vec3i.ZERO, null);
		if (syncMask == null) return;
		// TODO clear syncMask from affectedMask
	}

	private void ensureOpened() {
		if (!opened) throw new IllegalStateException("Request is closed");
	}

	@Override
	public boolean isOpened() { return opened; }

	@Override
	public ValuedTask<Action> submit() {
		if (!opened) return new ImmediateValuedTask<>(null, new IllegalStateException("Request is closed"));
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancel() {
		if (!opened) return;
		// TODO Auto-generated method stub
		opened = false;
	}
}
