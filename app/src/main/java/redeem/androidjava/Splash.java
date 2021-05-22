package redeem.androidjava;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.karumi.dexter.Dexter;

public class Splash extends AppCompatActivity {

    private ImageView imageView;
    private static  SimplePermisionListener simplePermisionListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        imageView=findViewById(R.id.splashScreen);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.splash_scrn));
        simplePermisionListener=new SimplePermisionListener(this);
        Dexter.withActivity(this).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(simplePermisionListener).check();
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(simplePermisionListener).check();

        new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent mainIntent = new Intent(Splash.this,CppContent.class);
                        Splash.this.startActivity(mainIntent);
                        Splash.this.finish();
                    }
                },simplePermisionListener.isState()?500:5000);





    }
}
