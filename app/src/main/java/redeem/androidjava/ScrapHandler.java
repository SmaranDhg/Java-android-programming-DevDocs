package redeem.androidjava;

import android.os.Build;
import android.util.JsonWriter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;

import static android.content.ContentValues.TAG;
import static redeem.androidjava.ScrapHandler.*;

public class ScrapHandler {
    public static final String GOOGLE="g";
    public static final String eximSep="%%%%";
    public static final int INTERNET_RED=010;
    public static final int INTERNET_GREEN=101;
    public static  int INTERNET_FLAG=INTERNET_GREEN;


    public static void getData() throws IOException, InterruptedException {
        String url="https://developer.android.com/reference/android/accounts/AccountAuthenticatorActivity";


              Document doc = null;
                try {
                    doc = Jsoup.connect(url).maxBodySize(0).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements es = doc.getElementsByTag("tr");
                for(org.jsoup.nodes.Element e: es )

                {
                    Log.d("item", "run: "+e.text());
                    if(e.attr("data-version-added")!=null)
                    {
                        if(e.getElementsByAttribute("href").attr("href").contains("dismissKeyboardShortcutsHelper()"))
                        {
                            e.getElementsByTag("p").text();

                        }
                    }


                }









    }
    private static String setWhResult(String text) throws InterruptedException {
        String url= makeGSearchUrl(text,GOOGLE);
        String outCome="";

        return outCome;
    }
    private static  String makeGSearchUrl(String query,String flag)

    {

        String url="";
        switch (flag) {
            case GOOGLE:
                query.replace("what is","");
                query.replace("what", "");

                url="https://www.google.com/search?q=what+is+";
                for(String item:query.split(" "))
                {
                    if(query.endsWith(item)) break;//for last text
                    url=url+item+"+";
                }
                url=url+"&ie=&oe=";
                break;

            case "devPack":


            default:
                url=makeGSearchUrl(query, "g");
                break;
        }


        return url;
    }
    public static String getDescriptionOfPackage(String pName) throws IOException
    {

        Document doc =null;

        try{

            doc=Jsoup.connect("https://developer.android.com/reference/packages").maxBodySize(0).get();
        }catch (IOException e)
        {
            return "No Internet Connection,Sorry!";
        }

        Elements ds= doc.select("tr").select(">td:matches("+pName+"$)+td");
        //here selects the tag td which is directly after td.matches(pName) where pName is text
        return ds.text().length()!=0?ds.text():"Sorry, No description available for this "+pName+"!";
    }

    public static String getDescriptionOfClass(String pName,String cName) throws InterruptedException
    {
        String outCome="",ExIm;


        Document doc;
        try {
            doc = Jsoup.connect("https://developer.android.com/reference/"+(pName+"/").replaceAll("\\.", "/")+cName).maxBodySize(0).get();
        } catch (IOException e1) {

            return "No Internet Connection,Sorry!"+eximSep+"No connection";
        }
        Elements es = doc.getElementById("jd-content").select(">p");
        //getting the exact position of the paragraphs with discription of class
        //as the 1st paragraph alwayz has the ex im of the class
        //so we should care full checks the last siblings of div.p tag
        int last=es.size()!=0?(es.size()>1?(es.size()-1):0):(0);
        int first=last!=0?(last-es.size()+2):0;

        System.out.println("last:"+last+" first:"+first+" "+cName);
        for(int i=first;i<=last && i!=0;i++)
            outCome=((outCome.isEmpty())?outCome+es.get(i).text():outCome+" \n\n"+es.get(i).text());//except first

        ExIm=es.get(0).text().length()!=0?es.get(0).text():"No ExIm sorry";

        return outCome.length()!=0?outCome+eximSep+ExIm:"No description available for "+cName+" at this moment,sorry!"+eximSep+ExIm;


    }



}

 class fu {
    public static final String bs="https://www.crummy.com/software/BeautifulSoup/bs4/doc/";
    public static final String py="https://docs.python.org/3/library/index.html";
    public static final String tfjava="https://www.tensorflow.org/api_docs/java/reference/org/tensorflow/package-summary";
    public static final String uexPy="/3/library/";
    public static final String tfpy="https://www.tensorflow.org/api_docs/python/tf";
    public static final String cpp="http://www.cplusplus.com/reference/";
    public static final String ROW_COUNT="RC";
    public static final String COULUMN_COUNT="CC";
    public static final String NESTED_CLASS="NC";
    public static final String NESTED_CLASS_DESC="NCD";
    public static final String INHERITED_METHOD="IM";
    public static final String TH="TH";
    public static final String TD="TD";
    public static final String PARAMETER_NAME="PN";
    public static final String PARAMETER_DESC="PD";
    public static final String SEN_SEP="\\. ";
    public static final String LN_SEP="\\.\n";
    public static final boolean show=true;




