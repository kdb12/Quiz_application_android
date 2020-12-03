package com.example.quizapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.quizapplication.Fragments.ProfileFragment;
import com.example.quizapplication.Fragments.QuestionFragment;
import com.example.quizapplication.Fragments.QuizFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.main_activity_tool_bar);
        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigation_view);
        navigationView.bringToFront();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.ic_exit:
                        drawerLayout.closeDrawer(GravityCompat.START);
                       onBackPressed();

                        break;

                    case R.id.ic_profile:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(MainActivity.this, "profile", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment(),null).commit();
                        break;

                    case R.id.ic_quiz:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(MainActivity.this, "quiz", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new QuizFragment(),null).commit();
                        break;

                    case R.id.ic_question:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(MainActivity.this, "question", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new QuestionFragment(),null).commit();
                        break;

                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            drawerLayout.openDrawer(GravityCompat.START);
        }

        return true;
    }

    @Override
    public void onBackPressed() {



        if(getSupportFragmentManager().getBackStackEntryCount()==0) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("EXIT");
            builder.setMessage("ARE YOU SURE YOU WANT TO DO THIS?");

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }
        else
        {
            super.onBackPressed();
        }

    }

    @Override
    protected void onStart() {

        super.onStart();

        Toast.makeText(this, "application started", Toast.LENGTH_SHORT).show();






    }
}