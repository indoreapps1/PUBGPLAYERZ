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


public class MatchResultAdapter extends RecyclerView.Adapter<MatchResultAdapter.MyClass> {

    private Context context;
    private List<Result> resultList;

    public MatchResultAdapter(Context context, List resultList) {
        this.context = context;
        this.resultList = resultList;
    }


    @NonNull
    @Override
    public MatchResultAdapter.MyClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_match_result, viewGroup, false);
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
        myClass.tv_position.setText(i + 1 + "");
        myClass.tv_playername.setText(resultList.get(i).getUser_name());
        myClass.tv_perkill.setText("" + resultList.get(i).getPer_kill());
        myClass.tv_wining.setText(resultList.get(i).getChicken_dinner());
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

        public TextView tv_position, tv_playername, tv_perkill, tv_wining;

        public MyClass(View itemView) {
            super(itemView);
            tv_position = itemView.findViewById(R.id.tv_position);
            tv_playername = itemView.findViewById(R.id.tv_playername);
            tv_perkill = itemView.findViewById(R.id.tv_perkill);
            tv_wining = itemView.findViewById(R.id.tv_wining);

        }
    }

}
