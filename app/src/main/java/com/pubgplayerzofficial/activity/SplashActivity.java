package com.pubgplayerzofficial.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.Result;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends AppCompatActivity {
    List<Result> resultList;
    String versionOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ImageView imageView = findViewById(R.id.imageView);
//        Glide.with(this).load(R.drawable.matchpage).apply(RequestOptions.circleCropTransform()).into(imageView);
        checkForceUpdate();
    }


    public void checkForceUpdate() {
        resultList = new ArrayList<>();
        ServiceCaller serviceCaller = new ServiceCaller(this);
        serviceCaller.callupdateService(new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(final String workName, boolean isComplete) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        makeData(workName);
                    }
                }, 0);
            }
        });

    }

    private void makeData(String workName) {
        resultList.clear();
//        Toast.makeText(this, workName, Toast.LENGTH_SHORT).show();
//        if (!workName.equalsIgnoreCase("no")) {
//            ContentData contentData = new Gson().fromJson(workName, ContentData.class);
//            for (Result result : contentData.getResult()) {
//                resultList.addAll(Arrays.asList(result));
//            }
//        }
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionOld = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        if (!resultList.get(resultList.size() - 1).getVersion().equalsIgnoreCase(versionOld)) {
//            Intent intent = new Intent(SplashActivity.this, UpdateActivity.class);
//            intent.putExtra("data", resultList.get(resultList.size() - 1).getUrl());
//            intent.putExtra("des", resultList.get(resultList.size() - 1).getDescription());
//            intent.putExtra("version", resultList.get(resultList.size() - 1).getVersion());
//            intent.putExtra("date", resultList.get(resultList.size() - 1).getDate());
//            startActivity(intent);
//            finish();
//        } else {
        DbHelper dbHelper = new DbHelper(SplashActivity.this);
        Result result = dbHelper.getUserData();
        if (result != null) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, ChooserActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
