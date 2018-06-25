package learing01.android.mh.learningandroidapps;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import learing01.android.mh.learningandroidapps.Common.Common;
import learing01.android.mh.learningandroidapps.Model.User;

public class SignIn extends AppCompatActivity {


    //public static final String TAG = SignIn.class.getSimpleName();

    EditText edtPhone, edtPassword;
    Button btnSignIn;

    //31min video

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        edtPhone = findViewById(R.id.edtPhone);             // Java + XML connection
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.sign_in_button);

        //Init Firebase connection

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();


                table_user.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // check if user exist in database

                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {

                            mDialog.dismiss();
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                //get password for a user from database compare to one user entered
                                //if same/equal then get message

                                {
                                    Intent Home = new Intent(SignIn.this,Home.class);
                                    Common.currentUser = user;
                                    startActivity(Home);
                                    finish();
                                }

                                //Toast.makeText(SignIn.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                Toast.makeText(SignIn.this, "Invalid password!!!", Toast.LENGTH_SHORT).show();
                            }
                        } else

                        {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "Username doesn't exist!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        //Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }
        });


    }
}
