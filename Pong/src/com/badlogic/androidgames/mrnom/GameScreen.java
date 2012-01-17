package com.badlogic.androidgames.mrnom;

import java.util.List;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;

import android.app.Activity;

public class GameScreen extends Screen 
{
    enum GameState {
        Ready,
        Running,
        Paused,
        GameOver
    }
    
    private int counter = 0;
    private int tempY = 0;
    
    GameState state = GameState.Ready;
    World world;
    int oldScore = 0;
    String score = "0";
    
    public GameScreen(Game game) 
    {
        super(game);
        World.display = ((Activity) game).getWindowManager().getDefaultDisplay();
        world = new World();
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();      
   
        if(state == GameState.Ready)
            updateReady(touchEvents);
        if(state == GameState.Running)
            updateRunning(touchEvents, deltaTime);
        if(state == GameState.Paused)
            updatePaused(touchEvents);
        if(state == GameState.GameOver)
            updateGameOver(touchEvents);        
    }
    
    private void updateReady(List<TouchEvent> touchEvents) {
        if(touchEvents.size() > 0)
            state = GameState.Running;
    }
    
    
    private void updateRunning(List<TouchEvent> touchEvents, float deltaTime) 
    {
    	 world.update(deltaTime);
    	 counter++;
    	 
    	 int len = touchEvents.size();
         for(int i = 0; i < len; i++) 
         {
        	 TouchEvent event = touchEvents.get(i);
        	 if(event.type == TouchEvent.TOUCH_UP) {
                 if(event.x < 64 && event.y < 64) {
                     if(Settings.soundEnabled)
                         Assets.click.play(1);
                     state = GameState.Paused;
                     return;
                 }
             }
            
             Log.d("MyApp","Touch X: " + event.x);
             Log.d("MyApp","Touch Y: " + event.y);
             tempY=event.y;
         }
         
         world.paddle1.moveToward(tempY-(Assets.paddle1.getHeight()/2),0,410,Paddle.MOVESPEED);
         world.paddle2.moveToward((int)world.ball.y-(Assets.paddle1.getHeight()/2),0,410,Paddle.MOVESPEED);

    }
    
    private void updatePaused(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x > 80 && event.x <= 240) {
                    if(event.y > 100 && event.y <= 148) {
                        if(Settings.soundEnabled)
                            Assets.click.play(1);
                        state = GameState.Running;
                        return;
                    }                    
                    if(event.y > 148 && event.y < 196) {
                        if(Settings.soundEnabled)
                            Assets.click.play(1);
                        game.setScreen(new MainMenuScreen(game));                        
                        return;
                    }
                }
            }
        }
    }
    
    private void updateGameOver(List<TouchEvent> touchEvents) {
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                if(event.x >= 128 && event.x <= 192 &&
                   event.y >= 200 && event.y <= 264) {
                    if(Settings.soundEnabled)
                        Assets.click.play(1);
                    game.setScreen(new MainMenuScreen(game));
                    return;
                }
            }
        }
    }
    

    @Override
    public void present(float deltaTime) 
    {
        Graphics g = game.getGraphics();

        g.clear(Color.BLACK);
        drawWorld(world);
        
        if(state == GameState.Ready) 
            drawReadyUI();
        if(state == GameState.Running)
            drawRunningUI();
        if(state == GameState.Paused)
            drawPausedUI();
        if(state == GameState.GameOver)
            drawGameOverUI();
        
        drawText(g, score, g.getWidth() / 2 - score.length()*20 / 2, g.getHeight() - 42);                
    }
    
    private void drawWorld(World world) 
    {
        Graphics g = game.getGraphics();
        Ball ball = world.ball;
        Paddle paddle1 = world.paddle1;
        Paddle paddle2 = world.paddle2;
        
        Pixmap stainPixmap = null;

        // draw paddles
        g.drawPixmap(Assets.paddle1, paddle1.x, paddle1.y);
        g.drawPixmap(Assets.paddle2, paddle2.x, paddle2.y);
        
        // draw ball
        g.drawPixmap(Assets.ball, (int)ball.x, (int)ball.y);
        
        Pixmap headPixmap = null;
      
    }
    
    private void drawReadyUI() 
    {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.ready, 47, 100);
    }
    
    private void drawPausedUI() 
    {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.pause, 80, 100);
    }
    
    private void drawRunningUI() 
    {
        Graphics g = game.getGraphics();

        g.drawPixmap(Assets.buttons, 0, 0, 64, 128, 64, 64);
       
    }
    

    private void drawGameOverUI() {
        Graphics g = game.getGraphics();
        
        g.drawPixmap(Assets.gameOver, 62, 100);
        g.drawPixmap(Assets.buttons, 128, 200, 0, 128, 64, 64);

    }
    
    public void drawText(Graphics g, String line, int x, int y) {
        int len = line.length();
        for (int i = 0; i < len; i++) {
            char character = line.charAt(i);

            if (character == ' ') {
                x += 20;
                continue;
            }

            int srcX = 0;
            int srcWidth = 0;
            if (character == '.') {
                srcX = 200;
                srcWidth = 10;
            } else {
                srcX = (character - '0') * 20;
                srcWidth = 20;
            }

            g.drawPixmap(Assets.numbers, x, y, srcX, 0, srcWidth, 32);
            x += srcWidth;
        }
    }
    
    @Override
    public void pause() {
        if(state == GameState.Running)
            state = GameState.Paused;
        
        if(world.gameOver) {
            Settings.addScore(world.score);
            Settings.save(game.getFileIO());
        }
    }

    @Override
    public void resume() {
        
    }

    @Override
    public void dispose() {
        
    }
}