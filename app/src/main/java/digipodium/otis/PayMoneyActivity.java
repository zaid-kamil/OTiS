package digipodium.otis;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PayMoneyActivity extends AppCompatActivity {

    public static final String TRANSACTIONS = "transactions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_money);

        Intent intent = getIntent();
        if (intent.getExtras() == null) {
            finish();
        }
        String name = intent.getStringExtra("personname");
        TextView textPersonName = findViewById(R.id.textPersonName);
        textPersonName.setText(name);
        Button btnPay = findViewById(R.id.btnPay);
        final EditText editAmount = findViewById(R.id.editAmount);
        final EditText editBank = findViewById(R.id.editBank);
        final EditText editIFSC = findViewById(R.id.editIFSC);
        final EditText editAccountNo = findViewById(R.id.editAccountNo);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String senderName = auth.getCurrentUser().getDisplayName();

        final String uid = auth.getCurrentUser().getUid();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String amt = editAmount.getText().toString();
                String bank = editBank.getText().toString();
                String ifsc = editIFSC.getText().toString();
                final String account = editAccountNo.getText().toString();

                db.collection(TRANSACTIONS).add(new PayTransaction(
                        senderName,
                        amt,
                        bank,
                        ifsc,
                        account,
                        uid
                )).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(PayMoneyActivity.this, PaymentFingerprintAcitivity.class);
                            i.putExtra("name", account);
                            i.putExtra("amount", amt);
                            startActivity(i);
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(PayMoneyActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                        editAccountNo.setText("");
                        editIFSC.setText("");
                        editBank.setText("");
                        editAmount.setText("");
                    }
                });


            }
        });

    }
}
