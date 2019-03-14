package digipodium.otis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressBar loginBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        loginBar = findViewById(R.id.loginBar);

        FloatingActionButton fab = findViewById(R.id.fab);
        TextView textRegister = findViewById(R.id.textRegister);
        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBar.setVisibility(View.VISIBLE);

                EditText editEmail = findViewById(R.id.editEmail);
                TextInputEditText editPass = findViewById(R.id.editPass);
                TextInputLayout editPassWrapper = findViewById(R.id.editPassWrapper);

                String email = editEmail.getText().toString().toLowerCase().trim();
                String password = editPass.getText().toString().toLowerCase().trim();

                if (email.length() < 11) {
                    editEmail.setError("invalid Email");
                    return;
                }
                if (password.length() < 8) {
                    editPassWrapper.setError("invalid Password");
                    return;
                }
                if(!email.contains("@")){
                    editEmail.setError("invalid Email, must contain @");
                    return;
                }

                if(!email.endsWith(".com") && !email.endsWith(".in")){
                    editEmail.setError("invalid Email, must end with .com or .in");
                    return;
                }

                if (email.length() >= 11 && password.length() >= 8) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            loginBar.setVisibility(View.GONE);
            startActivity(new Intent(this, FingerAuthActivity.class));
            finish();
        }
    }

}
