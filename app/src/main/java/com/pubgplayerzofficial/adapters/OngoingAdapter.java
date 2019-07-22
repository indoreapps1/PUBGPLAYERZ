package com.pubgplayerzofficial.adapters;

import android.app.Dialog;
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
import com.google.gson.Gson;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.database.DbHelper;
import com.pubgplayerzofficial.fragment.OngoingMatchViewFragment;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;
import com.pubgplayerzofficial.model.Result;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


public class OngoingAdapter extends RecyclerView.Adapter<OngoingAdapter.MyClass> {

    private Context context;
    private List<Result> resultList, passList;
    Result result;

    public OngoingAdapter(Context context, List resultList) {
        this.context = context;
        this.resultList = resultList;
        passList = new ArrayList<>();
    }


    @NonNull
    @Override
    public OngoingAdapter.MyClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_ongoing, viewGroup, false);
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
                OngoingMatchViewFragment ongoingMatchViewFragment = OngoingMatchViewFragment.newInstance(data, "");
                moveFragment(ongoingMatchViewFragment);
                showPopUp(i);
            }
        });
//        myClass.bt_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String url = "https://www.youtube.com/channel/UCukGnyFMzVwuJ1RDoDGCl2g";
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                if (intent.resolveActivity(context.getPackageManager()) != null) {
//                    context.startActivity(intent);
//                }
//            }
//        });
    }

    private void showPopUp(final int i) {
        DbHelper dbHelper = new DbHelper(context);
        Result resultd = dbHelper.getUserData();
        ServiceCaller serviceCaller = new ServiceCaller(context);
        serviceCaller.callcheckongoinpopService(resultd.getId(), resultList.get(i).getId(), new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    if (!workName.trim().equalsIgnoreCase("no")) {
                        if (resultList.get(i).getUser_name() != null && !resultList.get(i).getUser_name().trim().equals("")) {
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.custom_ongoingusr_dialog);
                            TextView textView = dialog.findViewById(R.id.tv_username);
                            TextView password = dialog.findViewById(R.id.tv_passwod);
                            Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
                            Button btn_submit = dialog.findViewById(R.id.btn_join);
                            btn_cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            btn_submit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    openApplication(context, "com.tencent.ig");
//                                        Toasty.error(context, "Pubg have not been installed.").show();
                                    dialog.dismiss();
                                }
                            });
                            textView.setText(resultList.get(i).getUser_name());
                            password.setText(resultList.get(i).getPassword());
                            dialog.show();
                        }
                    }
                } else {
                    Toasty.error(context, "Error Try Again").show();
                }
            }
        });


    }

    public void openApplication(Context context, String packageN) {
        Intent i = context.getPackageManager().getLaunchIntentForPackage(packageN);
        if (i != null) {
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
        } else {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageN)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageN)));
            }
        }
    }

    public void moveFragment(Fragment fragment) {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frgcontainer, fragment, "ongoing")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class MyClass extends RecyclerView.ViewHolder {

        public ImageView image_game;
        public TextView tv_gametitle, tv_gametime, tv_winprice, tv_perkill, tv_entryfee, tv_type, tv_version, tv_map;
        public CardView cardView;
//        Button bt_submit;

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
//            bt_submit = itemView.findViewById(R.id.bt_submit);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }

}
