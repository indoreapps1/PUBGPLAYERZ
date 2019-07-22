package com.pubgplayerzofficial.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.model.Result;

import java.util.List;


public class MyReferralsAdapter extends RecyclerView.Adapter<MyReferralsAdapter.MyClass> {

    private Context context;
    private List<Result> resultList;

    public MyReferralsAdapter(Context context, List resultList) {
        this.context = context;
        this.resultList = resultList;
    }


    @NonNull
    @Override
    public MyReferralsAdapter.MyClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_referrals, viewGroup, false);
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
        myClass.tv_date.setText(resultList.get(i).getDate());
        myClass.tv_playername.setText(resultList.get(i).getUser_name());
        myClass.tv_status.setText("" + resultList.get(i).getStatus());
    }

    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frgcontainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyClass extends RecyclerView.ViewHolder {

        public TextView tv_date, tv_playername, tv_status;

        public MyClass(View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_playername = itemView.findViewById(R.id.tv_playername);
            tv_status = itemView.findViewById(R.id.tv_status);

        }
    }

}
