package digipodium.otis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editAccount;
    private EditText editIFSC;
    private EditText editBank;
    private FirebaseFirestore db;
    private TextView tvEmail;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editName = findViewById(R.id.editName);
        tvEmail = findViewById(R.id.tvEmail);
        editAccount = findViewById(R.id.editAccount);
        editIFSC = findViewById(R.id.editIFSC);
        editBank = findViewById(R.id.editBank);

        db = FirebaseFirestore.getInstance();

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (tvEmail != null) {
            tvEmail.setText(user.getEmail());
        }
        editName.setText(user.getDisplayName());
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToCloud();
            }
        });
    }

    private void saveToCloud() {
        String account = editAccount.getText().toString();
        final String name = editName.getText().toString();
        String ifsc = editIFSC.getText().toString();
        String bank = editBank.getText().toString();


        if (account.length() > 0 && name.length() > 0 && ifsc.length() > 0 && bank.length() > 0) {
            db.collection("profile").add(new UserProfile(
                    name,
                    ifsc,
                    bank,
                    account,
                    user.getUid(),
                    user.getEmail(),
                    user.getPhoneNumber()
            )).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "success", Toast.LENGTH_SHORT).show();
                        user.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build());
                        finish();
                    } else if (task.getException() != null) {
                        Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(this, "something Fishy", Toast.LENGTH_SHORT).show();
        }
    }
}
