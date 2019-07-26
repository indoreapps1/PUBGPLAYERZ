package com.pubgplayerzofficial.activity;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.firebase.Config;
import com.pubgplayerzofficial.firebase.NotificationUtils;
import com.pubgplayerzofficial.fragment.HelpFragment;
import com.pubgplayerzofficial.fragment.MeFragment;
import com.pubgplayerzofficial.fragment.OnGoingFragment;
import com.pubgplayerzofficial.fragment.ParticipatedFragment;
import com.pubgplayerzofficial.fragment.PlayFragment;
import com.pubgplayerzofficial.fragment.ResultFragment;
import com.pubgplayerzofficial.fragment.WalletFragment;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.ContentData;
import com.pubgplayerzofficial.model.Result;
import com.pubgplayerzofficial.utilities.GooglePlayStoreAppVersionNameLoader;
import com.pubgplayerzofficial.utilities.NetworkChecker;
import com.pubgplayerzofficial.utilities.Utility;
import com.pubgplayerzofficial.utilities.WSCallerVersionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity implements WSCallerVersionListener {
    boolean isForceUpdate = true;
    private ActionBar actionBar;
    public TextView tv_title, tv_amount, tv_help;
    private BottomNavigationView navigation;
    private LinearLayout layout;
    private BroadcastReceiver mNetworkReceiver;
    DbHelper dbHelper;
    String versionOld;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_participated:
                    ParticipatedFragment participatedFragment = ParticipatedFragment.newInstance("", "");
                    moveFragment(participatedFragment);
                    setTitle("JOINED");
                    return true;
                case R.id.navigation_ongoing:
                    OnGoingFragment onGoingFragment = OnGoingFragment.newInstance("", "");
                    moveFragment(onGoingFragment);
                    setTitle("LIVE");
                    return true;
                case R.id.navigation_play:
                    PlayFragment playFragment = PlayFragment.newInstance("", "");
                    moveFragment(playFragment);
                    setTitle("PUBGPLAYERZ");
                    return true;
                case R.id.navigation_result:
                    ResultFragment resultFragment = ResultFragment.newInstance("", "");
                    moveFragment(resultFragment);
                    setTitle("RESULT");
                    return true;
                case R.id.navigation_me:
                    MeFragment meFragment = MeFragment.newInstance("", "");
                    moveFragment(meFragment);
                    setTitle("ME");
                    return true;
            }
            return false;
        }
    };
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onGetResponse(boolean isUpdateAvailable) {
        Log.e("ResultAPPMAIN", String.valueOf(isUpdateAvailable));
        if (isUpdateAvailable) {
            showUpdateDialog();
        }
    }

    /**
     * Method to show update dialog
     */
    public void showUpdateDialog() {
        if (Utility.isOnline(MainActivity.this)) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle(MainActivity.this.getString(R.string.app_name));
            alertDialogBuilder.setMessage("Your Application update is available!");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    MainActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                    dialog.cancel();
                }
            });
//        alertDialogBuilder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (isForceUpdate) {
//                    finish();
//                }
//                dialog.dismiss();
//            }
//        });
            alertDialogBuilder.show();
            alertDialogBuilder.setCancelable(false);
        } else {
            Toasty.error(MainActivity.this, "No Internet Connection").show();
        }
    }

    private long back_pressed;

    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) this).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frgcontainer, fragment, "main")
//                .addToBackStack(null)
                .commit();
    }

    public void moveFragmentW(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) this).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frgcontainer, fragment)
//                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DbHelper(this);
        actionBar = getSupportActionBar();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        layout = (LinearLayout) findViewById(R.id.layout);
        tv_help = findViewById(R.id.tv_help);
        tv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mNetworkReceiver = new NetworkChecker();
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        getUserProfile();
        setUpDashboardFrg();
        checkForceUpdate();
        new GooglePlayStoreAppVersionNameLoader(MainActivity.this, MainActivity.this).execute();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    Intent intents = new Intent(MainActivity.this, MainActivity.class);
                    intents.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1410,
                            intents, PendingIntent.FLAG_ONE_SHOT);
                    String message = intent.getStringExtra("message");
                    NotificationCompat.Builder notificationBuilder = new
                            NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle("Message")
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager)
                                    getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify(1410, notificationBuilder.build());
