package io.github.nahkd123.voxelity.voxel.view;

import java.util.HashMap;
import java.util.Map;

import io.github.nahkd123.voxelity.math.Box3i;
import io.github.nahkd123.voxelity.math.Vec3i;
import io.github.nahkd123.voxelity.voxel.Voxel;

public class VoxelBuffer implements MutableVoxelView {
	private Map<Vec3i, Voxel[]> chunks = new HashMap<>();
	private Box3i bounds = null;

	public Box3i getBounds() { return bounds; }

	private Voxel[] getChunkFromVoxelPos(int x, int y, int z, boolean create) {
		int cx = x >> 4, cy = y >> 4, cz = z >> 4;
		Vec3i key = Vec3i.of(cx, cy, cz);
		if (create) return chunks.computeIfAbsent(key, k -> new Voxel[16 * 16 * 16]);
		return null;
	}

	@Override
	public Voxel get(int x, int y, int z) {
		int chunkIdx = ((y & 0xF) << 8) + ((z & 0xF) << 4) + (x & 0xF);
		Voxel[] chunk = getChunkFromVoxelPos(x, y, z, false);
		return chunk != null ? chunk[chunkIdx] : null;
	}

	@Override
	public void set(int x, int y, int z, Voxel voxel) {
		int chunkIdx = ((y & 0xF) << 8) + ((z & 0xF) << 4) + (x & 0xF);
		Voxel[] chunk = getChunkFromVoxelPos(x, y, z, true);
		chunk[chunkIdx] = voxel;
		bounds = bounds != null ? bounds.expandToIntersect(x, y, z) : new Box3i(x, y, z, 1, 1, 1);
	}
}
