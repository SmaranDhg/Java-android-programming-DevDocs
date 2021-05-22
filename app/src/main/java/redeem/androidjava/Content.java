package redeem.androidjava;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static redeem.androidjava.RawDatas.*;

public class Content extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tittle,pOnCcatName,pOnCpDesc,pOnCpName;
    private ImageView platIcon;
    private RelativeLayout mainView;
    private CardView pOnCCrd;
    private ImageView pOnCCatIcon;
    private ProgressBar pOnCprogBar;
    private LinearLayout actionBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this makes the screen full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_content);
        tittle=findViewById(R.id.TittleOfContent);
        platIcon=findViewById(R.id.navigationbutton);
        mainView=findViewById(R.id.MainView);
        pOnCCatIcon=findViewById(R.id.pOnCcatIcon);
        pOnCCrd=findViewById(R.id.pOnCCrd);
        pOnCpDesc=findViewById(R.id.pOnCpDesc);
        pOnCcatName=findViewById(R.id.pOnCcatName);
        pOnCpName=findViewById(R.id.pOnCpName);
        pOnCprogBar=findViewById(R.id.pOnCprogBar);
        actionBar=findViewById(R.id.ActionBarForClassrr);
        platIcon.setImageDrawable(getResources().getDrawable(R.drawable.java));
        tittle.setText("JAVA");
        setTheme();




        final ArrayList<String> arrayList=new ArrayList<>();
        for(int i = 0; i< libraries.length; i++ )
        {
            arrayList.add(libraries[i]);
        }

        recyclerView=findViewById(R.id.RecycleViewForLibrary);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//fetching the data to the array the adaptor
        AdapterforLibraries mAdaptera=new AdapterforLibraries(this,arrayList);
        mAdaptera.setOnItemClickListener(new AdapterforLibraries.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(Content.this,ListOfClasses.class).putExtra("LibraryPosition",arrayList.get(position)));
            }

            @Override
            public void onTouchListener(String pName, TextView libDesc, ProgressBar progressBar,int Flag) {
                if(libDesc.getText().length()==0 && Flag==GREEN)
                new AssyncForJLib(libDesc,pOnCpDesc,pOnCCatIcon,pName,progressBar,pOnCprogBar,mAdaptera).execute(Content.this);
            }

            @Override
            public void onLongClickListener(TextView pDesc, String pName) {
                if(pDesc.length()!=0)
                {
                    manageBackgroungBlur();
                    pOnCpDesc.setText(pDesc.getText());
                    pOnCpName.setText(pName);
                    pOnCprogBar.setVisibility(View.GONE);
                    pOnCCatIcon.setVisibility(View.VISIBLE);

                }
                else
                {
                    manageBackgroungBlur();
                    pOnCpDesc.setText("");
                    pOnCpName.setText(pName);
                    pOnCCatIcon.setVisibility(View.GONE);
                    pOnCprogBar.setVisibility(View.VISIBLE);
                }


            }


        });
        recyclerView.setAdapter(mAdaptera);






    }

    @Override
    public void onBackPressed() {
        if(pOnCCrd.getVisibility()==View.VISIBLE)
            restoreActivity();
        else super.onBackPressed();
    }

    private void setTheme()
    {
        //maiview
        mainView.setBackgroundColor(getResources().getColor(getViewTheme()));
        //iv
        platIcon.setColorFilter(getIconTheme());
        //tv
        tittle.setTextColor(getResources().getColor(getTextTitleTheme()));
        //pOnC
        pOnCcatName.setTextColor(getResources().getColor(getTextContentTheme()));
        pOnCpDesc.setTextColor(getResources().getColor(getTextContentTheme()));
        pOnCpName.setTextColor(getResources().getColor(getTextContentTheme()));
        pOnCCatIcon.setColorFilter(getIconTheme());
        pOnCCrd.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_ONLONGCLICKS)));
    }

    public static void cntnJvCrdSetTheme(View view)
    {
      //vardview
        CardView main;
        main=view.findViewById(R.id.cntnJavaLib);
        main.setCardBackgroundColor(view.getResources().getColor(getCardViewTheme(CARD_FLAG_CONTENT_JAVA)));
        //textView
        TextView libName,libDesc;
        libName=view.findViewById(R.id.LibraryName);
        libDesc=view.findViewById(R.id.LibraryDescrition);
        libDesc.setTextColor(view.getResources().getColor(getTextContentTheme()));
        libName.setTextColor(view.getResources().getColor(getTextContentTheme()));

    }

    private void manageBackgroungBlur()
    {

        if(pOnCCrd.getVisibility()==View.GONE)
        {
            Bitmap image = blur(mainView,15f);
            mainView.setBackgroundDrawable(new BitmapDrawable(Content.this.getResources(), image));

        }
        clearActivity();

    }
    private  void clearActivity()
    {
        actionBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        pOnCCrd.setVisibility(View.VISIBLE);
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreActivity();
            }
        });

    }
    private  void restoreActivity()
    {

        recyclerView.setVisibility(View.VISIBLE);
        actionBar.setVisibility(View.VISIBLE);
        setTheme();
        pOnCCrd.setVisibility(View.GONE);


    }








}
class AssyncForJLib extends AsyncTask<Context,String,String>
{
    public AssyncForJLib(TextView libDesc, TextView pOnCpDesc,ImageView pCatView,String pName,ProgressBar progressBar,ProgressBar mOnProg,AdapterforLibraries madapter) {
        this.libDesc = libDesc;
        this.pName = pName;
        this.mAdapter=madapter;
        this.progressBar=progressBar;
        this.pOnCprogBar=mOnProg;
        this.pOnCpDesc=pOnCpDesc;
        this.pCatIcon=pCatView;

    }

    private TextView libDesc,pOnCpDesc;
    private ImageView pCatIcon;
    private String pName;
    private AdapterforLibraries mAdapter;
    private ProgressBar progressBar,pOnCprogBar;

    @Override
    protected void onPreExecute() {
        mAdapter.setFLAG(RED);
        progressBar.setVisibility(View.VISIBLE);
        pOnCpDesc.setVisibility(View.VISIBLE);
        pCatIcon.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onPostExecute(String aVoid) {
        mAdapter.setFLAG(GREEN);

    }

    @Override
    protected void onProgressUpdate(String... values) {

        if(values[1].equals("first"))
        {
            libDesc.setText(values[0]);
            pOnCpDesc.setText(values[0]);
            pCatIcon.setVisibility(View.GONE);
            pOnCprogBar.setVisibility(View.GONE);


        }
        else
        {
            mAdapter.dataChang();

        }

    }

    @Override
    protected String  doInBackground(Context... contexts) {
        String finalOut;
        String item="";
        try {
            SearchDataBaseHelper mHelper= new SearchDataBaseHelper(contexts[0]);
            finalOut=ScrapHandler.getDescriptionOfPackage(pName);
            if(!item.startsWith("No Internet Connection,Sorry!"))
            mHelper.insertPackage(finalOut,item,"j");
            publishProgress(finalOut,"first");

            int i=0;
            for(String s: libraries)
            {
                item=ScrapHandler.getDescriptionOfPackage(s);
                if(!item.startsWith("No Internet Connection,Sorry!"))
               if( mHelper.insertPackage(s,item,"j")) i++;
                publishProgress(item,"second");
                if(i>15) break;

            }

        } catch (IOException e) {
            finalOut= "Sorry!";

        }
        return  finalOut;
    }
}

