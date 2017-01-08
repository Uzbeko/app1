package com.example.uzbeko.service_test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Ed on 8/11/2015.
 */
public class Servisas extends Service {
    Sax_implement sax;
    Handler handl;
    Thread gija;
    private final MyBinder bind = new MyBinder();
    int counter = 0;
    boolean runn =true;
    LocalBroadcastManager lbm2;
    int startID;
    String old_public_data;
///-----------------------------------------------
//==test Serializable ==========================
    public class DataWrap implements Serializable {
          private ArrayList<Items> dataParserW;
        public DataWrap(ArrayList<Items> items){
            dataParserW = items;
        }
        public ArrayList<Items> getData(){
            return dataParserW;
        }
    }
//==parcelable class============================

//==============================================
    @Override
    public void onDestroy() {
        super.onDestroy();
        runn = false;
        this.stopSelf(startID);                        System.out.println("servisas onStartCommand int startId: " + startID);
        System.out.println("CCCCCCC: service  onDestroy is caled from SERVISAS");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("CCCCCCC: service  onUnbind is caled from SERVISAS");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return bind;
    }

    @Override
    public void onCreate() {

        System.out.println("servisas sukurtas");
        sax = new Sax_implement();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handl = new Handler();
        this.startID = startId;
    //------------------------
        gija = new Thread() { //pati gija veikia salutinei gijoje sercice

            @Override
            public void run() {
                while (runn) {

                    try {
                        this.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("TTTTTT sleap 10s: ");
        //################################################################################################################################
                    handl.post(new Runnable() { // metodas veikia pagrindinei gijoi kur ir turetu  buti  atvaidujamas  vaizdas

                        @Override
                        public void run() {

                            try {
//                                sax.goFeed(getResources().openRawResource(R.raw.test));                       //go parsing
//                                URL url = new URL("http://www.abc.net.au/news/feed/51120/rss.xml");
//                                URL url = new URL("http://www.15min.lt/rss");
//                                URL url = new URL("http://feeds.bbci.co.uk/news/rss.xml?edition=uk");
                                URL url = new URL("http://lorem-rss.herokuapp.com/feed?unit=second&interval=30");
                                sax.goFeed(url.openStream());                           //atidarau puslapi feedu ir paduodu srauta parseriui
                            } catch (ParserConfigurationException e) {
                                e.printStackTrace();
                            } catch (SAXException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                System.out.println("FFFFFFF: ivykde finally");
                            }
                                Toast.makeText(getBaseContext(), "parser working: " , Toast.LENGTH_SHORT).show();

                            //--LocalBraodcastManager-------perduodu duomenis-parsintus i activity1
                                System.out.println("################################################################################### handl.post: ");
                                Intent intent = new Intent();
                                intent.setAction("MyAction");
//                                intent.putExtra("value", ++counter + " | " + sax.getParseResult());
                                intent.putExtra("title"," | " + sax.getParseData().get(0).getTitle());
                                intent.putExtra("pubDate", " | " + sax.getParseData().get(0).getPubDate());
                                intent.putExtra("thumbnail", " | " + sax.getParseData().get(0).getthumbnail());
                                intent.putExtra("link", " | " + sax.getParseData().get(0).getLink());
                                intent.putExtra("ArrayList", new Servisas.DataWrap (sax.getParseData()));

                                lbm2 = LocalBroadcastManager.getInstance(getBaseContext());
                                lbm2.sendBroadcast(intent);                                          //--sius intent tik mano programeles viduje delto nekils saugumo problemu kad perims kiti

                            //---kuriu intenta (prisegu parsintus duomenis) ir ji perduodu i aktivity2
                            ArrayList<Items> filtered_data = Servisas.this.filter_parsed(sax.getParseData());
                                if(!filtered_data.isEmpty()) {                                      //jei turim nuju feedu tada tik vykdom
                                    old_public_data = filtered_data.get(0).getPubDate();
                                    Log.d("old_public_data: ", old_public_data);
                                    Intent i = new Intent(getApplicationContext(), Activity_2.class);
                                    i.putParcelableArrayListExtra("rawData", filtered_data);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

                                    //---------------------------------------------------------------------

                                    Notification.Builder noteBuild = new Notification.Builder(getBaseContext());
                                    noteBuild.setContentText("zinutes tekstas");
                                    noteBuild.setContentTitle("zinutes pavadinimas");
                                    noteBuild.setSmallIcon(R.drawable.skype_small);
                                    noteBuild.setContentIntent(pendingIntent);
                                    noteBuild.setAutoCancel(true);
                                    Notification notificationObj = noteBuild.getNotification();

                                    //--------garsas
                                    notificationObj.defaults |= Notification.DEFAULT_SOUND;
                                    notificationObj.defaults |= Notification.DEFAULT_VIBRATE;

                                    NotificationManager noteManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    noteManager.notify(7, notificationObj);
                                }
                        }
                    });
                }
            }
        };
        gija.start();

        return START_STICKY;
    }
//--------------------------------------------------------------------------------------------------

    //--bind class---------------------------
    class MyBinder extends Binder {
        public Servisas getServisasInst(){
            return Servisas.this;
        }
    }

    //check is it new------------------------
    public ArrayList<Items> filter_parsed(ArrayList<Items> arr){

        if(old_public_data == null){
            return arr;
        }else{
            for(int i=0; i<arr.size();i++){
                if(arr.get(i).getPubDate().equals(old_public_data)){
                    arr.subList(i,arr.size()).clear();;
                }
            }
            return arr;
        }

    }

}