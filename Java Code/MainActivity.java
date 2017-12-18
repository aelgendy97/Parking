package com.example.maupi.parkking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Button AccBtn;
    private Button Login;
   // private Button MapBtn;
    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccBtn = (Button) findViewById(R.id.AccBtn);

        AccBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                goToCreateAccount();
            }
        });

        Login = (Button) findViewById(R.id.login);

        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                checkLogin();
            }
        });

    }
    public void goToCreateAccount()
    {
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }
    public void getPaymentInfo(){
        Intent intent = new Intent(this, getPaymentInfo.class);
        startActivity(intent);
    }
    public void goToOptions(){
        Intent intent = new Intent(this ,OptionsActivity.class );
        startActivity(intent);
    }

    public void checkLogin(){

        EditText uname = (EditText)findViewById(R.id.user);
        String userName = uname.getText().toString();

        EditText pass = (EditText)findViewById(R.id.password);
        String password = pass.getText().toString();

        String checkedPass = helper.searchInfo(userName);

        if(userName.isEmpty() && password.isEmpty()) {
            Toast noInfo = Toast.makeText(MainActivity.this, "Please input the username and password", Toast.LENGTH_LONG);
            noInfo.show();
        } else if(checkedPass.equals(password)){
            Toast success =  Toast.makeText(MainActivity.this, "Congratulations, logged in successfully", Toast.LENGTH_LONG);
            success.show();
            helper.userName = userName;
            if(helper.checkPaymentExists()) {
                Intent myintent=new Intent(this, OptionsActivity.class).putExtra("user", userName);
                startActivity(myintent);
            }else{
                getPaymentInfo();
            }
        } else{
            Toast failure =  Toast.makeText(MainActivity.this, "Sorry, wrong username or password", Toast.LENGTH_LONG);
            failure.show();
        }
    }
}

