package io.github.nahkd123.voxelity.voxel;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.nahkd123.voxelity.math.Vec3i;

public final class Volume {
	public static final int CHUNK_SIZE = 16 * 16 * 16;
	public static final int SEGMENTS_PER_CHUNK = CHUNK_SIZE / 64;
	private Vec3i translation;
	private Map<Vec3i, long[]> chunks;

	private Volume(Vec3i translation, Map<Vec3i, long[]> chunks) {
		this.translation = translation;
		this.chunks = chunks;
	}

	public Volume(Vec3i translation, Collection<Map.Entry<Vec3i, long[]>> chunks) {
		this.translation = translation;
		this.chunks = new HashMap<>();
		for (Map.Entry<Vec3i, long[]> chunk : chunks) this.chunks.put(chunk.getKey(), chunk.getValue());
	}

	public Volume() {
		this(Vec3i.ZERO, Collections.emptyList());
	}

	public Vec3i getTranslation() { return translation; }

	private long[] getChunkFromVoxelPos(int x, int y, int z, boolean create) {
		int cx = x >> 4, cy = y >> 4, cz = z >> 4;
		Vec3i key = Vec3i.of(cx, cy, cz);
		if (create) return chunks.computeIfAbsent(key, k -> new long[SEGMENTS_PER_CHUNK]);
		return null;
	}

	public Map<Vec3i, long[]> getChunks() { return chunks; }

	public boolean isOccupied(int x, int y, int z) {
		long[] chunk = getChunkFromVoxelPos(x, y, z, false);
		int chunkIdx = ((y & 0xF) << 8) + ((z & 0xF) << 4) + (x & 0xF);
		return chunk != null && (chunk[chunkIdx / 64] & (1L << (chunkIdx % 64))) != 0;
	}

	public void set(int x, int y, int z, boolean state) {
		long[] chunk = getChunkFromVoxelPos(x, y, z, true);
		int chunkIdx = ((y & 0xF) << 8) + ((z & 0xF) << 4) + (x & 0xF);
		int segmentIdx = chunkIdx / 64;
		long mask = 1L << (chunkIdx % 64);
		chunk[segmentIdx] &= ~mask; // clear
		if (state) chunk[segmentIdx] |= mask; // set
	}

	public void serialize(DataOutput stream) throws IOException {
		translation.serialize(stream);
		stream.writeInt(chunks.size());

		for (Map.Entry<Vec3i, long[]> entry : chunks.entrySet()) {
			Vec3i coords = entry.getKey();
			long[] chunk = entry.getValue();
			coords.serialize(stream);
			for (int i = 0; i < SEGMENTS_PER_CHUNK; i++) stream.writeLong(chunk[i]);
		}
	}

	public static Volume deserialize(DataInput stream) throws IOException {
		Vec3i translation = Vec3i.deserialize(stream);
		int chunksCount = stream.readInt();
		List<Map.Entry<Vec3i, long[]>> chunks = new ArrayList<>();

		for (int i = 0; i < chunksCount; i++) {
			Vec3i coords = Vec3i.deserialize(stream);
			long[] chunk = new long[SEGMENTS_PER_CHUNK];
			for (int j = 0; j < SEGMENTS_PER_CHUNK; j++) chunk[j] = stream.readLong();
			chunks.add(Map.entry(coords, chunk));
		}

		return new Volume(translation, chunks);
	}

	public Volume translate(int dx, int dy, int dz) {
		return new Volume(translation.add(dx, dy, dz), chunks);
	}
}
