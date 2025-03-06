package io.github.nahkd123.voxelity.fabric.registry;

import java.util.Set;

import io.github.nahkd123.voxelity.registry.ReadonlyRegistry;
import io.github.nahkd123.voxelity.registry.RegistryEntry;
import io.github.nahkd123.voxelity.registry.RegistryKey;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RegistryWrapper<S, T extends RegistryEntry<T>> implements ReadonlyRegistry<T> {
	private Registry<S> minecraft;

	public RegistryWrapper(Registry<S> minecraft) {
		this.minecraft = minecraft;
	}

	public Registry<S> getMinecraft() { return minecraft; }

	@SuppressWarnings("unchecked")
	@Override
	public T get(RegistryKey key) {
		S source = minecraft.get((Identifier) (Object) key);
		return (T) source;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RegistryKey getKeyOf(T entry) {
		return (RegistryKey) (Object) minecraft.getId((S) entry);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Set<RegistryKey> getKeys() { return (Set<RegistryKey>) (Set) minecraft.getIds(); }
}
