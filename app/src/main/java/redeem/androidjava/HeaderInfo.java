package redeem.androidjava;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import java.util.Iterator;
import java.util.Stack;

import static android.content.ContentValues.TAG;
import static redeem.androidjava.Cpp.*;
import static redeem.androidjava.SearchDataBaseHelper.*;
import static redeem.androidjava.fu.get_js;
import static redeem.androidjava.fu.gtSecHdin;


class HeaderInfo {

    public String hNmae,hDesc="";
    private JSONObject jsonHeader=new JSONObject();
    private Context mContext;
    private SearchDataBaseHelper dbHelper;

    public Stack<String> dTtl=new Stack<>();
    public Stack<Desc> descs=new Stack<>();
    public  Stack<Function> functions=new Stack<>();
    public Stack<String[]> clsAndDesc=new Stack<>();
    public ArrayList<String> clsLinks=new ArrayList<>();
    public boolean hasFunc=false,hasClass=false,hasMacrosConstant=false,hasTypes=false,hasCompatibitlity=false;
    public  int itemcount=0;//for the recylerview


    public HeaderInfo(Context context,String hName) throws IOException, JSONException {
        this.mContext=context;
        dbHelper=new SearchDataBaseHelper(context);
        exploreInternet(hName);


    }
   //for loaded call
    public  HeaderInfo(JSONObject jsonObject) throws JSONException {
        exploreJson(jsonObject);
    }

    private void exploreInternet(String hNname) throws IOException, JSONException {
        String hLink=dbHelper.getValue(hNname, KEY, VALUE, TABLE_ARCHIVE);
        try{
            Element hbody=gtBody(get_js(hLink));
            //putting inside the json format
            jsonHeader.put("hName",hNname);
            jsonHeader.put("hDesc",gtHDesc(hbody));
            itemcount++;
            //classes
            if(hasClasses(hbody))
            {
                /*
                * note JSON keeps the AL as the array []
                * and String[] or array as array too[]*/
                /*
                * but update
                *  is the alist on unfolding is alist so try
                * to store data in pure jason format
                * other wise runtime error is there for you*/
                jsonHeader.put("hasClass",Boolean.valueOf(true));
                jsonHeader.put("clsLinks",new JSONArray(gtBoxClassLink(hbody)));//for avoiding runtime shit
                jsonHeader.put("clsAndDesc",gtHClasses(hbody));//here it restuns the jason arra with element of json array it self [[],[],[]]
                itemcount++;


            }else jsonHeader.put("hasClass",Boolean.valueOf(false));
            //function
            if(hasFunction(hbody))
            {
                jsonHeader.put("hasFunc",Boolean.valueOf(true));
                JSONArray jsonArray=new JSONArray();
                Function function;
                Element fSec=gtSecCat(hbody,ID_FUNCTIONS);
                if(fSec.equals(null)) fSec=hbody.selectFirst(":has(h3:contains(function))");
                for(Element m:gtSecItems(fSec)) {
                    try {
                        String url=fu.mkUrl(fu.cpp, m.select("a[href]").attr("href"));
                        function=new Function(url,jsonArray);

                     } catch (Exception ignored)
                    {
                    }
                }
                itemcount++;
                jsonHeader.put("functions",jsonArray);

            }else jsonHeader.put("hasFunc", Boolean.FALSE);

            //other items
           JSONObject other=new JSONObject();
            for(Element e:hbody.select("section:not(."+ID_CLASS+",."+ID_FUNCTIONS+",."+ID_DESCRIPTION+",:has(h3:contains(class)))"))
            {
                try {
                    String hdin = e.selectFirst(">h3").text() == null ? "hdin" : e.selectFirst(">h3").text();
                    Desc desc = new Desc(e);
                    other.put(hdin, new JSONObject(desc.jsonDes));
                    Log.e(TAG, "exploreInternet: "+desc.jsonDes );

                    itemcount++;
                }catch (Exception err){
                }
            }
            jsonHeader.put("other",other);


           //now all other in json


           exploreJson(jsonHeader);

        }catch (Exception e){}

       //here loaded part is archived and that's it
        jsonHeader.put("itemCount",Integer.valueOf(itemcount));
       if(itemcount!=0)
        dbHelper.insertHeader(hNname,jsonHeader.toString());




    }
    private void exploreJson(JSONObject jsonObject) throws JSONException {
        hNmae=jsonObject.getString("hName");
        hDesc=jsonObject.getString("hDesc");
        itemcount=jsonObject.getInt("itemCount");
        hasFunc=jsonObject.getBoolean("hasFunc");
        if(hasFunc)
        {
            //here we store the function in json array so
            JSONArray funarr=jsonObject.getJSONArray("functions");
            JSONObject func;
            for(int i=0;i<funarr.length();i++)
            {
                try {//you Know the shit happens
                    func = funarr.getJSONObject(i);//and each object got the jsonobject
                    functions.add(new Function(func));
                }catch (Exception e){continue;}
            }

        }
        hasClass=jsonObject.getBoolean("hasClass");
        if(hasClass)
        {
            JSONArray cLinks=jsonObject.getJSONArray("clsLinks");
            for(int i=0;i<cLinks.length();i++) clsLinks.add(cLinks.getString(i));
            JSONArray cClas=jsonObject.getJSONArray("clsAndDesc");
            JSONArray cls;
            for(int i=0;i<cClas.length();i++)
            {
                cls=cClas.getJSONArray(i);
                clsAndDesc.add(new String[]{cls.getString(0),cls.getString(0)});

            }


        }
        //others
        try {
            JSONObject other = jsonObject.getJSONObject("other");
            JSONArray names = other.names();

            String name;
            for (int i = 0; i < names.length(); i++) {
                try {
                    name = names.getString(i);
                    dTtl.add(name);//titles
                    descs.add(new Desc(other.getJSONObject(name)));

                }catch (Exception e){
                    Log.e(TAG, "exploreJson: err " +e);
                }
            }
        }catch (Exception e)
        {

        }



    }






}

