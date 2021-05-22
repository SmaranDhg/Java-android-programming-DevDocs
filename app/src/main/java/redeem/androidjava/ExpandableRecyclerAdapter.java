package redeem.androidjava;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import static redeem.androidjava.A_categorized_packageList.crdSetTheme;
import static redeem.androidjava.MainActivity.threadPool;
import static redeem.androidjava.RawDatas.*;

public class ExpandableRecyclerAdapter extends RecyclerView.Adapter<ExpandableRecyclerAdapter.ViewHolder> {

    private HashMap<String, ArrayList<String>> listHashMap;
    private ArrayList<String> categouxs;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private Context context;
    private Stack<Integer> stack = new Stack<>();
    private Stack<LinearLayout> LayoutStack = new Stack<>();
    private Stack<RelativeLayout> ButtonStack = new Stack<>();

    public int getShowup() {
        return showup;
    }

    public void setShowup(int showup) {
        this.showup = showup;
    }

    private int showup = 1;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    private int flag = 0;


    public TextView getPutSelected() {
        return PutSelected;
    }

    public String getDefalt() {
        return Defalt;
    }

    private final String Defalt;

    private TextView PutSelected;



    public void setPositons(String positons) {
        this.positons = positons;
    }

    public String getPositons() {
        return positons;
    }

    private String positons;


    public HashMap<String, ArrayList<String>> getListHashMap() {
        return listHashMap;
    }

    public ArrayList<String> getCategouxs() {
        return categouxs;
    }

    public ExpandableRecyclerAdapter(HashMap<String, ArrayList<String>> listHashMap, ArrayList<String> categoriz, Context ActContexrt, String packName, TextView PutSelscted, final String defalt) {
        this.context = ActContexrt;
        this.listHashMap = listHashMap;
        this.PutSelected = PutSelscted;
        this.positons = packName;
        this.Defalt = defalt;
        this.categouxs = categoriz;
        //set initial expanded state to false
        for (int i = 0; i < listHashMap.size(); i++) {
            expandState.append(i, false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.a_card_expandble, viewGroup, false);
        crdSetTheme(view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExpandableRecyclerAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.setIsRecyclable(false);
        //textView1
        viewHolder.CategoryNAmae.setText(this.categouxs.get(i));




        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(viewHolder.listView.getContext(), getExpLstPackTheme(), listHashMap.get(categouxs.get(i)));
        viewHolder.listView.setAdapter(arrayAdapter);

        //for adjusting height stuff

        viewHolder.listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, arrayAdapter.getCount() * 105));
        viewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //for adding the methods to the crab
                threadPool.submit(()->{
                    SearchDataBaseHelper dataBaseHelper = new SearchDataBaseHelper(context);
                    dataBaseHelper.insertMethods(getPositons(),getListHashMap().get(getCategouxs().get(i)).get(position),"a",context);

                });
                //for starting the class
                context.startActivity(new Intent(context, A_Classes_Activity.class).putExtra("pName",getPositons()).putExtra("cName",getListHashMap().get(getCategouxs().get(i)).get(position)));
            }
        });


        //check if view is expanded
        final boolean isExpanded = expandState.get(i);
        viewHolder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        viewHolder.buttonLayout.setRotation(expandState.get(i) ? 180f : 0f);

        viewHolder.cliker.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(final View v) {
                //just for for firstclick
                if (getFlag() == 0 ) {
                    //Toast.makeText(v.getContext(),"At zeor",Toast.LENGTH_SHORT).show();
                    getPutSelected().setText(categouxs.get(i));
                    setFlag(1);
                }

                if (!stack.empty() && stack.peek() != i) {
                   // Toast.makeText(v.getContext(),"At diffrent item",Toast.LENGTH_SHORT).show();
                    //they just rote on close step so
                   if(LayoutStack.peek().getVisibility()==View.VISIBLE) createRotateAnimator(ButtonStack.pop(), 180f, 0f).start();
                    LayoutStack.pop().setVisibility(View.GONE);
                    expandState.put(stack.pop(), false);
                    setShowup(1);




                } else if (!stack.empty() && stack.peek() == i) {
                   // Toast.makeText(v.getContext(),"At same item",Toast.LENGTH_SHORT).show();
                    LayoutStack.pop();
                    ButtonStack.pop();
                    stack.pop();
                    //just for repeated clicke of same view
                    if (getShowup() == 1) {
                        setShowup(2);
                        getPutSelected().setText(getDefalt());
                        setFlag(0);
                    } else if (getShowup() == 2) {
                        setShowup(1);
                        setFlag(1);
                    }


                }
                if (getFlag() == 1) {
                   // Toast.makeText(v.getContext(),"At catagori item name",Toast.LENGTH_SHORT).show();
                    getPutSelected().setText(categouxs.get(i));

                }

                stack.push(i);
                ButtonStack.push(viewHolder.buttonLayout);
                LayoutStack.push(viewHolder.expandableLayout);


                onClickButton(viewHolder.expandableLayout, viewHolder.buttonLayout, i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listHashMap.size();
    }


    //connecting the components
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView CategoryNAmae;

        public RelativeLayout buttonLayout;
        public LinearLayout expandableLayout, cliker;
        private ListView listView;

        public ViewHolder(View view) {
            super(view);
            cliker = view.findViewById(R.id.CanClickHere);
            CategoryNAmae = view.findViewById(R.id.textView_name);
            listView = view.findViewById(R.id.ListOfItemsExpandble);
            buttonLayout = view.findViewById(R.id.button);
            expandableLayout = view.findViewById(R.id.expandableLayout);
        }
    }


    private void onClickButton(final LinearLayout expandableLayout, final RelativeLayout buttonLayout, final int i) {


        if (expandableLayout.getVisibility() == View.VISIBLE) {
            createRotateAnimator(buttonLayout, 180f, 0f).start();
            expandableLayout.setVisibility(View.GONE);
            expandState.put(i, false);
        } else {
            createRotateAnimator(buttonLayout, 0f, 180f).start();
            expandableLayout.setVisibility(View.VISIBLE);
            expandState.put(i, true);
        }
    }

    //Code to rotate button
    private ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }
}