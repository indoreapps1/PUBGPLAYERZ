package com.pubgplayerzofficial.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.model.Result;

import java.util.List;

public class TopPayerAdapter extends RecyclerView.Adapter<TopPayerAdapter.MyClass> {

    private Context context;
    private List<Result> resultList;
    private int num = 10;

    public TopPayerAdapter(Context context, List resultList) {
        this.context = context;
        this.resultList = resultList;
    }


    @NonNull
    @Override
    public TopPayerAdapter.MyClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_top_player, viewGroup, false);
        TopPayerAdapter.MyClass myClass = new TopPayerAdapter.MyClass(v);
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
    public void onBindViewHolder(@NonNull final TopPayerAdapter.MyClass myClass, int i) {
        myClass.tv_position.setText(i + 1 + "");
        myClass.tv_name.setText(resultList.get(i).getUser_name());
        if (i < 3) {
            myClass.tv_wining.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        }
        myClass.tv_wining.setText("\u20B9" + resultList.get(i).getWinning_amount());
    }


    @Override
    public int getItemCount() {
        if (num < resultList.size()) {
            return 10;
        } else {
            return resultList.size();
        }
    }

    public class MyClass extends RecyclerView.ViewHolder {

        public TextView tv_position, tv_name, tv_wining;

        public MyClass(View itemView) {
            super(itemView);
            tv_position = (TextView) itemView.findViewById(R.id.tv_position);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_wining = (TextView) itemView.findViewById(R.id.tv_wining);
        }
    }

}
