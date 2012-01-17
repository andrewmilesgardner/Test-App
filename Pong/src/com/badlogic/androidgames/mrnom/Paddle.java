package com.badlogic.androidgames.mrnom;

public class Paddle 
{
	public int x, y;
	public int dx = 0;
	public int dy = 0;
	public static final int MOVESPEED = 20;
	
	public int cover;
	
	public Paddle(int x, int y) 
	{
        this.x = x;
        this.y = y;
    }
	
	// moves the sprite towards the y position
	public void moveToward(int y,int upperBounds,int lowerBounds, int speed)
	{
		if(y<upperBounds) 
		{
			this.y=upperBounds;
			return;
		}
		else if (y>lowerBounds) 
		{
			this.y=lowerBounds;
			return;
		}
		
		if ((y > (this.y-speed)) && (y < (this.y+speed)))
		{
			this.y=y;
			dy=0;
			return;
		}
		if (y > this.y) this.dy = speed;
		else if (y < this.y) this.dy = -speed;
		
	}
	
	// moves the ball sprite
	public void update(float deltaTime)
	{
        this.x += dx; //* deltaTime;
        this.y += dy; //* deltaTime;
    }
	
	

}