    public static void print(Element e) {
        if(e!=null) print(e.text());
    }
    public static void print(String e) {
        System.out.println(e);
    }
    public static void print(String[] es) {
        for(String e:es) print(e);
    }
    public static void print(ArrayList<String> dats) {
        for(String dat: dats) print(dat+"\n");
    }


    public static Document get_js(String url) throws IOException {
        try {
            INTERNET_FLAG=INTERNET_GREEN;
            return Jsoup.connect(url).maxBodySize(0).get();
        }catch (Exception e)
        {
            Log.e("err", "get_js: ",e );
            //just to try for 5 more time;
            INTERNET_FLAG= INTERNET_RED;
            return get_js(url,0);
        }



    }
     private static Document get_js(String url,int loopiter ) throws IOException {
         try {
             INTERNET_FLAG=INTERNET_GREEN;
             return Jsoup.connect(url).maxBodySize(0).get();
         } catch (Exception e) {
             INTERNET_FLAG = INTERNET_RED;
             Log.e("get_js", "get_js: "+loopiter );
             if(loopiter>5) return null;
             else return get_js(url,loopiter=loopiter+1);
         }

     }





         public static ArrayList<String> getAllUrl(String Url, boolean show, String extraSwith, String extra2Ewith, String extra3Contains, String notContain, String extra4uAddOn) throws IOException
    {
        ArrayList<String> urls=new ArrayList<String>();
        Document js=get_js(Url);
        for(Element e:js.select("a[href]"))
        {
            String url=extra4uAddOn+e.attr("href");
            if(url.startsWith(extraSwith) && url.endsWith(extra2Ewith) &&url.contains(extra3Contains)&&!url.contains(notContain) && url.length()>1&& !urls.contains(url))
            {
                urls.add(mkUrl(Url, url));
                if(show)System.out.println(mkUrl(Url, url));
            }


        }


        return urls;

    }
    public static ArrayList<String> getAllUrl(String Url,boolean show,String starts,String ends) throws IOException
    {

        return getAllUrl(Url, show, starts, ends,"","","");

    }
    public static ArrayList<String> getAllUrl(String Url,String contains,String notcontains) throws IOException
    {

        return getAllUrl(Url, true, "", "",contains,notcontains,"");

    }
    public static ArrayList<String> getAllUrl(String Url,String starts,String contains,String notcontains) throws IOException
    {

        return getAllUrl(Url, true, starts, "",contains,notcontains,"");

    }
    public static ArrayList<String> getAllUrl(String Url) throws IOException
    {

        return getAllUrl(Url, false, "", "","","","");

    }
    public static ArrayList<String> getAllUrl(String Url,boolean show) throws IOException
    {

        return getAllUrl(Url, show, "", "","","))(*&$%^","");

    }
    public static ArrayList<String> getAllUrl(String Url,String starts) throws IOException
    {

        return getAllUrl(Url, false, starts, "","","))(*&$%^","");

    }


    public static String mkUrl(String mainUrl,String internalUrl) throws MalformedURLException {
        String url="";
        URL u=new URL(mainUrl);
        url=u.getProtocol()+"://"+u.getAuthority()+internalUrl;

        return url;

    }


    public static String rmLn(String data) {

        return data.replace("\n", " ").replace("   ", "");

    }




    public static void print(Elements es) {

        System.out.println("Section:"+gtSecHdin(es.get(0).parent().parent()));
        for (Element e : es) {
            try {
                System.out.println(e.text()+":"+gtfDesc(e.parent()));
            }catch (Exception err) {
                System.out.println("error:"+e.text()+err);
                continue;
            }
        }


    }

    //for pythons
    public static Elements gtSections(Element parent) {
        return parent.select(">.section");

    }
    public static String gtSecHdin(Element sec)
    {
        try {
            return sec.selectFirst(">h1, >h2, >h3 ,>h4 ,>h5,>h6").text();
        }catch (Exception e) {
            return null;
        }
    }
    public static Elements gtHdin(Element sec)
    {
        return sec.select("h1, h2, h3 ,h4 ,h5,h6");
    }

    public static String gtSecDecs(Element sec) {
        return sec.select(">p").text();

    }
    public static Elements gtAlFn(Element sec)
    {
        return sec.select(">.function");
    }
    public static String gtfDesc(Element function)

    {
        return function.select(">dd").text();
    }
    public static String gtfName(Element function)

