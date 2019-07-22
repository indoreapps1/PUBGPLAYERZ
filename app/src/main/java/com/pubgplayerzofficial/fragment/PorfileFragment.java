package com.pubgplayerzofficial.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.activity.MainActivity;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.Result;
import com.pubgplayerzofficial.utilities.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

public class PorfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public PorfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PorfileFragment newInstance(String param1, String param2) {
        PorfileFragment fragment = new PorfileFragment();
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
    private TextView tv_back, tv_editProfile;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private ImageView imageView;
    EditText edit_first_name, edit_username, edit_phone, edit_email;
    RadioGroup radio_group;
    RadioButton male, female;
    Button btn_save, btn_reset;
    TextInputEditText edit_retypepass, edit_new_pass, edit_old, edit_dob;
    String gender = "male", name, oldPass, newPass, rePass, dob;
    final Calendar myCalendar = Calendar.getInstance();
    ProgressBar progressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_porfile, container, false);
        init();
        return view;

    }

    private void init() {
        ((MainActivity) context).hideActionBar(true);
        ((MainActivity) context).hideShowNav(true);
        tv_back = view.findViewById(R.id.tv_back);
        tv_editProfile = view.findViewById(R.id.tv_editProfile);
        progressbar = view.findViewById(R.id.progressbar);
        edit_first_name = view.findViewById(R.id.edit_first_name);
        edit_username = view.findViewById(R.id.edit_username);
        edit_phone = view.findViewById(R.id.edit_phone);
        edit_dob = view.findViewById(R.id.edit_dob);
        radio_group = view.findViewById(R.id.radio_group);
        edit_email = view.findViewById(R.id.edit_email);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        edit_retypepass = view.findViewById(R.id.edit_retypepass);
        edit_new_pass = view.findViewById(R.id.edit_new_pass);
        edit_old = view.findViewById(R.id.edit_old);
        btn_save = view.findViewById(R.id.btn_save);
        btn_reset = view.findViewById(R.id.btn_reset);
        imageView = view.findViewById(R.id.imageView);
        final int cyear = myCalendar.get(Calendar.YEAR);
        final int cmonth = myCalendar.get(Calendar.MONTH);
        final int cday = myCalendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        tv_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkRuntimePermission()) {
                    showImageChooser();
                }
            }
        });
        edit_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(context, date, cyear, cmonth, cday);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileData();
            }
        });
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePassword();
            }
        });
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                if (id == R.id.male) {
                    gender = "male";
                } else {
                    gender = "female";
                }
            }
        });
        edit_retypepass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                newPass = edit_new_pass.getText().toString();
                rePass = edit_retypepass.getText().toString();
                if (newPass.equals(rePass)) {
                } else {
                    edit_retypepass.setError("Password Not Match");
                    edit_retypepass.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        setprofile();
    }

    private void updatePassword() {
        if (validationPass()) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Reset Password wait");
            progressDialog.show();
            DbHelper dbHelper = new DbHelper(context);
            Result result = dbHelper.getUserData();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callUpdatePasswordService(result.getId(), rePass, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        if (workName.equalsIgnoreCase("no")) {
                            Toasty.error(context, "Password Not Reset").show();
                        } else {
                            Toasty.success(context, "Password Change Success").show();
                            edit_old.setText("");
                            edit_old.requestFocus();
                            edit_new_pass.setText("");
                            edit_retypepass.setText("");
                        }
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }

    private boolean validationPass() {
        DbHelper dbHelper = new DbHelper(context);
        Result result = dbHelper.getUserData();
//        oldPass = edit_old.getText().toString();
        newPass = edit_new_pass.getText().toString();
        rePass = edit_retypepass.getText().toString();
//        if (oldPass.length() == 0) {
//            edit_old.requestFocus();
//            edit_old.setError("Enter Old Password");
//            return false;
//        } else if (!result.getPassword().equals(oldPass)) {
//            edit_old.setError("Enter Valid Old Password");
//            edit_old.requestFocus();
//            return false;
        if (newPass.length() == 0) {
            edit_new_pass.setError("Enter New Password");
            edit_new_pass.requestFocus();
            return false;
        } else if (rePass.length() == 0) {
            edit_retypepass.setError("Enter Retype Password");
            edit_retypepass.requestFocus();
            return false;
        }
        return true;

    }

    private void updateProfileData() {
        if (validation()) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Update Profile wait");
            progressDialog.show();
            DbHelper dbHelper = new DbHelper(context);
            Result result = dbHelper.getUserData();
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callProfileDataService(result.getId(), name, dob, gender, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        if (workName.equalsIgnoreCase("no")) {
                            Toasty.error(context, "Profile Not Saved").show();
                        } else {
                            Toasty.success(context, "Profile Saved Success").show();
                            ((MainActivity) context).getUserProfile();
//                            MeFragment meFragment = new MeFragment();
//                            meFragment.setvalue();
//                            moveFragment(meFragment);
                        }
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }

    private boolean validation() {
        name = edit_first_name.getText().toString();
        dob = edit_dob.getText().toString();
        if (name.length() == 0) {
            edit_first_name.setError("Enter name");
            edit_first_name.requestFocus();
            return false;
        } else if (dob.length() == 0) {
            edit_dob.setError("Select Date of Birth");
            edit_dob.requestFocus();
            return false;
        } else if (gender.length() == 0) {
            Toasty.error(context, "Select gender").show();
            return false;
        }
        return true;
    }

    private void setprofile() {
        DbHelper dbHelper = new DbHelper(context);
        Result result = dbHelper.getUserData();
        edit_first_name.setText(result.getFull_name());
        edit_username.setText(result.getUser_name());
        edit_email.setText(result.getEmail());
        edit_phone.setText(result.getMobile_no());
        edit_dob.setText(result.getDob());
        if (result.getGender().equalsIgnoreCase("male")) {
            male.setChecked(true);
        } else {
            if (!result.getGender().equalsIgnoreCase("")) {
                female.setChecked(true);
            } else {
                male.setChecked(true);
            }
        }
        byte[] decodedString = Base64.decode(result.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (result.getImage().equalsIgnoreCase("")) {
            Glide.with(context).load(R.drawable.userprofile).apply(RequestOptions.circleCropTransform()).into(imageView);
        } else {
            Glide.with(context).load(decodedByte).apply(RequestOptions.circleCropTransform()).into(imageView);
        }
    }


    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edit_dob.setText(sdf.format(myCalendar.getTime()));
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void updateProfileImage(String stringImage) {
        DbHelper dbHelper = new DbHelper(context);
        Result result = dbHelper.getUserData();
        if (Utility.isOnline(context)) {
            progressbar.setVisibility(View.VISIBLE);
            ServiceCaller serviceCaller = new ServiceCaller(context);
            serviceCaller.callAllprofileImageService(result.getId(), stringImage, new IAsyncWorkCompletedCallback() {
                @Override
                public void onDone(String workName, boolean isComplete) {
                    if (isComplete) {
                        if (workName.equalsIgnoreCase("ok")) {
//                        Toasty.success(ProfileActivity.this, workName).show();
                            Toasty.success(context, "Upload Profile Success").show();
                            ((MainActivity) context).getUserProfile();
                        } else {
                            Toasty.error(context, "Profile Not Update.").show();

                        }
                    }
                    progressbar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int RC, int RQC, Intent I) {

        super.onActivityResult(RC, RQC, I);

        if (RC == 1 && RQC == RESULT_OK && I != null && I.getData() != null) {

            Uri uri = I.getData();

            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);

                Glide.with(this).load(bitmap).apply(RequestOptions.circleCropTransform()).into(imageView);

                updateProfileImage(getStringImage(bitmap));

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    //check storage and camera run time permission
    private Boolean checkRuntimePermission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Storage");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
       /* if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");*/

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                            REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                                }
                            }
                        });
                return false;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return false;
        }
        return true;
    }

    //add run time permission
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    //show permission alert
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    showImageChooser();
                } else {
                    // Permission Denied
                    Toast.makeText(context, "Permission is Denied", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frgcontainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void showImageChooser() {
        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("image/*");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Choose File to Upload.."), 1);
    }
}
