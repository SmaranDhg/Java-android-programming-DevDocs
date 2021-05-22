package redeem.androidjava;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.PermissionToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static redeem.androidjava.MainActivity.*;
import static redeem.androidjava.MainActivity.threadPool;
import static redeem.androidjava.R.id.homeIcon;
import static redeem.androidjava.R.id.scrollIndicatorDown;

public class RawDatas {

    private Context context;
    public static  final int dark_color= Color.parseColor("#ffffff");
    public static  final int light_color= Color.parseColor("#000000");
    private static  float BITMAP_SCALE = 0.25f;
    private static  float BLUR_RADIUS = 15f;
    public static final String THEME_LIGHT="LIGHT";
    public static final String THEME_DARK="DARK";
    private static  String THEME=THEME_DARK;
    private static int nd_toggle=1;
    public static final int CARD_FLAG_CONTENT_JAVA=2;
    public static final int CARD_FLAG_CLASS_ANDROID=2;
    public static final int CARD_FLAG_CLASS_JAVA=2;
    public static final int CARD_FLAG_MAIN=2;
    public static final int CARD_FLAG_CONTENT_ANDROID=2;
    public static final int CARD_FLAG_CATPACKCLASS_ANDROID=2;
    public static final int CARD_FLAG_SMALL=3;
    public static final int CARD_FLAG_ONLONGCLICKS=1;
    public static final int CARD_FLAG_HEADER_PARAMETER=2121;
    public static final int GREEN=1;
    public static final int RED=0;
    public static final String VT_TABLE="VTTable";
    public static final String VT_CODE="VTCODE";
    public static final String VT_LIST="VTLISt";
    public static final String VT_NORMAL="VTNORMAL";
    public static final String HEADER_LINK_SEP=":;HLS:;;";
    public  static final   String[] Alibraries={"Android Platform"};
    public  static  final String[] AlibDesc={"It has all the data and platform issues we care about."};


    public static final String libraries[] = {"java.security.cert","java.util.zip","javax.xml.transform.dom","java.nio.file","java.time","javax.security.auth.callback","javax.xml.validation","javax.microedition.khronos.egl","java.util.logging","javax.xml","javax.security.auth.x500","java.util.stream","java.time.chrono","javax.security.auth","java.math","java.text","javax.xml.transform.stream","javax.xml.datatype","java.beans","javax.sql","java.util.prefs","java.net","java.time.format","java.util.concurrent","javax.xml.parsers","javax.net","javax.xml.xpath","java.lang.invoke","javax.security.cert","java.util.concurrent.atomic","java.nio.channels.spi","javax.net.ssl","java.nio.file.attribute","java.nio","java.lang.ref","java.util.regex","java.lang.reflect","java.security","java.lang","java.sql","javax.crypto","java.util","java.nio.channels","java.time.temporal","java.nio.charset","javax.xml.transform","java.nio.charset.spi","java.security.spec","java.nio.file.spi","javax.xml.namespace","java.awt.font","javax.xml.transform.sax","java.util.concurrent.locks","java.time.zone","javax.crypto.spec","java.io","java.util.jar"};
    public static final   String  APacakge[] = {"android.service.autofill","android.icu.math","android.media","android.appwidget","android.telephony.euicc","android.util","android.telecom","android.icu.text","android.app.slice","android.view.inspector","android.sax","android.telephony","android.telephony.gsm","android.net.http","android.os.storage","android.net.ssl","android.test.suitebuilder","android.security.keystore","android.net","android.view.autofill","android.hardware.camera2.params","android.system","android.nfc","android.mtp","android.media.browse","android.content.pm","android.drm","android.service.media","android.accounts","android.widget","android.renderscript","android.service.restrictions","android.printservice","android.test","android.graphics.drawable","android.net.wifi.hotspot2.pps","android.media.midi","android.opengl","android.view.textclassifier","android.graphics.pdf","android","android.companion","android.os","android.view.animation","android.hardware.fingerprint",
            "android.media.projection","android.view.textservice","android.nfc.cardemulation","android.net.wifi.hotspot2","android.preference","android.accessibilityservice","android.media.effect",
            "android.view","android.print.pdf","android.service.vr","android.net.wifi.hotspot2.omadm","android.text.style","android.inputmethodservice",
            "android.os.strictmode","android.hardware.input","android.hardware.camera2","android.test.mock","android.content","android.service.dreams",
            "android.service.carrier","android.database","android.net.nsd","android.nfc.tech","android.service.textservice","android.icu.lang","android.app.job",
            "android.net.wifi.p2p.nsd","android.speech","android.graphics.drawable.shapes","android.service.notification","android.security","android.icu.util",
            "android.graphics.fonts","android.text.util","android.view.contentcapture","android.telephony.emergency","android.view.accessibility","android.service.quicksettings",
            "android.media.session","android.net.wifi.aware","android.net.wifi.p2p","android.se.omapi","android.text.method","android.app.admin","android.hardware.display",
            "android.gesture","android.graphics","android.hardware.usb","android.speech.tts","android.text","android.net.sip","android.media.audiofx","android.app.role",
            "android.telephony.mbms","android.transition","android.bluetooth.le","android.database.sqlite","android.service.chooser","android.app.assist","android.service.voice",
            "android.animation","android.service.wallpaper","android.graphics.text","android.view.inputmethod","android.print","android.telephony.data","android.net.wifi","android.media.tv",
            "android.hardware.biometrics","android.app.backup","android.content.res","android.hardware","android.net.wifi.rtt","android.app.usage","android.text.format","android.net.rtp",
            "android.provider","android.app","android.telephony.cdma","android.os.health","android.bluetooth","android.location","android.webkit"};
    public static  final  String CLASS="Class";

