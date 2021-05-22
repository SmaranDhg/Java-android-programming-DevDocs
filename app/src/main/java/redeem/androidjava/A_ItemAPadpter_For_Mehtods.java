package redeem.androidjava;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import static redeem.androidjava.RawDatas.clsMCDCrdSetThemes;
import static redeem.androidjava.RawDatas.getTextContentBrightTheme;

public class A_ItemAPadpter_For_Mehtods extends ExpandableRecyclerViewAdapter<A_ItemAPadpter_For_Mehtods.AMgroupViewHolder, A_ItemAPadpter_For_Mehtods.AMViewHolderContent> {

    private View gView = null, cView = null;
    private OnLongClickListener mListener;

    public A_ItemAPadpter_For_Mehtods(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public A_ItemAPadpter_For_Mehtods.AMgroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View gView = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_methods, parent, false);
        clsMCDCrdSetThemes(gView,1);
        return new AMgroupViewHolder(gView,this.mListener);
    }

    @Override
    public A_ItemAPadpter_For_Mehtods.AMViewHolderContent onCreateChildViewHolder(ViewGroup parent, int viewType) {

        View cView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_title, parent, false);
        //apply themes
        clsMCDCrdSetThemes(cView,2);
        return new AMViewHolderContent(cView);
    }

    @Override
    public void onBindChildViewHolder(AMViewHolderContent holder, int flatPosition, ExpandableGroup group, int childIndex) {
        A_itemModel_methods itemModel_methods = (A_itemModel_methods) group.getItems().get(childIndex);
        holder.bind(itemModel_methods);

    }

    @Override
    public void onBindGroupViewHolder(AMgroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        A_roupModel_methods roupModel_methods = (A_roupModel_methods) group;
        holder.bind(roupModel_methods);


    }

    interface OnLongClickListener
    {
        void onLongClickListner(String cName,String mName,String mDesc);
    }
    public void setOnLongClickListener(OnLongClickListener mListerner)
    {
       this.mListener=mListerner;
    }



    public class AMgroupViewHolder extends GroupViewHolder {

        private TextView mMethods;
        private OnLongClickListener mListener;
        private View mView;

        public AMgroupViewHolder(final View itemView,OnLongClickListener mListener) {
            super(itemView);
            mMethods = itemView.findViewById(R.id.ALibraryName);
            this.mListener=mListener;
            this.mView=itemView;


        }

        public void bind(A_roupModel_methods groupModel) {
            mMethods.setTextColor(itemView.getResources().getColor(getTextContentBrightTheme()));
            mMethods.setText(groupModel.getTitle());
            mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onLongClickListner(groupModel.getpName(),groupModel.getTitle(),groupModel.getItems().get(0).getName());
                    return false;
                }
            });

        }

    }

    public class AMViewHolderContent extends ChildViewHolder {


        private TextView mDescrition;

        public AMViewHolderContent(View itemView) {
            super(itemView);
            mDescrition = itemView.findViewById(R.id.ClassTitle_For_recyler);


        }

        public void bind(A_itemModel_methods itemModel) {
            mDescrition.setTextColor(itemView.getResources().getColor(getTextContentBrightTheme()));
            mDescrition.setText(itemModel.getName());

        }


    }


}
