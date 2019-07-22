package com.pubgplayerzofficial.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.pubgplayerzofficial.model.Result;
import com.pubgplayerzofficial.utilities.Contants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lalit on 7/25/2017.
 */

public class DbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = Contants.DATABASE_NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS userData");
        db.execSQL("DROP TABLE IF EXISTS playData");
        db.execSQL("DROP TABLE IF EXISTS participatedData");
        db.execSQL("DROP TABLE IF EXISTS specialData");
        onCreate(db);

    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);

    }

    public void onCreate(SQLiteDatabase db) {
        String CREATE_user_TABLE = "CREATE TABLE userData(id INTEGER,full_name TEXT,user_name TEXT,image TEXT,email TEXT,mobile_no TEXT,password TEXT,code TEXT,sponsor_code TEXT,dob TEXT,gender TEXT,total_kill INTEGER,winning_amount INTEGER,wallet_amount INTEGER,is_deleted TEXT,active_status TEXT,created_date TEXT,status INTEGER,total_match INTEGER)";
        String CREATE_play_TABLE = "CREATE TABLE playData(id INTEGER,event_name TEXT,date TEXT,time TEXT,chicken_dinner INTEGER,per_kill INTEGER,entry_fee INTEGER,type TEXT,map TEXT,version TEXT,slot INTEGER,description TEXT,image TEXT,status INTEGER,special INTEGER,total INTEGER)";
        String CREATE_participated_TABLE = "CREATE TABLE participatedData(id INTEGER,event_name TEXT,date TEXT,time TEXT,chicken_dinner INTEGER,per_kill INTEGER,entry_fee INTEGER,type TEXT,map TEXT,version TEXT,slot INTEGER,description TEXT,image TEXT,status INTEGER,special INTEGER,total INTEGER)";
        String CREATE_Special_TABLE = "CREATE TABLE specialData(id INTEGER,event_name TEXT,date TEXT,time TEXT,chicken_dinner INTEGER,per_kill INTEGER,entry_fee INTEGER,type TEXT,map TEXT,version TEXT,slot INTEGER,description TEXT,image TEXT,status INTEGER,special INTEGER,total INTEGER)";
        db.execSQL(CREATE_user_TABLE);
        db.execSQL(CREATE_play_TABLE);
        db.execSQL(CREATE_participated_TABLE);
        db.execSQL(CREATE_Special_TABLE);

    }
    //id full_name user_name image email mobile_no password code sponsor_code dob gender total_kill winning_amount wallet_amount is_deleted
