package redeem.androidjava;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import static redeem.androidjava.R.id.Methods_on_Click;
import static redeem.androidjava.RawDatas.CARD_FLAG_CLASS_ANDROID;
import static redeem.androidjava.RawDatas.CARD_FLAG_CLASS_JAVA;
import static redeem.androidjava.RawDatas.CARD_FLAG_CONTENT_ANDROID;
import static redeem.androidjava.RawDatas.CARD_FLAG_ONLONGCLICKS;
import static redeem.androidjava.RawDatas.blur;
import static redeem.androidjava.RawDatas.getCardViewTheme;
import static redeem.androidjava.RawDatas.getIconTheme;
import static redeem.androidjava.RawDatas.getTextContentBrightTheme;
import static redeem.androidjava.RawDatas.getTextContentTheme;
import static redeem.androidjava.RawDatas.getTextTitleTheme;
import static redeem.androidjava.RawDatas.getViewTheme;
import static redeem.androidjava.SearchDataBaseHelper.CAT;
import static redeem.androidjava.SearchDataBaseHelper.METHOD_NAME;
import static redeem.androidjava.SearchDataBaseHelper.TABLE_METHODS;
import static redeem.androidjava.SearchDataBaseHelper.getValue;

public class A_Classes_Activity extends AppCompatActivity {


    private int positions[];

    private ImageView Nbbutton;
    private TextView ClassTitle, usedFor, constructor, ExIm,cnstktrTtl,constTtl,mthdTtl;
  private LinearLayout actionBar;
    private TextView mOnCmNames;
    private TextView mOnCmDesc;
    private TextView mOnCcName;
    private static CardView mOnC;
    private ImageView mOnCpIcon;


    private ArrayList<A_groupModel_Constants> ConstantsList = new ArrayList<>();
    private ArrayList<A_roupModel_methods> MethodsList = new ArrayList<>();
    private CardView crd1, crd2,crd3,crd4;
    private RelativeLayout mainView;


    private   ArrayList<String> Constructor = new ArrayList<>();
    private ArrayList<String> Methods = new ArrayList<>();
    private ArrayList<String> MethodDesc = new ArrayList<>();
    private  ArrayList<String> Constant=new ArrayList<>();
    private  ArrayList<String> ConstantDesc = new ArrayList<>();
    private String exim,usedfor,className,packageName;
    private int scrollPosY=0,scrollPosX=0;
    private  NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_a__classes_);



        Nbbutton = findViewById(R.id.Anavigationbutton);
        ClassTitle = findViewById(R.id.AClassTitle);
        usedFor = findViewById(R.id.AUsedFor);
        constructor = findViewById(R.id.AConstructor);
        ExIm = findViewById(R.id.AExtendsImplements);
        cnstktrTtl=findViewById(R.id.constTtl);
        constTtl=findViewById(R.id.cntTtl);
        mthdTtl=findViewById(R.id.mthdTtl);
        crd1=findViewById(R.id.firstStuff);
        crd2=findViewById(R.id.Secondsstuff);
        crd3=findViewById(R.id.ThirdStuff);
        crd4=findViewById(R.id.fourthStuff);
        mainView=findViewById(R.id.MainView);
        actionBar=findViewById(R.id.AActionBarForClass);
        //selectedCase
        mOnC=findViewById(Methods_on_Click);
        mOnCmDesc=findViewById(R.id.MethodsDescResults);
        mOnCmNames=findViewById(R.id.MethodsResults);
        mOnCcName=findViewById(R.id.MethodsClassName);
        mOnCpIcon=findViewById(R.id.MethodsPlatIcon);


        helperClass helper= new helperClass(getApplicationContext());
        className=getIntent().getExtras().getString("cName");
        packageName=getIntent().getExtras().getString("pName");



        usedfor=helper.getText(packageName,RawDatas.USEDFOR,className);
        exim=helper.getText(packageName,RawDatas.EXIM,className);
        Constructor=helper.getItemList(packageName,RawDatas.CONSTRUCTOR,className);
        Methods=helper.getItemList(packageName,RawDatas.METHODS,className);
        MethodDesc=helper.getItemList(packageName,RawDatas.METHODS_DESCRIPTION,className);
        Constant=helper.getItemList(packageName,RawDatas.CONSTANTS,className);
        ConstantDesc=helper.getItemList(packageName,RawDatas.CONSTANTS_DESCRIPTION,className);







//just for smooth scrollling effect
        scrollView = findViewById(R.id.AContenstScrollbar);
        scrollView.setSmoothScrollingEnabled(true);
        scrollView.fullScroll(View.FOCUS_UP);
        scrollView.setNestedScrollingEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scrollView.setNestedScrollingEnabled(true);
        }

