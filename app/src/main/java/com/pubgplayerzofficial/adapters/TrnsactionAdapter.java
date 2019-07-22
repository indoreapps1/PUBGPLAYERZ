package com.pubgplayerzofficial.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.model.Result;

import java.util.List;


public class TrnsactionAdapter extends RecyclerView.Adapter<TrnsactionAdapter.MyClass> {

    private Context context;
    private List<Result> resultList;

    public TrnsactionAdapter(Context context, List resultList) {
        this.context = context;
        this.resultList = resultList;
    }


    @NonNull
    @Override
    public TrnsactionAdapter.MyClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_transaction, viewGroup, false);
        TrnsactionAdapter.MyClass myClass = new TrnsactionAdapter.MyClass(v);
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
    public void onBindViewHolder(@NonNull final TrnsactionAdapter.MyClass holder, int i) {
        String type = resultList.get(i).getType();
        if (type.equalsIgnoreCase("debit")) {
            holder.tv_type.setText(type);
            holder.tv_type.setTextColor(Color.RED);
            holder.tv_amount.setTextColor(Color.RED);
            holder.tv_amount.setText("-" + "\u20B9" + resultList.get(i).getAmount());
        } else {
            holder.tv_type.setText(type);
            holder.tv_type.setTextColor(context.getResources().getColor(R.color.colorGreen));
            holder.tv_amount.setTextColor(context.getResources().getColor(R.color.colorGreen));
            holder.tv_amount.setText("+" + "\u20B9" + resultList.get(i).getAmount());
        }
        holder.tv_name.setText(resultList.get(i).getSubject());
        holder.tv_time.setText(resultList.get(i).getDate());
    }


    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyClass extends RecyclerView.ViewHolder {

        public TextView tv_amount, tv_time, tv_name, tv_type;

        public MyClass(View itemView) {
            super(itemView);
            tv_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
        }
    }

}
