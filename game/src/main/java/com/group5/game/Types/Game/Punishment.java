package com.group5.game.Types.Game;

import com.group5.game.Types.Math.*;

/**
 * Entity that will add a negative score effect
 * to the current game's score when touched by the player entity
 * @author Harry Nguyen
 */
public class Punishment extends DrawableEntity {
    private int m_punishmentAmount;

    //constructor
    public Punishment (Board parentBoard, Int2 startCoordinates, int punishmentAmount) {
        super(parentBoard, startCoordinates, false, "punishment.png", new Float2(1f, 1f));
        
        //Ensure always positive
        m_punishmentAmount = Math.abs(punishmentAmount);
    }

    @Override
    public void OnEntityWasTouched(Entity other) {
        super.OnEntityWasTouched(other);
        if (other instanceof Player) {
            RemoveEntity();
            ((Player)other).ChangeScore(m_punishmentAmount * -1);
        }
    }
}
