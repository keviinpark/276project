package com.group5.game;

import com.group5.game.Handlers.*;
import com.group5.game.Interfaces.IRenderable;
import com.group5.game.Miscellaneous.BoardBuilder;
import com.group5.game.Types.Game.*;
import com.group5.game.Types.Math.*;

import com.group5.game.Types.UI.*;

import com.group5.game.Types.Rendering.BoardRenderer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

public class Game implements ApplicationListener {

	private static Game s_game;
	private Game() { }
	public static Game GetInstance(){
		if(s_game == null) s_game = new Game();
		return s_game;
	}

	//Board & entities
	private Board m_board;
	private Player m_player;

	//Used for debugging
	public Player GetPlayer(){
		return m_player;
	}

	//Renderers
	private UItable UI;
	private LoseScreen loseScreen;
	private Leaderboard lBoard;
	private WinScreen winScreen;
	private StartScreen startScreen;
	private BoardRenderer boardRenderer;

	//Used for debugging
	public IRenderable[] GetRenderers(){
		return new IRenderable[]{ UI, loseScreen, lBoard, winScreen, startScreen, boardRenderer };
	}

	//Game state
	GameState m_gameState = GameState.Unstarted;

	//Map generation variables
	int mapSize = 20;
	int punishmentCount = 10;
	
	int slowEnemies = 8;
	int fastEnemies = 1;

	int enemyFastTicks = 2;
	int enemySlowTicks = 4;

	int wallFormationCount = 15;

	//View range (radius of tiles viewable around player)
	int viewRange = 5;

	//Time elapsed
	private float m_timeElapsed = 0;

	//Ticks (interval is time between ticks in seconds)
	float tickInterval = 0.33f;
	private float m_timeSinceLastTick = 0;

	//Rewards remaining
	int rewardsRemaining = 5;
	int ticksBetweenReward = 5;
	Reward m_currentReward;
	int m_ticksSinceLastRewardSpawn = 0;

	//Bonus variables
	int ticksBetweenBonus = 20;
	RewardBonus m_currentBonus;
	int m_ticksSinceLastBonusSpawn = 0;

	/**
	 * The elapsed time since game start
	 * @return The time in seconds
	 */
	public float GetElapsedTime(){
		return m_timeElapsed;
	}
	/**
	 * Get the current state of the game
	 * @return
	 */
	public GameState GetGameState(){
		return m_gameState;
	}

	/**
	 * Tries to set the state of the game
	 * @param targetState The target state to set to
	 * @return True if successful, false otherwise
	 */
	public boolean SetGameState(GameState targetState){
		m_gameState = targetState;
		return true;
	}

	public float GetScore(){
		return m_player != null ? m_player.GetScore() : 0;
	}

	//Start the game
	public void StartGame(){
		
		//Reset values
		rewardsRemaining = 5;
		m_currentReward = null;
		m_currentBonus = null;
		m_ticksSinceLastRewardSpawn = 0;
		m_ticksSinceLastBonusSpawn = 0;
		m_timeElapsed = 0;
		m_timeSinceLastTick = 0;

		//Build board
		BoardBuilder builder = new BoardBuilder();
		
		builder.boardSize = mapSize;

		builder.fastEnemyCount = fastEnemies;
		builder.fastEnemyTicks = enemyFastTicks;
		builder.slowEnemyCount = slowEnemies;
		builder.slowEnemyTicks = enemySlowTicks;

		builder.wallFormationCount = wallFormationCount;
		builder.punishmentCount = punishmentCount;


		builder.Build();
		m_player = builder.player;
		m_board = builder.board;


		//Bind renderer, startup game
		boardRenderer.SetBoard(m_board);
		m_gameState = GameState.Active;
	}
	
