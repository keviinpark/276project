package com.group5.game.Miscellaneous;

import com.group5.game.Types.Game.Board;
import com.group5.game.Types.Game.Punishment;
import com.group5.game.Types.Math.Int2;

import java.util.LinkedList;
import java.util.Queue;


public class AI {

    //offset to find the neighbors of a particular node
    //1.Directly above
    //2.Directly below
    //3.Directly to the right
    //4.Directly to the left
    static Int2[] adjacencies = new Int2[]{ new Int2(0, 1), new Int2(0, -1), new Int2(1, 0), new Int2(-1, 0) };
    
    /**
     * An algorithm to find a path between two coordinates, which automatically converts a target board 
     * to a matrix to be used by the algorithm.
     * @param board The board to navigate on
     * @param source The coordinates of the cell to navigate from
     * @param destination The coordinates of the cell to navigate to
     * @param punishmentAsObstacle Whether or not punishments are considered obstacles
     * @return A sequence of positions if successful, or null if no path was found
     */
    public static LinkedList<Int2> Pathfind(Board board, Int2 source, Int2 destination, boolean punishmentAsObstacle){
        Int2 dims = board.GetBoardDimensions();
        boolean[][] passageMatrix = new boolean[dims.x][];
        for(int x = 0; x < dims.x; x++){
            passageMatrix[x] = new boolean[dims.y];
            for(int y = 0; y < dims.y; y++){
                passageMatrix[x][y] = board.SpaceAvailable(new Int2(x,y));
                if(punishmentAsObstacle){
                    for(var ent : board.GetEntitiesAt(new Int2(x,y)))
                        if(ent instanceof Punishment){
                            passageMatrix[x][y] = false;
                            break;
                        }
                }
            }
        }
        return Pathfind(passageMatrix, source, destination);
    }

    /**
     * An algorithm to find a path between two coordinates.
     * @param passageMatrix A two-dimensional array of booleans respresenting a grid of spaces. 
     * A true value in a cell means no obsatlce is present, while a false value means an obstacle 
     * is present.
     * @param source The coordinates of the cell to navigate from
     * @param destination The coordinates of the cell to navigate to
     * @return A sequence of positions if successful, or null if no path was found
     */
    public static LinkedList<Int2> Pathfind(boolean[][] passageMatrix, Int2 source, Int2 destination){
        
        //System.out.println("Finding from " + source + " to " + destination);

        //create a matrix of false values that turn to true after that grid position has been visited
        boolean [][]visited = new boolean[passageMatrix.length][passageMatrix[0].length];

        //Create open set, add initial position as first element
        Queue<LinkedList<Int2>> openSet = new LinkedList<LinkedList<Int2>>();
        LinkedList<Int2> start = new LinkedList<Int2>();
        start.add(source);        
        openSet.add(start);

        LinkedList<Int2> currentChain = null;
        while (openSet.size() > 0){
            //Get first element from open set
            currentChain = openSet.remove();

            //Get last step of path
            Int2 lastStep = currentChain.getLast();

            //If the chain's end is at the target, return the chain of steps
            if(lastStep.x == destination.x && lastStep.y == destination.y) {
                return currentChain;
            }
            //Still work to do; check adjacent nodes
            else if(visited[lastStep.x][lastStep.y] == false) {
                for(int i = 0; i < 4; i++) {
                    //Get the above, below, right or left grid position (depending on i)
                    Int2 adjacent = Int2.Plus(currentChain.getLast(), adjacencies[i]);
    
                    //x within board range
                    if(adjacent.x >= 0 && adjacent.x < passageMatrix.length){
                        //y within board range
                        if(adjacent.y >= 0 && adjacent.y < passageMatrix[0].length){
                            //passable
                            if(passageMatrix[adjacent.x][adjacent.y] == true){
                                //add new step in sequence to open set
                                LinkedList<Int2> newChain = new LinkedList<Int2>(currentChain);
                                newChain.add(adjacent);
                                openSet.add(newChain);
                            }
                        }
                    }
                }
            }
            //mark visited
            visited[lastStep.x][lastStep.y] = true;
        }
        return null;
    }
}

