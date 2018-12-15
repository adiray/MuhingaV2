package com.example.dell.muhingav2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize and assign views here
        ImageView housesButton = (ImageView) findViewById(R.id.houses_services_user_area);

        housesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                housesButtonClicked();
            }
        });

    }


    //This method is called when the houses button/image is clicked
    void housesButtonClicked() {
        //opens the houses activity
        Intent intent = new Intent(MainActivity.this, Houses.class);
        MainActivity.this.startActivity(intent);

    }


}

