package io.github.nahkd123.voxelity.utils.packed;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PackedLongArrayTest {
	@Test
	void test() {
		for (int bitsPerElement = 1; bitsPerElement < 32; bitsPerElement++) {
			PackedLongArray array = new PackedLongArray(bitsPerElement, new long[1024]);
			for (int i = 0; i < array.size(); i++) array.set(i, i % array.getPossibleValues());
			for (int i = 0; i < array.size(); i++) assertEquals(i % array.getPossibleValues(), array.get(i));
		}
	}
}
