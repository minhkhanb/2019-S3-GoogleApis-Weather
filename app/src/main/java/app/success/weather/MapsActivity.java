package app.success.weather;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.WeatherResponse;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import static com.google.android.gms.awareness.state.Weather.CONDITION_CLEAR;
import static com.google.android.gms.awareness.state.Weather.CONDITION_CLOUDY;
import static com.google.android.gms.awareness.state.Weather.CONDITION_FOGGY;
import static com.google.android.gms.awareness.state.Weather.CONDITION_HAZY;
import static com.google.android.gms.awareness.state.Weather.CONDITION_ICY;
import static com.google.android.gms.awareness.state.Weather.CONDITION_RAINY;
import static com.google.android.gms.awareness.state.Weather.CONDITION_SNOWY;
import static com.google.android.gms.awareness.state.Weather.CONDITION_STORMY;
import static com.google.android.gms.awareness.state.Weather.CONDITION_UNKNOWN;
import static com.google.android.gms.awareness.state.Weather.CONDITION_WINDY;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {

    private GoogleMap mMap;
    private GoogleApiClient mClient;
    private String[] conditions = {
            "unknown",
            "It's clear",
            "It's cloudy",
            "It's foggy",
            "It's hazy",
            "It's icy",
            "It's rainy",
            "It's snowy",
            "It's stormy",
            "It's windy"
    };
    private final static int REQUEST_PERMISSION_REQUEST_CODE = 42;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .addConnectionCallbacks(this)
                .build();

        mClient.connect();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Success and move the camera
        LatLng success = new LatLng(10.801419,106.6465949);
        mMap.addMarker(new MarkerOptions().position(success).title("Marker in Success"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(success));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("KhaLe: ", "onConnect");

        getWeatherInfo();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    //granted
                    getWeatherInfo();
                }
                else {
                    Log.i("KhaLe", "Location permission denied.");
                }
            }
        }
    }
    public void getWeatherInfo() {
        if (!checkLocationPermission()) {
            return;
        }
        Awareness.getSnapshotClient(this).getWeather()
                .addOnSuccessListener(new OnSuccessListener<WeatherResponse>() {
                    @Override
                    public void onSuccess(WeatherResponse weatherResponse) {
                        Weather weather = weatherResponse.getWeather();
                        float temperature = weather.getTemperature(Weather.CELSIUS);

                        Log.i("KhaLe", "Temperature: " + temperature);
                        for (int i = 0; i < weather.getConditions().length; i++) {
                             int conditionType = weather.getConditions()[i];
                             String conditionDesc = null;
                            switch (conditionType) {
                                case CONDITION_UNKNOWN:
                                    conditionDesc = conditions[CONDITION_UNKNOWN];
                                    break;
                                case CONDITION_CLEAR:
                                    conditionDesc = conditions[CONDITION_CLEAR];
                                    break;

                                case CONDITION_CLOUDY:
                                    conditionDesc = conditions[CONDITION_CLOUDY];
                                    break;

                                case CONDITION_FOGGY:
                                    conditionDesc = conditions[CONDITION_FOGGY];
                                    break;

                                case CONDITION_HAZY:
                                    conditionDesc = conditions[CONDITION_HAZY];
                                    break;

                                case CONDITION_ICY:
                                    conditionDesc = conditions[CONDITION_ICY];
                                    break;

                                case CONDITION_RAINY:
                                    conditionDesc = conditions[CONDITION_RAINY];
                                    break;

                                case CONDITION_SNOWY:
                                    conditionDesc = conditions[CONDITION_SNOWY];
                                    break;

                                case CONDITION_STORMY:
                                    conditionDesc = conditions[CONDITION_STORMY];
                                    break;

                                case CONDITION_WINDY:
                                    conditionDesc = conditions[CONDITION_WINDY];
                                    break;

                                default:
                                    break;

                            }
                            Log.i("KhaLe", "Conditions: " + conditionDesc);
                        }

                        int humidity = weather.getHumidity();
                        Log.i("KhaLe", "Humidity: " + humidity);
                        float dewPoint = weather.getDewPoint(Weather.CELSIUS);
                        Log.i("KhaLe", "Dew point: " + humidity);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("KhaLe", "Cannot get weather information");
                    }
                });
    }

    private boolean checkLocationPermission() {
        if (!hasLocationPermission()) {
            Log.i("KhaLe", "Does not have location permission granted.");
            requestLocationPermission();
            return false;
        }
        return true;
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_PERMISSION_REQUEST_CODE
        );
    }
}
