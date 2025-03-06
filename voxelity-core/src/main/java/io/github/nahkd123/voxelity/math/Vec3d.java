package io.github.nahkd123.voxelity.math;

public interface Vec3d {
	static Vec3d ZERO = new Vec3dImpl(0, 0, 0);
	static Vec3d X = new Vec3dImpl(1, 0, 0);
	static Vec3d Y = new Vec3dImpl(0, 1, 0);
	static Vec3d Z = new Vec3dImpl(0, 0, 1);

	static Vec3d of(double x, double y, double z) {
		return new Vec3dImpl(x, y, z);
	}

	static Vec3d of(Vec3i v) {
		return of(v.x(), v.y(), v.z());
	}

	double x();

	double y();

	double z();

	default double lengthSqr() {
		return x() * x() + y() * y() + z() * z();
	}

	default double length() {
		return Math.sqrt(lengthSqr());
	}

	default Vec3d add(double x, double y, double z) {
		return of(x() + x, y() + y, z() + z);
	}

	default Vec3d add(Vec3d v) {
		return of(x() + v.x(), y() + v.y(), z() + v.z());
	}

	default Vec3d sub(double x, double y, double z) {
		return of(x() - x, y() - y, z() - z);
	}

	default Vec3d sub(Vec3d v) {
		return of(x() - v.x(), y() - v.y(), z() - v.z());
	}

	default Vec3d mul(double x, double y, double z) {
		return of(x() * x, y() * y, z() * z);
	}

	default Vec3d mul(double scale) {
		return of(x() * scale, y() * scale, z() * scale);
	}

	default Vec3d mul(Vec3d v) {
		return of(x() * v.x(), y() * v.y(), z() * v.z());
	}

	default Vec3d div(double x, double y, double z) {
		return of(x() / x, y() / y, z() / z);
	}

	default Vec3d div(double scale) {
		return of(x() / scale, y() / scale, z() / scale);
	}

	default Vec3d div(Vec3d v) {
		return of(x() / v.x(), y() / v.y(), z() / v.z());
	}

	default Vec3d normalize() {
		return div(length());
	}

	default double dot(double x, double y, double z) {
		return x() * x + y() * y + z() * z;
	}

	default double dot(Vec3d v) {
		return x() * v.x() + y() * v.y() + z() * v.z();
	}

	default Vec3d cross(double x, double y, double z) {
		return of(
			y() * z - z() * y,
			z() * x - x() * z,
			x() * y - y() * x);
	}

	default Vec3d cross(Vec3d v) {
		return of(
			y() * v.z() - z() * v.y(),
			z() * v.x() - x() * v.z(),
			x() * v.y() - y() * v.x());
	}

	default void setToArray(double[] buf, int off) {
		for (int i = off; i < Math.min(off + 3, buf.length); i++) {
			buf[i] = switch (i - off) {
			case 0 -> x();
			case 1 -> y();
			case 2 -> z();
			default -> 0d;
			};
		}
	}
}
