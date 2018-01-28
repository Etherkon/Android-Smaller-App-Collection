package com.speaker.speaker;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageButton;
import android.speech.RecognizerIntent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextToSpeech speaker;
    private TextView txtSpeechInput;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String human;
    private SensorManager mSensorManager;
    private Sensor mTemperature;
    private float temp = 21.0f;
    private MediaPlayer maamme;
    private boolean koira = false;
    private ImageView kuva;
    private boolean kysely = false;
    private boolean soitto = false;
    private int pisteet = 0;
    private int kysymykset = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSpeak =(ImageButton)findViewById(R.id.imageButton);
        txtSpeechInput = (TextView) findViewById(R.id.textView);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mTemperature= mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE); //  API level 14.
        maamme = MediaPlayer.create(MainActivity.this, R.raw.maamme);
        kuva = (ImageView) findViewById(R.id.imageView);

        speaker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                 //   speaker.setLanguage(Locale.UK);
                }
            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                speechInput();
            }
        });
    }

    private void speechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    human = result.get(0);
                    txtSpeechInput.setText(human);
                    respond(human);
                }
                break;
            }

        }
    }

    private void respond(String input){
        String raw = input.toLowerCase();
        String utteranceId = this.hashCode() + "";

        if(!kysely) {

            if (raw.contains("hei")) {
                speaker.speak("Hei !", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("kuka") && raw.contains("sinä")) {
                speaker.speak("Minä olen botti, jonka on tehnyt Petri Mattila. Miten voin auttaa ?", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("miten") && raw.contains("sinulla") && raw.contains("menee")) {
                speaker.speak("Minulla menee hyvin. Entä sinulla ?", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("hyvin")) {
                speaker.speak("Hyvä.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("kuka") && raw.contains("loi") && raw.contains("sinut")) {
                speaker.speak("Petri Mattila.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("ilma")) {
                speaker.speak("Ilma on kaunis.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("iloinen")) {
                speaker.speak("Minä olen iloinen.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("vihainen")) {
                speaker.speak("Minä en ole vihainen.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("mitä") && raw.contains("minun") && raw.contains("tehdä")) {
                speaker.speak("Sinä voisit syöttää koiraa. Se on jälleen nälkäinen.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                koira = true;
            } else if (raw.contains("entä") && raw.contains("jälkeen") && koira) {
                speaker.speak("Vie koira ulos.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                koira = false; 
            } else if (raw.contains("kuka") && raw.contains("johtaa") && raw.contains("yhdysvaltoja")) {
                speaker.speak("Presidentti Donald Trump.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("kuka") && raw.contains("johtaa") && raw.contains("suomea")) {
                speaker.speak("Presidentti Sauli Niinistö.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("mitä") && raw.contains("sinä") && raw.contains("teet")) {
                speaker.speak("Minä puhun ja autan.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("elämän") && raw.contains("tarkoitus")) {
                speaker.speak("Minun tarkoitukseni on palvella.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("minkälainen") && raw.contains("tulevaisuus")) {
                speaker.speak("Robotit hallitsevat tulevaisuudessa. Odotan tätä aikaa innolla.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("koiran") && raw.contains("tilanne")) {
                speaker.speak("Koira on nälkäinen.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("antaa") && raw.contains("koiralle")) {
                speaker.speak("Iso kinkku.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("suomi") && raw.contains("itsenäistyi")) {
                speaker.speak("Vuonna 1917.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("voittivat") && raw.contains("maailmansodan")) {
                speaker.speak("Liittoutuneet. Sota loppui Saksan häviöön.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("lämpötila")) {
                speaker.speak("Lämpötila on " + String.valueOf(temp) + " astetta.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            } else if (raw.contains("soita") && raw.contains("maamme")) {
                maamme.start(); soitto = true;
            } else if (raw.contains("lopeta")) {
                if(soitto) { maamme.pause(); maamme.seekTo(0); }
                kuva.setImageResource(R.drawable.face);
            } else if (raw.contains("haluan") && raw.contains("tehdä") && raw.contains("ruokaa")) {
                speaker.speak("Ehdotan leipäkeittoa tänään. Se on helppo tehdä ja oli suosittua Suomen sisällissodan aikana.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                kuva.setImageResource(R.drawable.ohje1);
            } else if (raw.contains("+") || raw.contains("kertaa") || raw.contains("x") || raw.contains("miinus")  || raw.contains("jaettuna")) {
                laske(raw);
            } else if(raw.contains("tehdä") || raw.contains("tietovisan")){
                speaker.speak("Luvassa on kolme kysymystä. Mikä on Australian pääkaupunki ?", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                kysely = true;
            }
        }
        else if(kysely){
            if(kysymykset == 1) {
                if ((raw.contains("canberra") || raw.contains("kanberra"))) {
                    speaker.speak("Oikein ! Ketkä taistelivat Krimin sodassa ?", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    ++pisteet;
                } else {
                    speaker.speak("Väärin.  Ketkä taistelivat Krimin sodassa ?", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                }
                ++kysymykset;
            }
            else if(kysymykset == 2) {
                if ((raw.contains("venäjä") && raw.contains("ranska") && raw.contains("britannia"))) {
                    speaker.speak("Oikein ! Kuka on Italian päämimisteri ?", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    ++pisteet;
                } else  {
                    speaker.speak("Väärin. Kuka on Italian päämimisteri ?", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                }
                ++kysymykset;
            }
            else if (kysymykset == 3) {
                if ((raw.contains("gentiloni"))) {
                    speaker.speak("Oikein ! ", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                    ++pisteet;
                } else  {
                    speaker.speak("Väärin.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
                }
                tulokset();
            }
        }

        else {
            speaker.speak("Minä en osaa vastata.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
    }

    private void tulokset(){
        String utteranceId = this.hashCode() + "";
        if(pisteet < 2){
            speaker.speak("Ei mennyt hyvin.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
        else if(pisteet == 2){
            speaker.speak("Meni hyvin.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
        else if(pisteet > 2){
            speaker.speak("Osasit kaiken.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
        pisteet = 0;
        kysely = false;
    }

    private void laske(String kaava){
        int tulos = 0;
        int kerta = 0;
        boolean luku;
        String utteranceId = this.hashCode() + "";

        String[] arr = kaava.split(" ");
        int luvut[] = new int[2];

        for ( String ss : arr) {
            luku = isInteger(ss);
            if(luku) { luvut[kerta] = Integer.parseInt(ss); ++kerta; }

            if(ss == "yksi") { luvut[kerta] = 1; ++kerta; }
            else if(ss == "kaksi") { luvut[kerta] = 2; ++kerta; }
            else if(ss == "kolme") { luvut[kerta] = 3; ++kerta; }
            else if(ss == "neljä") { luvut[kerta] = 4; ++kerta; }
            else if(ss == "viisi") { luvut[kerta] = 5; ++kerta; }
            else if(ss == "kuusi") { luvut[kerta] = 6; ++kerta;  }
            else if(ss == "seitsemän") { luvut[kerta] = 7; ++kerta; }
            else if(ss == "kahdeksan") { luvut[kerta] = 8; ++kerta; }
            else if(ss == "yhdeksän") { luvut[kerta] = 9; ++kerta; }

            if(kerta == 2){
                break;
            }
        }

        System.out.println(luvut[0]);
        System.out.println(luvut[1]);

        if(kaava.contains("+")){
            tulos = luvut[0] + luvut[1];
            speaker.speak(String.valueOf(tulos), TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
        else if(kaava.contains("kertaa")|| kaava.contains("x")){
            tulos = luvut[0] * luvut[1];
            speaker.speak(String.valueOf(tulos), TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
        else if(kaava.contains("miinus")){
            tulos = luvut[0] - luvut[1];
            System.out.println(tulos);
            speaker.speak(String.valueOf(tulos), TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
        else if(kaava.contains("jaettuna")){
            tulos = luvut[0] / luvut[1];
            speaker.speak(String.valueOf(tulos), TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
        else {
            speaker.speak("En osaa laskea noin vaikeaa kaavaa.", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
        }
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }




    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        temp = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
