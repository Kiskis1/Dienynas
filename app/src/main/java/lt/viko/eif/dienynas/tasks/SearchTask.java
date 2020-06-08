package lt.viko.eif.dienynas.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.fragments.MainFragment;
import lt.viko.eif.dienynas.models.Destytojas;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.models.Student;
import lt.viko.eif.dienynas.repositories.StorageRepository;

//https://stackoverflow.com/questions/9458258/return-a-value-from-asynctask-in-android
//https://stackoverflow.com/questions/10446125/how-to-show-progress-dialog-in-android
//https://stackoverflow.com/questions/39015136/hide-show-progressbar-using-asynctask
//https://stackoverflow.com/questions/4280608/disable-a-whole-activity-from-user-action
public class SearchTask extends AsyncTask<Void, Void, Void> {
    private final static String TAG = SearchTask.class.getSimpleName();
    private StorageRepository repo = StorageRepository.getInstance();
    private List<Group> groupList = new ArrayList<>();
    private String code;
    private MainFragment mainFragment;
    private Context context;
    private ProgressBar progressBar;

    public SearchTask(String code, MainFragment mainFragment, ProgressBar progressBar) {
        this.code = code;
        this.mainFragment = mainFragment;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
        mainFragment.getView().findViewById(R.id.main_progressBar_container).setVisibility(View.VISIBLE);
        mainFragment.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
//        Log.i(TAG, "SearchTask: " + code);
        groupList.clear();
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        repo.searchForGrades().get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Destytojas dest = queryDocumentSnapshot.toObject(Destytojas.class);
                    for (Group group : dest.getGroup()) {
                        for (Student stud : group.getStudents()) {
                            if (stud.getCode().equals(code)) {
                                Group newGroup = new Group();
                                List<Student> student = new ArrayList<>();
                                newGroup.setId(group.getId());
                                newGroup.setTask(group.getTask());
                                newGroup.setName(group.getName());
                                student.add(stud);
                                newGroup.setStudents(student);
                                groupList.add(newGroup);
                            }
                        }
                    }
                }
                if (mainFragment.getView() != null) {
                    mainFragment.postGrades(groupList);
                }
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.i(TAG, "onPostExecute: " + mainFragment.toString());
        if (mainFragment.getView() != null) {
            mainFragment.getView().findViewById(R.id.main_progressBar_container).setVisibility(View.INVISIBLE);
            mainFragment.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (progressBar != null && progressBar.isShown()) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}