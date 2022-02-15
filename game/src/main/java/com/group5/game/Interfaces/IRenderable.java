package com.group5.game.Interfaces;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.group5.game.Types.Math.Int2;

/**
 * An abstracted interface for rendering.
 * @author Evan Sarkozi
 */
public interface IRenderable {
    boolean IsActive();
    boolean UsesOwnBatch();
    void Render(SpriteBatch batch);
    void OnResize(Int2 screenDimensions);
}