package com.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HelloWorldActivity extends Activity 
                                implements View.OnClickListener {
    Button button;
    int touchCount;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        button = new Button(this);
        button.setText( "Click me!" );
        button.setOnClickListener(this);
        setContentView(button);
    }

    public void onClick(View v) 
    {
        touchCount++;
        button.setText("Click count "+touchCount);
    }
    
}