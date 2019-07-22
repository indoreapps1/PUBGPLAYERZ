package com.pubgplayerzofficial.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.activity.MainActivity;
import com.pubgplayerzofficial.activity.PatmActivity;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.model.Result;
import com.pubgplayerzofficial.utilities.FontManager;

import java.util.Random;


public class AddMoneyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddMoneyFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AddMoneyFragment newInstance(String param1, String param2) {
        AddMoneyFragment fragment = new AddMoneyFragment();
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

    View view;
    Context context;
    EditText edt_amount;
    Button btn_add;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_add_money, container, false);
        init();
        return view;
    }

    private void init() {
        TextView tv_rupayIcon = view.findViewById(R.id.tv_rupayIcon);
        Typeface materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        tv_rupayIcon.setTypeface(materialDesignIcons);
        tv_rupayIcon.setText(Html.fromHtml("&#xf1af;"));
        ((MainActivity) context).getUserProfile();
        edt_amount = view.findViewById(R.id.edt_amount);
        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = edt_amount.getText().toString();
                if (amount.length() == 0) {
                    edt_amount.setError("Enter Amount");
                    edt_amount.requestFocus();
                } else if (Integer.parseInt(amount) < 10) {
                    edt_amount.setError("Minimum 10 Rs");
                    edt_amount.requestFocus();
                } else {
                    addMoney();
                }
            }
        });
    }

    private void addMoney() {
        final int random = new Random().nextInt(10000) + 99999999;
        DbHelper dbHelper = new DbHelper(context);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setMessage("Adding Amount wait");
        Result result = dbHelper.getUserData();
        Intent intent = new Intent(context, PatmActivity.class);
        intent.putExtra("uid", result.getId());
//        intent.putExtra("random", random);
        intent.putExtra("amount", edt_amount.getText().toString());
        startActivity(intent);
//        serviceCaller.callPaytmService(result.getId(), random, edt_amount.getText().toString(), new IAsyncWorkCompletedCallback() {
//            @Override
//            public void onDone(String workName, boolean isComplete) {
//                if (isComplete) {
//
//                }
//                progressDialog.dismiss();
//            }
//        });
    }
}
