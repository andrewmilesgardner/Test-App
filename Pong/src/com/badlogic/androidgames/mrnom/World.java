package com.badlogic.androidgames.mrnom;
import android.util.Log;
import android.view.Display;
import java.util.Random;

import com.badlogic.androidgames.framework.math.OverlapTester;
import com.badlogic.androidgames.framework.math.Rectangle;

public class World {
    static final int WORLD_WIDTH = 10;
    static final int WORLD_HEIGHT = 13;
    static final int SCORE_INCREMENT = 10;
    static final float TICK_INITIAL = 0.05f;

    public Paddle paddle1, paddle2;
    public Ball ball;
    public boolean gameOver = false;
    public int score = 0;
	public boolean turn = false;
	
    static Display display;

    boolean fields[][] = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
    Random random = new Random();
    float tickTime = 0;
    static float tick = TICK_INITIAL;

    public World() 
    {
       
        paddle1 = new Paddle(0,(440/2)-(Assets.paddle1.getHeight()/2));
        paddle2 = new Paddle(320 /*display.getWidth()*/-Assets.paddle1.getWidth(),(440/2)-(Assets.paddle1.getHeight()/2));
        ball = new Ball((320/2)-(Assets.ball.getWidth()/2),(440/2)-(Assets.ball.getHeight()/2));
        turn = ball.release();
    }
    
    public boolean checkPaddleBallCollision() 
    {
    	 Rectangle ballRect = new Rectangle(ball.x, ball.y, Assets.ball.getWidth(), Assets.ball.getHeight());
         Rectangle paddle2Rect = new Rectangle(paddle2.x, paddle2.y, Assets.paddle2.getWidth(), Assets.paddle2.getHeight());
         Rectangle paddle1Rect = new Rectangle(paddle1.x, paddle1.y, Assets.paddle1.getWidth(), Assets.paddle1.getHeight());
         if ((!turn) && (OverlapTester.overlapRectangles(ballRect, paddle2Rect)))
         {
         	Log.d("MyApp","OVERLAP: ");
         	ball.collision(1,paddle2.x-Assets.ball.getWidth(),paddle2.y+Assets.paddle2.getHeight()/2);
         	turn=true;
         	return true;
         }
         if ((turn) && (OverlapTester.overlapRectangles(ballRect, paddle1Rect)))
         {
         	Log.d("MyApp","OVERLAP: ");
         	ball.collision(1,paddle1.x+Assets.paddle1.getWidth(),paddle1.y+Assets.paddle1.getHeight()/2);
         	turn=false;
         	return true;
         }
         return false;
    }
    
    public boolean checkBallWallCollision() 
    {
         if (ball.y < 0)
         {
         	ball.collision(2,0,0);
         	return true;
         }
         else if (ball.y+Assets.ball.getHeight() > 480)
         {
         	ball.collision(2,480-Assets.ball.getHeight(),0);
         	return true;
         }
        
         if (ball.x < (0 - Assets.ball.getWidth()))
         {
        	 ball.collision(3,(320/2)-(Assets.ball.getWidth()/2),(440/2)-(Assets.ball.getHeight()/2));
        	 turn = ball.release();
        	 return true;
         }
         else if (ball.x > 320 +  Assets.ball.getWidth())
         {
        	 ball.collision(3,(320/2)-(Assets.ball.getWidth()/2),(440/2)-(Assets.ball.getHeight()/2));
        	 turn = ball.release();
        	 return true;
         }
         return false;
    }

    public void update(float deltaTime) 
    {
        if (gameOver)
            return;

        tickTime += deltaTime;

        while (tickTime > tick) 
        {
            tickTime -= tick;
            
            paddle1.update(deltaTime);
            paddle2.update(deltaTime);
            ball.update(deltaTime);
            
            checkPaddleBallCollision();
            checkBallWallCollision();
  
        }
    }
}
