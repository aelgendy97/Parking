package com.example.maupi.parkking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by ahmedsameh19997 on 12/1/2017.
 */

public class EditPaymentActivity extends AppCompatActivity {

    DatabaseHelper helper = new DatabaseHelper(this);
    private Button saveChanges;
    private Button cancel;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_payment_info);

        final String user = getIntent().getStringExtra("user");
        id = helper.getClientInfo(user , "id");

        String nameOnCard = helper.getPaymentData(id, "Name");
        String cardNumber = helper.getPaymentData(id, "cNumber");
        String secCode = helper.getPaymentData(id, "SecurityCode");
        String expirationDate = helper.getPaymentData(id, "ExpirationDate");
        String address = helper.getPaymentData(id, "BillingAddress");
        String zipCode = helper.getPaymentData(id, "ZipCode");
        String cityState = helper.getPaymentData(id, "CityState");
        String country = helper.getPaymentData(id, "Country");

        EditText modifiedName = (EditText) findViewById(R.id.set_name);
        modifiedName.append(nameOnCard);

        EditText modified_cardNumber = (EditText) findViewById(R.id.set_cardNumber);
        modified_cardNumber.append(cardNumber);

        EditText modified_secCode = (EditText) findViewById(R.id.set_secCode);
        modified_secCode.append(secCode);

        EditText modified_expiry = (EditText) findViewById(R.id.set_expiry);
        modified_expiry.append(expirationDate);

        EditText modified_address = (EditText) findViewById(R.id.set_user_address);
        modified_address.append(address);

        EditText modified_zipCode = (EditText) findViewById(R.id.set_zipCode);
        modified_zipCode.append(zipCode);

        EditText modified_cityState = (EditText) findViewById(R.id.set_city);
        modified_cityState.append(cityState);

        EditText modified_country = (EditText) findViewById(R.id.set_country);
        modified_country.append(country);

        saveChanges = (Button) findViewById(R.id.save_payment);
        cancel = (Button) findViewById(R.id.cancelPayment);


        saveChanges.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onSaveChangesClick(user);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent myintent=new Intent(EditPaymentActivity.this, OptionsActivity.class).putExtra("user" , user);
                startActivity(myintent);
            }
        });


    }


    public void onSaveChangesClick(String user){

        EditText modifiedName = (EditText) findViewById(R.id.set_name);
        EditText modified_cardNumber = (EditText) findViewById(R.id.set_cardNumber);
        EditText modified_secCode = (EditText) findViewById(R.id.set_secCode);
        EditText modified_expiry = (EditText) findViewById(R.id.set_expiry);
        EditText modified_address = (EditText) findViewById(R.id.set_user_address);
        EditText modified_zipCode = (EditText) findViewById(R.id.set_zipCode);
        EditText modified_cityState = (EditText) findViewById(R.id.set_city);
        EditText modified_country = (EditText) findViewById(R.id.set_country);

        String nameOnCard = modifiedName.getText().toString();
        String cardNumber = modified_cardNumber.getText().toString();
        String secCode = modified_secCode.getText().toString();
        String expirationDate = modified_expiry.getText().toString();
        String address = modified_address.getText().toString();
        String zipCode = modified_zipCode.getText().toString();
        String cityState = modified_cityState.getText().toString();
        String country = modified_country.getText().toString();

        CardType tempCardBrand = CardType.detect(cardNumber);
        String cardBrand;
        if(tempCardBrand == CardType.VISA){
            cardBrand = "Visa";
        }
        else if(tempCardBrand == CardType.MASTERCARD){
            cardBrand = "Mastercard";
        }
        else if(tempCardBrand == CardType.UNKNOWN){
            cardBrand = "Unknown";
        }
        else if(tempCardBrand == CardType.DISCOVER){
            cardBrand = "Discover";
        }
        else if(tempCardBrand == CardType.AMERICAN_EXPRESS){
            cardBrand = "American Express";
        }
        else if(tempCardBrand == CardType.DINERS_CLUB){
            cardBrand = "Diners Club";
        }
        else if(tempCardBrand == CardType.JCB){
            cardBrand = "JCB";
        }
        else cardBrand = "unknown";

        // Make sure the user enters all the necessary fields for the payment
        if(address.isEmpty() || zipCode.isEmpty() || cityState.isEmpty() || secCode.isEmpty() ||
                expirationDate.isEmpty() || cardNumber.isEmpty() || nameOnCard.isEmpty() || country.isEmpty()){
            if(nameOnCard.isEmpty())
                modifiedName.setError("Name on card is required");
            else if(cardNumber.isEmpty())
                modified_cardNumber.setError("Card number is required");
            else if(expirationDate.isEmpty())
                modified_expiry.setError("Expiration date is required");
            else if(secCode.isEmpty())
                modified_secCode.setError("Security code is required");
            else if(address.isEmpty())
                modified_address.setError("Address is required");
            else if(zipCode.isEmpty())
                modified_zipCode.setError("Zip code is required");
            else if(cityState.isEmpty())
                modified_cityState.setError("State and city are required");
            else
                modified_country.setError("Country is required");
            return;
        }

        if(cardNumber.length() < 14 || cardNumber.length() > 16){
            modified_cardNumber.setError("Please enter a valid card number");
            return;
        }

        if(secCode.length() < 3 || secCode.length() > 3){
            modified_secCode.setError("Please enter a valid security code");
            return;
        }


        helper.modifyPayment(id , nameOnCard , "Name");
        helper.modifyPayment(id , cardNumber , "cNumber");
        helper.modifyPayment(id , secCode , "SecurityCode");
        helper.modifyPayment(id , expirationDate , "ExpirationDate");
        helper.modifyPayment(id , address , "BillingAddress");
        helper.modifyPayment(id , zipCode , "ZipCode");
        helper.modifyPayment(id , cityState , "CityState");
        helper.modifyPayment(id , country , "Country");
        helper.modifyPayment(id , cardBrand , "CardBrand");

        Toast.makeText(this , "Your information has been updated." , Toast.LENGTH_LONG).show();
        Intent myintent=new Intent(this, OptionsActivity.class).putExtra("user" , user);
        startActivity(myintent);
    }

    public enum CardType {

        UNKNOWN,
        VISA("^4[0-9]{12}(?:[0-9]{3})?$"),
        MASTERCARD("^5[1-5][0-9]{14}$"),
        AMERICAN_EXPRESS("^3[47][0-9]{13}$"),
        DINERS_CLUB("^3(?:0[0-5]|[68][0-9])[0-9]{11}$"),
        DISCOVER("^6(?:011|5[0-9]{2})[0-9]{12}$"),
        JCB("^(?:2131|1800|35\\d{3})\\d{11}$");

        private Pattern pattern;

        CardType() {
            this.pattern = null;
        }

        CardType(String pattern) {
            this.pattern = Pattern.compile(pattern);
        }

        public static CardType detect(String cardNumber) {

            for (CardType cardType : CardType.values()) {
                if (null == cardType.pattern) continue;
                if (cardType.pattern.matcher(cardNumber).matches()) return cardType;
            }

            return UNKNOWN;
        }
    }
}
