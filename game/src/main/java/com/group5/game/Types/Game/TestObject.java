package com.group5.game.Types.Game;

import com.group5.game.Types.Math.Int2;

/**
 * Object to test the entity system.
 * Does nothing but take up space!
 * Just like me :)
 */
public class TestObject extends Entity {
    public TestObject(Board parentBoard, Int2 startCoordinates, boolean isBlocking) {
        super(parentBoard, startCoordinates, isBlocking);
    }
}
