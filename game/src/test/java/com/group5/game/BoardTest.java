package com.group5.game;

import com.group5.game.Types.Game.*;
import com.group5.game.Types.Math.Int2;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class BoardTest extends BoardedTestCore
{
    // --------------------------------------------------------------------------------------
    // [Unit tests] Simple board placement, moving, removing
    // ---------------------------------------------------------------------------------------

    //Tests simple placement case
    public void shouldSpawnProperly(){
        CreateBoard(0,0,0);
        assert(player.SetCoordinates(new Int2(0,0)) == true); //Can move to open space
        assert(player.SpawnEntity() == false); //Cannot spawn again after initial spawn
    }

    //Tests out of bounds movement
    @Test
    public void shouldPreventOutOfBoundsMove()
    {
        CreateBoard(0, 0, 0);

        //Tests negative out of bounds
        assert(player.SetCoordinates(new Int2(-1, -1)) == false);
        assert(player.SetCoordinates(new Int2(-1, 0)) == false);
        assert(player.SetCoordinates(new Int2(0, -1)) == false);

        //Tests positive out of bounds
        assert(player.SetCoordinates(new Int2(testBoardSize, testBoardSize)) == false);
        assert(player.SetCoordinates(new Int2(testBoardSize, 0)) == false);
        assert(player.SetCoordinates(new Int2(0, testBoardSize)) == false);
    }

    //Tests movement into obstacle
    @Test
    public void shouldPreventObstacleMove(){
        CreateBoard(0, 0, 0);

        player.SetCoordinates(new Int2(0,0));
        Obstacle obstacle = new Obstacle(board, new Int2(0, 1));
        obstacle.SpawnEntity();

        assert(player.SetCoordinates(new Int2(0, 1)) == false);
    }

    //Tests spawning on top of obstacle
    @Test
    public void shouldPreventSpawnAtopObstacle(){
        CreateBoard(0, 0, 0);

        Obstacle obstacle = new Obstacle(board, new Int2(0, 1));
        obstacle.SpawnEntity();

        assert(player.RemoveEntity() == true);
        player.SetCoordinates(new Int2(0, 1));
        assert(player.SpawnEntity() == false);
    }

    //Tests spawning atop a non-obstacle entity
    @Test
    public void shouldAllowSpawningAtopNonBlocker(){
        CreateBoard(0,0,0);
        Int2 testPosition = new Int2(1, 1);

        Entity nonBlocker = new TestObject(board, testPosition, false);
        nonBlocker.SpawnEntity();
        assert(player.SetCoordinates(testPosition) == true);
    }

    @Test
    public void shouldHandleDespawn(){
        
        //Test repeated removal attempts
        CreateBoard(0,0,0);
        Entity entity = new TestObject(board, new Int2(1,1), false);
        entity.SpawnEntity();
        assert(entity.RemoveEntity() == true);
        assert(board.IsEmpty(entity.GetCoordinates()));
        assert(entity.RemoveEntity() == false);

        //Test when there is the wrong entity on the tile (i.e. set exists, does not contain entity)
        CreateBoard(0, 0, 0);
        Entity wrongEntity = new TestObject(board, new Int2(1,1), false);
        wrongEntity.SpawnEntity();
        assert(entity.RemoveEntity() == false);

        //Test when it is out of bounds (i.e., first clause not met)
        CreateBoard(0,0,0);
        Entity outOfBoundsEntity = new TestObject(board, new Int2(-1,-1), false);
        assert(outOfBoundsEntity.RemoveEntity() == false);
    }

    // --------------------------------------------------------------------------------------
    // [Unit tests] Simple entity fetching, removal, checking on board, other misc. tests
    // ---------------------------------------------------------------------------------------
    @Test
    public void shouldPassEasiestTest(){
        CreateBoard(0,0,0);
        assert(board.GetBoardDimensions().equals(new Int2(testBoardSize, testBoardSize)));
    }
    
    @Test
    public void shouldGetEntitySet(){
        CreateBoard(0,0,0);

        Int2 coordinate = new Int2(1,1);
        assert(board.IsEmpty(coordinate));

        TestObject obj = new TestObject(board, coordinate, false);
        assert(obj.SpawnEntity() == true);
        assert(board.GetEntitiesAt(coordinate).size() == 1);

        TestObject obj2 = new TestObject(board, coordinate, false);
        assert(obj2.SpawnEntity() == true);
        assert(board.GetEntitiesAt(coordinate).size() == 2);
    }

    //Tests removing all entities from a tile (never actually used in code, still maybe useful)
    @Test
    public void shouldFlushEntitiesCorrectly(){
        CreateBoard(0, 0, 0);
        
        assert(board.RemoveAllEntitiesAt(new Int2(-1,-1)) == false); //Test out of bounds
        assert(board.RemoveAllEntitiesAt(new Int2(1,1)) == false); //Test with no existing entities

        Entity testObj1 = new TestObject(board, new Int2(1,1), false);
        testObj1.SpawnEntity();

        assert(board.RemoveAllEntitiesAt(new Int2(1,1)) == true); //Test with existing entities
        assert(board.IsEmpty(new Int2(1,1)) == true); //Test entities were actually removed
        

    }

    //Tests if position finding works when it should, and doesn't when it shouldn't
    @Test
    public void shouldHandlePositionFindingIssues(){

        Int2 from = new Int2(1,1);
        Int2 min = new Int2(0,0);
        Int2 max = new Int2(testBoardSize - 1, testBoardSize - 1);

        //Start with 100% reachable
        CreateBoard(0,0,0);
        assert(board.RandomEmptySpot(min, max) != null);
        assert(board.RandomReachableEmptySpot(min, max, from, 5) != null);

        //Test with completely unreachable
        CreateBoard(0,0,0);
        for(int x = 0; x < testBoardSize; x++)
            for(int y = 0; y < testBoardSize; y++)
            {
                Obstacle obst = new Obstacle(board, new Int2(x,y));
                obst.SpawnEntity();
            }
        assert(board.RandomEmptySpot(min, max) == null);
        assert(board.RandomReachableEmptySpot(min, max, from, 5) == null);

        //Test with no areas in range of RandomReachableEmptySpot
        CreateBoard(0,0,0);
        for(int x = 0; x < testBoardSize; x++)
            for(int y = 0; y < testBoardSize; y++)
            {
                if(x < 5 && y < 5){
                    Obstacle obst = new Obstacle(board, new Int2(x,y));
                    obst.SpawnEntity();
                }
            }
        assert(board.RandomEmptySpot(min, max) != null);
        assert(board.RandomReachableEmptySpot(min, max, from, 5) == null);

        //Test with areas in range of RandomReachableEmptySpot, but with no path to them
        CreateBoard(0,0,0);

        Obstacle obst1 = new Obstacle(board, Int2.Plus(from, new Int2(1,0)));
        Obstacle obst2 = new Obstacle(board, Int2.Plus(from, new Int2(-1,0)));
        Obstacle obst3 = new Obstacle(board, Int2.Plus(from, new Int2(0,1)));
        Obstacle obst4 = new Obstacle(board, Int2.Plus(from, new Int2(0,-1)));
        obst1.SpawnEntity();
        obst2.SpawnEntity();
        obst3.SpawnEntity();
        obst4.SpawnEntity();

        assert(board.RandomEmptySpot(min, max) != null);
        assert(board.RandomReachableEmptySpot(min, max, from, 5) == null);
    }

    //Simply checks if having the player not included in Tick cycle causes issues
    @Test
    public void shouldHandlePlayerAbsenceForTicks(){
        CreateBoard(0, 0, 0);
        board.Tick();
        player.RemoveEntity();
        board.Tick();
    }
}
