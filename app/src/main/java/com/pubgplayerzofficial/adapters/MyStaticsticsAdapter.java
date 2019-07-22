package com.pubgplayerzofficial.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.model.Result;

import java.util.List;


public class MyStaticsticsAdapter extends RecyclerView.Adapter<MyStaticsticsAdapter.MyClass> {

    private Context context;
    private List<Result> resultList;

    public MyStaticsticsAdapter(Context context, List resultList) {
        this.context = context;
        this.resultList = resultList;
    }


    @NonNull
    @Override
    public MyStaticsticsAdapter.MyClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_statisticcs, viewGroup, false);
        MyStaticsticsAdapter.MyClass myClass = new MyStaticsticsAdapter.MyClass(v);
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
    public void onBindViewHolder(@NonNull final MyStaticsticsAdapter.MyClass myClass, int i) {
        myClass.tv_position.setText(i + 1 + "");
        myClass.tv_gametitle.setText(resultList.get(i).getEvent_name());
        myClass.tv_gametime.setText("Played On:" + resultList.get(i).getDate() + " at " + resultList.get(i).getTime());
        myClass.tv_wining.setText("\u20B9" + resultList.get(i).getWinning_amount());
        myClass.tv_paid.setText("\u20B9" + resultList.get(i).getPaid());
    }


    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyClass extends RecyclerView.ViewHolder {

        public TextView tv_position, tv_gametitle, tv_paid, tv_wining, tv_gametime;

        public MyClass(View itemView) {
            super(itemView);
            tv_position = itemView.findViewById(R.id.tv_position);
            tv_gametitle = itemView.findViewById(R.id.tv_gametitle);
            tv_wining = itemView.findViewById(R.id.tv_wining);
            tv_paid = itemView.findViewById(R.id.tv_paid);
            tv_gametime = itemView.findViewById(R.id.tv_gametime);
        }
    }

}
