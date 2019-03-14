package digipodium.otis;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class PhoneAuthAcitivity extends AppCompatActivity {

    private static final String TAG = PhoneAuthAcitivity.class.getName();
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mVerifyOtpCallback;
    private TextInputEditText inputNumber;
    private TextInputLayout inputWrapper;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private ViewGroup otpWrapper;
    private TextInputEditText editOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth_acitivity);

        mAuth = FirebaseAuth.getInstance();
        otpWrapper = findViewById(R.id.otpwrapper);
        editOtp = findViewById(R.id.editOtp);
        editOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 6) {
                    String code = s.toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
                    signInWithPhoneAuthCredential(credential);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputNumber = findViewById(R.id.inputNumber);
        inputWrapper = findViewById(R.id.inputWrapper);
        mVerifyOtpCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    inputWrapper.setError(e.getMessage());
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    inputWrapper.setError(e.getMessage());
                }

                updateUI(null);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                otpWrapper.animate().scaleY(1).setDuration(1400).setInterpolator(new AccelerateInterpolator()).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        editOtp.requestFocus();
                    }
                });
            }
        };

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = inputNumber.getText().toString();
                if (phoneNumber.length() > 10 && phoneNumber.length() <= 17) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,        // Phone number to verify
                            60,                 // Timeout duration
                            TimeUnit.SECONDS,   // Unit of timeout
                            PhoneAuthAcitivity.this,               // Activity (for callback binding)
                            mVerifyOtpCallback);        // OnVerificationStateChangedCallbacks
                } else {
                    inputWrapper.setError("Invalid Phone Number");
                }
            }
        });

    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {

        final FirebaseUser user = mAuth.getCurrentUser();
        user.updatePhoneNumber(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task != null) {
                    updateUI(mAuth.getCurrentUser());
                } else {
                    Toast.makeText(PhoneAuthAcitivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    inputWrapper.setError(task.getException().getMessage());
                    updateUI(null);
                }
            }
        });

    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {

        }
    }
}