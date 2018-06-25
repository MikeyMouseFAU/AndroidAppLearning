package learing01.android.mh.learningandroidapps;

import android.app.ProgressDialog;
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

import learing01.android.mh.learningandroidapps.Model.User;

public class MySignUp extends AppCompatActivity {

    public static final String TAG = MySignUp.class.getSimpleName();

    EditText edtPhone, edtPassword, edtName;
    Button btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sign_up);


        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);             // Java + XML connection
        edtPassword = findViewById(R.id.edtPassword);
        btnSignUp = findViewById(R.id.sign_up_button);

        //Init Firebase connection

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog mDialog = new ProgressDialog(MySignUp.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();


                table_user.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // check if user exist in database

                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            mDialog.dismiss();
                            Toast.makeText(MySignUp.this, "Username already exist!!!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else

                        {
                            mDialog.dismiss();
                            User user = new User(edtName.getText().toString(),edtPassword.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            Toast.makeText(MySignUp.this, "Sign Up successfully!", Toast.LENGTH_SHORT).show();
                            finish();
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
