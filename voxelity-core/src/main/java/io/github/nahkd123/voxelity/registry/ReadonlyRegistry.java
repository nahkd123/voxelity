package io.github.nahkd123.voxelity.registry;

import java.util.Set;

public interface ReadonlyRegistry<T extends RegistryEntry<T>> {
	T get(RegistryKey key);

	RegistryKey getKeyOf(T entry);

	Set<RegistryKey> getKeys();
}
