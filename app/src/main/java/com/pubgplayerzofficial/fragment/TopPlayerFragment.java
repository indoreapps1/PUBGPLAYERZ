package com.pubgplayerzofficial.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.e.mysimmer.LalitRecyclerViewStatic;
import com.google.gson.Gson;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.activity.MainActivity;
import com.pubgplayerzofficial.adapters.TopPayerAdapter;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.ContentData;
import com.pubgplayerzofficial.model.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class TopPlayerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public TopPlayerFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static TopPlayerFragment newInstance(String param1, String param2) {
        TopPlayerFragment fragment = new TopPlayerFragment();
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
    private TextView tv_back;
    private Result result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_top_player, container, false);
        init();
        return view;

    }

    private void init() {
        final List<Result> resultList = new ArrayList<>();
        ((MainActivity) context).hideActionBar(true);
        ((MainActivity) context).hideShowNav(true);
        tv_back = view.findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        final LalitRecyclerViewStatic recyleview = view.findViewById(R.id.recyleview);
        recyleview.setLayoutManager(new LinearLayoutManager(context));
        recyleview.showShimmerAdapter();
//        final ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Just Sec..");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callTopPlayerService(new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    if (workName.equalsIgnoreCase("no")) {
                        Toasty.error(context, "No Data Found").show();
                    } else {
                        ContentData contentData = new Gson().fromJson(workName, ContentData.class);
                        for (Result result : contentData.getResult()) {
                            resultList.addAll(Arrays.asList(result));
                        }
                        if (resultList != null) {
                            Collections.sort(resultList, new Comparator<Result>() {
                                @Override
                                public int compare(Result item, Result t1) {
                                    int s1 = item.getWinning_amount();
                                    int s2 = t1.getWinning_amount();
                                    return s2 - s1;
                                }

                            });
                            recyleview.setAdapter(new TopPayerAdapter(context, resultList));
                            recyleview.hideShimmerAdapter();
                        }
                    }
                } else {
                    Toasty.error(context, "Error Try Again").show();
                }
//                progressDialog.dismiss();
            }
        });
    }
}
