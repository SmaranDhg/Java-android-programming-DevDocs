package redeem.androidjava;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.CpuUsageInfo;
import android.util.Log;

import com.karumi.dexter.Dexter;

import java.io.File;
import java.util.ArrayList;

import static redeem.androidjava.RawDatas.dark_color;
import static redeem.androidjava.RawDatas.openSettings;

public class helperClass extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "killer.db";
    public static final String BDLocation = "/data/data/redeem.androidjava/databases";
    private Context context;
    private SQLiteDatabase database;


    public helperClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean openDatabases() {
        String dbPath = context.getDatabasePath(DATABASE_NAME).getPath();
        if (database != null && database.isOpen() ) {
            return true;
        }
        try{
            database = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
            return true;
        }catch (Exception e)
        {
            openSettings(context);
          return false;
        }



    }

    public  void closeDB() {
        if (database != null) {
            database.close();
        }

    }

    public static boolean checkDBPresence(Context context)
    {
        helperClass helperClass=new helperClass(context);
        boolean r= helperClass.openDatabases();
        helperClass.closeDB();
        return r;
    }


    public  ArrayList<String> getItemList(String tableName,String fieldName)
    {


        openDatabases();
        ArrayList<String>  datas=new ArrayList<>();
        Cursor cursor = database.rawQuery("select "+fieldName+" from  "+tableName.replace('.','_'),null);

        if(cursor.getCount()==0)
        {
            database.close();
            return null;
        }

        int i=0;
        if(cursor.moveToFirst()) {
            do {
                if (i>50) break;
                if(cursor.getString(0).length()==0) {

                    datas.add("null");
                }
                else
                    datas.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        database.close();


        return  datas;
    }
    public  ArrayList<String> getItemList(String tableName,String fieldName,String className)
    {
        openDatabases();

        ArrayList<String>  datas=new ArrayList<>();
        Cursor cursor = database.rawQuery("select "+fieldName+" from  "+tableName.replace('.','_')+" where class=\""+className+"\"",null);

        if(cursor.getCount()==0)
        {

            database.close();
            datas.add("null");
            return datas;
        }
        cursor.moveToFirst();
        datas=RawDatas.makeAlist(cursor.getString(0));



        database.close();


        return  datas;
    }
    //incase no break point
    public  ArrayList<String> getItemList(String tableName,String fieldName,String className,int flag)
    {
        openDatabases();

        ArrayList<String>  datas=new ArrayList<>();
        Cursor cursor = database.rawQuery("select "+fieldName+" from  "+tableName.replace('.','_')+" where class=\""+className+"\"",null);

        if(cursor.getCount()==0)
        {

            database.close();
            datas.add("null");
            return datas;
        }
        cursor.moveToFirst();
        datas=RawDatas.makeAlist(cursor.getString(0),flag);



        database.close();


        return  datas;
    }


    public  String getText(String tableName,String fieldName,String className)
    {
        openDatabases();


        Cursor cursor = database.rawQuery("select "+fieldName+" from  "+tableName.replace('.','_')+" where class=\""+className+"\"",null);

        if(cursor.getCount()==0)
        {

            database.close();
            return "null";
        }
        cursor.moveToFirst();
        String data=cursor.getString(0);
        if(data.length()==0)
        {
            database.close();

            return "null";
        }
        else

        return data;






    }


}
