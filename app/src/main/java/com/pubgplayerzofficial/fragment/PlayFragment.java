package com.pubgplayerzofficial.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.e.mysimmer.LalitRecyclerView;
import com.google.gson.Gson;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.activity.MainActivity;
import com.pubgplayerzofficial.adapters.GameViewerAdapter;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.ContentData;
import com.pubgplayerzofficial.model.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class PlayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PlayFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PlayFragment newInstance(String param1, String param2) {
        PlayFragment fragment = new PlayFragment();
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
    private LalitRecyclerView lalitRecyclerView;
    List<Result> resultListPlay;
    LinearLayout layout;
    Result result;
    DbHelper dbHelper;
    ServiceCaller serviceCaller;
    RadioGroup radio_grp;
    RadioButton all, solo, duo, squad;
    String type = "all";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_play, container, false);
        init();
        return view;
    }

    private void init() {
        dbHelper = new DbHelper(context);
        serviceCaller = new ServiceCaller(getActivity());
        result = dbHelper.getUserData();
        resultListPlay = new ArrayList<>();
        ((MainActivity) getActivity()).hideActionBar(false);
        ((MainActivity) context).hideShowNav(false);
        ((MainActivity) context).getUserProfile();
        layout = view.findViewById(R.id.layout);
        lalitRecyclerView = view.findViewById(R.id.recyleview);
        lalitRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        getDataList();
        lalitRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0) {
//                    ((MainActivity) getActivity()).hideShowNav(true);
//                } else if (dy < 0) {
//                    ((MainActivity) getActivity()).hideShowNav(false);
//                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        if (resultListPlay == null) {
            {
                noDataFound();
            }
        }
        radio_grp = view.findViewById(R.id.radio_grp);
        all = view.findViewById(R.id.all);
        solo = view.findViewById(R.id.solo);
        duo = view.findViewById(R.id.duo);
        squad = view.findViewById(R.id.squad);
        radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = radio_grp.getCheckedRadioButtonId();
                if (id == R.id.all) {
                    type = "all";
                    getDataList();
                } else if (id == R.id.solo) {
                    type = "solo";
                    getDataList();
                } else if (id == R.id.duo) {
                    type = "duo";
                    getDataList();
                } else {
                    type = "squad";
                    getDataList();
                }
            }
        });
    }

    private void getDataList() {
        resultListPlay.clear();
        lalitRecyclerView.showShimmerAdapter();
        serviceCaller.callPlayMatchService("1", result.getId(),type, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    ContentData contentData = new Gson().fromJson(workName, ContentData.class);
                    for (Result result : contentData.getResult()) {
                        resultListPlay.addAll(Arrays.asList(result));
                    }
                    if (resultListPlay != null) {
                        Collections.reverse(resultListPlay);
                        GameViewerAdapter gameViewerAdapter = new GameViewerAdapter(context, resultListPlay);
                        lalitRecyclerView.setAdapter(gameViewerAdapter);

                    }
                } else {
                    noDataFound();
                }
                lalitRecyclerView.hideShimmerAdapter();
            }
        });
    }


    private void noDataFound() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.no_data_found, null);
        TextView nodata = (TextView) view.findViewById(R.id.nodata);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);
        nodata.setText("There are no matches currently. Check back in some time...");
        layout.setGravity(Gravity.CENTER);
        layout.removeAllViews();
        layout.addView(view);
    }
}
