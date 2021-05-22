package redeem.androidjava;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import static redeem.androidjava.Content.cntnJvCrdSetTheme;
import static redeem.androidjava.RawDatas.GREEN;
import static redeem.androidjava.SearchDataBaseHelper.*;

public class AdapterforLibraries extends RecyclerView.Adapter<AdapterforLibraries.MyViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private ArrayList<String> LibraryNAme;
    private int FLAG=GREEN;
    private SearchDataBaseHelper mHelper;


    public void setFLAG(int FLAG) {
        this.FLAG = FLAG;
    }



    public interface  OnItemClickListener
    {
        void  onItemClick(int position);
        void onTouchListener(String s, TextView libreyDesc, ProgressBar progressBar, int flag);
        void onLongClickListener(TextView pDesc,String pName);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener=listener;
    }


    public void dataChang()
    {
        notifyDataSetChanged();
        mHelper=new SearchDataBaseHelper(context);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder  {


      private   TextView libraryName,libreyDesc;
      private ProgressBar progressBar;


        public MyViewHolder(View v, final OnItemClickListener listener) {
            super(v);
            libraryName=v.findViewById(R.id.LibraryName);
            libreyDesc=v.findViewById(R.id.LibraryDescrition);
            progressBar=v.findViewById(R.id.LibraryProgressBar);




            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }

                }
            });

        }

    }



    public AdapterforLibraries(Context context,ArrayList<String> list) {
        this.context = context;
        this.LibraryNAme=list;
        mHelper=new SearchDataBaseHelper(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.l_libraries, parent, false);
        cntnJvCrdSetTheme(view);
        MyViewHolder viewHolder = new MyViewHolder(view,this.listener);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String pName=LibraryNAme.get(position),pDescStr="";
        holder.libraryName.setText(pName);
        if(mHelper.getCount(TABLE_PACKAGE,PACKAGE_NAME,pName)!=0)
            pDescStr=mHelper.getValue(pName,PACKAGE_NAME,PACKAGE_DESCRIPTION,TABLE_PACKAGE);
        holder.libreyDesc.setText(pDescStr);
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                listener.onTouchListener(LibraryNAme.get(position),holder.libreyDesc,holder.progressBar,FLAG);
                return false;
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClickListener(holder.libreyDesc,pName);
                return false;
            }
        });







    }


    @Override
    public int getItemCount() {


        return LibraryNAme.size();
    }
}



