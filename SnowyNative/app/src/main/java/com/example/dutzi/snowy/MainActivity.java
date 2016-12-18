package com.example.dutzi.snowy;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dutzi.snowy.model.Resort;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private SQLManager dbManager;
    private List<Resort> itemList;
    private List<Resort> db_itemList;
    private ArrayAdapter<Resort> adapter;

    protected Resort get_resort_by_id(int id)
    {
        for(Resort r : itemList)
        {
            if(r.getId() == id)
            {
                return r;
            }
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0) {    //DetailsActivity
            if(resultCode== Activity.RESULT_OK) {
                if(data.getIntExtra(getResources().getString(R.string.requestCode), -1) == 0) {
                    String name = data.getStringExtra(getResources().getString(R.string.name));
                    String coordinates = data.getStringExtra(getResources().getString(R.string.coords));
                    String country = data.getStringExtra(getResources().getString(R.string.country));
                    double slopes = data.getDoubleExtra(getResources().getString(R.string.slopes), 0);
                    final int id = data.getIntExtra(getResources().getString(R.string.id), 0);
                    Resort res = new Resort(name, coordinates, country, slopes);
                    res.setId(id);
                    int ret = dbManager.updateResort(res);
                    if (ret != 0) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setTheme(R.style.AppTheme);
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Resort " + res.getName() + " updated!").setPositiveButton("Ok", dialogClickListener).show();
                    }
                }
                else if(data.getIntExtra(getResources().getString(R.string.requestCode), -1) == 1) {

                    String month = data.getStringExtra(getResources().getString(R.string.month));
                    System.out.println("month " + month);
                    int visitors = data.getIntExtra(getString(R.string.visitors), 0);
                    final int id = data.getIntExtra(getResources().getString(R.string.id), 0);
                    Resort res = get_resort_by_id(id);
                    res.addVisitors(month, visitors);
                    dbManager.updateResort(res);
                }
                adapter.clear();
                adapter.addAll(dbManager.getAllResorts());
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        listView = (ListView) findViewById(R.id.list_view);
        dbManager = new SQLManager(this);

        itemList = dbManager.getAllResorts();

        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final Resort item = (Resort) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra(getResources().getString(R.string.name), item.getName());
                intent.putExtra(getResources().getString(R.string.coords), item.getCoordinates());
                intent.putExtra(getResources().getString(R.string.country), item.getCountry());
                intent.putExtra(getResources().getString(R.string.slopes), item.getSlopes_km());
                intent.putExtra(getResources().getString(R.string.id), item.getId());
                startActivityForResult(intent, 0);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setTheme(R.style.AppTheme);
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                final Resort item = (Resort) itemList.get(position);
                                dbManager.deleteResort(item);
                                itemList.remove(position);
                                adapter.clear();
                                adapter.addAll(dbManager.getAllResorts());
                                adapter.notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure you want to delete this?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            }
        });


        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        final TextView txtName = (TextView) findViewById(R.id.editTextName);
        final TextView txtCountry = (TextView) findViewById(R.id.editTextCountry);
        final TextView txtCoord = (TextView) findViewById(R.id.editTextCoords);
        final TextView txtSlopes = (TextView) findViewById(R.id.editTextSlopes);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resortName = txtName.getText().toString();
                String resortCountry = txtCountry.getText().toString();
                String resortCoord = txtCoord.getText().toString();
                double resortSlopes = Double.parseDouble(txtSlopes.getText().toString());
                Resort resort = new Resort(resortName, resortCoord, resortCountry, resortSlopes);
                dbManager.addResort(resort);
                adapter.clear();
                System.out.println("SIZE" + dbManager.getAllResorts().size());
                adapter.addAll(dbManager.getAllResorts());
                adapter.notifyDataSetChanged();
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setTheme(R.style.AppTheme);
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Added!").setPositiveButton("Ok", dialogClickListener).show();
            }
        });
    }
}
