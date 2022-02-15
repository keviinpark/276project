package com.group5.game.Types.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.group5.game.Interfaces.IRenderable;
import com.group5.game.Types.Math.Int2;

import com.group5.game.Game;
import com.group5.game.GameState;
import com.group5.game.Handlers.InputHandler;

import java.io.*;
import java.util.Scanner;


/**
 * The screen that is shown at the end of the game if you win.
 * Displays your final score, time, and a button to restart.
 */
public class WinScreen implements IRenderable {
    private Skin skin;

    private Stage stage;
    private Label win;
    private Label finalScore;
    private Label highScore;
    private Label finalTime;

    public WinScreen() {
        skin = new Skin(Gdx.files.internal("arcade-ui.json"));
        stage = new Stage(new ScreenViewport());
        Table table = new Table(skin);

        // set up background for table
        table.setFillParent(true); // this makes it so table will fill the entire stage/screen
        NinePatch patch = new NinePatch(new Texture("grey.9.png"), 10, 10, 20, 20);
		NinePatchDrawable background = new NinePatchDrawable(patch);
		table.setBackground(background);
		table.pack();

        // add table to stage
        stage.addActor(table);

        // labels for displaying final stats 
        win = new Label("!VICTORY!", skin , "default");
        win.setFontScale(1.00f);
        win.setAlignment(Align.center);

        finalScore = new Label("Your Score: ", skin, "default");
        finalScore.setFontScale(0.80f);
        finalScore.setAlignment(Align.center);

        finalTime = new Label("Final Time: ", skin, "default");
        finalTime.setFontScale(0.80f);
        finalTime.setAlignment(Align.center);

        highScore = new Label("High Score: ", skin, "default");
        highScore.setFontScale(0.80f);
        highScore.setAlignment(Align.center);

		//Add behaviour for pausing/unpausing
        Skin mySkin = new Skin(Gdx.files.internal("uiskin.json"));
		TextButton restartButton = new TextButton("   Restart   ", mySkin);
        restartButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				Game.GetInstance().StartGame();
			}
		});
        
        // add labels to table
        table.add(win).row();
        table.add(finalScore).row();
        table.add(finalTime).row();
        table.add(highScore).row();
        table.add(restartButton).row();

        //Register input handler
        InputHandler.GetInstance().AddInputProcessor(stage);
    }

    @Override
    public void Render(SpriteBatch batch) {
        //read the value of the current high score from the file leaderboard.txt
        float high_score=0;
        try{
            File file = new File("game/src/main/java/com/group5/game/Types/UI/leaderboard.txt");
            Scanner scan = new Scanner (file);
            high_score = Float.parseFloat(scan.nextLine());
            scan.close();
        }
        catch(FileNotFoundException e){ high_score = -1; }

        //overwrite with the new high score, if applicable
        if (Game.GetInstance().GetScore() > high_score){
            high_score = Game.GetInstance().GetScore();
            File file = new File("game/src/main/java/com/group5/game/Types/UI/leaderboard.txt");

            if (file.exists()){
                try{
                    Writer fileWriter = new FileWriter("game/src/main/java/com/group5/game/Types/UI/leaderboard.txt");
                    fileWriter.write(String.valueOf(high_score));
                    fileWriter.close();
                }
                catch(IOException ioe){}
            }
        }

        finalScore.setText("Your Score: " + Game.GetInstance().GetScore());
        finalTime.setText("Final Time: " + String.format("%.01f", Game.GetInstance().GetElapsedTime()));
        highScore.setText("High Score: " + String.format("%.01f", high_score));

        stage.draw();
    }

    @Override
    public void OnResize(Int2 screenDimensions) 
    {
        stage.getViewport().update(screenDimensions.x, screenDimensions.y, true);
    }

    @Override
    public boolean IsActive() { return Game.GetInstance().GetGameState() == GameState.OverWin; }

    @Override
    public boolean UsesOwnBatch(){ return true; }
}
