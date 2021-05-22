package redeem.androidjava;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.widget.LinearLayout.LayoutParams.*;
import static redeem.androidjava.A_categorized_packageList.*;
import static redeem.androidjava.RawDatas.GREEN;
import static redeem.androidjava.RawDatas.getExpLstPackTheme;
import static redeem.androidjava.RawDatas.getTextContentTheme;

public class A_Adapter_For_Catagorized_PakageList extends RecyclerView.Adapter<A_Adapter_For_Catagorized_PakageList.ViewHolder> {

    private HashMap<String, ArrayList<String>> listHashMap;

    public ArrayList<String> getCategouxs() {
        return categouxs;
    }

    public void setCategouxs(ArrayList<String> categouxs) {
        this.categouxs = categouxs;
    }

    private ArrayList<String> categouxs;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private Context context;
    private Stack<Integer> stack=new Stack<>();
    private Stack<LinearLayout> LayoutStack=new Stack<>();
    private onTouchListerner mListerner;



    public void setAFLAG(int AFLAG) {
        this.AFLAG = AFLAG;
    }

    private Stack<RelativeLayout> ButtonStack = new Stack<>();
    private int AFLAG=GREEN;

    public String getDefalt() {
        return Default;
    }
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
    private String Default;

    public TextView getPutSelected() {
        return putSelected;
    }

    private TextView putSelected;



    private String Library;

    public HashMap<String, ArrayList<String>> getListHashMap() {
        return listHashMap;
    }

    public A_Adapter_For_Catagorized_PakageList(HashMap<String, ArrayList<String>> listHashMap, ArrayList<String> categouxs, Context context, TextView putSelected, String defaultTitle) {
        this.listHashMap = listHashMap;
        this.categouxs = categouxs;
        this.context = context;
        this.Library=defaultTitle;
        this.putSelected=putSelected;
        this.Default=defaultTitle;
    }


    @Override
    public A_Adapter_For_Catagorized_PakageList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.a_card_expandble, viewGroup, false);
        crdSetTheme(view);
        return new A_Adapter_For_Catagorized_PakageList.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final A_Adapter_For_Catagorized_PakageList.ViewHolder viewHolder, final int i) {

        viewHolder.setIsRecyclable(false);
        viewHolder.CategoryNAmae.setText(this.categouxs.get(i));
        //For listView

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(viewHolder.listView.getContext(), getExpLstPackTheme(), listHashMap.get(categouxs.get(i)));
        viewHolder.listView.setLayoutParams(new LayoutParams(MATCH_PARENT, arrayAdapter.getCount()* 120));
        viewHolder.listView.setAdapter(arrayAdapter);

        viewHolder.listView.isFastScrollAlwaysVisible();
        viewHolder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override//onClick stuff
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(parent.getContext(), arrayAdapter.getItem(position), Toast.LENGTH_LONG).show();
                Animation animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.bounch);
                animation.setDuration(1000);
                view.startAnimation(animation);
                context.startActivity(new Intent(context, Catagorized_Classes.class).putExtra("positions", getListHashMap().get(getCategouxs().get(i)).get(position)));
            }
        });
        viewHolder.listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mListerner.onTouch(getListHashMap().get(getCategouxs().get(i)).get(position),AFLAG);
                return false;
            }
        });



        //check if view is expanded
        final boolean isExpanded = expandState.get(i);
        viewHolder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        viewHolder.buttonLayout.setRotation(expandState.get(i) ? 180f : 0f);
        viewHolder.clicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //just for for firstclick
                if (getFlag() == 0 ) {
                    getPutSelected().setText(categouxs.get(i));
                    setFlag(1);
                }

                if (!stack.empty() && stack.peek() != i) {
                    //they just rote on close step so
                    if(LayoutStack.peek().getVisibility()==View.VISIBLE) createRotateAnimator(ButtonStack.pop(), 180f, 0f).start();
                    LayoutStack.pop().setVisibility(View.GONE);
                    expandState.put(stack.pop(), false);
                    setShowup(1);


                } else if (!stack.empty() && stack.peek() == i) {
                    LayoutStack.pop();
                    ButtonStack.pop();
                    stack.pop();
                    //just for repeated click of same view
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
        public LinearLayout expandableLayout, clicker;
        private ListView listView;


        public ViewHolder(View view) {
            super(view);
            clicker = view.findViewById(R.id.CanClickHere);
            CategoryNAmae = view.findViewById(R.id.textView_name);
            listView = view.findViewById(R.id.ListOfItemsExpandble);
            buttonLayout = view.findViewById(R.id.button);
            expandableLayout = view.findViewById(R.id.expandableLayout);
        }
    }


    private void onClickButton(final LinearLayout expandableLayout, final RelativeLayout buttonLayout, final int i) {

        //Simply set View to Gone if not expanded
        //Not necessary but I put simple rotation on button layout
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

    interface onTouchListerner
    {
        void onTouch(String pName,int AFlag);
    }
    public void setOnTouchListener(onTouchListerner mlisterner)
    {
        this.mListerner=mlisterner;
    }


}