package com.example.maupi.parkking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class getPaymentInfo extends AppCompatActivity {

    DatabaseHelper helper = new DatabaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_payment_info);

    }
    public void goToMap()
    {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void onSubmit(View v){

        if(v.getId() == R.id.SubmitPayment) {

            EditText address = (EditText) findViewById(R.id.address);
            EditText zip = (EditText) findViewById(R.id.zip);
            EditText cityState = (EditText) findViewById(R.id.cityState);
            EditText securityCode = (EditText) findViewById(R.id.securityCode);
            EditText expirationDate = (EditText) findViewById(R.id.expirationDate);
            EditText cardNum = (EditText) findViewById(R.id.cardNum);
            EditText name = (EditText) findViewById(R.id.name);
            EditText country = (EditText) findViewById(R.id.country);

            String addressString = address.getText().toString();
            String zipString = zip.getText().toString();
            String cityStateString = cityState.getText().toString();
            String securityCodeString = securityCode.getText().toString();
            String expirationDateString = expirationDate.getText().toString();
            String cardNumString = cardNum.getText().toString();
            String nameString = name.getText().toString();
            String countryString = country.getText().toString();
            CardType tempCardBrand = CardType.detect(cardNumString);
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
            if(addressString.isEmpty() || zipString.isEmpty() || cityStateString.isEmpty() || securityCodeString.isEmpty() ||
                    expirationDateString.isEmpty() || cardNumString.isEmpty() || nameString.isEmpty() || countryString.isEmpty()){
                if(nameString.isEmpty())
                    name.setError("Name on card is required");
                else if(cardNumString.isEmpty())
                    cardNum.setError("Card number is required");
                else if(expirationDateString.isEmpty())
                    expirationDate.setError("Expiration date is required");
                else if(securityCodeString.isEmpty())
                    securityCode.setError("Security code is required");
                else if(addressString.isEmpty())
                    address.setError("Address is required");
                else if(zipString.isEmpty())
                    zip.setError("Zip code is required");
                else if(cityStateString.isEmpty())
                    cityState.setError("State and city are required");
                else
                    country.setError("Country is required");
                return;
            }

            if(cardNumString.length() < 14 || cardNumString.length() > 16){
                cardNum.setError("Please enter a valid card number");
                return;
            }

            if(securityCodeString.length() < 3 || securityCodeString.length() > 3){
                securityCode.setError("Please enter a valid security code");
                return;
            }

            if(helper.uniquePayment(cardNumString , securityCodeString)){

                PaymentInfo p = new PaymentInfo();
                p.setAddress(addressString);
                p.setCardNum(cardNumString);
                p.setCityState(cityStateString);
                p.setCountry(countryString);
                p.setExpDate(expirationDateString);
                p.setNameOnCard(nameString);
                p.setSecurityCode(securityCodeString);
                p.setZip(zipString);
                p.setCardType(cardBrand);

                helper.insertPayment(p);
                Toast c = Toast.makeText(getPaymentInfo.this, "payment info accepted", Toast.LENGTH_SHORT);
                c.show();

                String user = getIntent().getStringExtra("user");
                Intent myintent=new Intent(this, OptionsActivity.class).putExtra("user", user);
                startActivity(myintent);
            } else{

                Toast unique =  Toast.makeText(getPaymentInfo.this, "Payment already exists", Toast.LENGTH_SHORT);
                unique.show();
            }
        }
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


