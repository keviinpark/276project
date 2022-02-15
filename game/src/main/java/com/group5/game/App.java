package com.group5.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import java.io.*;

public class App 
{
    public static void main( String[] args ) throws FileNotFoundException
    {
		  LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		  new LwjglApplication(Game.GetInstance(), config);
    }
} 
