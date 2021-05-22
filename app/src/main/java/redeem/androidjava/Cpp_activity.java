package redeem.androidjava;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.provider.AlarmClock;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.INotificationSideChannel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.listener.scroll.VerticalRecyclerViewListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PrimitiveIterator;
import java.util.Stack;

import static android.content.ContentValues.TAG;
import static redeem.androidjava.Cpp.BoxHeader;
import static redeem.androidjava.Cpp.fBoxDesc;
import static redeem.androidjava.Cpp.gtBody;
import static redeem.androidjava.Cpp.gtBoxClassLink;
import static redeem.androidjava.RawDatas.AL_TO_STRING;
import static redeem.androidjava.RawDatas.CARD_FLAG_CLASS_JAVA;
import static redeem.androidjava.RawDatas.CARD_FLAG_CONTENT_JAVA;
import static redeem.androidjava.RawDatas.CARD_FLAG_HEADER_PARAMETER;
import static redeem.androidjava.RawDatas.CARD_FLAG_ONLONGCLICKS;
import static redeem.androidjava.RawDatas.STRING_TO_ALS;
import static redeem.androidjava.RawDatas.VT_CODE;
import static redeem.androidjava.RawDatas.VT_LIST;
import static redeem.androidjava.RawDatas.VT_NORMAL;
import static redeem.androidjava.RawDatas.VT_TABLE;
import static redeem.androidjava.RawDatas.blur;
import static redeem.androidjava.RawDatas.dark_color;
import static redeem.androidjava.RawDatas.getCardViewTheme;
import static redeem.androidjava.RawDatas.getIconTheme;
import static redeem.androidjava.RawDatas.getTextContentBrightTheme;
import static redeem.androidjava.RawDatas.getTextContentTheme;
import static redeem.androidjava.RawDatas.getTextTitleTheme;
import static redeem.androidjava.RawDatas.getViewTheme;
import static redeem.androidjava.RawDatas.setNd_toggle;
import static redeem.androidjava.ScrapHandler.*;
import static redeem.androidjava.SearchDataBaseHelper.*;

public class Cpp_activity extends AppCompatActivity {

