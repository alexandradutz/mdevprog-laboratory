package com.example.dutzi.snowy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dutzi.snowy.model.Resort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dutzi on 07.12.2016.
 */

public class SQLManager extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "db_resorts_v3";
    private static final String TABLE_RESORTS = "resorts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COORDS = "coordinates";
    private static final String KEY_SLOPESKM = "slopeskm";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_NOV= "nov";
    private static final String KEY_DEC = "dec";
    private static final String KEY_JAN = "jan";
    private static final String KEY_FEB = "feb";

    public SQLManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_Resorts_TABLE = "CREATE TABLE " + TABLE_RESORTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + KEY_NAME + " TEXT,"
                + KEY_COORDS + " TEXT,"
                + KEY_COUNTRY + " TEXT," + KEY_SLOPESKM + " TEXT," +
                KEY_NOV + " TEXT," + KEY_DEC + " TEXT," +
                KEY_JAN + " TEXT," + KEY_FEB + " TEXT" +")";
        db.execSQL(CREATE_Resorts_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESORTS);
        onCreate(db);
    }

    public void addResort(Resort resort) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        System.out.println("get visitors " + resort.getVisitors(0));
        values.put(KEY_NAME, resort.getName());
        values.put(KEY_COUNTRY, resort.getCountry());
        values.put(KEY_SLOPESKM, resort.getSlopes_km());
        values.put(KEY_COORDS, resort.getCoordinates());
        values.put(KEY_NOV, resort.getVisitors(0));
        values.put(KEY_DEC, resort.getVisitors(1));
        values.put(KEY_JAN, resort.getVisitors(2));
        values.put(KEY_FEB, resort.getVisitors(3));
        resort.setId((int)db.insert(TABLE_RESORTS, null, values));
        db.close();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://snowyreact.firebaseio.com/")//url of firebase app
                .addConverterFactory(GsonConverterFactory.create())//use for convert JSON file into object
                .build();

        REST service = retrofit.create(REST.class);
        Call<Resort> c = service.addResort(resort);
        c.enqueue(new Callback<Resort>() {
            @Override
            public void onResponse(Call<Resort> call, Response<Resort> response) {
                Log.e("RESPONSE",response.raw().toString());
            }

            @Override
            public void onFailure(Call<Resort> call, Throwable t) {
                Log.e("RESPONSE","FAILURE");
            }
        });
    }

    public Resort getResort(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RESORTS, new String[] { KEY_ID,
                        KEY_NAME, KEY_COORDS, KEY_COUNTRY, KEY_SLOPESKM,
                        KEY_NOV, KEY_DEC, KEY_JAN, KEY_FEB}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Resort resort = new Resort(cursor.getString(1), cursor.getString(2),
                cursor.getString(3), Double.parseDouble(cursor.getString(4)));
        resort.addVisitors("NOV", Integer.parseInt(cursor.getString(5)));
        resort.addVisitors("DEC", Integer.parseInt(cursor.getString(6)));
        resort.addVisitors("JAN", Integer.parseInt(cursor.getString(7)));
        resort.addVisitors("FEB", Integer.parseInt(cursor.getString(8)));
        return resort;
    }

    public List<Resort> getAllResorts() {
        List<Resort> ResortList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_RESORTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Resort resort = new Resort(cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), Double.parseDouble(cursor.getString(4)));
                resort.setId(Integer.parseInt(cursor.getString(0)));
                resort.addVisitors("NOV", Integer.parseInt(cursor.getString(5)));
                resort.addVisitors("DEC", Integer.parseInt(cursor.getString(6)));
                resort.addVisitors("JAN", Integer.parseInt(cursor.getString(7)));
                resort.addVisitors("FEB", Integer.parseInt(cursor.getString(8)));
                ResortList.add(resort);

            } while (cursor.moveToNext());
        }
        return ResortList;
    }

    public int updateResort(final Resort resort) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, resort.getName());
        values.put(KEY_COUNTRY, resort.getCountry());
        values.put(KEY_SLOPESKM, resort.getSlopes_km());
        values.put(KEY_COORDS, resort.getCoordinates());
        values.put(KEY_NOV, resort.getVisitors(0));
        values.put(KEY_DEC, resort.getVisitors(1));
        values.put(KEY_JAN, resort.getVisitors(2));
        values.put(KEY_FEB, resort.getVisitors(3));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://snowyreact.firebaseio.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final REST service = retrofit.create(REST.class);
        Call<ResponseBody> c = service.getId("\"id\"",String.valueOf(resort.getId() - 1));
        c.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("RESPONSE",response.raw().toString());
                try {
                    String id = response.body().string().split(":")[0].replace("\"","").replace("{","");
                    Log.e("UPDATE_ID",id);
                    Call<Resort> r = service.updateResort(id,resort);
                    r.enqueue(new Callback<Resort>() {
                        @Override
                        public void onResponse(Call<Resort> call, Response<Resort> response) {
                            Log.e("UPDATE_RESPONSE",response.raw().toString());
                        }

                        @Override
                        public void onFailure(Call<Resort> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
                catch (IOException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("RESPONSE","FAILURE");
            }
        });

        return db.update(TABLE_RESORTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(resort.getId())});
    }

    public void deleteResort(Resort resort) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RESORTS, KEY_ID + " = ?",
                new String[] { String.valueOf(resort.getId() - 1) });
        db.close();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://snowyreact.firebaseio.com/")//url of firebase app
                .addConverterFactory(GsonConverterFactory.create())//use for convert JSON file into object
                .build();

        final REST service = retrofit.create(REST.class);
        Call<ResponseBody> c = service.getId("\"id\"",String.valueOf(resort.getId()-1));
        c.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("RESPONSE",response.raw().toString());
                try {
                    String id = response.body().string().split(":")[0].replace("\"","").replace("{","");
                    Log.e("DELETE_ID",id);
                    Call<Resort> r = service.removeResort(id);
                    r.enqueue(new Callback<Resort>() {
                        @Override
                        public void onResponse(Call<Resort> call, Response<Resort> response) {
                            Log.e("DELETE_RESPONSE",response.raw().toString());
                        }

                        @Override
                        public void onFailure(Call<Resort> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
                }
                catch (IOException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("RESPONSE","FAILURE");
            }
        });
    }
}
