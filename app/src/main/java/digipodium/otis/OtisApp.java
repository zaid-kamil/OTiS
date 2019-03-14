package digipodium.otis;

import android.app.Application;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

public class OtisApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());

    }
}