    private LinearLayout actionBar;
    private RecyclerView recyclerView;
    private TextView title,loadedpromt;
    private ProgressBar progressBar;
    private RelativeLayout mainview;
    private Context mContext=Cpp_activity.this;
    public static boolean LOADED=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpp_activity);
        //all the ui stuff
        actionBar=findViewById(R.id.AB_Cpp_ACT);
        recyclerView=findViewById(R.id.CPP_ACT_RV);
        title=findViewById(R.id.TittleOfContent);
        progressBar=findViewById(R.id.CPP_ACT_PB);
        loadedpromt=findViewById(R.id.CPP_ACT_USR_PRMT);
        mainview=findViewById(R.id.MainView);



        //setting up data
        String hName=getIntent().getExtras().getString("hName");
        title.setText(hName);
        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
        AdapterforCppActivity adapterforCppActivity=new AdapterforCppActivity(mContext);
        adapterforCppActivity.setmListener(cName -> {
            startActivity(new Intent(mContext,CppClassActivity.class).putExtra(CLASS_NAME,cName));

        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterforCppActivity);

        SearchDataBaseHelper dbHelper=new SearchDataBaseHelper(mContext);
        actionBar.setOnClickListener(v -> startActivity(new Intent(mContext,MainActivity.class)));


        //just checking the data base if loaded already or not
        JSONObject jsonObject=null;
        String dat=dbHelper.getValue(hName,HEADER_NAME,DESC_HEADER,TABLE_HEADER);
        if(dat.length()>2)
        {
            try {
                jsonObject=new JSONObject(dat);
                adapterforCppActivity.onDataChange( new HeaderInfo(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else
            {
            AssynForCppAct assynForCppAct = new AssynForCppAct(mContext, hName, adapterforCppActivity, progressBar, loadedpromt);//doin all downlaoding stuff in backgroud
            assynForCppAct.execute(mContext);


            mainview.setOnTouchListener((v, event) -> {
                if (LOADED) {
                    loadedpromt.setVisibility(View.GONE);

                    try {
                        String dat1 =dbHelper.getValue(hName,HEADER_NAME,DESC_HEADER,TABLE_HEADER);
                        Log.e(TAG, "onTouch: "+ dat1);
                        adapterforCppActivity.onDataChange(new HeaderInfo( new JSONObject(dat1)));
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                }
                return false;
            });

        }

        setTheme();


    }

    private void setTheme()
    {
        //main
        mainview.setBackgroundColor(getResources().getColor(getViewTheme()));
        //ttls
        title.setTextColor(getResources().getColor(getTextTitleTheme()));
    }



}
class AssynForCppAct extends AsyncTask<Context,String,String>
{
    private String hLink,hName;
    private AdapterforCppActivity cpAdapeter;
    private ProgressBar progressBar;
    private Context context;
    private TextView updated;
    SearchDataBaseHelper dbHelper;


    public AssynForCppAct(Context context,String hName,AdapterforCppActivity rv,ProgressBar progressBar,TextView textView) {
        this.hName = hName;
        this.progressBar=progressBar;
        this.cpAdapeter=rv;
        this.context=context;
        this.updated=textView;
        dbHelper=new SearchDataBaseHelper(context);
    }

    @Override
    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String s) {
        progressBar.setVisibility(View.GONE);
        if(INTERNET_FLAG==INTERNET_GREEN)
            updated.setVisibility(View.VISIBLE);
        else{
            updated.setVisibility(View.VISIBLE);
            updated.setText("Sorry!Connection Problem.");

        }
        Cpp_activity.LOADED=true;

    }

    @Override
    protected void onProgressUpdate(String... values) {

    }

    @Override
    protected String doInBackground(Context... contexts) {

        try {

           new HeaderInfo(context,hName);//this is if not loaded from internet just loads it
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
//adapter for the main RV
class AdapterforCppActivity extends RecyclerView.Adapter<AdapterforCppActivity.ViewHolder>
{
    private HeaderInfo headerInfo;
    private Context context;
    private onActionListerner mListener;
    private HashMap<Character,Integer> tokens=new HashMap<>();
    private HashMap<Integer,Desc> tokOthers=new HashMap<>();
    private HashMap<Integer,String> tokTtl=new HashMap<>();

    public void setmListener(onActionListerner mListener) {
        this.mListener = mListener;
    }


    public void onDataChange(HeaderInfo headerInfo)
    {
        this.headerInfo=headerInfo;
        notifyDataSetChanged();
    }

    public AdapterforCppActivity(Context context) {
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.card_desc,viewGroup,false);
        ViewHolder viewHolder=new ViewHolder(view);








        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String vt=VT_NORMAL;
        String ttl="";
        String data="";
        String code="";
        Stack<String[]>  list=new Stack<>();

        TableInfo tableInfo=null;

        /*here as we are working with same card with different viewtype
        * so we introduce a token system to check and place the data in its
        * respective view order*/

        //here we just organize the data in the order of recycler view
        if(headerInfo!=null)
        {
            if(!tokens.containsKey('d'))
            {
                tokens.put('d',i);

            }else if(!tokens.containsKey('f')&&headerInfo.hasFunc)
            {
                tokens.put('f',i);
            }else  if(!tokens.containsKey('c')&&headerInfo.hasClass)
            {

                tokens.put('c',i);

            }else if(!headerInfo.descs.empty()&&!headerInfo.dTtl.empty())
            {
                //now archiving the data
                Log.e(TAG, "onBindViewHolder: "+headerInfo.dTtl.peek() );
                tokOthers.put(i,headerInfo.descs.pop());
                tokTtl.put(i,headerInfo.dTtl.pop());


            }

            //now fetching the data
            if(tokens.containsKey('d')&&tokens.get('d').equals(i))
            {
                ttl="Description";
                data=headerInfo.hDesc;
                vt=VT_NORMAL;
            }else if(tokens.containsKey('f') && tokens.get('f').equals(i))
            {
                data="Functions are:";
                ttl="Functions";
                Log.e(TAG, "onBindViewHolder: "+i );
                vt=VT_LIST;
            }
            else if(tokens.containsKey('c')&& tokens.get('c').equals(i))
            {
                data="Classes are:";
                ttl="Class";
                vt=VT_LIST;
            }else if(tokOthers.containsKey(i)&&tokTtl.containsKey(i))
            {
                Desc desc=tokOthers.get(i);
                ttl=tokTtl.get(i);

                data=desc.n_desc==null?"":desc.n_desc;
                vt=VT_NORMAL;
                if(desc.hasCode){
                    code=desc.code;
                    vt=VT_CODE;
                }
                else if(desc.hasList) {
                    vt = VT_LIST;
                    list=desc.list;
                }
                else if (desc.hasTable) {
                    vt = VT_TABLE;
                    tableInfo=desc.tableInfo;
                }


            }


        }
//now making card ready as usual because the oncreate view gives the same inflated
        //layout repeatedly so we so this
        reSetDefault(viewHolder);
        switch (vt)
        {

            case VT_NORMAL:
                viewHolder.normalDesc.setVisibility(View.VISIBLE);
                viewHolder.normalttl.setVisibility(View.VISIBLE);
                viewHolder.normalttl.setText(ttl);
                viewHolder.normalDesc.setText(data);
                break;
            case VT_CODE:
                viewHolder.coderl.setVisibility(View.VISIBLE);
                viewHolder.codeView.setText(code);
                viewHolder.codettl.setText(ttl);
                viewHolder.codedesc.setText(data);
                break;
            case VT_TABLE:
                viewHolder.tableRL.setVisibility(View.VISIBLE);
                viewHolder.tablettl.setText(ttl);
                viewHolder.tabledesc.setText(data);
                viewHolder.tableView.setTableViewListener(new TableActionListener());
                viewHolder.tableView.setAdapter(new AdapterForTable(context,tableInfo));
                break;
            case VT_LIST:
                viewHolder.lstrl.setVisibility(View.VISIBLE);
                viewHolder.lstdesc.setText(data);
                viewHolder.lsttl.setText(ttl);
                viewHolder.lstRv.setLayoutManager(new LinearLayoutManager(context));
                if(ttl.equals("Class"))
                {
                 viewHolder.lstRv.setAdapter(new CppAdapterForClass(context,headerInfo.clsAndDesc));
                }else if(ttl.equals("Functions"))
                {
                    viewHolder.lstRv.setAdapter(new CppAdapterForFunctions(context,headerInfo.functions));
                }
                else{
                    viewHolder.lstRv.setAdapter(new CppAdapterList(context,list));
                }
                break;

        }





    }
    private void reSetDefault(ViewHolder viewHolder)
    {
        if(viewHolder.tableRL.getVisibility()==View.VISIBLE) viewHolder.tableRL.setVisibility(View.GONE);
        if(viewHolder.coderl.getVisibility()==View.VISIBLE) viewHolder.coderl.setVisibility(View.GONE);
        if(viewHolder.lstrl.getVisibility()==View.VISIBLE)viewHolder.lstrl.setVisibility(View.GONE);
        if(viewHolder.normalttl.getVisibility()==View.VISIBLE) viewHolder.normalttl.setVisibility(View.GONE);
        if(viewHolder.normalDesc.getVisibility()==View.VISIBLE) viewHolder.normalDesc.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        if(headerInfo!=null)
        return headerInfo.itemcount;
        else
            return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        //main
        CardView cardView;
        //code
        TextView codettl,codedesc,codeView;
        RelativeLayout coderl;
        //lst
        RelativeLayout lstrl;
        TextView lsttl,lstdesc;
        RecyclerView lstRv;
        //table
        RelativeLayout tableRL;
        TextView tablettl,tabledesc;
        TableView tableView;
         //normal
         TextView normalttl,normalDesc;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //crd
            cardView=itemView.findViewById(R.id.Desc_crd);

            //code
            codettl=itemView.findViewById(R.id.CODE_ttl);
            codedesc=itemView.findViewById(R.id.CODE_Desc);
            coderl=itemView.findViewById(R.id.RC_CODE);
            codeView=itemView.findViewById(R.id.CodeView);
            //lst
            lsttl=itemView.findViewById(R.id.LSt_ttl);
            lstdesc=itemView.findViewById(R.id.LSt_Desc);
            lstrl=itemView.findViewById(R.id.RC_LST);
            lstRv=itemView.findViewById(R.id.LST_RV);
            //table
            tableRL=itemView.findViewById(R.id.RC_TABLE);
            tabledesc=itemView.findViewById(R.id.TableView_desc);
            tablettl=itemView.findViewById(R.id.TableView_ttl);
            tableView=itemView.findViewById(R.id.TableView);
            //normal
            normalDesc=itemView.findViewById(R.id.NORMAL_DESC);
            normalttl=itemView.findViewById(R.id.NORMAL_ttl);


            //theming
            setTheme(itemView);


        }


        private void setTheme(View view)
        {
            //code
            codedesc.setTextColor(view.getResources().getColor(getTextContentTheme()));
            codettl.setTextColor(view.getResources().getColor(getTextTitleTheme()));
            codeView.setTextColor(view.getResources().getColor(getTextTitleTheme()));
            //normal
            normalDesc.setTextColor(view.getResources().getColor(getTextContentTheme()));
            normalttl.setTextColor(view.getResources().getColor(getTextTitleTheme()));

            //lst
            lstdesc.setTextColor(view.getResources().getColor(getTextContentTheme()));
            lsttl.setTextColor(view.getResources().getColor(getTextTitleTheme()));
            //table
            tabledesc.setTextColor(view.getResources().getColor(getTextContentTheme()));
            tablettl.setTextColor(view.getResources().getColor(getTextTitleTheme()));
            //card
            cardView.setCardBackgroundColor(view.getResources().getColor(getCardViewTheme(CARD_FLAG_CLASS_JAVA)));


        }
    }

    //handling any interaction with the activity
    interface onActionListerner
    {
        void onClassClickListener(String cName);
    }






}
//for functions
class CppAdapterForFunctions extends RecyclerView.Adapter<CppAdapterForFunctions.ViewHolder>
{

    private Stack<Function> functions;
    private int item;
    private Context mContext;
    private boolean SquareCard=false;
    private Stack<String> tokens=new Stack<>();//for the items to added

    public CppAdapterForFunctions(Context context,Stack<Function> functions) {
        this.functions = functions;
        item=functions.size();
        mContext=context;
    }

    public CppAdapterForFunctions(Context context,Stack<Function> functions,boolean SquareCard) {
        this.functions = functions;
        this.SquareCard=SquareCard;
        item=functions.size();
        mContext=context;
    }

    @NonNull
    @Override
    public CppAdapterForFunctions.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.card_desc,viewGroup,false);
        return new ViewHolder(view,functions.pop());
    }

    @Override
    public void onBindViewHolder(@NonNull CppAdapterForFunctions.ViewHolder viewHolder, int i) {


        //for the description
        viewHolder.normalttl.setVisibility(View.VISIBLE);
        viewHolder.normalttl.setText(viewHolder.function.fName);


        //now for expand feature
        viewHolder.forfunction.setVisibility(View.GONE);
        viewHolder.normalttl.setOnClickListener(v -> {
            if(viewHolder.forfunction.getVisibility()==View.GONE)
            {
                viewHolder.forfunction.setVisibility(View.VISIBLE);
                viewHolder.normalDesc.setVisibility(View.VISIBLE);
                viewHolder.normalDesc.setText(viewHolder.function.fDesc);
            //for the example
              if(viewHolder.function.hasCode) {
                viewHolder.coderl.setVisibility(View.VISIBLE);
                viewHolder.codettl.setText("Example");
                viewHolder.codedesc.setVisibility(View.GONE);//not needed here so
                viewHolder.codeView.setText(viewHolder.function.Example);
            }
                //for return
                if (viewHolder.function.hasReturns) {
                    viewHolder.normal1.setVisibility(View.VISIBLE);
                    viewHolder.normalttl1.setText("Return");
                    viewHolder.normaldesc1.setText(viewHolder.function.Return.length()>2?viewHolder.function.Return:"void i.e Nothing!");

                }
                //for parameter
                if (viewHolder.function.hasParameter) {
                    viewHolder.lstrl.setVisibility(View.VISIBLE);
                    viewHolder.lsttl.setText("Parameters");
                    viewHolder.lstdesc.setVisibility(View.GONE);
                    viewHolder.lstRv.setLayoutManager(new LinearLayoutManager(mContext));
                    viewHolder.lstRv.setAdapter(new CppAdapterList(mContext, viewHolder.function.parAndDesc, true));

                }
            }else {
                viewHolder.forfunction.setVisibility(View.GONE);
                viewHolder.normalDesc.setVisibility(View.GONE);
            }

        });




    }

    @Override
    public int getItemCount() {
        return item;
    }
    class ViewHolder extends RecyclerView.ViewHolder
    {
        //data
        Function function;
        //main
        CardView cardView;
        RelativeLayout forfunction;
        //code
        TextView codettl,codedesc,codeView;
        RelativeLayout coderl;
        //lst
        RelativeLayout lstrl;
        TextView lsttl,lstdesc;
        RecyclerView lstRv;
        //table
        RelativeLayout tableRL;
        TextView tablettl,tabledesc;
        TableView tableView;
        //normal
        TextView normalttl,normalDesc;
        //normal 1
        RelativeLayout normal1;
        TextView normalttl1,normaldesc1;


        public ViewHolder(@NonNull View itemView,Function function) {
            super(itemView);
            //crd
            cardView=itemView.findViewById(R.id.Desc_crd);
            if(SquareCard) {cardView.setRadius(0);
                ViewGroup.MarginLayoutParams params= (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
                params.setMargins(5,5,5,5);
                cardView.setLayoutParams(params);
            }
            forfunction=itemView.findViewById(R.id.ForFunction);
            //code
            codettl=itemView.findViewById(R.id.CODE_ttl);
            codedesc=itemView.findViewById(R.id.CODE_Desc);
            coderl=itemView.findViewById(R.id.RC_CODE);
            codeView=itemView.findViewById(R.id.CodeView);
            //table
            tableRL=itemView.findViewById(R.id.RC_TABLE);
            tabledesc=itemView.findViewById(R.id.TableView_desc);
            tablettl=itemView.findViewById(R.id.TableView_ttl);
            tableView=itemView.findViewById(R.id.TableView);
            //normal
            normalDesc=itemView.findViewById(R.id.NORMAL_DESC);
            normalttl=itemView.findViewById(R.id.NORMAL_ttl);
            //lst
            lsttl=itemView.findViewById(R.id.LSt_ttl);
            lstdesc=itemView.findViewById(R.id.LSt_Desc);
            lstrl=itemView.findViewById(R.id.RC_LST);
            lstRv=itemView.findViewById(R.id.LST_RV);
            lstRv.setPadding(0,0,0,0);

            //normal 1
            normalttl1=itemView.findViewById(R.id.NORMAL_ttl1);
            normaldesc1=itemView.findViewById(R.id.NORMAL_DESC1);
            normal1=itemView.findViewById(R.id.RC_NORMAL1);
            //data
            this.function=function;

            setTheme(itemView);
        }
        private void setTheme(View view)
        {
            //code
            codedesc.setTextColor(view.getResources().getColor(getTextContentTheme()));
            codettl.setTextColor(view.getResources().getColor(getTextTitleTheme()));
            codeView.setTextColor(view.getResources().getColor(getTextTitleTheme()));
            //normal
            normalDesc.setTextColor(view.getResources().getColor(getTextContentTheme()));
            normalttl.setTextColor(view.getResources().getColor(getTextTitleTheme()));
            normalttl1.setTextColor(view.getResources().getColor(getTextTitleTheme()));
            normaldesc1.setTextColor(view.getResources().getColor(getTextContentTheme()));
            //table
            tabledesc.setTextColor(view.getResources().getColor(getTextContentTheme()));
            tablettl.setTextColor(view.getResources().getColor(getTextTitleTheme()));
            //lst
            lstdesc.setTextColor(view.getResources().getColor(getTextContentTheme()));
            lsttl.setTextColor(view.getResources().getColor(getTextTitleTheme()));
            //card
            cardView.setCardBackgroundColor(view.getResources().getColor(getCardViewTheme(CARD_FLAG_CLASS_JAVA)));



        }
    }
}
//for clases
class CppAdapterForClass extends RecyclerView.Adapter<CppAdapterForClass.ViewHolder>
{


    private Stack<String[]> CandD;
    private Context mContext;






    public CppAdapterForClass(Context context,Stack<String[]> candD) {
    CandD = candD;
    mContext=context;
}

    @NonNull
    @Override
    public CppAdapterForClass.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.card_desc,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CppAdapterForClass.ViewHolder viewHolder, int i) {
        if(CandD.get(i).length==2) {
            viewHolder.normalDesc.setVisibility(View.VISIBLE);
            viewHolder.normalttl.setVisibility(View.VISIBLE);
            viewHolder.normalDesc.setText(CandD.get(i)[0]);
            viewHolder.normalDesc.setText(CandD.get(i)[1]);
        }

    }

    @Override
    public int getItemCount() {
        return CandD.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder
    {
        //normal
        TextView normalttl,normalDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //normal
            normalDesc=itemView.findViewById(R.id.NORMAL_DESC);
            normalttl=itemView.findViewById(R.id.NORMAL_ttl);
            normalttl.setOnClickListener(v -> {
                String cName=CandD.get(getAdapterPosition())[0];




            });
            setTheme(itemView);
        }
        private void setTheme(View view)
        {

            //normal
            normalDesc.setTextColor(view.getResources().getColor(getTextContentTheme()));
            normalttl.setTextColor(view.getResources().getColor(getTextTitleTheme()));



        }
    }


}
//for usual list
class CppAdapterList extends RecyclerView.Adapter<CppAdapterList.ViewHolder>
{


    private Stack<String[]> list;
    private Context mContext;


    public boolean SQUARE=false;//for the card corners


    public CppAdapterList(Context context,Stack<String[]> candD) {
        this.list = candD;
        mContext=context;

    }
    public CppAdapterList(Context context,Stack<String[]> candD,boolean sqaureCrd) {
        this.list = candD;
        this.SQUARE=sqaureCrd;
        mContext=context;

    }

    @NonNull
    @Override
    public CppAdapterList.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.card_desc,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CppAdapterList.ViewHolder viewHolder, int i) {
        viewHolder.normalDesc.setVisibility(View.VISIBLE);
        viewHolder.normalttl.setVisibility(View.VISIBLE);
        viewHolder.normalttl.setText(list.get(i)[0]);
        viewHolder.normalDesc.setText(list.get(i)[1]);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder
    {

        //normal
        TextView normalDesc,normalttl;
        //card
        CardView crd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            normalDesc=itemView.findViewById(R.id.NORMAL_DESC);
            normalttl=itemView.findViewById(R.id.NORMAL_ttl);
            crd=itemView.findViewById(R.id.Desc_crd);
            setTheme(itemView);
        }
        private void setTheme(View view)
        {

            //card
            crd.setCardBackgroundColor(view.getResources().getColor(getCardViewTheme(CARD_FLAG_HEADER_PARAMETER)));
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(0,0,0,0);
            crd.setLayoutParams(p);
            if(SQUARE) crd.setRadius(0);
            crd.setPadding(0,0,0,0);

            //normal
            normalDesc.setTextColor(view.getResources().getColor(getTextContentTheme()));
            normalttl.setTextColor(view.getResources().getColor(getTextTitleTheme()));



        }

    }
}
