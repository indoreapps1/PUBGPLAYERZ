package com.pubgplayerzofficial.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.activity.MainActivity;
import com.pubgplayerzofficial.adapters.MatchResultAdapter;
import com.pubgplayerzofficial.adapters.MatchWinResultAdapter;
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

public class MatchResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String gametitle, datetime, winprice, youtube_url, anouncementStr;
    int enteryfee, perkill, event_id;


    public MatchResultFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MatchResultFragment newInstance(int event_id, String gametitle, String datetime, String winprice, int perkill, int enteryfee, String youtube_url, String anouncementStr) {
        MatchResultFragment fragment = new MatchResultFragment();
        Bundle args = new Bundle();
        args.putString("gametitle", gametitle);
        args.putString("datetime", datetime);
        args.putString("winprice", winprice);
        args.putString("youtube_url", youtube_url);
        args.putString("anouncementStr", anouncementStr);
        args.putInt("perkill", perkill);
        args.putInt("enteryfee", enteryfee);
        args.putInt("event_id", event_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gametitle = getArguments().getString("gametitle");
            datetime = getArguments().getString("datetime");
            winprice = getArguments().getString("winprice");
            anouncementStr = getArguments().getString("anouncementStr");
            youtube_url = getArguments().getString("youtube_url");
            perkill = getArguments().getInt("perkill");
            enteryfee = getArguments().getInt("enteryfee");
            event_id = getArguments().getInt("event_id");
        }
    }

    View view;
    Context context;
    TextView tv_gametime, tv_gametitle, tv_winprice, tv_perkill, tv_entryfee;
    RecyclerView recyleviewDinner, recyleviewResult;
    Result result;
    ImageView imageView_youtube;
    WebView anouncement;
    CardView card_anounce;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        view = inflater.inflate(R.layout.fragment_match_result, container, false);
        init();
        return view;
    }

    private void init() {
        ((MainActivity) getActivity()).hideActionBar(true);
        ((MainActivity) getActivity()).hideShowNav(true);
        card_anounce = view.findViewById(R.id.card_anounce);
        tv_gametitle = view.findViewById(R.id.tv_gametitle);
        tv_gametime = view.findViewById(R.id.tv_gametime);
        tv_winprice = view.findViewById(R.id.tv_winprice);
        tv_perkill = view.findViewById(R.id.tv_perkill);
        tv_entryfee = view.findViewById(R.id.tv_entryfee);
        anouncement = view.findViewById(R.id.anouncement);
        imageView_youtube = view.findViewById(R.id.imageView_youtube);
        recyleviewDinner = view.findViewById(R.id.recyleviewDinner);
        recyleviewResult = view.findViewById(R.id.recyleviewResult);
        recyleviewDinner.setLayoutManager(new LinearLayoutManager(context));
        recyleviewResult.setLayoutManager(new LinearLayoutManager(context));
        anouncement.getSettings().setJavaScriptEnabled(true);
        getChikenData();
        getfullResultData();
//        recyleviewResult.setAdapter(new MatchWinResultAdapter(context, getWinDataList()));
        setValue();
        TextView tv_back = view.findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        imageView_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (youtube_url != null && !youtube_url.equalsIgnoreCase("")) {
                String url = "https://www.youtube.com/channel/UCfol1h--DajZmpZRlqA5TqQ";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
//                    }
                }
            }
        });
    }


    private void getChikenData() {
        final List<Result> resultList = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Just Sec..");
        progressDialog.show();
        progressDialog.setCancelable(false);
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callWinChickenService(event_id, new IAsyncWorkCompletedCallback() {
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
                            List<Result> itemList = sortData(resultList);
                            Collections.sort(itemList, new Comparator<Result>() {
                                @Override
                                public int compare(Result item, Result t1) {
                                    String s1 = item.getChicken_dinner();
                                    String s2 = t1.getChicken_dinner();
                                    return s2.compareToIgnoreCase(s1);
                                }

                            });
                            recyleviewDinner.setAdapter(new MatchResultAdapter(context, itemList));
                        }
                    }
                } else {
                    Toasty.error(context, "Error Try Again").show();
                }
                progressDialog.dismiss();
            }
        });
    }

    private List<Result> sortData(List<Result> resultList) {
        List<Result> newList = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            if (!resultList.get(i).getChicken_dinner().equalsIgnoreCase("0")) {
                result = new Result();
                result.setUser_name(resultList.get(i).getUser_name());
                result.setPer_kill(resultList.get(i).getPer_kill());
                result.setChicken_dinner(resultList.get(i).getChicken_dinner());
                newList.add(result);
            }
        }
        return newList;
    }

    private void getfullResultData() {
        final List<Result> resultListWin = new ArrayList<>();
//        final ProgressDialog progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Just Sec..");
//        progressDialog.show();
//        progressDialog.setCancelable(false);
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callfullMatchService(event_id, new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    if (workName.trim().equalsIgnoreCase("no")) {
                        Toasty.error(context, "No Data Found").show();
                    } else {
                        ContentData contentData = new Gson().fromJson(workName, ContentData.class);
                        for (Result result : contentData.getResult()) {
                            resultListWin.addAll(Arrays.asList(result));
                        }
                        if (resultListWin != null) {
                            Collections.sort(resultListWin, new Comparator<Result>() {
                                @Override
                                public int compare(Result item, Result t1) {
                                    int s1 = item.getWinning_amount();
                                    int s2 = t1.getWinning_amount();
                                    return s2 - s1;
                                }

                            });
                            recyleviewResult.setAdapter(new MatchWinResultAdapter(context, resultListWin));
                        }
                    }
                } else {
                    Toasty.error(context, "Error Try Again").show();
                }
//                progressDialog.dismiss();
            }
        });
    }


    private void setValue() {
        tv_gametitle.setText(gametitle);
        tv_gametime.setText("Organised On:" + datetime);
        tv_winprice.setText("\u20B9" + winprice);
        tv_perkill.setText("\u20B9" + perkill);
        tv_entryfee.setText("\u20B9" + enteryfee);
        if (anouncementStr != null && !anouncementStr.equalsIgnoreCase("")) {
            card_anounce.setVisibility(View.VISIBLE);
            anouncement.loadData(anouncementStr, "text/html", "UTF-8");
        }
    }
}
