package lt.viko.eif.dienynas;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

import lt.viko.eif.dienynas.utils.App;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private static final int OPEN_REQUEST_CODE = 41;
    private static final int STORAGE_PERMISSION_CODE = 23;

    private DestytojasViewModel destytojasViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    private Uri currentUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.setContext(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        String jsonFileString = Utils.getJsonFromAssets(App.getContext(), "db.json");
//        Destytojas dest = Utils.getGsonParser().fromJson(jsonFileString, Destytojas.class);
//        StorageRepository.getInstance().setData(dest);

        destytojasViewModel = new ViewModelProvider(this).get(DestytojasViewModel.class);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login, R.id.nav_groups)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

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
                    Log.d("TAG", "onActivityResult: " + currentUri);
                    try {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            destytojasViewModel.addBulkStudentsFromExcel(currentUri);
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
                    destytojasViewModel.addBulkStudentsFromExcel(currentUri);
                    Snackbar.make(getWindow().getDecorView().getRootView(), R.string.adding_success, Snackbar.LENGTH_LONG).show();
                } catch (IOException | InvalidFormatException e) {
                    e.printStackTrace();
                }
                Snackbar.make(getWindow().getDecorView().getRootView(), R.string.perms_granted_permission, Snackbar.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, R.string.perms_grant_permission, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
