package com.pubgplayerzofficial.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.activity.MainActivity;
import com.pubgplayerzofficial.adapters.MyReferralsAdapter;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.ContentData;
import com.pubgplayerzofficial.model.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MyReferalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyReferalFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyReferalFragment newInstance(String param1, String param2) {
        MyReferalFragment fragment = new MyReferalFragment();
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
    private TextView tv_back, tv_refer, tv_amount, tv_actualrefer, tv_actualamount;
    List<Result> resultList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_my_referal, container, false);
        init();
        return view;
    }

    private void init() {
        resultList = new ArrayList<>();
        ((MainActivity) getActivity()).hideActionBar(true);
        ((MainActivity) getActivity()).hideShowNav(true);
        tv_back = view.findViewById(R.id.tv_back);
        tv_refer = view.findViewById(R.id.tv_refer);
        tv_actualrefer = view.findViewById(R.id.tv_actualrefer);
        tv_amount = view.findViewById(R.id.tv_amount);
        tv_actualamount = view.findViewById(R.id.tv_actualamount);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        final RecyclerView recyleviewRefre = view.findViewById(R.id.recyleviewRefre);
        recyleviewRefre.setLayoutManager(new LinearLayoutManager(context));
        DbHelper dbHelper = new DbHelper(context);
        Result result = dbHelper.getUserData();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Just Sec..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callMyReferralsService(result.getId(), new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    if (!workName.trim().equalsIgnoreCase("no")) {
                        ContentData contentData = new Gson().fromJson(workName, ContentData.class);
                        for (Result result : contentData.getResult()) {
                            resultList.addAll(Arrays.asList(result));
                        }
                        if (resultList != null) {
                            MyReferralsAdapter myReferralsAdapter = new MyReferralsAdapter(context, resultList);
                            recyleviewRefre.setAdapter(myReferralsAdapter);
                        } else {
                            Toasty.error(context, "No Data Found").show();
                        }

                    } else {
                        Toasty.error(context, "No Data Found").show();
                    }
                }
                tv_refer.setText(resultList.size() + "");
//                tv_amount.setText("\u20B9" + (resultList.size() * 10));
                progressDialog.dismiss();
            }
        });
        ServiceCaller serviceCallera = new ServiceCaller(context);
        serviceCallera.callMyReferralsAmountService(result.getId(), new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    if (!workName.trim().equalsIgnoreCase("0")) {
//                        Result contentData = new Gson().fromJson(workName, Result.class);
                        tv_actualrefer.setText(workName.trim() + "");
                        tv_actualamount.setText("\u20B9" + (Integer.parseInt(workName.trim()) * 10));
                    } else {
                        Toasty.error(context, "No Data Found").show();
                    }
                }

                progressDialog.dismiss();
            }
        });
    }
}
