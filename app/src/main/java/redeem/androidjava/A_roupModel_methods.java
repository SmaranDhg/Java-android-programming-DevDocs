package redeem.androidjava;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class A_roupModel_methods extends ExpandableGroup<A_itemModel_methods> {

    private String pName;

    public String getpName() {
        return pName;
    }

    public A_roupModel_methods(String title, List<A_itemModel_methods> items,String pName) {
        super(title, items);
        this.pName=pName;
    }
}
