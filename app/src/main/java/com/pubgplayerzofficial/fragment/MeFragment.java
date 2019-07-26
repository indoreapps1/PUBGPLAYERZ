package com.pubgplayerzofficial.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.activity.LoginActivity;
import com.pubgplayerzofficial.activity.MainActivity;
import com.pubgplayerzofficial.activity.NewsActivity;
import com.pubgplayerzofficial.activity.SplashActivity;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.ContentData;
import com.pubgplayerzofficial.model.Result;


public class MeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MeFragment newInstance(String param1, String param2) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private View view;
    private Context context;
    private TextView tv_amount, tv_kill, tv_won, tv_username, tv_email, tv_total_match;
    private NestedScrollView nestedscroll;
    private CardView card_earn, card_report, card_wallet, card_rcp, card_pp, card_profile, card_Statistics, card_toplayer, card_aboutus, card_support, card_tc, card_share, card_logout, card_news;
    private LinearLayout layout_won, layout_totalKill, layout_matchPlayer;
    private DbHelper dbHelper;
    private Result result;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_me, container, false);
        init();
        return view;
    }

    private void init() {
        ((MainActivity) context).hideActionBar(true);
        ((MainActivity) context).hideShowNav(false);
        ((MainActivity) context).getUserProfile();
        tv_amount = view.findViewById(R.id.tv_amount);
        tv_kill = view.findViewById(R.id.tv_kill);
        tv_won = view.findViewById(R.id.tv_won);
        tv_username = view.findViewById(R.id.tv_username);
        tv_email = view.findViewById(R.id.tv_email);
        tv_total_match = view.findViewById(R.id.tv_total_match);
        card_earn = view.findViewById(R.id.card_earn);
        card_wallet = view.findViewById(R.id.card_wallet);
        card_profile = view.findViewById(R.id.card_profile);
        card_Statistics = view.findViewById(R.id.card_Statistics);
        card_toplayer = view.findViewById(R.id.card_toplayer);
        card_aboutus = view.findViewById(R.id.card_aboutus);
        card_support = view.findViewById(R.id.card_support);
        card_tc = view.findViewById(R.id.card_tc);
        card_share = view.findViewById(R.id.card_share);
        card_logout = view.findViewById(R.id.card_logout);
        card_report = view.findViewById(R.id.card_report);
        card_rcp = view.findViewById(R.id.card_rcp);
        card_pp = view.findViewById(R.id.card_pp);
        card_news = view.findViewById(R.id.card_news);
        layout_won = view.findViewById(R.id.layout_won);
        layout_totalKill = view.findViewById(R.id.layout_totalKill);
        layout_matchPlayer = view.findViewById(R.id.layout_matchPlayer);
        imageView = view.findViewById(R.id.imageView);
        card_earn.setOnClickListener(this);
        card_wallet.setOnClickListener(this);
        card_profile.setOnClickListener(this);
        card_Statistics.setOnClickListener(this);
        card_toplayer.setOnClickListener(this);
        card_aboutus.setOnClickListener(this);
        card_support.setOnClickListener(this);
        card_tc.setOnClickListener(this);
        card_share.setOnClickListener(this);
        card_logout.setOnClickListener(this);
        layout_won.setOnClickListener(this);
        layout_matchPlayer.setOnClickListener(this);
        layout_totalKill.setOnClickListener(this);
        card_report.setOnClickListener(this);
        card_rcp.setOnClickListener(this);
        card_pp.setOnClickListener(this);
        card_news.setOnClickListener(this);
        nestedscroll = view.findViewById(R.id.nestedscroll);
        setvalue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedscroll.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                        ((MainActivity) getActivity()).hideShowNav(true);
                    }
                    if (scrollY < oldScrollY) {
                        ((MainActivity) getActivity()).hideShowNav(false);
                    }
                }
            });
        } else {
            ((MainActivity) getActivity()).hideShowNav(true);
        }

    }

    public void setvalue() {
        dbHelper = new DbHelper(context);
        result = dbHelper.getUserData();
        checkSessionData(result.getId());
        tv_amount.setText("\u20B9 " + (result.getWallet_amount() + ""));
        tv_kill.setText("" + result.getTotal_kill());
        tv_won.setText("" + result.getWinning_amount());
        tv_username.setText(result.getFull_name());
        tv_email.setText(result.getEmail());
        tv_total_match.setText("" + result.getTotal_match());
        byte[] decodedString = Base64.decode(result.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//        if (result.getImage().equalsIgnoreCase("")) {
//            Glide.with(context).load(R.drawable.userprofile).apply(RequestOptions.circleCropTransform()).into(imageView);
//        } else {
//            Glide.with(context).load(decodedByte).apply(RequestOptions.circleCropTransform()).into(imageView);
//        }
    }

    private void checkSessionData(int id) {
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callCheckSessionService(id, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    ContentData contentData = new Gson().fromJson(workName, ContentData.class);
                    for (Result result : contentData.getResult()) {
                        if (result.getStatus() == 0) {
                            dbHelper.deleteUserData();
                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_earn:
                ReferEarnFragment referEarnFragment = ReferEarnFragment.newInstance(result.getUser_name(), "");
                moveFragment(referEarnFragment);
                break;
            case R.id.card_wallet:
                WalletFragment walletFragment = WalletFragment.newInstance(false, 0);
                moveFragment(walletFragment);
                break;
            case R.id.card_profile:
                PorfileFragment porfileFragment = PorfileFragment.newInstance("", "");
                moveFragment(porfileFragment);
                break;
            case R.id.card_Statistics:
                MyStatisticsFragment myStatisticsFragment = MyStatisticsFragment.newInstance("", "");
                moveFragment(myStatisticsFragment);
                break;
            case R.id.card_toplayer:
                TopPlayerFragment topPlayerFragment = TopPlayerFragment.newInstance("", "");
                moveFragment(topPlayerFragment);
                break;
            case R.id.card_share:
                shareData();
                break;
            case R.id.card_logout:
                Intent intent = new Intent(context, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("key", "me");
                dbHelper.deleteUserData();
                dbHelper.deletePlayData();
                dbHelper.deleteparticipatedData();
                dbHelper.deletespecialData();
                startActivity(intent);
                break;
            case R.id.layout_won:
                MyStatisticsFragment layout_won = MyStatisticsFragment.newInstance("", "");
                moveFragment(layout_won);
                break;
            case R.id.layout_totalKill:
                MyStatisticsFragment layout_totalKill = MyStatisticsFragment.newInstance("", "");
                moveFragment(layout_totalKill);
                break;
            case R.id.layout_matchPlayer:
                MyStatisticsFragment layout_matchPlayer = MyStatisticsFragment.newInstance("", "");
                moveFragment(layout_matchPlayer);
                break;

            case R.id.card_tc:
                Intent c = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pubgplayerz.com/terms-and-conditions.php"));
                if (c.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(c);
                }

                break;
            case R.id.card_aboutus:
                Intent intenta = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pubgplayerz.com/about.php"));
                if (intenta.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intenta);
                }

                break;

            case R.id.card_support:
                Intent intents = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pubgplayerz.com/contact.php"));
                if (intents.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intents);
                }

                break;
            case R.id.card_rcp:
                Intent rcp = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pubgplayerz.com/refund-and-cancellation.php"));
                if (rcp.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(rcp);
                }

                break;
            case R.id.card_pp:
                Intent pp = new Intent(Intent.ACTION_VIEW, Uri.parse("http://pubgplayerz.com/privacy-policy.php"));
                if (pp.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(pp);
                }

                break;
            case R.id.card_report:
                ReportFragment reportFragment = ReportFragment.newInstance("", "");
                moveFragment(reportFragment);
                break;
            case R.id.card_news:
                startActivity(new Intent(context, NewsActivity.class));
                break;
        }
    }

    private void shareData() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Do you want to earn money by playing pubg ? Check PUBGPLAYERZ application , join pubg tournaments and earn on each kill and chicken dinner.Just download the PUBGPLAYERZ application  & Register  \n" +
                "Download http://pubgplayerz.com/pubgplayerz.apk");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frgcontainer, fragment, "me")
                .addToBackStack(null)
                .commit();
    }
}
