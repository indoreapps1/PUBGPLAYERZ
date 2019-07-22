package com.pubgplayerzofficial.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.e.mysimmer.LalitRecyclerViewStatic;
import com.google.gson.Gson;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.activity.MainActivity;
import com.pubgplayerzofficial.adapters.MyStaticsticsAdapter;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.ContentData;
import com.pubgplayerzofficial.model.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MyStatisticsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyStatisticsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyStatisticsFragment newInstance(String param1, String param2) {
        MyStatisticsFragment fragment = new MyStatisticsFragment();
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
    LalitRecyclerViewStatic lalitRecyclerViewStatic;
    List<Result> resultList;
    LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_my_statistics, container, false);
        init();
        return view;

    }

    private void init() {
        ((MainActivity) context).hideActionBar(true);
        ((MainActivity) context).hideShowNav(true);
        tv_back = view.findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        layout = view.findViewById(R.id.layout);
        lalitRecyclerViewStatic = view.findViewById(R.id.recyleview);
        lalitRecyclerViewStatic.setLayoutManager(new LinearLayoutManager(context));
        lalitRecyclerViewStatic.showShimmerAdapter();
        resultList = new ArrayList<>();
        ServiceCaller serviceCaller = new ServiceCaller(context);
        DbHelper dbHelper = new DbHelper(context);
        final Result result = dbHelper.getUserData();
        serviceCaller.callAllStatisticsService(result.getId(), new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
//                    Toast.makeText(context, workName, Toast.LENGTH_SHORT).show();
                    if (workName.trim().equals("no")) {
                        lalitRecyclerViewStatic.hideShimmerAdapter();
                        noDataFound();
                    } else {
                        ContentData contentData = new Gson().fromJson(workName, ContentData.class);
                        for (Result result1 : contentData.getResult()) {
                            resultList.addAll(Arrays.asList(result1));
                        }
                        if (resultList != null) {
                            lalitRecyclerViewStatic.setAdapter(new MyStaticsticsAdapter(context, resultList));
                            lalitRecyclerViewStatic.hideShimmerAdapter();
                        } else {
                            noDataFound();
                        }
                    }
                }
            }
        });

    }

    private void noDataFound() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);
        nodata.setText("Any Statistics Not found");
        layout.setGravity(Gravity.CENTER);
        layout.removeAllViews();
        layout.addView(view);
    }

}
