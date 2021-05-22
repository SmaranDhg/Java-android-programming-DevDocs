package redeem.androidjava;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static redeem.androidjava.RawDatas.light_color;
import static redeem.androidjava.SearchActivity.srchSetTheme;
import static redeem.androidjava.SearchDataBaseHelper.*;

public class AdapterForResultSet  extends RecyclerView.Adapter<AdapterForResultSet.Viewholder> {


    private ArrayList<String> resultSet;
   private OnItemClickListener listener;
   private Context context;
   private ArrayList<Character[]> cat;


    public interface  OnItemClickListener
    {

        void  onItemClick(String pName,Character plat);

        void onItemClick(String pName, String cName, Character plat);

        void onItemClick(String mName, String MDesc, Character platform, String m);
    }

    public void setOnItemClickListener(AdapterForResultSet.OnItemClickListener listener)
    {
        this.listener= (OnItemClickListener) listener;
    }



    public synchronized void filter(ArrayList<String> filterItem,ArrayList<Character[]> cat) {
        Log.d("selected_count", String.valueOf(filterItem.size()));
        this.resultSet=  filterItem;
        this.cat=cat;
        notifyDataSetChanged();

    }


    public AdapterForResultSet(ArrayList<String> resultSet,ArrayList<Character[]> cat,Context context) {
        this.resultSet=resultSet;
        this.cat=cat;
        this.context=context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       final View view= LayoutInflater.from(context).inflate(R.layout.cards_for_search,viewGroup,false);
       srchSetTheme(view);
       return new Viewholder(view,this.listener);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
        viewholder.results.setText(resultSet.get(i));
        viewholder.resultCat.setText(cat.get(i)[1]+"");

        Character l;
        //incase methods and class
         if(viewholder.resultCat.getText().equals("c")||viewholder.resultCat.getText().equals("m")) {
             if(viewholder.resultCat.getText().equals("c"))
             l = getValue(resultSet.get(i), CLASS_NAME, CAT, TABLE_CLASSES, context).charAt(0);
             else
                 l=getValue(resultSet.get(i),METHOD_NAME,CAT,TABLE_METHODS,context).charAt(0);
         }
         else l =cat.get(i)[0];


        if(l.equals('a'))
        {

           viewholder.platformIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.android));

        }
        else {

            viewholder.platformIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.java));
        }






    }

    @Override
    public int getItemCount() {
        return resultSet.size();
    }

    public class  Viewholder extends RecyclerView.ViewHolder
    {
        TextView results,resultCat;
        ImageView platformIcon;


        public Viewholder(View view, final OnItemClickListener listener) {
            super(view);
            resultCat=view.findViewById(R.id.search_result_category);
            results=view.findViewById(R.id.search_result);
            platformIcon=view.findViewById(R.id.search_result_icon);


                view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    if (cat.get(getAdapterPosition())[1].equals('m')) {
                        String value = resultSet.get(getAdapterPosition());
                        Character platform = getValue(String.valueOf(results.getText()), METHOD_NAME, CAT, TABLE_METHODS, context).charAt(0);

                        listener.onItemClick(value, getValue(value, METHOD_NAME, METHOD_DESCRIPTION, TABLE_METHODS, context), platform, "on");


                    }
                    return false;
                }
            });

                view.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if(cat.get(getAdapterPosition())[1].equals('m')) {
                            String value = resultSet.get(getAdapterPosition());
                            Character platform = getValue(String.valueOf(results.getText()), METHOD_NAME, CAT, TABLE_METHODS, context).charAt(0);

                            switch (event.getAction()) {

//                                case MotionEvent.ACTION_DOWN:
//                                    listener.onItemClick(value, getValue(value, METHOD_NAME, METHOD_DESCRIPTION, TABLE_METHODS, context), platform, "on");
//                                    break;
                                case MotionEvent.ACTION_UP:
                                    listener.onItemClick(value, getValue(value, METHOD_NAME, METHOD_DESCRIPTION, TABLE_METHODS, context), platform, "off");
                                    break;


                            }
                        }

                            return false;

                    }
                });


                view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                    {

                        Character platform;
                        String value=resultSet.get(getAdapterPosition());
                        Character category=cat.get(getAdapterPosition())[1];
                        if(category.equals('c'))
                            platform= getValue(String.valueOf(results.getText()), CLASS_NAME, PACKAGE_NAME, TABLE_CLASSES,context).charAt(0);
                         else
                              platform=cat.get(getAdapterPosition())[0];

                        if(category.equals('p'))
                            listener.onItemClick(value,platform);
                        else if(category.equals('c')) {
                            String pName=getValue(value, CLASS_NAME, PACKAGE_NAME, TABLE_CLASSES, context);
                            Log.d("pNames", "onClick: "+pName);
                            listener.onItemClick(pName, value, platform);
                        }
                      //  else listener.onItemClick(value,getValue(value,METHOD_NAME,METHOD_DESCRIPTION,TABLE_METHODS,context),platform,"m");


                    }

                }
            });

        }
    }

}
