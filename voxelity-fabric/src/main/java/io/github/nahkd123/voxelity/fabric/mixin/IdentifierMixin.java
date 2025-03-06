package io.github.nahkd123.voxelity.fabric.mixin;

import org.spongepowered.asm.mixin.Mixin;

import io.github.nahkd123.voxelity.registry.RegistryKey;
import net.minecraft.util.Identifier;

@Mixin(Identifier.class)
public abstract class IdentifierMixin implements RegistryKey {
	@Override
	public String namespace() {
		return ((Identifier) (Object) this).getNamespace();
	}

	@Override
	public String id() {
		return ((Identifier) (Object) this).getPath();
	}
}
