package com.pubgplayerzofficial.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;


import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.utilities.Utility;

import es.dmoral.toasty.Toasty;

public class SignupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public SignupFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
    private EditText edit_first_name, edit_second, edit_username, edit_email, edit_phone, edit_promocode;
    TextInputEditText edt_pass;
    Button btn_register;
    String firstName, userName, email, phone, pass, promocode, deviceId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        init();
        return view;
    }

    private void init() {
        Button btn_sign = view.findViewById(R.id.btn_signin);
        edit_first_name = view.findViewById(R.id.edit_first_name);
        edit_second = view.findViewById(R.id.edit_second);
        edit_username = view.findViewById(R.id.edit_username);
        edit_email = view.findViewById(R.id.edit_email);
        edit_phone = view.findViewById(R.id.edit_phone);
        edt_pass = view.findViewById(R.id.edt_pass);
        edit_promocode = view.findViewById(R.id.edit_promocode);
        btn_register = view.findViewById(R.id.btn_register);
        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginFragment loginFragment = LoginFragment.newInstance("", "");
                moveFragment(loginFragment);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isOnline(context)) {
                    signupData();
                } else {
                    Toasty.error(context, "No Internet Connection").show();
                }

            }
        });
    }

    private void signupData() {
        if (validation()) {
            deviceId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Signup Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callSignupService(firstName, userName, email, phone, pass, promocode, deviceId, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        if (workName.trim().equalsIgnoreCase("d")) {
                            edit_first_name.setError("Your Device Already Used");
                            edit_first_name.requestFocus();
                        } else if (workName.trim().equalsIgnoreCase("u")) {
                            edit_username.setError("Username Already Exists");
                            edit_username.requestFocus();
                        } else if (workName.trim().equalsIgnoreCase("e")) {
                            edit_email.setError("Email Already Exists");
                            edit_email.requestFocus();
                        } else if (workName.trim().equalsIgnoreCase("p")) {
                            edit_phone.requestFocus();
                            edit_phone.setError("Phone Number Already Exists");
                        } else {
                            if (workName.trim().equalsIgnoreCase("ok")) {
                                showOtpPopUp();
                            }
                        }
                    } else {
                        Toasty.error(context, "Your Connection Slow.Please Try Again").show();
                    }
                    progressDialog.dismiss();
                }
            });
//
//            ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);
//
//            apiInterface.getRegister(firstName, deviceId, userName, email, phone, pass, promocode).enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
//                    String workName = response.body();
//                    Toasty.info(context,workName).show();
//                    if (workName != null) {
//                        if (workName.trim().equalsIgnoreCase("d")) {
//                            edit_first_name.setError("Your Device Already Used");
//                            edit_first_name.requestFocus();
//                        } else if (workName.trim().equalsIgnoreCase("u")) {
//                            edit_username.setError("Username Already Exists");
//                            edit_username.requestFocus();
//                        } else if (workName.trim().equalsIgnoreCase("e")) {
//                            edit_email.setError("Email Already Exists");
//                            edit_email.requestFocus();
//                        } else if (workName.trim().equalsIgnoreCase("p")) {
//                            edit_phone.requestFocus();
//                            edit_phone.setError("Phone Number Already Exists");
//                        } else {
//                            if (workName.trim().equalsIgnoreCase("ok")) {
//                                showOtpPopUp();
//                            }
//                        }
//                    } else {
//                        Toasty.error(context, "Your Connection Slow.Please Try Again").show();
//                    }
//                    progressDialog.dismiss();
//
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
//                    Toasty.error(context, "Your Connection Slow ! Time Out").show();
//                    progressDialog.dismiss();
//
//                }
//            });
        }

    }

    private void showOtpPopUp() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_otp_dialog);
        dialog.show();
        Button cancel = dialog.findViewById(R.id.btn_cancel);
        Button reset = dialog.findViewById(R.id.btn_verify);
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
                if (edit_otp.getText().toString().length() != 0) {
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Otp Verifying");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    ServiceCaller serviceCaller = new ServiceCaller(context);
                    serviceCaller.callOtpVerifiyService(firstName, userName.toLowerCase(), email.toLowerCase(), phone, pass, promocode, edit_otp.getText().toString(), deviceId, new IAsyncWorkCompletedCallback() {
                        @Override
                        public void onDone(String workName, boolean isComplete) {
                            if (isComplete) {
                                if (workName.equalsIgnoreCase("no")) {
                                    edit_otp.setError("Enter Correct Otp");
                                    edit_otp.requestFocus();
                                } else {
//                                    if (workName.equalsIgnoreCase("ok")) {
                                    dialog.dismiss();
                                    LoginFragment loginFragment = LoginFragment.newInstance("", "");
                                    moveFragment(loginFragment);
//                                    }
                                }
                            } else {
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

    private boolean validation() {
        firstName = edit_first_name.getText().toString();
//        lastName = edit_second.getText().toString();
        userName = edit_username.getText().toString().trim();
        userName.replaceAll("\\s+", "");
        email = edit_email.getText().toString();
        phone = edit_phone.getText().toString();
        pass = edt_pass.getText().toString();
        promocode = edit_promocode.getText().toString();
        if (firstName.length() == 0) {
            edit_first_name.setError("Enter Full Name");
            edit_first_name.requestFocus();
            return false;
//        } else if (lastName.length() == 0) {
//            edit_second.setError("Enter Last Name");
//            edit_second.requestFocus();
//            return false;
        } else if (userName.length() == 0) {
            edit_username.setError("Enter Username");
            edit_username.requestFocus();
            return false;
        } else if (userName.contains(" ")) {
            edit_username.setError("No Space Allowed");
            edit_username.requestFocus();
            return false;
        } else if (edit_email.length() == 0) {
            edit_email.setError("Enter Email");
            edit_email.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edit_email.setError("Enter Valid Email");
            edit_email.requestFocus();
            return false;
        } else if (phone.length() == 0) {
            edit_phone.setError("Enter Phone number");
            edit_phone.requestFocus();
            return false;
        } else if (phone.length() != 10) {
            edit_phone.setError("Enter Valid Phone number");
            edit_phone.requestFocus();
            return false;
        } else if (pass.length() == 0) {
            edt_pass.setError("Enter Password");
            edt_pass.requestFocus();
            return false;
        }
        return true;
    }


    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
                .replace(R.id.loginactivity, fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
}
