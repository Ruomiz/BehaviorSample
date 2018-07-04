package com.ruomiz.coordinatorheader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btFirst = findViewById(R.id.bt_first);
        Button btSecond = findViewById(R.id.bt_second);
        btFirst.setOnClickListener(this);
        btSecond.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_first:
                turnToMarket();
                break;
            case R.id.bt_second:
                turnSearchBar();
                break;
            default:
                turnToMarket();
                break;
        }
    }

    private void turnToMarket() {
        Intent intent = new Intent(this, MarketActivity.class);
        startActivity(intent);
    }

    private void turnSearchBar() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
