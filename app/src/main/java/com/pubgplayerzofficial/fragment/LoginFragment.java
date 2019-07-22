package com.pubgplayerzofficial.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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
import com.pubgplayerzofficial.activity.MainActivity;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.ContentData;
import com.pubgplayerzofficial.model.Result;
import com.pubgplayerzofficial.utilities.Utility;

import es.dmoral.toasty.Toasty;

public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
    TextInputEditText edt_pass, edt_username;
    String username, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_login, container, false);
        init();
        return view;
    }

    private void init() {
        Button btn_sign = view.findViewById(R.id.btn_sign);
        Button btn_login = view.findViewById(R.id.btn_login);
        TextView resetnow = view.findViewById(R.id.resetnow);
        TextView resetu = view.findViewById(R.id.resetu);
        edt_username = view.findViewById(R.id.edt_username);
        edt_pass = view.findViewById(R.id.edt_pass);
        resetnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isOnline(context)) {
                    showResetDialog();
                } else {
                    Toasty.error(context, "No Internet Connection").show();
                }
            }
        });
        resetu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isOnline(context)) {
                    showResetDialogU();
                } else {
                    Toasty.error(context, "No Internet Connection").show();
                }
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//check login Data here.
                if (Utility.isOnline(context)) {
                    loginData();
                } else {
                    Toasty.error(context, "No Internet Connection").show();
                }
            }
        });
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignupFragment signupFragment = SignupFragment.newInstance("", "");
                moveFragment(signupFragment);
            }
        });
    }

    private void loginData() {
//        Invalid Information
        if (validation()) {
            final DbHelper dbHelper = new DbHelper(context);
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Verifing Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callLoginService(username, password, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        if (workName.trim().equalsIgnoreCase("Invalid UserName")) {
                            edt_username.setError(workName);
                            edt_username.requestFocus();
                        } else if (workName.trim().equalsIgnoreCase("Invalid Password")) {
                            edt_pass.setError(workName);
                            edt_pass.requestFocus();
                        } else {
                            ContentData contentData = new Gson().fromJson(workName, ContentData.class);
                            dbHelper.deleteUserData();
                            for (Result result : contentData.getResult()) {
                                if (result.getStatus() == 1) {
                                    dbHelper.upsertUserData(result);
                                    Intent intent = new Intent(context, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    Toasty.info(context, "Sorry you Can't Access. Please Contact To Admin").show();
//                                    Toasty.info(context, workName).show();
                                }
                            }
                        }
                    } else {
                        Toasty.error(context, "Login Error Try Again").show();
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }

    private boolean validation() {
        username = edt_username.getText().toString().trim();
        password = edt_pass.getText().toString();
        if (username.length() == 0) {
            edt_username.setError("Enter Username");
            edt_username.requestFocus();
            return false;
        } else if (password.length() == 0) {
            edt_pass.setError("Enter Password");
            edt_pass.requestFocus();
            return false;
        }

        return true;
    }

    private void showResetDialogU() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_reset_dialogu);
        final Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        Button cancel = dialog.findViewById(R.id.btn_cancel);
        Button reset = dialog.findViewById(R.id.btn_reset);
        final EditText edit_phone = dialog.findViewById(R.id.edit_phone);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = edit_phone.getText().toString();
                if (phone.length() != 0) {
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Reset Wait");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    ServiceCaller serviceCaller = new ServiceCaller(context);
                    serviceCaller.callResetUserNameService(phone, new IAsyncWorkCompletedCallback() {
                        @Override
                        public void onDone(String workName, boolean isComplete) {
                            if (isComplete) {
                                if (workName.trim().equalsIgnoreCase("ok")) {
                                    Toasty.success(context, "Username Send On your Number").show();
                                    dialog.dismiss();
                                } else {
                                    edit_phone.setError("Enter Registerd Number");
                                    edit_phone.requestFocus();
                                }
                            }
                            progressDialog.dismiss();

                        }
                    });
                } else {
                    edit_phone.setError("Enter Valid Phone");
                    edit_phone.requestFocus();
                }
            }
        });
//        Window window = dialog.getWindow();
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        window.setGravity(Gravity.CENTER);
    }

    private void showResetDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_reset_dialog);
        final Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.show();
        Button cancel = dialog.findViewById(R.id.btn_cancel);
        Button reset = dialog.findViewById(R.id.btn_reset);
        final EditText edit_phone = dialog.findViewById(R.id.edit_phone);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phone = edit_phone.getText().toString();
                if (phone.length() != 0) {
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Reset Wait");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    ServiceCaller serviceCaller = new ServiceCaller(context);
                    serviceCaller.callResetUserService(phone, new IAsyncWorkCompletedCallback() {
                        @Override
                        public void onDone(String workName, boolean isComplete) {
                            if (isComplete) {
                                if (workName.trim().equalsIgnoreCase("ok")) {
                                    Toasty.success(context, "Password Send On your Number").show();
                                    dialog.dismiss();
                                } else {
                                    edit_phone.setError("Enter Registerd Number");
                                    edit_phone.requestFocus();
                                }
                            }
                            progressDialog.dismiss();

                        }
                    });
                } else {
                    edit_phone.setError("Enter Valid Phone");
                    edit_phone.requestFocus();
                }
            }
        });
//        Window window = dialog.getWindow();
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        window.setGravity(Gravity.CENTER);
    }

    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
                .replace(R.id.loginactivity, fragment)
                .addToBackStack(null)
                .commit();
    }
}
