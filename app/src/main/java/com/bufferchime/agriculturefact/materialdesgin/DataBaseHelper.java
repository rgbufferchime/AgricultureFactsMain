package com.bufferchime.agriculturefact.materialdesgin;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {

	// Logcat tag
	private static final String LOG = DataBaseHelper.class.getName();
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "userDataManager";

	// Table Names
	private static final String TABLE_USERDATA = "userData";


	// Common column names
	private static final String KEY_QUIZNAME = "quizName";
	private static final String KEY_QUIZLEVEL= "quizlevelname";
	private static final String KEY_SCORE = "score";
	private static final String KEY_TOTALQUESTIONS = "totalNoQuestions";
	private static final String KEY_DATEOFQUIZ = "dateofquiz";
	private static final String KEY_ATTEMPTS = "attempts";


	// Table Create Statements
	// DbUSer table create statement
	private static final String CREATE_TABLE_USERDATA = "CREATE TABLE "
			+ TABLE_USERDATA + "(" + KEY_QUIZNAME+ " TEXT," + KEY_QUIZLEVEL
			+ " TEXT PRIMARY KEY," + KEY_SCORE + " INTEGER," + KEY_ATTEMPTS + " INTEGER," + KEY_TOTALQUESTIONS + " INTEGER," + KEY_DATEOFQUIZ
			+ " DATETIME" + ")";



	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_USERDATA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_USERDATA);
		// create new tables
		 onCreate(db);
	}

	/**
	 * Creating a todo
	 */
	public long createToDo(DbUSer todo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_QUIZNAME, todo.getQuizName());
		values.put(KEY_QUIZLEVEL, todo.getQuizlevelname());
		values.put(KEY_SCORE, todo.getScore());
		values.put(KEY_ATTEMPTS, todo.getAttempts()) ;
		values.put(KEY_TOTALQUESTIONS, todo.getTotalNoQuestions());
		values.put(KEY_DATEOFQUIZ, getDateTime());
		// insert row
		long todo_id = db.insert(TABLE_USERDATA, null, values);

		return todo_id;
	}

	/**
	 * get single todo
	 */
	public DbUSer checkQuizLevelExists(String quizlevel) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT  * FROM " + TABLE_USERDATA + " WHERE "
				+ KEY_QUIZLEVEL + " = " + "'" + quizlevel + "'";

				Log.e(LOG, selectQuery);

		Cursor c = db.rawQuery(selectQuery, null);
		DbUSer td = null;
		if (c != null && c.moveToFirst()){
			
			td = new DbUSer();
			td.setQuizName(c.getString(c.getColumnIndex(KEY_QUIZNAME)));
			td.setQuizlevelname(c.getString(c.getColumnIndex(KEY_QUIZLEVEL)));
			td.setScore(c.getInt(c.getColumnIndex(KEY_SCORE)));
			td.setAttempts(c.getInt(c.getColumnIndex(KEY_ATTEMPTS)));
			td.setTotalNoQuestions(c.getInt(c.getColumnIndex(KEY_TOTALQUESTIONS)));
			td.setDateofquiz(c.getString(c.getColumnIndex(KEY_DATEOFQUIZ)));
		}

		return td;
	}

	/**
	 * getting all todos
	 * */
	    public List<DbUSer> getAllData() {
	        List<DbUSer> todos = new ArrayList<DbUSer>();
	        String selectQuery = "SELECT  * FROM " + TABLE_USERDATA;
	 
	        Log.e(LOG, selectQuery);
	 
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor c = db.rawQuery(selectQuery, null);
	 
	        // looping through all rows and adding to list
	        if (c.moveToFirst()) {
	            do {
	            	DbUSer td = new DbUSer();
	    			td.setQuizName(c.getString(c.getColumnIndex(KEY_QUIZNAME)));
	    			td.setQuizlevelname(c.getString(c.getColumnIndex(KEY_QUIZLEVEL)));
	    			td.setScore(c.getInt(c.getColumnIndex(KEY_SCORE)));
	    			td.setAttempts(c.getInt(c.getColumnIndex(KEY_ATTEMPTS)));
	    			td.setTotalNoQuestions(c.getInt(c.getColumnIndex(KEY_TOTALQUESTIONS)));
	    			td.setDateofquiz(c.getString(c.getColumnIndex(KEY_DATEOFQUIZ)));
	                // adding to todo list
	                todos.add(td);
	            } while (c.moveToNext());
	        }
	 
	        return todos;
	    }



	/**
	 * getting todo count
	 */
	public int getToDoCount() {
		String countQuery = "SELECT  * FROM " + TABLE_USERDATA;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		int count = cursor.getCount();
		cursor.close();

		// return count
		return count;
	}

	/**
	 * Updating a todo
	 */
	    public int updateToDo(DbUSer todo) {
	        SQLiteDatabase db = this.getWritableDatabase();
	 
	        ContentValues values = new ContentValues();
	        values.put(KEY_ATTEMPTS, todo.getAttempts());
	        values.put(KEY_SCORE, todo.getScore());
	 
	        // updating row
	        return db.update(TABLE_USERDATA, values, KEY_QUIZLEVEL + " = ?",
	                new String[] { String.valueOf(todo.getQuizlevelname()) });
	    }

	/**
	 * Deleting a todo
	 */
	//    public void deleteToDo(long tado_id) {
	//        SQLiteDatabase db = this.getWritableDatabase();
	//        db.delete(TABLE_TODO, KEY_ID + " = ?",
	//                new String[] { String.valueOf(tado_id) });
	//    }

	// closing database
	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}

	/**
	 * get datetime
	 * */
	private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "ddMMyyyy", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
}
}