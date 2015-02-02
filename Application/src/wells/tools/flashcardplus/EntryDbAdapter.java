package wells.tools.flashcardplus;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EntryDbAdapter 
{

	public static final String ADPTR_LOGTAG = EntryDbAdapter.class.getSimpleName() + "_TAG";
	public static final String QUERY_LOGTAG = "QUERY_TAG";
	
	public static final String DB_NAME            			= "entry.db";
	public static final int    DB_VERSION         			= 1;
	
	public static final String ENTRY_TABLE					= "entries";	
	public static final String ENTRY_NAME_COL_NAME        	= "entry_name";
	public static final String ENTRY_CONTENT_COL_NAME   	= "entry_content";
	public static final String ENTRY_TIME_COL_NAME	 		= "entry_time";
	
	private SQLiteDatabase   	 mDb = null;
	private EntryDBOpenHelper mDbHelper = null;
	
	// BookInfoDBOpenHelper class creates the table in the database
		private static class EntryDBOpenHelper extends SQLiteOpenHelper 
		{
			static final String HELPER_LOGTAG = EntryDBOpenHelper.class.getSimpleName() + "_TAG";
			
			public EntryDBOpenHelper(Context context, String name, 
					CursorFactory factory, int version) 
			{
				super(context, name, factory, version);
			}
			
			static final String ENTRY_TABLE_CREATE =
		            "create table " + ENTRY_TABLE + 
		            " (" + 
		            ENTRY_NAME_COL_NAME            + " text NOT NULL, " + 
		            ENTRY_CONTENT_COL_NAME  + " text NOT NULL, " +
		            ENTRY_TIME_COL_NAME + "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP" +
		            ");";
				
			@Override
			public void onCreate(SQLiteDatabase db) 
			{
				Log.d(HELPER_LOGTAG, ENTRY_TABLE_CREATE);
				db.execSQL(ENTRY_TABLE_CREATE);
			}

			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
			{
				Log.d(ADPTR_LOGTAG, "Upgrading from version " +
						oldVersion + " to " +
						newVersion + ", which will destroy all old data");
				db.execSQL("DROP TABLE IF EXISTS " + ENTRY_TABLE);
				onCreate(db);
			}
		} // end of BookInfoDBOpenHelper class
		
		// initialize the mContext and the helper objects
		public EntryDbAdapter(Context context) 
		{
			mDbHelper = new EntryDBOpenHelper(context, DB_NAME, null, DB_VERSION);
		}
		
		public void open() throws SQLiteException 
		{
			try 
			{
				mDb = mDbHelper.getWritableDatabase();
				Log.d(ADPTR_LOGTAG, "WRITEABLE DB CREATED");
			}
			catch ( SQLiteException ex ) 
			{
				Log.d(ADPTR_LOGTAG, "READABLE DB CREATED");
				mDb = mDbHelper.getReadableDatabase();
			}
		}
		
		public SQLiteDatabase getReadableDatabase() 
		{
			try 
			{
				return mDbHelper.getReadableDatabase();
			}
			catch ( SQLiteException ex ) 
			{
				ex.printStackTrace();
				return null;
			}
		}
		
		public void close() 
		{
			mDb.close();
		}
		
		// Strongly typed insertion method but inserts multiple books with the same ISBN.
		public long insertEntry(Entry entry) 
		{
			ContentValues newEntry = new ContentValues();
			newEntry.put(ENTRY_NAME_COL_NAME, entry.getName());
			newEntry.put(ENTRY_CONTENT_COL_NAME, entry.getContent());
			
			long insertedRowIndex = 
				mDb.insertWithOnConflict(ENTRY_TABLE, null, newEntry, SQLiteDatabase.CONFLICT_REPLACE);
			Log.d(ADPTR_LOGTAG, "Inserted new entry " + insertedRowIndex);
			return insertedRowIndex;
		}
		
		public ArrayList<Entry> retrieveEntries(int id) 
		{
			String entryName = null;
			String entryContent = null;
			ArrayList<Entry> list = new ArrayList<Entry>();
			
			Cursor rslt = 
				mDb.rawQuery("select * from " + ENTRY_TABLE, null);
			
			if (rslt.moveToFirst()) {

	            while (rslt.isAfterLast() == false) {
	                entryName = rslt.getString(rslt.getColumnIndex(ENTRY_NAME_COL_NAME));
	                entryContent = rslt.getString(rslt.getColumnIndex(ENTRY_CONTENT_COL_NAME));
	                Entry entry = new Entry(entryName, entryContent);

	                list.add(entry);
	                rslt.moveToNext();
	            }
	            
	            return list;
	        }
			else 
			{
				return null;
			}
		}
		
}
