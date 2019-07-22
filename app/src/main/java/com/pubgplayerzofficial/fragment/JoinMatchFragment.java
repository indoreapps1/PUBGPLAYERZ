package com.pubgplayerzofficial.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.activity.MainActivity;


public class JoinMatchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private int mParam2;

    public JoinMatchFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static JoinMatchFragment newInstance(int param1, int param2) {
        JoinMatchFragment fragment = new JoinMatchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    View view;
    Context context;
    Button btn_add, btn_cancel;
    TextView tv_back, tv_fee, tv_blance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_join_match, container, false);
        init();
        return view;
    }

    private void init() {
        ((MainActivity) getActivity()).hideActionBar(true);
        ((MainActivity) getActivity()).hideShowNav(true);
        btn_add = view.findViewById(R.id.btn_add);
        tv_back = view.findViewById(R.id.tv_back);
        tv_fee = view.findViewById(R.id.tv_fee);
        tv_blance = view.findViewById(R.id.tv_blance);
        tv_blance.setText("\u20B9" + mParam1);
        tv_fee.setText("\u20B9" + mParam2);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WalletFragment walletFragment = WalletFragment.newInstance(false, 0);
                moveFragment(walletFragment);
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });


    }

    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frgcontainer, fragment)
                .addToBackStack(null)
                .commit();
    }

}
