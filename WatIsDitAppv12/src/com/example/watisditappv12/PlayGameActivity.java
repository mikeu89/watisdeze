package com.example.watisditappv12;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PlayGameActivity extends Activity implements OnClickListener{

	MySQLiteHelper db = new MySQLiteHelper(this);
	
	//ui element
	private LinearLayout layout;
	private LinearLayout fourbuttonLayout;
	private LinearLayout fourbuttonLayout2;
	private Button button;
	private Button answerbutton1;
	private Button answerbutton2;
	private Button answerbutton3;
	private Button answerbutton4;
	
	//device rotation
	private int rotation;
	
	//plaatjes en antwoorden
	private String image_url;
	private String answer;
	private String possibility1;
	private String possibility2;
	private String possibility3;
	private String possibility4;
	private String correctanswer;
	
	//timer en score
	private int timerstartvalue;
	private int timeleft;

	private static int currentStreak;
	private int questionAnswering;
	
	//gevuld vanuit de database online
	public static String id, vraagfoto;
	private static JSONArray quizQuestions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rotation = this.getResources().getConfiguration().orientation;
		
		//landscape
		if(rotation == 2)
		{
			setContentView(R.layout.play_game_activity_landscape);
		}
		//default portrait (1)
		else
		{
			setContentView(R.layout.play_game_activity_portrait);
			fourbuttonLayout2 = (LinearLayout) findViewById(R.id.fourbuttonLayout2);	
		}

		questionAnswering = 1;
		
		//link to ui elements
		button = (Button) findViewById(R.id.button5);
		//button.setVisibility(View.GONE);
		
		//de vier antwoord buttons
		answerbutton1 = (Button) findViewById(R.id.answerbutton1);
		answerbutton1.setOnClickListener(this);
		answerbutton2 = (Button) findViewById(R.id.answerbutton2);
		answerbutton2.setOnClickListener(this);
		answerbutton3= (Button) findViewById(R.id.answerbutton3);
		answerbutton3.setOnClickListener(this);
		answerbutton4 = (Button) findViewById(R.id.answerbutton4);
		answerbutton4.setOnClickListener(this);
		
		//de vier antwoord buttons
		fourbuttonLayout = (LinearLayout) findViewById(R.id.fourbuttonLayout);		
			
		//background hoogste layout
		layout = (LinearLayout) findViewById(R.id.buttonLayout);

		//het ophalen van de vragen
        getQuestion();
        getQuestions();
        
        //set de button values
        answerbutton1.setText(possibility1);
        answerbutton2.setText(possibility2);
        answerbutton3.setText(possibility3);
        answerbutton4.setText(possibility4);
        
		// Loader image - will be shown before loading image
        int loader = R.drawable.ic_launcher;
        ImageView image = (ImageView) findViewById(R.id.imageView1);        
        image_url = "http://student.cmi.hro.nl/0832925/standaardProjectBG.jpg";
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());       
        imgLoader.DisplayImage(image_url, loader, image);
        
        Log.d("Vraagfoto", "vraagfoto"+ vraagfoto);
        button.setText("Vraag "+questionAnswering);
       timerstartvalue = 60; //aanta seconden om vragen te beantwoorden
       trackTime();
	 }

	@Override
	public void onClick(View view) {
	
		//haal de text van de button op als antwoord
		Button b = (Button) view;
		answer = b.getText().toString().toLowerCase();
		correctanswer = correctanswer.toLowerCase();
		
				if(answer.equals(correctanswer))
				{
					setCurrentStreak(getCurrentStreak() + 1);
					
					//verander het scherm
					changeScreenAppearance(true);
					
					//score toevoegen aan de database SQLITE
					db.checkScore();
										
					//EXTERNE database
					
					//verbinding maken met een http client
					
					
				}
				else
				{	
			                
			        // get all scores
			        //db.getAllHighscores();
			        
					//zet alles weer op 0
					//setCurrentStreak(0);
					
					//verander het scherm		
					changeScreenAppearance(false);
							
				}
				questionAnswering += 1;
				button.setText("Vraag "+questionAnswering);
	}
	
	public void getQuestion(){
		
		try 
		{
			// Set Question
			setQuestion(new GetQuestions().execute().get());
		} 
		
		catch (InterruptedException e) 	{ e.printStackTrace(); } 
		catch (ExecutionException e) 	{ e.printStackTrace(); }
					
	}
	
	//getter
	public static JSONArray getQuizQuestion() {
		return quizQuestions;
	}

	//setter
	public void setQuestion(JSONArray quizQuestions) {
		this.quizQuestions = quizQuestions;
	}
	
	//alle vragen ophalen
    public void getQuestions(){
        
        JSONArray question = PlayGameActivity.getQuizQuestion();
        int foundQuestion = 0;	

        if(question.length() > 0) {
        
        	for(int i=0; i < question.length(); i++){
 			
        		JSONObject items;

	 			try {
	 				items = PlayGameActivity.getQuizQuestion().getJSONObject(i);
	 			    vraagfoto = items.getString("vraagfoto");
	 			    possibility1 = items.getString("possibility1");
	 			    possibility2 = items.getString("possibility2");
	 			    possibility3 = items.getString("possibility3");
	 			    possibility4 = items.getString("possibility4");
	 			    correctanswer = items.getString("antwoordtext");
	 			   
	 			} catch (JSONException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			
	 			}
		
 			}
        	        	 
        }
           
    }
  
	//om het scherm te veranderen
	public void changeScreenAppearance(Boolean b)
	{
				
		//landscape
		if(rotation == 2)
		{
			//question is correct
			if(b == true)
			{				
				layout.setBackgroundResource(R.drawable.landscapecorrectquestion);
				
			}
			else
			{
				layout.setBackgroundResource(R.drawable.landscapewrongquestion);							
			}
			button.setBackgroundResource(R.drawable.landscapebuttonbackground);
			
		}
		//default portrait (1)
		else
		{
			//question is correct
			if(b == true)
			{		
				layout.setBackgroundResource(R.drawable.portraitcorrectquestion);
			}
			else
			{
				layout.setBackgroundResource(R.drawable.portraitwrongquestion);
			}
			
			button.setBackgroundResource(R.drawable.portraitbuttonbackground);		
			
		}
		
	
	}
	
	//functie om de tijd bij te houden
	public void trackTime()
	{
		final Timer mytimer = new Timer();  // Create a Timer object
        TimerTask mytimerTask = new TimerTask(){
           // This is what we want the Timer to do once a second.
            public void run(){
            	timeleft = timerstartvalue -= 1;
               	//Log.d("answer","timeleft "+timeleft);
            	if(timeleft <= 0)
            	{
            		//Log.d("answer","currentStreak "+currentStreak);
            		mytimer.cancel();
            		
            	}
            	
            }
        };

        mytimer.schedule(mytimerTask, 1000, 1000);
    
	}

	public static int getCurrentStreak() {
		return currentStreak;
	}

	public static void setCurrentStreak(int currentStreak) {
		PlayGameActivity.currentStreak = currentStreak;
	}

}