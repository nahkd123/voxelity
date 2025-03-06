package io.github.nahkd123.voxelity.voxel;

import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * An immutable representation of voxel. This can be used as value in various
 * places. Implementation of this {@link Voxel} must also override
 * {@link #hashCode()} to return the same value for voxel with same type and set
 * of property values.
 * </p>
 */
public interface Voxel {
	VoxelType getType();

	/**
	 * <p>
	 * Get the property value of this voxel.
	 * </p>
	 * 
	 * @param <T>      The type of value.
	 * @param property The property to get.
	 * @return The property value.
	 * @throws IllegalArgumentException if property type is {@code null} or not
	 *                                  included in {@link #getType()}.
	 */
	<T extends Comparable<T>> T get(VoxelProperty<T> property) throws IllegalArgumentException;

	/**
	 * <p>
	 * Create new immutable {@link Voxel} with different property value for given
	 * property type.
	 * </p>
	 * 
	 * @param <T>      The type of value.
	 * @param property The property type.
	 * @param value    The property value.
	 * @return A new immutable {@link Voxel}.
	 * @throws IllegalArgumentException if property type is {@code null} or not
	 *                                  included in {@link #getType()}; if value is
	 *                                  not included in property type.
	 */
	<T extends Comparable<T>> Voxel with(VoxelProperty<T> property, T value) throws IllegalArgumentException;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	default Map<VoxelProperty<?>, ?> getProperties(boolean includeDefaults) {
		Map<VoxelProperty<?>, ?> out = new HashMap<>();

		for (VoxelProperty<?> property : getType().getProperties()) {
			Object value = get(property);

			if (!includeDefaults) {
				Object def = getType().getDefault(property);
				if (value == def || value.equals(def)) continue;
			}

			((Map) out).put(property, value);
		}

		return out;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	default void serialize(DataOutput stream) throws IOException {
		getType().getKey().serialize(stream);
		Map<VoxelProperty<?>, ?> properties = getProperties(false);
		stream.writeInt(properties.size());

		for (Map.Entry<VoxelProperty<?>, ?> entry : properties.entrySet()) {
			VoxelProperty<?> property = entry.getKey();
			Object value = entry.getValue();
			int index = ((VoxelProperty) property).indexOf((Comparable) value);
			stream.writeUTF(property.getTypeName());
			stream.writeInt(index);
		}
	}
}
