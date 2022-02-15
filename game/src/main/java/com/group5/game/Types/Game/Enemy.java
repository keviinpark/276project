package com.group5.game.Types.Game;

import com.group5.game.Types.Math.*;
import com.group5.game.Miscellaneous.AI;

import java.util.LinkedList;

public class Enemy extends DrawableEntity {

    private String m_texName;
    private String m_texNameCharged;

    private Player m_target;
    private int m_ticksPerMove;
    private int m_ticksSinceMove;

    public Enemy(Board parentBoard, Int2 startCoordinates, Player target, int ticksPerMove, String texName, String texNameCharged) {
        super(parentBoard, startCoordinates, true, texName, new Float2(1f, 1f));
        m_target = target;
        m_ticksPerMove = ticksPerMove;

        m_texName = texName;
        m_texNameCharged = texNameCharged;
    }

    @Override
    public void OnTick(){
        super.OnTick();

        //See if should move yet; if not, return
        m_ticksSinceMove++;

        //Set visual depending on movement state
        SetTexturePath(m_ticksSinceMove != m_ticksPerMove - 1 ? m_texName : m_texNameCharged);

        if(m_ticksSinceMove < m_ticksPerMove) return;
        else m_ticksSinceMove = 0;

        //Get position parameters
        Int2 target = m_target.GetCoordinates();
        Int2 start = GetCoordinates();

        //Create a matrix for passable positions
        Int2 dimensions = this.GetParentBoard().GetBoardDimensions();
        boolean[][] passageMatrix = new boolean[dimensions.x][dimensions.y];
        
        for(int x = 0; x < dimensions.x; x++)
            for(int y = 0; y < dimensions.y; y++)
            {
                //Set to passable if it is the player's coordinate, this enemy's coordinate,
                //or if it is otherwise passable
                Int2 coord = new Int2(x, y);
                if(coord == target || coord == start){
                    passageMatrix[x][y] = true;
                }
                passageMatrix[x][y] = this.GetParentBoard().SpaceAvailable(coord);
            }

        //Try to find a path to the target and move to the first step in the path (path[1], as path[0] is start)
        LinkedList<Int2> path = AI.Pathfind(passageMatrix, start, target);
        if(path != null && path.size() > 1){
            SetCoordinates(path.get(1));
        }
    }

    @Override
    public void OnEntityTouch(Entity other) {
        super.OnEntityTouch(other);
    }
}
