package com.sapayth.bloodbangla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sapayth.bloodbangla.Model.Person;
import com.sapayth.bloodbangla.Utils.Util.FirebaseUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailET, mPasswordET;
    private TextView mloginMessageTV;
    private ProgressDialog mDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Logging In");
        mDialog.setCancelable(false);
        mDialog.setInverseBackgroundForced(false);

        mEmailET = findViewById(R.id.emailEditText);
        mPasswordET = findViewById(R.id.passwordEditText);
        mloginMessageTV = findViewById(R.id.loginMessageTextView);

        firebaseAuth = FirebaseAuth.getInstance();
    }


    public void loginUser(View view) {
        String email = mEmailET.getText().toString().trim();
        String password = mPasswordET.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            // email is empty
            String message = "Please enter email";
            mloginMessageTV.setText(message);
            // stopping function execution
            return;
        }

        if (TextUtils.isEmpty(password)) {
            // password is empty
            String message = "Please enter password";
            mloginMessageTV.setText(message);
            // stopping function execution
            return;
        }
        mDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    FirebaseUtils.userListRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            for (DataSnapshot postData :  dataSnapshot.getChildren()) {
                                Person person = postData.getValue(Person.class);
                                if (person.getuId().equals(userId)) {
                                    // set the userReferance as soon as user logged in
                                    FirebaseUtils.userReferance = FirebaseUtils.userListRef.child(person.getPersonKeyId());
                                    FirebaseUtils.currentPerson = person;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    mDialog.dismiss();
                    finish();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    mloginMessageTV.setText(e.getMessage());
                }
            });
    }
}
