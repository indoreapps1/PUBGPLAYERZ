package com.pubgplayerzofficial.fragment;

import android.app.Dialog;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

public class WithdrawFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WithdrawFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WithdrawFragment newInstance(String param1, String param2) {
        WithdrawFragment fragment = new WithdrawFragment();
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
    TextView tv_msg;
    int amount;
    String phone, samount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_withdraw, container, false);
        init();
        return view;
    }

    private void init() {
        TextView tv_rupayIcon = view.findViewById(R.id.tv_rupayIcon);
        Typeface materialDesignIcons = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        tv_rupayIcon.setTypeface(materialDesignIcons);
        tv_rupayIcon.setText(Html.fromHtml("&#xf1af;"));
        btn_send = view.findViewById(R.id.btn_send);
        edt_amount = view.findViewById(R.id.edt_amount);
        edt_phone = view.findViewById(R.id.edt_phone);
        tv_msg = view.findViewById(R.id.tv_msg);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone = edt_phone.getText().toString();
                samount = edt_amount.getText().toString();
                if (phone.length() == 0) {
                    edt_phone.setError("Enter Phone Number");
                    edt_phone.requestFocus();
                } else if (samount.length() == 0) {
                    edt_amount.setError("Enter amount");
                    edt_amount.requestFocus();
                } else if (Integer.parseInt(samount) > 10000) {
                    edt_amount.setError("Maximum withdraw amount 10000");
                    edt_amount.requestFocus();
                } else if (Integer.parseInt(samount) < 40) {
                    edt_amount.setError("Minimum withdraw amount 40");
                    edt_amount.requestFocus();
                } else {
                    sendMoneyData(phone, samount);
                }
            }
        });
    }

    private void showOtpPopUp() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_otp_dialog);
        dialog.show();
        Button cancel = dialog.findViewById(R.id.btn_cancel);
        final Button reset = dialog.findViewById(R.id.btn_verify);
        final EditText edit_otp = dialog.findViewById(R.id.edit_otp);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setCancelable(false);
        //remove singupdate if not otp verify....
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        //opt verify serice......
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbHelper dbHelper = new DbHelper(context);
                Result result = dbHelper.getUserData();
                if (edit_otp.getText().toString().length() != 0) {
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Otp Verifying");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    ServiceCaller serviceCaller = new ServiceCaller(context);
                    serviceCaller.callWithDrawMoneyService(result.getId(), phone, samount, edit_otp.getText().toString(), result.getMobile_no(), new IAsyncWorkCompletedCallback() {
                        @Override
                        public void onDone(String workName, boolean isComplete) {
                            if (isComplete) {
                                if (workName.trim().equalsIgnoreCase("no")) {
                                    edit_otp.setError("Enter Correct Otp");
                                    edit_otp.requestFocus();
                                } else {
                                    tv_msg.setVisibility(View.VISIBLE);
                                    edt_amount.setText("");
                                    edt_phone.setText("");
                                    edt_phone.requestFocus();
                                    DoneDialog doneDialog = new DoneDialog(context);
                                    doneDialog.show();
                                    doneDialog.setCancelable(true);
                                    doneDialog.setCanceledOnTouchOutside(true);
                                    doneDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    updateProfileData();
                                    dialog.dismiss();
                                }
                            } else {
                                updateProfileData();
                                Toasty.error(context, "Otp Error Try Again").show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    edit_otp.setError("Enter Otp");
                }
            }
        });

    }

    private void sendMoneyData(String phone, String amount) {
        DbHelper dbHelper = new DbHelper(context);
        Result result = dbHelper.getUserData();
        if (result.getWallet_amount() >= Integer.parseInt(amount)) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Withdrawal Money please wait.");
            progressDialog.setCancelable(false);
            progressDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callOtpVerifiyService(result.getMobile_no(), new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        if (!workName.trim().equalsIgnoreCase("no")) {
                            showOtpPopUp();
                        } else {
                            Toasty.error(context, "Withdraw failed").show();
                        }
                    } else {
                        Toasty.error(context, "Try Again Money Not withdraw").show();
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
                            dbHelper.upsertUserData(result);
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
