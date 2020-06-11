package lt.viko.eif.dienynas.utils;

import android.app.Application;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import lt.viko.eif.dienynas.models.Destytojas;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.repositories.StorageRepository;
//https://stackoverflow.com/questions/12157991/using-a-class-to-store-static-data-in-java

public class ApplicationData extends Application {
    private final static String TAG = ApplicationData.class.getSimpleName();
    private static Destytojas destytojas;
    private static long groupId;
    private static boolean signedIn = false;

    public static String getTAG() {
        return TAG;
    }

    public static boolean isSignedIn() {
        return signedIn;
    }

    public static void setSignedIn(boolean signedIn) {
        ApplicationData.signedIn = signedIn;
    }

    public static long getGroupId() {
        return groupId;
    }

    public static void setGroupId(long groupId) {
        ApplicationData.groupId = groupId;
    }

    public static Destytojas getDestytojas() {
        return destytojas;
    }

    public static void setDestytojas(Destytojas destytojas) {
        ApplicationData.destytojas = destytojas;
    }

    public static long getLastId() {
        long max = 0;
        for (Group g : destytojas.getGroup()) {
            if (max < g.getId()) {
                max = g.getId();
            }
        }
        return max + 1;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //TODO MAYBE PUT THIS IN SPLASH SCREEN
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ApplicationData.setSignedIn(true);
            StorageRepository.getInstance().getDest(FirebaseAuth.getInstance().getCurrentUser());

        } else ApplicationData.setSignedIn(false);
        Log.i(TAG, "onCreate: aSDASDASFSFHGASFDADGASDGSDFGSDGSDFGSDFGDFSGADHADFHDSFH");

    }
}