    public static  final  String USEDFOR="ExIm";
    public static  final  String EXIM="UsedFor";
    public static  final  String CONSTRUCTOR="Constructor";
    public static  final  String METHODS="Methods";
    public static  final  String METHODS_DESCRIPTION="MethodDesc";
    public static  final  String CONSTANTS="Constant";
    public static  final  String CONSTANTS_DESCRIPTION="ConstantDesc";
    public static final String AL_TO_STR_SEP=";:ATSS;:";

    public static final int REQUEST = 112;

    public static final String[] Cat_packages={"ANDROID","ACCESSIBILITY.SERVICE","ACCOUNTS","ANIMATION","APP","BLUETOOTH","COMPANION","CONTENT","DATABASE","DRM","GESTURE","GRAPHICS","HARDWARE","ICU","INPUTMETHODSERVICE","LOCATION","MEDIA","MTP","NET","NFC","OPENGL","OS","RENDERSCRIPT","SAX","SE","SECURITY","SERVICE","SPEECH","SYSTEM","TELECOM","TELEPHONY","TEST","TEXT","TRANSITION","UTIL","VIEW","WEBKIT"};



    public RawDatas(Context context) {
        this.context = context;


    }






//for making list from comma separated string of methods
    public static ArrayList<String> makeAlist(String data)
    {
        String lt = "";
        ArrayList<String> datas = new ArrayList<>();
        int i=0;
        for (char l : data.toCharArray()) {
            lt = lt + l;
            if (l == ',' || i==data.length()-1) {
                //for remaining , sign
                datas.add(lt.replace(",", ""));
                lt = "";

            }
            if(datas.size()>50) break;
            i++;

        }

        return datas;
    }

    //incase no need for break point
    public static ArrayList<String> makeAlist(String data,int flag)
    {
        String lt = "";
        ArrayList<String> datas = new ArrayList<>();
        int i=0;
        for (char l : data.toCharArray()) {
            lt = lt + l;
            if (l == ',' || i==data.length()-1) {
                //for remaining , sign
                datas.add(lt.replace(",", ""));
                lt = "";

            }
            if(datas.size()>50) break;
            i++;

        }

        return datas;
    }




   public static ArrayList<String> getCategory(String data)
   {

       ArrayList<String> catPackage=new ArrayList<>();

     for(String pack : APacakge)
     {

         if(pack.contains(data.toLowerCase()))
         {
             Log.d("get_cat", "Pck: "+pack);
             catPackage.add(pack);
         }

     }

     return catPackage;
   }

   public static  void onPersionGranted(String permissionName,Context context) throws InterruptedException {
   boolean state=false;
    switch (permissionName)
    {
        case (READ_EXTERNAL_STORAGE):
            state=true;
            break;
        case (WRITE_EXTERNAL_STORAGE):
            state= true;//in case we need confirmation that the asset  is copied
            copyAssets(context);

            break;

    }
    Log.d("permission", "onPersionGranted: "+state);

}


//incase use permanently denied the permission  we just lead them to settings
public static  void openSettings(Context context)
{
    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    Uri  uri = Uri.fromParts("package",context.getPackageName(),null);
    intent.setData(uri);
    context.startActivity(intent);


}

//all the resnderscipt going on here
    public static Bitmap blur(View v) {
        return blur(v.getContext(), getScreenshot(v),5f,0.25f);
    }

    public static Bitmap blur(View v,float blurRaidus) {

        return blur(v.getContext(), getScreenshot(v),blurRaidus,0.25f);


    }
    public static Bitmap blur(View v,float blurRaidus,float scale) {

        return blur(v.getContext(), getScreenshot(v),blurRaidus,scale);


    }

