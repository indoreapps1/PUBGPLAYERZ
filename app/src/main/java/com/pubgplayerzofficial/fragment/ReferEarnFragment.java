package com.pubgplayerzofficial.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.activity.MainActivity;


public class ReferEarnFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public ReferEarnFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ReferEarnFragment newInstance(String param1, String param2) {
        ReferEarnFragment fragment = new ReferEarnFragment();
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
    private TextView tv_back, tv_menu, tv_refer;
    private Button btn_refer, btn_referEarn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_refer_earn, container, false);
        init();
        return view;
    }

    private void init() {
        ((MainActivity) getActivity()).hideActionBar(true);
        ((MainActivity) getActivity()).hideShowNav(true);
        tv_back = view.findViewById(R.id.tv_back);
        tv_menu = view.findViewById(R.id.tv_menu);
        tv_refer = view.findViewById(R.id.tv_refer);
        btn_refer = view.findViewById(R.id.btn_refer);
        btn_referEarn = view.findViewById(R.id.btn_referEarn);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        btn_referEarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyReferalFragment myReferalFragment = MyReferalFragment.newInstance("", "");
                moveFragment(myReferalFragment);
            }
        });
//        tv_menu.setVisibility(View.GONE);
        tv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup();
            }
        });
        tv_refer.setText(mParam1);
        btn_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Do you want to earn money by playing pubg ? Check PUBGPLAYERZ application , join pubg tournaments and earn on each kill and chicken dinner.Just download the PUBGPLAYERZ application  & Register with Promo code " + "'" + mParam1 + "'" + " \nhttp://pubgplayerz.com/pubgplayerz.apk");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });

    }

    private void showPopup() {
        PopupMenu popup = new PopupMenu(context, tv_menu);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_refer) {
                    MyReferalFragment myReferalFragment = MyReferalFragment.newInstance("", "");
                    moveFragment(myReferalFragment);
                }
                return true;
            }
        });

        popup.show();//showing popup menu
    }

    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frgcontainer, fragment)
                .addToBackStack(null)
                .commit();
    }
}
