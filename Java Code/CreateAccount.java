package com.example.maupi.parkking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {

    DatabaseHelper helper = new DatabaseHelper(this);
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onSignUpClick(view);
            }
        });
    }
    public void getPaymentInfo(String user_name){
        Intent intent = new Intent(this, getPaymentInfo.class).putExtra("user" , user_name);
        startActivity(intent);
    }

    // Functions that needs to be called after the user submits his new account information

    public void onSignUpClick(View v){

        if(v.getId() == R.id.submit){

            EditText uname = (EditText)findViewById(R.id.uname);
            EditText pass1 = (EditText)findViewById(R.id.pass1);
            EditText pass2 = (EditText)findViewById(R.id.pass2);
            EditText email = (EditText)findViewById(R.id.email);

            String user_name = uname.getText().toString();
            String pass_str1 = pass1.getText().toString();
            String pass_str2 = pass2.getText().toString();
            String user_email = email.getText().toString();

            // Make sure the user enters data in all the fields
            if(user_name.isEmpty() || pass_str1.isEmpty() || pass_str2.isEmpty() || user_email.isEmpty()){
                if(user_name.isEmpty())
                    uname.setError("Username is required");
                else if(pass_str1.isEmpty())
                    pass1.setError("Password is required");
                else if(pass_str2.isEmpty())
                    pass2.setError("Password verification is required");
                else
                    email.setError("Email is required");
                return;
            }

            // Make sure the user enters a valid email address
            if(!isValidEmailAddress(user_email)){
                email.setError("Please enter a valid email address");
                return;
            }

            // Make sure the user verifies the password
            if(!pass_str1.equals(pass_str2)){

                Toast pass =  Toast.makeText(CreateAccount.this, "Passwords don't match", Toast.LENGTH_SHORT);
                pass.show();

            }else{

                if(helper.uniqueUname(user_name)) {
                    client c = new client();
                    c.setEmail(user_email);
                    c.setPass(pass_str1);
                    c.setUname(user_name);

                    helper.insertContact(c);
                    Toast pass = Toast.makeText(CreateAccount.this, "congratulations, successfully created the new account", Toast.LENGTH_LONG);
                    pass.show();
                    getPaymentInfo(user_name);
                } else{

                    Toast unique =  Toast.makeText(CreateAccount.this, "Username already exists", Toast.LENGTH_LONG);
                    unique.show();
                }
            }
        }
    }


    boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

}
