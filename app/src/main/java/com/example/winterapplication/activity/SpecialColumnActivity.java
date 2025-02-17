package com.example.winterapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.winterapplication.R;

public class SpecialColumnActivity extends AppCompatActivity {

    private TextView BackTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialcolumn);

        initView();
        initClick();
    }

    private void initClick() {
        BackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SpecialColumnActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initView() {
        BackTextView = findViewById(R.id.net_back);

    }
}
