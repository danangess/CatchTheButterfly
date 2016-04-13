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
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by danang on 13/04/16.
 */
public class NavigationActivity extends AppCompatActivity
        implements  GoogleApiClient.ConnectionCallbacks,
                    GoogleApiClient.OnConnectionFailedListener,
                    LocationListener {

    LocationModel L;
    private static final int MY_PREMISSIONS_REQUEST = 99;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Location locationDest;
    TextView tVMyCoordinate;
    TextView tVFar;
    TextView tvArrow;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        tVMyCoordinate = (TextView) findViewById(R.id.tvMyCoordinate);
        tVFar = (TextView) findViewById(R.id.tVFar);
        tvArrow = (TextView) findViewById(R.id.tVArrow);
        fromDegree = tvArrow.getRotation();

        L = new LocationModel();

        Intent intent = getIntent();
        L.setId(intent.getStringExtra("id"));
        L.setLocation(intent.getStringExtra("location"));
        L.setLatitude(intent.getStringExtra("latitude"));
        L.setLongitude(intent.getStringExtra("longitude"));
        L.setRange(intent.getStringExtra("range"));

        TextView tVTargetLocation = (TextView) findViewById(R.id.tVTargetLocation);
        TextView tVCoordinateTarget = (TextView) findViewById(R.id.tvCoordinateTarget);
        tVTargetLocation.setText(L.getLocation());
        String Coordinate = "(" + L.getLatitude() + "," + L.getLongitude() + ")";
        tVCoordinateTarget.setText(Coordinate);

        buildGoogleApiClient();
        createLocationRequest();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //tampilkan dialog minta ijin
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PREMISSIONS_REQUEST);
            return;
        }
        // perbedaan dg project lokasi terakhir
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    float fromDegree;
    float toDegree;
    @Override
    public void onLocationChanged(Location location) {
        locationDest = new Location("Target Location");
        locationDest.setLatitude(Double.valueOf(L.getLatitude()));
        locationDest.setLongitude(Double.valueOf(L.getLongitude()));
        float distanceMeters;
        distanceMeters = location.distanceTo(locationDest);

        tVMyCoordinate.setText(String.valueOf("(" + location.getLatitude() + "," + location.getLongitude() + ")"));

        toDegree = location.bearingTo(locationDest);
        toDegree -= 90; //posisi awal petunjuk arah di 90 derajat
        if (toDegree < 0) toDegree += 360;
        RotateAnimation rotateAnimation = new RotateAnimation(
                fromDegree, toDegree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );

        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        tvArrow.startAnimation(rotateAnimation);
        String Far = String.valueOf(distanceMeters) + " meters";
        tVFar.setText(Far);
        fromDegree = toDegree;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    // muncul dialog & user memberikan reson (allow/deny), method ini akan dipanggil
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == MY_PREMISSIONS_REQUEST) {
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

    protected synchronized void buildGoogleApiClient () {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    protected void createLocationRequest () {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder back = new AlertDialog.Builder(NavigationActivity.this);
        back.setTitle("Cancel Navigation?");
        back.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NavigationActivity.this.startActivity(new Intent(NavigationActivity.this, MainActivity.class));
                finish();
            }
        });
        back.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        back.show();
    }
}
