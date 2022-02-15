package com.group5.game.Types.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;



import com.group5.game.Game;
import com.group5.game.GameState;
import com.group5.game.Handlers.InputHandler;
import com.group5.game.Interfaces.IRenderable;
import com.group5.game.Types.Math.Int2;

public class UItable implements IRenderable {
	
	private Skin skin;
	private Stage stage;
	private Int2 screenSize;
	private float score;
	private int rewardsCollected;

	private Table table;
	private Label gameTitle;
	private Label gameTime;
	private Label gameScore;
	private Label rewardProgress;
	private Label leaderboardView;

	private TextButton pauseButton;
	private TextButton exitButton;
	private TextButton restartButton;

	public UItable(){
		skin = new Skin(Gdx.files.internal("arcade-ui.json"));
		stage = new Stage(new ScreenViewport());
		table = new Table(skin);

		// set up the background of our table
		NinePatch patch = new NinePatch(new Texture("grey.9.png"), 10, 10, 20, 20);
		NinePatchDrawable background = new NinePatchDrawable(patch);
		table.setBackground(background);
		table.pack();


		// labels for displaying relevant game stats
		gameTitle = new Label("2D Game", skin , "default");
		gameTitle.setFontScale(0.8f);
		gameTitle.setAlignment(Align.center);

		gameTime = new Label("", skin, "default");
		gameTime.setFontScale(0.6f);
		gameTime.setAlignment(Align.center);

		gameScore = new Label("", skin, "default");
		gameScore.setFontScale(0.6f);
		gameScore.setAlignment(Align.center);

		rewardProgress = new Label("", skin, "default");
		rewardProgress.setFontScale(0.6f);
		rewardProgress.setAlignment(Align.center);

		leaderboardView = new Label("Leaderboards" , skin, "default");
		leaderboardView.setFontScale(0.6f);
		leaderboardView.setAlignment(Align.center);

		// adding buttons to table
		Skin mySkin = new Skin(Gdx.files.internal("uiskin.json"));
		pauseButton = new TextButton("   Pause   ", mySkin);
		exitButton = new TextButton("   Exit   ", mySkin);
		restartButton = new TextButton("   Restart   ", mySkin);

		//Add behaviour for exiting the game
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				Gdx.app.exit();
			}
		});

		//Add behaviour for pausing/unpausing
		pauseButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				GameState state = Game.GetInstance().GetGameState();
				if(state == GameState.Paused) Game.GetInstance().SetGameState(GameState.Active);
				else Game.GetInstance().SetGameState(GameState.Paused);
			}
		});

		//Add behaviour for pausing/unpausing
		restartButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				Game.GetInstance().StartGame();
			}
		});


		// add everything to the table
		table.add(gameTitle).padTop(0).row();
		table.add(gameTime).padTop(0).row();
		table.add(gameScore).padTop(0).row();
		table.add(rewardProgress).padTop(0).row();
		table.add(leaderboardView).padTop(0).row();

		table.add(pauseButton).padTop(60).row();
		table.add(exitButton).padTop(5).row();
		table.add(restartButton).padTop(5).row();

		stage.addActor(table);

		// add a new input processor
		InputHandler.GetInstance().AddInputProcessor(stage);
	}


    @Override
    public void Render(SpriteBatch batch) {

		// position of table to be changed whenever screen is resized
		table.setPosition(screenSize.x * 0.02f, screenSize.y * 0.10f);
		table.setTransform(true);
		table.setScale(screenSize.x *0.001f, screenSize.y *0.003f);

		// set new values
		gameTime.setText("Time: " + String.format("%.01f", Game.GetInstance().GetElapsedTime()));
		gameScore.setText("Score: " + score);
		rewardProgress.setText("Rewards: " + rewardsCollected + "/5");

		// set pause button text depending on game state
		pauseButton.setText(Game.GetInstance().GetGameState() != GameState.Paused ? "Pause" : "Unpause");

		stage.draw();
    }

	// updates all relevant game statistics in one function
	public void updateStats(float updatedScore, int rewardsCollected) {
		score = updatedScore;
		this.rewardsCollected = rewardsCollected;
	}

	@Override
    public void OnResize(Int2 screenDimensions) {
		screenSize = screenDimensions;
		stage.getViewport().update(screenDimensions.x, screenDimensions.y, true);
	}

    @Override
    public boolean IsActive() { return true; }

	@Override
    public boolean UsesOwnBatch(){ return true; }
}
