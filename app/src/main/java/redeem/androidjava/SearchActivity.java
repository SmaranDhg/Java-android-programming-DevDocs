package redeem.androidjava;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;

import java.net.URI;
import java.util.ArrayList;

import static android.view.HapticFeedbackConstants.*;
import static redeem.androidjava.MainActivity.*;
import static redeem.androidjava.R.id.Methods_on_Click;
import static redeem.androidjava.R.id.masked;
import static redeem.androidjava.R.id.title;
import static redeem.androidjava.RawDatas.APacakge;
import static redeem.androidjava.RawDatas.CARD_FLAG_CONTENT_JAVA;
import static redeem.androidjava.RawDatas.CLASS;
import static redeem.androidjava.RawDatas.blur;
import static redeem.androidjava.RawDatas.getCardViewTheme;
import static redeem.androidjava.RawDatas.getIconTheme;
import static redeem.androidjava.RawDatas.getTextContentTheme;
import static redeem.androidjava.RawDatas.getTextTitleTheme;
import static redeem.androidjava.RawDatas.getViewTheme;
import static redeem.androidjava.RawDatas.libraries;
import static redeem.androidjava.RawDatas.light_color;
import static redeem.androidjava.RawDatas.openSettings;
import static redeem.androidjava.SearchActivity.*;
import static redeem.androidjava.SearchDataBaseHelper.*;

public class SearchActivity extends AppCompatActivity implements AssycResult{
    private static  final String TAG="search_activity";
    private EditText search;
    private NestedScrollView scrollView;
    private ImageView search_icon;
    private ImageView home_icon;
    private ImageView search_icon_selected;
    private ImageView mOnCpIcon;
    private RecyclerView resultRecyler,frequentRecycler,activitiesRecycler;
    private TextView results;
    private TextView frequents;
    private TextView activities;
    private TextView tittle;
    private int scrollPosY=0,scrollPosX=0;
    private TextView mOnCmNames;
    private TextView mOnCmDesc;
    private TextView mOnCcName;
    public static RelativeLayout resultView,mainView;
    private LinearLayout bNab,titleView;
    private static  final int BREAK_POINT=10;
    private static  final int NUMBER_GRAMS=4;
    private static ImageView srchCdPlatIcon;
    private static TextView srchCdresultSet;
    private static TextView srchCdresultCat;
    private static AdapterForResultSet Radapter;
    private static CardView mOnC,srchMainView;
    private CardView crdResult,searchBar,initailLoad;
    private SimplePermisionListener msimplePermisionListener;
    private ProgressBar progressBarCircular,progressBarHorizontal;
    private TextView progressBarProgressText;






