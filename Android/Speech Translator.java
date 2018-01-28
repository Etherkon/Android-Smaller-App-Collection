package com.translator.translator;

import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.content.Context;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import org.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {

    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private String TAG = MainActivity.class.getSimpleName();
    private Locale dstLanguage = Locale.ENGLISH;
    private Locale srcLanguage = Locale.ROOT;
    private Spinner spinner1;
    private String human;
    private String[] countries = {"English", "French","German","Japanese","Chinese"};
    private ArrayAdapter<String> trans_adapter;
    private TextToSpeech speaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        btnSpeak =(ImageButton)findViewById(R.id.imageButton);
        spinner1 = (Spinner) findViewById(R.id.spinner);

        speaker = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter2, View v,
                                       int position, long id) {
                if (position == 0) {
                    dstLanguage = Locale.ENGLISH;
                    speaker.setLanguage(Locale.UK);
                } else if (position == 1) {
                    dstLanguage = Locale.FRENCH;
                    speaker.setLanguage(Locale.FRANCE);
                } else if (position == 2) {
                    dstLanguage = Locale.GERMAN;
                    speaker.setLanguage(Locale.GERMANY);
                } else if (position == 3) {
                    dstLanguage = Locale.JAPANESE;
                    speaker.setLanguage(Locale.JAPAN);
                }else if (position == 4) {
                    dstLanguage = Locale.CHINESE;
                    speaker.setLanguage(Locale.CHINA);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
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

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    human = result.get(0);
                    translate(human);
                }
                break;
            }
        }
    }

    public String translate(String text) {
        String translated = null;
        String utteranceId = this.hashCode() + "";
        try {
            String query = URLEncoder.encode(text, "UTF-8");
            String langpair = URLEncoder.encode(srcLanguage.getLanguage()+"|"+dstLanguage.getLanguage(), "UTF-8");
            String url = "http://mymemory.translated.net/api/get?q="+query+"&langpair="+langpair;
            HttpClient hc = new DefaultHttpClient();
            HttpGet hg = new HttpGet(url);
            HttpResponse hr = hc.execute(hg);
            if(hr.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                JSONObject response = new JSONObject(EntityUtils.toString(hr.getEntity()));
                translated = response.getJSONObject("responseData").getString("translatedText");
                speaker.speak(String.valueOf(translated), TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return translated;
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = speaker.setLanguage(dstLanguage);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e(TAG, "Language data is missing or the language is not supported.");
            } else {
                Log.i(TAG, "Ready to use Text to Speech service");}
        } else {
            Log.e(TAG, "Could not initialize TextToSpeech.");
        }
    }


}
