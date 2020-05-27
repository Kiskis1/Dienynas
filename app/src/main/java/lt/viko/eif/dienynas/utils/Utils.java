package lt.viko.eif.dienynas.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

    private static Gson gson;

    public static Gson getGsonParser() {
        if (null == gson) {
            GsonBuilder builder = new GsonBuilder();
            gson = builder.create();
        }
        return gson;
    }

    public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }

//    public static int getStringWidth(String str) {
//        Paint paint = new Paint();
//        paint.setTextSize(15);
//        Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
//        paint.setTypeface(typeface);
//        paint.setColor(Color.BLACK);
//        paint.setStyle(Paint.Style.FILL);
//        Rect result = new Rect();
//        paint.getTextBounds(str, 0, str.length(), result);
//
//        return result.width();
//    }
}