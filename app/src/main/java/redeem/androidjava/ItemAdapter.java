
package redeem.androidjava;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

import static redeem.androidjava.RawDatas.clsMCDCrdSetThemes;

public class ItemAdapter extends ExpandableRecyclerViewAdapter<groupViewHolder,ViewHolderContent> {


    private List<GroupModel> list;
    private OnItemClickTouch mListner;

    public ItemAdapter(List<GroupModel> list) {
        super(list);
        this.list=list;

    }

    @Override
    public groupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_methods,parent,false);
        Log.d("gView", "onCreateGroupViewHolder: "+viewType);
        clsMCDCrdSetThemes(v,1);
        return new groupViewHolder(v,mListner);
    }

    @Override
    public ViewHolderContent onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v =LayoutInflater.from(parent.getContext()).inflate(R.layout.class_title,parent,false);
        clsMCDCrdSetThemes(v,2);
        return new ViewHolderContent(v);
    }


    @Override
    public void onBindChildViewHolder(ViewHolderContent holder, int flatPosition, ExpandableGroup group, int childIndex) {

        final  ItemModel model = (ItemModel) group.getItems().get(childIndex);
        holder.bind(model);

    }




    @Override
    public void onBindGroupViewHolder(groupViewHolder holder, int flatPosition, ExpandableGroup group) {
        final  GroupModel groupModel = (GroupModel) group;
        holder.bind(groupModel);

    }

    public interface OnItemClickTouch
    {
        void onLongClick(String mName,String cName,String mDesc);
    }

    public void setItemTouchListener(OnItemClickTouch listener)
    {
        this.mListner=listener;

    }


    }




    //Interfaces for item click events