    private static ArrayList<String> Methods = new ArrayList<>();
    private static ArrayList<String>  classes = new ArrayList<>();
    private  static ArrayList<String>  packages= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_search);
        msimplePermisionListener=new SimplePermisionListener(this);
        Dexter.withActivity(this).withPermission(Manifest.permission.VIBRATE).withListener(msimplePermisionListener).check();
        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(msimplePermisionListener).check();
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(msimplePermisionListener).check();


        search=findViewById(R.id.search_bar_edt);
        search_icon=findViewById(R.id.search_icon);
        scrollView=findViewById(R.id.Content);
        home_icon=findViewById(R.id.homeIcon);
        resultView=findViewById(R.id.Results);
        tittle=findViewById(R.id.Title);
        mainView=findViewById(R.id.MainView);
        resultRecyler=findViewById(R.id.RecycleViewForSearchResult);
        frequentRecycler=findViewById(R.id.RecycleViewForFrequentSearch);
        activitiesRecycler=findViewById(R.id.RecycleViewForActivities);
        bNab=findViewById(R.id.BNab);
        searchBar=findViewById(R.id.SearchBar);
        titleView=findViewById(R.id.TitleView);
        mOnC=findViewById(Methods_on_Click);
        mOnCmDesc=findViewById(R.id.MethodsDescResults);
        mOnCmNames=findViewById(R.id.MethodsResults);
        mOnCcName=findViewById(R.id.MethodsClassName);
        mOnCpIcon=findViewById(R.id.MethodsPlatIcon);
        search_icon_selected=findViewById(R.id.searchIcon);
        resultRecyler=findViewById(R.id.RecycleViewForSearchResult);
        results=findViewById(R.id.title_result);
        results.setVisibility(View.GONE);
        activities=findViewById(R.id.title_activity);
        frequents=findViewById(R.id.title_frequent);
        crdResult=findViewById(R.id.reCrd);
        progressBarCircular=findViewById(R.id.ProgressBarCircular);
        progressBarHorizontal=findViewById(R.id.ProgressBarHorizontal);
        initailLoad=findViewById(R.id.ProgressBar);
        progressBarProgressText=findViewById(R.id.ProgressBarProgressText);






      //setting themes
        setTheme();



        scrollView.setSmoothScrollingEnabled(true);
        scrollView.setNestedScrollingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scrollView.setNestedScrollingEnabled(true);
        }


        home_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        threadPool.submit(()->{
            search.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    search.setFocusable(true);
                    
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInputFromWindow(v.getWindowToken(),0,1);
                    search.setTextSize(20);
                    search_icon.setPadding(10,10,10,10);


                    resultView.setVisibility(LinearLayout.VISIBLE);
                    searchThing(search,SearchActivity.this);
                    tittle.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {

                            minimizeSearch();
                            resultView.setVisibility(View.GONE);
                            search.getText().clear();

                            return false;
                        }



                    });




                    return false;
                }
            });

        });

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

               minimizeSearch();
                return false;
            }
        });





        ArrayList<String> dum= new ArrayList<>();
        ArrayList<Character[]> cat = new ArrayList<>();
        Character[] a={'n','n'};
        dum.add("NULL");
        cat.add(a);

        //now setting up the recylerView for the search activity
        Radapter=new AdapterForResultSet(dum,cat,SearchActivity.this);
        Radapter.setOnItemClickListener(new AdapterForResultSet.OnItemClickListener() {


            @Override
            public void onItemClick(String pName, Character plat) {
                if(plat.equals('a'))
                {
                    startActivity(new Intent(SearchActivity.this,Catagorized_Classes.class).putExtra("positions",pName));
                }
                else startActivity(new Intent(SearchActivity.this,ListOfClasses.class).putExtra("LibraryPosition",pName));

            }

            @Override
            public void onItemClick(String pName, String cName, Character plat) {
                //for adding the methods
                threadPool.submit(()->{
                    SearchDataBaseHelper dataBaseHelper = new SearchDataBaseHelper(SearchActivity.this);
                    dataBaseHelper.insertMethods(pName,cName,String.valueOf(plat),SearchActivity.this);

                });
                if(plat.equals('a')) {
                    Log.d(TAG, "onItemClick: "+pName);

                    startActivity(new Intent(SearchActivity.this, A_Classes_Activity.class).putExtra("cName", cName).putExtra("pName", pName));
                }else {
                    startActivity(new Intent(SearchActivity.this, Classes.class).putExtra("ClassIndex", cName).putExtra("Librarypostion", pName));
                }

            }

            @Override
            public void onItemClick(String mName, String mDesc, Character platform, String m) {

                if(m.equals("on"))
                {
                    getWindow().getDecorView().performHapticFeedback(VIRTUAL_KEY,FLAG_IGNORE_VIEW_SETTING);
                    manageBackgroungBlur();
                    mOnC.setVisibility(View.VISIBLE);
                    mOnCmDesc.setText(mDesc);
                    mOnCmNames.setText(mName);
                    //for selecting the icon acording to the platform
                    int pic=platform.equals('a')?R.drawable.android:R.drawable.java;
                    mOnCpIcon.setImageDrawable(getResources().getDrawable(pic));
                    mOnCcName.setText(getValue(mName,METHOD_NAME,CLASS_NAME,TABLE_METHODS,SearchActivity.this));
                    mainView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resoreSearchActivity();
                        }
                    });
                    minimizeSearch();
                }
                else {
                    resoreSearchActivity();
                }

            }
        });


        resultRecyler.setNestedScrollingEnabled(true);
        LinearLayoutManager Rmanager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        Rmanager.setSmoothScrollbarEnabled(true);
        resultRecyler.setLayoutManager(Rmanager);
        resultRecyler.setAdapter(Radapter);
        resultRecyler.setFocusable(false);

        //for firsttime travesing
        SearchDataBaseHelper helper = new SearchDataBaseHelper(this);
        int i = 0;
        i = helper.getCount(TABLE_CLASSES,CLASS_NAME);
       if(msimplePermisionListener.isState() && i==0) {
          // clearForBeginner();
           new AssyncHelper(SearchActivity.this, progressBarHorizontal, progressBarCircular, progressBarProgressText, SearchActivity.this).execute(SearchActivity.this);
       }




    }




    private static  synchronized void searchThing(final EditText search,Context context)
    {
         search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            try {
                                filter(editable.toString().toLowerCase(),context);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    });



        }

    @Override
    public void onBackPressed() {
        if(initailLoad.getVisibility()==View.VISIBLE)
        {
            Toast.makeText(SearchActivity.this,"Please! wait.",Toast.LENGTH_LONG).show();
        }
        else super.onBackPressed();
    }

    private synchronized static void filter(String text, Context context) throws InterruptedException {

        ArrayList<String> datas  = new ArrayList<>();
        ArrayList<Character[]> cats =  new ArrayList<>();
        SearchDataBaseHelper dataBaseHelper = new SearchDataBaseHelper(context);


        if (text.startsWith("c ")) {


            for (String s : dataBaseHelper.getItemList(TABLE_CLASSES, CLASS_NAME, text.substring(2), BREAK_POINT))//static contents are imported so
            {
                Log.d(TAG, "filter: " + s);
                datas.add(s);

                Character a[] = {'n', 'c'};
                cats.add(a);


                if (datas.size() > BREAK_POINT) break;


            }
        }

        threadPool.submit(()->{

            if(text.length()>4 || text.startsWith("c ")) {


                    int i = 0;
                    for (String s : dataBaseHelper.getItemList(TABLE_CLASSES, CLASS_NAME, text, BREAK_POINT))//statoc contents are imported so
                    {
                        Log.d(TAG, "filter: " + s);
                        datas.add(s);

                        Character a[] = {'n', 'c'};
                        cats.add(a);
                        if (i > BREAK_POINT) break;
                        if (datas.size() > BREAK_POINT) break;
                        i++;

                    }


                }
           // }

        });


        threadPool.submit(()->{


            for(String s : RawDatas.APacakge)
            {

                if(s.toLowerCase().contains(text))
                {
                    datas.add(s);
                    Character a[]={'a','p'};
                    cats.add(a);
                    if(datas.size()>BREAK_POINT) break;
                }

            }
            for(String s: RawDatas.libraries)
            {
                if(s.toLowerCase().contains(text)){
                    datas.add(s);
                    Character a[]={'j','p'};
                    cats.add(a);
                    if(datas.size()>BREAK_POINT) break;
                }

            }


        });

        if(text.startsWith("m "))

        {
//
            for(String s: getItemList(TABLE_METHODS,METHOD_NAME,text.substring(2),BREAK_POINT,context))
            {
                datas.add(s);
                Character a[]={'n','m'};
                cats.add(a);
                if(datas.size()>BREAK_POINT) break;

            }
        }




        Radapter.filter(datas,cats);





    }




    private  void minimizeSearch()
    {
        if(search_icon.getPaddingLeft()==10)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(search_icon.getWindowToken(),0);

            Log.d(TAG, "onBackPressed: ");
            search_icon.setPadding(0,0,0,0);
            search.setTextSize(27);

        }
    }

    private  void clearSearchActivity()
    {

        scrollPosY=scrollView.getScrollY();
        scrollPosX=scrollView.getScrollX();
        titleView.setVisibility(View.GONE);
        bNab.setVisibility(View.GONE);
        scrollView.setVisibility(View.GONE);
        searchBar.setVisibility(View.GONE);


    }

    public void resoreSearchActivity()
    {

        titleView.setVisibility(View.VISIBLE);
        bNab.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.VISIBLE);
        setTheme();
        mOnC.setVisibility(View.GONE);
        initailLoad.setVisibility(View.GONE);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                scrollView.smoothScrollBy(scrollPosX,scrollPosY);
                scrollView.smoothScrollTo(scrollPosX,scrollPosY);
            }
        });



    }
    private void manageBackgroungBlur()
    {
        if(titleView.getVisibility()==View.VISIBLE)
        {

            Bitmap image = blur(mainView,10f);
            mainView.setBackgroundDrawable(new BitmapDrawable(SearchActivity.this.getResources(), image));
            clearSearchActivity();
        }

    }





    private void setTheme()
    {
        //ImageView
        search_icon_selected.setColorFilter(getIconTheme());
        search_icon.setColorFilter(getIconTheme());
        home_icon.setColorFilter(getIconTheme());
        mOnCpIcon.setColorFilter(getIconTheme());

        //mainView
        mainView.setBackgroundColor(getResources().getColor(getViewTheme()));
        //crdView
        searchBar.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_CONTENT_JAVA)));
        crdResult.setCardBackgroundColor(getResources().getColor(getViewTheme()));
        mOnC.setCardBackgroundColor(getResources().getColor(getCardViewTheme(1)));

        //textview
        tittle.setTextColor(getResources().getColor(getTextTitleTheme()));
        results.setTextColor(getResources().getColor(getTextContentTheme()));
        activities.setTextColor(getResources().getColor(getTextContentTheme()));
        frequents.setTextColor(getResources().getColor(getTextContentTheme()));
        mOnCcName.setTextColor(getResources().getColor(getTextContentTheme()));
        mOnCmNames.setTextColor(getResources().getColor(getTextContentTheme()));
        mOnCmDesc.setTextColor(getResources().getColor(getTextContentTheme()));




    }
    public static void srchSetTheme(View view)
    {
        srchCdPlatIcon=view.findViewById(R.id.search_result_icon);
        srchCdresultSet=view.findViewById(R.id.search_result);
        srchCdresultCat=view.findViewById(R.id.search_result_category);
        srchMainView=view.findViewById(R.id.srchMainView);
        //imageview
        srchCdPlatIcon.setColorFilter(getIconTheme());
        //textview
        srchCdresultSet.setTextColor(view.getResources().getColor(getTextContentTheme()));
        srchCdresultCat.setTextColor(view.getResources().getColor(getTextContentTheme()));
        //mainview
        srchMainView.setCardBackgroundColor(view.getResources().getColor(getViewTheme()));
    }

    public static void  actvtSetTheme(View view)
    {
        //imageView
        ImageView resultIcon;
        resultIcon=view.findViewById(R.id.activities_result_icon);
        resultIcon.setColorFilter(getIconTheme());

        //mainview
        CardView mainView;
        mainView=view.findViewById(R.id.actvtMainView);
        mainView.setCardBackgroundColor(view.getResources().getColor(getViewTheme()));

        //TextView
        TextView pNameTitle,pName,cNameTitle,cName,mtitle,ctitle;
        pName=view.findViewById(R.id.activities_pName);
        pNameTitle=view.findViewById(R.id.activities_pName_title);
        cName=view.findViewById(R.id.activities_cName);
        cNameTitle=view.findViewById(R.id.activities_cName_title);
        mtitle=view.findViewById(R.id.activities_methods_title);
        ctitle=view.findViewById(R.id.activities_constant_title);
        pName.setTextColor(view.getResources().getColor(getTextContentTheme()));
        pNameTitle.setTextColor(view.getResources().getColor(getTextContentTheme()));
        cName.setTextColor(view.getResources().getColor(getTextContentTheme()));
        cNameTitle.setTextColor(view.getResources().getColor(getTextContentTheme()));
        mtitle.setTextColor(view.getResources().getColor(getTextContentTheme()));
        ctitle.setTextColor(view.getResources().getColor(getTextContentTheme()));
    }

    public static void frqntSetTheme(View view)
    {
        //imageview
        ImageView platIcon;
        platIcon=view.findViewById(R.id.frequents_icon);
        platIcon.setColorFilter(getIconTheme());
        //mainView
        CardView mainView;
        mainView=view.findViewById(R.id.frqntMainView);
        mainView.setCardBackgroundColor(view.getResources().getColor(getViewTheme()));
        //textView
        TextView resultSet,extra1,extra2;
        resultSet=view.findViewById(R.id.frequents_class_name);
        extra1=view.findViewById(R.id.frequents_extra1);
        extra2=view.findViewById(R.id.frequents_extra2);
        resultSet.setTextColor(view.getResources().getColor(getTextContentTheme()));
        resultSet.setTextColor(view.getResources().getColor(getTextContentTheme()));




    }

    @Override
    public void clearForBeginner() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                manageBackgroungBlur();
                initailLoad.setVisibility(View.VISIBLE);
            }
        },60);

    }

    @Override
    public void restoreDefault() {
        titleView.setVisibility(View.VISIBLE);
        bNab.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);
        searchBar.setVisibility(View.VISIBLE);
        setTheme();
        mOnC.setVisibility(View.GONE);
        initailLoad.setVisibility(View.GONE);

    }






}

