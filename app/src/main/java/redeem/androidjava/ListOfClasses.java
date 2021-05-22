package redeem.androidjava;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static redeem.androidjava.AdapterForClassList.*;
import static redeem.androidjava.MainActivity.threadPool;
import static redeem.androidjava.RawDatas.*;
import static redeem.androidjava.RawDatas.CARD_FLAG_CONTENT_JAVA;
import static redeem.androidjava.RawDatas.GREEN;
import static redeem.androidjava.RawDatas.RED;
import static redeem.androidjava.RawDatas.blur;
import static redeem.androidjava.RawDatas.getCardViewTheme;
import static redeem.androidjava.RawDatas.getTextContentTheme;
import static redeem.androidjava.RawDatas.getTextTitleTheme;
import static redeem.androidjava.RawDatas.getViewTheme;
import static redeem.androidjava.ScrapHandler.eximSep;

public class ListOfClasses extends AppCompatActivity {

    private   AdapterForClassList madapter;
    private ArrayList<String> classes=new ArrayList<>();
    private TextView tittle;
    private EditText editText;
    private RelativeLayout mainView;
    private CardView cDescOnCCrd;
    private TextView cDescOnCcName,cDescOnCpName,cDescOnCcDesc;
    private ProgressBar cDescOnCprogressBar;
    private LinearLayout actionBar,Content;
    private AssyncHelperLOC assyncHelperLOC=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_of_classes);

        tittle = findViewById(R.id.TittleOfContent);
        editText=findViewById(R.id.EditText);
        mainView=findViewById(R.id.MainView);
        cDescOnCCrd=findViewById(R.id.cListOnCCrd);
        cDescOnCCrd.setVisibility(View.GONE);
        cDescOnCcDesc=findViewById(R.id.cListOnCcDesc);
        cDescOnCcName=findViewById(R.id.cListOnCcName);
        cDescOnCpName=findViewById(R.id.cListOnCpName);
        cDescOnCprogressBar=findViewById(R.id.cListOnCprogBar);
        cDescOnCprogressBar.setVisibility(View.GONE);
        actionBar=findViewById(R.id.cListActionBar);
        Content=findViewById(R.id.cListContent);
        final String title=getIntent().getExtras().getString("LibraryPosition");
        tittle.setText(title);

        //themes
        setTheme();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });

        //for sending the data to view the class
        helperClass helper=new helperClass(this);
        this.classes= helper.getItemList(title, CLASS);



        RecyclerView recyclerView= findViewById(R.id.RecycleViewForClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        madapter = new AdapterForClassList(this,classes);
        madapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ListOfClasses.this,Classes.class);
                intent.putExtra("ClassIndex",classes.get(position));
                intent.putExtra("Librarypostion",title);
                startActivity(intent);
                threadPool.submit(()->{
                    SearchDataBaseHelper dataBaseHelper = new SearchDataBaseHelper(ListOfClasses.this);
                    dataBaseHelper.insertMethods(title,classes.get(position),"j",ListOfClasses.this);


                });
            }

            @Override
            public void onTouchListener(String cName, ProgressBar progressBar, TextView cDesc,int flag) {
                if(cDesc.getText().length()==0 && flag==GREEN) {//flag helpes controling the unnessary object construction
                    assyncHelperLOC = new AssyncHelperLOC(cDesc, cDescOnCcDesc, progressBar, cDescOnCprogressBar, title, cName, madapter);
                    assyncHelperLOC.execute(ListOfClasses.this);
                }
               //just quick progress stuff
                else if(cDesc.getText().length()==0 && flag==RED){
                    progressBar.setVisibility(View.VISIBLE);
                    cDescOnCprogressBar.setVisibility(View.VISIBLE);

                }
                else if(cDesc.getText().length()!=0)
                {
                    cDescOnCprogressBar.setVisibility(View.GONE);

                }
            }

            @Override
            public void onLongClickListener(String cName, TextView cDesc) {
                Log.d("length", "onLongClickListener: "+cDesc.getText().length());
                if(cDesc.getText().length()==0)
                {
                    manageBackgroungBlur();
                    cDescOnCpName.setText(title);
                    cDescOnCcName.setText(cName);
                    cDescOnCcDesc.setText("");//so that previously inflated output does not comes
                    mainView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            restoreActivity();
                        }
                    });
                }
                else
                {
                    manageBackgroungBlur();
                    cDescOnCpName.setText(title);
                    cDescOnCcName.setText(cName);
                    cDescOnCcDesc.setText(cDesc.getText());
                    mainView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            restoreActivity();
                        }
                    });

                }
            }
        });
