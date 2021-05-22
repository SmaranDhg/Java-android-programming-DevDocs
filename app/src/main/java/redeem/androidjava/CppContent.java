package redeem.androidjava;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;

import static redeem.androidjava.Cpp.BoxHeader;
import static redeem.androidjava.Cpp.CPP_MAIN_URL;
import static redeem.androidjava.Cpp.gtBody;
import static redeem.androidjava.Cpp.gtHDesc;
import static redeem.androidjava.Cpp.gtHeader;
import static redeem.androidjava.Cpp.gtHeaders;
import static redeem.androidjava.Cpp.gtHeadersCat;
import static redeem.androidjava.Cpp.gtLink;
import static redeem.androidjava.Cpp.gtSecHd;
import static redeem.androidjava.CppContent.cntnJvCrdSetTheme;
import static redeem.androidjava.RawDatas.CARD_FLAG_CONTENT_JAVA;
import static redeem.androidjava.RawDatas.CARD_FLAG_ONLONGCLICKS;
import static redeem.androidjava.RawDatas.GREEN;
import static redeem.androidjava.RawDatas.HEADER_LINK_SEP;
import static redeem.androidjava.RawDatas.blur;
import static redeem.androidjava.RawDatas.getCardViewTheme;
import static redeem.androidjava.RawDatas.getIconTheme;
import static redeem.androidjava.RawDatas.getTextContentTheme;
import static redeem.androidjava.RawDatas.getTextTitleTheme;
import static redeem.androidjava.RawDatas.getViewTheme;
import static redeem.androidjava.RawDatas.libraries;
import static redeem.androidjava.SearchDataBaseHelper.CAT;
import static redeem.androidjava.SearchDataBaseHelper.DESC_HEADER;
import static redeem.androidjava.SearchDataBaseHelper.HEADER_NAME;
import static redeem.androidjava.SearchDataBaseHelper.KEY;
import static redeem.androidjava.SearchDataBaseHelper.TABLE_ARCHIVE;
import static redeem.androidjava.SearchDataBaseHelper.TABLE_HEADER;
import static redeem.androidjava.SearchDataBaseHelper.VALUE;

public class CppContent extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tittle,pOnCcatName,pOnCpDesc,pOnCpName;
    private ImageView platIcon,nbIcon;
    private RelativeLayout mainView;
    private CardView pOnCCrd;
    private ImageView pOnCCatIcon;
    private ProgressBar pOnCprogBar;
    private LinearLayout actionBar;
    private Context context=CppContent.this;




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
        platIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MainActivity.class));
            }
        });
        tittle.setText("<Headers>");
        platIcon.setImageDrawable(getResources().getDrawable(R.drawable.cpp));
        setTheme();





       AdapterCppContent madapter=new AdapterCppContent(context);
       madapter.setClickListener(new AdapterCppContent.onClickListener() {
           @Override
           public void setOnClickListener(String hName) {
               startActivity(new Intent(context,Cpp_activity.class).putExtra("hName",hName));
           }

           @Override
           public void setOnLongClickListener(String hName) {

           }
       });

        recyclerView=findViewById(R.id.RecycleViewForLibrary);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(madapter);

        ArrayList<String> headers;
        SearchDataBaseHelper helper=new SearchDataBaseHelper(context);
        headers=helper.getItems(TABLE_ARCHIVE,KEY,CPP_MAIN_URL,VALUE);
        if(headers.size()==0)
        new AssyncCppAdt(context,madapter).execute(context);
        else madapter.onDataChage(headers);




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
            mainView.setBackgroundDrawable(new BitmapDrawable(CppContent.this.getResources(), image));

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
class AssyncCppAdt  extends AsyncTask<Context,String,String>
{
    public AssyncCppAdt(Context context, AdapterCppContent madapter) {
        this.context = context;
        this.madapter = madapter;
        this.dbHelper=new SearchDataBaseHelper(context);

    }

    private Context context;
    private AdapterCppContent madapter;
    private ArrayList<String> hNames=new ArrayList<>();
    private SearchDataBaseHelper dbHelper;

    @Override
    protected void onProgressUpdate(String... values) {
        hNames.add(values[0]);
        dbHelper.insertVal(values[0],values[1]);
        madapter.onDataChage(hNames);
 }

    @Override
    protected void onPostExecute(String s) {
        //now after loading the header name we just start extracting the header data
        for(String header: hNames)
        {
            try {
                //jst to make sure not loading loaded one
                if(dbHelper.getValue(header,HEADER_NAME,DESC_HEADER,TABLE_HEADER).length()<2)
                new HeaderInfo(context,header);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected String doInBackground(Context... contexts) {
        try {
            for(Element hCat:gtHeadersCat())
            {
                try {
                    for(Element header:gtHeaders(hCat))
                    {
                        publishProgress(header.text(),gtLink(header));

                    }
                }catch (Exception e)
                {
                    continue;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();


        }

    return null;
    }


}

//adapert of the list
 class AdapterCppContent extends RecyclerView.Adapter<AdapterCppContent.ViewHolder> {
    public AdapterCppContent(Context context) {
        this.context = context;


    }

    private Context context;
    private onClickListener mlistener;
    private  ArrayList<String> headers=new ArrayList<>();





    public  void onDataChage(ArrayList<String> data)
    {
        headers=data;
        notifyDataSetChanged();
    }



    public  void setClickListener(onClickListener mlistener)
    {
        this.mlistener=mlistener;
    }

    @NonNull
    @Override
    public AdapterCppContent.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.l_libraries,viewGroup,false);

        cntnJvCrdSetTheme(view);
        return  new ViewHolder(view,mlistener);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCppContent.ViewHolder viewHolder, int i) {
        viewHolder.libraryName.setText(headers.get(i));
        //for descritption of header
        {
            //now for the data in the database or for the  header that is loaded
            SearchDataBaseHelper helper=new SearchDataBaseHelper(context);
            String desc=helper.getValue(headers.get(i),HEADER_NAME,DESC_HEADER,TABLE_HEADER);
            if(desc.length()>2)
            {
                viewHolder.libreyDesc.setText(desc);
            }

        }



    }

    @Override
    public int getItemCount() {
        return headers.size();
    }
    interface onClickListener
    {
        void setOnClickListener(String hName);
        void setOnLongClickListener(String hName);
    }
    class  ViewHolder extends RecyclerView.ViewHolder
    {

        private TextView libraryName,libreyDesc;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View v,onClickListener mListener) {
            super(v);
            libraryName=v.findViewById(R.id.LibraryName);
            libreyDesc=v.findViewById(R.id.LibraryDescrition);
            progressBar=v.findViewById(R.id.LibraryProgressBar);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.setOnClickListener(libraryName.getText().toString());

                }
            });

        }

    }

}