//so that on finising the task we can restore to default
interface AssycResult
{
    void clearForBeginner();
    void restoreDefault();
}
class AssyncHelper extends AsyncTask<Context,String,Boolean>
{


  private ProgressBar progressBarCircular,progressBarHorizontal;
  private TextView textProgress;
  private Context context;
  private AssycResult result;

    public AssyncHelper(Context context,ProgressBar p,ProgressBar p2,TextView t1,AssycResult result) {
        this.context = context;
        this.progressBarHorizontal= p;
        this.progressBarCircular= p2;
        this.textProgress= t1;
        this.result=result;

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onPreExecute() {


        result.clearForBeginner();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBarCircular.setProgress(0,true);
            progressBarHorizontal.setProgress(0,true);
        }else
        {
            progressBarCircular.setProgress(0);
            progressBarHorizontal.setProgress(0);
        }
        progressBarHorizontal.setMax(100);



    }

    @Override
    protected void onPostExecute(Boolean aVoid) {

        result.restoreDefault();

    }

    @Override
    protected void onProgressUpdate(String... values) {
        //just those class name here and there
        Log.d("progress", "onProgressUpdate: "+values[0]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            progressBarHorizontal.setProgress(Integer.parseInt(values[1]),true);
            textProgress.setText("Traversing:"+values[0]);
        }else {
            progressBarHorizontal.setProgress(Integer.parseInt(values[1]));
            textProgress.setText("Traversing:" + values[0]);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected Boolean doInBackground(Context... contexts) {

        Context context=contexts[0];
        {
            SearchDataBaseHelper dataBaseHelper = new SearchDataBaseHelper(context);
            dataBaseHelper.freshUpSearch();
            ThreadSafeHelper threadSafeHelper= new ThreadSafeHelper(context);
            int p=0;
            int tot=APacakge.length+libraries.length;
            for(String packs: APacakge) {

                int prog= (p*100) /tot;

                for (String cName : threadSafeHelper.helper.get().getItemList(packs, CLASS)) {
                    publishProgress(cName, String.valueOf(prog));
                    threadSafeHelper.searchHelper.get().insertClass(packs,cName,"ac");
                   // Log.d("traversing", cName );
                   // textProgress.setText("Traversing:"+cName);
                }



                p++;

            }

            for(String packs: libraries) {
                int prog= (p*100) /tot;
                for (String cName : threadSafeHelper.helper.get().getItemList(packs, CLASS)) {
                    publishProgress(cName, String.valueOf(prog));
                    threadSafeHelper.searchHelper.get().insertClass(packs,cName,"jc");
                   // textProgress.setText("Traversing:"+cName);
                  //  Log.d("traversing", cName);
                }



                p++;



            }

        }







        return true;
    }
}
