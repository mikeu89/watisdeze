package com.example.watisditappv12;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class SplashScreenActivity extends Activity {
	Context context = (Context) this;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		int rotation = this.getResources().getConfiguration().orientation;
		//landscape
		if(rotation == 2)
		{
			setContentView(R.layout.activity_splash_screen_landscape);
		}
		//default portrait (1)
		else
		{
			setContentView(R.layout.activity_splash_screen_portrait);
		}

		autoContinue();		
		
		LinearLayout mainview = (LinearLayout) findViewById(R.id.splashscreenlayout);
		mainview.setOnClickListener(new OnClickListener() {

		            @Override
		            public void onClick(View v) {
		            	Intent mainActivityIntent = new Intent(context, PlayGameActivity.class);
		      	      startActivity(mainActivityIntent);               
		            }
		        });
	}

public void autoContinue()
{

	final Handler handler = new Handler();
	handler.postDelayed(new Runnable() {
	  @Override
	  public void run() {
		
		  Intent mainActivityIntent = new Intent(context, PlayGameActivity.class);
	      startActivity(mainActivityIntent);
	  
	}}, 1500);	
}

}


