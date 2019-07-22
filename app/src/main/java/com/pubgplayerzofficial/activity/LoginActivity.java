package com.pubgplayerzofficial.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.fragment.LoginFragment;
import com.pubgplayerzofficial.fragment.SignupFragment;


public class LoginActivity extends AppCompatActivity {
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        String check_username = getIntent().getStringExtra("username_key");
        if (check_username==null){
            LoginFragment loginFragment = LoginFragment.newInstance("", "");
            moveFragment(loginFragment);
        }
        else {
            SignupFragment signupFragment=SignupFragment.newInstance("", "");
            moveFragment(signupFragment);
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
//        LoginFragment loginFragment = LoginFragment.newInstance("", "");
//        moveFragment(loginFragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (key != null) {
//            finish();
//        } else {
            LoginFragment loginFragment = LoginFragment.newInstance("", "");
            moveFragment(loginFragment);
//        }
    }

    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) this).getSupportFragmentManager();
        fragmentManager.beginTransaction()
//                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
                .replace(R.id.loginactivity, fragment)
//                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
//                .addToBackStack(null)
                .commit();
    }

    public void moveFragmentAnim(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) this).getSupportFragmentManager();
        fragmentManager.beginTransaction()
//                .setCustomAnimations(R.anim.slide_out_up, R.anim.slide_in_up)
                .replace(R.id.loginactivity, fragment)
//                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
//                .addToBackStack(null)
                .commit();
    }

}
