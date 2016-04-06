package id.miehasiswa.game.catchthebutterfly;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by danang on 05/04/16.
 */
public class DMLActivity extends AppCompatActivity {
    DbCatchTheButterfly db = new DbCatchTheButterfly(this);
    LocationModel L;
    boolean UPDATE = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        UPDATE = intent.getBooleanExtra("UPDATE", false);

        if (UPDATE) {
            L = new LocationModel();
            L.setId(intent.getStringExtra("id"));
            L.setLocation(intent.getStringExtra("location"));
            L.setLatitude(intent.getStringExtra("latitude"));
            L.setLongitude(intent.getStringExtra("longitude"));
            L.setRange(intent.getStringExtra("range"));
            update(L);
        } else {
            setContentView(R.layout.location_dml);
        }
    }

    public void update(LocationModel locate) {
        setContentView(R.layout.location_dml);

        EditText eTLocation = (EditText) findViewById(R.id.eTLocation);
        EditText eTLatitude = (EditText) findViewById(R.id.eTLatitude);
        EditText eTLongitude = (EditText) findViewById(R.id.eTLongitude);
        EditText eTRange = (EditText) findViewById(R.id.eTRange);
        TextView tVId = (TextView) findViewById(R.id.tVId);

        tVId.setTag(locate.getId());
        eTLocation.setText(locate.getLocation());
        eTLatitude.setText(locate.getLatitude());
        eTLongitude.setText(locate.getLongitude());
        eTRange.setText(locate.getRange());
    }

    public void Save(View v) {
        TextView tVId = (TextView) findViewById(R.id.tVId);
        EditText eTLocation = (EditText) findViewById(R.id.eTLocation);
        EditText eTLatitude = (EditText) findViewById(R.id.eTLatitude);
        EditText eTLongitude = (EditText) findViewById(R.id.eTLongitude);
        EditText eTRange = (EditText) findViewById(R.id.eTRange);

        L = new LocationModel();
        if (UPDATE) L.setId(tVId.getTag().toString());
        L.setLocation(eTLocation.getText().toString());
        L.setLatitude(eTLatitude.getText().toString());
        L.setLongitude(eTLongitude.getText().toString());
        L.setRange(eTRange.getText().toString());

        if (UPDATE) {
            db.updateLocation(L);
        } else {
            db.insertLocation(L);
        }

        Intent intent = new Intent(DMLActivity.this, MainActivity.class);
        DMLActivity.this.startActivity(intent);
        finish();
    }

    public void deleteLocation(LocationModel locate) {
        db.deleteLocation(locate);
    }

    @Override
    public void onBackPressed () {
        AlertDialog.Builder back = new AlertDialog.Builder(DMLActivity.this);
        back.setTitle("Unsaved Changes");
        back.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Button bSave = (Button) findViewById(R.id.bSave);
                bSave.performClick();
            }
        });
        back.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DMLActivity.this, MainActivity.class);
                DMLActivity.this.startActivity(intent);
                finish();
            }
        });
        back.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        back.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
