package com.group5.game.Types.Rendering;

import com.group5.game.Game;
import com.group5.game.GameState;
import com.group5.game.Interfaces.IRenderable;
import com.group5.game.Types.Game.Board;
import com.group5.game.Types.Game.DrawableEntity;
import com.group5.game.Types.Game.Entity;
import com.group5.game.Types.Math.Float2;
import com.group5.game.Types.Math.Int2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;

import java.util.HashMap;

/**
 * A renderer which draws a subset of a Grid each frame.
 * <p>
 * As an example, suppose the Board is a 100x100 board.
 * If the DrawCorner is set to (40,40), and the TileDrawDiameter is 10, this renderer will draw between tile [40, 40], and (50, 50).
 * <p>
 * Only Entities inheriting from DrawableEntity will be drawn. This may be subject to change, but I kept Entity and DrawableEntity seperate for the time being.
 * @author Evan Sarkozi
 */
public class BoardRenderer implements IRenderable {
    
    private static HashMap<String, Texture> s_textureLookup = new HashMap<String, Texture>();
    /**
     * Gets a Texture from a specified path, searching from a cached lookup.
     * @param path The path of the Texture
     * @return The Texture at the path.
     */
    public static Texture GetTexture(String path){
        if(s_textureLookup.containsKey(path))
            return s_textureLookup.get(path);
        else{
            Texture texture = new Texture(Gdx.files.internal(path));
            s_textureLookup.put(path, texture);
            return texture;
        }
    }

    private Int2 m_screenSize;

    private int m_tileDrawDiameter;
    private float m_screenFraction;
    private Board m_board;


    private Int2 m_drawCorner;
    /**
     * Sets the bottom-most corner to be drawn in the grid
     * @param corner The coordinate.
     */
    public void SetDrawCorner(Int2 corner) {
        m_drawCorner = corner;
    }
    /**
     * Retrieves the bottom-most corner to be drawn in the grid
     * @return The coordinate.
     */
    public Int2 GetDrawCorner(){
        return m_drawCorner;
    }

    /**
     * Sets the Tile Draw Diameter.
     * @param tiles The diameter.
     */
    public void SetTileDrawDiameter(int tiles){
        m_tileDrawDiameter = tiles;
    }

    /**
     * Retrieves the Tile Draw Diameter.
     * @return The diameter.
     */
    public int GetTileDrawDiameter(){
        return m_tileDrawDiameter;
    }

    /**
     * Sets the Screen Fraction.
     * @param fraction The Fraction.
     */
    public void SetScreenFraction(float fraction){
        m_screenFraction = fraction;
    }
    /**
     * Retrieves the Screen Fraction.
     * @return The Fraction.
     */
    public float GetScreenFraction(){
        return m_screenFraction;
    }

    /**
     * Retrieves the target board
     * @return The board
     */
    public Board GetBoard(){
        return m_board;
    }
    /**
     * Sets the target board
     * @param board The board
     */
    public void SetBoard(Board board){
        m_board = board;
    }

    /**
     * Constructs a GridRenderer
     * @param tileDrawDiameter The amount of Grid-Tiles on screen.
     * @param screenFraction A fraction representing how much of the screen the Grid will take up (1 takes as much as possible, 0.5 takes up half the screen, etc.).
     * @param target The target Board to render.
     */
    public BoardRenderer(int tileDrawDiameter, float screenFraction, Board target){
        m_tileDrawDiameter = tileDrawDiameter;
        m_screenFraction = screenFraction;
        m_board = target;
        m_drawCorner = new Int2(0,0);
    }

    @Override
    public boolean IsActive() {
        GameState state = Game.GetInstance().GetGameState();
        return (state == GameState.Active || state == GameState.Paused) && m_board != null;
    }

    @Override
    public void Render(SpriteBatch batch) {
        
        //Determine size of grid, locate bottom left corner for draws
        float dimension = Math.min(m_screenSize.y * m_screenFraction, m_screenSize.x * m_screenFraction);
        float sizePerTile = dimension / m_tileDrawDiameter;
        Int2 pxCornerBase = new Int2(m_screenSize.x / 2 - ((int)dimension / 2),  m_screenSize.y / 2 - ((int)dimension / 2));

        //Iterate through visible tiles
        for(int x = 0; x < m_tileDrawDiameter; x++){
            for(int y = 0; y < m_tileDrawDiameter; y++){

                //Get local pixel corner
                Int2 pxCornerTile = new Int2(pxCornerBase.x + (int)sizePerTile * x, pxCornerBase.y + (int)sizePerTile * y);
                
                //Draw grid base
                batch.draw(GetTexture("grid.png"), pxCornerTile.x, pxCornerTile.y, (int)sizePerTile, (int)sizePerTile);

                //Iterate through entities on the tile
                for(Entity entity : m_board.GetEntitiesAt(new Int2(m_drawCorner.x + x, m_drawCorner.y + y))){

                    //Draw if a drawable object and is currently spawned
                    if(entity.GetIsSpawned() && entity instanceof DrawableEntity){
                        DrawableEntity drawable = (DrawableEntity)entity;
                        
                        //Get drawing scale ([1,1] takes up the entire tile for reference)
                        Float2 scale = drawable.GetDrawScale();

                        //Calculate the offset in pixels from the corner of the grid tile
                        Int2 padding = new Int2((int)(sizePerTile * (1 - scale.x) / 2f), (int)(sizePerTile * (1 - scale.y) / 2f));

                        //Calculate the size in pixels of the object
                        Int2 size = new Int2((int)(sizePerTile * scale.x), (int)(sizePerTile * scale.y));

                        //Calculate the overall pixel corner (tile + offset)
                        Int2 pxCornerEntity = new Int2(pxCornerTile.x + padding.x, pxCornerTile.y + padding.y);

                        //Get the texture
                        Texture texture = GetTexture(drawable.GetTexturePath());

                        //Finally, submit draw call
                        batch.draw(texture, pxCornerEntity.x, pxCornerEntity.y, size.x, size.y);
                    }

                }
            }
        }
    }

    @Override
    public void OnResize(Int2 screenDimensions){
        m_screenSize = screenDimensions;
    }

    @Override
    public boolean UsesOwnBatch(){ return false; }
}
