package io.github.nahkd123.voxelity.world;

import java.util.HashMap;
import java.util.Map;

import io.github.nahkd123.voxelity.math.Box3i;
import io.github.nahkd123.voxelity.voxel.Voxel;

public class VoxelBuffer implements MutableVoxelView {
	private Map<ChunkPos, Voxel[]> chunks = new HashMap<>();
	private Box3i bounds = null;

	private record ChunkPos(int x, int y, int z) {
	}

	public Box3i getBounds() { return bounds; }

	private Voxel[] getChunkFromVoxelPos(int x, int y, int z, boolean create) {
		int cx = x >> 4, cy = y >> 4, cz = z >> 4;
		ChunkPos key = new ChunkPos(cx, cy, cz);
		if (create) return chunks.computeIfAbsent(key, k -> new Voxel[16 * 16 * 16]);
		return null;
	}

	@Override
	public Voxel get(int x, int y, int z) {
		int lx = x & 0xF, ly = y & 0xF, lz = z & 0xF;
		Voxel[] chunk = getChunkFromVoxelPos(x, y, z, false);
		return chunk != null ? chunk[ly * 16 * 16 + lz * 16 + lx] : null;
	}

	@Override
	public void set(int x, int y, int z, Voxel voxel) {
		int lx = x & 0xF, ly = y & 0xF, lz = z & 0xF;
		Voxel[] chunk = getChunkFromVoxelPos(x, y, z, true);
		chunk[ly * 16 * 16 + lz * 16 + lx] = voxel;
		bounds = bounds != null ? bounds.expandToIntersect(x, y, z) : new Box3i(x, y, z, 1, 1, 1);
	}
}