    {
        return function.select(">dt").first().text();

    }
    public static Elements gtClasses(Element sec)
    {
        return sec.select(">.class");
    }
    public static String gtcName(Element Class)
    {
        return Class.select(">dt").text();

    }
    public static String gtcDesc(Element Class)
    {
        return Class.select(">dd>p").text();
    }
    public static ArrayList<String> gtcAttributesNm(Element Class)
    {
        ArrayList<String> attrs= new ArrayList<String>();
        for(Element e : Class.select(">.attributes"))
        {
            attrs.add(e.text());

        }
        return attrs;

    }
    public static ArrayList<String> gtcAlMtdsNm(Element Class)
    {
        ArrayList<String> mtds= new ArrayList<String>();
        for(Element e : Class.select(">dd>dl.method"))
        {
            mtds.add(e.text());

        }
        return mtds;

    }
    public static Elements gtAlMtds(Element Class) {
        return Class.select(">dd>dl.method");
    }
    public static String gtmName(Element method) {
        return method.select(">dt").first().text();
    }
    public static String gtmDesc(Element method) {
        return method.select(">dd").text();

    }

    public static void ExplorePython(Document js) {
        // body hieraracy class-> document->.documentwrapper->.bodywrapper->.body->.section #mainId  h1 p divs
        // ->.section #secindaryId h2

        //body
        Element  body,mainTitle,mainDesc;
        Elements attrs,methods,describe,seconds,secondsTitles,secondsDesc,mName,mDesc,functions,fTitle,fDesc,classes,cName,cDesc;
        body=js.select(".body[role=main]>.section").first();
        mainTitle=body.selectFirst("h1");
        mainDesc=body.selectFirst("p");


        //sections
        seconds=body.select(">.section");
        secondsTitles=seconds.select(">h2");
        secondsDesc=seconds.select(">p");


        //functions
        if(seconds.isEmpty()) functions=body.select(">dl.function");
        else functions=seconds.select(">dl.function");//inside particular section only
        fTitle=functions.select(">dt");
        fDesc=functions.select(">dd");



        //classes
        classes=seconds.select(".class");
        cName=classes.select(">dt");
        cDesc=classes.select(">dd>p");
        attrs=classes.select(">.attributes");
        describe=classes.select(">dd>dl.describe");
        methods=classes.select(">dd>dl.method");
        mName=methods.select(">dt");
        mDesc=methods.select(">dd");

        print(fTitle);






    }


}
class tfJava
{
    public static final String ROW_COUNT="RC";
    public static final String COULUMN_COUNT="CC";
    public static final String NESTED_CLASS="NC";
    public static final String NESTED_CLASS_DESC="NCD";
    public static final String INHERITED_METHOD="IM";
    public static final String TH="TH";
    public static final String TD="TD";
    public static final String PARAMETER_NAME="PN";
    public static final String PARAMETER_DESC="PD";
    public static final String SEN_SEP="\\. ";
    public static final String LN_SEP="\\.\n";
    public static final String Tbl_Ttl_Sep=",:,";
    public static final String Tbl_Ttl_In_Str="TTIS";
    public static final String NONE="";


    public static void print(Element e) {
        if(e!=null)System.out.println(e.text());

    }
    public static void print(String e) {
        System.out.println(e);
    }
    public static void print(Elements e) {
        for(Element es:e) print(e.text());
    }//


    public static Element gtBody(Document js) {
        return js.selectFirst("article.devsite-article-inner").selectFirst(".devsite-article-body,.clearfix").selectFirst("div[style]");
    }
    public static String gtHdin(Element body)
    {
        return body.selectFirst(">div#jd-header").text();
    }
    public static Element gtMnCnt(Element body)
    {
        return body.selectFirst(">div#jd-content");

    }
    public static Element gtClsDescr(Element content)
    {
        return content.selectFirst(".jd-descr");
    }
    public static Elements gtTblSec(Element content,Boolean expandable)
    {
        if(!expandable)
            return content.select(".jd-descr>section#xml-attributes>:not(#inhmethods)");
        else
            return content.select(".jd-descr>section#xml-attributes>#inhmethods .expandable");
    }
    public static Element gtTbl(Element tblsec)
    {

        return tblsec.selectFirst(".jd-sumtable>tbody, .jd-sumtable-expando>tbody,.jd-tagtable>tbody");

    }
    public static String prosTbl(Element tbody_tr,String value)
    {
        String dat="";
        Element tbody=tbody_tr,tr=tbody_tr;
        Elements tds;
        tds=tr.select(">td");
        switch (value) {
            case COULUMN_COUNT:
                return String.valueOf(tbody.selectFirst(">tr").select(">td").size());
            case ROW_COUNT:
                return String.valueOf(tbody.select(">tr").size());
            case NESTED_CLASS:
                return tds.size()==3?tds.get(0).text()+" "+tds.get(1).text():tds.get(tds.size()-2).text();
            case NESTED_CLASS_DESC:
                return tds.get(2).text();
            case INHERITED_METHOD:
                for(Element td:tds)
                {
                    dat=dat+" "+td.text();
                }
                return dat;
            case TH:
                return tr.select(">th").text();
            case TD:
                return tr.select(">td").text();
            case PARAMETER_NAME:
                return prosTbl(tr, TH);
            case PARAMETER_DESC:
                return prosTbl(tr, TD);
            case Tbl_Ttl_In_Str:

                for(Element e: tbody.selectFirst("tr").select("th"))
                {
                    dat=dat+Tbl_Ttl_Sep+e.text();
                }

                //so that first seperator is gone
                return dat.replaceFirst(Tbl_Ttl_Sep, NONE);





            default:
                break;
        }
        return null;

    }
    public static Elements gtRows(Element tbody)
    {
        return tbody.select(">tr");
    }
    //methods with description
    public static Elements gtMethods(Element content)
    {
        return  content.selectFirst(">section#public-methods").select(".jd-details");
    }

