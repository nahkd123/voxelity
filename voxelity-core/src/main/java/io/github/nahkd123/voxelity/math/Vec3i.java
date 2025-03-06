package io.github.nahkd123.voxelity.math;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Vec3i {
	static Vec3i ZERO = new Vec3iImpl(0, 0, 0);
	static Vec3i X = new Vec3iImpl(1, 0, 0);
	static Vec3i Y = new Vec3iImpl(0, 1, 0);
	static Vec3i Z = new Vec3iImpl(0, 0, 1);

	static Vec3i of(int x, int y, int z) {
		return new Vec3iImpl(x, y, z);
	}

	static Vec3i castFrom(Vec3d v) {
		return new Vec3iImpl((int) v.x(), (int) v.y(), (int) v.z());
	}

	int x();

	int y();

	int z();

	default Vec3i add(int x, int y, int z) {
		return of(x() + x, y() + y, z() + z);
	}

	default Vec3i add(Vec3i v) {
		return of(x() + v.x(), y() + v.y(), z() + v.z());
	}

	default Vec3i sub(int x, int y, int z) {
		return of(x() - x, y() - y, z() - z);
	}

	default Vec3i sub(Vec3i v) {
		return of(x() - v.x(), y() - v.y(), z() - v.z());
	}

	default Vec3i mul(int x, int y, int z) {
		return of(x() * x, y() * y, z() * z);
	}

	default Vec3i mul(Vec3i v) {
		return of(x() * v.x(), y() * v.y(), z() * v.z());
	}

	default Vec3i divFloor(int x, int y, int z) {
		return of(x() / x, y() / y, z() / z);
	}

	default Vec3i divFloor(Vec3i v) {
		return of(x() / v.x(), y() / v.y(), z() / v.z());
	}

	default int lengthSqr() {
		return x() * x() + y() * y() + z() * z();
	}

	default double length() {
		return Math.sqrt(lengthSqr());
	}

	default void serialize(DataOutput stream) throws IOException {
		stream.writeInt(x());
		stream.writeInt(y());
		stream.writeInt(z());
	}

	static Vec3i deserialize(DataInput stream) throws IOException {
		int x = stream.readInt();
		int y = stream.readInt();
		int z = stream.readInt();
		return of(x, y, z);
	}
}
