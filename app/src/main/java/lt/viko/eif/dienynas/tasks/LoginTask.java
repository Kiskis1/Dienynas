package lt.viko.eif.dienynas.tasks;

import android.os.AsyncTask;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.fragments.LoginFragment;
import lt.viko.eif.dienynas.models.Destytojas;
import lt.viko.eif.dienynas.models.Group;
import lt.viko.eif.dienynas.repositories.StorageRepository;
import lt.viko.eif.dienynas.utils.ApplicationData;

public class LoginTask extends AsyncTask<Void, Void, Void> {
    private final static String TAG = LoginTask.class.getSimpleName();
    private StorageRepository repo = StorageRepository.getInstance();
    private LoginFragment loginFragment;
    private ProgressBar progressBar;
    private FirebaseUser firebaseUser;

    public LoginTask(LoginFragment loginFragment, ProgressBar progressBar, FirebaseUser firebaseUser) {
        this.loginFragment = loginFragment;
        this.progressBar = progressBar;
        this.firebaseUser = firebaseUser;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
        loginFragment.getView().findViewById(R.id.login_progressBar_container).setVisibility(View.VISIBLE);
        loginFragment.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        repo.getDestytojas(firebaseUser).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Destytojas dest = documentSnapshot.toObject(Destytojas.class);
                        ApplicationData.setDestytojas(dest);
                        loginFragment.loginSuccess();
                        postExec();
                    } else {
                        Random random = new Random();
                        final Destytojas destytojas = new Destytojas();
                        String[] name = firebaseUser.getDisplayName().split(" ");
                        if (name.length > 1) {
                            destytojas.setFirstName(name[0]);
                            destytojas.setLastName(name[1]);
                        } else {
                            destytojas.setFirstName(name[0]);
                            destytojas.setLastName(name[0]);
                        }
                        destytojas.setEmail(firebaseUser.getEmail());
                        destytojas.setId(random.nextInt(Integer.MAX_VALUE));
                        List<Group> groupList = new ArrayList<>();
                        destytojas.setGroup(groupList);
                        repo.setDestytojas(firebaseUser, destytojas).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                ApplicationData.setDestytojas(destytojas);
                                loginFragment.loginSuccess();
                                postExec();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(loginFragment.getContext(), R.string.error_someting_went_wrong, Toast.LENGTH_SHORT).show();
                                postExec();
                            }
                        });
                    }
                }
            }
        });
        return null;
    }

    private void postExec() {
        if (loginFragment.getView() != null) {
            loginFragment.getView().findViewById(R.id.login_progressBar_container).setVisibility(View.INVISIBLE);
            loginFragment.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            if (progressBar != null && progressBar.isShown()) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        if (loginFragment.getView() != null) {
//            loginFragment.getView().findViewById(R.id.login_progressBar_container).setVisibility(View.INVISIBLE);
//            loginFragment.getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//            if (progressBar != null && progressBar.isShown()) {
//                progressBar.setVisibility(View.GONE);
//            }
//        }
    }
}