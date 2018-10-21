package learing01.android.mh.learningandroidapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignIn extends AppCompatActivity {


//   public static final String TAG = SignIn.class.getSimpleName();


    EditText edtEmail, edtPassword;
    Button btnSignIn, btnSignUp;
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

//Need to add connection with Firebase SDK auth and custom user information (retrieving data from database + putting data into DB
//Need to do validation of SignUp values
//Need to do upload image + text and retrieve it from DB


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        //Init FirebaseAuth connection - Firebase SDK
        mAuth = FirebaseAuth.getInstance();

        // Java + XML connection
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.sign_in_button_active);
        btnSignUp = findViewById(R.id.sign_up_button_in_sign_in);


//        ActiveUserCheck();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(SignIn.this, MySignUp.class));
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressBar();
                SignInMethod();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null)

//        ActiveUserCheck();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void SignInMethod() {
        String username = edtEmail.getText().toString().trim();          //use as email for now
        String password = edtPassword.getText().toString().trim();


        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success.
                            mDialog.dismiss();

                            FirebaseUser userA = mAuth.getCurrentUser();
                            if (userA != null) {
                                // user logged in need to log him out or move to different activity
                                startActivity(new Intent(SignIn.this, Home.class));
                            }
                        } else {

                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void ActiveUserCheck() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            // user logged in need to log him out or move to different activity
            Toast.makeText(SignIn.this, "User Is Already Logged In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Home.class));
        }
    }

    public void ProgressBar() {
        mDialog = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();
    }

}
