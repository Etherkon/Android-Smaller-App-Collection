package com.liiketunniste.liiketunniste;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.media.MediaPlayer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button tarkistus;
    private TextView tila;
    private String vastaus;
    private ImageView alert;
    private CountDownTimer timer;
    private boolean aika;
    private boolean paikalla;

    public MediaPlayer tulo;
    public MediaPlayer lahto;

    private String osoite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tarkistus = (Button) findViewById(R.id.button);
        tila = (TextView) findViewById(R.id.tila);
        alert = (ImageView) findViewById(R.id.imageView);
        alert.setVisibility(View.INVISIBLE);
        aika = true;
        paikalla = false;
        tulo =MediaPlayer.create(MainActivity.this, R.raw.aani1);
        lahto =MediaPlayer.create(MainActivity.this, R.raw.aani2);
        osoite = "http://192.168.1.40";

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        timer = new CountDownTimer(3000, 20) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                try{
                    new GetData().execute(osoite);
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();

        tarkistus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aika) {
                    timer.cancel();
                    tarkistus.setText("Jatka");
                    aika = false;
                }
                else {
                    timer.start();
                    tarkistus.setText("Lopeta");
                    aika = true;
                }
            }
        });

        timer.start();
    }

    private class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            URL urlCould;
            HttpURLConnection connection;
            InputStream in = null;

            try {
                String url = params[0];
                urlCould = new URL(url);

                connection = (HttpURLConnection) urlCould.openConnection();
                System.out.println(url);

                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);
                connection.setRequestMethod("GET");
                connection.connect();

                in = connection.getInputStream();

                System.out.println("C");

            } catch (MalformedURLException MEx){

            } catch (IOException IOEx){
                System.out.println("NOOO");
                System.out.println(IOEx.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tila.setText("Yhteys pätkii...");
                    }
                });
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();

            try {
                // Pyydetään info

                vastaus = reader.readLine();
                vastaus = reader.readLine();
                vastaus = reader.readLine();
                System.out.println(vastaus);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(vastaus.charAt(0) == 'X') {
                            if(!paikalla) { tulo.start(); }
                            tila.setText("Liikettä havaittu !");
                            alert.setVisibility(View.VISIBLE);
                            paikalla = true;
                        }
                        else {
                            if(paikalla) { lahto.start(); }
                            tila.setText("Ei liikettä");
                            alert.setVisibility(View.INVISIBLE);
                            paikalla = false;
                        }
                    }
                });
            } catch (IOException e) {

                 System.out.println(e.toString());
                 e.printStackTrace();
            } finally {
                try {
                    in.close();
                    timer.start();
                } catch (IOException e) {
                    System.out.println(e.toString());
                    e.printStackTrace();
                }
           }
           return sb.toString();
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("TAG", result);
        }
    }

}