class HClass
{
    public String cNmae;
    public String cDesc;
    public ArrayList<String> dTtl=new ArrayList<>();
    public ArrayList<Desc> descs=new ArrayList<>();
    public  ArrayList<Function> functions=new ArrayList<>();
    public boolean hasFunc=false;//incase we have to chose the cards
    private int itemCount=0;




}


class Function
{
    public String fName;
    public String fDesc;
    public String Example;
    public String Return;
    public  boolean hasCode;
    public  boolean hasParameter;
    public boolean hasReturns;

    private JSONObject jsonFunct=new JSONObject();

    public Stack<String[]> parAndDesc=new Stack<>();

//in case for first time it give the link
    public Function(String fLink,JSONArray putinside) throws IOException, JSONException {
        ExploreInternet(fLink,putinside);

    }
    //other wise it recieves the jsonobject
    public Function(JSONObject jsonObject) throws JSONException {
        this.jsonFunct=jsonObject;
        exploreJsonFucnt(jsonObject);
    }

    private void ExploreInternet(String fLink,JSONArray putinside) throws IOException, JSONException {
        Element fBody=gtBody(get_js(fLink));
        String name=!fBody.select("."+CLASS_FUN_NAME).isEmpty()?fBody.select("."+CLASS_FUN_NAME).text():fu.gtSecHdin(fBody);
        name=name.isEmpty()?new URL(fLink).getFile():name;//because it gave the err so
        jsonFunct.put("fName",name);
        jsonFunct.put("fDesc",fBody.select("#"+ID_DESCRIPTION_TITLE).text()+"\n"+fBody.select("#"+ID_DESCRIPTION).text());

        //now for the for all the other shit
        if(hasReturn(fBody))
       {
         jsonFunct.put("hasReturn", Boolean.TRUE);
         jsonFunct.put("return",gtFReturn(fBody));
       }
        else jsonFunct.put("hasReturn", Boolean.FALSE);

       if(hasExample(fBody))
       {
           jsonFunct.put("hasExample",Boolean.valueOf(true));
           jsonFunct.put("example", gtFExample(fBody));
       } else jsonFunct.put("hasExample", Boolean.FALSE);

       if(hasParameter(fBody))
       {
           jsonFunct.put("hasParameter",Boolean.valueOf(true));
           jsonFunct.put("Parameters",gtFParameter(fBody));
       } else jsonFunct.put("hasParameter", Boolean.FALSE);
       //now putting the object into jasonarray from main class
        putinside.put(jsonFunct);
        exploreJsonFucnt(jsonFunct);

    }
    private void exploreJsonFucnt(JSONObject jsonObject) throws JSONException {
      fName=jsonObject.getString("fName").replaceAll("(\\s)+"," ").replaceAll("\\);(\\s)*?","\\);\n\b");//just for  overloaded function to be in new line
        fDesc=jsonObject.getString("fDesc");
      hasReturns=jsonObject.getBoolean("hasReturn");
      if(hasReturns) Return=jsonObject.getString("return");
      hasCode=jsonObject.getBoolean("hasExample");
      if(hasCode) Example=jsonObject.getString("example");
      hasParameter=jsonObject.getBoolean("hasParameter");
      if(hasParameter) {
          JSONObject parObj=jsonObject.getJSONObject("Parameters");//here the json obj from gtParameter function
          Iterator<String> jsonArray=parObj.keys();//now getting the names
          if(jsonArray.hasNext())
          {
              String nam=jsonArray.next();//here name is a object so we have to change it string
              parAndDesc.add(new String[]{nam,parObj.getString(nam)});
          }

      }


    }






}