//                    Toast.makeText(getApplicationContext(), "Notification: " + message, Toast.LENGTH_LONG).show();

                }
            }
        };

    }

    private void showPopup() {
        PopupMenu popup = new PopupMenu(this, tv_help);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menuhelp, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_refer) {
                    HelpFragment myReferalFragment = HelpFragment.newInstance("", "");
                    moveFragment(myReferalFragment);
                }
                return true;
            }
        });

        popup.show();//showing popup menu
    }

    public void getUserProfile() {
        Result result = dbHelper.getUserData();
        ServiceCaller serviceCaller = new ServiceCaller(this);
        serviceCaller.callUserProfileService(result.getId(), new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
//                    Toast.makeText(MainActivity.this, workName, Toast.LENGTH_SHORT).show();
                    if (!workName.trim().equalsIgnoreCase("no")) {
                        ContentData contentData = new Gson().fromJson(workName, ContentData.class);
                        dbHelper.deleteUserData();
                        for (Result result : contentData.getResult()) {
                            dbHelper.upsertUserData(result);
                        }
                    }
                }
                DbHelper dbHelper = new DbHelper(MainActivity.this);
                final Result result = dbHelper.getUserData();
                if (result != null) {
                    tv_amount.setText("\u20B9 " + result.getWallet_amount());
                }
            }
        });

        serviceCaller.callCheckSessionService(result.getId(), new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    ContentData contentData = new Gson().fromJson(workName, ContentData.class);
                    for (Result result : contentData.getResult()) {
                        if (result.getStatus() == 0) {
                            dbHelper.deleteUserData();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkReceiver);
    }


    private void setUpDashboardFrg() {
        PlayFragment playFragment = PlayFragment.newInstance("", "");
        moveFragment(playFragment);
//        View view = LayoutInflater.from(this).inflate(R.layout.custom_actionbar, null);
        ImageView imageView = findViewById(R.id.image);
        Glide.with(this).load(R.drawable.logo).apply(RequestOptions.circleCropTransform()).into(imageView);
        tv_amount = findViewById(R.id.tv_amount);
        tv_title = findViewById(R.id.tv_title);
        setTitle("PUBGPLAYERZ");
        navigation.setSelectedItemId(R.id.navigation_play);
        DbHelper dbHelper = new DbHelper(this);
        final Result result = dbHelper.getUserData();
//        if (result != null) {
//            tv_amount.setText("\u20B9" + result.getWallet_amount());
//        }
        tv_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserProfile();
                WalletFragment walletFragment = WalletFragment.newInstance(true, result.getWallet_amount());
                moveFragmentW(walletFragment);
            }
        });
    }

    public void hideShowNav(boolean flag) {
        if (flag) {
            navigation.setVisibility(View.INVISIBLE);
        } else {
            navigation.setVisibility(View.VISIBLE);
        }
    }


    public void hideActionBar(boolean flag) {
        if (flag) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
        }
    }

    public void setTitle(String s) {
        tv_title.setText(s);
    }


    public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

        private int height;

        @Override
        public boolean onLayoutChild(CoordinatorLayout parent, BottomNavigationView child, int layoutDirection) {
            height = child.getHeight();
            return super.onLayoutChild(parent, child, layoutDirection);
        }

        @Override
        public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                           BottomNavigationView child, @NonNull
                                                   View directTargetChild, @NonNull View target,
                                           int axes, int type) {
            return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
        }

        @Override
        public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child,
                                   @NonNull View target, int dxConsumed, int dyConsumed,
                                   int dxUnconsumed, int dyUnconsumed,
                                   @ViewCompat.NestedScrollType int type) {
            if (dyConsumed > 0) {
                slideDown(child);
            } else if (dyConsumed < 0) {
                slideUp(child);
            }
        }

        private void slideUp(BottomNavigationView child) {
            child.clearAnimation();
            child.animate().translationY(0).setDuration(250);
        }

        private void slideDown(BottomNavigationView child) {
            child.clearAnimation();
            child.animate().translationY(height).setDuration(250);
        }
    }

    @Override
    public void onBackPressed() {
        final Fragment fragment = getSupportFragmentManager().findFragmentByTag("ongoing");
        final Fragment fragmentplay = getSupportFragmentManager().findFragmentByTag("play");
        final Fragment fragmentresult = getSupportFragmentManager().findFragmentByTag("result");
        final Fragment fragmentparti = getSupportFragmentManager().findFragmentByTag("parti");
        final Fragment fragmentme = getSupportFragmentManager().findFragmentByTag("me");
        final Fragment fragmentmain = getSupportFragmentManager().findFragmentByTag("main");


        if (fragment != null) {
            openMain();
//            OnGoingFragment onGoingFragment = OnGoingFragment.newInstance("", "");
//            moveFragment(onGoingFragment);
            ((BottomNavigationView) this.findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_ongoing);
//            hideShowNav(false);
        }
//
        if (fragmentplay != null) {
            openMain();
//            PlayFragment onGoingFragment = PlayFragment.newInstance("", "");
//            moveFragment(onGoingFragment);
            ((BottomNavigationView) this.findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_play);
//            hideShowNav(false);
//
        }
        if (fragmentresult != null) {
            openMain();
//            ResultFragment onGoingFragment = ResultFragment.newInstance("", "");
//            moveFragment(onGoingFragment);
            ((BottomNavigationView) this.findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_result);
//            hideShowNav(false);
        }

        if (fragmentparti != null) {
            openMain();
//            ParticipatedFragment onGoingFragment = ParticipatedFragment.newInstance("", "");
//            moveFragment(onGoingFragment);
            ((BottomNavigationView) this.findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_participated);
//            hideShowNav(false);
        }
        if (fragmentme != null) {
            openMain();
//            MeFragment meFragment = MeFragment.newInstance("", "");
//            moveFragment(meFragment);
            ((BottomNavigationView) this.findViewById(R.id.navigation)).setSelectedItemId(R.id.navigation_me);
//            hideShowNav(false);
        }
        if (back_pressed + 3000 > System.currentTimeMillis()) {
//            super.onBackPressed();
            backAlert();
        } else {
//            hideShowNav(false);
//            Toast.makeText(getBaseContext(),
//                    "Press once again to exit!", Toast.LENGTH_SHORT)
//                    .show();
        }
        back_pressed = System.currentTimeMillis();

    }

    private void openMain() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
    }

    private void backAlert() {
        new AlertDialog.Builder(this)
                .setMessage("Are You sure you want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    public void checkForceUpdate() {

        final List<Result> resultList = new ArrayList<>();
        if (Utility.isOnline(MainActivity.this)) {
            ServiceCaller serviceCaller = new ServiceCaller(this);
            serviceCaller.callupdateService(new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (!workName.trim().equalsIgnoreCase("no")) {
                        ContentData contentData = new Gson().fromJson(workName, ContentData.class);
                        for (Result result : contentData.getResult()) {
                            resultList.addAll(Arrays.asList(result));
                        }
                        try {
                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            versionOld = pInfo.versionName;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (Float.parseFloat(resultList.get(resultList.size() - 1).getVersion()) > Float.parseFloat(versionOld)) {
                            showUpdateDialog(resultList.get(resultList.size() - 1).getUrl());
                        }
                    }
                }
            });
        }
    }

    private void showUpdateDialog(final String s) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alert = builder.create();
        View view = alert.getLayoutInflater().inflate(R.layout.custom_update_alert, null);
        TextView title = view.findViewById(R.id.textMessage);
        TextView title2 = view.findViewById(R.id.textMessage2);
        Button ok = view.findViewById(R.id.buttonUpdate);
        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        alert.setCustomTitle(view);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(s));
                startActivity(intent);
//                alert.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                //MoveNextScreen();
            }
        });
        alert.show();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
    }
}
