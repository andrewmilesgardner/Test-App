package com.badlogic.androidgames.mrnom;

import java.util.Random;
import com.badlogic.androidgames.framework.math.Vector2;

import android.util.Log;

public class Ball 
{
	private static final float MOVESPEED = 12;
	public float x, y;
	private Vector2 vec = new Vector2(0,0);

	//public int cover;

	public Ball(float x, float y) 
	{
		this.x = x;
		this.y = y;
		vec = new Vector2(0,0);
	}
	
	public boolean release() 
	{
		boolean turn = true;
		int min = 1;
		int max = 2;

		Random r = new Random();
		int i1 = r.nextInt(max - min + 1) + min;

		if(i1==2) 
		{
			vec.y = 2;
		}
		else 
		{
			vec.y = -2;
		}
		r = new Random();
		i1 = r.nextInt(max - min + 1) + min;
		if(i1==2) 
		{
			vec.x = -MOVESPEED;
		}
		else 
		{
			vec.x = MOVESPEED;
			turn=false;
		}
		return turn;
	}
	
	public void calculateAngle(int pos) 
	{
		if (pos > 25)
		{
			if (vec.y < 0) vec.y = -8;
			else vec.y += 8;
		}
		else if (pos > 15) 
		{
			if (vec.y < 0) vec.y = -4;
			else vec.y += 4;
		}
		else if (pos > 5)
		{
			if (vec.y < 0) vec.y = -2;
			else vec.y += 2;
		}
		else if (pos > 0)
		{
			if (vec.y < 0) vec.y = -1;
			else vec.y += 1;
		}
		else if (pos < -25)
		{
			if (vec.y < 0) vec.y = -8;
			else vec.y += 8;
		}
		else if (pos < -15)
		{
			if (vec.y < 0) vec.y = -4;
			else vec.y += 4;
		}
		else if (pos < -5)
		{
			if (vec.y < 0) vec.y = -2;
			else vec.y += 2;
		}
		else if (pos < 0)
		{
			if (vec.y < 0) vec.y = -1;
			else vec.y += 1;
		}
		else 
		{
			if (vec.y < 0) vec.y = 0;
			else vec.y += 8;
		}
		
		float angle = vec.angle();
		vec.x = (float)Math.cos(angle* Vector2.TO_RADIANS) * 10;
		vec.y = (float)Math.sin(angle* Vector2.TO_RADIANS) * 10;
	}

	//
	public boolean collision(int type, int point, int paddlePos) 
	{
		switch (type) 
		{
		case 1:
			this.x=point;
			vec.x =- vec.x;
			
			// calculate y val based on paddlePos
			int batPos = paddlePos-((int)this.y+Assets.ball.getHeight()/2);
			calculateAngle(batPos) ;

			return true;
		case 2:
			this.y=point;
			vec.y =- vec.y;
			return true;
		case 3:
			this.x=point;
			this.y=paddlePos;
			
			return true;

		}
		return false;		
	}

	// moves the ball sprite
	public void update(float deltaTime) 
	{
		this.x += vec.x /** deltaTime */;
		this.y += vec.y /** deltaTime */;
	}

}