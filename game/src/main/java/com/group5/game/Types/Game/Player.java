package com.group5.game.Types.Game;
import com.group5.game.GameState;
import com.group5.game.Handlers.InputHandler;
import com.group5.game.Interfaces.IInputListener;
import com.group5.game.Types.Math.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.group5.game.Game;

public class Player extends DrawableEntity implements IInputListener {

    int m_score;
    int m_rewardsCollected;

    public float GetScore() {
        return m_score;
    }
    public void ChangeScore(int value){
        m_score += value;
        // if score is negative then trigger a game over
        if (m_score < 0) {
            m_score = 0;
            Game.GetInstance().SetGameState(GameState.OverLoss);
        }
    }

    public int GetRewardsCollected() {
        return m_rewardsCollected;
    }
    public void IncrementRewards(){
        m_rewardsCollected++;
    }

    public Player(Board parentBoard, Int2 startCoordinates) {
        super(parentBoard, startCoordinates, true, "player.png", new Float2(1f, 1f));
        m_score = 0;
        SetIsBlocking(false);
    }

    @Override
    public boolean SpawnEntity(){
        boolean success = super.SpawnEntity();
        if(success) InputHandler.GetInstance().AddInputListener(this);
        return success;
    }
    @Override
    public boolean RemoveEntity(){
        boolean success = super.RemoveEntity();
        if(success) InputHandler.GetInstance().RemoveInputListener(this);
        return success;
    }

    private Int2 m_targetPosition;
    @Override
    public void OnInputReceived() {
		Int2 oldPos = GetCoordinates();
        
		int x = 0;
		int y = 0;

		if(Gdx.input.isKeyPressed(Input.Keys.D)) x = 1;
		else if(Gdx.input.isKeyPressed(Input.Keys.A)) x = -1;
		else if(Gdx.input.isKeyPressed(Input.Keys.W)) y = 1;
		else if(Gdx.input.isKeyPressed(Input.Keys.S)) y = -1;

		if(x != 0 || y != 0) m_targetPosition = new Int2(oldPos.x + x, oldPos.y + y);
    }

    // this method is only for testing purposes
    public void setTargetPosition(Int2 coordinates) {
        m_targetPosition = coordinates;
    }

    @Override
    public boolean IsActive() {
        return true;
    }

    @Override
    public void OnTick(){
        super.OnTick();
        if(m_targetPosition != null)
            SetCoordinates(m_targetPosition);
    }

    @Override
    public void OnEntityTouch(Entity other) {
        super.OnEntityTouch(other);
        if (other instanceof Gate) {
            Game.GetInstance().SetGameState(GameState.OverWin);
        }
    }

    @Override
    public void OnEntityWasTouched(Entity other) {
        super.OnEntityWasTouched(other);
        if (other instanceof Enemy) {
            Game.GetInstance().SetGameState(GameState.OverLoss);
        }
    }
}
