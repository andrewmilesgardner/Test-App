package com.badlogic.androidgames.mrnom;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.badlogic.androidgames.mrnom.World;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class PongGame extends AndroidGame 
{
    @Override
    public Screen getStartScreen() 
    {

        return new LoadingScreen(this); 
    }
    
}