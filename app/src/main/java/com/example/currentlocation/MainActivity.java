package com.example.currentlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE_LOCATION_PERMISSION);
                }else {
                    startLocationService();
                }
            }
        });

        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationService();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_CODE_LOCATION_PERMISSION && grantResults.length>0){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                startLocationService();
            }else {
                Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocaionservicesRunning(){
        ActivityManager activityManager =  (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager!=null){
            for (ActivityManager.RunningServiceInfo service :
                    activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(LocationService.class.getName().equals(service.service.getClassName())){
                    if(service.foreground)
                        return true;
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService(){
        if(!isLocaionservicesRunning()){
            Intent intent =new Intent(getApplicationContext(),LocationService.class);
            intent.setAction(Constans.ACTON_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this,"Locations Service started",Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLocationService(){
        if(isLocaionservicesRunning()){
            Intent intent =new Intent(getApplicationContext(),LocationService.class);
            intent.setAction(Constans.ACTON_STOP_LOCATION_SERVICE);
            startService(intent);
        }
    }
}