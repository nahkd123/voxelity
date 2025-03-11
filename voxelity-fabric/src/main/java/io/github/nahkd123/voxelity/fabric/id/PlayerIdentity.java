package io.github.nahkd123.voxelity.fabric.id;

import java.util.UUID;

public record PlayerIdentity(UUID uuid) implements UserIdentity {
	@Override
	public String getDatabaseKey() { return uuid.toString().toLowerCase(); }
}
