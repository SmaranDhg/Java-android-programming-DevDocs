package redeem.androidjava;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import static redeem.androidjava.R.id.Methods_on_Click;
import static redeem.androidjava.R.id.scrollView;
import static redeem.androidjava.RawDatas.*;
import static redeem.androidjava.RawDatas.CARD_FLAG_CLASS_JAVA;
import static redeem.androidjava.RawDatas.blur;
import static redeem.androidjava.RawDatas.getCardViewTheme;
import static redeem.androidjava.RawDatas.getIconTheme;
import static redeem.androidjava.RawDatas.getTextContentBrightTheme;
import static redeem.androidjava.RawDatas.getTextContentTheme;
import static redeem.androidjava.RawDatas.getTextTitleTheme;
import static redeem.androidjava.RawDatas.getViewTheme;
import static redeem.androidjava.SearchDataBaseHelper.*;
import static redeem.androidjava.SearchDataBaseHelper.CLASS_NAME;
import static redeem.androidjava.SearchDataBaseHelper.METHOD_DESCRIPTION;
import static redeem.androidjava.SearchDataBaseHelper.METHOD_NAME;
import static redeem.androidjava.SearchDataBaseHelper.TABLE_METHODS;
import static redeem.androidjava.SearchDataBaseHelper.getValue;

public class Classes extends AppCompatActivity implements Serializable {


    private String positionClass, positionLibrary;

    private ItemAdapter adapter;
    private ImageView Nbbutton;
    private TextView ClassTitle, usedFor, mtdTl, constructor, ExIm,cnstktrTtl;
    private EditText editText;
    private CardView crd1, crd2,crd3;
    private ImageView mOnCpIcon;

   private ArrayList<GroupModel> MethodsList = new ArrayList<>();
   private ArrayList<ItemModel> DescitpionList = new ArrayList<>();
   private ArrayList<String> Constructor = new ArrayList<>();
   private ArrayList<String> Methods = new ArrayList<>();
   private ArrayList<String> MethodDesc = new ArrayList<>();
   private String exim,usedfor;
   private RelativeLayout mainView;
   private LinearLayout actionBar;
    private TextView mOnCmNames;
    private TextView mOnCmDesc;
    private TextView mOnCcName;
    private static CardView mOnC;
    private  NestedScrollView scrollView;
    private int scrollPosY=0,scrollPosX=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_classes);

        this.positionClass = getIntent().getExtras().getString("ClassIndex");
        this.positionLibrary = getIntent().getExtras().getString("Librarypostion");

        //now for data
        helperClass helper=new helperClass(getApplicationContext());
        Methods=helper.getItemList(positionLibrary, METHODS,positionClass);
        Constructor=helper.getItemList(positionLibrary, CONSTRUCTOR,positionClass);
        MethodDesc=helper.getItemList(positionLibrary, METHODS_DESCRIPTION,positionClass);
        exim=helper.getText(positionLibrary, EXIM,positionClass);
        SearchDataBaseHelper mHelper=new SearchDataBaseHelper(this);
        if(mHelper.getCount(TABLE_CLASS_DESC,CLASS_NAME,positionClass)!=0)
        {
            exim=mHelper.getValue(positionClass,CLASS_NAME,cEX_IM,TABLE_CLASS_DESC);
        }
        usedfor=helper.getText(positionLibrary, USEDFOR,positionClass);




