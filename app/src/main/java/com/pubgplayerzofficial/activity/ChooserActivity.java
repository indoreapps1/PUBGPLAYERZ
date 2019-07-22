package com.pubgplayerzofficial.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.fragment.SignupFragment;

public class ChooserActivity extends AppCompatActivity {

    Button btn_create_acc, btn_signin;
    SignupFragment signupFragment;

    //    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chooser);
        btn_create_acc = findViewById(R.id.btn_create_acc);
        btn_signin = findViewById(R.id.btn_signin);
        signupFragment = new SignupFragment();
        btn_create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooserActivity.this, LoginActivity.class);
                intent.putExtra("username_key", "username");
                startActivity(intent);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooserActivity.this, LoginActivity.class));
            }
        });
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

}
