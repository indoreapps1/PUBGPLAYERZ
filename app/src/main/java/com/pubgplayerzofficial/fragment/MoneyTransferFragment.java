package com.pubgplayerzofficial.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.ContentData;
import com.pubgplayerzofficial.model.Result;
import com.pubgplayerzofficial.utilities.DoneDialog;
import com.pubgplayerzofficial.utilities.FontManager;

import es.dmoral.toasty.Toasty;

public class MoneyTransferFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MoneyTransferFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MoneyTransferFragment newInstance(String param1, String param2) {
        MoneyTransferFragment fragment = new MoneyTransferFragment();
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
    Button btn_send;
    EditText edt_amount, edt_phone;
    int amount;
    Result result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_moneytransfer, container, false);
        init();
        return view;
    }

    private void init() {
        DbHelper dbHelper = new DbHelper(context);
        result = dbHelper.getUserData();
        TextView tv_rupayIcon = view.findViewById(R.id.tv_rupayIcon);
        Typeface materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        tv_rupayIcon.setTypeface(materialDesignIcons);
        tv_rupayIcon.setText(Html.fromHtml("&#xf1af;"));
        btn_send = view.findViewById(R.id.btn_send);
        edt_amount = view.findViewById(R.id.edt_amount);
        edt_phone = view.findViewById(R.id.edt_phone);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = edt_phone.getText().toString();
                String amount = edt_amount.getText().toString();
                if (phone.length() == 0) {
                    edt_phone.setError("Enter Phone Number");
                    edt_phone.requestFocus();
                } else if (phone.equalsIgnoreCase(result.getMobile_no())) {
                    edt_phone.setError("You can't send money your own Number");
                    edt_phone.requestFocus();
                } else if (amount.length() == 0) {
                    edt_amount.setError("Enter amount");
                    edt_amount.requestFocus();
                } else if (amount.equals("0")) {
                    edt_amount.setError("Enter amount atleast 1");
                    edt_amount.requestFocus();
                } else {
                    sendMoneyData(phone, amount);
                }
            }
        });
    }

    private void sendMoneyData(String phone, String amount) {
        if (result.getWallet_amount() >= Integer.parseInt(amount)) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Sending Money please wait.");
            progressDialog.setCancelable(false);
            progressDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callSendMoneyService(result.getId(), phone, amount, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        if (!workName.trim().equalsIgnoreCase("no")) {
                            Toasty.success(context, "Money Send Success").show();
                            edt_amount.setText("");
                            edt_phone.setText("");
                            DoneDialog doneDialog = new DoneDialog(context);
                            doneDialog.show();
                            doneDialog.setCancelable(true);
                            doneDialog.setCanceledOnTouchOutside(true);
                            doneDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            updateProfileData();

                        } else {
                            Toasty.error(context, "Money Send failed").show();
                        }
                    } else {
                        Toasty.error(context, "Try Again Money Not send").show();
                    }
                    progressDialog.dismiss();
                }
            });
        } else {
            edt_amount.requestFocus();
            edt_amount.setError("No Enough Amount");
        }
    }

    private void updateProfileData() {
        final DbHelper dbHelper = new DbHelper(context);
        Result result = dbHelper.getUserData();
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callUserProfileService(result.getId(), new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    if (!workName.equalsIgnoreCase("no")) {
                        ContentData contentData = new Gson().fromJson(workName, ContentData.class);
                        for (Result result : contentData.getResult()) {
                            amount = result.getWallet_amount();
                        }
                        Intent intent = new Intent("ACTION_ID");
                        intent.putExtra("extra_id", amount);
                        context.sendBroadcast(intent);
                    }
                }
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

    public void getUserProfile() {
        final DbHelper dbHelper = new DbHelper(context);
        Result result = dbHelper.getUserData();
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callUserProfileService(result.getId(), new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    if (!workName.equalsIgnoreCase("no")) {
                        ContentData contentData = new Gson().fromJson(workName, ContentData.class);
                        dbHelper.deleteUserData();
                        for (Result result : contentData.getResult()) {
                            dbHelper.upsertUserData(result);
                        }
                    }
                }
            }
        });
    }
}
