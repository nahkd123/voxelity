package io.github.nahkd123.voxelity.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import io.github.nahkd123.voxelity.fabric.hook.permission.PermissionManager;
import net.minecraft.server.PlayerManager;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
	@Inject(method = "removeFromOperators", at = @At("TAIL"))
	private void voxelity$removeFromOperators(GameProfile profile, CallbackInfo ci) {
		PermissionManager.recalculatePermissions((PlayerManager) (Object) this, profile);
	}
}
