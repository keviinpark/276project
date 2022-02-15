package com.group5.game.Miscellaneous;

import com.group5.game.Types.Game.*;
import com.group5.game.Types.Math.*;

import java.util.ArrayList;
import java.util.List;

public class BoardBuilder { 
    
    public Player player;
    public Board board;
    public List<Enemy> enemies;
	public List<Punishment> punishments;

    public int gateUnlockRewardCount = 5;

    public int punishmentCount = 10;

    public int boardSize = 20;
    public int wallFormationCount = 15;

    public int fastEnemyCount = 1;
    public int fastEnemyTicks = 2;

    public int slowEnemyCount = 8;
    public int slowEnemyTicks = 4;

    public void BuildBasicBoard(){
        board = new Board(boardSize, boardSize);
        player = new Player(board, new Int2(boardSize / 2, 1));
        player.SpawnEntity();
        enemies = new ArrayList<Enemy>();
		punishments = new ArrayList<Punishment>();
    }
    public void BuildGates(){
        //Create gates
		Gate northGate = new Gate(board, new Int2(boardSize / 2, boardSize - 1), player, gateUnlockRewardCount);
		northGate.SpawnEntity();

		Gate southGate = new Gate(board, new Int2(boardSize / 2, 0), player, 9999999);
		southGate.SpawnEntity();
    }
    public void BuildWalls(){
		//Create map for walls
		MazeBuilder builder = new MazeBuilder();
		builder.AddPatterns(MazeShapes.List);
		boolean[][] maze = builder.Create(boardSize, wallFormationCount);
		
		//Add exterior walls,
		//And cut out inside exterior walls to prevent getting trapped in corners as easily
		for(int x = 0; x < boardSize; x++){
			for(int y = 0; y < boardSize; y++){
				if((x == 1 || x == boardSize - 2) || (y == 1 || y == boardSize - 2)) maze[x][y] = false;
				if((x == 0 || x == boardSize - 1) || (y == 0 || y == boardSize - 1)) maze[x][y] = true;
			}
		}




		//Ensure at least *one* path to the exit
		for(int y = 0; y < boardSize; y++)
			maze[boardSize / 2][y] = false;

		//Create wall entities
		for(int x = 0; x < boardSize; x++){
			for(int y = 0; y < boardSize; y++){
				if(maze[x][y]){
					Entity wall = new Obstacle(board, new Int2(x,y));
					wall.SpawnEntity();
				}
			}
		}
    }
    public void BuildPunishments(){
		//Create punishments
		for(int i = 0; i < punishmentCount; i++){
			Int2 coord = board.RandomReachableEmptySpot(new Int2(0,0), new Int2(boardSize - 1, boardSize - 1), player.GetCoordinates(), 5);
			
			//Prevent death-tunnels by checking adjacent tiles for obstacles/walls
			boolean placeable = true;
			if(!board.IsEmpty(new Int2(coord.x + 1, coord.y))) placeable = false;
			if(!board.IsEmpty(new Int2(coord.x - 1, coord.y))) placeable = false;
			if(!board.IsEmpty(new Int2(coord.x, coord.y + 1))) placeable = false;
			if(!board.IsEmpty(new Int2(coord.x, coord.y - 1))) placeable = false;
			if(!board.IsEmpty(new Int2(coord.x + 1, coord.y + 1))) placeable = false;
			if(!board.IsEmpty(new Int2(coord.x - 1, coord.y - 1))) placeable = false;
			if(!board.IsEmpty(new Int2(coord.x - 1, coord.y + 1))) placeable = false;
			if(!board.IsEmpty(new Int2(coord.x + 1, coord.y - 1))) placeable = false;

			//Place if possible
			if(placeable){
				Punishment punishment = new Punishment(board, coord, 100);
				punishment.SpawnEntity();
				punishments.add(punishment);
			}
		}
    }
    public void BuildFastEnemies(){
        //Create fast enemies
		for(int i = 0; i < fastEnemyCount; i++){
			Int2 coord = board.RandomReachableEmptySpot(new Int2(0,0), new Int2(boardSize - 1, boardSize - 1), player.GetCoordinates(), 5);
			Enemy enemy = new Enemy(board, coord, player, fastEnemyTicks, "enemy.png", "enemy_charged.png");
			enemy.SpawnEntity();
            enemies.add(enemy);
		}
    }
    public void BuildSlowEnemies(){
		//Create slow enemies
		for(int i = 0; i < slowEnemyCount; i++){
			Int2 coord = board.RandomReachableEmptySpot(new Int2(0,0), new Int2(boardSize - 1, boardSize - 1), player.GetCoordinates(), 5);
			Enemy enemy = new Enemy(board, coord, player, slowEnemyTicks, "lazy.png", "lazy_charged.png");
			enemy.SpawnEntity();
            enemies.add(enemy);
        }
    }

    public void Build() {
        BuildBasicBoard();
        BuildGates();
        BuildWalls();
        BuildPunishments();
        BuildFastEnemies();
        BuildSlowEnemies();
    }
}
