package io.github.nahkd123.voxelity.fabric.client.editor.view;

import io.github.nahkd123.voxelity.editor.view.EditorCamera;
import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.math.Vec3d;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

public class ClientEditorCamera implements EditorCamera {
	private MinecraftClient client;

	public ClientEditorCamera(MinecraftClient client) {
		this.client = client;
	}

	@Override
	public Vec3d getPosition() {
		net.minecraft.util.math.Vec3d mc = client.gameRenderer.getCamera().getPos();
		return Vec3d.of(mc.x, mc.y, mc.z);
	}

	@Override
	public void setPosition(Vec3d position) {
		if (!client.player.isSpectator()) {
			if (FabricLoader.getInstance().isDevelopmentEnvironment())
				VoxelityFabric.LOGGER.warn("Camera movement only available in spectator mode");
			return;
		}

		client.player.requestTeleport(position.x(), position.y(), position.z());
	}

	@Override
	public double getYaw() { return client.player.getYaw(); }

	@Override
	public double getPitch() { return client.player.getPitch(); }

	@Override
	public void setRotation(double yaw, double pitch) {
		client.player.rotate((float) yaw, (float) pitch);
	}
}