recyclerView.setAdapter(madapter);



    }


    //for searching stuff
    private void filter(String text) {
        ArrayList<String> filterItem = new ArrayList<>();
         HashMap<String,Integer> orgPsition=new HashMap<>();
         int i=0;
        for(String s:classes)
        {
            orgPsition.put(s,i);
            if(s.toLowerCase().contains(text.toLowerCase()))
            {
                filterItem.add(s);
            }
            i++;
        }
        madapter.filter(filterItem,orgPsition);


    }

    private void setTheme()
    {
        //tv
        tittle.setTextColor(getResources().getColor(getTextTitleTheme()));
        cDescOnCpName.setTextColor(getResources().getColor(getTextContentTheme()));
        cDescOnCcName.setTextColor(getResources().getColor(getTextContentTheme()));
        cDescOnCcDesc.setTextColor(getResources().getColor(getTextContentTheme()));
        //mainview
        mainView.setBackgroundColor(getResources().getColor(getViewTheme()));
        //crdView
        cDescOnCCrd.setCardBackgroundColor(getResources().getColor(R.color.CardColorDark));



    }

    @Override
    public void onBackPressed() {
        if(cDescOnCCrd.getVisibility()==View.VISIBLE)
        restoreActivity();
        else
        super.onBackPressed();
    }

    public static void lstClsCrdSetTheme(View view)
    {
        //crdView
        CardView lClass=view.findViewById(R.id.clsMCDCrd3);
        lClass.setCardBackgroundColor(view.getResources().getColor(getCardViewTheme(CARD_FLAG_CONTENT_JAVA)));
        //tv
        TextView textView=view.findViewById(R.id.ClassName),cDesc=view.findViewById(R.id.ClassDescrition);
        cDesc.setTextColor(view.getResources().getColor(getTextContentTheme()));
        textView.setTextColor(view.getResources().getColor(getTextContentTheme()));


    }
    private void manageBackgroungBlur()
    {

        if(cDescOnCCrd.getVisibility()==View.GONE)
        {
            Bitmap image = blur(mainView,15f);
            mainView.setBackgroundDrawable(new BitmapDrawable(ListOfClasses.this.getResources(), image));

        }
        clearActivity();

    }
    private  void clearActivity()
    {
        actionBar.setVisibility(View.GONE);
        Content.setVisibility(View.GONE);
        cDescOnCCrd.setVisibility(View.VISIBLE);

    }
    private  void restoreActivity()
    {

        Content.setVisibility(View.VISIBLE);
        actionBar.setVisibility(View.VISIBLE);
        setTheme();
        cDescOnCCrd.setVisibility(View.GONE);


    }

}

class AssyncHelperLOC extends AsyncTask<Context,String,String>
{


    public AssyncHelperLOC(TextView cDesc, TextView cDesc1, ProgressBar progressBar,ProgressBar progressBar1, String pName, String cName,AdapterForClassList mAdapter) {
        this.cDesc = cDesc;
        this.cDesc1 = cDesc1;
        this.progressBar = progressBar;
        this.progressBar1=progressBar1;
        this.pName = pName;
        this.cName = cName;
        this.mAdapter=mAdapter;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    private TextView cDesc1,cDesc;
    private ProgressBar progressBar;
    private ProgressBar progressBar1;

    public void setcDesc1(TextView cDesc1) {
        this.cDesc1 = cDesc1;
    }

    public void setPb3(ProgressBar pb3) {
        this.pb3 = pb3;
    }

    private ProgressBar pb3;
    private String pName,cName;
    private AdapterForClassList mAdapter;


    @Override
    protected void onPreExecute() {

        cDesc.setText("");//so that it doesnot shows the crab from before classes
        mAdapter.setFlag(RED);//to indicate assync is busy
        progressBar.setVisibility(View.VISIBLE);
        progressBar1.setVisibility(View.VISIBLE);//so that to show the progress



    }

    @Override
    protected void onPostExecute(String s) {
        //now setting the flag to green for other to invoke the assync
        mAdapter.setFlag(GREEN);

    }

    @Override
    protected void onProgressUpdate(String... values) {
        //for the one who is touched or trigger view if you will
        if(values[1].equals("first"))
        {   cDesc.setText(values[0]);
            cDesc1.setText(values[0]);//so that one time works
            progressBar.setVisibility(View.GONE);
            progressBar1.setVisibility(View.GONE);//for both
        }
        else
        {
            mAdapter.dataChange();//so that is shows the data very well

        }




    }

    @Override
    protected String doInBackground(Context... contexts) {

        String cDescStr="Sorry!";
        String item="";

        try {

            helperClass helperClass=new helperClass(contexts[0]);
            SearchDataBaseHelper mdataBaseHelper=new SearchDataBaseHelper(contexts[0]);
            cDescStr= ScrapHandler.getDescriptionOfClass(pName,cName);
            if(!item.startsWith("No Internet Connection,Sorry!"))
              mdataBaseHelper.insertClassDesc(cName,cDescStr.split(eximSep)[0],cDescStr.split(eximSep)[1],"j");
            //first one to be touch should be kept first
            publishProgress(cDescStr.split(eximSep)[0],"first");
            ArrayList<String>  cSs=helperClass.getItemList(pName, CLASS);

            //remaining also auto load
            for(int i=cSs.indexOf(cName);i<cSs.size();i++)
            {
                String cS=helperClass.getItemList(pName, CLASS).get(i);
                item= ScrapHandler.getDescriptionOfClass(pName,cS);
               //item has both implementation and extend in description
                if(!item.startsWith("No Internet Connection,Sorry!"))
                mdataBaseHelper.insertClassDesc(cS,item.split(eximSep)[0],item.split(eximSep)[1],"j");
                publishProgress(cS,"second");

                

            }
        } catch (InterruptedException e) {


        }
        return cDescStr;
    }
}

