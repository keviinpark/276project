package com.group5.game.Handlers;

import com.group5.game.Interfaces.IRenderable;
import com.group5.game.Types.Math.Int2;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Vector;

/**
 * Handles drawing to the screen, abstracting the process through the use of the interface IRenderable.
 * @author Evan Sarkozi
 */
public class RenderHandler {
    private RenderHandler(){ }
    private static RenderHandler s_singleton;
    public static RenderHandler GetInstance(){
        if(s_singleton == null)
            s_singleton = new RenderHandler();
        return s_singleton;
    }

    private Vector<IRenderable> m_renderers = new Vector<IRenderable>();
    
    private SpriteBatch m_spriteBatch = new SpriteBatch();
    private OrthographicCamera m_camera = new OrthographicCamera();

    private Int2 m_screenDimensions;
    public Int2 GetScreenDimensions(){
        return m_screenDimensions;
    }

    /**
     * Adds a renderer to the drawing list.
     * @param renderer The renderer to be added.
     * @return True if the renderer was successfully added, false if unsuccessful or the renderer was already in the list.
     */
    public boolean AddRenderable(IRenderable renderer){
            if(!m_renderers.contains(renderer)){
                m_renderers.add(renderer);
                return true;
            }
            else return false;
    }

    /**
     * Removes a renderer from the drawing list.
     * @param renderer The renderer to be removed.
     * @return True if the renderer was successfully removed, false if unsuccessful or the renderer was not in the list.
     */
    public boolean RemoveRenderable(IRenderable renderer){
        if(m_renderers.contains(renderer)){
            m_renderers.remove(renderer);
            return true;
        }
        else return false;
    }

    public Camera GetCamera(){ 
        return m_camera; 
    }

    /**
     * Flushes the screen, then invokes Render calls on all active Renderables, passing a SpriteBatch to draw into depending on implementation. Upon querying Renderables, all commands queued in the SpriteBatch are submitted and shown on screen.
    */
    public void Render() {

        //Flushes drawing context, and begins SpriteBatch
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        //Iterates through bound renderers that use a supplied batch
        m_spriteBatch.begin();
        for(IRenderable renderer : m_renderers){
            if(renderer.IsActive()){
                if(!renderer.UsesOwnBatch()) renderer.Render(m_spriteBatch);
            }
        }
        m_spriteBatch.end();

        //Iterates through UI renderers that 
        for(IRenderable renderer : m_renderers){
            if(renderer.IsActive()){
                if(renderer.UsesOwnBatch()) renderer.Render(null);
            }
        }
    }

    public void ResizeScreen(){
        m_camera.setToOrtho(false);
        m_spriteBatch.setProjectionMatrix(m_camera.combined);
        m_screenDimensions = new Int2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        for(IRenderable renderer : m_renderers){
            renderer.OnResize(m_screenDimensions);
        }
    }
}
