package redeem.androidjava;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.karumi.dexter.Dexter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static redeem.androidjava.RawDatas.*;
import static redeem.androidjava.SearchDataBaseHelper.*;

public class MainActivity extends AppCompatActivity {


    private static TextView mJava, mAndroid,title,mCPP,mPy,mSoup,mTf;
    private CardView first, second,third,pycrd,tfcrd,soupcrd;
    private ImageView home,search,nbButton,ndMode,tficon,jicon,pyicon,soupicon,cppicon,androidicon;
    private RelativeLayout mainView;
    private Context context =MainActivity.this;
    public static ExecutorService threadPool = Executors.newFixedThreadPool(20);
    private static  SimplePermisionListener simplePermisionListener;

    private volatile boolean flag=false;//for thread safety and to make sure data is completed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



        //getting permision and all ,here we use dexter repository to tacle all the nuts with permission
        simplePermisionListener= new SimplePermisionListener(this);
        Dexter.withActivity(this).withPermission(Manifest.permission.INTERNET).withListener(simplePermisionListener).check();

        //first time
        fillsearch(MainActivity.this);

        mJava = findViewById(R.id.JavaContent);
        nbButton = findViewById(R.id.navigationbutton);
        nbButton.setImageDrawable(getResources().getDrawable(R.drawable.home));
        first=findViewById(R.id.firstStuff);
        second=findViewById(R.id.Secondsstuff);
        mainView=findViewById(R.id.MainView);
        mAndroid = findViewById(R.id.AndroidContent);
        search= findViewById(R.id.searchIcon);
        home=findViewById(R.id.homeIcon);
        title=findViewById(R.id.Title);
        third=findViewById(R.id.t);
        mCPP=findViewById(R.id.CPPContent);
        ndMode=findViewById(R.id.nd_mode);
        pycrd=findViewById(R.id.python_crd);
        soupcrd=findViewById(R.id.soup_crd);
        tfcrd=findViewById(R.id.tf_crd);
        mPy=findViewById(R.id.python_Content);
        mSoup=findViewById(R.id.soup_Content);
        mTf=findViewById(R.id.tf_Content);
        jicon=findViewById(R.id.java_cnt_icon);
        tficon=findViewById(R.id.tf_cnt_icon);
        pyicon=findViewById(R.id.python_cnt_icon);
        androidicon=findViewById(R.id.android_cnt_icon);
        soupicon=findViewById(R.id.soup_cnt_icon);
        tficon=findViewById(R.id.tf_cnt_icon);
        cppicon=findViewById(R.id.cpp_cnt_icon);





        ndMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getTHEME().equals(THEME_DARK))
                {

                    setNd_toggle(1);
                    setTheme();
                }
                else{

                    setNd_toggle(0);
                    setTheme();
                }
            }
        });



        //now setting the theme
        setTheme();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(MainActivity.this,SearchActivity.class));
            }
        });




        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, Content.class).putExtra("Java", 0).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, ContentForAndroid.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
   third.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
            startActivity(new Intent(MainActivity.this,CppContent.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
       }
   });
    }


    //nd_mode
    private void ndmode()
    {
        if(getTHEME().equals(THEME_DARK)) ndMode.setImageDrawable(getResources().getDrawable(R.drawable.sun));
        else  ndMode.setImageDrawable(getResources().getDrawable(R.drawable.dark));

    }

    //first time set up
    private  static void fillsearch(Context context)
    {
        //first time setting of the search activity
        //for firsttime travesing
        SearchDataBaseHelper helper = new SearchDataBaseHelper(context);
        int i = 0;
        i = helper.getCount(TABLE_CLASSES,CLASS_NAME);
        if(i==0) context.startActivity(new Intent(context,SearchActivity.class));
    }


    //executed during the splash
    public   static  void copyAssets(final Context context ) throws InterruptedException {

        if(!helperClass.checkDBPresence(context))
        threadPool.submit(()->{if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(context, PERMISSIONS)) {
                //if permission is denied
               openSettings(context);
                Decompress.unzipFromAssets(context,"killer.zip",helperClass.BDLocation);
            } else {
                Decompress.unzipFromAssets(context,"killer.zip",helperClass.BDLocation);

            }
        } else {
            Decompress.unzipFromAssets(context,"killer.zip",helperClass.BDLocation);
        }
        });




    }



    //permission check with copy asset
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



    private  void setTheme()
    {

        //iv
        home.setColorFilter(getIconTheme());
        search.setColorFilter(getIconTheme());
        nbButton.setColorFilter(Color.GRAY);
        ndMode.setColorFilter(Color.GRAY);
//        tficon.setColorFilter(getIconTheme());
//        jicon.setColorFilter(getIconTheme());
//        pyicon.setColorFilter(getIconTheme());
//        androidicon.setColorFilter(getIconTheme());
//        soupicon.setColorFilter(getIconTheme());
//        pyicon.setColorFilter(getIconTheme());
        tficon.setImageDrawable(getResources().getDrawable(R.drawable.tf));
        jicon.setImageDrawable(getResources().getDrawable(R.drawable.java));
        androidicon.setImageDrawable(getResources().getDrawable(R.drawable.android));
        soupicon.setImageDrawable(getResources().getDrawable(R.drawable.soup));
        pyicon.setImageDrawable(getResources().getDrawable(R.drawable.python));
        cppicon.setImageDrawable(getResources().getDrawable(R.drawable.cpp));
        //mainView
        mainView.setBackgroundColor(getResources().getColor(getViewTheme()));
        //crdView
        first.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_MAIN)));
        second.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_MAIN)));
        third.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_MAIN)));
        pycrd.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_MAIN)));
        tfcrd.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_MAIN)));
        soupcrd.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_MAIN)));
        //textView
        mJava.setTextColor(getResources().getColor(getTextContentTheme()));
        mAndroid.setTextColor(getResources().getColor(getTextContentTheme()));
        mCPP.setTextColor(getResources().getColor(getTextContentTheme()));
        mTf.setTextColor(getResources().getColor(getTextContentTheme()));
        mSoup.setTextColor(getResources().getColor(getTextContentTheme()));
        mPy.setTextColor(getResources().getColor(getTextContentTheme()));
        title.setTextColor(getResources().getColor(getTextTitleTheme()));

        //setting the ndmode
        ndmode();

    }


}

//here we create the threadlocal object so that it will make things fast
class ThreadSafeHelper{

    public static Context getContext() {
        return context;
    }

    private  static Context context;
    ThreadSafeHelper(Context context)
    {this.context=context;}






    public  ThreadLocal<helperClass> helper = new ThreadLocal<helperClass>(){
        @Nullable
        @Override
        protected helperClass initialValue() {
            return new helperClass(ThreadSafeHelper.getContext());
        }

        @Nullable
        @Override
        public helperClass get() {
            return super.get();
        }
    };
    public  ThreadLocal<SearchDataBaseHelper> searchHelper=new ThreadLocal<SearchDataBaseHelper>(){
        @Nullable
        @Override
        protected SearchDataBaseHelper initialValue() {
            return new SearchDataBaseHelper(ThreadSafeHelper.getContext());
        }
    };
}


