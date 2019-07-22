package com.pubgplayerzofficial.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.activity.MainActivity;
import com.pubgplayerzofficial.adapters.CustomPagerAdapter;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.model.Result;
import com.pubgplayerzofficial.utilities.FontManager;


public class WalletFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private boolean mParam1;
    private int walletAmount;


    public WalletFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WalletFragment newInstance(boolean param1, int param2) {
        WalletFragment fragment = new WalletFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBoolean(ARG_PARAM1);
            walletAmount = getArguments().getInt(ARG_PARAM2);
        }
    }

    View view;
    Context context;
    private TextView tv_back, tv_amount;
    ViewPager viewPager;
    TabLayout tabLayout;
    CustomPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_wallet, container, false);
        init();
        return view;

    }

    private void init() {
        ((MainActivity) context).hideActionBar(true);
        ((MainActivity) context).hideShowNav(true);
        ((MainActivity) context).getUserProfile();
        Typeface materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        TextView tv_amount_icon = view.findViewById(R.id.tv_amount_icon);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tv_amount = view.findViewById(R.id.tv_amount);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tv_amount_icon.setTypeface(materialDesignIcons);
        tv_amount_icon.setText(Html.fromHtml("&#xf1af;"));
        tv_back = view.findViewById(R.id.tv_back);
        setAmount();
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mParam1) {
                    startActivity(new Intent(context, MainActivity.class));
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int s = intent.getIntExtra("extra_id", 0);
                tv_amount.setText(s + "");
            }
        };
        context.registerReceiver(broadcastReceiver, new IntentFilter("ACTION_ID"));


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int i, final float v, final int i2) {
            }

            @Override
            public void onPageSelected(final int i) {
                if (i == 3) {
                    TransactionsFragment fragment = new TransactionsFragment();
                    if (fragment != null) {
//                        fragment.getTransaction();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(final int i) {
            }
        });
    }

    // implements Refreshpage interface in both fragment TotalSelectedCompanies and CompaniesInterest
//
    public void setAmount() {
        DbHelper dbHelper = new DbHelper(context);
        final Result result = dbHelper.getUserData();
        if (result != null) {
            tv_amount.setText("" + result.getWallet_amount());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        addTabs(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void addTabs(ViewPager viewPager) {
        adapter = new CustomPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new AddMoneyFragment(), "ADD MONEY");
//        adapter.addFrag(new MoneyTransferFragment(), "SEND MONEY");
        adapter.addFrag(new WithdrawFragment(), "WITHDRAW");
        adapter.addFrag(new TransactionsFragment(), "TRANSACTIONS");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(0);
//        if (walletAmount==1){
//            viewPager.setCurrentItem(2);
//        }
//        if (walletAmount==2){
//            viewPager.setCurrentItem(1);
//        }
    }
}
