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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.group5.game.Interfaces.IRenderable;
import com.group5.game.Types.Math.Int2;

import com.group5.game.Game;
import com.group5.game.GameState;
import com.group5.game.Handlers.InputHandler;;

public class StartScreen implements IRenderable {
    private Skin skin;

    private Stage stage;
    private Button button;
    private Label label;

    public StartScreen() {
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

        // label
        label = new Label("SUPER FUN GAME ULTRA EDITION", skin , "default");
        label.setFontScale(1.00f);
        label.setAlignment(Align.center);

        // button
        Skin mySkin = new Skin(Gdx.files.internal("uiskin.json"));
        button = new TextButton("   Start   ", mySkin);
		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y){
				Game.GetInstance().StartGame();
			}
		});


        // add labels to table
        table.add(label).row();
        table.add(button).row();

        //Register input handler
        InputHandler.GetInstance().AddInputProcessor(stage);

    }

    @Override
    public void Render(SpriteBatch batch){
        stage.draw();
    }

    @Override
    public void OnResize(Int2 screenDimensions) 
    {
        stage.getViewport().update(screenDimensions.x, screenDimensions.y, true);
    }

    @Override
    public boolean IsActive() { return Game.GetInstance().GetGameState() == GameState.Unstarted; }

    @Override
    public boolean UsesOwnBatch(){ return true; }
}
