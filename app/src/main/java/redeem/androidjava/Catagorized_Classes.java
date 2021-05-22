package redeem.androidjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static redeem.androidjava.RawDatas.*;

public class Catagorized_Classes extends AppCompatActivity {

    private TextView title;
    private RelativeLayout mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for fulscreen stuff
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_catagorized__classes);


        ArrayList<String> catNAm = new ArrayList<>();
        HashMap<String, ArrayList<String>> listHashMap = new HashMap<>();


         title = findViewById(R.id.ATittleOfContent);
         mainView=findViewById(R.id.MainView);



        final String Defalt = getIntent().getExtras().getString("positions");
        title.setText(Defalt);

        //theme
        setTheme();

        helperClass helper = new helperClass(getApplicationContext());
        catNAm=helper.getItemList(Defalt, CLASS);




        for (String catName:catNAm) {

            listHashMap.put(catName, makeAlist(catName));
        }

        //for fixing data nad layout manager for the recycle view

        RecyclerView recyclerView = findViewById(R.id.RVForExpandablestuff);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        ExpandableRecyclerAdapter adapter = new ExpandableRecyclerAdapter(listHashMap, catNAm, Catagorized_Classes.this,Defalt ,title, Defalt);
        recyclerView.setAdapter(adapter);
    }

    private void setTheme()
    {
        //textview
        title.setTextColor(getResources().getColor(getTextTitleTheme()));
        //mainVIew
        mainView.setBackgroundColor(getResources().getColor(getViewTheme()));

    }



}