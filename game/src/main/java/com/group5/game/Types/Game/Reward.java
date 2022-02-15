package com.group5.game.Types.Game;

import com.group5.game.Types.Math.*;

/**
 * Entity that will add a positive score effect
 * to the current game's score when touched by the player entity
 * <p>
 * <b>A certain amount of rewards will need to be collected in order to unlock the game's exit</b>
 * @author Harry Nguyen
 */
public class Reward extends DrawableEntity {
    private int m_rewardAmount;

    //constructor
    public Reward(Board parentBoard, Int2 startCoordinates, int rewardAmount) {
        super(parentBoard, startCoordinates, false, "reward.png", new Float2(1.00f, 1.00f));
        m_rewardAmount = Math.abs(rewardAmount);
    }

    @Override
    public void OnEntityWasTouched(Entity other) {
        super.OnEntityWasTouched(other);
        if (other instanceof Player) {
            RemoveEntity();
            ((Player)other).ChangeScore(m_rewardAmount);
            ((Player)other).IncrementRewards();
        }
    }
}   
