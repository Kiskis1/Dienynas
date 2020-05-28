package lt.viko.eif.dienynas.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import lt.viko.eif.dienynas.models.Destytojas;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.repositories.StorageRepository;
//https://stackoverflow.com/questions/12157991/using-a-class-to-store-static-data-in-java

public class ApplicationData extends Application {
    private final static String TAG = ApplicationData.class.getSimpleName();
    private static Destytojas destytojas;

    public static Destytojas getDestytojas() {
        return destytojas;
    }

    public static void setDestytojas(Destytojas destytojas) {
        ApplicationData.destytojas = destytojas;
    }

    public static long getLastId(){
        long max = 0;
        for (Group g : destytojas.getGroup()){
            if (max < g.getId()) {
                max = g.getId();
            }
        }
        return max+1;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        StorageRepository.getInstance().getDest();

    }
}


