package com.example.maupi.parkking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MeterActivity extends AppCompatActivity {
    //Initializes the TextViews of the values in the activity_meter.xml
    EditText addressTextView;
    EditText addressTextView2;
    EditText idTextView;
    EditText priceTextView;
    TextView completePrice;

    String timeLastUsed;
    String timePerLastUsed; //TODO change to int
    DatabaseHelper db = new DatabaseHelper(this);
    private Button pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meter);

        Bundle extras = getIntent().getExtras();

        int meterID = -1;
        if(extras != null){
            meterID = extras.getInt("markerID");
        }
        System.out.println("meter ID!!!" + meterID);

        String tempaddress = db.getInfo(Integer.toString(meterID) , "address");
        String[]address = tempaddress.split("," , 2);

        addressTextView = (EditText) findViewById(R.id.get_address);
        addressTextView.setBackground(null);
        addressTextView.append(" " + address[0]);

        addressTextView2 = (EditText) findViewById(R.id.get_address_2);
        addressTextView2.setBackground(null);
        addressTextView2.append(address[1]);

        idTextView = (EditText) findViewById(R.id.get_meterId);
        idTextView.setBackground(null);
        idTextView.append(" " + db.getInfo(Integer.toString(meterID) , "id"));

        priceTextView = (EditText) findViewById(R.id.get_price);
        priceTextView.setBackground(null);
        priceTextView.append(" $ " + db.getInfo(Integer.toString(meterID) , "price"));

        pay = (Button) findViewById(R.id.pay_for_meter_button);

        final int[] delay1 = new int[1];

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup_time);
        final int finalMeterID = meterID;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);
                String delay = checkedRadioButton.getText().toString().substring(0 , 2);
                delay = delay.replace(" " , "");
                delay1[0] = Integer.parseInt(delay);
                completePrice = (TextView) findViewById(R.id.complete_price);
                completePrice.setText("$ " + (delay1[0] * Double.parseDouble(db.getInfo(Integer.toString(finalMeterID), "price"))));
            }
        });

        pay.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view)
            {
                if(radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(MeterActivity.this , "Please choose the time needed." , Toast.LENGTH_LONG).show();
                }else{
                    String availability = getIntent().getStringExtra("availability");
                    if(availability.contains("YES")) {
                        payMeter(view, radioGroup);
                    }else{
                        Toast.makeText(MeterActivity.this , "Sorry, this parking spot isn't available right now." , Toast.LENGTH_LONG).show();
                    }
                }
            }


        });


    }


    public void payMeter(View view , RadioGroup group) {
        int selectedID = group.getCheckedRadioButtonId();
        pay = (RadioButton) findViewById(selectedID);
        int time = 0;
        Intent intent = new Intent(this, Timer.class);
        if (pay.getId() == R.id.radio_5min)
            time = 60000 *2;
        else if (pay.getId() == R.id.radio_15min)
            time = 60000 * 15;
        else if (pay.getId() == R.id.radio_30min)
            time = 60000 * 30;

        Toast.makeText(MeterActivity.this, "Your parking spot has been reserved for " + pay.getText(), Toast.LENGTH_LONG).show();
        intent.putExtra("time", time);
        startService(intent);
    }

}