//just for smooth scrollling effect
        scrollView = findViewById(R.id.ContenstScrollbar);
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.setNestedScrollingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scrollView.setNestedScrollingEnabled(true);
        }

        crd1=findViewById(R.id.crd1);
        crd2 = findViewById(R.id.crd2);
        crd3 = findViewById(R.id.crd3);

        Nbbutton = findViewById(R.id.navigationbutton);
        ClassTitle = findViewById(R.id.ClassTitle);
        usedFor = findViewById(R.id.UsedFor);
        constructor = findViewById(R.id.Constructor);
        mainView=findViewById(R.id.MainView);
        cnstktrTtl=findViewById(R.id.cnstktrTtl);
        mtdTl=findViewById(R.id.mtdTtl);
        ExIm = findViewById(R.id.ExtendsImplements);
        actionBar=findViewById(R.id.ActionBarForClass);
        mOnC=findViewById(Methods_on_Click);
        mOnCmDesc=findViewById(R.id.MethodsDescResults);
        mOnCmNames=findViewById(R.id.MethodsResults);
        mOnCcName=findViewById(R.id.MethodsClassName);
        mOnCpIcon=findViewById(R.id.MethodsPlatIcon);


        //themse
        setTheme();

        Nbbutton.setImageDrawable(getResources().getDrawable(R.drawable.home));
        Nbbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Classes.this, MainActivity.class));
            }
        });

        crd2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.bounch);
                    animation.setDuration(1000);
                    v.startAnimation(animation);





                return false;
            }
        });

        crd1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.bounch);
                animation.setDuration(1000);
                v.startAnimation(animation);
                return false;
            }
        });

        setTittles(ClassTitle);
        setConstructor(constructor);
        setTextToExIm(ExIm);
        setUsedFor(usedFor);

        RecyclerView recyclerView = findViewById(R.id.RVFOrmethods);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ItemAdapter(setMethods());
        adapter.setItemTouchListener(new ItemAdapter.OnItemClickTouch() {
            @Override
            public void onLongClick(String mName, String cName,String mDesc) {
                manageBackgroungBlur();
                mOnCmDesc.setText(mDesc);
                mOnCmNames.setText(mName);
                //for selecting the icon acording to the platform
                String cat=getValue(mName,METHOD_NAME,CAT,TABLE_METHODS,Classes.this);
                int pic=cat.equals("a")?R.drawable.android:R.drawable.java;
                mOnCpIcon.setImageDrawable(getResources().getDrawable(pic));
                mOnCcName.setText(cName);
                mainView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restoreActivity();
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setFocusable(false);


    }


    //classe
    private void setTittles(TextView textView) {
        textView.setText(positionClass);

    }

    @Override
    public void onBackPressed() {
        if(mOnC.getVisibility()==View.VISIBLE)
            restoreActivity();
        else
        super.onBackPressed();
    }

    //ExIm
    private void setTextToExIm(TextView textView) {
        textView.setTextSize(18);
        textView.setText("EXTENSION/IMPLEMENTAION:\n" + exim);

    }

    //And its used
    private void setUsedFor(TextView textView) {
        textView.setTextSize(18);
        textView.setText(usedfor);
    }

    //for Constructor
    private void setConstructor(TextView textView) {
        textView.setTextSize(18);
        for (int i = 0; i < Constructor.size(); i++) {
            if (i == 0)
                textView.setText(Constructor.get(0) + "\n");
            else
                textView.append(Constructor.get(i) + "\n");

        }

    }


    private ArrayList<GroupModel> setMethods() {
        int l1=Methods.size();
        int l2=MethodDesc.size();
        //to tackele index bound exception
        int a=l1<=l2?l1:l2;
        for (int i = 0; i < a; i++) {

        MethodsList.add(new GroupModel(Methods.get(i), new ItemModel(MethodDesc.get(i)),positionClass));

    }



        return MethodsList;

    }

    private void setTheme()
    {
        //mainview
        mainView.setBackgroundColor(getResources().getColor(getViewTheme()));
        //iv
        Nbbutton.setColorFilter(getIconTheme());
        //tv
        ClassTitle.setTextColor(getResources().getColor(getTextTitleTheme()));
        ExIm.setTextColor(getResources().getColor(getTextContentBrightTheme()));
        constructor.setTextColor(getResources().getColor(getTextContentBrightTheme()));
        usedFor.setTextColor(getResources().getColor(getTextContentBrightTheme()));
        cnstktrTtl.setTextColor(getResources().getColor(getTextContentTheme()));
        mtdTl.setTextColor(getResources().getColor(getTextContentTheme()));
        //crdView
        crd1.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_CLASS_JAVA)));
        crd2.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_CLASS_JAVA)));
        crd3.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_CLASS_JAVA)));
        //mOnC
        mOnC.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_ONLONGCLICKS)));
        mOnCcName.setTextColor(getResources().getColor(getTextContentBrightTheme()));
        mOnCmDesc.setTextColor(getResources().getColor(getTextContentBrightTheme()));
        mOnCmNames.setTextColor(getResources().getColor(getTextContentBrightTheme()));
        mOnCpIcon.setColorFilter(getIconTheme());



    }
    private void manageBackgroungBlur()
    {

        if(crd1.getVisibility()==View.VISIBLE)
        {
            Bitmap image = blur(getWindow().getDecorView().getRootView(),10f);
            mainView.setBackgroundDrawable(new BitmapDrawable(Classes.this.getResources(), image));

        }
        clearActivity();

    }
    private  void clearActivity()
    {
        scrollPosY=scrollView.getScrollY();
        scrollPosX=scrollView.getScrollX();
        crd1.setVisibility(View.GONE);
        crd2.setVisibility(View.GONE);
        crd3.setVisibility(View.GONE);
        actionBar.setVisibility(View.GONE);
        mOnC.setVisibility(View.VISIBLE);

    }
    private  void restoreActivity()
    {
        crd3.setVisibility(View.VISIBLE);
        crd2.setVisibility(View.VISIBLE);
        crd1.setVisibility(View.VISIBLE);
        actionBar.setVisibility(View.VISIBLE);
        setTheme();
        mOnC.setVisibility(View.GONE);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            scrollView.smoothScrollBy(scrollPosX,scrollPosY);
            scrollView.smoothScrollTo(scrollPosX,scrollPosY);
        }
    });

        Log.d("SCROLL", scrollPosY+"clearActivity: "+scrollPosX);


    }



}
