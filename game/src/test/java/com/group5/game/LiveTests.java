package com.group5.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.group5.game.Interfaces.IRenderable;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for things using the Gdx library (e.g. UI), requiring the use of the Game class, etc.
 */
public class LiveTests {
    
    private static Game game;
    private static HeadlessApplication app;

    @BeforeClass
    public static void initiateHeadless(){
        game = Game.GetInstance();
        HeadlessApplicationConfiguration configuration = new HeadlessApplicationConfiguration();
        configuration.updatesPerSecond = 60; 
        app = new HeadlessApplication(game, configuration);
    }

    @Test
    public void headlessShouldWork(){
        assert(game != null);
        assert(app != null);
    }

    // --------------------------------------------------------------------------------------
    // [Integration Test] Tests initial values, how those values interact with other systems
    // ---------------------------------------------------------------------------------------

    private boolean wait;
    /**
     * Allows the tests to wait for the Gdx context thread.
     * Prevents improper synchronization.
     */
    @Before
    public void AwaitGDX(){
        Gdx.app.postRunnable(new Runnable(){
            @Override
            public void run(){
                wait = false;
            }
        });
        while(wait) {
            try {
                Thread.sleep(10);
            }
            catch(Exception e) { }
        }
    }

    // NOTE: 
    // None of these tests below work correctly!

    // LibGDX stalls indefinitely on the create() method in Game.java, and looking into 
    // it revealed that this had something to do with its calls to OpenGL under the hood.
    // Since we can't create a GL context when unit testing (or at least shouldn't), and
    // HeadlessApplication seems to be the "best" solution for tests, we cannot get a 
    // correct game state to begin testing with in the first place.

    // This effectively makes it impossible to test any runtime behaviour without going in
    // and changing a bunch of things, which does not seem like a good idea this close to the
    // deadline.

    // Realistically speaking, most of the UI reports values directly from Game.java every 
    // frame anyways, so there isn't a major need for it to be integration tested.

    // The only things that could have really benefitted from being int. tested here are the
    // Leaderboard I/O, and which UI panels are open during which state (e.g., win/lose screen
    // during game over, or UI during game active).

    // -Evan
    // P.S.: libGDX exists only to cause pain
    /*
    @Test
    public void shouldChangeScore(){
        game.StartGame();
        game.GetPlayer().ChangeScore(100);
        assert(game.GetScore() == 100);
    }

    @Test
    public void shouldHaveCorrectRenderers(){
        // Renderers: { UI, loseScreen, lBoard, winScreen, startScreen, boardRenderer }
        IRenderable[] renderers = game.GetRenderers();
        
        //Test startup behaviour
        assert(renderers[4].IsActive() == true);

        //Test game start behaviour
        game.StartGame();
        assert(renderers[0].IsActive() == true);
        assert(renderers[1].IsActive() == false);
        assert(renderers[2].IsActive() == true);
    }
    */
}
