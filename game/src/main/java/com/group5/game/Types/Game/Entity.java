package com.group5.game.Types.Game;
import com.group5.game.Types.Math.Int2;

/**
 * The class from which grid-based Entities must be derived.
 * <p>
 * When constructing an Entity, simply provide it an initial position, a parent Board, and an isBlocking value (which determines if other entities can enter the same tile).
 * <p>
 * The Entity will only be on the Board once SpawnEntity() is invoked, and taken off the Board when RemoveEntity() is invoked. When spawning, despawning, and moving an Entity,
 * these updates are automatically tracked on the Board through the SetCoordinates function of the base Entity class. If overriding the setter, ensure the base function is called!
 * <p>
 * @author Evan Sarkozi
 */
public abstract class Entity {
    
    private Board m_parentBoard;
    private Int2 m_coordinate;
    private boolean m_isBlocking;

    private boolean m_isSpawned;

    /**
     * Gets the x,y coordinates of the entity.
     * @return Coordinates of the entity.
     */
    public Int2 GetCoordinates(){
        return m_coordinate;
    }
    /**
     * Sets the x,y coordinates of the entity.
     * @param coordinate The target new coordinates of the entity.
     * @return If not spawned, will always return true. If spawned, will only return true if successfully moved to the new position, and false otherwise.
     */
    public boolean SetCoordinates(Int2 coordinate){
        //Always allow position update if not yet spawned
        if(!m_isSpawned){
            m_coordinate = coordinate;
            return true;
        }
        //If spawned, attempt to move
        else if(m_parentBoard.MoveEntity(this, coordinate)){
            m_coordinate = coordinate;
            return true;
        }
        else return false;
    }
    
    /**
     * Sets the IsBlocking parameter
     * @param value The new value of IsBlocking.
     */
    public void SetIsBlocking(boolean value) { m_isBlocking = value; }
    /**
     * Gets the IsBlocking parameter
     * @return The value of IsBlocking.
     */
    public boolean GetIsBlocking() { return m_isBlocking; }

    /**
     * Checks whether the entity is spawned or not.
     * @return True if spawned, false otherwise.
     */
    public boolean GetIsSpawned(){
        return m_isSpawned;
    }

    public Board GetParentBoard(){
        return m_parentBoard;
    }

    /**
     * Spawns the entity on the parent board.
     * @return True if the entity was successfully spawned, false otherwise.
     */
    public boolean SpawnEntity() 
    {
        if(!m_isSpawned){
            m_isSpawned = m_parentBoard.PlaceEntity(this);
            return m_isSpawned;
        }
        else return false;
    }
    /**
     * Despawns the entity on the parent board.
     * @return True if the entity was successfully removed, false otherwise.
     */
    public boolean RemoveEntity() 
    {
        if(m_isSpawned){
            m_isSpawned = !m_parentBoard.RemoveEntity(this);
            return !m_isSpawned;
        }
        else return false;
    }

    /**
     * Invoked when this entity touches another entity (is the instigator).
     * @param other The other entity.
    */
    public void OnEntityTouch(Entity other) {    }
    /**
     * Invoked when entity is touched by another entity (other entity is instigator).
     * @param other The other entity.
     */
    public void OnEntityWasTouched(Entity other) {   }

    /**
     * Invoked when a game tick occurs.
     */
    public void OnTick() {  }

    /**
     * Instantiates a new entity.
     * @param parentBoard The board this Entity is to be placed on.
     * @param startCoordinates The initial coordinates of the Entity.
     * @param isBlocking The initial value of isBlocking
     */
    public Entity(Board parentBoard, Int2 startCoordinates, boolean isBlocking){
        m_parentBoard = parentBoard;
        SetCoordinates(startCoordinates);
        SetIsBlocking(isBlocking);
    }
}