    //in case enums and with all
    public static Element gtDescNrm(Element mtd) {
        return mtd.selectFirst(".jd-tagdata,.jd-tagdescr");
    }
    //like  parameters
    public static Element gtDescWdHd(Element mtd) {
        return mtd.selectFirst(".jd-tagdata:has(.jd-tagtable)");
    }
    public static String gtNrmDesc(Element desc)
    {
        return desc.select(">p").text();
    }
    public static String gtCodDes(Element desc)
    {
        if(desc.select("pre").isEmpty())
            return null;
        else return desc.select("pre").text();
    }
    //for enums has heading and desc
    public static Elements gtEnumVals(Element content)
    {
        return content.selectFirst(">section#enum-values").select(".jd-details");
    }
    public static String eName(Element Enum)
    {
        return fu.gtSecHdin(Enum);
    }
    public static String eDesc(Element Enum)
    {
        return Enum.select("div.jd-details-descr").text();
    }
    public static void Explore(String URl) throws IOException {
        Document js=fu.get_js("https://www.tensorflow.org/api_docs/java/reference/org/tensorflow/OperationBuilder");

        Element body,content,tblSec,tbl,desc;
        Elements sections,methods,Enums;



        //for classs>div[style]
        body=gtBody(js);
        content=gtMnCnt(body);
        Enums=gtEnumVals(content);
        methods=gtMethods(content);



//        tblSec=gtTblSec(content, true).get(0);
//       tbl=gtTbl(tblSec);
//        methods=gtPubMtd(content);
//        desc=gtDesc(gtMethods(methods).get(2));
//
//
//
//       System.out.println(content);
//
//

    }


}
class Tfpy
{
    public static final String CODE_SEP="Code:\n";

    //for modules
    public static Elements gtModules(Element body)
    {
        return body.select("p:matches(^?module:(.)*$)");
    }
    public static String peekModule(Element module)
    {
        return module.text().split("module:")[0]+"\n"+module.text().split("module: ")[1];
    }
    public static String  gtHref(Element module)
    {
        return module.selectFirst("a").attr("href");

    }
    public static Elements gtCls(Element body)
    {

        return body.select("p:has(a:matches(^?class(.)*$?))");
    }
    public static String  peekClass(Element Class)
    {
        return Class.text().split(": ")[0]+":\n"+Class.text().split(": ")[1];
    }
    public static Elements gtFunct(Element body)
    {
        return body.select("p:has(a:matches(^?(.)+?\\(...\\)))");
    }
    //for classs
    public static String gtcName(Document js)
    {
        return js.select("h1").text();
    }
    public static Elements gtH3(Element body)
    {
        return body.select("h3[id]:matches(^?.*?:$?)");
    }
    public static String gtUl(Element H3)

    {
        //here data is linear without section so wwe have to deal with parent
        //and the previous element id
        String a="";
        for(Element l:H3.parent().select("#"+H3.id()+" + ul").select("li"))
        {
            a=a+l.text()+"\n";
        }
        return a;
    }
    public static ArrayList<String> gtClsDescr(Element body)
    {
        //we use constructor as the breaking point of the description and the other component
        Element cosnt=body.selectFirst("#__init__");
        ArrayList<String> desc= new ArrayList<String>();
        for(Element e:  body.select("p:lt("+cosnt.elementSiblingIndex()+"),pre:lt("+cosnt.elementSiblingIndex()+")"))
        {
            if(e.tagName()=="pre") desc.add(CODE_SEP+e.text());
            else desc.add(e.text());

        }
        return desc;
    }

    public static String gtConst(Element body)
    {
        return CODE_SEP+body.select("pre:matches(^__init__(.)*)").text();

    }

