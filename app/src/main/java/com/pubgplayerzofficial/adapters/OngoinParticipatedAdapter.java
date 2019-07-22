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


public class OngoinParticipatedAdapter extends RecyclerView.Adapter<OngoinParticipatedAdapter.MyClass> {

    private Context context;
    private List<Result> resultList;
    private int num = 10;

    public OngoinParticipatedAdapter(Context context, List resultList) {
        this.context = context;
        this.resultList = resultList;
    }


    @NonNull
    @Override
    public OngoinParticipatedAdapter.MyClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ongoingparticipated, viewGroup, false);
        OngoinParticipatedAdapter.MyClass myClass = new OngoinParticipatedAdapter.MyClass(v);
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
    public void onBindViewHolder(@NonNull final OngoinParticipatedAdapter.MyClass myClass, int i) {
        myClass.tv_position.setText(i + 1 + "");
        myClass.tv_name.setText(resultList.get(i).getPubgname());
    }


    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyClass extends RecyclerView.ViewHolder {

        public TextView tv_position, tv_name;

        public MyClass(View itemView) {
            super(itemView);
            tv_position = (TextView) itemView.findViewById(R.id.tv_position);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

}
