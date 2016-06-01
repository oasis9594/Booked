package com.example.dell.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper{
    // instance
    private static MyDBHandler sInstance;

    // Database Info
    private static final String DATABASE_NAME = "empUsers.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_NAME = "Users";

    //Column Names
    private static final String COLUMN_ID="id";
    private static final String COLUMN_NAME="name";
    private static final String COLUMN_PHONE="phone";
    private static final String COLUMN_ENTITY="entity";
    private static final String COLUMN_REGION="region";
    private static final String COLUMN_AVAILABLE="available";

    //make your database instance a singleton instance across the entire application's lifecycle
    public static synchronized MyDBHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        Log.w(MyConstants.TAG, "getInstance");
        if (sInstance == null) {
            sInstance = new MyDBHandler(context.getApplicationContext());
            Log.w(MyConstants.TAG, "Instance null");
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.w(MyConstants.TAG, "onCreate");
        String query="CREATE TABLE "+TABLE_NAME+"("+
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_ENTITY + " TEXT, " +
                COLUMN_REGION + " INTEGER, " +
                COLUMN_AVAILABLE + " INTEGER " +
                ");";
        try {
            db.execSQL(query);
        }catch (Exception e)
        {
            Log.w(MyConstants.TAG, e.getMessage());
        }
        Log.w(MyConstants.TAG, "onCreate Database");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
    public void addUser(UserDetails item)
    {
        Log.w(MyConstants.TAG, "addUser in database");
        int m_id;
        SQLiteDatabase db=getWritableDatabase();
        // It's a good idea to wrap our insert in a transaction. This helps with performance and ensures
        // consistency of the database.
        db.beginTransaction();
        try{
            ContentValues values=new ContentValues();
            values.put(COLUMN_NAME, item.getName());
            values.put(COLUMN_PHONE, item.getPhone());
            values.put(COLUMN_ENTITY, item.getEntity());
            values.put(COLUMN_REGION, item.getRegionOfWork());
            values.put(COLUMN_AVAILABLE, item.isAvailable());

            m_id=(int)db.insertOrThrow(TABLE_NAME, null, values);
            item.setId(m_id);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.w(MyConstants.TAG, "Error while adding user to database");
        }finally {
            db.endTransaction();
        }
    }
    public void removeUser(int id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.beginTransaction();
        try
        {
            db.delete(TABLE_NAME, COLUMN_ID + " = " + id, null);
            db.setTransactionSuccessful();
        }catch (Exception e) {
            Log.w(MyConstants.TAG, "Error while deleting user from database");
        }
        finally {
            db.endTransaction();
        }
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=\"" + id + "\";");
    }
    public void updateAvailable(int id, boolean b)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_AVAILABLE, b);
            db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(id) });
        }
        catch (Exception e)
        {
            Log.w(MyConstants.TAG, "Error while updating user in database");
        }
    }
    public ArrayList<UserDetails> getAllUsers() {
        ArrayList<UserDetails> myItems = new ArrayList<>();

        // SELECT * FROM POSTS
        // LEFT OUTER JOIN USERS
        // ON POSTS.KEY_POST_USER_ID_FK = USERS.KEY_USER_ID
        String USER_SELECT_QUERY =
                String.format("SELECT * FROM %s",
                        TABLE_NAME);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USER_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    UserDetails newItem = new UserDetails();
                    int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                    String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                    String entity=cursor.getString(cursor.getColumnIndex(COLUMN_ENTITY));
                    int region=cursor.getInt(cursor.getColumnIndex(COLUMN_REGION));
                    String phone=cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));
                    boolean av;
                    av = cursor.getInt(cursor.getColumnIndex(COLUMN_AVAILABLE)) != 0;
                    Log.w(MyConstants.TAG, id+""+av);
                    newItem.setAvailable(av);
                    newItem.setId(id);
                    newItem.setPhone(phone);
                    newItem.setEntity(entity);
                    newItem.setRegionOfWork(region);
                    newItem.setName(name);
                    myItems.add(newItem);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(MyConstants.TAG, "Error while trying to get users from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return myItems;
    }

    public UserDetails getUser(int id)
    {
        UserDetails user=new UserDetails();
        SQLiteDatabase db = getReadableDatabase();
        String query="SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_ID +"=\"" + id + "\";";
        Cursor c=db.rawQuery(query, null);
        try {
            c.moveToFirst();
            user.setId(id);
            user.setName(c.getString(c.getColumnIndex(COLUMN_NAME)));
            user.setPhone(c.getString(c.getColumnIndex(COLUMN_PHONE)));
            user.setEntity(c.getString(c.getColumnIndex(COLUMN_ENTITY)));
            user.setRegionOfWork(c.getInt(c.getColumnIndex(COLUMN_REGION)));
            int b=c.getInt(c.getColumnIndex(COLUMN_AVAILABLE));
            if(b==1)
                user.setAvailable(true);
            else
                user.setAvailable(false);
            c.close();
            return user;
        } catch (Exception e) {
            Log.w(MyConstants.TAG, "Error while getting data");
            return null;
        }
    }
    public boolean userExists(int username)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_ID +"=\"" + username + "\";";
        Cursor c=db.rawQuery(query, null);
        Log.w(MyConstants.TAG, "userExists");
        boolean b= c.moveToFirst();
        c.close();
        return b;
    }
}
