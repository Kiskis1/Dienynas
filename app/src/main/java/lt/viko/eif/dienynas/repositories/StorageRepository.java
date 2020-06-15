package lt.viko.eif.dienynas.repositories;

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

    public Task<DocumentSnapshot> getDestytojas(FirebaseUser firebaseUser) {
        return db.collection("dest")
                .document(firebaseUser.getUid()).get();
    }

    public void getDest(FirebaseUser firebaseUser) {
        db.collection("dest")
                .document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Destytojas destytojas = documentSnapshot.toObject(Destytojas.class);
                ApplicationData.setDestytojas(destytojas);
            }
        });
    }

    public Task<Void> setDestytojas(FirebaseUser firebaseUser, Destytojas destytojas) {
        return db.collection("dest")
                .document(firebaseUser.getUid())
                .set(destytojas);
    }

    public void addGroup(Group group, FirebaseUser user) {
        db.collection("dest")
                .document(user.getUid())
                .update("group", FieldValue.arrayUnion(group))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });
    }

    public CollectionReference searchForGrades() {
        return db.collection("dest");
    }
}
