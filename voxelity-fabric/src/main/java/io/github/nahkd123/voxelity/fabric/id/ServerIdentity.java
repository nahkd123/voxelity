package io.github.nahkd123.voxelity.fabric.id;

public enum ServerIdentity implements UserIdentity {
	SERVER;

	@Override
	public String getDatabaseKey() { return "server"; }
}
