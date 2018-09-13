package cobe.com.bejbikjum.helpers;

import android.arch.persistence.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cobe.com.bejbikjum.models.StringList;

public class StringListConverter {
    @TypeConverter
    public StringList storedStringToStringList(String value) {
        if(value == null)
            return  new StringList();
        ArrayList<String> list = new ArrayList<String>(Arrays.asList(value.split("\\s*,\\s*")));
        return new StringList(list);
    }

    @TypeConverter
    public String stringListToStoredString(StringList sl) {
        if(sl==null)
            return "";
        String value = "";

        for (String str :sl.getStringList())
            value += str + ",";

        return value;
    }
}

