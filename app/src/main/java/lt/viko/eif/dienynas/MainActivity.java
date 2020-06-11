package lt.viko.eif.dienynas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

import lt.viko.eif.dienynas.utils.App;
import lt.viko.eif.dienynas.utils.ApplicationData;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private static final int OPEN_REQUEST_CODE = 41;
    private static final int STORAGE_PERMISSION_CODE = 23;

    private DestytojasViewModel destytojasViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    private Uri currentUri;

    //Kad butu galima naudoti XLS ir XLSX excel formatus
    static {
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLInputFactory",
                "com.fasterxml.aalto.stax.InputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLOutputFactory",
                "com.fasterxml.aalto.stax.OutputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLEventFactory",
                "com.fasterxml.aalto.stax.EventFactoryImpl"
        );
    }

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private FirebaseUser firebaseUser;
    private ImageView mHeaderImage;
    private MaterialTextView mHeaderName;
    private MaterialTextView mHeaderEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setContext(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //https://stackoverflow.com/questions/43707386/how-to-change-the-items-of-a-navigation-drawer-after-login

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        destytojasViewModel = new ViewModelProvider(this).get(DestytojasViewModel.class);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_login, R.id.nav_home, R.id.nav_groups)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().findItem(R.id.nav_logout).setVisible(ApplicationData.isSignedIn());
        navigationView.getMenu().findItem(R.id.nav_login).setVisible(!ApplicationData.isSignedIn());

        mHeaderImage = navigationView.getHeaderView(0).findViewById(R.id.image_nav_header);
        mHeaderEmail = navigationView.getHeaderView(0).findViewById(R.id.text_header_email);
        mHeaderName = navigationView.getHeaderView(0).findViewById(R.id.text_header_fullname);

        updateHeader();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == OPEN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (resultData != null) {
                    currentUri = resultData.getData();
                    Log.d(TAG, "onActivityResult: " + currentUri);
                    try {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            if (destytojasViewModel.addBulkStudentsFromExcel(currentUri))
                                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.adding_success, Snackbar.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, R.string.perms_grant_permission, Toast.LENGTH_LONG).show();
                        }
//                        destytojasViewModel.addBulkStudentsFromExcel(currentUri);
                    } catch (IOException | InvalidFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    if (destytojasViewModel.addBulkStudentsFromExcel(currentUri))
                        Snackbar.make(getWindow().getDecorView().getRootView(), R.string.adding_success, Snackbar.LENGTH_LONG).show();
                    else
                        Toast.makeText(this, R.string.error_someting_went_wrong, Toast.LENGTH_LONG).show();
                } catch (IOException | InvalidFormatException e) {
                    e.printStackTrace();
                }
                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.perms_granted_permission, Snackbar.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, R.string.perms_grant_permission, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onLogoutClicked(MenuItem menuItem) {
        ApplicationData.setSignedIn(false);
        FirebaseAuth.getInstance().signOut();
        menuItem.setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_login).setVisible(true);
        drawer.closeDrawer(navigationView);
        updateHeader();
        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_home);
    }

    public void updateHeader() {
        if (ApplicationData.isSignedIn()) {
            mHeaderEmail.setText(firebaseUser.getEmail());
            mHeaderName.setText(firebaseUser.getDisplayName());
            if (firebaseUser.getPhotoUrl() != null)
                Picasso.get().load(firebaseUser.getPhotoUrl()).resize(256, 256).into(mHeaderImage);
            else mHeaderImage.setImageResource(R.mipmap.ic_launcher_round);
        } else {
            mHeaderEmail.setText("");
            mHeaderName.setText(R.string.nav_header_not_logged_in);
            mHeaderImage.setImageResource(R.mipmap.ic_launcher_round);
        }
    }
}