    /*other imps*/
    public static Elements gtH2s(Element body)
    {
        //i.e constructor
        int breakZone=body.selectFirst("pre:matches(^__init__(.)*)").elementSiblingIndex();
        return body.select("h2:gt("+breakZone+")");
    }
    //layesrs matrics_names stuff
    public static ArrayList<String> gtH2Desc(Element H2) {
        int Start=H2.elementSiblingIndex();
        ArrayList<String> desc= new ArrayList<String>();

        for(Element e: H2.parent().select(">:gt("+Start+")"))
        {
            if(e.tagName()=="h2") break;
            if(e.tagName().matches("^?(p|ul)$?")) desc.add(e.text());
            else desc.add(e.tag()+":\n"+e.text());

        }
        return desc;
    }

    //for function
    public static ArrayList<String> gtFuDesc(Element body)
    {
        ArrayList<String> desc= new ArrayList<String>();

        //only the tag with text
        for(Element e: body.select(">*:matches(.+)"))
        {
            if(e.tagName()=="h2") break;
            if(e.tagName().matches("^?(p|ul)$?")) desc.add(e.text());
            else desc.add(e.tag()+":\n"+e.text());

        }
        return desc;

    }
    //extra
    public static Element gtBody(Document js) {
        return js.selectFirst("article.devsite-article-inner").selectFirst(".devsite-article-body,.clearfix");
    }

    public static void Explore(String Url) throws IOException
    {
        Document js=fu.get_js(Url);
        Element body,tblSec,cName,desc,h3,cosnt,h2;
        Elements modules,cls,function;

        body=gtBody(js);

        fu.print(gtFuDesc(body));
//		h2=gtH2s(body).get(1);
//		System.err.println(h2.text());

//		h3=gtH3(body).get(1);
//		cosnt=body.selectFirst("#__init__");
//
//
// 		//print(gtUl(h3));
//		modules=gtModules(body);
//		cls=gtCls(body);
//		function=gtFunct(body);
//






    }
}
class Cpp
{
    public static final String ID_PARAMETER="parameters";
    public static final String ID_INSTANTIATION="instantiations";
    public static final String ID_ENUM="Etypes";
    public static final String ID_TYPES="types";
    public static final String ID_FUNCTIONS="functions";
    public static final String ID_CLASS="classes";
    public static final String ID_DESCRIPTION="description";
    public static final String ID_DESCRIPTION_TITLE="I_description";
    public static final String ID_SEE="see";
    public static final String ID_EXAMPLE="example";
    public static final String ID_M_ACCESS="access";
    public static final String ID_MACROS="macros";
    public static final String ID_M_EXCEPTIONS="exceptions";
    public static final String ID_M_RETURN="return";
    public static final String CLASS_FUN_NAME="C_prototype";
    public static final String CLASS_CODE_SOURCE="source";
    public static final String ITEM_SEP="::ITEM_SEP;;";
    public static final String HEAD_SEP="::HEAD_SEP;;";
    public static final String HEAD_DATA_SEP="::HDS;;";
    public static final String H_ITEM_SEP="::HITEM_SEP;;";
    public static final String H_HEAD_SEP="::HHEAD_SEP;;";
    public static final String H_HEAD_DESC_SEP="::HHDS;;";
    public static final String C_ITEM_SEP="::CITEM_SEP;;";
    public static final String C_HEAD_SEP="::CHEAD_SEP;;";
    public static final String C_HEAD_DESC_SEP="::CHDS;;";
    public static final String CODE_ADD_ON=" :;Code:; ";
    public static final String TBL_ADD_ON=" :;tbl:; ";
    public static final String TBl_COL_SEP="::TCS;;";
    public static final String TBL_COL_DAT_SEP=":TCDS;";
    public static final String TBL_DATA_SEP=":TDS;";
    public static final String ClsLink_Add_On=";:CLAO:;";
    public static final String ClsLink_SEP=";:CLS:;";
    public static final String LST_ADD_ON=":;LAO:;";
    public static final String L_DATA_SEP=":;LDS:;";
    public static final String CPP_MAIN_URL="http://www.cplusplus.com";

