package com.group5.game.Types.Game;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import com.group5.game.Miscellaneous.AI;
import com.group5.game.Types.Math.Int2;


/**
 * <b>General</b>
 * <p>
 * A class to represent the Game board. Utilizes a HashMap of Vectors to represent a limitless amount of entities per tile, automatically tracking 
 * movement of bound Entities and invoking Events when an Entity enters a pre-occupied tile.
 * <p>
 * <b>Remarks</b>:
 * <p>
 * I know we discussed using a simple array for the board, but looking at the requirements document, I saw that there are points where multiple entities can be on the same tile
 * (e.g., a reward and an enemy). Using a Vector to store entities on a tile seems like the cleanest solution.
 * <p>
 * The HashMap was used for implementation partly because it scales better with a larger map, but mainly because you cannot initialize an array of Entity Vectors (I don't know why!).
 * <p>
 * There are a fair amount of duplicate checks being run in some of these operations, and plenty of things that could be optimized. It's probably fine for the time being though.
 * @author Evan Sarkozi
 */
public class Board {

    private Int2 m_boardDimensions;
    private HashMap<Int2, Vector<Entity>> m_entityMap;

    private Vector<Entity> m_emptyList;

    /**
     * Constructs a Board.
     * @param width The width of the board.
     * @param height The height of the board.
     */
    public Board(int width, int height){
        //Sets board dimensions
        m_boardDimensions = new Int2(width, height);
        m_entityMap = new HashMap<Int2, Vector<Entity>>();
        m_emptyList = new Vector<Entity>();
    }

    /**
     * Gets the dimensions of the board.
     * @return An Int2 containing (width, height)
     */
    public Int2 GetBoardDimensions(){
        return m_boardDimensions;
    }


    /**
     * Checks if a coordinate is within the board bounds.
     * @param coordinate The coordinate to check.
     * @return True if the coordinates are greater than zero and within the width and height.
     */
    public boolean WithinBounds(Int2 coordinate){
        int x = coordinate.x;
        int y = coordinate.y;
        return x >= 0 && x < m_boardDimensions.x && y >= 0 && y < m_boardDimensions.y;
    }

    /**
     * Checks if a coordinate is available to place an entity.
     * @param coordinate The coordinate to check.
     * @return True if within bounds, and no blocking entities are at the coordinate; false otherwise.
     */
    public boolean SpaceAvailable(Int2 coordinate){
        if(WithinBounds(coordinate)){
            //Check if any entities at the coordinate are blocking
            Vector<Entity> entitiesAt = GetEntitiesAt(coordinate);
            for(Entity e : entitiesAt){
                if(e.GetIsBlocking()) return false;
            }
            return true;
        }
        else return false;
    }

    /**
     * Gets the list of entities at the given coordinate.
     * @param coordinate The coordinate being queried.
     * @return A list of entities at these coordinates, or an empty list otherwise.
     */
    public Vector<Entity> GetEntitiesAt(Int2 coordinate) {
        if(WithinBounds(coordinate) && m_entityMap.containsKey(coordinate)){
            return m_entityMap.get(coordinate);
        }
        else return m_emptyList;
    }

    public boolean IsEmpty(Int2 coordinate){
        return GetEntitiesAt(coordinate).size() == 0;
    }

    /**
     * Try to place an Entity at a given coordinate
     * @param entity The entity to place.
     * @param coordinate The new coordinate (x,y) to place the entity at.
     * @return True if the entity was successfully placed, false otherwise.
     */
    public boolean PlaceEntityAt(Entity entity, Int2 coordinate){
        if(WithinBounds(coordinate)){
            
            //Add new entry to map if necessary
            if(!m_entityMap.containsKey(coordinate))
                m_entityMap.put(coordinate, new Vector<Entity>());

            //Check if space is available
            if(SpaceAvailable(coordinate)){

                //Place the object in the new space
                m_entityMap.get(coordinate).add(entity);

                //Invoke collision event(s)
                //(Uses a list copy to prevent collection modified exceptions)
                //Note: Events are triggered for both entities in the pair -- this allows for: 
                // -Touchers to affect touchees (e.g., an enemy touching the player)
                // -Touchees to affect touchers (e.g., a reward being touched by the player)
                Vector<Entity> listCopy = new Vector<Entity>(GetEntitiesAt(coordinate));
                for(Entity e : listCopy){
                    entity.OnEntityTouch(e);
                    e.OnEntityWasTouched(entity);
                }
                return true;
                
            }
            else return false;
        }
        else return false;
    }

    /**
     * Try to place an entity at its currently set coordinate.
     * @param entity The entity to place.
     * @return True if the entity was successfully placed, false otherwise.
     */
    public boolean PlaceEntity(Entity entity){
        return PlaceEntityAt(entity, entity.GetCoordinates());
    }

