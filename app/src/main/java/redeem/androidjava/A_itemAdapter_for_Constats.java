package redeem.androidjava;

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

public class A_itemAdapter_for_Constats extends ExpandableRecyclerViewAdapter<A_itemAdapter_for_Constats.ACTgroupViewHolder, A_itemAdapter_for_Constats.ACTViewHolderContent> {
    private View gView = null, cView = null;

    public A_itemAdapter_for_Constats(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public ACTgroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View gView = LayoutInflater.from(parent.getContext()).inflate(R.layout.l_methods, parent, false);
        clsMCDCrdSetThemes(gView,1);
        return (new ACTgroupViewHolder(gView));
    }

    @Override
    public ACTViewHolderContent onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View cView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_title, parent, false);
        clsMCDCrdSetThemes(cView,2);//themes
        return (new ACTViewHolderContent(cView));
    }

    @Override
    public void onBindChildViewHolder(ACTViewHolderContent holder, int flatPosition, ExpandableGroup group, int childIndex) {
        A_itemModel_Constants constants = (A_itemModel_Constants) group.getItems().get(childIndex);
        holder.bind(constants);

    }

    @Override
    public void onBindGroupViewHolder(ACTgroupViewHolder holder, int flatPosition, ExpandableGroup group) {
        A_groupModel_Constants groupModel_constants = (A_groupModel_Constants) group;
        holder.bind(groupModel_constants);


    }

    public class ACTgroupViewHolder extends GroupViewHolder {

        private TextView mConstans;

        public ACTgroupViewHolder(final View itemView) {
            super(itemView);
            mConstans = itemView.findViewById(R.id.ALibraryName);


        }

        public void bind(A_groupModel_Constants groupModel) {


            mConstans.setTextColor(itemView.getResources().getColor(getTextContentBrightTheme()));
            mConstans.setText(groupModel.getTitle());

        }

    }

    public class ACTViewHolderContent extends ChildViewHolder {


        private TextView mDescription;

        public ACTViewHolderContent(View itemView) {
            super(itemView);
            mDescription = itemView.findViewById(R.id.ClassTitle_For_recyler);


        }

        public void bind(A_itemModel_Constants itemModel) {
            //for themeing
            mDescription.setTextColor(itemView.getResources().getColor(getTextContentBrightTheme()));
            mDescription.setText(itemModel.getName());

        }


    }


}
