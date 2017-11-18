package ua.tsisar.abetka.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ua.tsisar.abetka.R;

public class CoffeeActivity extends AppCompatActivity {
    private final static String URL = "https://play.google.com/store/apps/details?id=ua.tsisar.buymeacoffee";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee);
    }

    public void onCancel(View view){
        finish();
    }

    public void onBuy(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL)));
    }
}
