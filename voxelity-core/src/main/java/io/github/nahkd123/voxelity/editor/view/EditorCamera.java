package io.github.nahkd123.voxelity.editor.view;

import io.github.nahkd123.voxelity.math.Vec3d;

public interface EditorCamera {
	Vec3d getPosition();

	void setPosition(Vec3d position);

	/**
	 * <p>
	 * Get yaw rotation of camera in degrees.
	 * </p>
	 * 
	 * @return The yaw in degrees.
	 */
	double getYaw();

	default void setYaw(double yaw) {
		setRotation(yaw, getPitch());
	}

	/**
	 * <p>
	 * Get pitch rotation of camera in degrees.
	 * </p>
	 * 
	 * @return The pitch in degrees.
	 */
	double getPitch();

	default void setPitch(double pitch) {
		setRotation(getYaw(), pitch);
	}

	void setRotation(double yaw, double pitch);

	default Vec3d getDirection() { return Vec3d.ofRotation(getYaw(), getPitch()); }

	default void setDirection(Vec3d direction) {
		direction = direction.normalize();
		setRotation(direction.getYaw(getYaw()), direction.getPitch());
	}
}
