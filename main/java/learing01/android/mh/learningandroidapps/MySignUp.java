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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import learing01.android.mh.learningandroidapps.Model.User;

public class MySignUp extends AppCompatActivity {

//    public static final String TAG = MySignUp.class.getSimpleName();

    //Variables are declared here
    EditText edtPhone, edtPassword, edtName, edtEmail;
    Button btnSignUp, btnSignInInactive;

    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;
    public DatabaseReference table_user;

    public void createNewUser(final FirebaseDatabase database) {

        final String name = edtName.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        final String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        //This method is from Firebase library create user on Firebase
        //Had issues with it due to Google Play Services
        //Needed to uninstall and update Google Play to get to work


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {


// save the user's profile into Firebase so we can list users,
// use them in Security and Firebase Rules, and show profiles


                            //Database is passed as an argument by value (made final)
                            table_user = database.getReference("user");     //User -> Message testing

                            FirebaseUser currentUser = mAuth.getCurrentUser();

                            if (currentUser != null) {

                                String uid = currentUser.getUid();


                                // CREATED OBJECT FILLS IN ALL THE DATA Inputted by The User
                                User userValue = new User(name, phone, email);        //argument defined and declared above line 40-43
                                // Send and Writes data into Firebase DB  -> due to rule it might need UserUID
                                table_user.child(uid).setValue(userValue);

                                Toast.makeText(getApplicationContext(), "" + name + phone + email, Toast.LENGTH_LONG).show();
                                mDialog.dismiss();
                            }

                            // Set Current User Back To Null (null)
                            // This

                            //Message to the User  - SUCCESS
                            Toast.makeText(getApplicationContext(), "User Successfully Registered", Toast.LENGTH_LONG).show();

                            //Moves to Main Activity


                        } else if (!task.isSuccessful()) {
                            mDialog.dismiss();
                            Toast.makeText(MySignUp.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                        }

                        //   FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(MySignUp.this, MainActivity.class));

                    }

                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sign_up);

        mAuth = FirebaseAuth.getInstance();

        // Java + XML connection UI elements are linked with functionality code
        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);

        btnSignUp = findViewById(R.id.sign_up_button_active);
        btnSignInInactive = findViewById(R.id.sign_in_button_in_sign_up);

//        ActiveUserCheck();

        //Initiates Firebase connection
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        //On Click Listener Waits for User to Click the Button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Function Is Called
                ProgressBar();
                createNewUser(database);
            }
        });

        btnSignInInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Function Is Called
                startActivity(new Intent(MySignUp.this, SignIn.class));
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

    public void ActiveUserCheck() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Toast.makeText(MySignUp.this, "User Is Already Logged In", Toast.LENGTH_SHORT).show();  // user logged in need to log him out or move to different activity

            startActivity(new Intent(this, Home.class));
        }
    }

    public void ProgressBar() {
        mDialog = new ProgressDialog(MySignUp.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();
    }
}
