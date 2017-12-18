package com.example.maupi.parkking;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "parking_meter.db";
    SQLiteDatabase db;

    public DatabaseHelper(Context context){
        super(context , DATABASE_NAME , null , DATABASE_VERSION);
    }

    // Creating tables in the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_PAYMENT);
        db.execSQL(CREATE_TABLE_METER);
        this.db = db;
    }

    /*********************************************************************************************
     * Creating the client table in the database and handling the following functions            *
     *      - Inserting a new account in the table                                               *
     *      - Checking user information to authenticate login                                    *
     *      - Checking the uniqueness of the user name a user enters while creating a new account*
     *      - Getting a user's information                                                       *
     *      - Modifying a user's information                                                     *
     *********************************************************************************************/

    private static final String TABLE_CLIENT = "client";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_UNAME = "uname";
    private static final String COLUMN_PASS = "pass";
    private static final String COLUMN_EMAIL = "email";

    // Create table command
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_CLIENT + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_UNAME + " VARCHAR(255) NOT NULL, " +
            COLUMN_PASS + " VARCHAR(255) NOT NULL, " +
            COLUMN_EMAIL + " VARCHAR(255) NOT NULL);" ;


    // Inserting a new account in the client table
    public void insertContact(client c){

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_UNAME , c.getUname());
        values.put(COLUMN_PASS , c.getPass());
        values.put(COLUMN_EMAIL , c.getEmail());

        db.insert(TABLE_CLIENT , null , values);
        db.close();
    }

    // Checking user information (if user exists or not) to authenticate login
    public String searchInfo(String uname){

        db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_UNAME + " , " + COLUMN_PASS + " FROM " + TABLE_CLIENT;
        Cursor cursor = db.rawQuery(query , null);
        String u , p = "not found";
        if(cursor.moveToFirst()){

            do{
                u = cursor.getString(cursor.getColumnIndex(COLUMN_UNAME));

                if(u.equals(uname)) {
                    p = cursor.getString(cursor.getColumnIndex(COLUMN_PASS));
                    break;
                }
            } while(cursor.moveToNext());
        }
        return p;
    }

    // Get all the needed information about a client
    // The first argument is the username and the second argument is the name of the column you want to get information for
    public String getClientInfo(String user , String column){

        db = this.getReadableDatabase();
        String query = "SELECT " + column + " FROM " + TABLE_CLIENT + " WHERE " + COLUMN_UNAME + " = '" + user + "';";
        String info = "";
        Cursor c = db.rawQuery(query , null);
        if(c.moveToFirst()) {
            do {
                info = c.getString(c.getColumnIndex(column));
            } while (c.moveToNext());
        }
        return info;
    }

    //Modify data about the client (updating the client information
    //The first argument is the user name
    //The second argument is the new data the is going to be replacing something in the database
    //Tha thrid argument is the column name in which the data is going to be updated
    public void modifyClient(String user , String modification , String column){

        db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_CLIENT + " SET " + column + " = '" + modification + "'" + " WHERE " + COLUMN_UNAME + " = '" + user + "';";
        db.execSQL(query);
    }

    // Checking the username entered by the user while creating a new account to make sure it's unique
    // and storing the user's username to use it later when getting the foreign key
    static String userName;
    public boolean uniqueUname(String uname){

        db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_UNAME + " FROM " + TABLE_CLIENT;
        Cursor cursor = db.rawQuery(query , null);
        String user = "";
        boolean unique = true;

        if(cursor.moveToFirst()){

            do{
                user = cursor.getString(cursor.getColumnIndex(COLUMN_UNAME));

                if(user.equals(uname)){
                    unique = false;
                } else{
                    unique = true;
                }
            }while(cursor.moveToNext());
        }

        if(unique){
            userName = uname;
        }
        return unique;
    }


    /******************************************************************************************************************
     * Creating the payment table in the database and handling the following functions                                *
     *      - Checking the uniqueness of the user's payment by having a unique card number and a unique security code *
     *      - Inserting the new payment into the table                                                                *
     *      - Getting the user's ID to link it with the payment table                                                 *
     *      - Checking if a payment already exists for a particular user                                              *
     *      - Getting information about a particular user's payment                                                   *
     *      - Modifying a payment entered by a particular user                                                        *
     ******************************************************************************************************************/

    private static final String TABLE_PAYMENT = "Payment";
    private static final String COLUMN_PAYMENT_ID = "id";
    private static final String COLUMN_CLIENT = "client";
    private static final String COLUMN_CARD_NUMBER = "cNumber";
    private static final String COLUMN_SECURITY_CODE = "SecurityCode";
    private static final String COLUMN_EXP_DATE = "ExpirationDate";
    private static final String COLUMN_COUNTRY = "Country";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_ZIP_CODE = "ZipCode";
    private static final String COLUMN_BILLING_ADDRESS = "BillingAddress";
    private static final String COLUMN_CITY_STATE = "CityState";
    private static final String COLUMN_CARD_BRAND = "CardBrand";

    private static final String CREATE_TABLE_PAYMENT = "CREATE TABLE " + TABLE_PAYMENT + "(" +
            COLUMN_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
            COLUMN_CLIENT + " , " +
            COLUMN_NAME + " VARCHAR(255) NOT NULL , " +
            COLUMN_CARD_NUMBER + " VARCHAR(15) , " +
            COLUMN_CARD_BRAND + " VARCHAR(30) , " +
            COLUMN_SECURITY_CODE + " VARCHAR(5) NOT NULL , " +
            COLUMN_EXP_DATE + " VARCHAR(10) NOT NULL , " +
            COLUMN_BILLING_ADDRESS + " VARCHAR(50) NOT NULL , " +
            COLUMN_ZIP_CODE + " VARCHAR(6) NOT NULL , " +
            COLUMN_CITY_STATE + " VARCHAR(20) NOT NULL , " +
            COLUMN_COUNTRY + " VARCHAR(20) NOT NULL , " +
            "FOREIGN KEY " + "(" + COLUMN_CLIENT + ") REFERENCES " + TABLE_CLIENT + "(" + COLUMN_ID + ") );";


    // Checking the payment entered by the user after creating a new account to make sure it's unique
    public boolean uniquePayment(String cardNumber , String securityCode){

        db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_CARD_NUMBER + " , " + COLUMN_SECURITY_CODE + " FROM " + TABLE_PAYMENT;
        Cursor cursor = db.rawQuery(query , null);
        String cardNo , secCode;
        boolean unique = true;

        if(cursor.moveToFirst()){

            do{
                cardNo = cursor.getString(cursor.getColumnIndex(COLUMN_CARD_NUMBER));
                secCode = cursor.getString(cursor.getColumnIndex(COLUMN_SECURITY_CODE));

                if(cardNo.equals(cardNumber) || secCode.equals(securityCode)){
                    unique = false;
                } else{
                    unique = true;
                }
            }while(cursor.moveToNext());
        }
        return unique;
    }

    // Inserting a new payment in the payment table
    public void insertPayment(PaymentInfo p){

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME , p.getNameOnCard());
        values.put(COLUMN_CARD_NUMBER , p.getCardNum());
        values.put(COLUMN_SECURITY_CODE , p.getSecurityCode());
        values.put(COLUMN_EXP_DATE , p.getExpDate());
        values.put(COLUMN_BILLING_ADDRESS , p.getAddress());
        values.put(COLUMN_ZIP_CODE , p.getZip());
        values.put(COLUMN_CITY_STATE , p.getCityState());
        values.put(COLUMN_COUNTRY , p.getCountry());
        values.put(COLUMN_CLIENT , getForeignInfo());
        values.put(COLUMN_CARD_BRAND , p.getCardType());


        db.insert(TABLE_PAYMENT , null , values);
        db.close();
    }


    // Get the id of the user to connect the user with his/her payment and meter
    public String getForeignInfo(){
        db = this.getReadableDatabase();
        String checkExistence = "SELECT " + COLUMN_UNAME + " FROM " + TABLE_CLIENT + ";";
        String userExist , id = "";
        Cursor c = db.rawQuery(checkExistence , null);
        if(c.moveToFirst()){
            do{
                userExist = c.getString(c.getColumnIndex(COLUMN_UNAME));
                if(userExist.equals(userName)){
                    String query  = "SELECT " + COLUMN_ID + " FROM " + TABLE_CLIENT +" WHERE " + COLUMN_UNAME + " = '" + userName + "';";
                    Cursor cursor = db.rawQuery(query , null);
                    cursor.moveToFirst();
                    id = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
                }
            }while(c.moveToNext());
        }
        return id;
    }

    // Get the id of the user if he/she has payment info inserted (checking if a user already has a payment stored in the database
    public boolean checkPaymentExists(){
        boolean paymentMade = false;
        db = this.getReadableDatabase();
        String checkExistence = "SELECT " + COLUMN_CLIENT + " FROM " + TABLE_PAYMENT + ";";
        String userExist , id = "";
        Cursor c = db.rawQuery(checkExistence , null);
        if(c.moveToFirst()){
            do{
                userExist = c.getString(c.getColumnIndex(COLUMN_CLIENT));
                if(userExist.equals(getForeignInfo())){
                    id = userExist;
                    paymentMade = true;
                }
            }while(c.moveToNext());
        }
        return paymentMade;
    }

    //Getting payment information for a specific user
    //The first argument is the id of the user which is retrieved from the client table (foreign key)
    //The second column is the name of the column for which we need to get information
    public String getPaymentData(String id , String column){

        db = this.getWritableDatabase();
        String query = "SELECT " + column + " FROM " + TABLE_PAYMENT + " WHERE " + COLUMN_CLIENT + " = '" + id + "';";
        String info = "";
        Cursor c = db.rawQuery(query , null);
        if(c.moveToFirst()){
            do{
                info = c.getString(c.getColumnIndex(column));
            }while(c.moveToNext());

        }

        return info;
    }

    //Modify data about the client's payment
    //The first argument is the client's id
    //The second argument is the new data that is going to replace old data in the database
    //The third argument is the name of the column in which the new data is going to be stored.
    public void modifyPayment(String id , String modification , String column){

        db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_PAYMENT + " SET " + column + " = '" + modification + "'" + " WHERE " + COLUMN_CLIENT + " = '" + id + "';";
        db.execSQL(query);
    }


    /******************************************************************************************************************
     * Creating the meter table in the database and handling the following functions                                  *
     *      - Checking if the meter table is empty or not                                                             *
     *      - Inserting new meters into the table                                                                     *
     *      - Getting information about a particular meter                                                            *
     ******************************************************************************************************************/
    private static final String TABLE_METER = "meter";
    private static final String COLUMN_METER_ID = "id";
    private static final String COLUMN_METER_PRICE = "price";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_METER_ADDRESS = "address";

    private static final String CREATE_TABLE_METER = "CREATE TABLE " + TABLE_METER + "(" +
            COLUMN_METER_ID + " VARCHAR(255) PRIMARY KEY, " +
            COLUMN_METER_ADDRESS + " VARCHAR(255) NOT NULL, " +
            COLUMN_LATITUDE + " VARCHAR(100) NOT NULL, " +
            COLUMN_LONGITUDE + " VARCHAR(100) NOT NULL, " +
            COLUMN_METER_PRICE + " VARCHAR(10) NOT NULL); ";

    //Check if the meter table is empty or not
    public boolean checkMeterTable(){
        boolean empty = false;
        db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_METER + " ;";
        Cursor c = db.rawQuery(query , null);
        if(c.moveToFirst()) {
            return empty;
        }else{
            empty = true;
            return empty;
        }
    }

    // Inserting a new meter in the meter table
    public void insertMeter(ParkingMeterData m){

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_METER_ID , m.getId());
        values.put(COLUMN_METER_ADDRESS , m.getAddress());
        values.put(COLUMN_LATITUDE , m.getLatlng().latitude);
        values.put(COLUMN_LONGITUDE , m.getLatlng().longitude);
        values.put(COLUMN_METER_PRICE , m.getPrice());

        db.insert(TABLE_METER , null , values);
        db.close();
    }

    //Getting information about the meter
    //The first argument is the id of the meter
    //The second argument is the name of the column for which we are getting data for
    public String getInfo(String meterId , String category){
        db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_METER_ID + " FROM " + TABLE_METER;
        String ID , inquiry = "";
        Cursor c = db.rawQuery(query , null);
        if(c.moveToFirst()) {
            do {
                ID = c.getString(c.getColumnIndex(COLUMN_METER_ID));
                if (ID.equals(meterId)) {
                    String priceFromDb = "SELECT " + category + " FROM " + TABLE_METER + " WHERE " + COLUMN_METER_ID + " = '" + meterId + "';";
                    Cursor cursor = db.rawQuery(priceFromDb, null);
                    cursor.moveToFirst();
                    inquiry = cursor.getString(cursor.getColumnIndex(category));
                }
            } while (c.moveToNext());
        }
        return inquiry;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_client = "DROP TABLE IF EXISTS " + TABLE_CLIENT;
        String drop_payment = "DROP TABLE IF EXISTS " + TABLE_PAYMENT;
        String drop_meter = "DROP TABLE IF EXISTS " + TABLE_METER;

        db.execSQL(drop_client);
        db.execSQL(drop_payment);
        db.execSQL(drop_meter);
        this.onCreate(db);
    }
}