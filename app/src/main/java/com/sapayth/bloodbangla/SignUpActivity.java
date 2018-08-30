package com.sapayth.bloodbangla;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sapayth.bloodbangla.Model.Person;
import com.sapayth.bloodbangla.Utils.Util.FirebaseUtils;

public class SignUpActivity extends AppCompatActivity {

    private EditText mNameET, mEmailET, mPasswordET;
    private ProgressDialog mDialog;
    private Button mRegistrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading");
        mDialog.setCancelable(false);
        mDialog.setInverseBackgroundForced(false);

        mNameET = findViewById(R.id.nameEditText);
        mEmailET = findViewById(R.id.emailEditText);
        mPasswordET = findViewById(R.id.passwordEditText);
        mRegistrationButton = findViewById(R.id.registrationButton);

    }

    public void registerUser(View view) {
        final String name = mNameET.getText().toString().trim();
        final String email = mEmailET.getText().toString().trim();
        String password = mPasswordET.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            // username is empty
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT).show();
            // stopping function execution
            return;
        }

        if (TextUtils.isEmpty(email)) {
            // email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            // stopping function execution
            return;
        }

        if (TextUtils.isEmpty(password)) {
            // password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            // stopping function execution
            return;
        }

        mDialog.show();

        FirebaseUtils.firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String keyId = FirebaseUtils.userListRef.push().getKey();
                    String uId = currentUser.getUid();
                    Person person = new Person(name, keyId, uId);
                    FirebaseUtils.currentPerson = person;
                    FirebaseUtils.userListRef.child(keyId).setValue(person);
                    FirebaseUtils.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    goToUserProfile(person);
                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goToUserProfile(Person person) {
        Intent intent = new Intent(SignUpActivity.this, UserProfileActivity.class);
        intent.putExtra("Person", person);
        startActivity(intent);
        finish();
    }
}