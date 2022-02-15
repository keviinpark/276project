package com.group5.game;

import com.group5.game.Types.Game.*;
import com.group5.game.Types.Math.*;
import org.junit.Test;

public class LogicIntegrationTest extends BoardedTestCore {
    
    // --------------------------------------------------------------------------------------
    // [Integration tests] Tests interaction between things like Player's score and Game State.
    // ---------------------------------------------------------------------------------------

    //Tests if 'OverLoss' gamestate occurs when game score reaches < 0
    @Test
    public void scoreShouldTriggerOverLoss_State() {
        CreateBoard(0, 1, 0);
        player.SetCoordinates(new Int2(5,4));
        player.ChangeScore(-1);
        assert(Game.GetInstance().GetGameState() == GameState.OverLoss);

        //Test when punishment causes score to become <0
        player.ChangeScore(10);
        Game.GetInstance().SetGameState(GameState.Active);
        Int2 punish_coord = board.RandomEmptySpot(new Int2(1,1), new Int2(testBoardSize - 1, testBoardSize - 1));

        Punishment testPunish1 = new Punishment(board, punish_coord,100);
        testPunish1.SpawnEntity();
        player.setTargetPosition(punish_coord);

        board.Tick();
        assert(Game.GetInstance().GetGameState() == GameState.OverLoss);  

        //Test when punishment does not cause score to beocme <0
        player.ChangeScore(1000);
        player.SetCoordinates(new Int2(5,4));
        Game.GetInstance().SetGameState(GameState.Active);
        punish_coord = board.RandomEmptySpot(new Int2(1,1), new Int2(testBoardSize - 1, testBoardSize - 1));

        Punishment testPunish2 = new Punishment(board, punish_coord,100);
        testPunish2.SpawnEntity();
        player.setTargetPosition(punish_coord);

        board.Tick();
        assert(Game.GetInstance().GetGameState() != GameState.OverLoss);  
    }

    //Tests if 'OverLoss' gamestate occurs when player is touched by enemy
    @Test
    public void enemyShouldTriggerOverLoss_State() {
        CreateBoard(1, 0, 0);
        enemies.get(0).SetCoordinates(new Int2(1,1));
        player.SetCoordinates(new Int2(2,1));
        player.ChangeScore(100);

        board.Tick();
        board.Tick();

        assert(Game.GetInstance().GetGameState() == GameState.OverLoss);

    }

    //Tests if rewards, punishments, etcetera. trigger when touched by a non-player
    //The desired behaviour is for all of these things to stay the same.
    @Test
    public void scoreModifiersShouldIgnoreNonPlayers(){
        ;
        Reward reward = new Reward(board, new Int2(1,1), 100);
        reward.SpawnEntity();

        RewardBonus bonus = new RewardBonus(board, new Int2(2,1), 100);
        bonus.SpawnEntity();

        Punishment punishment = new Punishment(board, new Int2(3,1), 100);
        punishment.SpawnEntity();

        Entity randomEntity = new TestObject(board, new Int2(0,0), false);
        randomEntity.SpawnEntity();

        randomEntity.SetCoordinates(new Int2(1,1));
        assert(reward.GetIsSpawned() == true);

        randomEntity.SetCoordinates(new Int2(2,1));
        assert(bonus.GetIsSpawned() == true);

        randomEntity.SetCoordinates(new Int2(3,1));
        assert(punishment.GetIsSpawned() == true);
    }

    //Tests values of punishments, rewards, etc.
    @Test
    public void modifierSignsShouldBeCorrect() {
        ;

        Reward reward = new Reward(board, new Int2(1,1), -100);
        reward.SpawnEntity();
        player.SetCoordinates(new Int2(1,1));
        assert(player.GetScore() == 100);

        RewardBonus bonus = new RewardBonus(board, new Int2(2,1), -100);
        bonus.SpawnEntity();
        player.SetCoordinates(new Int2(2,1));
        assert(player.GetScore() == 200);

        Punishment punishment = new Punishment(board, new Int2(3,1), -100);
        punishment.SpawnEntity();
        player.SetCoordinates(new Int2(3,1));
        assert(player.GetScore() == 100);

        Reward reward2 = new Reward(board, new Int2(1,1), 100);
        reward2.SpawnEntity();
        player.SetCoordinates(new Int2(1,1));
        assert(player.GetScore() == 200);

        RewardBonus bonus2 = new RewardBonus(board, new Int2(2,1), 100);
        bonus2.SpawnEntity();
        player.SetCoordinates(new Int2(2,1));
        assert(player.GetScore() == 300);

        Punishment punishment2 = new Punishment(board, new Int2(3,1), 100);
        punishment2.SpawnEntity();
        player.SetCoordinates(new Int2(3,1));
        assert(player.GetScore() == 200);
    }

    //Tests if bonus rewards are increasing score as expected
    @Test
    public void bonusRewardsShouldIncreaseScore() {
        CreateBoard(0, 0, 0);
        RewardBonus testReward = new RewardBonus(board, new Int2(1, 1), 500);
        testReward.SpawnEntity();
        player.SetCoordinates(new Int2(1, 1));
        assert(player.GetScore() == 500);
    }

    //Tests if rewards are increasing score as expected
    @Test
    public void rewardsShouldIncreaseScore() {
        CreateBoard(0, 0, 0);
        Reward testReward = new Reward(board, new Int2(1, 1), 100);
        testReward.SpawnEntity();
        player.SetCoordinates(new Int2(1, 1));
        assert(player.GetScore() == 100);
    }

    //Tests if punishments are decreasing score as expected
    @Test
    public void punishmentsShouldDecreaseScore() {
        CreateBoard(0, 0, 0);
        player.ChangeScore(200);
        Punishment testPunish = new Punishment(board, new Int2(1,1),100);
        testPunish.SpawnEntity();
        player.SetCoordinates(new Int2(1, 1));
        assert(player.GetScore() == 100);
    }

    //Tests if sufficient rewards opens the exit (5)
    @Test
    public void rewardsShouldOpenExit() {
        CreateBoard(0, 0, 0);
        Gate testGate = new Gate(board, new Int2(1,1), player, 5);
        testGate.SpawnEntity();
        for(int i = 0; i < 5; i++) player.IncrementRewards();
        board.Tick();
        assert(testGate.GetIsBlocking() == false);

    }

    // --------------------------------------------------------------------------------------
    // [Integration Test] Tests initial values, how those values interact with other systems
    // ---------------------------------------------------------------------------------------

    //Tests that score = 0 at the start of the game
    @Test
    public void scoreShouldBeZero() {
        CreateBoard(0, 0, 0);
        assert(player.GetScore() == 0);

    }

    //Tests that time == 0 start of game
    @Test
    public void timeShouldBeZero() {
        assert(Game.GetInstance().GetElapsedTime() == 0);
    }
}
