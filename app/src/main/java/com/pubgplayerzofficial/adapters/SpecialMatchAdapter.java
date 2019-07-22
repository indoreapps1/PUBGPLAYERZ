package com.pubgplayerzofficial.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.fragment.JoinMatchFragment;
import com.pubgplayerzofficial.fragment.PlayFragment;
import com.pubgplayerzofficial.fragment.PlayMatchViewFragment;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.Result;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class SpecialMatchAdapter extends RecyclerView.Adapter<SpecialMatchAdapter.MyClass> {

    private Context context;
    private List<Result> resultList, passList;
    Result result;
    String name;

    public SpecialMatchAdapter(Context context, List resultList) {
        this.context = context;
        this.resultList = resultList;
        passList = new ArrayList<>();
        setHasStableIds(true);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @NonNull
    @Override
    public SpecialMatchAdapter.MyClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_special_match, viewGroup, false);
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
        int uspace = resultList.get(i).getTotalused();
        int total = resultList.get(i).getSlot();
        myClass.tv_count.setText(uspace + "/" + total);
        if (total == uspace) {
            myClass.tv_info.setText("No Spots Left! Match is Full");
            myClass.seekbar.setMax(100);
            myClass.seekbar.setProgress(100);
        } else {
            myClass.seekbar.setMax(total);
            if (uspace == 0) {
                myClass.seekbar.setProgress(0);
            } else {
                myClass.seekbar.setProgress(uspace);
            }
            myClass.tv_info.setText("Only " + (total - uspace) + " Spots Left!");
            myClass.tv_info.setTextColor(context.getResources().getColor(R.color.colorSecondary));
        }
        if (resultList.get(i).getImage() != null && !resultList.get(i).getImage().equalsIgnoreCase("")) {
            Glide.with(context).load(resultList.get(i).getImage()).into(myClass.image_game);
        } else {
            myClass.image_game.setVisibility(View.GONE);
        }
        if (total == uspace) {
            myClass.bt_submit.setText("Match Full");
        } else {
            myClass.bt_submit.setText("Join");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                myClass.bt_submit.setBackground(context.getResources().getDrawable(R.drawable.roundable));
                myClass.bt_submit.setTextColor(context.getResources().getColor(R.color.colorBlue));
            }
        }
        myClass.bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myClass.bt_submit.getText().toString().equalsIgnoreCase("Join")) {
//                    Toasty.success(context, "Success").show();
                    checkJoin(i);
                } else {
                    Toasty.error(context, "Sorry Spots Full").show();
                }
            }
        });
        myClass.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result = new Result();
                result.setEvent_name(resultList.get(i).getEvent_name());
                result.setId(resultList.get(i).getId());
                result.setDate(resultList.get(i).getDate());
                result.setTime(resultList.get(i).getTime());
                result.setChicken_dinner(resultList.get(i).getChicken_dinner());
                result.setPer_kill(resultList.get(i).getPer_kill());
                result.setEntry_fee(resultList.get(i).getEntry_fee());
                result.setType(resultList.get(i).getType());
                result.setVersion(resultList.get(i).getVersion());
                result.setMap(resultList.get(i).getMap());
                result.setTotalused(resultList.get(i).getTotalused());
                result.setSlot(resultList.get(i).getSlot());
                result.setDescription(resultList.get(i).getDescription());
                result.setImage(resultList.get(i).getImage());
                passList.add(result);
                String data = new Gson().toJson(passList);
                PlayMatchViewFragment playMatchViewFragment = PlayMatchViewFragment.newInstance(data, false);
                moveFragment(playMatchViewFragment);
            }
        });
    }

    private void checkJoin(int i) {
        DbHelper dbHelper = new DbHelper(context);
        final Result result = dbHelper.getUserData();
        if (result != null) {
            if (result.getWallet_amount() >= resultList.get(i).getEntry_fee()) {
                showPopUp(i);
            } else {
                JoinMatchFragment joinMatchFragment = (JoinMatchFragment) JoinMatchFragment.newInstance(result.getWallet_amount(), resultList.get(i).getEntry_fee());
                moveFragment(joinMatchFragment);
            }
        }

    }

    private void showPopUp(final int i) {
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_pubgname_dialog);
        dialog.show();
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button btn_join = dialog.findViewById(R.id.btn_join);
        final EditText edit_username = dialog.findViewById(R.id.edit_username);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit_username.getText().toString().length() == 0) {
                    edit_username.setError("Enter PUBG Username ");
                    edit_username.requestFocus();
                } else {
                    DbHelper dbHelper = new DbHelper(context);
                    final Result results = dbHelper.getUserData();
                    ServiceCaller serviceCaller = new ServiceCaller(context);
                    serviceCaller.callCheckPubgnameService(edit_username.getText().toString().trim(), results.getId(), resultList.get(i).getId(), new IAsyncWorkCompletedCallback() {
                        @Override
                        public void onDone(String workName, boolean isComplete) {
//                            Toast.makeText(context, workName.trim(), Toast.LENGTH_SHORT).show();
                            if (isComplete) {
                                if (!workName.trim().equalsIgnoreCase("no")) {
                                    dialog.dismiss();
                                    PlayFragment playFragment = PlayFragment.newInstance("", "");
                                    moveFragment(playFragment);
                                    final DbHelper dbHelper = new DbHelper(context);
                                    Result result = dbHelper.getUserData();
                                    ServiceCaller serviceCaller = new ServiceCaller(context);
                                    serviceCaller.callParticipatedMatchService(result.getId(), new IAsyncWorkCompletedCallback() {
                                        @Override
                                        public void onDone(String workName, boolean isComplete) {

                                        }
                                    });
                                    Toasty.success(context, "Joind").show();
                                } else {
                                    edit_username.requestFocus();
                                    edit_username.setError("Username Blocked By Khelpubg");
                                }
                            }
                        }
                    });
                }
            }
        });
    }


    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frgcontainer, fragment, "play")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyClass extends RecyclerView.ViewHolder {

        public ImageView image_game;
        public TextView tv_gametitle, tv_gametime, tv_winprice, tv_perkill, tv_entryfee, tv_type, tv_version, tv_map, tv_count, tv_info;
        public CardView cardView;
        Button bt_submit;
        SeekBar seekbar;

        public MyClass(View itemView) {
            super(itemView);
            image_game = itemView.findViewById(R.id.image_game);
            tv_gametitle = itemView.findViewById(R.id.tv_gametitle);
            tv_gametime = itemView.findViewById(R.id.tv_gametime);
            tv_winprice = itemView.findViewById(R.id.tv_winprice);
            tv_perkill = itemView.findViewById(R.id.tv_perkill);
            tv_entryfee = itemView.findViewById(R.id.tv_entryfee);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_version = itemView.findViewById(R.id.tv_version);
            tv_map = itemView.findViewById(R.id.tv_map);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_info = itemView.findViewById(R.id.tv_info);
            bt_submit = itemView.findViewById(R.id.bt_submit);
            cardView = itemView.findViewById(R.id.cardView);
            seekbar = itemView.findViewById(R.id.seekbar);

        }
    }

}
