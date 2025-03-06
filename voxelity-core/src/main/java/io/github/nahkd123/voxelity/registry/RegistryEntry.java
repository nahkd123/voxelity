package io.github.nahkd123.voxelity.registry;

public interface RegistryEntry<T extends RegistryEntry<T>> {
	ReadonlyRegistry<T> getRegistry();

	@SuppressWarnings("unchecked")
	default RegistryKey getKey() { return getRegistry().getKeyOf((T) this); }
}
