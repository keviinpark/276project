package com.group5.game.Miscellaneous;

/**
 * A collection of 4x4 patterns to randomly place on a map
 */
public class MazeShapes {
    //Maze shapes
	public static int[][][] List = new int[][][]{
		//l1
		new int[][]{
			new int[]{ 1, 0, 0, 0},
			new int[]{ 1, 0, 0, 0},
			new int[]{ 1, 0, 0, 0},
			new int[]{ 1, 0, 0, 0}
		},
		//l2
		new int[][]{
			new int[]{ 0, 0, 0, 0},
			new int[]{ 0, 0, 0, 0},
			new int[]{ 0, 0, 0, 0},
			new int[]{ 1, 1, 1, 1}
		},
		//l3
		new int[][]{
			new int[]{ 1, 1, 1, 1},
			new int[]{ 0, 0, 0, 0},
			new int[]{ 0, 0, 0, 0},
			new int[]{ 0, 0, 0, 0}
		},
		//l4
		new int[][]{
			new int[]{ 0, 0, 0, 1},
			new int[]{ 0, 0, 0, 1},
			new int[]{ 0, 0, 0, 1},
			new int[]{ 0, 0, 0, 1}
		},
		//L1
		new int[][]{
			new int[]{ 1, 0, 0, 0},
			new int[]{ 1, 0, 0, 0},
			new int[]{ 1, 0, 0, 0},
			new int[]{ 1, 1, 1, 1}
		},
		//L2
		new int[][]{
			new int[]{ 0, 0, 0, 1},
			new int[]{ 0, 0, 0, 1},
			new int[]{ 0, 0, 0, 1},
			new int[]{ 1, 1, 1, 1}
		},
		//L3
		new int[][]{
			new int[]{ 1, 1, 1, 1},
			new int[]{ 0, 0, 0, 1},
			new int[]{ 0, 0, 0, 1},
			new int[]{ 0, 0, 0, 1}
		},
		//L4
		new int[][]{
			new int[]{ 1, 0, 0, 0},
			new int[]{ 1, 0, 0, 0},
			new int[]{ 1, 0, 0, 0},
			new int[]{ 1, 1, 1, 1}
		}
	};
}
