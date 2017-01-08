package com.example.uzbeko.service_test;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.ed.service_test.R.*;

public class My extends Activity {

    LocalBroadcastManager lbm1;
    BroadcastReceiver br;
    boolean unbind_on_off = false;
    boolean receiver_on_off = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

    //---leidimas jungtis  prie interneto (jei api >8)-------------------
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    //--Buttons---------------------------------------------
        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Activity_2.class));

            }
        });
        //-----------------------------------------------
        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              destroi_lbm1_br();
              System.out.println("CCCCCCC: service close  is caled: " + getBaseContext().stopService(new Intent(getBaseContext(), Servisas.class)));

                SharedPreferences sp = getApplicationContext().getSharedPreferences("ED", MODE_PRIVATE);
                SharedPreferences.Editor editor =  sp.edit();
                editor.putString("reiksme", "7");
                editor.commit();

            }
        });
        //---------------------------------------------
        Button button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(!isMyServiceRunning(Servisas.class)) {
                    Intent i = new Intent(getBaseContext(), Servisas.class);  //Starter service
                    startService(i);
                    bindService(i, svc, Context.BIND_AUTO_CREATE);

                    //---LocalBraodcastManager registuojam RECEIVER---------------------------------

                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("MyAction");
                    intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

                    lbm1 = LocalBroadcastManager.getInstance(getBaseContext());
                    br = new BroadcastReceiver() {

                        @Override
                        public void onReceive(Context context, Intent intent) {

                            Servisas.DataWrap result = (Servisas.DataWrap) intent.getSerializableExtra("ArrayList");

                            System.out.println("Test receive from arraylis: " + result.getData().size());


                            Bundle extras = intent.getExtras();
                            if (extras != null) {
                                System.out.println("RRRRRRRRRR onReceive() gavau kazka is serviso ");
                            }
                            TextView title = (TextView) findViewById(R.id.title_id);
                            title.setText((String) extras.get("title"));

                            TextView pubDate = (TextView) findViewById(R.id.pubDate_id);
                            pubDate.setText((String) extras.get("pubDate"));

                            TextView thumbnail = (TextView) findViewById(R.id.thumbnail_id);
                            thumbnail.setText((String) extras.get("thumbnail"));

                        }
                    };

                    lbm1.registerReceiver(br, intentFilter);
                    receiver_on_off = true;
            }
            }
        });
        //-------------------------------------------------
        Button button4 = (Button)findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"isMyServiceRunning? :"+ Boolean.toString(isMyServiceRunning(Servisas.class)), Toast.LENGTH_SHORT).show();

//            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.perlgurl.org") );
//            startActivity(in);

            SharedPreferences sp = getApplicationContext().getSharedPreferences("ED",MODE_PRIVATE);
                System.out.println("444444 is Shared preferences: "+ sp.getString("reiksme","notExist"));


            }
        });

    //--Buttons END----------------------------------------
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getBaseContext(), "onPause from MY.Class", Toast.LENGTH_LONG).show();
        System.out.println("RRRRRRRRRR onPause from MY.Class ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getBaseContext(), "onResume from MY.Class", Toast.LENGTH_LONG).show();
        System.out.println("RRRRRRRRRR onResume from MY.Class ");

        if(isMyServiceRunning(Servisas.class)) {
            start_BInd_Receiv();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getBaseContext(), "onDestroy from MY.Class", Toast.LENGTH_LONG).show();
        System.out.println("RRRRRRRRRR onDestroy from MY.Class ");

        destroi_lbm1_br();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(getBaseContext(), "onRestart from MY.Class", Toast.LENGTH_LONG).show();
        System.out.println("RRRRRRRRRR onRestart from MY.Class ");

        start_BInd_Receiv();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getBaseContext(), "onSTOP from MY.Class", Toast.LENGTH_LONG).show();
        System.out.println("RRRRRRRRRR onSTOp from MY.Class ");

        destroi_lbm1_br();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_my, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
//--- start LocalBraodcastManager registuojam RECEIVER----------------------------------------------
    public void start_BInd_Receiv() {
        if(!unbind_on_off && !receiver_on_off){
            //--- start LocalBraodcastManager registuojam RECEIVER----------------------------------

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("MyAction");
            intentFilter.addCategory(Intent.CATEGORY_DEFAULT);

            lbm1 = LocalBroadcastManager.getInstance(getBaseContext());
            br = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {

                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        System.out.println("RRRRRRRRRR onReceive() gavau intent is serviso: " + (String) extras.get("ArrayList"));
                    }
                    TextView title = (TextView) findViewById(R.id.title_id);
                    title.setText((String) extras.get("title"));

                    TextView pubDate = (TextView) findViewById(R.id.pubDate_id);
                    pubDate.setText((String) extras.get("pubDate"));

                    TextView thumbnail = (TextView) findViewById(R.id.thumbnail_id);
                    thumbnail.setText((String) extras.get("thumbnail"));
                }
            };

            lbm1.registerReceiver(br, intentFilter);
            receiver_on_off = true;
        }
    }
//-- destroi receiver and unbind--------------------------------------------------------------------
    public void destroi_lbm1_br(){

        if (receiver_on_off) {
            lbm1.unregisterReceiver(br);                                    System.out.println("CCCCCCC: service  unregisterReceiver(br);   is caled: " + br);
            receiver_on_off = false;
        }
        if (unbind_on_off) {
            getBaseContext().unbindService(svc);                            System.out.println("CCCCCCC: service  unbindService(svc) is caled: " + svc);
            unbind_on_off = false;
        }

    }
//--ar mano servisas veikia-------------------------------------------------------------------------
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
//--------------------------------------------------------------------------------------------------
//---Service conection interface for binder---------------------------------------------
    private ServiceConnection svc = new ServiceConnection() { // tai interfeisas!!! kuri inplementuoja anonymous klase. Metodai (callback) bus iskviesti kai prisijungsime prie serviso
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            unbind_on_off = true;
            final Servisas.MyBinder bind = (Servisas.MyBinder)service;                              System.out.println("tttttttttttttt: prisijunge Activity prie serviso =>  onServiceConnected() is caled: ");

//            Object [] result = bind.getResultSax();
//
//            String e = ((StringBuffer) result[0]).toString();
//
//------Atskira gija duomenim paimti---------------------------------------------------------
//            final Handler han = new Handler();
//
//            Thread g = new Thread(){
//                @Override
//                public void run() {
//
//                    super.run();
//                    han.postDelayed(new Runnable() {
//                        Object[] result = bind.getObArray();
//                        @Override
//                        public void run() {
//
//                            synchronized(bind.getDataResult()) {
//
//                                if (bind.getDataResult().getResult() == "null") {
//                                    System.out.println("!!!!!!!!!! uzmigo ");
//                                   Toast.makeText(getBaseContext(), "null !!!!", Toast.LENGTH_LONG).show();
////                                    title.setText(bind.getResultSax());
//
//                                    try {
//                                        System.out.println("!!!!!!!!!! kvieciamas Wait() ");
//                                        bind.getDataResult().wait();
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                    title.setText(bind.getDataResult().getResult()+" : issauktas  po wait");
//                                } else {
//                                    title.setText(bind.getDataResult().getResult());
//                                }
//                            }
//                        }
//                    },10000);
//                }
//            };
//            g.start();
//-------------------------------------------------------------------------------------------------
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            unbind_on_off = false;
            System.out.println("tttttttttttttt: Activity atsijunge nuo serviso =>  onServiceDisconnected is caled: ");
        }
    };
//--------------------------------------------------------------------------------------
}
