package com.example.uzbeko.service_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ed on 8/22/2015.
 */
public class Activity_2 extends Activity {

    TextView textview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2_layout);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "onPAUSE from Activity_2.Class", Toast.LENGTH_LONG).show();
        System.out.println("RRRRRRRRRR onPAUSE from Activity_2.Class ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "onResume from Activity_2.Class", Toast.LENGTH_LONG).show();

        ArrayList<Items> myList = this.getIntent().getParcelableArrayListExtra("rawData");
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(new ListAdapter(myList));

    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "onStop from Activity_2.Class", Toast.LENGTH_LONG).show();
        System.out.println("RRRRRRRRRR onStop from Activity_2.Class ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "onDestroy from Activity_2.Class", Toast.LENGTH_LONG).show();
        System.out.println("RRRRRRRRRR onDestroy from Activity_2.Class ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getApplicationContext(), "onRestart from Activity_2.Class", Toast.LENGTH_LONG).show();
        System.out.println("RRRRRRRRRR onRestart from Activity_2.Class ");
    }
}
