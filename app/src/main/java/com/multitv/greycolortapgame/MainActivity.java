package com.multitv.greycolortapgame;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.multitv.greycolortapgame.viewmodels.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MainActivityViewModel activityViewModel;
    private TextView scoreCounter;
    private Drawable greyDrawable;
    private AlertDialog alertDialog;

    private List<ExpectedViewHolder> viewHolderList = new ArrayList<ExpectedViewHolder>() {
        {
            for (int i = 0; i < 4; i++)
                add(new ExpectedViewHolder());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activityViewModel.updateCounter(true);
            }
        });
        alertDialog = alertDialogBuilder.create();
        scoreCounter = findViewById(R.id.scoreCounter);
        viewHolderList.get(0).view = findViewById(R.id.red);
        viewHolderList.get(0).oldBg = AppCompatResources.getDrawable(this, R.color.red);

        viewHolderList.get(1).view = findViewById(R.id.yellow);
        viewHolderList.get(1).oldBg = AppCompatResources.getDrawable(this, R.color.yellow);

        viewHolderList.get(2).view = findViewById(R.id.green);
        viewHolderList.get(2).oldBg = AppCompatResources.getDrawable(this, R.color.green);

        viewHolderList.get(3).view = findViewById(R.id.blue);
        viewHolderList.get(3).oldBg = AppCompatResources.getDrawable(this, R.color.blue);

        greyDrawable = AppCompatResources.getDrawable(this, R.color.grey);

        for (ExpectedViewHolder vh : viewHolderList) {
            vh.view.setOnClickListener(this);
        }
        activityViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MainActivityViewModel.class);
        activityViewModel.getElapsedTime().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                int rand = activityViewModel.generateRandomNumber();
                for (ExpectedViewHolder vh : viewHolderList) {
                    vh.view.setBackground(vh.oldBg);
                }
                viewHolderList.get(rand).view.setBackground(greyDrawable);
            }
        });

        activityViewModel.getScoreCounterData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer count) {
                scoreCounter.setText(String.valueOf(count));
            }
        });

        activityViewModel.getDisplayAlert().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean display) {
                if (display)
                    alertDialog.show();
                else
                    alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getBackground() == greyDrawable) {
            activityViewModel.updateCounter(false);
        } else {
            activityViewModel.updateCounter(true);
            activityViewModel.displayAlert(true);
        }
    }

    private static class ExpectedViewHolder {
        View view;
        Drawable oldBg;
    }
}