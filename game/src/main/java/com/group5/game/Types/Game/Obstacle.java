package com.group5.game.Types.Game;

import com.group5.game.Types.Math.Float2;
import com.group5.game.Types.Math.Int2;

public class Obstacle extends DrawableEntity {

    public Obstacle(Board board, Int2 coordinate){
        super(board, coordinate, true, "obstacle.png", new Float2(1,1));
    }
}
