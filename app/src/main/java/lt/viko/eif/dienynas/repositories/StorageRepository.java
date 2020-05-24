package lt.viko.eif.dienynas.repositories;

import android.util.Log;

import com.google.gson.Gson;

import lt.viko.eif.dienynas.models.Destytojas;
import lt.viko.eif.dienynas.utils.App;
import lt.viko.eif.dienynas.utils.Utils;

public class StorageRepository {
    private static final StorageRepository ourInstance = new StorageRepository();

    public static StorageRepository getInstance() {
        return ourInstance;
    }

    private StorageRepository() {
    }

    public Destytojas getDestytojas() {

        Gson gson = new Gson();
        String jsonFileString = Utils.getJsonFromAssets(App.getContext(), "db.json");
        Log.i("data", jsonFileString);
        Destytojas dest = gson.fromJson(jsonFileString, Destytojas.class);
        return dest;
    }
}