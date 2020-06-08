package lt.viko.eif.dienynas.repositories;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import lt.viko.eif.dienynas.models.Destytojas;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.utils.ApplicationData;

//TODO: DOT NOTATION
public class StorageRepository {
    private final static String TAG = StorageRepository.class.getSimpleName();

    private static final StorageRepository ourInstance = new StorageRepository();

    public static StorageRepository getInstance() {
        return ourInstance;
    }

    private StorageRepository() {
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Destytojas dest;


    public void getDest() {
        db.collection("dest")
                .document("0xMxDmsl6maRFdBYhJ9T")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        dest = documentSnapshot.toObject(Destytojas.class);
                        //Log.i(TAG, "onCreate: " + dest.getGroup().get(0).getStudents().toString());
                        ApplicationData.setDestytojas(dest);
                    }
                });
    }

    public Task<DocumentSnapshot> getDestytojas(FirebaseUser firebaseUser) {
        return db.collection("dest")
                .document(firebaseUser.getUid()).get();
    }

    public Task<Void> setDestytojas(FirebaseUser firebaseUser, Destytojas destytojas) {
        return db.collection("dest")
                .document(firebaseUser.getUid())
                .set(destytojas);
    }

    public void addGroup(Group group) {
        db.collection("dest")
                .document("0xMxDmsl6maRFdBYhJ9T")
                .update("group", FieldValue.arrayUnion(group))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "onSuccess: add group success ASDSDGARaftRAGDFrwqsfgASGFNRWasgftAMFTesgfsatsgdmftewADHTEWAHDGTEWGF");
                    }
                });
    }

    public void setData(Destytojas dest) {
        db.collection("dest")
                .add(dest);
    }

    public void setDest(Destytojas dest) {
        db.collection("dest")
                .document("0xMxDmsl6maRFdBYhJ9T")
                .set(dest);
    }

    public CollectionReference searchForGrades() {
        return db.collection("dest");
    }
}
