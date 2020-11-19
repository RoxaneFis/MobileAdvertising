package uk.ac.imperial.seclab.android.co447.mp1.myadlibrary;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import android.provider.ContactsContract;
//import com.google.android.gms.location.LocationListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;

public class MyAdView {
    private static final String TAG = "MP1: MyAdView";
    public static Context ctx;
    public static boolean isLAT = false;
    public String GAID;
    public static void loadAd(TextView tv, Context ctx) {
        MyAdView.ctx = ctx;

        tv.setText("ALLURING ADVERTISEMENT");
        onLoad();

    }


    private static void onLoad() {
        //TODO: Implement me


        // GET LOCATION
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location;
        LocationManager locationManager;
        locationManager = (LocationManager) ctx.getSystemService(ctx.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            Log.d("Answers", "Lat: " + location.getLatitude() + "Long: " + location.getLongitude());
        }else{
            LocationListener loc_listener = new LocationListener() {
                public void onLocationChanged(Location l) {}
                public void onProviderEnabled(String p) {}
                public void onProviderDisabled(String p) {}
                public void onStatusChanged(String p, int status, Bundle extras) {}
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, loc_listener);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            try {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                Log.d("Answers", "Lat: " + lat + "Long: " + lon);
            } catch (NullPointerException e) {Log.d("Answers", "Lat: N" + "Long: N" );}
        }


        // GET IMEI
        TelephonyManager telephonyManager = (TelephonyManager)ctx.getSystemService(ctx.TELEPHONY_SERVICE);
        String id = telephonyManager.getDeviceId();
        Log.d("Answers", "IMEI: " + id);



        //GET ADVERTISING ID
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(ctx.getApplicationContext());
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String advertId = null;
                try{
                    advertId = idInfo.getId();
                }catch (Exception e){
                    e.printStackTrace();
                }
                return advertId;
            }
            @Override
            protected void onPostExecute(String advertId) {
                Log.d("Answers", "advertId: " + advertId);
            }
        };
        task.execute();


        ////////// READ CONTACTS
        //Uri personUri = ContentUris.withAppendedId(People.CONTENT_URI, personId);
        //Uri phonesUri = Uri.withAppendedPath(personUri, People.Phones.CONTENT_DIRECTORY);
        String[] proj = new String[] {Phones._ID, Contacts.People.Phones.TYPE, Contacts.People.Phones.NUMBER, Contacts.People.Phones.LABEL};
        //Cursor cursor = contentResolver.query(phonesUri, proj, null, null, null);
        }

}





