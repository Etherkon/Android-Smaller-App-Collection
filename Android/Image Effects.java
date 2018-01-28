package com.imageffects.imageeffects;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button b1, b2, b3;
    ImageView im;

    private BitmapDrawable abmp;
    private Bitmap bmp;
    private Bitmap operation;

    private int f1;
    private int westie;
    private int earth;
    private int turn;
    private int pic;

    private TextView lataus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        im = (ImageView) findViewById(R.id.imageView);
        lataus = (TextView) findViewById(R.id.textView3);

        f1 = R.drawable.ferrari;
        westie = R.drawable.westie;
        earth = R.drawable.earth;
        turn = 1;
        pic = f1;

        abmp = (BitmapDrawable) im.getDrawable();
        bmp = abmp.getBitmap();

        im.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ++turn;
               if(turn == 1) {  pic = f1; im.setImageResource(pic);   }
               else if(turn == 2) {  pic = westie; im.setImageResource(pic);  }
               else {  pic = earth; im.setImageResource(pic); turn = 0;  }
            }
        });
    }

    public void gray(View view) {
        im.setImageResource(pic);
        abmp = (BitmapDrawable) im.getDrawable();
        bmp = abmp.getBitmap();
        operation = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig());
        double red = 0.33;
        double green = 0.59;
        double blue = 0.11;

        lataus.setText("Ladataan...");
        im.setClickable(false);

        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                r = (int) red * r;
                g = (int) green * g;
                b = (int) blue * b;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        im.setImageBitmap(operation);
        lataus.setText("");
        im.setClickable(true);
    }

    public void bright(View view){
        im.setImageResource(pic);
        abmp = (BitmapDrawable) im.getDrawable();
        bmp = abmp.getBitmap();

        operation= Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),bmp.getConfig());

        lataus.setText("Ladataan...");
        im.setClickable(false);

        for(int i=0; i<bmp.getWidth(); i++){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r = 100  +  r;
                g = 100  + g;
                b = 100  + b;
                alpha = 100 + alpha;
                operation.setPixel(i, j, Color.argb(alpha, r, g, b));
            }
        }
        im.setImageBitmap(operation);
        lataus.setText("");
        im.setClickable(true);
    }

    public void dark(View view){
        im.setImageResource(pic);
        abmp = (BitmapDrawable) im.getDrawable();
        bmp = abmp.getBitmap();

        operation= Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());

        for(int i=0; i<bmp.getWidth(); i++){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r =  r - 50;
                g =  g - 50;
                b =  b - 50;
                alpha = alpha -50;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        im.setImageBitmap(operation);
        lataus.setText("");
        im.setClickable(true);
    }

    public void gama(View view) {
        im.setImageResource(pic);
        abmp = (BitmapDrawable) im.getDrawable();
        bmp = abmp.getBitmap();
        operation = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(),bmp.getConfig());

        lataus.setText("Ladataan...");
        im.setClickable(false);

        for(int i=0; i<bmp.getWidth(); i++){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r =  r + 150;
                g =  0;
                b =  0;
                alpha = 0;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        im.setImageBitmap(operation);
        lataus.setText("");
        im.setClickable(true);
    }

    public void green(View view){
        im.setImageResource(pic);
        abmp = (BitmapDrawable) im.getDrawable();
        bmp = abmp.getBitmap();

        operation = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig());

        lataus.setText("Ladataan...");
        im.setClickable(false);

        for(int i=0; i < bmp.getWidth(); i++){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r =  0;
                g =  g+150;
                b =  0;
                alpha = 0;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        im.setImageBitmap(operation);
        lataus.setText("");
        im.setClickable(true);
    }

    public void blue(View view){
        im.setImageResource(pic);
        abmp = (BitmapDrawable) im.getDrawable();
        bmp = abmp.getBitmap();

        operation = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(), bmp.getConfig());
        lataus.setText("Ladataan...");
        im.setClickable(false);

        for(int i=0; i<bmp.getWidth(); i++){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r =  0;
                g =  0;
                b =  b+150;
                alpha = 0;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        im.setImageBitmap(operation);
        lataus.setText("");
        im.setClickable(true);
    }
}
