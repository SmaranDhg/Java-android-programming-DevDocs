package redeem.androidjava;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemModel implements Parcelable {


    public ItemModel(String name) {
        this.name = name;
    }

    public   String getName() {
        return name;
    }

    private   String name;


    protected ItemModel(Parcel in) {
        name = in.readString();
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }
}
