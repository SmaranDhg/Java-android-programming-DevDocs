package redeem.androidjava;

import android.content.Context;
import android.util.Log;

import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.security.acl.Permission;

import static redeem.androidjava.RawDatas.*;

public class SimplePermisionListener implements PermissionListener {
    private Context mContext;

    public boolean isState() {
        return state;
    }

    private boolean state=false;

    public SimplePermisionListener(Context context) {
        this.mContext=context;
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {


        Log.e("onGranted", "onPermissionGranted: "+response.getPermissionName());
        state=true;
        try {
            onPersionGranted(response.getPermissionName(),mContext);
        } catch (InterruptedException e) {
            state=false;
            e.printStackTrace();
        }
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {
        Log.e("Denied", "onPermissionDenied: "+response.getPermissionName());

    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

        Log.e("Rationale", "onPermissionRationaleShouldBeShown: "+permission.getName());
        state=true;

    }
}
