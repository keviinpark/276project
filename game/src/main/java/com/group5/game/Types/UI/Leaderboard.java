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
import com.group5.game.Game;
import com.group5.game.GameState;
import com.group5.game.Handlers.InputHandler;
import com.group5.game.Interfaces.IRenderable;
import com.group5.game.Types.Math.Int2;

public class Leaderboard implements IRenderable{
    private Skin skin;

    private Table table;
    private Stage stage;

    private Label place_1;
    private Label place_2;
    private Label place_3;
    private Label place_4;
    private Label place_5;
    private Label place_6;
    private Label place_7;
    private Label place_8;
    private Label place_9;
    private Label place_10;
   

    public Leaderboard(){
        skin = new Skin(Gdx.files.internal("arcade-ui.json"));
        stage = new Stage(new ScreenViewport());
        table = new Table(skin);

        table.setFillParent(true); // this makes it so table will fill the entire stage/screen
        NinePatch patch = new NinePatch(new Texture("grey.9.png"), 10, 10, 20, 20);
		NinePatchDrawable background = new NinePatchDrawable(patch);
		table.setBackground(background);
		table.pack();

        // add table to stage
        stage.addActor(table);

        place_1 = new Label("1", skin, "default");
        place_1.setFontScale(1.00f);
        place_1.setAlignment(Align.left);

        place_2 = new Label("2", skin, "default");
        place_2.setFontScale(1.00f);
        place_2.setAlignment(Align.left);

        place_3 = new Label("3", skin, "default");
        place_3.setFontScale(1.00f);
        place_3.setAlignment(Align.left);

        place_4 = new Label("4", skin, "default");
        place_4.setFontScale(1.00f);
        place_4.setAlignment(Align.left);

        place_5 = new Label("5", skin, "default");
        place_5.setFontScale(1.00f);
        place_5.setAlignment(Align.left);

        place_6 = new Label("6", skin, "default");
        place_6.setFontScale(1.00f);
        place_6.setAlignment(Align.left);

        place_7 = new Label("7", skin, "default");
        place_7.setFontScale(1.00f);
        place_7.setAlignment(Align.left);

        place_8 = new Label("8", skin, "default");
        place_8.setFontScale(1.00f);
        place_8.setAlignment(Align.left);

        place_9 = new Label("9", skin, "default");
        place_9.setFontScale(1.00f);
        place_9.setAlignment(Align.left);

        place_10 = new Label("10", skin, "default");
        place_10.setFontScale(1.00f);
        place_10.setAlignment(Align.left);

        //Add behaviour for pausing/unpausing
        Skin mySkin = new Skin(Gdx.files.internal("uiskin.json"));
		TextButton restartButton = new TextButton("   Restart   ", mySkin);
        restartButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				Game.GetInstance().StartGame();
			}
		});



        table.add(place_1).row();
        table.add(place_2).row();
        table.add(place_3).row();
        table.add(place_4).row();
        table.add(place_5).row();
        table.add(place_6).row();
        table.add(place_7).row();
        table.add(place_8).row();
        table.add(place_9).row();
        table.add(place_10).row();



        //Register input handler
        InputHandler.GetInstance().AddInputProcessor(stage);

        
    }

    @Override
    public void Render(SpriteBatch batch) {
        
        place_1.setText("First: ");
        place_2.setText("Second: ");
        place_3.setText("Third: ");
        place_4.setText("Fourth: ");
        place_5.setText("Fifth: ");
        place_6.setText("Sixth: ");
        place_7.setText("Seventh: ");
        place_8.setText("Eighth: ");
        place_9.setText("Nineth: ");
        place_10.setText("Tenth: ");

        stage.draw();
    }

    @Override
    public void OnResize(Int2 screenDimensions) 
    {
		stage.getViewport().update(screenDimensions.x, screenDimensions.y, true);
    }

    @Override
    public boolean IsActive() { return Game.GetInstance().GetGameState() == GameState.Leaderboard; }

    @Override
    public boolean UsesOwnBatch(){ return true; }
    
}
