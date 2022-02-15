package com.group5.game;

import java.util.LinkedList;
import java.util.List;

import com.group5.game.Miscellaneous.BoardBuilder;
import com.group5.game.Types.Game.*;
import com.group5.game.Types.Math.Int2;

import org.junit.Before;

public class BoardedTestCore {
    protected int bruteForceIterationCount = 100;
    protected int testBoardSize = 20;
    protected int wallGenerationCount = 15;

    protected Board board;
    protected Player player;
    protected List<Enemy> enemies;
    protected List<Punishment> punishments;

    
    //Helper to create a board
    protected void CreateBoard(int enemies, int punishments, int mazePieces){
        BoardBuilder builder = new BoardBuilder();

        builder.boardSize = testBoardSize;

        if(enemies != -1){
            builder.slowEnemyCount = 0;
            builder.fastEnemyCount = enemies;
        }
        if(punishments != -1){
            builder.punishmentCount = punishments;
        }
        if(mazePieces != -1){
            builder.wallFormationCount = mazePieces;
        }

        builder.Build();
        this.board = builder.board;
        this.player = builder.player;
        this.enemies = builder.enemies;
        this.punishments = builder.punishments;
    } 

    //Helper to see if a path is valid
    protected  boolean PathValid(LinkedList<Int2> path){
        for(int i = 1; i < path.size() - 1; i++){
            if(!board.SpaceAvailable(path.get(i)))
                return false;
        }
        return true;
    }

    @Before
    public void CreateDefaultEmptyBoard(){
        CreateBoard(0,0,0);
    }
}
