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

	static Vec3d ofRotation(double yaw, double pitch) {
		double y = -Math.sin(pitch);
		double xz = Math.cos(pitch);
		double x = -xz * Math.sin(yaw);
		double z = xz * Math.cos(yaw);
		return of(x, y, z);
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
		if (lengthSqr() == 1d) return this;
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

	default double getYaw(double oldYaw) {
		double x = x(), z = z();
		if (x == 0d && z == 0d) return oldYaw;
		double fullCircle = Math.PI * 2;
		return (Math.atan2(-x, z) + fullCircle) % fullCircle;
	}

	default double getYawOrZero() { return getYaw(0d); }

	default double getPitch() {
		double x = x(), y = y(), z = z();
		if (x == 0d && z == 0d) return y > 0 ? -Math.PI / 2 : Math.PI / 2;
		double x2 = x * x;
		double z2 = z * z;
		double xz = Math.sqrt(x2 + z2);
		return Math.atan(-y / xz);
	}
}
