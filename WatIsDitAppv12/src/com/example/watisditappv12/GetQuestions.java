package com.example.watisditappv12;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class GetQuestions extends AsyncTask<String, Integer,JSONArray>{
	@Override
	protected JSONArray doInBackground(String... params) {
		
		JSONArray response = new JSONArray();
		HttpClient client = new DefaultHttpClient();
		  
		try 
		{	
			Log.d("Progress","Get Questions.. ");	
		  	HttpPost httppost = new HttpPost("http://student.cmi.hro.nl/0832925/watisditapp/?question=1");
		   	httppost.setHeader("Content-type", "application/json");
		    	
		   	ResponseHandler<String> responseHandler=new BasicResponseHandler();
		   	String responseBody = client.execute(httppost, responseHandler);
		   	JSONObject jObj = new JSONObject(responseBody);
		   	response = jObj.getJSONArray("questions");  	
		} 
		
		catch (Exception e) { e.printStackTrace(); }
		      
		return response;	
	}

}
