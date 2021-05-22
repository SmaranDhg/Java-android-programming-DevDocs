package redeem.androidjava;

import android.view.View;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;


public class ViewHolderContent extends ChildViewHolder {


    private TextView mClasses;
    public ViewHolderContent(View itemView) {
        super(itemView);
        mClasses=itemView.findViewById(R.id.ClassTitle_For_recyler);



    }

public void bind(ItemModel itemModel)
{
    mClasses.setTextColor(itemView.getResources().getColor(RawDatas.getTextContentBrightTheme()));
    mClasses.setText(itemModel.getName());

}


}
