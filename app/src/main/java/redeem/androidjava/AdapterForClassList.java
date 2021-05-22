package redeem.androidjava;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import static redeem.androidjava.ListOfClasses.*;
import static redeem.androidjava.RawDatas.clsMCDCrdSetThemes;
import static redeem.androidjava.RawDatas.getTextContentTheme;
import static redeem.androidjava.SearchDataBaseHelper.CLASS_DESCRIPTION;
import static redeem.androidjava.SearchDataBaseHelper.CLASS_NAME;
import static redeem.androidjava.SearchDataBaseHelper.TABLE_CLASS_DESC;

public class AdapterForClassList extends RecyclerView.Adapter<AdapterForClassList.ViewHolderForClass> {

AdapterForClassList.OnItemClickListener mlistener;
    private Context context;
    private  ArrayList<String> clases;
    private HashMap<String,Integer> orgPsition=new HashMap<>();
    private HashMap<Integer,String> dupPosition=new HashMap<>();
    private SearchDataBaseHelper dbHelper;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    private int flag=RawDatas.GREEN;




    public void filter(ArrayList<String> filterItem, HashMap<String, Integer> orgPsition) {
        clases=filterItem;
        this.orgPsition=orgPsition;
        notifyDataSetChanged();

    }
    public void dataChange()
    {
        this.dbHelper=new SearchDataBaseHelper(context);
        notifyDataSetChanged();
    }


    public interface  OnItemClickListener
    {
        void  onItemClick(int position);
        void onTouchListener(String cName,ProgressBar progressBar,TextView cDesc,int flag);
        void onLongClickListener(String cName,TextView cDesc);
    }

    public void setOnItemClickListener(AdapterForClassList.OnItemClickListener listener)
    {
        this.mlistener=listener;
    }

    public class  ViewHolderForClass extends RecyclerView.ViewHolder {




       private TextView mClasses,mClassDesc;
       private ProgressBar progressBar;
        public ViewHolderForClass(@NonNull View itemView,final AdapterForClassList.OnItemClickListener listener) {
            super(itemView);
            mClasses=itemView.findViewById(R.id.ClassName);
            mClassDesc=itemView.findViewById(R.id.ClassDescrition);
            progressBar=itemView.findViewById(R.id.ClassProgressBar);
            progressBar.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        int position;
                        if(!orgPsition.isEmpty())
                         position=orgPsition.get(dupPosition.get(getAdapterPosition()));
                        else position=getAdapterPosition();//incase noone activated search re listing
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }

                }
            });






        }
    }
public AdapterForClassList(Context context, ArrayList<String> Clases){
        this.context=context;
        this.clases=Clases;
        this.dbHelper=new SearchDataBaseHelper(context);


}



    @NonNull
    @Override
    public ViewHolderForClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.l_clases,viewGroup,false);
        lstClsCrdSetTheme(view);
        ViewHolderForClass obj = new ViewHolderForClass(view,this.mlistener);
        return obj;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderForClass viewHolderForClass, int i) {
        dupPosition.put(i,clases.get(i));
        viewHolderForClass.mClasses.setText(clases.get(i));
        if(dbHelper.getCount(TABLE_CLASS_DESC,CLASS_NAME,clases.get(i))!=0)
        {
            viewHolderForClass.mClassDesc.setText(dbHelper.getValue(clases.get(i),CLASS_NAME,CLASS_DESCRIPTION,TABLE_CLASS_DESC));
            viewHolderForClass.progressBar.setVisibility(View.GONE);
        }

        else viewHolderForClass.mClassDesc.setText("");
        //loads the cDescOnTouching
        viewHolderForClass.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mlistener.onTouchListener(String.valueOf(viewHolderForClass.mClasses.getText()),viewHolderForClass.progressBar,viewHolderForClass.mClassDesc,getFlag());
                return false;
            }
        });
        viewHolderForClass.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mlistener.onLongClickListener(clases.get(i),viewHolderForClass.mClassDesc);
                return false;
            }
        });


    }

    @Override
    public int getItemCount() {
        return clases.size();
    }
}
