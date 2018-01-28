package com.temperature.temperature;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {

    private TextView virhe;
    private Button huone1;
    private Button historia;
    private Button liike;
    private String ip;

    private String aste;
    private String prosentti;
    private String liikeTila;
    private boolean loki = true;
    private boolean graafi = false;
    private boolean liikeTarkistus = false;
    public MediaPlayer onLiike;
    public MediaPlayer eiLiike;
    public MediaPlayer l1k1;          // 1 = Lammin, 1 = ei kosteaa
    public MediaPlayer l0k1;
    public MediaPlayer l1k0;
    public MediaPlayer l0k0;

    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        huone1 = (Button) findViewById(R.id.button);
        historia = (Button) findViewById(R.id.button2);
        virhe = (TextView) findViewById(R.id.textView6);
        liike = (Button) findViewById(R.id.button3);
        onLiike = MediaPlayer.create(MainActivity.this, R.raw.liiketta);
        eiLiike = MediaPlayer.create(MainActivity.this, R.raw.eiliiketta);
        l1k1 = MediaPlayer.create(MainActivity.this, R.raw.l1k1);
        l1k0 = MediaPlayer.create(MainActivity.this, R.raw.l1k0);
        l0k1 = MediaPlayer.create(MainActivity.this, R.raw.l0k1);
        l0k0 = MediaPlayer.create(MainActivity.this, R.raw.l0k0);

        ip = "http://192.168.1.40/";

        huone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loki = false;
                liikeTarkistus = false;
                new GetData().execute(ip + "oma");
            }
        });

        historia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (graafi) {
                    graph.removeAllSeries();
                }
                loki = true;
                liikeTarkistus = false;
                new GetData().execute(ip + "historia");
            }
        });

        liike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loki = false;
                liikeTarkistus = true;
                new GetData().execute(ip + "liike");
            }
        });
    }

    private class GetData extends AsyncTask<String, Void, String> {

        private TextView lampotila;
        private TextView kosteus;

        private LineGraphSeries<DataPoint> series;

        @Override
        protected void onPreExecute() {
            lampotila = (TextView) findViewById(R.id.textView3);
            kosteus = (TextView) findViewById(R.id.textView4);
            graph = (GraphView) findViewById(R.id.graph);
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

                connection.setConnectTimeout(30000);
                connection.setReadTimeout(30000);
                connection.setRequestMethod("GET");
                connection.connect();

                in = connection.getInputStream();

                System.out.println("CC");

            } catch (MalformedURLException MEx){

            } catch (IOException IOEx){
                Log.e("Utils", "HTTP failed to fetch data");
                System.out.println(IOEx.toString());
                return null;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = "aloita";
            Vector pvmt = new Vector();
            Vector asteet = new Vector();
            String[] tokens;
            String[] lammot;
            String[] aika;
            String luvut = "";
            String ajat = "";
            boolean saatu = false;
            int summa = 0;

            System.out.println("Alku");

            try {
                // Pyydetään info

                if(!loki && !liikeTarkistus) {

                    System.out.println("Info");

                    aste = reader.readLine();
                    System.out.println(aste);
                    prosentti = reader.readLine();
                    System.out.println(prosentti);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lampotila.setText(aste + " C");
                            kosteus.setText(prosentti + " %");
                        }
                    });

                    if( Integer.valueOf(aste) >= 20 && Integer.valueOf(prosentti) >= 30){
                        l1k0.start();
                    }
                    else if( Integer.valueOf(aste) < 20 && Integer.valueOf(prosentti) >= 30){
                        l0k0.start();
                    }
                    else if( Integer.valueOf(aste) < 20 && Integer.valueOf(prosentti) < 30){
                        l0k1.start();
                    }
                    else if( Integer.valueOf(aste) >= 20 && Integer.valueOf(prosentti) < 30){
                        l1k1.start();
                    }

                    System.out.println("Info ilmoitettu");
                }

                else if(!loki && liikeTarkistus){
                    System.out.println("Onko liiketta ?");

                    liikeTila = reader.readLine();
                    System.out.println(liikeTila);
                    if(liikeTila.charAt(0) == 'X'){
                        onLiike.start();
                    }
                    else {
                        eiLiike.start();
                    }
                }

                // Piirretään graafi

                else {

                    System.out.println("Loki");

                    // Tulkitaan loki

                    while(line != null){
                        line = reader.readLine();
                        System.out.println(line);
                         if(line != null) {
                             tokens = line.split(" ");

                             for(String token : tokens){
                                 if(!saatu) { ajat = token; }
                                 saatu = true;
                                 luvut = token;
                             }

                             System.out.println("Saatu lampo " + luvut);

                             lammot = luvut.split(",");

                             System.out.println("Saatu aika " + lammot[0]);
                             aika = lammot[0].split("\\.");

                             summa = Integer.valueOf(aika[0]) * 100 + Integer.valueOf(aika[1]);

                             System.out.println("Aika " + summa);

                             asteet.add(new Integer(Integer.valueOf(lammot[1])));
                             pvmt.add(new Integer(summa));
                         }

                    }

                    int koko = asteet.size();
                    int koko2 = pvmt.size();

                    System.out.println("Loki saatu " + koko + " " + pvmt.size());

                    if(koko == 0){
                        series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint(0, 0)
                        });}
                    else if(koko == 1){
                            series = new LineGraphSeries<>(new DataPoint[] {
                            new DataPoint((Integer) pvmt.get(0), (Integer) asteet.get(0))
                    });}
                    else if(koko == 2){
                        series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint((Integer) pvmt.get(0), (Integer) asteet.get(0)),
                                new DataPoint((Integer) pvmt.get(1), (Integer) asteet.get(1))
                        });}
                    else if(koko == 3){
                        series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint((Integer) pvmt.get(0), (Integer) asteet.get(0)),
                                new DataPoint((Integer) pvmt.get(1), (Integer) asteet.get(1)),
                                new DataPoint((Integer) pvmt.get(2), (Integer) asteet.get(2))
                        });}
                    else if(koko == 4){
                        series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint((Integer) pvmt.get(0), (Integer) asteet.get(0)),
                                new DataPoint((Integer) pvmt.get(1), (Integer) asteet.get(1)),
                                new DataPoint((Integer) pvmt.get(2), (Integer) asteet.get(2)),
                                new DataPoint((Integer) pvmt.get(3), (Integer) asteet.get(3))
                        });}
                    else if(koko == 5){
                        series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint((Integer) pvmt.get(0), (Integer) asteet.get(0)),
                                new DataPoint((Integer) pvmt.get(1), (Integer) asteet.get(1)),
                                new DataPoint((Integer) pvmt.get(2), (Integer) asteet.get(2)),
                                new DataPoint((Integer) pvmt.get(3), (Integer) asteet.get(3)),
                                new DataPoint((Integer) pvmt.get(4), (Integer) asteet.get(4))
                        });}
                    else if(koko == 6){
                        series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint((Integer) pvmt.get(0), (Integer) asteet.get(0)),
                                new DataPoint((Integer) pvmt.get(1), (Integer) asteet.get(1)),
                                new DataPoint((Integer) pvmt.get(2), (Integer) asteet.get(2)),
                                new DataPoint((Integer) pvmt.get(3), (Integer) asteet.get(3)),
                                new DataPoint((Integer) pvmt.get(4), (Integer) asteet.get(4)),
                                new DataPoint((Integer) pvmt.get(5), (Integer) asteet.get(5))
                        });}
                    else if(koko == 7){
                        series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint((Integer) pvmt.get(0), (Integer) asteet.get(0)),
                                new DataPoint((Integer) pvmt.get(1), (Integer) asteet.get(1)),
                                new DataPoint((Integer) pvmt.get(2), (Integer) asteet.get(2)),
                                new DataPoint((Integer) pvmt.get(3), (Integer) asteet.get(3)),
                                new DataPoint((Integer) pvmt.get(4), (Integer) asteet.get(4)),
                                new DataPoint((Integer) pvmt.get(5), (Integer) asteet.get(5)),
                                new DataPoint((Integer) pvmt.get(6), (Integer) asteet.get(6))
                        });}
                    else if(koko == 8){
                        series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint((Integer) pvmt.get(0), (Integer) asteet.get(0)),
                                new DataPoint((Integer) pvmt.get(1), (Integer) asteet.get(1)),
                                new DataPoint((Integer) pvmt.get(2), (Integer) asteet.get(2)),
                                new DataPoint((Integer) pvmt.get(3), (Integer) asteet.get(3)),
                                new DataPoint((Integer) pvmt.get(4), (Integer) asteet.get(4)),
                                new DataPoint((Integer) pvmt.get(5), (Integer) asteet.get(5)),
                                new DataPoint((Integer) pvmt.get(6), (Integer) asteet.get(6)),
                                new DataPoint((Integer) pvmt.get(7), (Integer) asteet.get(7))
                        });}
                    else if(koko == 9){
                        series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint((Integer) pvmt.get(0), (Integer) asteet.get(0)),
                                new DataPoint((Integer) pvmt.get(1), (Integer) asteet.get(1)),
                                new DataPoint((Integer) pvmt.get(2), (Integer) asteet.get(2)),
                                new DataPoint((Integer) pvmt.get(3), (Integer) asteet.get(3)),
                                new DataPoint((Integer) pvmt.get(4), (Integer) asteet.get(4)),
                                new DataPoint((Integer) pvmt.get(5), (Integer) asteet.get(5)),
                                new DataPoint((Integer) pvmt.get(6), (Integer) asteet.get(6)),
                                new DataPoint((Integer) pvmt.get(7), (Integer) asteet.get(7)),
                                new DataPoint((Integer) pvmt.get(8), (Integer) asteet.get(8))
                        });}
                    else if(koko > 9){
                        series = new LineGraphSeries<>(new DataPoint[] {
                                new DataPoint((Integer) pvmt.get(koko2 - 10), (Integer) asteet.get(koko - 10)),
                                new DataPoint((Integer) pvmt.get(koko2 - 9)+0.1, (Integer) asteet.get(koko - 9)),
                                new DataPoint((Integer) pvmt.get(koko2 - 8)+0.2, (Integer) asteet.get(koko - 8)),
                                new DataPoint((Integer) pvmt.get(koko2 - 7)+0.3, (Integer) asteet.get(koko - 7)),
                                new DataPoint((Integer) pvmt.get(koko2 - 6)+0.4, (Integer) asteet.get(koko - 6)),
                                new DataPoint((Integer) pvmt.get(koko2 - 5)+0.5, (Integer) asteet.get(koko - 5)),
                                new DataPoint((Integer) pvmt.get(koko2 - 4)+0.6, (Integer) asteet.get(koko - 4)),
                                new DataPoint((Integer) pvmt.get(koko2 - 3)+0.7, (Integer) asteet.get(koko - 3)),
                                new DataPoint((Integer) pvmt.get(koko2 - 2)+0.9, (Integer) asteet.get(koko - 2)),
                                new DataPoint((Integer) pvmt.get(koko2 - 1)+0.9, (Integer) asteet.get(koko - 1))
                        });
                        System.out.println("Graafi lisätty");
                    }
                    System.out.println("Graafi luotu");
                    graafi = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            graph.addSeries(series);
                        }
                    });

                    System.out.println("Graafi piirretty");
                }

            } catch (IOException e) {
                System.out.println(e.toString());
                e.printStackTrace();
            } finally {
                try {
                    in.close();

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


