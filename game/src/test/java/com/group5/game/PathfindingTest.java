package com.group5.game;
import com.group5.game.Types.Game.*;
import com.group5.game.Types.Math.*;
import org.junit.Test;
import com.group5.game.Miscellaneous.AI;

public class PathfindingTest extends BoardedTestCore {
    
    // --------------------------------------------------------------------------------------
    // [Integration tests] Tests integration between Board, pathfinding, and generation
    // ---------------------------------------------------------------------------------------

    //Tests navigation when possible
    @Test
    public void shouldPathfindCorrectly(){
        CreateBoard(0, 0, 0);
        
        //Create a wall with one opening
        for(int i = 0; i < testBoardSize - 2; i++)
        {
            Obstacle obstacle = new Obstacle(board, new Int2(i, 3));
            obstacle.SpawnEntity();
        }
        var path = AI.Pathfind(board, player.GetCoordinates(), new Int2(testBoardSize / 2, testBoardSize - 2), true);
        assert(path != null && PathValid(path));
    }

    //Tests navigation when impossible
    @Test
    public void shouldFailToPathfind(){
        CreateBoard(0, 0, 0);
        
        //Create a wall with one opening
        for(int i = 0; i < testBoardSize; i++)
        {
            Obstacle obstacle = new Obstacle(board, new Int2(i, 3));
            obstacle.SpawnEntity();
        }
        var path = AI.Pathfind(board, player.GetCoordinates(), new Int2(testBoardSize / 2, testBoardSize - 2), true);
        assert(path == null);
    }

    //Tests randomized generation for issues in being able to get to the other side. This could include: 
    //wall formations that block your way to the exit
    //punishments which will kill you on your only way to the exit,
    //enemies which block the way to the exit
    @Test
    public void shouldHavePathToExit(){
        for(int i = 0; i < bruteForceIterationCount; i++){
            CreateBoard(-1, -1, -1);
            var path = AI.Pathfind(board, player.GetCoordinates(), new Int2(testBoardSize / 2, testBoardSize - 2), true);
            assert(path != null && PathValid(path));
        }
    }

    //Tests if a randomly spawned enemy is able to reach the player properly
    //Note: tested with only one enemy because enemies tend to block each-other's paths to the player,
    //Even though in actual gameplay they will eventually move out of the way.
    @Test
    public void shouldEnemyHavePathToPlayer(){
        for(int i = 0; i < bruteForceIterationCount; i++){
            CreateBoard(1, -1, -1);
            var path = AI.Pathfind(board, enemies.get(0).GetCoordinates(), player.GetCoordinates(), false);
            assert(path != null && PathValid(path));
        }
    }

    //Tests if RandomReachableEmptySpot actually finds a random, reachable empty spot
    //Inadvertently tests spawning of Enemies, Rewards, Punishments, etcetera., so should be thoroughly tested.
    @Test
    public void shouldFindRandomReachablePosition(){
        for(int i = 0; i < bruteForceIterationCount; i++){
            CreateBoard(0, 0, -1);
            Int2 pos = board.RandomReachableEmptySpot(new Int2(0, 0), new Int2(testBoardSize, testBoardSize), player.GetCoordinates(), 5);
            var path = AI.Pathfind(board, player.GetCoordinates(), pos, false);
            assert(path != null && PathValid(path));
        }
    }

    //Tests handling of a player blocked from an enemy
    //Note: This is mainly to determine if Enemies handle having no path to the player without issue.
    @Test
    public void shouldHandleUnreachablePlayerByEnemy(){
        CreateBoard(1, 0, 0);
        enemies.get(0).SetCoordinates(new Int2(testBoardSize / 2, testBoardSize - 2));
        player.SetCoordinates(new Int2(testBoardSize / 2, 1));

        //Create an impassable wall
        for(int x = 0; x < testBoardSize; x++)
            new Obstacle(board, new Int2(x, testBoardSize / 2)).SpawnEntity();

        //Tick board, see if everything is ok
        board.Tick();
    }

}
