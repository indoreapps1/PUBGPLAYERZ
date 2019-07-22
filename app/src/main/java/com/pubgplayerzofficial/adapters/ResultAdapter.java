package com.pubgplayerzofficial.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.fragment.MatchResultFragment;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.Result;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.MyClass> {

    private Context context;
    private List<Result> resultList;
    private int num = 20;

    public ResultAdapter(Context context, List resultList) {
        this.context = context;
        this.resultList = resultList;
    }


    @NonNull
    @Override
    public ResultAdapter.MyClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_result, viewGroup, false);
        MyClass myClass = new MyClass(v);
        return myClass;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyClass myClass, final int i) {
        myClass.tv_gametitle.setText(resultList.get(i).getEvent_name() + " - Match #" + resultList.get(i).getId());
        myClass.tv_gametime.setText("Time:" + resultList.get(i).getDate() + " at " + resultList.get(i).getTime());
        myClass.tv_winprice.setText("\u20B9" + resultList.get(i).getChicken_dinner());
        myClass.tv_perkill.setText("\u20B9" + resultList.get(i).getPer_kill());
        myClass.tv_entryfee.setText("\u20B9" + resultList.get(i).getEntry_fee());
        myClass.tv_type.setText(resultList.get(i).getType());
        myClass.tv_version.setText(resultList.get(i).getVersion());
        myClass.tv_map.setText(resultList.get(i).getMap());
        if (resultList.get(i).getImage() != null && !resultList.get(i).getImage().equalsIgnoreCase("")) {
            Glide.with(context).load(resultList.get(i).getImage()).into(myClass.image_game);
        } else {
            myClass.image_game.setVisibility(View.GONE);
        }
        myClass.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchResultFragment matchResultFragment = (MatchResultFragment) MatchResultFragment.newInstance(resultList.get(i).getId(), resultList.get(i).getEvent_name(), resultList.get(i).getDate() + " at " + resultList.get(i).getTime(), resultList.get(i).getChicken_dinner(), resultList.get(i).getPer_kill(), resultList.get(i).getEntry_fee(), resultList.get(i).getYoutube_url(), resultList.get(i).getAnouncement());
                moveFragment(matchResultFragment);

            }
        });
        myClass.bt_watchmatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = resultList.get(i).getYoutube_url();
                if (url != null && !url.equalsIgnoreCase("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }
            }
        });
        DbHelper dbHelper = new DbHelper(context);
        Result resultd = dbHelper.getUserData();
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callcheckongoinpopService(resultd.getId(), resultList.get(i).getId(), new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    if (!workName.trim().equalsIgnoreCase("no")) {
                        myClass.bt_submit.setText("Joined");
                    } else
                        myClass.bt_submit.setText("Not Joined");

                } else {
                    Toasty.error(context, "Error Try Again").show();
                }
            }
        });

    }

    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frgcontainer, fragment, "result")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        if (num < resultList.size()) {
            return 20;
        } else {
            return resultList.size();
        }
    }

    public class MyClass extends RecyclerView.ViewHolder {

        public ImageView image_game;
        public TextView tv_gametitle, tv_gametime, tv_winprice, tv_perkill, tv_entryfee, tv_type, tv_version, tv_map;
        public CardView cardView;
        Button bt_submit, bt_watchmatch;

        public MyClass(View itemView) {
            super(itemView);
//             progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
            image_game = (ImageView) itemView.findViewById(R.id.image_game);
            tv_gametitle = (TextView) itemView.findViewById(R.id.tv_gametitle);
            tv_gametime = (TextView) itemView.findViewById(R.id.tv_gametime);
            tv_winprice = (TextView) itemView.findViewById(R.id.tv_winprice);
            tv_perkill = (TextView) itemView.findViewById(R.id.tv_perkill);
            tv_entryfee = (TextView) itemView.findViewById(R.id.tv_entryfee);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_version = (TextView) itemView.findViewById(R.id.tv_version);
            tv_map = (TextView) itemView.findViewById(R.id.tv_map);
            bt_submit = itemView.findViewById(R.id.bt_submit);
            bt_watchmatch = itemView.findViewById(R.id.bt_watchmatch);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }

}
