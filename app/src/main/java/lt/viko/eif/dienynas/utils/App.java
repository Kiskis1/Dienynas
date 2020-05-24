package lt.viko.eif.dienynas.utils;

import android.content.Context;
//https://stackoverflow.com/questions/2002288/static-way-to-get-context-in-android
public class App {
    private static Context context;

    public static void setContext(Context cntxt) {
        context = cntxt;
    }

    public static Context getContext() {
        return context;
    }
}