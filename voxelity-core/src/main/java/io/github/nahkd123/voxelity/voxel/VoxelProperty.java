package io.github.nahkd123.voxelity.voxel;

public interface VoxelProperty<T extends Comparable<T>> {
	/**
	 * <p>
	 * Get the serializable name of this property type.
	 * </p>
	 * 
	 * @return The property type name.
	 */
	String getTypeName();

	int getValuesCount();

	T get(int index);

	int indexOf(T value);

	default boolean has(T value) {
		for (int i = 0; i < getValuesCount(); i++) {
			T val = get(i);
			if (val == value || val.equals(value)) return true;
		}

		return false;
	}
}
