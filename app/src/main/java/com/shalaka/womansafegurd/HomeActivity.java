package com.shalaka.womansafegurd;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.shalaka.womansafegurd.Database.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ImageButton sos;
    CardView card1,card2,card3,card4;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FusedLocationProviderClient fusedLocationProviderClient;
    DatabaseHelper mydb;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    Double latitude;
    Double longitude;
    String address;
    ArrayList<String> numList;
    NavigationView navigationView;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupToolbar();
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
       getSupportActionBar().setHomeAsUpIndicator(R.drawable.dehaze_24);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setDisplayShowTitleEnabled(false);
       sos=findViewById(R.id.sos);
       card1=findViewById(R.id.card1);
        card2=findViewById(R.id.card2);
        card3=findViewById(R.id.card3);
        card4=findViewById(R.id.card4);
        navigationView=findViewById(R.id.nav_view);
        mydb=new DatabaseHelper(getApplicationContext());
        numList=new ArrayList<String>();

        if (checkpermission(Manifest.permission.SEND_SMS)){
            sos.setEnabled(true);
        }else {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.SEND_SMS},MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

       sos.findViewById(R.id.sos);

       sos.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            //   Toast.makeText(HomeActivity.this, "Under Development", Toast.LENGTH_SHORT).show();
               try {
                   Cursor result=mydb.getNumber();
                   if (result.getCount()==0)
                   {
                       Toast.makeText(getApplicationContext(),"Add Number to send Emergency Alert",Toast.LENGTH_SHORT).show();
                   } else {
                       while (result.moveToNext()) {
                           numList.add(result.getString(0));
                       }
                   }
                   if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                   {
                       fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                           @Override
                           public void onComplete(@NonNull Task<Location> task) {
                               Location location=task.getResult();
                               if (location!=null)
                               {
                                   Geocoder geocoder=new Geocoder(HomeActivity.this, Locale.getDefault());
                                   try {
                                       List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                       latitude=addresses.get(0).getLatitude();
                                       longitude=addresses.get(0).getLongitude();
                                       address=addresses.get(0).getAddressLine(0);
                                       String msg ="Hi,I am in trouble,please help me by reaching to below location.Google Map Location ";
                                       String uri ="http://maps.google.com/maps?saddr="+latitude+","+longitude+"\n"+msg;;

                                       // Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                                       //  PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

                                       SmsManager sms=SmsManager.getDefault();
                                       for (int i=0;i<numList.size();i++)
                                       {
                                           sms.sendTextMessage(numList.get(i), null,uri,null,null);

                                       }

                                       Toast.makeText(getApplicationContext(), "Emergency Alert Sent successfully!",
                                               Toast.LENGTH_LONG).show();

                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   }
                               }

                           }
                       });

                   }
                   else
                   {
                       ActivityCompat.requestPermissions(HomeActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                   }


               }catch (Exception e){
                   Toast.makeText(HomeActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
               }
           }
       });
       card1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                 startActivity(new Intent(getApplicationContext(),EscapeActivity.class));
           }
       });
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LawActivity.class));

            }
        });
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),VideoActivity.class));

            }
        });
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CameraActivity.class));
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.tips :
                        startActivity(new Intent(getApplicationContext(),EscapeActivity.class));
                        break;
                    case R.id.laws :
                        startActivity(new Intent(getApplicationContext(),LawActivity.class));
                        break;
                    case R.id.video:
                        startActivity(new Intent(getApplicationContext(),VideoActivity.class));
                    case R.id.camera:
                      //  startActivity(new Intent(getApplicationContext(),CameraActivity.class));
                        break;
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(),ContactActivity.class));
                        break;
                    case R.id.about:
                        Toast.makeText(HomeActivity.this, "Under Development", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.share:
                        Toast.makeText(HomeActivity.this, "Under Development", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });


    }
    public void setupToolbar()
    {
        drawerLayout=findViewById(R.id.drawerlayout);
        toolbar=findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public void add_number(View view)
    {
        startActivity(new Intent(getApplicationContext(),ContactActivity.class));
    }
    public boolean checkpermission(String Permission)
    {
        int check= ContextCompat.checkSelfPermission(this,Permission);
        return (check== PackageManager.PERMISSION_GRANTED);
    }
}