findViewById(R.id.firstStuff).setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.bounch);
        animation.setDuration(1000);
        v.startAnimation(animation);
        return false;
    }
});

        findViewById(R.id.Secondsstuff).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.bounch);
                animation.setDuration(1000);
                v.startAnimation(animation);
                return false;
            }
        });





        Nbbutton.setImageDrawable(getResources().getDrawable(R.drawable.home));
        Nbbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(A_Classes_Activity.this,MainActivity.class));
            }
        });

        setTittles(ClassTitle);
        setConstructor(constructor);
        setTextToExIm(ExIm);
        setUsedFor(usedFor);

        RecyclerView recyclerViewMethods = findViewById(R.id.ARVFOrmethods), recyclerViewCopnstants = findViewById(R.id.ARVFOrConstants);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
//so that scroll view will satart from the top not from the recylerviews
        recyclerViewCopnstants.setFocusable(false);
        recyclerViewMethods.setFocusable(false);

        recyclerViewMethods.setLayoutManager(linearLayoutManager);
        recyclerViewCopnstants.setLayoutManager(new LinearLayoutManager(this));

        A_ItemAPadpter_For_Mehtods adapter = new A_ItemAPadpter_For_Mehtods(setMethods());
        A_itemAdapter_for_Constats madapter = new A_itemAdapter_for_Constats(setConstants());
        adapter.setOnLongClickListener(new A_ItemAPadpter_For_Mehtods.OnLongClickListener() {
            @Override
            public void onLongClickListner(String cName, String mName,String mDesc) {
                manageBackgroungBlur();
                mOnCcName.setText(cName);
                mOnCmNames.setText(mName);
                //for selecting the icon acording to the platform
                String cat=getValue(mName,METHOD_NAME,CAT,TABLE_METHODS,A_Classes_Activity.this);
                int pic=cat.equals("a")?R.drawable.android:R.drawable.java;
                mOnCpIcon.setImageDrawable(getResources().getDrawable(pic));
                mOnCmDesc.setText(mDesc);
                mainView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restoreActivity();
                    }
                });
            }
        });

        recyclerViewMethods.setAdapter(adapter);
        recyclerViewCopnstants.setAdapter(madapter);

        //themeing
        setTheme();


    }

    @Override
    public void onBackPressed() {
        if(mOnC.getVisibility()==View.VISIBLE)
            restoreActivity();
        else
            super.onBackPressed();
    }


    //classe
    private void setTittles(TextView textView) {
        textView.setText(className);

    }

    //ExIm
    private void setTextToExIm(TextView textView) {
        textView.setTextSize(18);
        textView.setText("EXTENSION/IMPLEMENTAION:\t" + exim);

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
                textView.setText(Constructor.get(i) + "\n");
            else
                textView.append(Constructor.get(i) + "\n");

        }

    }


    private ArrayList<A_roupModel_methods> setMethods() {

        for (int i = 0; i < Methods.size(); i++) {

            MethodsList.add(new A_roupModel_methods(Methods.get(i), Collections.singletonList(new A_itemModel_methods(MethodDesc.get(i))),className));

        }
        return MethodsList;

    }

    private ArrayList<A_groupModel_Constants> setConstants() {

        for (int i = 0; i < Constant.size(); i++) {

            ConstantsList.add(new A_groupModel_Constants(Constant.get(i), Collections.singletonList(new A_itemModel_Constants(ConstantDesc.get(i)))));

        }
        return ConstantsList;

    }

    private void setTheme()
    {
        //mainview
        mainView.setBackgroundColor(getResources().getColor(getViewTheme()));
        //iv
        Nbbutton.setColorFilter(getIconTheme());
        //textView
        ClassTitle.setTextColor(getResources().getColor(getTextTitleTheme()));
        ExIm.setTextColor(getResources().getColor(getTextContentBrightTheme()));
        usedFor.setTextColor(getResources().getColor(getTextContentBrightTheme()));
        constTtl.setTextColor(getResources().getColor(getTextContentTheme()));
        constructor.setTextColor(getResources().getColor(getTextContentBrightTheme()));
        cnstktrTtl.setTextColor(getResources().getColor(getTextContentTheme()));
        constTtl.setTextColor(getResources().getColor(getTextContentTheme()));
        mthdTtl.setTextColor(getResources().getColor(getTextContentTheme()));
        //cardview
        crd4.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_CLASS_ANDROID)));
        crd2.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_CLASS_ANDROID)));
        crd3.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_CLASS_ANDROID)));
        crd1.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_CLASS_ANDROID)));
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
            mainView.setBackgroundDrawable(new BitmapDrawable(A_Classes_Activity.this.getResources(), image));
            clearActivity();


        }

    }
    private  void clearActivity()
    {
        scrollPosY=scrollView.getScrollY();
        scrollPosX=scrollView.getScrollX();
        crd1.setVisibility(View.GONE);
        crd2.setVisibility(View.GONE);
        crd4.setVisibility(View.GONE);
        crd3.setVisibility(View.GONE);
        actionBar.setVisibility(View.GONE);
        mOnC.setVisibility(View.VISIBLE);

    }
    private  void restoreActivity()
    {
        crd3.setVisibility(View.VISIBLE);
        crd2.setVisibility(View.VISIBLE);
        crd4.setVisibility(View.VISIBLE);
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



    }


}

