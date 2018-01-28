package com.call.phonecall;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;

import android.telephony.SmsManager;


public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private static final int MY_PERMISSIONS_CALL_PHONE =1 ;

    private Button soita;

    private Button tekstiviesti;
    private Button sposti;

    private EditText viesti;
    private String lahetettava;

    private String phoneNo = "";
    private String sosoite = "";

    private String Je = "";
    private String Ju = "";
    private String Or = "";

    private String jeosoite = "";
    private String juosoite = "";
    private String orosoite ="";

    private boolean onnistui = false;

    private Spinner spinner1;
    private String[] soitettavat = {"Je","Ju","Or"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        soita = (Button) findViewById(R.id.button);
        tekstiviesti = (Button) findViewById(R.id.button3);
        sposti = (Button) findViewById(R.id.button5);
        viesti = (EditText) findViewById(R.id.editText);
        spinner1 = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, soitettavat);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner1.setAdapter(adapter);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter2, View v,
                                       int position, long id) {
                if (position == 0) {
                    phoneNo = Je;
                    sosoite = jeosoite;
                } else if (position == 1) {
                    phoneNo = Juk;
                    sosoite = juosoite;
                } else if (position == 2) {
                    phoneNo = Or;
                    sosoite = orosoite;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        soita.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                soita();
            }
        });

        tekstiviesti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                System.out.println("TV-nappi");
                tekstiviesti();
            }
        });

        sposti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sposti();
            }
        });
    }

    protected void tekstiviesti() {
        lahetettava = viesti.getText().toString();

        System.out.println("Viesti luettu");

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("0");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                System.out.println("A");
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
                System.out.println("B");
            }
        }
        else {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, lahetettava, null, null);
        }

        System.out.println("Hoidettu");
    }

    protected void soita(){

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNo));

        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
               != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Soita 1");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                   Manifest.permission.CALL_PHONE)) {
                System.out.println("Soita 2");
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_CALL_PHONE);
                System.out.println("Soita 3");
            }
        }
        if(onnistui)
        { startActivity(callIntent);}
        onnistui = false;
    }

    private void sposti(){
        lahetettava = viesti.getText().toString();
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, sosoite);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Viesti Petri Mattilalta");
        emailIntent.putExtra(Intent.EXTRA_TEXT, lahetettava);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Toast.makeText(MainActivity.this, "Sähköposti lähetetty.", Toast.LENGTH_SHORT).show();
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Ei onnistunut.", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        System.out.println("Lupa");

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Lupa saatu");
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, lahetettava, null, null);
                    Toast.makeText(getApplicationContext(), "Viesti lähetetty.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Viestin lähettäminen epäonnistui !", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            case MY_PERMISSIONS_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Soita");
                          onnistui = true;
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Soitto epäonnistui !", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }


}