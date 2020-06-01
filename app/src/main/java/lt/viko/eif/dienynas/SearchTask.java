package lt.viko.eif.dienynas;

import android.os.AsyncTask;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import lt.viko.eif.dienynas.fragments.MainFragment;
import lt.viko.eif.dienynas.models.Destytojas;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.models.Student;
import lt.viko.eif.dienynas.repositories.StorageRepository;

//https://stackoverflow.com/questions/9458258/return-a-value-from-asynctask-in-android
public class SearchTask extends AsyncTask<Void, Void, Void> {
    private final static String TAG = SearchTask.class.getSimpleName();
    private StorageRepository repo = StorageRepository.getInstance();
    private List<Group> groupList = new ArrayList<>();
    private Group newGroup;
    private List<Student> student = new ArrayList<>();
    private String code;
    private MainFragment mainFragment;

    public SearchTask(String code, MainFragment mainFragment) {
        this.code = code;
        this.mainFragment = mainFragment;
    }

//TODO: CHECK IF FOUND > 0
    @Override
    protected Void doInBackground(Void... voids) {
//        Log.i(TAG, "SearchTask: " + code);
        groupList.clear();
        student.clear();
        repo.searchForGrades().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Destytojas dest = queryDocumentSnapshot.toObject(Destytojas.class);
                    for (Group group : dest.getGroup()) {
                        newGroup = new Group();
                        newGroup.setId(group.getId());
                        newGroup.setTask(group.getTask());
                        newGroup.setName(group.getName());
                        for (Student stud : group.getStudents()) {
                            if (stud.getCode().equals(code)) {
                                student.add(stud);
                                newGroup.setStudents(student);
                                groupList.add(newGroup);
                            }
                        }
                    }
                }
                mainFragment.postGrades(groupList);
            }
        });
        return null;
    }

}