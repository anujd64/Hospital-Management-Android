package com.example.hospital_management.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.hospital_management.R;
import com.example.hospital_management.admin.fragments.AdminManageFragment;
import com.example.hospital_management.admin.fragments.AdminProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class loginAdmin extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);
        replaceFragment(new AdminManageFragment());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_admin);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuItemId =  item.getItemId();

                if (menuItemId ==  R.id.admin_manage) {
                    replaceFragment(new AdminManageFragment());
                    return true;
                }
                else if (menuItemId ==  R.id.admin_profile) {
                    replaceFragment(new AdminProfileFragment());
                    return true;
                }


                return true;
            }
        });


    }

    private  void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}