package ua.tsisar.abetka.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.GridView;

import ua.tsisar.abetka.R;
import ua.tsisar.abetka.adapter.GridAdapter;

public class GridActivity extends AppCompatActivity {
    private static final String POSITION = "position";
    private static final String RANDOM_OFFSET = "random_offset";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grid);

        GridView gridView = findViewById(R.id.grid_view);
        GridAdapter gridAdapter = new GridAdapter(this,
                getResources().getStringArray(R.array.letters_array));

        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.startAnimation(new AlphaAnimation(1F, 0.2F));
                StartMainActivity(position);
            }
        });

    }

    private void StartMainActivity(int position) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(POSITION, position);
        intent.putExtra(RANDOM_OFFSET, false);

        startActivity(intent);
    }

}