    //works for all
    public static Element gtBody(Document js)
    {
        return js.selectFirst(".C_doc");
    }
    //for the main page getting the modules and stuff with Hcats
    public static Elements gtHeadersCat() throws IOException
    {
        return fu.get_js("http://www.cplusplus.com/reference/").select("#I_nav>.C_BoxLabels>ul").select(">li") ;

    }
    public static Elements gtHeaders(Element hCat)
    {
        return hCat.select("ul>li");

    }
    public static String gtHCatDesc(Element hCat) throws IOException
    {
        Document js=fu.get_js(fu.mkUrl(fu.cpp, hCat.select("h4>a[href]").attr("href")));
        Element body=js.selectFirst("div.C_doc");
        String desc="";
        for(Element a:body.select(">*:not(#headers)"))
        {

            if(a.tagName().matches("^?h3$?")|| a.attr("id")=="headers") break;
            desc=desc+a.text();
        }
        return desc;

    }
    public static Document gtHeader(Element header) throws IOException
    {
        return fu.get_js(fu.mkUrl(fu.cpp, header.select("a[href]").attr("href")));
    }
    //headers
    public static String gtHDesc(Element hBody)
    {
        return hBody.select(">#description").text();
    }
    public static Element gtSecCat(Element hBody,String catId)
    {
        Element e=null;
        //here the enum_class and member types contains save id so
        if(catId==ID_ENUM ) if(gtSecHd(gtSecCat(hBody, catId.replaceAll("E", ""))).startsWith("Enum"))
            e= hBody.selectFirst("section#"+catId.replace("E", "")+"");
                            else return e;
        else e= hBody.selectFirst("section#"+catId.replace("E", "")+"");
        return e;

    }
    public static Elements gtSecCats(Element hBody)
    {
        return hBody.select("section[id]:not(#see)");

    }
    public static String gtLink(Element e) throws MalformedURLException {
        return fu.mkUrl(CPP_MAIN_URL,e.select("[href]").attr("href"));
    }

    public static Elements gtSecItems(Element secCat)
    {
        return secCat.select(">*:not(h1,h2,h3,h4,h5)");
    }
    public static String  gtSecTbl(Element secCat)
    {
        String table="",val;
        Elements tbls=secCat.select("table:not(.snippet,:has(pre))>tbody");
        for(Element tbl:tbls)
        {
            Elements rows=tbl.select(">tr:not(:has(th))");
            Elements cols=rows.select(">td");
            //jst with out the table heading

            try {
                String[] ttls = tfJava.prosTbl(tbl, tfJava.Tbl_Ttl_In_Str).split(tfJava.Tbl_Ttl_Sep);

                for(int i=0;i<cols.size();i++)
                {
                    //columes are separeted
                    if(i!=0) table=table+TBl_COL_SEP+ttls[i];
                    else table=table+ttls[i];

                    int j=0;
                    //we are adding the coulume data vertically
                    for(Element e:rows)
                    {
                        val=e.select(">td").get(i).text();
                        table=j!=0?table+TBL_DATA_SEP+val:table+TBL_COL_DAT_SEP+TBL_DATA_SEP+val;
                        j++;

                    }

                }

            }catch (Exception e) {

            }
            //if last table no need for tble add_on
            table=tbls.get(tbls.size()-1).equals(tbl)?table+HEAD_DATA_SEP:table+HEAD_DATA_SEP+TBL_ADD_ON;

        }

        return table;
    }
    public static String gtSecHd(Element secCat)
    {
        return secCat!=null?secCat.select(">h3").text():null;
    }
    public static String BoxHeader(Element hBody) throws IOException
    {
        String header="";
        header="HDR"+H_HEAD_SEP+fu.gtSecHdin(hBody);
        header=header+H_ITEM_SEP+"Description"+H_HEAD_SEP+gtHDesc(hBody);
        if(gtSecCats(hBody).isEmpty()) header=header+H_ITEM_SEP+"Description"+H_HEAD_SEP+BoxDesc(hBody, H_HEAD_DESC_SEP);
        else {
            for(Element sec:gtSecCats(hBody))
            {
                try {
                    if(isFunction(sec))header=header+H_ITEM_SEP+"Functions"+H_HEAD_SEP+BoxFunction(hBody, H_HEAD_DESC_SEP);
                    else if(isClass(sec)) header=header+H_ITEM_SEP+"Classes"+H_HEAD_SEP+BoxList(sec);
                    else if(hasListLink(sec)) header=header+H_ITEM_SEP+fu.gtSecHdin(sec)+H_HEAD_SEP+BoxListLink(sec);
                    else header=header+H_ITEM_SEP+fu.gtSecHdin(sec)+H_HEAD_SEP+BoxDesc(sec,H_HEAD_DESC_SEP);
                }catch (Exception e) {
                    continue;
                }
            }
        }

        return header;
    }
    //class
    public static String gtClsDesc(Element cbody)
    {
        return cbody.select("#description").text();
    }
    public static String BoxClass(Element cBody)
    {
        String Cls="CLS"+C_HEAD_SEP+fu.gtSecHdin(cBody)+"\n"+cBody.select("."+CLASS_FUN_NAME).text();
        for(Element sec:gtSecCats(cBody))
        {

            try {
                if(isFunction(sec))Cls=Cls+C_ITEM_SEP+"Functions"+C_HEAD_SEP+BoxFunction(cBody, C_HEAD_DESC_SEP);
                else if(hasListLink(sec)) Cls=Cls+C_ITEM_SEP+fu.gtSecHdin(sec)+C_HEAD_SEP+BoxListLink(sec);
                else if(isDescription(sec))Cls=Cls+C_ITEM_SEP+"Description"+C_HEAD_SEP+gtHDesc(cBody);
                else Cls=Cls+C_ITEM_SEP+fu.gtSecHdin(sec)+C_HEAD_SEP+BoxDesc(sec,C_HEAD_DESC_SEP);
            }catch (Exception e) {
                continue;
            }


        }

        return Cls;
    }

