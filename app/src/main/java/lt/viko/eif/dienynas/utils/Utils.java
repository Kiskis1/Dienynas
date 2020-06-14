package lt.viko.eif.dienynas.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utils {

    private static Gson gson;
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context cntxt) {
        context = cntxt;
    }

    public static Gson getGsonParser() {
        if (null == gson) {
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
        }
        return gson;
    }

}