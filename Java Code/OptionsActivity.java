package com.example.maupi.parkking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by ahmedsameh19997 on 12/1/2017.
 */

public class OptionsActivity extends AppCompatActivity {

    private Button editProfile , goToMaps , editPayment , signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        editProfile = (Button) findViewById(R.id.edit);
        goToMaps = (Button) findViewById(R.id.maps);
        editPayment = (Button) findViewById(R.id.payment);
        signOut = (Button) findViewById(R.id.signOut);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditProfile();
            }
        });

        goToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMap();
            }
        });

        editPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditPayment();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMain();
            }
        });



    }

    public void goToMap()
    {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void goToEditProfile(){
        String value = getIntent().getExtras().getString("user");
        Intent intent = new Intent (this , EditProfileActivity.class).putExtra("user", value);
        startActivity(intent);
    }

    public void goToEditPayment(){
        String value = getIntent().getExtras().getString("user");
        Intent intent = new Intent (this , EditPaymentActivity.class).putExtra("user" , value);
        startActivity(intent);
    }

    public void goToMain(){
        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);
    }


}
