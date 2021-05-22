package redeem.androidjava;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.nio.DoubleBuffer;
import java.security.PublicKey;
import java.util.ArrayList;

import static android.provider.Contacts.SettingsColumns.KEY;
import static redeem.androidjava.RawDatas.METHODS;
import static redeem.androidjava.RawDatas.METHODS_DESCRIPTION;

public class SearchDataBaseHelper extends SQLiteOpenHelper {

    private static  int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "viper.db";
    private static final String BDLocation = "/data/data/redeem.androidjava/databases";
    public static  final  String METHOD_NAME="mName";
    public static  final  String PACKAGE_NAME="pName";
    public static  final  String CLASS_NAME="cName";
    public static  final  String METHOD_DESCRIPTION="MethodsDesciption";
    public static final String CAT="cat";
    public static final String KEY="KKKEWY";
    public static final String VALUE="value";
    public static final String PACKAGE_DESCRIPTION="pDesc";
    public static final String CLASS_DESCRIPTION="cDesc";
    public static final String FREQUENCY="frequency";
    public static final String DATE_TIME="curTime";
    public static final String cEX_IM="cExIm";
    public static  final String TABLE_METHODS="Methods";
    public  static  final String TABLE_PACKAGE="Package";
    public static final String TABLE_CLASSES="Classes";
    public static final String TABLE_FREQUENTS="Frequents";
    public static final String TABLE_ACTIVITIES="Activities";
    public static final String TABLE_CLASS_DESC="ClassDesc";
    public static final String TABLE_ARCHIVE="arcchiveValue";

    //cpp
    public static final String TABLE_HEADER_FUNCTION="TABLEHEADERFUNCTION";
    public static final String TABLE_CLASS_FUNCTION="TABLECLASSFUNCTION";
    public static final String TABLE_HEADER="TABLEHEADER";
    public static final String TABLE_CPP_CLASS="TABLECPPCLASS";
    public static final String HEADER_NAME="hName";
    public static final String DESC_HEADER="boxHeader";
    public static final String CPP_CLASS="cppClass";
    public static final String DESC_CLASS="boxedClass";
    public static final String FUNCTION_NAME="fName";
    public static final String BOXED_FUNCTION="boxedFunction";


