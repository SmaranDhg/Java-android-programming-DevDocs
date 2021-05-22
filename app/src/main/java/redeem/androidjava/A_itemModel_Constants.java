package redeem.androidjava;

import android.os.Parcel;
import android.os.Parcelable;

public class A_itemModel_Constants implements Parcelable {
    public A_itemModel_Constants(String name) {
        this.name = name;
    }

    private String name;
    protected A_itemModel_Constants(Parcel in) {
        name=in.readString();
    }

    public static final Creator<A_itemModel_Constants> CREATOR = new Creator<A_itemModel_Constants>() {
        @Override
        public A_itemModel_Constants createFromParcel(Parcel in) {
            return new A_itemModel_Constants(in);
        }

        @Override
        public A_itemModel_Constants[] newArray(int size) {
            return new A_itemModel_Constants[size];
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

    public String getName() {
        return name;
    }
}
