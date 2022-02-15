package com.group5.game.Types.Game;

import com.group5.game.Types.Math.Float2;
import com.group5.game.Types.Math.Int2;

public class Gate extends DrawableEntity {

    private Player m_player;
    private int m_rewardsRequired;

    public Gate(Board parentBoard, Int2 startCoordinates, Player player, int rewardsRequired) {
        super(parentBoard, startCoordinates, true, "gate.png", new Float2(1,1));
        m_player = player;
        m_rewardsRequired = rewardsRequired;
    }
    
    @Override 
    public String GetTexturePath() {
        if(m_player.GetRewardsCollected() < m_rewardsRequired)
            return "gate.png";
        else return "exit.png";
    }

    @Override
    public void OnTick()
    {
        if(m_player.GetRewardsCollected() < m_rewardsRequired)
            this.SetIsBlocking(true);
        else this.SetIsBlocking(false);
    }
}
