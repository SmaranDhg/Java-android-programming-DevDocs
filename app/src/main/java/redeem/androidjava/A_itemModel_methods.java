package redeem.androidjava;

import android.os.Parcel;
import android.os.Parcelable;

public class A_itemModel_methods implements Parcelable {

    public A_itemModel_methods(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    private String name;


    protected A_itemModel_methods(Parcel in) {
        this.name=in.readString();
    }

    public static final Creator<A_itemModel_methods> CREATOR = new Creator<A_itemModel_methods>() {
        @Override
        public A_itemModel_methods createFromParcel(Parcel in) {
            return new A_itemModel_methods(in);
        }

        @Override
        public A_itemModel_methods[] newArray(int size) {
            return new A_itemModel_methods[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }
}
