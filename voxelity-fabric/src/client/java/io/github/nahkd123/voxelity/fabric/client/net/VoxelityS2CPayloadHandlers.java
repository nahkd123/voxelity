package io.github.nahkd123.voxelity.fabric.client.net;

import io.github.nahkd123.voxelity.fabric.VoxelityFabric;
import io.github.nahkd123.voxelity.fabric.client.bridge.MinecraftClientBridge;
import io.github.nahkd123.voxelity.fabric.client.editor.FabricClientVoxelityEditor;
import io.github.nahkd123.voxelity.fabric.net.VoxelityFeature;
import io.github.nahkd123.voxelity.fabric.net.s2c.VoxelityS2CEditorStatePayload;
import io.github.nahkd123.voxelity.fabric.net.s2c.VoxelityS2CFeaturePayload;
import io.github.nahkd123.voxelity.fabric.net.s2c.VoxelityS2CNotifyPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class VoxelityS2CPayloadHandlers {
	public static void initialize() {
		// @formatter:off
		ClientConfigurationNetworking.registerGlobalReceiver(VoxelityS2CFeaturePayload.ID, (payload, context) -> handleFeature(payload.feature(), payload.enabled(), context.client()));
		ClientPlayNetworking.registerGlobalReceiver(VoxelityS2CFeaturePayload.ID, (payload, context) -> handleFeature(payload.feature(), payload.enabled(), context.client()));

		ClientConfigurationNetworking.registerGlobalReceiver(VoxelityS2CNotifyPayload.ID, (payload, context) -> handleNotify(payload, context.client()));
		ClientPlayNetworking.registerGlobalReceiver(VoxelityS2CNotifyPayload.ID, (payload, context) -> handleNotify(payload, context.client()));

		ClientConfigurationNetworking.registerGlobalReceiver(VoxelityS2CEditorStatePayload.ID, (payload, context) -> handleEditorState(payload, context.client()));
		ClientPlayNetworking.registerGlobalReceiver(VoxelityS2CEditorStatePayload.ID, (payload, context) -> handleEditorState(payload, context.client()));
		// @formatter:on
	}

	public static void handleFeature(VoxelityFeature feature, boolean enabled, MinecraftClient client) {
		((MinecraftClientBridge) client).voxelity$setFeature(feature, enabled);
		Text message = enabled ? feature.getEnabledMessage() : feature.getDisabledMessage();
		client.inGameHud.setOverlayMessage(message, false);
	}

	public static void handleNotify(VoxelityS2CNotifyPayload payload, MinecraftClient client) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment())
			VoxelityFabric.LOGGER.info("Notified by server: {}", payload);
	}

	public static void handleEditorState(VoxelityS2CEditorStatePayload state, MinecraftClient client) {
		FabricClientVoxelityEditor editor = ((MinecraftClientBridge) client).voxelity$getEditor();

		if (editor == null) {
			VoxelityFabric.LOGGER.warn("Server tried to update editor state while editor is not initialized");
			return;
		}

		editor.updateState(state);
	}
}
