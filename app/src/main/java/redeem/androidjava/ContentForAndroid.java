package redeem.androidjava;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static redeem.androidjava.RawDatas.CARD_FLAG_CONTENT_ANDROID;
import static redeem.androidjava.RawDatas.getCardViewTheme;
import static redeem.androidjava.RawDatas.getIconTheme;
import static redeem.androidjava.RawDatas.getTextContentTheme;
import static redeem.androidjava.RawDatas.getTextTitleTheme;
import static redeem.androidjava.RawDatas.getViewTheme;

public class ContentForAndroid extends AppCompatActivity {

    private ImageView nb_nottom;
    private TextView title;
    private RelativeLayout mainView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_content_for_android);
        RecyclerView recyclerView= findViewById(R.id.ARecycleViewForLibrary);
        title=findViewById(R.id.ATittleOfContent);
        mainView=findViewById(R.id.MainView);
        nb_nottom=findViewById(R.id.Anavigationbutton);
        title.setText("Android");
        //themes
        setTheme();


        nb_nottom.setImageDrawable(getResources().getDrawable(R.drawable.android));

        ArrayList<A_libraryInfo> lib_info=new ArrayList<>();
        helperClass helper=new helperClass(getApplicationContext());

        for(String lib:RawDatas.Alibraries)
        {
            lib_info.add(new A_libraryInfo(lib,RawDatas.AlibDesc[0],RawDatas.Cat_packages));
        }


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        A_Adapter_forLibraries mAdapter = new A_Adapter_forLibraries(lib_info);
        recyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new A_Adapter_forLibraries.OnItemClickListener() {
           @Override
           public void onItemClick(int position) {

               startActivity(new Intent(ContentForAndroid.this,A_categorized_packageList.class).putExtra("library",RawDatas.Alibraries[position]));
           }
       });

    }

    private void setTheme()
    {
        //iv
        nb_nottom.setColorFilter(getIconTheme());
        //mv
        mainView.setBackgroundColor(getResources().getColor(getViewTheme()));
        //tv
        title.setTextColor(getResources().getColor(getTextTitleTheme()));


    }

    public static void crdSetTheme(View view)
    {
        //mainview
        CardView mainView;
        mainView=view.findViewById(R.id.cntForAndroid);
        mainView.setCardBackgroundColor(view.getResources().getColor(getCardViewTheme(CARD_FLAG_CONTENT_ANDROID)));
        //textViews
        TextView lName,lDecs,lCats;
        lName=view.findViewById(R.id.ALibraryName);
        lDecs=view.findViewById(R.id.ALibraryDescrition);
        lCats=view.findViewById(R.id.AlibraryCats);
        lName.setTextColor(view.getResources().getColor(getTextContentTheme()));
        lDecs.setTextColor(view.getResources().getColor(getTextContentTheme()));
        lCats.setTextColor(view.getResources().getColor(getTextContentTheme()));



    }


}