package learing01.android.mh.learningandroidapps;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import learing01.android.mh.learningandroidapps.Common.Common;
import learing01.android.mh.learningandroidapps.Interface.ItemClickListener;
import learing01.android.mh.learningandroidapps.Model.Category;
import learing01.android.mh.learningandroidapps.Model.User;
import learing01.android.mh.learningandroidapps.ViewHolder.MenuViewHolder;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txtFullName;                           //This is used get text from database....
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private StorageReference mStorageRef;

    FirebaseDatabase database;
    DatabaseReference category, userInfo;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Menu");   //Toolbar change name/title
        mAuth = FirebaseAuth.getInstance();

        //Below line of code creates an error
        //mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://appdevelopmentpoczatek.appspot.com/home/Music-Spring-2.jpg");


        ///Firebase Database Connection
        database = FirebaseDatabase.getInstance();

        //Need to change Category to category to keep keep it easier to use
        category = database.getReference("Category");
        userInfo = database.getReference("user");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Under Development", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set name for user
        View headerView = navigationView.getHeaderView(0);

        //Below is comment out as it supposed to get info from DB need to connect it again
        txtFullName = headerView.findViewById(R.id.txtFullName);


        // Read from the database
        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String uid = currentUser.getUid();

                //Sets values from Database for object user
                User user = dataSnapshot.child(uid).getValue(User.class);

                //Sets Text that is from DataBase from the database after user is logged in
                txtFullName.setText(user.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

//        FirebaseUser currentUser = mAuth.getCurrentUser();


        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();

        NoActiveUserCheck();

    }


    @Override
    protected void onStart() {
        super.onStart();
        NoActiveUserCheck();
    }

    private void NoActiveUserCheck() {

        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            //User NOT logged in need to log him IN or move to different activity
//            Toast.makeText(Home.this, "User Is Not Logged In", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void loadMenu() {

        //This is connecting Database values with Layout
        FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, R.layout.menu_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                //      Category.class,R.id.menu_image,MenuViewHolder.class,category
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);
                final Category clickItem = model;

                viewHolder.setItemClickListener(new ItemClickListener() {

                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(Home.this, "" + clickItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        };


        // Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));

        // StorageReference riversRef = storageRef.child("images/rivers.jpg");


//// Create a storage reference from our app
//        StorageReference storageRef = storage.getReference();
//
//// Create a reference with an initial file path and name
//        StorageReference pathReference = storageRef.child("images/stars.jpg");
//
//// Create a reference to a file from a Google Cloud Storage URI
//        StorageReference gsReference = storage.getReferenceFromUrl("gs://bucket/images/stars.jpg");
//
//// Create a reference from an HTTPS URL
//// Note that in the URL, characters are URL escaped!
//        StorageReference httpsReference = storage.getReferenceFromUrl("https://firebasestorage.googleapis.com/b/bucket/o/images%20stars.jpg");


        recycler_menu.setAdapter(adapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!drawer.isDrawerOpen(GravityCompat.START)) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /// Theses guys are located in XML under menu / activity_home_drawer.xml
        if (id == R.id.nav_music_menu) {

//            Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
//
//            mStorageRef.child("home/rivers.jpg");
//
//
//            File localFile = null;
//            try {
//                localFile = File.createTempFile("images", "jpg");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            mStorageRef.getFile(localFile)
//                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                            // Successfully downloaded data to local file
//                            // ...
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle failed download
//                    // ...
//                }
//            });


            //Test git

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, MusicMenu.class));
            // Handle the camera action
        } else if (id == R.id.nav_video_menu) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_gallery_menu) {

        } else if (id == R.id.nav_log_out) {

            //To lines of code below are not needed as return true closes the drawer
            //  DrawerLayout drawer = findViewById(R.id.drawer_layout);
            //   drawer.closeDrawer(GravityCompat.START);
            FirebaseAuth.getInstance().signOut();
            NoActiveUserCheck();
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}