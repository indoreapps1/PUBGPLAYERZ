package com.pubgplayerzofficial.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

import com.pubgplayerzofficial.R;


public class DoneDialog extends AlertDialog {


    public DoneDialog(Context context) {
        super(context);
        setvalue();
    }

    private void setvalue() {
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setTitle(null);
        setOnCancelListener(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custion_done_dialog);
        setCanceledOnTouchOutside(false);
    }

}