	//Invoke a game tick
	public void GameTick(){
		m_board.Tick();

		//No reward right now
		if(m_currentReward == null){
			if(rewardsRemaining > 0){
				m_ticksSinceLastRewardSpawn++;
	
				//If enough time passed, spawn reward
				if(m_ticksSinceLastRewardSpawn >= ticksBetweenReward){
					m_ticksSinceLastRewardSpawn = 0;
					rewardsRemaining--;
	
					Int2 coord = m_board.RandomReachableEmptySpot(new Int2(0,0), new Int2(mapSize - 1, mapSize - 1), m_player.GetCoordinates(), 5);
					m_currentReward = new Reward(m_board, coord, 100);
					m_currentReward.SpawnEntity();
				}
			}
		}

		//Erase collected or despawned reward
		else {
			m_ticksSinceLastRewardSpawn = 0;
			if(!m_currentReward.GetIsSpawned()) m_currentReward = null;
		}


		//No bonus right now
		if(m_currentBonus == null){
			m_ticksSinceLastBonusSpawn++;

			//If enough time passed, spawn bonus
			if(m_ticksSinceLastBonusSpawn >= ticksBetweenBonus){
				m_ticksSinceLastBonusSpawn = 0;

				Int2 coord = m_board.RandomReachableEmptySpot(new Int2(0,0), new Int2(mapSize - 1, mapSize - 1), m_player.GetCoordinates(), 5);
				m_currentBonus = new RewardBonus(m_board, coord, 100);
				m_currentBonus.SpawnEntity();
			}
		}
		//Erase collected or despawned bonus
		else {
			m_ticksSinceLastBonusSpawn = 0;
			if(!m_currentBonus.GetIsSpawned()) m_currentBonus = null;
		}

		//Update UI
		UI.updateStats(m_player.GetScore(), m_player.GetRewardsCollected());
	}

	@Override
	public void create () {
		//Create UI
		UI = new UItable();		
		loseScreen = new LoseScreen();
		winScreen = new WinScreen();
		lBoard = new Leaderboard();
		startScreen = new StartScreen();
		boardRenderer = new BoardRenderer(viewRange * 2 + 1, 0.9f, null);

		RenderHandler.GetInstance().AddRenderable(UI);
		RenderHandler.GetInstance().AddRenderable(loseScreen);
		RenderHandler.GetInstance().AddRenderable(winScreen);
		RenderHandler.GetInstance().AddRenderable(lBoard);
		RenderHandler.GetInstance().AddRenderable(startScreen);
		RenderHandler.GetInstance().AddRenderable(boardRenderer);
	}
	@Override
	public void resize (int width, int height) {
		RenderHandler.GetInstance().ResizeScreen();
	}
	@Override
	public void render () {
		//Render all objects
		RenderHandler.GetInstance().Render();

		//Handle Tick events on board of game is active	
		if(m_gameState == GameState.Active){
			m_timeSinceLastTick += Gdx.graphics.getDeltaTime();	
			if(m_timeSinceLastTick > tickInterval){
				m_timeSinceLastTick = 0;
				GameTick();
			}
			m_timeElapsed += Gdx.graphics.getDeltaTime();
		}

		//Move "camera" to follow player
		if(m_player != null && (viewRange * 2 + 1) < mapSize) {
			Int2 pcoord = m_player.GetCoordinates();
			Int2 idealMin = new Int2(pcoord.x - viewRange, pcoord.y - viewRange);
			int truex = idealMin.x;
			int truey = idealMin.y;
		
			if(idealMin.x < 0) truex = 0;
			if(idealMin.y < 0) truey = 0;
		
			if(idealMin.x + viewRange * 2 + 1 >= mapSize) truex = mapSize - (viewRange * 2) - 1;
			if(idealMin.y + viewRange * 2 + 1 >= mapSize) truey = mapSize - (viewRange * 2) - 1;
		
			boardRenderer.SetDrawCorner(new Int2(truex, truey));
		}
		
	}
	@Override
	public void pause () { }
	@Override
	public void resume () { }
	@Override
	public void dispose () { }
}

