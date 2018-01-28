package com.radio.radio;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Button buttonPlay;
    private Button buttonStopPlay;
    private MediaPlayer player;
    private Spinner spinner1;
    private String kanava = "http://stream3.bauermedia.fi/nova.mp3";

    private String nova = "http://stream3.bauermedia.fi/nova.mp3";
    private String city = "http://stream2.bauermedia.fi/rc-hki.mp3";
    private String rock = "http://icelive0.80692-icelive0.cdn.qbrick.com/10565/80692_RadioRock.mp3";
    private String nostalgia = "http://cdn.nrjaudio.fm/adwz1/fi/35059/mp3_128.mp3";
    private String sputnik = "http://audio.raa.fi:8000/radiosputnik.m3u";
    private String helmi = "http://icelive0.80692-icelive0.cdn.qbrick.com/10567/80692_Helmiradio.mp3";
    private String basso = "http://stream.basso.fi:8000/stream";
    private String kiss = "http://stream2.bauermedia.fi/kiss.mp3";
    private String ihyvinkaa = "http://stream2.bauermedia.fi/isk-hyv.mp3";
    private String itampere = "http://stream2.bauermedia.fi/isk-tre.mp3";
    private String ihelsinki = "http://stream2.bauermedia.fi/isk-hki.mp3";

    private String[] kanavat = { "Radio Nova", "Radio City","Radio Rock","Radio Nostalgia","Radio Sputnik",
                                 "Helmiradio","Bassoradio","Kiss",
                                 "Iskelmä Hyvinkää","Iskelmä Tampere","Iskelmä Helsinki"};
    private ArrayAdapter<String> rkanavat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUIElements();
        source();
        initializeMediaPlayer();
    }

    private void initializeUIElements() {

        spinner1 = (Spinner) findViewById(R.id.spinner);
        rkanavat = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kanavat);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kanavat);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter2, View v,
                                       int position, long id) {
                if (position == 0) {
                    kanava = nova;
                } else if (position == 1) {
                    kanava = city;
                }else if (position == 2) {
                    kanava = rock;
                } else if (position == 3) {
                    kanava = nostalgia;
                } else if (position == 4) {
                    kanava = sputnik;
                } else if (position == 5) {
                    kanava = helmi;
                } else if (position == 6) {
                    kanava = basso;
                } else if (position == 7) {
                    kanava = kiss;
                } else if (position == 8) {
                    kanava = ihyvinkaa;
                } else if (position == 9) {
                    kanava = itampere;
                } else if (position == 10) {
                    kanava = ihelsinki;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonPlay.setOnClickListener(this);

        buttonStopPlay = (Button) findViewById(R.id.buttonStopPlay);
        buttonStopPlay.setEnabled(false);
        buttonStopPlay.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v == buttonPlay) {
            try{
            startPlaying();}
            catch (Error e) {
                System.out.println(e.toString());
            }
        } else if (v == buttonStopPlay) {
            stopPlaying();
        }
    }

    private void startPlaying() {
        buttonStopPlay.setEnabled(true);
        buttonPlay.setEnabled(false);
        source();
        player.prepareAsync();

        player.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                player.start();
            }
        });
    }

    private void stopPlaying() {
        if (player.isPlaying()) {
            player.stop();
            player.release();
            initializeMediaPlayer();
        }
        buttonPlay.setEnabled(true);
        buttonStopPlay.setEnabled(false);
    }

    private void source(){
        player = new MediaPlayer();
        try {
            player.setDataSource(kanava);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } catch (IllegalStateException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }

    private void initializeMediaPlayer() {

        player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                Log.i("Buffering", "" + percent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player.isPlaying()) {
            player.stop();
        }
    }


}
