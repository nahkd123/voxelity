package io.github.nahkd123.voxelity.math;

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
}