    //function
    //should be use after boolean check of the function
    public static String gtFReturn(Element fBody)
    {
        Element rSec=gtSecCat(fBody,ID_M_RETURN);
        String desc="";
        desc=rSec.select(">*:not(h1,h2,h3,h4)").text();
        return desc;

    }
    //should be use after boolean check of the function
    public static String gtFExample(Element fBody)
    {
        Element sec=gtSecCat(fBody,ID_EXAMPLE);
        return  sec.select(".source").text();
    }
    //should be use after boolean check of the function
    public static JSONObject gtFParameter(Element fBody) throws JSONException {
        JSONObject jsonObject=new JSONObject() ;
        //here data is not in separate dl instead in save dl
        Elements dts=gtSecCat(fBody,ID_PARAMETER).select(">dl>dt"),dds=gtSecCat(fBody,ID_PARAMETER).select(">dl>dd");
        int i=0;
        for(Element dt:dts)
        {
            String dat1=dt.text(),dat2=dds.get(i).text();
            if(!dat1.isEmpty()&&!dat2.isEmpty())
              jsonObject.put(dat1,dat2);
            i++;


        }
        return jsonObject;

    }


    public static ArrayList<String> gtClsFunc(Element cBody) throws IOException
    {
        ArrayList<String> methods=new ArrayList<String>();
        Element body;
        String hd,desc,id,descTitle,fName;
        for(Element m:gtSecItems(gtSecCat(cBody, ID_FUNCTIONS)))
        {
            try {
                String method="FN";
                body=gtBody(fu.get_js(fu.mkUrl(fu.cpp, m.select("a[href]").attr("href"))));
                fName=!body.select("."+CLASS_FUN_NAME).isEmpty()?body.select("."+CLASS_FUN_NAME).text():fu.gtSecHdin(body);
                method=method+HEAD_SEP+fName;
                descTitle=body.select("#"+ID_DESCRIPTION_TITLE).text();
                desc=body.select("#"+ID_DESCRIPTION).text();
                method=method+ITEM_SEP+descTitle+HEAD_SEP+desc;

                for(Element e : body.select("section:not(section#"+ID_SEE+",section#"+ID_DESCRIPTION+")"))

                {

                    method=method+ITEM_SEP+fBoxDesc(e);

                }
                methods.add(method);

            }
            catch (Exception e) {
                continue;
            }
        }

        return methods;
    }
    public static String fBoxDesc(Element fSec)
    {
        String desc="",hd=fu.gtHdin(fSec).first().text(),Desc;
        Desc=fSec.select(">*:not(h1,h2,h3,h4,dl,b,:has(tbody,pre,dl))").text();
        Desc=Desc.length()<2?fSec.select(">*:not(h1,h2,h3,h4,h5,dl,:has(tbody,pre,dl))").text():Desc;
        desc=hd+HEAD_SEP+Desc+HEAD_DATA_SEP;
        if(hasCode(fSec)) desc=desc+CODE_ADD_ON+fSec.select("."+CLASS_CODE_SOURCE+">pre").text()+HEAD_DATA_SEP;
        else if(hasTable(fSec)) desc=desc+TBL_ADD_ON+gtSecTbl(fSec)+HEAD_DATA_SEP;
        else if(hasTable(fSec)) desc=desc+LST_ADD_ON+BoxList(fSec)+HEAD_DATA_SEP;

        return desc;
    }
    public static String BoxDesc(Element sec,String sep)
    {
        String desc="",Desc;
        Desc=sec.select(">*:not(h1,h2,h3,h4,dl,b,:has(tbody,pre))").text();
        Desc=Desc.length()<2?sec.select(">*:not(h1,h2,h3,h4,h5,dl,:has(tbody,pre))").text():Desc;
        desc=Desc+sep;
        if(hasCode(sec)) desc=desc+CODE_ADD_ON+sec.select("."+CLASS_CODE_SOURCE+">pre").text()+sep;
        else if(hasTable(sec)) desc=desc+TBL_ADD_ON+gtSecTbl(sec)+sep;
        else if(hasList(sec)) desc=desc+LST_ADD_ON+BoxList(sec)+sep;

        return desc;

    }
    public static String BoxList(Element sec)
    {
        String list=LST_ADD_ON;
        for(Element e:sec.select(">dl"))
        {
            list=list+L_DATA_SEP+e.select(">dt").text()+L_DATA_SEP+e.select(">dd").text();

        }
        return list.replaceFirst(L_DATA_SEP, "");
    }
   public static Stack<String[]> gtList(Element sec)
   {
       Stack<String[]> dat=new Stack<>();
       String ttl,desc;
       for(Element e:sec.select(">dl"))
       {
           ttl=e.select(">dt").text();
           desc=e.select(">dd").text();
           ttl=ttl.length()<2?"**":ttl;
           desc=desc.length()<2?"**":desc;

           dat.add(new String[]{ttl,desc});

       }
       return dat;
   }
   //here returns the json array
    public static JSONArray gtHClasses(Element hBody)  {
        Element sec=gtSecCat(hBody,ID_CLASS);
        sec=sec!=null?sec:hBody.selectFirst("section:has(h1:contains(class),h2:contains(class),h3:contains(class),h4:contains(class))");
        JSONArray arrOfarr=new JSONArray();

        for(String[] e:gtList(sec))
        {
            JSONArray array=new JSONArray();
            array.put(e[0]).put(e[1]);//addin the array to jason array
            //now adding this array to big jason array
            arrOfarr.put(array);

        }
        return arrOfarr;
    }
    public static String BoxFunction(Element body,String sep) throws IOException
    {
        String function="";
        for(String fn:gtClsFunc(body))
        {
            function=function+sep+fn;
        }

        return function.replaceFirst(sep, "");
    }
    public static String BoxListLink(Element sec) throws IOException
    {
        String list=LST_ADD_ON;
        for(Element dl:sec.select("dl.links"))
        {
            Element body=gtBody(gtHeader(dl));//gtHeader can gt the document with by extracting the links
            list=list+fu.gtSecHdin(body)+L_DATA_SEP+fu.gtcDesc(body);

        }
        return list;
    }
    public static ArrayList<String> gtBoxClassLink(Element hBody) throws MalformedURLException
    {
        ArrayList<String> clsLink= new ArrayList<String>();
        Element clsSec=hBody.selectFirst("section:has(h4:contains(class),h2:contains(class),h3:contains(class))");
        if(clsSec!=null)
        {
            for(Element dl:clsSec.select("dl.links:has([href])"))
            {
                clsLink.add(fu.mkUrl(CPP_MAIN_URL, dl.select("[href]").attr("href")));
            }
        }
        return clsLink;
    }
    //extras
    public static boolean isClass(Element sec)
    {
        return sec.id().equals(ID_CLASS) || fu.gtSecHdin(sec).toLowerCase().contains("class");
    }
    public static boolean hasClasses(Element hbody)
    {
        return !hbody.select("[id=" + ID_CLASS + "]").isEmpty() || hbody.select("section").select("h1,h2,h3,h4").text().toLowerCase().contains("class");
    }
    public static boolean hasMacroConstants(Element hBody)
    {
        return !hBody.select("#"+ID_MACROS+":not(:has(dl.links),:not(:has(table)))").isEmpty();
    }
    public static Element gtMacroConstant(Element hbody)
    {
        return hbody.selectFirst("#"+ID_MACROS+":not(:has(dl.links),:not(:has(table)))");
    }
    public static boolean isDescription(Element sec)
    {

        return sec.id().equals(ID_DESCRIPTION);
    }
    public static  boolean hasParameter(Element fBody)
    {
        return gtSecCat(fBody, ID_PARAMETER) != null;
    }
    public static boolean hasReturn(Element fBody)
    {
        return  gtSecCat(fBody,ID_M_RETURN)!=null;
    }
    public static boolean hasExample(Element fBody)
    {
        return gtSecCat(fBody,ID_EXAMPLE)!=null;
    }
    public static boolean isFunction(Element sec)
    {
        return sec.id().equals(ID_FUNCTIONS);
    }
    public static boolean hasFunction(Element hBody)
    {
        return !hBody.select("#" + ID_FUNCTIONS + ",:has(h3:contains(function))").isEmpty();
    }
    public static Element gtfunctionSec(Element hBody)
    {
        return  hBody.selectFirst("#" + ID_FUNCTIONS + ",:has(h3:contains(function))");
    }
    public static boolean hasList(Element e)
    {
        return !e.select(">dl").isEmpty();
    }
    public static boolean hasListLink(Element e)
    {
        return !e.select("dl.links:has([href])").isEmpty();
    }
    public static boolean hasTable(Element e)
    {
        return !e.select("table:not(.snippet,:has(pre))").isEmpty();
    }
    public static Element gtTable(Element e)
    {
        return e.selectFirst("table:not(.snippet,:has(pre))");
    }

    public static boolean hasCode(Element e)
    {
        return !e.select(":has(pre):not(:has(table,dl.links)))").isEmpty();
    }




    public static String gtCode(String boxedCode)
    {
        return boxedCode.replace(CODE_ADD_ON, "");
    }
}