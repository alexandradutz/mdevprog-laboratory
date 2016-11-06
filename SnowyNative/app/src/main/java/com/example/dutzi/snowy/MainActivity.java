package com.example.dutzi.snowy;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.dutzi.snowy.model.Resort;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);
        final ArrayList<Resort> itemList = new ArrayList<>();
        for (int i=0;i<100;i++) {
            itemList.add(new Resort("resort"+i, "0.0,0.0", "country"+i, i));
        }

        final ArrayAdapter<Resort> adapter=new ArrayAdapter<Resort>(this,android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final Resort item = (Resort) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("coordinates", item.getCoordinates());
                intent.putExtra("country", item.getCountry());
                intent.putExtra("slopes", item.getSlopes_km());
                startActivity(intent);
            }
        });
    }
}