//active_status created_date status

    //--------------------------userDataData---------------
    public boolean upsertUserData(Result ob) {
        boolean done = false;
        Result data = null;
        if (ob.getId() != 0) {
            data = getUserDataByLoginId(ob.getId());
            if (data == null) {
                done = insertUserData(ob);
            } else {
                done = updateUserData(ob);
            }
        }
        return done;
    }

    //insert userData data.............
    public boolean insertUserData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("id", ob.getId());
        values.put("full_name", ob.getFull_name());
        values.put("user_name", ob.getUser_name());
        values.put("image", ob.getImage());
        values.put("email", ob.getEmail());
        values.put("mobile_no", ob.getMobile_no());
        values.put("password", ob.getPassword());
        values.put("code", ob.getCode());
        values.put("sponsor_code", ob.getSponsor_code());
        values.put("dob", ob.getDob());
        values.put("gender", ob.getGender());
        values.put("total_kill", ob.getTotal_kill());
        values.put("winning_amount", ob.getWinning_amount());
        values.put("wallet_amount", ob.getWallet_amount());
        values.put("is_deleted", ob.getIs_deleted());
        values.put("active_status", ob.getActive_status());
        values.put("created_date", ob.getCreated_date());
        values.put("status", ob.getStatus());
        values.put("total_match", ob.getTotal_match());

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("userData", null, values);
        db.close();
        return i > 0;
    }

    // for userData data.............
    private void populateUserformation(Cursor cursor, Result ob) {
        ob.setId(cursor.getInt(0));
        ob.setFull_name(cursor.getString(1));
        ob.setUser_name(cursor.getString(2));
        ob.setImage(cursor.getString(3));
        ob.setEmail(cursor.getString(4));
        ob.setMobile_no(cursor.getString(5));
        ob.setPassword(cursor.getString(6));
        ob.setCode(cursor.getString(7));
        ob.setSponsor_code(cursor.getString(8));
        ob.setDob(cursor.getString(9));
        ob.setGender(cursor.getString(10));
        ob.setTotal_kill(cursor.getInt(11));
        ob.setWinning_amount(cursor.getInt(12));
        ob.setWallet_amount(cursor.getInt(13));
        ob.setIs_deleted(cursor.getString(14));
        ob.setActive_status(cursor.getString(15));
        ob.setCreated_date(cursor.getString(16));
        ob.setStatus(cursor.getInt(17));
        ob.setTotal_match(cursor.getInt(18));
    }

    //userData data
    public Result getUserData() {

        String query = "Select * FROM userData";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateUserformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //userData data
    public Result getUserDataByLoginId(int id) {

        String query = "Select * FROM userData WHERE id = " + id + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateUserformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //update user data
    public boolean updateUserData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("id", ob.getId());
        values.put("full_name", ob.getFull_name());
        values.put("user_name", ob.getUser_name());
        values.put("image", ob.getImage());
        values.put("email", ob.getEmail());
        values.put("mobile_no", ob.getMobile_no());
        values.put("password", ob.getPassword());
        values.put("code", ob.getCode());
        values.put("sponsor_code", ob.getSponsor_code());
        values.put("dob", ob.getDob());
        values.put("gender", ob.getGender());
        values.put("total_kill", ob.getTotal_kill());
        values.put("winning_amount", ob.getWinning_amount());
        values.put("wallet_amount", ob.getWallet_amount());
        values.put("is_deleted", ob.getIs_deleted());
        values.put("active_status", ob.getActive_status());
        values.put("created_date", ob.getCreated_date());
        values.put("status", ob.getStatus());
        values.put("total_match", ob.getTotal_match());
        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("userData", values, "id = " + ob.getId() + " ", null);

        db.close();
        return i > 0;
    }

    // delete Address Data from addressId
    public boolean deleteUserData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("userData", null, null);
        db.close();
        return result;
    }


    //--------------------------playData---------------
    public boolean upsertPlayData(Result ob) {
        boolean done = false;
        Result data = null;
        if (ob.getId() != 0) {
            data = getPlayDataId(ob.getId());
            if (data == null) {
                done = insertPlayData(ob);
            } else {
                done = updatePlayData(ob);
            }
        }
        return done;
    }

    //insert play data.............
    public boolean insertPlayData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("id", ob.getId());
        values.put("event_name", ob.getEvent_name());
        values.put("date", ob.getDate());
        values.put("time", ob.getTime());
        values.put("chicken_dinner", ob.getChicken_dinner());
        values.put("per_kill", ob.getPer_kill());
        values.put("entry_fee", ob.getEntry_fee());
        values.put("type", ob.getType());
        values.put("map", ob.getMap());
        values.put("version", ob.getVersion());
        values.put("slot", ob.getSlot());
        values.put("description", ob.getDescription());
        values.put("image", ob.getImage());
        values.put("status", ob.getStatus());
        values.put("special", ob.getSpecial());
        values.put("total", ob.getTotalused());

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("playData", null, values);
        db.close();
        return i > 0;
    }

    // for play data.............
    private void populatePlayformation(Cursor cursor, Result ob) {
        ob.setId(cursor.getInt(0));
        ob.setEvent_name(cursor.getString(1));
        ob.setDate(cursor.getString(2));
        ob.setTime(cursor.getString(3));
        ob.setChicken_dinner(cursor.getString(4));
        ob.setPer_kill(cursor.getInt(5));
        ob.setEntry_fee(cursor.getInt(6));
        ob.setType(cursor.getString(7));
        ob.setMap(cursor.getString(8));
        ob.setVersion(cursor.getString(9));
        ob.setSlot(cursor.getInt(10));
        ob.setDescription(cursor.getString(11));
        ob.setImage(cursor.getString(12));
        ob.setStatus(cursor.getInt(13));
        ob.setSpecial(cursor.getString(14));
        ob.setTotalused(cursor.getInt(15));
    }

    //play data
    public Result getPlayData() {

        String query = "Select * FROM playData";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populatePlayformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //play data
    public Result getPlayDataId(int id) {

        String query = "Select * FROM playData WHERE id = " + id + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populatePlayformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //update play data
    public boolean updatePlayData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("id", ob.getId());
        values.put("event_name", ob.getEvent_name());
        values.put("date", ob.getDate());
        values.put("time", ob.getTime());
        values.put("chicken_dinner", ob.getChicken_dinner());
        values.put("per_kill", ob.getPer_kill());
        values.put("entry_fee", ob.getEntry_fee());
        values.put("type", ob.getType());
        values.put("map", ob.getMap());
        values.put("version", ob.getVersion());
        values.put("slot", ob.getSlot());
        values.put("description", ob.getDescription());
        values.put("image", ob.getImage());
        values.put("status", ob.getStatus());
        values.put("special", ob.getSpecial());
        values.put("total", ob.getTotalused());


        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("playData", values, "id = " + ob.getId() + " ", null);

        db.close();
        return i > 0;
    }

    public boolean deletePlayData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("playData", null, null);
        db.close();
        return result;
    }

    public boolean deletePlayData(int id) {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "id = " + id + " ";
        db.delete("playData", null, null);
        db.close();
        return result;
    }

    //show  play  list data
    public List<Result> GetAllPlayData() {
        ArrayList<Result> list = new ArrayList<Result>();
        String query = "Select * FROM playData";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populatePlayformation(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }


    //--------------------------participatedData---------------
    public boolean upsertparticipatedData(Result ob) {
        boolean done = false;
        Result data = null;
        if (ob.getId() != 0) {
            data = getParticipatedDataId(ob.getId());
            if (data == null) {
                done = insertparticipatedData(ob);
            } else {
                done = updateparticipatedData(ob);
            }
        }
        return done;
    }

    //insert participated data.............
    public boolean insertparticipatedData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("id", ob.getId());
        values.put("event_name", ob.getEvent_name());
        values.put("date", ob.getDate());
        values.put("time", ob.getTime());
        values.put("chicken_dinner", ob.getChicken_dinner());
        values.put("per_kill", ob.getPer_kill());
        values.put("entry_fee", ob.getEntry_fee());
        values.put("type", ob.getType());
        values.put("map", ob.getMap());
        values.put("version", ob.getVersion());
        values.put("slot", ob.getSlot());
        values.put("description", ob.getDescription());
        values.put("image", ob.getImage());
        values.put("status", ob.getStatus());
        values.put("special", ob.getSpecial());
        values.put("total", ob.getTotalused());

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("participatedData", null, values);
        db.close();
        return i > 0;
    }

    // for participated data.............
    private void populateparticipatedformation(Cursor cursor, Result ob) {
        ob.setId(cursor.getInt(0));
        ob.setEvent_name(cursor.getString(1));
        ob.setDate(cursor.getString(2));
        ob.setTime(cursor.getString(3));
        ob.setChicken_dinner(cursor.getString(4));
        ob.setPer_kill(cursor.getInt(5));
        ob.setEntry_fee(cursor.getInt(6));
        ob.setType(cursor.getString(7));
        ob.setMap(cursor.getString(8));
        ob.setVersion(cursor.getString(9));
        ob.setSlot(cursor.getInt(10));
        ob.setDescription(cursor.getString(11));
        ob.setImage(cursor.getString(12));
        ob.setStatus(cursor.getInt(13));
        ob.setSpecial(cursor.getString(14));
        ob.setTotalused(cursor.getInt(15));
    }

    //participated data
    public Result getparticipatedData() {

        String query = "Select * FROM participatedData";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateparticipatedformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //participated data
    public Result getParticipatedDataId(int id) {

        String query = "Select * FROM participatedData WHERE id = " + id + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populateparticipatedformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //update participated data
    public boolean updateparticipatedData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("id", ob.getId());
        values.put("event_name", ob.getEvent_name());
        values.put("date", ob.getDate());
        values.put("time", ob.getTime());
        values.put("chicken_dinner", ob.getChicken_dinner());
        values.put("per_kill", ob.getPer_kill());
        values.put("entry_fee", ob.getEntry_fee());
        values.put("type", ob.getType());
        values.put("map", ob.getMap());
        values.put("version", ob.getVersion());
        values.put("slot", ob.getSlot());
        values.put("description", ob.getDescription());
        values.put("image", ob.getImage());
        values.put("status", ob.getStatus());
        values.put("special", ob.getSpecial());
        values.put("total", ob.getTotalused());


        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("participatedData", values, "id = " + ob.getId() + " ", null);

        db.close();
        return i > 0;
    }

    public boolean deleteparticipatedData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("participatedData", null, null);
        db.close();
        return result;
    }

    //show  participated  list data
    public List<Result> GetAllparticipatedData() {
        ArrayList<Result> list = new ArrayList<Result>();
        String query = "Select * FROM participatedData";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populateparticipatedformation(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }

    //special match..............................
    public boolean upsertspecialData(Result ob) {
        boolean done = false;
        Result data = null;
        if (ob.getId() != 0) {
            data = getspecialDataId(ob.getId());
            if (data == null) {
                done = insertspecialData(ob);
            } else {
                done = updatespecialData(ob);
            }
        }
        return done;
    }

    //insert special data.............
    public boolean insertspecialData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("id", ob.getId());
        values.put("event_name", ob.getEvent_name());
        values.put("date", ob.getDate());
        values.put("time", ob.getTime());
        values.put("chicken_dinner", ob.getChicken_dinner());
        values.put("per_kill", ob.getPer_kill());
        values.put("entry_fee", ob.getEntry_fee());
        values.put("type", ob.getType());
        values.put("map", ob.getMap());
        values.put("version", ob.getVersion());
        values.put("slot", ob.getSlot());
        values.put("description", ob.getDescription());
        values.put("image", ob.getImage());
        values.put("status", ob.getStatus());
        values.put("special", ob.getSpecial());
        values.put("total", ob.getTotalused());

        SQLiteDatabase db = this.getWritableDatabase();

        long i = db.insert("specialData", null, values);
        db.close();
        return i > 0;
    }

    // for special data.............
    private void populatespecialformation(Cursor cursor, Result ob) {
        ob.setId(cursor.getInt(0));
        ob.setEvent_name(cursor.getString(1));
        ob.setDate(cursor.getString(2));
        ob.setTime(cursor.getString(3));
        ob.setChicken_dinner(cursor.getString(4));
        ob.setPer_kill(cursor.getInt(5));
        ob.setEntry_fee(cursor.getInt(6));
        ob.setType(cursor.getString(7));
        ob.setMap(cursor.getString(8));
        ob.setVersion(cursor.getString(9));
        ob.setSlot(cursor.getInt(10));
        ob.setDescription(cursor.getString(11));
        ob.setImage(cursor.getString(12));
        ob.setStatus(cursor.getInt(13));
        ob.setSpecial(cursor.getString(14));
        ob.setTotalused(cursor.getInt(15));
    }

    //special data
    public Result getspecialData() {

        String query = "Select * FROM specialData";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populatespecialformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //special data
    public Result getspecialDataId(int id) {

        String query = "Select * FROM specialData WHERE id = " + id + " ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Result data = new Result();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            populatespecialformation(cursor, data);

            cursor.close();
        } else {
            data = null;
        }
        db.close();
        return data;
    }

    //update special data
    public boolean updatespecialData(Result ob) {
        ContentValues values = new ContentValues();
        values.put("id", ob.getId());
        values.put("event_name", ob.getEvent_name());
        values.put("date", ob.getDate());
        values.put("time", ob.getTime());
        values.put("chicken_dinner", ob.getChicken_dinner());
        values.put("per_kill", ob.getPer_kill());
        values.put("entry_fee", ob.getEntry_fee());
        values.put("type", ob.getType());
        values.put("map", ob.getMap());
        values.put("version", ob.getVersion());
        values.put("slot", ob.getSlot());
        values.put("description", ob.getDescription());
        values.put("image", ob.getImage());
        values.put("status", ob.getStatus());
        values.put("special", ob.getSpecial());
        values.put("total", ob.getTotalused());


        SQLiteDatabase db = this.getWritableDatabase();
        long i = 0;
        i = db.update("specialData", values, "id = " + ob.getId() + " ", null);

        db.close();
        return i > 0;
    }

    public boolean deletespecialData() {
        boolean result = false;
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("specialData", null, null);
        db.close();
        return result;
    }

    //show  special  list data
    public List<Result> GetAllspecialData() {
        ArrayList<Result> list = new ArrayList<Result>();
        String query = "Select * FROM specialData";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Result ob = new Result();
                populatespecialformation(cursor, ob);
                list.add(ob);
                cursor.moveToNext();
            }
        }
        db.close();
        return list;
    }
}
