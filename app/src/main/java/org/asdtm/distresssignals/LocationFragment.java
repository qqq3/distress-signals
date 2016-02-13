package org.asdtm.distresssignals;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LocationFragment extends Fragment
{
    LocationManager locationManager;
    org.asdtm.distresssignals.model.Location mLocation;

    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private Button findCurrentLocation;
    private boolean isGPSEnabled;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_location, parent, false);

        mLocation = new org.asdtm.distresssignals.model.Location();

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);

        assert appCompatActivity.getSupportActionBar() != null;
        appCompatActivity.getSupportActionBar().setTitle(0);
        appCompatActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_cancel);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locationManager =
                (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        isGPSEnabled =
                locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER) &&
                        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        latitudeTextView = (TextView) v.findViewById(R.id.latitude_content);
        longitudeTextView = (TextView) v.findViewById(R.id.longitude_content);
        findCurrentLocation = (Button) v.findViewById(R.id.find_current_location);

        findCurrentLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isGPSEnabled) {
                    gpsRequestLocation();
                } else {
                    showSettingsDialog();
                }
            }
        });

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        isGPSEnabled = locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER) &&
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (ActivityCompat.checkSelfPermission(getActivity(),
                                               Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(mLocationListener);
    }

    private LocationListener mLocationListener = new LocationListener()
    {
        @Override
        public void onLocationChanged(Location location)
        {
            String latitude = String.format("%1.4f", location.getLatitude());
            String longitude = String.format("%1.4f", location.getLongitude());

            mLocation.setLatitude(location.getLatitude());
            mLocation.setLongitude(location.getLongitude());

            latitudeTextView.setText(latitude);
            longitudeTextView.setText(longitude);

            if (ActivityCompat.checkSelfPermission(getActivity(),
                                                   Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(mLocationListener);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

        @Override
        public void onProviderEnabled(String provider)
        {

        }

        @Override
        public void onProviderDisabled(String provider)
        {

        }
    };

    public void gpsRequestLocation()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(),
                                                   Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Toast.makeText(getActivity(), "Check app permission", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                                               mLocationListener);
    }

    public void showSettingsDialog()
    {
        AlertDialog.Builder showSettings = new AlertDialog.Builder(getActivity());
        showSettings.setTitle(R.string.settingsDialogTitle);
        showSettings.setMessage(R.string.settingsDialogMessage);
        showSettings.setPositiveButton(R.string.settingsDialog_positiveButton,
                                       new DialogInterface.OnClickListener()
                                       {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which)
                                           {
                                               Intent intent = new Intent(
                                                       Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                               startActivity(intent);
                                           }
                                       });
        showSettings.setNegativeButton(R.string.settingsDialog_negativeButton,
                                       new DialogInterface.OnClickListener()
                                       {
                                           @Override
                                           public void onClick(DialogInterface dialog, int which)
                                           {
                                               dialog.cancel();
                                           }
                                       });

        showSettings.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());

                    return true;
                }
            case R.id.share_location:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,
                                     "My coordinates!\nLatitude: " + mLocation.getLatitude() + "; Longitude: " + mLocation.getLongitude());
                startActivity(Intent.createChooser(shareIntent, "Share coordinates"));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