class Desc
{

    public   String code,n_desc;
    private JSONObject jsonDesc=new JSONObject();
    public  String jsonDes;
    public   Stack<String[]> list;
    public  TableInfo tableInfo;
    public Desc(Element sec) throws JSONException {
        ExploreInternet(sec);
    }
    public Desc(JSONObject jsonObject) throws JSONException {

        exploreJson(jsonObject);
    }


    public boolean hasList=false,hasTable=false,hasCode=false;
    private void ExploreInternet(Element sec) throws JSONException {
        String Desc="";
        Desc=sec.select(">*:not(h1,h2,h3,h4,dl,b,:has(tbody,pre))").text();
        jsonDesc.put("n_desc",Desc.length()<2?"":Desc);//to check the presence of b tag in the description
        jsonDesc.put("hasCode",hasCode);
        jsonDesc.put("hasTable",hasTable);
        jsonDesc.put("hasList",hasList);
        if(hasCode(sec)) {
            jsonDesc.put("hasCode", Boolean.TRUE);
            jsonDesc.put("code",sec.select("." + CLASS_CODE_SOURCE + ">pre").text());

        }
        else if(hasTable(sec)) {
            jsonDesc.put("hasTable", Boolean.TRUE);
            jsonDesc.put("tableBoxed",gtSecTbl(sec));

        }
        else if(hasList(sec)) {
            jsonDesc.put("hasList", Boolean.TRUE);
            jsonDesc.put("list",gtList(sec));

        }
        jsonDes=jsonDesc.toString();
        exploreJson(jsonDesc);
    }
    private void exploreJson(JSONObject jsonObject) throws JSONException {
        hasCode=jsonObject.getBoolean("hasCode");
        hasList=jsonObject.getBoolean("hasList");
        hasTable=jsonObject.getBoolean("hasTable");
        try {
            n_desc=jsonDesc.getString("n_desc");
        }catch (Exception e){}

        if(hasTable){
            tableInfo=new TableInfo(jsonObject.getString("tableBoxed"));
        }
        else if(hasList){
            JSONArray lst=jsonObject.getJSONArray("list");
            JSONArray itm;
            for(int i=0;i<lst.length();i++)
            {
                itm=lst.getJSONArray(i);
                list.add(new String[]{itm.getString(0),itm.getString(1)});
            }
        }
        else if(hasCode){
            code=jsonObject.getString("code");
        }
    }


}

