package com.pubgplayerzofficial.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.e.mysimmer.LalitRecyclerViewStatic;
import com.google.gson.Gson;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.activity.MainActivity;
import com.pubgplayerzofficial.adapters.ParticipatedAdapter;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.ContentData;
import com.pubgplayerzofficial.model.Result;
import com.pubgplayerzofficial.utilities.Contants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class PlayMatchViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private boolean mParam2;


    public PlayMatchViewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PlayMatchViewFragment newInstance(String param1, boolean param2) {
        PlayMatchViewFragment fragment = new PlayMatchViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putBoolean(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getBoolean(ARG_PARAM2);
        }
    }

    View view;
    Context context;
    TextView tv_refresh, tv_gametitle, tv_duo, tv_kill, tv_winprice, tv_matchsc, tv_entryfee, tv_type, tv_map, tv_tpp;
    WebView aboutus;
    Button btn_load, btn_submit;
    LinearLayout recyleviewLayout;
    LalitRecyclerViewStatic recyleview;
    Result[] result;
    ImageView image;
    List<Result> resultList;
    int event_id;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_play_match_view, container, false);
        init();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        ((MainActivity) context).hideActionBar(true);
        ((MainActivity) context).hideShowNav(true);
        resultList = new ArrayList<>();
        aboutus = view.findViewById(R.id.aboutus);
        tv_refresh = view.findViewById(R.id.tv_refresh);
        btn_load = view.findViewById(R.id.btn_load);
        btn_submit = view.findViewById(R.id.btn_submit);
        recyleview = view.findViewById(R.id.recyleview);
        recyleviewLayout = view.findViewById(R.id.recyleviewLayout);
        tv_gametitle = view.findViewById(R.id.tv_gametitle);
        tv_duo = view.findViewById(R.id.tv_duo);
        tv_kill = view.findViewById(R.id.tv_kill);
        tv_winprice = view.findViewById(R.id.tv_winprice);
        tv_matchsc = view.findViewById(R.id.tv_matchsc);
        tv_entryfee = view.findViewById(R.id.tv_entryfee);
        tv_type = view.findViewById(R.id.tv_type);
        tv_map = view.findViewById(R.id.tv_map);
        tv_tpp = view.findViewById(R.id.tv_tpp);
        image = view.findViewById(R.id.image);
        recyleview.setLayoutManager(new LinearLayoutManager(context));
        setValues();
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_refresh.setVisibility(View.VISIBLE);
                recyleviewLayout.setVisibility(View.VISIBLE);
                btn_load.setVisibility(View.GONE);
                setAdapter();
            }
        });
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAdapter();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mParam2) {
                } else {
                    if (btn_submit.getText().toString().equalsIgnoreCase("join")) {
                        DbHelper dbHelper = new DbHelper(context);
                        final Result resultu = dbHelper.getUserData();
                        if (result != null) {
                            if (resultu.getWallet_amount() >= result[0].getEntry_fee()) {
                                showPopUp();
                            } else {
                                JoinMatchFragment joinMatchFragment = (JoinMatchFragment) JoinMatchFragment.newInstance(resultu.getWallet_amount(), result[0].getEntry_fee());
                                moveFragment(joinMatchFragment);
                            }
                        }
                    }
                }
            }
        });
    }

    private void showPopUp() {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_pubgname_dialog);
        dialog.show();
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button btn_join = dialog.findViewById(R.id.btn_join);
        final EditText edit_username = dialog.findViewById(R.id.edit_username);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_username.getText().toString().length() == 0) {
                    edit_username.setError("Enter PUBG Username ");
                    edit_username.requestFocus();
                } else {
                    checkPubgName(dialog, edit_username);
                }
            }
        });
    }

    private void checkPubgName(final Dialog dialog, final EditText edit_username) {
        DbHelper dbHelper = new DbHelper(context);
        final Result results = dbHelper.getUserData();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Just Sec..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final ServiceCaller serviceCaller = new ServiceCaller(context);
        //check slot
        serviceCaller.callPCheckSlotService(result[0].getId(), new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    if (result[0].getSlot() > Integer.parseInt(workName.trim())) {
                        serviceCaller.callCheckPubgnameService(edit_username.getText().toString().trim(), results.getId(), result[0].getId(), new IAsyncWorkCompletedCallback() {
                            @Override
                            public void onDone(String workName, boolean isComplete) {
                                if (isComplete) {
                                    if (!workName.trim().equalsIgnoreCase("no")) {
                                        dialog.dismiss();
                                        PlayFragment playFragment = PlayFragment.newInstance("", "");
                                        moveFragment(playFragment);
                                        Toasty.success(context, "Joind").show();
                                    } else {
                                        edit_username.requestFocus();
                                        edit_username.setError("Username Blocked By PUBGPLAYERZ");
                                    }
                                }
                                progressDialog.dismiss();
                            }
                        });
                    } else {
                        Toasty.error(context, "Sorry Spots Full").show();
                    }
                } else {
                    dialog.dismiss();
                    progressDialog.dismiss();
                    Toasty.error(context, "Sorry Slot Full").show();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setValues() {
        result = new Gson().fromJson(mParam1, Result[].class);
        if (result != null) {
            if (result[0].getImage() != null && !result[0].getImage().equalsIgnoreCase("")) {
                Glide.with(context).load(result[0].getImage()).into(image);
            } else {
                Glide.with(context).load(Contants.SERVICE_BASE_URL + "utils/pubg.jpg").into(image);
            }
            tv_gametitle.setText(result[0].getEvent_name() + " - Match #" + result[0].getId());
            tv_duo.setText(result[0].getType());
            tv_tpp.setText(result[0].getVersion());
            tv_map.setText(result[0].getMap());
            tv_entryfee.setText("\u20B9" + result[0].getEntry_fee());
            tv_kill.setText("\u20B9" + result[0].getPer_kill());
            tv_winprice.setText("\u20B9" + result[0].getChicken_dinner());
            tv_matchsc.setText(result[0].getDate() + " at " + result[0].getTime());
            // aboutus.setText(Html.fromHtml(result[0].getDescription()));
            aboutus.getSettings().setJavaScriptEnabled(true);
            aboutus.loadData(result[0].getDescription(), "text/html", "UTF-8");
            event_id = result[0].getId();
            if (result[0].getSlot() <= result[0].getTotalused()) {
                btn_submit.setText("Match Full");
                btn_submit.setBackgroundDrawable(getResources().getDrawable(R.drawable.done_corner_round_acent));
                btn_submit.setTextColor(Color.BLACK);
            } else {
                if (mParam2) {
                    btn_submit.setText("Joined");
                    btn_submit.setBackgroundDrawable(getResources().getDrawable(R.drawable.done_corner_round_acent));
                    btn_submit.setTextColor(Color.BLACK);
                } else {
                    btn_submit.setText("Join");
                    btn_submit.setBackgroundDrawable(getResources().getDrawable(R.drawable.done_corner_round_acent));
                    btn_submit.setTextColor(Color.BLACK);
                }
            }
        }
    }

    private void setAdapter() {
        resultList.clear();
        recyleview.showShimmerAdapter();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Just Sec..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callParticipatedService(event_id, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    if (workName.trim().equalsIgnoreCase("no")) {
                        Toasty.error(context, "No Data Found").show();
                    } else {
                        ContentData contentData = new Gson().fromJson(workName, ContentData.class);
                        for (Result result : contentData.getResult()) {
                            resultList.addAll(Arrays.asList(result));
                        }
                        if (resultList != null) {
                            recyleview.hideShimmerAdapter();
                            recyleview.setAdapter(new ParticipatedAdapter(context, resultList));
                        }
                    }
                } else {
                    Toasty.error(context, " Error Try Again").show();
                }
                progressDialog.dismiss();
            }
        });

    }
}
