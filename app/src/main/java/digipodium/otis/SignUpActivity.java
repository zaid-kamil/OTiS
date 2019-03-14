package digipodium.otis;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressBar loginBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        loginBar = findViewById(R.id.loginBar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBar.setVisibility(View.VISIBLE);

                EditText editEmail = findViewById(R.id.editEmail);
                EditText editUser = findViewById(R.id.editName);
                EditText editAadhar = findViewById(R.id.editAadhar);
                EditText editPass = findViewById(R.id.editPass);

                String email = editEmail.getText().toString().toLowerCase().trim();
                String password = editPass.getText().toString().toLowerCase().trim();
                final String username = editUser.getText().toString().toLowerCase().trim();
                final String aadhar = editAadhar.getText().toString().toLowerCase().trim();

                if (email.length() < 11) {
                    editEmail.setError("invalid Email");
                    return;
                }
                if (password.length() < 8) {
                    editPass.setError("invalid Password");
                    return;
                }
                if (aadhar.length() > 16 || aadhar.length()  < 16 ) {
                    editAadhar.setError("invalid Aadhar");
                    return;
                }
                if (username.length() < 3) {
                    editPass.setError("invalid username");
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

                if (email.length() > 11 && password.length() > 8) {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                saveUserDetails(user,aadhar,username);
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });
                }
            }
        });

    }

    private void saveUserDetails(final FirebaseUser user, final String aadhar, final String username) {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
        user.updateProfile(profileChangeRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SignUpActivity.this, "updated Profile", Toast.LENGTH_SHORT).show();
                FirebaseFirestore.getInstance().collection("users").add(new OtisUser(user.getUid(),aadhar,username,null,user.getEmail())).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
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

    public void close(View v){
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
