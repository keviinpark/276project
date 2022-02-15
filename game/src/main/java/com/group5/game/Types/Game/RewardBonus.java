package com.group5.game.Types.Game;

import com.group5.game.Types.Math.*;

// couldn't extend from reward because otherwise wouldn't be able to change bonus reward image to diffientiate it from regular reward
// but if you find a way to do that, then let me know!!! - Harry

/**
 * Entity that will add a positive score effect, greater than that of regular rewards,
 * to the current game's score when touched by the player entity
 * <p>
 *  <b>NOTE</b>: Bonus rewards do not go toward the reward amount required to unlock the exit
 * @author Harry Nguyen
 */
public class RewardBonus extends DrawableEntity {
    private int m_bonusRewardAmount; // this value should be larger than that of regular rewards

    // constructor
    public RewardBonus(Board parentBoard, Int2 startCoordinates, int scoreAmount) {
        super(parentBoard, startCoordinates, false, "reward-bonus.png", new Float2(1f, 1f));
        m_bonusRewardAmount = Math.abs(scoreAmount);
   }

    @Override
    public void OnEntityWasTouched(Entity other) {
        super.OnEntityWasTouched(other);
        if (other instanceof Player) {
            RemoveEntity();
            ((Player)other).ChangeScore(m_bonusRewardAmount);
        }
    }
}
