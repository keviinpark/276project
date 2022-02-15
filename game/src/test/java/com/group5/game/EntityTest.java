package com.group5.game;
import com.group5.game.Types.Game.*;
import com.group5.game.Types.Math.*;
import org.junit.Test;

public class EntityTest extends BoardedTestCore {
    @Test
    public void shouldHaveConsistentGetSets(){
        Board board = new Board(10, 10);

        TestObject entity = new TestObject(board, new Int2(0,0), true);
        
        assert(entity.SetCoordinates(new Int2(1,1)) == true);
        assert(entity.GetCoordinates().equals(new Int2(1,1)));

        entity.SetIsBlocking(true);
        assert(entity.GetIsBlocking() == true);

        entity.SetIsBlocking(false);
        assert(entity.GetIsBlocking() == false);
    }
}
