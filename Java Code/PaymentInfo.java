package com.example.maupi.parkking;

/**
 * Created by Zacc on 10/13/2017.
 */

public class PaymentInfo {
    String address, zip, cityState, country, cardNum, securityCode, expDate, nameOnCard, cardType;

    public String getAddress(){return address;}
    public String getZip(){return zip;}
    public String getCityState(){return cityState;}
    public String getCountry(){return country;}
    public String getCardNum(){return cardNum;}
    public String getSecurityCode(){return securityCode;}
    public String getExpDate(){return expDate;}
    public String getNameOnCard(){return nameOnCard;}
    public String getCardType(){return cardType;}

    public void setAddress(String addressString){address = addressString;}
    public void setZip(String zipString){zip=zipString;}
    public void setCityState(String cityStateString){cityState=cityStateString;}
    public void setCountry(String countryString){country=countryString;}
    public void setCardNum(String cardNumString){cardNum=cardNumString;}
    public void setSecurityCode(String securityCodeString){securityCode=securityCodeString;}
    public void setExpDate(String expDateString){expDate=expDateString;}
    public void setNameOnCard(String nameOnCardString){nameOnCard=nameOnCardString;}
    public void setCardType(String cardTypeString){cardType=cardTypeString;}
}
