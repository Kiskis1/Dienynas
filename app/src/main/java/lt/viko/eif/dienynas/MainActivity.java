package lt.viko.eif.dienynas;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

import lt.viko.eif.dienynas.utils.App;
import lt.viko.eif.dienynas.viewmodels.DestytojasViewModel;


public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private static final int OPEN_REQUEST_CODE = 41;

    private DestytojasViewModel destytojasViewModel;
    private AppBarConfiguration mAppBarConfiguration;

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
        Log.i(TAG, "onActivityResult: ");
        Log.i(TAG, String.valueOf(requestCode));
        Log.i(TAG, String.valueOf(resultCode));
        Uri currentUri;
        if (requestCode == OPEN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (resultData != null) {
                    currentUri = resultData.getData();
                    Log.d("TAG", "onActivityResult: " + currentUri);
                    try {
                        destytojasViewModel.addBulkStudentsFromExcel(currentUri);
                    } catch (IOException | InvalidFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
