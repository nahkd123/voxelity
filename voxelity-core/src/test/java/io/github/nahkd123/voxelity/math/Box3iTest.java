package io.github.nahkd123.voxelity.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

class Box3iTest {
	@Test
	void testSplitIntoChunks() {
		assertEquals(Box3i.fromCorners(-7, -7, -7, 23, 36, 23).splitIntoChunks(),
			Set.of(
				Box3i.fromCorners(-7, -7, -7, -1, -1, -1),
				Box3i.fromCorners(-7, -7, 0, -1, -1, 15),
				Box3i.fromCorners(-7, -7, 16, -1, -1, 23),
				Box3i.fromCorners(-7, 0, -7, -1, 15, -1),
				Box3i.fromCorners(-7, 0, 0, -1, 15, 15),
				Box3i.fromCorners(-7, 0, 16, -1, 15, 23),
				Box3i.fromCorners(-7, 16, -7, -1, 31, -1),
				Box3i.fromCorners(-7, 16, 0, -1, 31, 15),
				Box3i.fromCorners(-7, 16, 16, -1, 31, 23),
				Box3i.fromCorners(-7, 32, -7, -1, 36, -1),
				Box3i.fromCorners(-7, 32, 0, -1, 36, 15),
				Box3i.fromCorners(-7, 32, 16, -1, 36, 23),

				Box3i.fromCorners(0, -7, -7, 15, -1, -1),
				Box3i.fromCorners(0, -7, 0, 15, -1, 15),
				Box3i.fromCorners(0, -7, 16, 15, -1, 23),
				Box3i.fromCorners(0, 0, -7, 15, 15, -1),
				Box3i.fromCorners(0, 0, 0, 15, 15, 15),
				Box3i.fromCorners(0, 0, 16, 15, 15, 23),
				Box3i.fromCorners(0, 16, -7, 15, 31, -1),
				Box3i.fromCorners(0, 16, 0, 15, 31, 15),
				Box3i.fromCorners(0, 16, 16, 15, 31, 23),
				Box3i.fromCorners(0, 32, -7, 15, 36, -1),
				Box3i.fromCorners(0, 32, 0, 15, 36, 15),
				Box3i.fromCorners(0, 32, 16, 15, 36, 23),

				Box3i.fromCorners(16, -7, -7, 23, -1, -1),
				Box3i.fromCorners(16, -7, 0, 23, -1, 15),
				Box3i.fromCorners(16, -7, 16, 23, -1, 23),
				Box3i.fromCorners(16, 0, -7, 23, 15, -1),
				Box3i.fromCorners(16, 0, 0, 23, 15, 15),
				Box3i.fromCorners(16, 0, 16, 23, 15, 23),
				Box3i.fromCorners(16, 16, -7, 23, 31, -1),
				Box3i.fromCorners(16, 16, 0, 23, 31, 15),
				Box3i.fromCorners(16, 16, 16, 23, 31, 23),
				Box3i.fromCorners(16, 32, -7, 23, 36, -1),
				Box3i.fromCorners(16, 32, 0, 23, 36, 15),
				Box3i.fromCorners(16, 32, 16, 23, 36, 23)));
	}
}