    private Context context;
    private SQLiteDatabase database;
    private static  final  String CREATE_TABLE_METHODS="create table if not exists "+TABLE_METHODS+"(id int  primary key,mName varchar(255) not null,MethodsDesciption varchar(255) default 'null',pName varchar(255) not null,cName varchar(255) not null,cat varchar(8) not null)";
    private static  final  String CREATE_TABLE_PACKAGE="create table if not exists "+TABLE_PACKAGE+"(id int  primary key,pName varchar(255) not null,pDesc varchar(255) not null,cat varchar(8) not null)";
    private static  final  String CREATE_TABLE_CLASSES="create table if not exists "+TABLE_CLASSES+"(id int  primary key,cName varchar(255) not null,pName varchar(255) not null,cat varchar(7) not null)";
    private static  final  String CREATE_TABLE_FREQUENTS="create table if not exists "+TABLE_FREQUENTS+"(id int  primary key,mName varchar(255) default 'null',pName varchar(255) not null,cName varchar(255) not null,curTime datetime default current_timestamp ,frequency int default 1,cat varchar(7) not null)";
    private static  final  String CREATE_TABLE_ACTIVITIES="create table if not exists "+TABLE_ACTIVITIES+"(id int  primary key,mName varchar(255) default 'null',pName varchar(255) not null,cName varchar(255) not null,curTime datetime default current_timestamp ,cat varchar(7) not null)";
    private static  final  String CREATE_TABLE_CLASS_DESCRITION="create table if not exists "+TABLE_CLASS_DESC+"(id int  primary key,cName varchar(255) not null,cDesc varchar(255) not null,cExIm varchar(255) not null,cat varchar(8) not null)";
    private static  final  String CREATE_TABLE_CPP_HEADER_FUNCTION="create table if not exists "+TABLE_HEADER_FUNCTION+"(id int  primary key,"+FUNCTION_NAME+" varchar(255) not null,"+BOXED_FUNCTION+" varchar(255) default 'null',"+HEADER_NAME+" varchar(255) not null,"+CPP_CLASS+" varchar(255) not null,cat varchar(8) not null)";
    private static  final  String CREATE_TABLE_CPP_HEADER="create table if not exists "+TABLE_HEADER+"(id int  primary key,"+HEADER_NAME+" varchar(255) not null,"+DESC_HEADER+" varchar(255) default 'null',cat varchar(8) not null)";
    private static  final  String CREATE_TABLE_CPP_CLASS_FUNCTION="create table if not exists "+TABLE_CLASS_FUNCTION+"(id int  primary key,"+FUNCTION_NAME+" varchar(255) not null,"+BOXED_FUNCTION+" varchar(255) default 'null',"+HEADER_NAME+" varchar(255) not null,"+CPP_CLASS+" varchar(255) not null,cat varchar(8) not null)";
    private static  final  String CREATE_TABLE_CPP_CLASS  ="create table if not exists "+TABLE_CPP_CLASS+"(id int  primary key,"+HEADER_NAME+" varchar(255) not null,"+CPP_CLASS+" varchar(255) not null,"+DESC_CLASS+" varchar(255) default 'null',cat varchar(8) not null)";
    private static final  String CREATE_TABLE_ARCHIVE="create table if not exists "+TABLE_ARCHIVE+"(id int primary key, "+KEY+" varchar(255) not null,"+VALUE+" varchar(255) not null)";
    public SearchDataBaseHelper( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_METHODS);
        db.execSQL(CREATE_TABLE_PACKAGE);
        db.execSQL(CREATE_TABLE_CLASSES);
        db.execSQL(CREATE_TABLE_ACTIVITIES);
        db.execSQL(CREATE_TABLE_FREQUENTS);
        db.execSQL(CREATE_TABLE_CLASS_DESCRITION);
        db.execSQL(CREATE_TABLE_CPP_HEADER_FUNCTION);
        db.execSQL(CREATE_TABLE_CPP_CLASS_FUNCTION);
        db.execSQL(CREATE_TABLE_CPP_HEADER);
        db.execSQL(CREATE_TABLE_CPP_CLASS);
        db.execSQL(CREATE_TABLE_ARCHIVE);
        Log.d("database Created","true");



    }
    //its for making sure that tables are created when data base already exist
    //during development phase
    private void update(SQLiteDatabase db)
    {   db.execSQL(CREATE_TABLE_METHODS);
        db.execSQL(CREATE_TABLE_PACKAGE);
        db.execSQL(CREATE_TABLE_CLASSES);
        db.execSQL(CREATE_TABLE_ACTIVITIES);
        db.execSQL(CREATE_TABLE_FREQUENTS);
        db.execSQL(CREATE_TABLE_CLASS_DESCRITION);
        db.execSQL(CREATE_TABLE_CPP_HEADER_FUNCTION);
        db.execSQL(CREATE_TABLE_CPP_CLASS_FUNCTION);
        db.execSQL(CREATE_TABLE_CPP_HEADER);
        db.execSQL(CREATE_TABLE_CPP_CLASS);
        db.execSQL(CREATE_TABLE_ARCHIVE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS "+TABLE_METHODS);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CLASSES);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_PACKAGE);

        Log.d("database Created","true");
        update(db);

    }





    public synchronized  boolean openDatabases() {
        String dbPath = context.getDatabasePath(DATABASE_NAME).getPath();
        Log.d("tag", "openDatabases: "+dbPath);
        if (database != null && database.isOpen()) {
            return true;
        }
        try {
            database = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
            return true;
        }catch (Exception e)
        {
          return false  ;
        }



    }

    public synchronized boolean  insertClass(String pName,String cName,String cat)
    {
        this.database = this.getWritableDatabase();
        if(!checkPresent(TABLE_CLASSES,CLASS_NAME,cName,database)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CLASS_NAME, cName);
            contentValues.put(PACKAGE_NAME, pName);
            contentValues.put(CAT, cat);
            database.insert(TABLE_CLASSES, null, contentValues);
            database.close();
        }else this.database.close();

        return true;
    }

    public synchronized boolean  insertMethods(String pName,String cName,String cat,Context context)
    {
        this.database=getWritableDatabase();
        //incase methods already present
        if(!checkPresent(TABLE_METHODS,CLASS_NAME,cName,database))
       {
           helperClass helperClass= new helperClass(context);
           ArrayList<String> mdesc= helperClass.getItemList(pName, METHODS_DESCRIPTION,cName,1);
           ArrayList<String> methods=helperClass.getItemList(pName, METHODS,cName,1);
           int i=0;
           for(String mName:methods)
           {
               if(i>=mdesc.size()) break;//for exception handling
               Log.d("fillin_methods", ": "+mName);
               insertMethods(pName,cName,mName,mdesc.get(i),cat,database);
               i++;
           }
       }
        else
            database.close();


        return true;
    }

    public synchronized boolean  insertMethods(String pName,String cName,String mName ,String mDesc,String cat,SQLiteDatabase database)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(METHOD_NAME,mName);
        contentValues.put(CLASS_NAME,cName);
        contentValues.put(PACKAGE_NAME,pName);
        contentValues.put(METHOD_DESCRIPTION,mDesc);
        contentValues.put(CAT,cat);
        database.insert(TABLE_METHODS,null,contentValues);
        database.close();

        return true;
    }
    public synchronized boolean  insertPackage(String pName,String pDesc,String cat)
    {
        this.database=this.getWritableDatabase();
        update(database);
        if(!checkPresent(TABLE_PACKAGE,PACKAGE_NAME,pName,database)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(PACKAGE_NAME, pName);
            contentValues.put(PACKAGE_DESCRIPTION, pDesc);
            contentValues.put(CAT, cat);
            database.insert(TABLE_PACKAGE, null, contentValues);
           this.database.close();
           return true;
        }else this.database.close();

        return false;
    }
    public synchronized boolean  insertClassDesc(String cName,String cDesc,String exim,String cat)
    {
        this.database=this.getWritableDatabase();
        update(database);
        if(!checkPresent(TABLE_CLASS_DESC,CLASS_NAME,cName,database)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CLASS_NAME, cName);
            contentValues.put(CLASS_DESCRIPTION, cDesc);
            contentValues.put(cEX_IM,exim);
            contentValues.put(CAT, cat);
            database.insert(TABLE_CLASS_DESC, null, contentValues);
            database.close();
            return true;
        }else database.close();

        return false;
    }
    public synchronized boolean  insertFrequents(String pName,String cName,String cat,int frequency)
    {
        this.database=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLASS_NAME,cName);
        contentValues.put(PACKAGE_NAME,pName);
        contentValues.put(CAT,cat);
        contentValues.put(FREQUENCY,frequency);
        database.insert(TABLE_FREQUENTS,null,contentValues);
        database.close();

        return true;
    }
    public synchronized boolean  insertFrequents(String pName,String cName,String mName,String cat,int frequency)
    {
        this.database=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CLASS_NAME,cName);
        contentValues.put(PACKAGE_NAME,pName);
        contentValues.put(CAT,cat);
        contentValues.put(METHOD_NAME,mName);
        contentValues.put(FREQUENCY,frequency);
        database.insert(TABLE_FREQUENTS,null,contentValues);
        database.close();

        return true;
    }

    public  boolean insertVal(String key,String value)
    {
        this.database=this.getWritableDatabase();
        update(database);
        //so that repeated traversal of save data
        ContentValues contentValues = new ContentValues();//just for traversing the data in the database
        contentValues.put(KEY,key);
        contentValues.put(VALUE ,value);
        database.insert(TABLE_ARCHIVE,null,contentValues);
        database.close();

        return true;
    }


    public  boolean insertHeader(String hName,String fDeesc)
    {
        this.database=this.getWritableDatabase();
        //so that repeated traversal of save data
        ContentValues contentValues = new ContentValues();//just for traversing the data in the database
        contentValues.put(HEADER_NAME,hName);
        contentValues.put(DESC_HEADER ,fDeesc);
        contentValues.put(CAT ,"cpp");
        database.insert(TABLE_HEADER,null,contentValues);
        database.close();

        return true;
    }
    public  boolean insertHeaderFunction(String hName,String fName,String boxedf)
    {
        this.database=this.getWritableDatabase();
        //so that repeated traversal of save data
        ContentValues contentValues = new ContentValues();//just for traversing the data in the database
        contentValues.put(HEADER_NAME,hName);
        contentValues.put(FUNCTION_NAME ,fName);
        contentValues.put(BOXED_FUNCTION ,boxedf);
        contentValues.put(CAT ,"cpp");
        database.insert(TABLE_HEADER_FUNCTION,null,contentValues);
        database.close();

        return true;
    }
    public  boolean insertClassFunction(String hName,String cName,String fName,String boxedf)
    {
        this.database=this.getWritableDatabase();
        //so that repeated traversal of save data
        ContentValues contentValues = new ContentValues();//just for traversing the data in the database
        contentValues.put(HEADER_NAME,hName);
        contentValues.put(FUNCTION_NAME ,fName);
        contentValues.put(CLASS_NAME ,cName);
        contentValues.put(BOXED_FUNCTION ,boxedf);
        contentValues.put(CAT ,"cpp");
        database.insert(TABLE_CLASS_FUNCTION,null,contentValues);
        database.close();

        return true;
    }
    public  boolean insertCppClass(String hName,String cName,String boxedc)
    {
        this.database=this.getWritableDatabase();
        //so that repeated traversal of save data
        ContentValues contentValues = new ContentValues();//just for traversing the data in the database
        contentValues.put(HEADER_NAME,hName);
        contentValues.put(CLASS_NAME ,cName);
        contentValues.put(BOXED_FUNCTION ,boxedc);
        contentValues.put(CAT ,"cpp");
        database.insert(TABLE_CPP_CLASS,null,contentValues);
        database.close();

        return true;
    }

    //for starting fresh search data traversal
    public   void freshUpSearch() {
        onUpgrade(this.getWritableDatabase(),DATABASE_VERSION,DATABASE_VERSION);

    }

    public synchronized ArrayList<String> getItemList(String tableName, String fieldName, String extrainfo,int BREAK_POINT)
    {
       this.database=getReadableDatabase();

        ArrayList<String>  datas=new ArrayList<>();
        Cursor cursor = database.rawQuery("select "+fieldName+" from  "+tableName+" where "+fieldName+" like \"%"+extrainfo+"%\"",null);


        if(cursor.moveToFirst()) {
            int i=0;
            do {
                if(BREAK_POINT!=0 && i>BREAK_POINT) break;
                if(cursor.getString(0).length()==0) {

                    datas.add("null");
                }
                else {
                    datas.add(cursor.getString(0));
                    Log.d("Data:", "getItemList: "+cursor.getString(0));
                }
                i++;
            } while (cursor.moveToNext());
        }


        database.close();


        return  datas;
    }

    public synchronized ArrayList<String> getItems(String tableName, String fieldName, String extrainfo,String infoField)
    {
        this.database=getReadableDatabase();

        ArrayList<String>  datas=new ArrayList<>();
        Cursor cursor = database.rawQuery("select "+fieldName+" from  "+tableName+" where "+infoField+" like \"%"+extrainfo+"%\"",null);


        if(cursor.moveToFirst()) {
            int i=0;
            do {

                if(cursor.getString(0).length()==0) {

                    datas.add("null");
                }
                else {
                    datas.add(cursor.getString(0));
                    Log.d("Data:", "getItemList: "+cursor.getString(0));
                }
                i++;
            } while (cursor.moveToNext());
        }


        database.close();


        return  datas;
    }

    public synchronized static ArrayList<String> getItemList(String tableName, String fieldName, String extrainfo,int BREAK_POINT,Context context)
    {
       SearchDataBaseHelper searchDataBaseHelper= new SearchDataBaseHelper(context);
       return searchDataBaseHelper.getItemList(tableName,fieldName,extrainfo,BREAK_POINT);
    }


    //here just database is let open so that it doesnot creat re-open object issue
    public synchronized boolean checkPresent(String tableName, String fieldName, String extrainfo,SQLiteDatabase database)
    {
        Cursor cursor = database.rawQuery("select "+fieldName+" from  "+tableName+" where "+fieldName+"=\""+extrainfo+"\"",null);
        if(cursor.getCount()==0)
            return false;

        else
            return true;




    }

    //for retriveing the cat of the class
    public synchronized String getValue(String infoValue,String infoField,String fieldName,String tableName)
    {
        this.database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("select "+fieldName+" from "+tableName+" where "+infoField+" = \""+infoValue+"\"",null);
        cursor.moveToFirst();
        String val="";
        if(cursor.getCount()!=0)
        val=cursor.getString(0);
        database.close();
        cursor.close();
        return val.length()!=0?val:"";
    }
    //for retriveing the cat of the class with two conditions
    public synchronized String getValue(String infoValue,String infoField,String infoValue1,String infoField1,String fieldName,String tableName)
    {
        this.database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery("select "+fieldName+" from "+tableName+" where "+infoField+" = \""+infoValue+"\" and "+infoField1+"= \""+infoValue1+"\"",null);
        cursor.moveToFirst();
        String val="";
        if(cursor.getCount()!=0)
            val= cursor.getString(0);
        database.close();
        return val.length()!=0?val:"";
    }

    public synchronized  static String getValue(String infoValue,String infoField,String infoValue1,String infoField1,String fieldName,String tableName,Context context)
    {
        SearchDataBaseHelper searchDataBaseHelper = new SearchDataBaseHelper(context);
        return searchDataBaseHelper.getValue(infoValue,infoField,infoValue1,infoField1,fieldName,tableName);
    }

    //incase satic data
    public synchronized static String getValue(String infoValue,String infoField,String fieldName,String tableName,Context context){
        SearchDataBaseHelper searchDataBaseHelper = new SearchDataBaseHelper(context);
        return searchDataBaseHelper.getValue(infoValue,infoField,fieldName,tableName);
    }



    //just in case all data is needed
    public ArrayList<String> getItemList(String tableName, String fieldName)
    {
        openDatabases();

        ArrayList<String>  datas=new ArrayList<>();
        Cursor cursor = database.rawQuery("select "+fieldName+" from  "+tableName,null);

        if(cursor.moveToFirst()) {
            do {
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

    public synchronized int getCount(String tableName, String fieldName, String fieldValue)
    {
        //becoause incase database is not present it it will retrun false
        int i=0;
        if(!openDatabases()) return  0;
        update(database);//in case any table missing
        Cursor cursor;
        try{
            ArrayList<String>  datas=new ArrayList<>();
            cursor = database.rawQuery("select "+fieldName+" from  "+tableName+" where "+fieldName+"=\""+fieldValue+"\"",null);
            i=cursor.getCount();
            database.close();
        }catch (SQLiteException e)
        {
            database.close();
            return 0;
        }


      return i;
    }
    public synchronized int  getCount(String tableName, String fieldName)
    {
        //becoause incase database is not present it it will retrun false
        int i=0;
        if(!openDatabases()) return  0;
        update(database);
        Cursor cursor;
        try{
            ArrayList<String>  datas=new ArrayList<>();
            cursor = database.rawQuery("select "+fieldName+" from  "+tableName,null);
            i=cursor.getCount();
            database.close();
        }catch (SQLiteException e) {
            database.close();
            return 0;
        }
        return i;
    }

}
