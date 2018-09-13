package cobe.com.bejbikjum.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringList {
    private ArrayList<String> list;

    public StringList(ArrayList<String> list) {
        this.list = list;
    }

    public StringList(){
        this.list = new ArrayList<String>();
    }

    public StringList(String newList){
        this.list = new ArrayList<String>(Arrays.asList(newList.split("\\s*,\\s*")));
    }

    public ArrayList<String> getStringList() {
        return list;
    }

    public void setStringList(ArrayList<String> list) {
        this.list = list;
    }

    public void add(String s){
        list.add(s);
    }

    public void remove(Object s){
        list.remove(s);
    }
}