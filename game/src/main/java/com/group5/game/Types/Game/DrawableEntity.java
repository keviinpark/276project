package com.group5.game.Types.Game;

import com.group5.game.Types.Math.Float2;
import com.group5.game.Types.Math.Int2;

public class DrawableEntity extends Entity {

    private Float2 m_drawScale;
    private String m_texturePath;

    public Float2 GetDrawScale() {
        return m_drawScale;
    }

    public String GetTexturePath(){
        return m_texturePath;
    }
    public void SetTexturePath(String path){
        m_texturePath = path;
    }

    public DrawableEntity(Board parentBoard, Int2 startCoordinates, boolean isBlocking, String texturePath, Float2 drawScale) {
        super(parentBoard, startCoordinates, isBlocking);
        m_drawScale = drawScale;
        m_texturePath = texturePath;
    }
}