    /**
     * Try to remove an entity from its currently set coordinate.
     * @param entity The entity to remove.
     * @return True if the entity was successfully removed, false otherwise.
     */
    public boolean RemoveEntity(Entity entity){

        //Get position
        Int2 coordinate = entity.GetCoordinates();

        //Check bounds, then check if any entities at position
        if(WithinBounds(coordinate) && m_entityMap.containsKey(coordinate)){

            //Search for entity, remove and return true if found
            Vector<Entity> entityList = GetEntitiesAt(coordinate);
            if(entityList.contains(entity)){
                entityList.remove(entity);
                return true;
            }
            else return false;

        }
        else return false;
    }

    /**
     * Remove all entities at a coordinate
     * @param coordinate The coordinate to remove all entities at.
     * @return True if any entities were removed at this coordinate, false otherwise.
     */
    public boolean RemoveAllEntitiesAt(Int2 coordinate){

        //Try to remove list at coordinate
        if(WithinBounds(coordinate)){
            return m_entityMap.remove(coordinate) != null;
        }
        else return false;
        
    }

    /**
     * Attempts to move an entity from a previous position to a new one.
     * @param entity The entity to move.
     * @param newPosition The new position of the entity.
     * @return True if the entity was already on the board and successfully could move, false otherwise.
     */
    public boolean MoveEntity(Entity entity, Int2 newPosition) {
        //Check bounds first
        if(!SpaceAvailable(newPosition)) return false;

        //Next, try to remove, then, try to replace
        return RemoveEntity(entity) && PlaceEntityAt(entity, newPosition); 
    }

    /**
     * Gets a random empty spot on this board
     * @return A random Int2 representing the spot.
     */
    public Int2 RandomEmptySpot(Int2 min, Int2 max) {

        Random random = new Random();

        //Fill a list of candidates in random order
        Vector<Int2> candidates = new Vector<Int2>();
        for(int x = min.x; x <= max.x; x++) {
            for(int y = min.y; y <= max.y; y++)
            {
                int index = candidates.size() > 1 ? random.nextInt(candidates.size() - 1) : 0;
                candidates.add(index, new Int2(x,y));
            }
        }
        
        //Return the first successful candidate
        for(Int2 candidate : candidates)
            if(IsEmpty(candidate)) return candidate;

        //Could not find any spot
        return null;
    }

    /**
     * Gets a random empty spot on this board that is reachable from the given coordinate
     * @return A random Int2 representing the spot.
     */
    public Int2 RandomReachableEmptySpot(Int2 min, Int2 max, Int2 reachableFrom, int minDistance) {
        Random random = new Random();

        //Fill a list of candidates in random order
        Vector<Int2> candidates = new Vector<Int2>();
        for(int x = min.x; x <= max.x; x++) {
            for(int y = min.y; y <= max.y; y++)
            {
                int index = candidates.size() > 1 ? random.nextInt(candidates.size() - 1) : 0;
                candidates.add(index, new Int2(x,y));
            }
        }
        
        boolean[][] collisionMap = new boolean[m_boardDimensions.x][m_boardDimensions.y];
        for(int x = 0; x < collisionMap.length; x++){
            collisionMap[x] = new boolean[m_boardDimensions.y];

            for(int y = 0; y < collisionMap[x].length; y++)
                collisionMap[x][y] = IsEmpty(new Int2(x,y));
        }

        //Return the first successful candidate
        for(Int2 candidate : candidates){
            if(IsEmpty(candidate)){
                //Fulfills minimum distance
                if(Math.abs(reachableFrom.x - candidate.x) >= minDistance && Math.abs(reachableFrom.y - candidate.y) >= minDistance){
                    //Fulfills reachable condition
                    if(AI.Pathfind(collisionMap, reachableFrom, candidate) != null){
                        return candidate;
                    }
                }
            }
        }
        //Could not find any spot
        return null;
    }

    /**
     * Invoke a Game Tick on all objects registered with the Board.
     */
    public void Tick(){
        
        //Create a temporary list of Entities to iterate over
        //This is to prevent collection modification exceptions if entities tick behaviour involves modifying the board.
        Entity playerEntity = null;
        Vector<Entity> entities = new Vector<Entity>();
        for(Vector<Entity> ents : m_entityMap.values())
            for(Entity ent : ents){
                if(ent instanceof Player) playerEntity = ent;
                else entities.add(ent);
            }
            
        //Always give priority to the player entity!
        //This allows the player to make narrow escapes, which is more fun
        if(playerEntity != null) {
            playerEntity.OnTick();
        }
        //Now iterate through
        for(Entity ent : entities)
            ent.OnTick();
    }
}