    public static Bitmap blur(Context context, Bitmap image,float radius , float scale) {
        int width = Math.round(image.getWidth() * scale);
        int height = Math.round(image.getHeight() * scale);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, true);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            RenderScript rs = RenderScript.create(context);
            final ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            final Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
            final Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);


            theIntrinsic.setInput(tmpIn);
            theIntrinsic.setRadius(radius);
            theIntrinsic.forEach(tmpOut);
            tmpOut.copyTo(outputBitmap);
        }




        return outputBitmap;
    }


    private static Bitmap getScreenshot(View v) {

         Bitmap b =Bitmap.createBitmap(v.getWidth(),v.getHeight(), Bitmap.Config.ARGB_8888) ;
         Canvas c = new Canvas(b);
          v.draw(c);
            Log.d("Bitmap", "getScreenshot: "+b);
          return b;
    }

    public static String AL_TO_STRING(ArrayList<String> data)
    {
        String dat=data.get(0);
        for(int i=1;i<data.size();i++)
        {
            dat=dat+AL_TO_STR_SEP+data.get(i);
        }
        return dat;

    }
    public static ArrayList<String> STRING_TO_ALS(String data)
    {
        ArrayList<String> datas=new ArrayList<>();
        for(String a:data.split(AL_TO_STR_SEP))
        {
            datas.add(a);
        }
        return datas;
    }
    public static int getViewTheme()
    {
        setTheme();
        int a;
        switch (THEME)
        {
            case THEME_LIGHT:
                a= R.color.colorTurquise;
                break;
            case THEME_DARK:
                a=R.color.colorGray;
                break;
            default:
                a=R.color.colorTurquise;
        }


        return a;
    }
    public static int getCardViewTheme(int flag)
     {
      setTheme();
        int a;
        switch (flag)
        {
            case 1:
                switch (THEME)
                {
                    case THEME_LIGHT:
                        a= R.color.CardColorDark;
                        break;
                    case THEME_DARK:
                        a=R.color.CardColorLight;
                        break;
                    default:
                        a=R.color.CardColorLight;
                }
                break;
            case 3:
                switch (THEME)
                {
                    case THEME_LIGHT:
                        a= R.color.CardColorLightSmall;
                        break;
                    case THEME_DARK:
                        a=R.color.CardColorDarkSmall;
                        break;
                    default:
                        a=R.color.CardColorDarkSmall;
                }
                break;
            case CARD_FLAG_HEADER_PARAMETER:
               //for the just the alpha
                a=R.color.CardHFuncParameter;
                break;


                default:
                 switch (THEME)
                 {
                case THEME_LIGHT:
                    a= R.color.CardColorLight;
                    break;
                case THEME_DARK:
                    a=R.color.CardColorDark;
                    break;
                default:
                    a=R.color.CardColorDark;
                 }
                break;
        }



        return a;
    }

    public static int getIndicatorTheme()
    {
        setTheme();
        int a;
        switch (THEME)
        {
            case THEME_DARK:
                a=R.drawable.triangle_light;
                break;
            default:
                a=R.drawable.triangle;
                break;
        }
         return a;
    }

    public static int getIconTheme()
    {
        setTheme();
        int a;
        switch (THEME)
        {
            case THEME_DARK:
                a=dark_color;
                break;
            default:
                a=light_color;
                break;
        }
        return a;
    }

    public static int getTextContentTheme()
    {
        setTheme();
        int a;
        switch (THEME)
        {
            case THEME_DARK:
                a=R.color.TextColorContentLight;
                break;
            default:
                a=R.color.TextColorContentDark;
                break;
        }
        return a;
    }
    public static int getTextContentBrightTheme()
    {
        setTheme();
        int a;
        switch (THEME)
        {
            case THEME_DARK:
                a=R.color.TextColorLight;
                break;
            default:
                a=R.color.TextColorDark;
                break;
        }
        return a;
    }

    public static int getExpLstPackTheme()
    {
        setTheme();
        int a;
        switch (THEME)
        {
            case THEME_LIGHT:
                a= R.layout.a_l_packages;
                break;
            case THEME_DARK:
                a=R.layout.a_l_package_dark;
                break;
            default:
                a= R.layout.a_l_packages;
                break;
        }
        return a;
    }

    public static int getTextTitleTheme()
    {
        setTheme();
        return getTextContentTheme();
    }

    public static void clsMCDCrdSetThemes(View view,int flag)
    {
        setTheme();
        //cardView
        CardView cTtl,lMethod;
        TextView data;
        if(flag==1)
        {
            cTtl=view.findViewById(R.id.clsMCDCrd1);
            cTtl.setCardBackgroundColor(view.getResources().getColor(getCardViewTheme(CARD_FLAG_SMALL)));

        }
       else {
            lMethod=view.findViewById(R.id.clsMCDCrd2);
            lMethod.setCardBackgroundColor(view.getResources().getColor(getCardViewTheme(CARD_FLAG_SMALL)));
        }


    }

    private static void setTheme()
    {
        Calendar  rightNow =Calendar.getInstance();
        int CurrentHourIn24Format=rightNow.get(Calendar.HOUR_OF_DAY);
        if(nd_toggle==0) THEME=THEME_DARK;
        if(CurrentHourIn24Format>6 && CurrentHourIn24Format<18&& getNd_toggle()!=0|| getNd_toggle()==1 )
        {
            THEME=THEME_LIGHT;
        }
        else {
            THEME=THEME_DARK;

        }


    }

    public static void setNd_toggle(int nd_toggle) {
        RawDatas.nd_toggle = nd_toggle;
        setTheme();

    }
    public static int getNd_toggle() {
        return nd_toggle;
    }
    public static String getTHEME() {
        return THEME;
    }












}


