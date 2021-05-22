package redeem.androidjava;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static redeem.androidjava.RawDatas.CARD_FLAG_CATPACKCLASS_ANDROID;
import static redeem.androidjava.RawDatas.CARD_FLAG_ONLONGCLICKS;
import static redeem.androidjava.RawDatas.GREEN;
import static redeem.androidjava.RawDatas.RED;
import static redeem.androidjava.RawDatas.blur;
import static redeem.androidjava.RawDatas.getCardViewTheme;
import static redeem.androidjava.RawDatas.getIconTheme;
import static redeem.androidjava.RawDatas.getIndicatorTheme;
import static redeem.androidjava.RawDatas.getTextContentTheme;
import static redeem.androidjava.RawDatas.getTextTitleTheme;
import static redeem.androidjava.RawDatas.getViewTheme;
import static redeem.androidjava.SearchDataBaseHelper.*;

public class A_categorized_packageList extends AppCompatActivity {


    private String LibraryName;
    private TextView title,pOnCcatName,pOnCpDesc,pOnCpName;
    private RelativeLayout mainView;
    private CardView pOnCCrd;
    private ImageView pOnCCatIcon;
    private ProgressBar pOnCprogBar;
    private LinearLayout actionBar;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_a_categorized_package_list);

        //getting the extra from intent here the library Name
         this.LibraryName = getIntent().getExtras().getString("library");

        ArrayList<String> catNAm = new ArrayList<>();
        HashMap<String, ArrayList<String>> listHashMap = new HashMap<>();

        title=findViewById(R.id.ATittleOfContent);
        mainView=findViewById(R.id.MainView);
        title.setText(this.LibraryName);
        pOnCCatIcon=findViewById(R.id.pOnCcatIcon);
        pOnCCrd=findViewById(R.id.pOnCCrd);
        pOnCpDesc=findViewById(R.id.pOnCpDesc);
        pOnCcatName=findViewById(R.id.pOnCcatName);
        pOnCpName=findViewById(R.id.pOnCpName);
        pOnCprogBar=findViewById(R.id.pOnCprogBar);
        actionBar=findViewById(R.id.AActionBarForClassrr);

        setTheme();
        for (String catName:RawDatas.Cat_packages) {

            catNAm.add(catName);
            listHashMap.put(catName, RawDatas.getCategory(catName));
            Log.d("a_cat_pack", "cat: "+catName);

        }

        recyclerView = findViewById(R.id.RVForExpandablestuffPackages);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        A_Adapter_For_Catagorized_PakageList adapter = new A_Adapter_For_Catagorized_PakageList(listHashMap, catNAm, A_categorized_packageList.this,title,LibraryName);
        adapter.setOnTouchListener(new A_Adapter_For_Catagorized_PakageList.onTouchListerner() {

            @Override
            public void onTouch(String pName, int AFlag) {
                String pDesc = getValue(pName,PACKAGE_NAME,PACKAGE_DESCRIPTION,TABLE_PACKAGE,A_categorized_packageList.this);
                if(AFlag==GREEN && pDesc.length()!=0)
                {
                    
                }
            }
        });
        recyclerView.setAdapter(adapter);


    }

    private void setTheme()
    {
        //tv
        title.setTextColor(getResources().getColor(getTextTitleTheme()));
        //mainview
        mainView.setBackgroundColor(getResources().getColor(getViewTheme()));
        //pOnC
        pOnCcatName.setTextColor(getResources().getColor(getTextContentTheme()));
        pOnCpDesc.setTextColor(getResources().getColor(getTextContentTheme()));
        pOnCpName.setTextColor(getResources().getColor(getTextContentTheme()));
        pOnCCatIcon.setColorFilter(getIconTheme());
        pOnCCrd.setCardBackgroundColor(getResources().getColor(getCardViewTheme(CARD_FLAG_ONLONGCLICKS)));

    }

    public static void crdSetTheme(View view)
    {
        //cardView
        CardView mainView;
        mainView=view.findViewById(R.id.expdCrdMainview);
        mainView.setCardBackgroundColor(view.getResources().getColor(getCardViewTheme(CARD_FLAG_CATPACKCLASS_ANDROID)));
        //traingle
        View view1;
        view1=view.findViewById(R.id.exprTriangle);
        view1.setBackground(view.getResources().getDrawable(getIndicatorTheme()));
        //textView
        TextView titleCat;
        titleCat=view.findViewById(R.id.textView_name);
        titleCat.setTextColor(view.getResources().getColor(getTextContentTheme()));



    }
    private void manageBackgroungBlur()
    {

        if(pOnCCrd.getVisibility()==View.GONE)
        {
            Bitmap image = blur(mainView,15f);
            mainView.setBackgroundDrawable(new BitmapDrawable(A_categorized_packageList.this.getResources(), image));

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

class AssyncForCatPackList extends AsyncTask<Context,String,String>
{
    private TextView pDesc,pName;
    private String pNameStr;

    public AssyncForCatPackList(TextView pDesc, TextView pName, String pNameStr) {
        this.pDesc = pDesc;
        this.pName = pName;
        this.pNameStr = pNameStr;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(String s) {

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(Context... contexts) {



        return null;
    }
}
