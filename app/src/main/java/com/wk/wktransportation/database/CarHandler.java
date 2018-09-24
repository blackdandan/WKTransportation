package com.wk.wktransportation.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wk.wktransportation.App;
import com.wk.wktransportation.entity.CarInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/9/24 5:22
 * **********************************
 **/
public class CarHandler {
    private static final String TAG = "CarHandler";
    private static CarHandler instance = null;
    private DataBaseHelper dataBaseHelper;
    private CarHandler() {
        dataBaseHelper = new DataBaseHelper(App.getApplication());
    }

    public static CarHandler getInstance() {
        if (instance == null) {
            synchronized (CarHandler.class) {
                if (instance == null) {
                    instance = new CarHandler();
                }
            }
        }
        return instance;
    }
    private void deleteAll(){
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        database.delete("car",null,null);
        database.close();
    }
    public boolean insert(List<CarInfo> carInfos){
        deleteAll();
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        database.beginTransaction();
        long result = 0;
        Log.d(TAG, "do==== CarHandler insert:carInfos size :"+carInfos.size());
        for (CarInfo carInfo:carInfos){
            ContentValues contentValues = new ContentValues();
            contentValues.put("carnumber",carInfo.getCarnumber());
            contentValues.put("incubatornumber",carInfo.getIncubatornumber());
            result = database.insert("car",null,contentValues);
            Log.d(TAG, "do==== CarHandler insert:"+result);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
        return result!=-1;
    }
    public List<String> getIncubatorByCarNumber(String carNumber){
        Log.d(TAG, "do==== CarHandler getIncubatorByCarNumber:"+carNumber);
        String sql = " SELECT DISTINCT incubatornumber FROM car WHERE carnumber = '"+carNumber+"' ;";
        Cursor cursor = dataBaseHelper.getWritableDatabase().rawQuery(sql,null);
        Log.d(TAG, "do==== CarHandler getIncubatorByCarNumber:xxx");
        List<String > incubators = new ArrayList<>();
        while (cursor.moveToNext()){
            String incubator = cursor.getString(cursor.getColumnIndex("incubatornumber"));
            Log.d(TAG, "do==== CarHandler getIncubatorByCarNumber:"+incubator);
            incubators.add(incubator);
        }
        cursor.close();
        return incubators;
    }
}
