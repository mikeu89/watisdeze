package com.example.watisditappv12;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	private static String id, username;
	private static int highscore;
	
	
	// Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Highscores";
   
	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);	
	}

	private ArrayList<String> results = new ArrayList<String>();
	
	public ArrayList<String> getResults() {
		return results;
	}

	public void setResults(ArrayList<String> results) {
		this.results = results;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// SQL statement to create book table
		String CREATE_BOOK_TABLE = "CREATE TABLE messages ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				"username TEXT, "+
				"highscore INT" + ")";		
		// create books table
		db.execSQL(CREATE_BOOK_TABLE);
	}
	
//	@Override
//	public void onCreate(SQLiteDatabase db) {
//		// SQL statement to create book table
//		String CREATE_BOOK_TABLE = "CREATE TABLE messages ( " +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
//                "id_school INT,"+
//                "id_message INT, "+
//				"text TEXT, "+
//				"timestamp TEXT" + ")";		
//		// create books table
//		db.execSQL(CREATE_BOOK_TABLE);
//	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS messages");
        
        // create fresh books table
        this.onCreate(db);
	}
	//---------------------------------------------------------------------
   
	/**
     * CRUD operations (create "add", read "get", update, delete) 
     */
	
	// Books table name
    private static final String TABLE_NAME = "messages";
    
    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_HIGHSCORE = "highscore";
    
    private static final String[] COLUMNS = {KEY_ID,KEY_USERNAME,KEY_HIGHSCORE};
    
    public boolean addHighscore(String username, int highscore){
		Log.d("addhighscore","test" + highscore);
		// 1. get reference to writable DB
		SQLiteDatabase db = this.getWritableDatabase();
		 
		// 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues(); 
        values.put(KEY_USERNAME, username); // get username
        values.put(KEY_HIGHSCORE, highscore);
        
        try {
            db.insertOrThrow(TABLE_NAME, null, values);
            return true; // Won't be executed if an error is thrown            
        }
        catch(SQLiteConstraintException e) {
            return false; 
        }
               
	}
	
    public void getAllHighscores() {

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME;
 
    	// 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build messages and add it to list
        if (cursor.moveToFirst()) {
            do {
            	id = cursor.getString(cursor.getColumnIndex(KEY_ID));
            	username = cursor.getString(cursor.getColumnIndex(KEY_USERNAME));
            	highscore = cursor.getInt(cursor.getColumnIndex(KEY_HIGHSCORE));
       	 		Log.d("LEES", "id,: " + id + "username: " + username + ",highscore: " + highscore);
       	 		results.add("id: " + id + ", username: " + username + ", highscore: " + highscore);
            } while (cursor.moveToNext());
        }   
    }

    //hier wordt de score vergeleken // NIET AANKOMEN
    public void checkScore(){
        
       if(highscore <= PlayGameActivity.getCurrentStreak()){
    	   addHighscore("mike", PlayGameActivity.getCurrentStreak());
       }
       
    }
    
	public static String[] getColumns() {
		return COLUMNS;
	}

}
