package com.group5.game.Handlers;

import com.group5.game.Interfaces.*;
import java.util.Vector;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

import com.badlogic.gdx.Gdx;

public class InputHandler implements InputProcessor {
    
    InputMultiplexer m_multiplexer;
    private InputHandler() 
    {
        //Create multiplexer
        m_multiplexer = new InputMultiplexer();
        m_multiplexer.addProcessor(this);

        //Bind input handler
		if(Gdx.input != null) Gdx.input.setInputProcessor(m_multiplexer);
    }
    private static InputHandler s_singleton;
    public static InputHandler GetInstance(){
        if(s_singleton == null)
            s_singleton = new InputHandler();
        return s_singleton;
    }

    private Vector<IInputListener> m_inputListener = new Vector<IInputListener>();
    
    /**
     * Adds a listener which will be called on event inputs
     * @param listener The Listener to be added.
     * @return True if the Listener was successfully added, false if unsuccessful or the Listener was already in the list.
     */
    public boolean AddInputListener(IInputListener listener){
            if(!m_inputListener.contains(listener)){
                m_inputListener.add(listener);
                return true;
            }
            else return false;
    }

    /**
     * Removes a listener from the list.
     * @param listener The listener to be removed.
     * @return True if the listener was successfully removed, false if unsuccessful or the listener was not in the list.
     */
    public boolean RemoveInputListener(IInputListener listener){
        if(!m_inputListener.contains(listener)){
            m_inputListener.remove(listener);
            return true;
        }
        else return false;
    }

    /**
     * Add an input processor aside from the main one (for UI and similar)
     */
    public boolean AddInputProcessor(InputProcessor processor){
        m_multiplexer.addProcessor(processor);
        return true;
    }
    /**
     * Remove an input processor aside from the main one
     */
    public boolean RemoveInputProcessor(InputProcessor processor){
        m_multiplexer.removeProcessor(processor);
        return true;
    }

    /**
     * Invokes OnInputReceived to all listeners
    */
    public void BroadcastInputReceived() {
        //Iterates through bound listeners
        for(IInputListener renderer : m_inputListener){
            if(renderer.IsActive());
                renderer.OnInputReceived();
        }
    }


    @Override
    public boolean keyDown(int keycode) {
        BroadcastInputReceived();
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        BroadcastInputReceived();
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        BroadcastInputReceived();
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        BroadcastInputReceived();
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        BroadcastInputReceived();
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        BroadcastInputReceived();
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) { 
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        BroadcastInputReceived();
        return false;
    }
}
