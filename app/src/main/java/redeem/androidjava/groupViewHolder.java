package redeem.androidjava;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import static redeem.androidjava.RawDatas.*;

public class groupViewHolder extends GroupViewHolder {

    private TextView mLibraries, c1, c2, c3;
    private GroupModel gModel;
    private ItemAdapter.OnItemClickTouch mListener;
    private View mView;

    public groupViewHolder(final View itemView, ItemAdapter.OnItemClickTouch listener) {
        super(itemView);
        mLibraries = itemView.findViewById(R.id.ALibraryName);
        this.mView=itemView;
        this.mListener=listener;




    }

    public void bind(GroupModel groupModel) {
        this.gModel=groupModel;
        mLibraries.setTextColor(mLibraries.getResources().getColor(getTextContentBrightTheme()));
        mLibraries.setText(groupModel.getTitle());

        mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onLongClick(groupModel.getTitle(),groupModel.getcName(),groupModel.getItems().get(0).getName());
                return false;
            }
        });

    }

}
