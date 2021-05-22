package redeem.androidjava;

public class A_libraryInfo {


    public A_libraryInfo(String libraryName, String descLibrary, String[] catagoryList) {
       this.LibraryName = libraryName;
        this.descLibrary = descLibrary;
        this.catagoryList = catagoryList;
    }

    public String getLibraryName() {
        return LibraryName;
    }

    public String getDescLibrary() {
        return descLibrary;
    }

    public String[] getCatagoryList() {
        return catagoryList;
    }

    private String LibraryName;
    private String descLibrary;
    private String[] catagoryList ;


}
