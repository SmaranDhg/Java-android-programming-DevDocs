package redeem.androidjava;

import android.sax.Element;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.select.Elements;

import java.security.AlgorithmConstraints;
import java.util.ArrayList;


import static redeem.androidjava.Cpp.*;
;

public class TableInfo implements table {

    private static int colCount=0,rowCount=0;
    private static ArrayList<ArrayList<String>> table=new ArrayList<>();
    private static ArrayList<String> titles=new ArrayList<>();




    public TableInfo(String boxTable) {
        unbox(boxTable.replace(TBL_ADD_ON, ""));
    }
    private static void unbox(String boxedTbl) {
        //data comes in colume wise so
        String cols[]=boxedTbl.split(TBl_COL_SEP);
        colCount=cols.length;
        String coldata[]=cols[0].split(TBL_COL_DAT_SEP)[1].split(TBL_DATA_SEP);
        rowCount=coldata.length;
       //adding data coulmn wise
        for(int j=0;j<colCount;j++)
        {
            String data;
            data=cols[j].split(TBL_COL_DAT_SEP)[0];
            titles.add(data);//here the column title
            ArrayList<String> colDat=new ArrayList<>();
            //now filling the columns
            for (int i = 0; i < rowCount; i++) {
                   data = cols[j].split(TBL_COL_DAT_SEP)[1].split(TBL_DATA_SEP)[i];
                   colDat.add(data);
               }

           table.add(colDat);

        }

    }

    @Override
    public int colCount() {
        return table.size();
    }

    @Override
    public int rowCount() {
        return table.get(0).size();
    }

    @Override
    public ArrayList<String> colDatAt(int colIndex) {
        return colIndex<colCount?table.get(colIndex):null;
    }

    @Override
    public ArrayList<String> rowDatAt(int rowIndex) {
        if(rowIndex<rowCount) {
            ArrayList<String> rowDatAt = new ArrayList<>();
            for (ArrayList<String> col : table) {
                rowDatAt.add(col.get(rowIndex));
            }
            return rowDatAt;
        }
        return null;

    }

    @Override
    public String datAt(int colIndex, int rowIndex) {
        return colIndex<colCount && rowIndex<rowCount?table.get(colIndex).get(rowIndex):"";
    }

    @Override
    public String titleOf(int colNum) {
        return colNum<colCount?titles.get(colNum):"";
    }

    @Override
    public ArrayList<String> titles() {
        return titles;
    }
}
interface table
{
      int colCount();
      int rowCount();
      ArrayList<String> colDatAt(int colIndex);
      ArrayList<String> rowDatAt(int rowIndex);
      String datAt(int colIndex,int rowIndex);
      String titleOf(int colNum);
      ArrayList<String> titles();

}
class TableJason implements table
{
    public TableJason(org.jsoup.nodes.Element tBody) throws JSONException {
        exploreSection(tBody);
    }
    public TableJason(JSONObject jsonObject) throws JSONException {
        exploreJasonTable(jsonObject);
    }
    private JSONObject tableJason;
    private ArrayList<ArrayList<String>> table;//[row][col]
    private ArrayList<String> titles;
    public static final String TITLES="titles";
    public static final String TABLE="Table";
    //on table table body
    private void exploreSection(org.jsoup.nodes.Element tBody) throws JSONException {
        try {
            Elements rows = tBody.select(">tr:not(:has(th))");
            String[] ttls = tfJava.prosTbl(tBody, tfJava.Tbl_Ttl_In_Str).split(tfJava.Tbl_Ttl_Sep);
            JSONArray titiles = new JSONArray();
            for (String ttl : ttls) {
                titiles.put(ttl);
            }//adding titles
            tableJason.put(TITLES,titiles);
            JSONArray tbl=new JSONArray();
            for (org.jsoup.nodes.Element row : rows) {
                try {
                    //for one row one alist of column data
                    JSONArray cDat=new JSONArray();
                    for (org.jsoup.nodes.Element col : row.select(">td")) {
                        cDat.put(col.text());
                    }
                    tbl.put(cDat);
                    //now adding the column in column data
                }catch (Exception e)
                {
                    continue;
                }
            }
            tableJason.put(TABLE,tbl);//finally adding the json array of json to tablejason objec
        }
        catch (Exception e){

        }
        exploreJasonTable(tableJason);
    }
    private void exploreJasonTable(JSONObject jsonObject) throws JSONException {
        JSONArray tls=jsonObject.getJSONArray(TITLES);
        for(int i=0;i<tls.length();i++) titles.add(tls.getString(i));
        JSONArray tbl=jsonObject.getJSONArray(TABLE);
        for(int i=0;i<tbl.length();i++)
        {
          JSONArray row=tbl.getJSONArray(i);
          ArrayList<String> rDat=new ArrayList<>();
          for(int j=0;j<row.length();j++) rDat.add(row.getString(j));
          table.add(rDat);
        }




    }

    @Override
    public int colCount() {
        return titles.size();
    }

    @Override
    public int rowCount() {
        return table.size();
    }

    @Override
    public ArrayList<String> colDatAt(int colIndex) {
        ArrayList<String> coldata=new ArrayList<>();
        for(ArrayList<String> row:table)
        {
            coldata.add(row.get(colIndex));
        }
        return coldata;
    }

    @Override
    public ArrayList<String> rowDatAt(int rowIndex) {
        return table.get(rowIndex);
    }

    @Override
    public String datAt(int colIndex, int rowIndex) {
        return table.get(rowIndex).get(colIndex);
    }

    @Override
    public String titleOf(int colNum) {
        return titles.get(colNum);
    }

    @Override
    public ArrayList<String> titles() {
        return titles;
    }
}

