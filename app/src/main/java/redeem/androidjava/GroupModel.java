package redeem.androidjava;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.Collections;

public class GroupModel extends ExpandableGroup<ItemModel> {

    public String getcName() {
        return cName;
    }

    private String cName;

    public GroupModel(String title, ItemModel model,String cName) {
        super(title, Collections.singletonList(model));
        this.cName=cName;
    }
}
