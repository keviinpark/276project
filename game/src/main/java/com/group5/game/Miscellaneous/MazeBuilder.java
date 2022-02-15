package com.group5.game.Miscellaneous;

import java.util.Vector;
import java.util.Random;;

/**
 * Creates a "pseudo-maze" by stamping predefined patterns around at random locations.
 * Returns a grid of 0s and 1s, where 1 represents a wall, 0 represents air.
 */
public class MazeBuilder {
    
    private Vector<int[][]> m_patterns = new Vector<int[][]>();

    /**
     * Add a pattern to be stamped onto a map
     * @param shapes A 2d array where 0s represent air, 1s represent walls
     */
    public void AddPatterns(int[][]... shapes){
        for(var shape : shapes)
            m_patterns.add(shape);
    }

    /**
     * Creates a grid of 0s and 1s with shapes "stamped" onto it
     * @param size The size of the grid diameter
     * @param formationCount The number of shapes to put on randomly
     * @return The grid
     */
    public boolean[][] Create(int size, int formationCount){
        
        Random random = new Random();

        boolean[][] result = new boolean[size][];
        for(int x = 0; x < size; x++)
            result[x] = new boolean[size];

        for(int i = 0; i < formationCount; i++){

            int patternIndex = random.nextInt(m_patterns.size());
            int[][] shape = m_patterns.get(patternIndex);

            int xmax = size - shape.length;
            int ymax = size - shape[0].length;

            int x = random.nextInt(xmax);
            int y = random.nextInt(ymax);

            for(int x0 = 0; x0 < shape.length; x0++)
                for(int y0 = 0; y0 < shape.length; y0++)
                    if(result[x + x0][y + y0] != true)
                        result[x + x0][y + y0] = (shape[x0][y0] == 1);

        }

        return result;
    }
}
