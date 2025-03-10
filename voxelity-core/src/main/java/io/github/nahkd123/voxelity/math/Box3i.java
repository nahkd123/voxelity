package io.github.nahkd123.voxelity.math;

import java.util.HashSet;
import java.util.Set;

public record Box3i(int x, int y, int z, int sizeX, int sizeY, int sizeZ) {
	public Box3i {
		if (sizeX * sizeY * sizeZ == 0) throw new IllegalArgumentException("Volume must have at least 1 voxel");
		if (sizeX < 0) throw new IllegalArgumentException("sizeX < 0");
		if (sizeY < 0) throw new IllegalArgumentException("sizeY < 0");
		if (sizeZ < 0) throw new IllegalArgumentException("sizeZ < 0");
	}

	public static Box3i fromCorners(int x1, int y1, int z1, int x2, int y2, int z2) {
		int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
		int minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
		int minZ = Math.min(z1, z2), maxZ = Math.max(z1, z2);
		return new Box3i(minX, minY, minZ, maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1);
	}

	public boolean intersect(int x, int y, int z) {
		return x >= x() && x < x() + sizeX
			&& y >= y() && y < y() + sizeY
			&& z >= z() && z < z() + sizeZ;
	}

	public Box3i expandToIntersect(int x, int y, int z) {
		if (intersect(x, y, z)) return this;
		int minX = Math.min(x(), x), maxX = Math.max(x() + sizeX - 1, x);
		int minY = Math.min(y(), y), maxY = Math.max(y() + sizeY - 1, y);
		int minZ = Math.min(z(), z), maxZ = Math.max(z() + sizeZ - 1, z);
		return new Box3i(minX, minY, minZ, maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1);
	}

	public Vec3i min() {
		return Vec3i.of(x, y, z);
	}

	public Vec3i max() {
		return Vec3i.of(x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1);
	}

	public Vec3i size() {
		return Vec3i.of(sizeX, sizeY, sizeZ);
	}

	public int volume() {
		return sizeX * sizeY * sizeZ;
	}

	/**
	 * <p>
	 * Split this bounding box into multiple bounding boxes with chunk alignment (as
	 * in at most 16x16x16). The main purpose of this is to split big schematic into
	 * multiple fragments so that they all fit inside packet limit of 32767.
	 * </p>
	 * 
	 * @return A set of boxes that are split into chunks from this bounding box.
	 */
	public Set<Box3i> splitIntoChunks() {
		// lc: Low chunk coords (min)
		// hc: High chunk coords (max)
		int lcx = x >> 4, lcy = y >> 4, lcz = z >> 4;
		int hcx = ((x + sizeX) >> 4) + 1, hcy = ((y + sizeY) >> 4) + 1, hcz = ((z + sizeZ) >> 4) + 1;
		Set<Box3i> set = new HashSet<>(); // TODO: Create Box3i[] first for efficient data structure

		for (int cy = lcy; cy <= hcy; cy++) {
			for (int cz = lcz; cz <= hcz; cz++) {
				for (int cx = lcx; cx <= hcx; cx++) {
					int cwx = cx << 4, cwy = cy << 4, cwz = cz << 4;
					int wx = Math.min(Math.max(x, cwx), cwx + 16); // Min
					int wy = Math.min(Math.max(y, cwy), cwy + 16);
					int wz = Math.min(Math.max(z, cwz), cwz + 16);
					int mwx = Math.max(Math.min(x + sizeX, cwx + 16), cwx); // Max
					int mwy = Math.max(Math.min(y + sizeY, cwy + 16), cwy);
					int mwz = Math.max(Math.min(z + sizeZ, cwz + 16), cwz);
					int sx = mwx - wx, sy = mwy - wy, sz = mwz - wz;
					if (sx * sy * sz == 0) continue; // Volume is zero
					set.add(new Box3i(wx, wy, wz, sx, sy, sz));
				}
			}
		}

		return set;
	}
}
