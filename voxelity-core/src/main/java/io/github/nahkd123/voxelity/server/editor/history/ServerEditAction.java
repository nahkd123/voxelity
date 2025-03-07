package io.github.nahkd123.voxelity.server.editor.history;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import io.github.nahkd123.voxelity.math.Vec3i;
import io.github.nahkd123.voxelity.schematic.PackedArraySchematic;
import io.github.nahkd123.voxelity.schematic.Schematic;
import io.github.nahkd123.voxelity.server.VoxelityServer;
import io.github.nahkd123.voxelity.server.world.ServerWorld;
import io.github.nahkd123.voxelity.task.ImmediateTask;
import io.github.nahkd123.voxelity.task.Task;

/**
 * <p>
 * A simple edit action where you replace the cuboid volume in the world with
 * schematic. When performing edit, this will paste the schematic at world
 * origin, which means the edit volume is the bounds of schematics.
 * </p>
 */
public class ServerEditAction extends ServerAction {
	private ServerWorld target;
	private Schematic original;
	private Schematic modified;

	public ServerEditAction(ServerHistoryQueue queue, ServerAction before, String label, ServerWorld target, Schematic original, Schematic modified) {
		super(queue, before, label);
		if (!original.getBounds().equals(modified.getBounds()))
			throw new IllegalArgumentException("Original schematic bounds must match modified schematic");
		this.target = target;
		this.original = original;
		this.modified = modified;
	}

	public ServerWorld getTarget() { return target; }

	public Schematic getOriginal() { return original; }

	public Schematic getModified() { return modified; }

	@Override
	protected Task onUndo() {
		original.pasteTo(getTarget(), Vec3i.ZERO, null);
		return ImmediateTask.FINISHED;
	}

	@Override
	protected Task onRedo() {
		modified.pasteTo(target, Vec3i.ZERO, null);
		return ImmediateTask.FINISHED;
	}

	public static class Serializer implements ServerActionSerializer<ServerEditAction> {
		@Override
		public void serialize(ServerEditAction action, DataOutput stream, VoxelityServer server) throws IOException {
			stream.writeUTF(action.getTarget().getName());
			action.getOriginal().serialize(stream);
			action.getModified().serialize(stream);
		}

		@Override
		public ServerEditAction deserialize(ServerHistoryQueue queue, ServerEditAction before, String label, DataInput stream, VoxelityServer server) throws IOException {
			ServerWorld target = server.getWorld(stream.readUTF());
			PackedArraySchematic original = Schematic.deserialize(stream, server.getPlatform());
			PackedArraySchematic modified = Schematic.deserialize(stream, server.getPlatform());
			return new ServerEditAction(queue, before, label, target, original, modified);
		}
	}
}
