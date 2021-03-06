package lt.viko.eif.dienynas.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import lt.viko.eif.dienynas.R;
import lt.viko.eif.dienynas.tasks.LoginTask;
import lt.viko.eif.dienynas.utils.ApplicationData;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;

import static android.app.Activity.RESULT_OK;

public class LoginFragment extends Fragment {
    private final static String TAG = LoginFragment.class.getSimpleName();

    private static final int RC_SIGN_IN = 123;

    private ProgressBar mProgressBar;
    private LoginFragment loginFragment;

    private FirebaseUser firebaseUser;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginFragment = this;
        DestytojasViewModel destytojasViewModel = new ViewModelProvider(this).get(DestytojasViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = view.findViewById(R.id.login_progressBar);

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    // [START auth_fui_result]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseUser = firebaseAuth.getCurrentUser();
                new LoginTask(loginFragment, mProgressBar, firebaseUser).execute();
                //Navigation.findNavController(getView()).navigate(R.id.action_nav_login_to_nav_home);

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Navigation.findNavController(getView()).navigate(R.id.action_nav_login_to_nav_home);
                Toast.makeText(getContext(), R.string.error_someting_went_wrong, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void loginSuccess() {
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        MenuItem logout = navigationView.getMenu().findItem(R.id.nav_logout);
        MenuItem login = navigationView.getMenu().findItem(R.id.nav_login);
        login.setVisible(false);
        logout.setVisible(true);
        ApplicationData.setSignedIn(true);

        ImageView mHeaderImage = navigationView.getHeaderView(0).findViewById(R.id.image_nav_header);
        MaterialTextView mHeaderEmail = navigationView.getHeaderView(0).findViewById(R.id.text_header_email);
        MaterialTextView mHeaderName = navigationView.getHeaderView(0).findViewById(R.id.text_header_fullname);

        mHeaderEmail.setText(firebaseUser.getEmail());
        mHeaderName.setText(firebaseUser.getDisplayName());
        if (firebaseUser.getPhotoUrl() != null)
            Picasso.get().load(firebaseUser.getPhotoUrl()).resize(256, 256).into(mHeaderImage);
        else mHeaderImage.setImageResource(R.mipmap.ic_launcher_round);

        Navigation.findNavController(getView()).navigate(R.id.action_nav_login_to_nav_home);
    }

    private void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(getView().getContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_signout]
    }
}
