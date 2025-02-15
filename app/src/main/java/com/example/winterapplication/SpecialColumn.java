package com.example.winterapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SpecialColumn extends AppCompatActivity {

    private TextView BackTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialcolumn);

        initView();
        initClick();
    }

    private void initClick() {
        BackTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SpecialColumn.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initView() {
        BackTextView=findViewById(R.id.net_back);

    }
}
