package id.miehasiswa.game.catchthebutterfly;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danang on 05/04/16.
 */
public class MainActivity extends AppCompatActivity {
    DbCatchTheButterfly db = new DbCatchTheButterfly(this);
    ArrayList<LocationModel> location;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updatelVLocation();

        // buat dialog update dan hapus
        ListView list_location = (ListView) findViewById(R.id.list_location);
        list_location.setClickable(true);
        list_location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
                intent.putExtra("id", location.get(position).getId());
                intent.putExtra("location", location.get(position).getLocation());
                intent.putExtra("latitude", location.get(position).getLatitude());
                intent.putExtra("longitude", location.get(position).getLongitude());
                intent.putExtra("range", location.get(position).getRange());
                MainActivity.this.startActivity(intent);
                finish();
            }
        });

        list_location.setLongClickable(true);
        list_location.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, final long id) {
                final CharSequence[] items = {"UPDATE", "DELETE"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // kalo update
                            Intent intent = new Intent(MainActivity.this, DMLActivity.class);
                            intent.putExtra("UPDATE", true);
                            intent.putExtra("id", location.get(position).getId());
                            intent.putExtra("location", location.get(position).getLocation());
                            intent.putExtra("latitude", location.get(position).getLatitude());
                            intent.putExtra("longitude", location.get(position).getLongitude());
                            intent.putExtra("range", location.get(position).getRange());
                            MainActivity.this.startActivity(intent);
                            finish();
                        } else if (which == 1) { // kalo delet
                            AlertDialog.Builder delete = new AlertDialog.Builder(MainActivity.this);
                            delete.setTitle("Delete\n" + location.get(position).getLocation() + "?");
                            delete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LocationModel locate = new LocationModel();
                                    locate.setId(location.get(position).getId());
                                    db.deleteLocation(locate);
                                    updatelVLocation();
                                }
                            });
                            delete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            delete.show();
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void updatelVLocation(){
        this.location = db.getAllLocation();
        List<String> list = new ArrayList<>();
        for (Integer i = 0; i < location.size(); i++){
            list.add(location.get(i).getLocation());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.location_lists, list);
        ListView listView = (ListView) findViewById(R.id.list_location);
        listView.setAdapter(adapter);
    }


    public void bInsert(View v) {
        Intent intent = new Intent(MainActivity.this, DMLActivity.class);
        MainActivity.this.startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder delete = new AlertDialog.Builder(MainActivity.this);
        delete.setTitle("Exit?");
        delete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { finish();onDestroy(); }
        });
        delete.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        delete.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
