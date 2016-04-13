package id.miehasiswa.game.catchthebutterfly;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by danang on 05/04/16.
 */
public class DMLActivity extends AppCompatActivity
        implements  GoogleApiClient.ConnectionCallbacks,
                    GoogleApiClient.OnConnectionFailedListener
{
    DbCatchTheButterfly db = new DbCatchTheButterfly(this);
    LocationModel L;
    boolean UPDATE = false;

    private static final int MY_PERMISSIONS_REQUEST = 99;
    //int bebas, maks 1 byte
    GoogleApiClient googleApiClient;
    Location location;
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    EditText eTLocation;
    EditText eTLatitude;
    EditText eTLongitude;
    EditText eTRange;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_dml);
        Intent intent = getIntent();

        eTLocation = (EditText) findViewById(R.id.eTLocation);
        eTLatitude = (EditText) findViewById(R.id.eTLatitude);
        eTLongitude = (EditText) findViewById(R.id.eTLongitude);
        eTRange = (EditText) findViewById(R.id.eTRange);

        L = new LocationModel();

        UPDATE = intent.getBooleanExtra("UPDATE", false);
        if (UPDATE) L.setId(intent.getStringExtra("id"));
        L.setLocation(intent.getStringExtra("location"));
        L.setLatitude(intent.getStringExtra("latitude"));
        L.setLongitude(intent.getStringExtra("longitude"));
        L.setRange(intent.getStringExtra("range"));

        eTLocation.setText(L.getLocation());
        eTLatitude.setText(L.getLatitude());
        eTLongitude.setText(L.getLongitude());
        eTRange.setText(L.getRange());

        buildGoogleApiClient();
    }

    public void Save(View v) {
        if (UPDATE) L.setId(L.getId());
        L.setLocation(eTLocation.getText().toString());
        L.setLatitude(eTLatitude.getText().toString());
        L.setLongitude(eTLongitude.getText().toString());
        L.setRange(eTRange.getText().toString());

        if (UPDATE) {
            db.updateLocation(L);
        } else {
            db.insertLocation(L);
        }
        stop();
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
                stop();
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
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected void stop() {
        Intent intent = new Intent(DMLActivity.this, MainActivity.class);
        DMLActivity.this.startActivity(intent);
        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //tampilkan dialog minta ijin
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);
            return;
        }
        //ambil lokasi
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location != null) {
            eTLatitude.setText(String.valueOf(location.getLatitude()));
            eTLongitude.setText(String.valueOf(location.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    // muncul dialog & user memberikan reson (allow/deny), method ini akan dipanggil
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission diberikan, mulai ambil lokasi
                buildGoogleApiClient();
            } else {
                //permssion tidak diberikan, tampilkan pesan
                AlertDialog ad = new AlertDialog.Builder(this).create();
                ad.setMessage("Tidak mendapat ijin, tidak dapat mengambil lokasi");
                ad.show();
            }
        }
    }
}
