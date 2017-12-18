package com.example.maupi.parkking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ahmedsameh19997 on 12/1/2017.
 */

public class EditProfileActivity extends AppCompatActivity {

    DatabaseHelper helper = new DatabaseHelper(this);
    String username;
    private Button saveChanges;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        username = getIntent().getStringExtra("user");
        String user_pass = helper.getClientInfo(username , "pass");
        String user_email = helper.getClientInfo(username , "email");

        EditText modifiedUser = (EditText) findViewById(R.id.set_username);
        modifiedUser.append(username);

        EditText modifiedPass = (EditText) findViewById(R.id.set_password);
        modifiedPass.append(user_pass);

        EditText modifiedEmail = (EditText) findViewById(R.id.set_email);
        modifiedEmail.append(user_email);

        saveChanges = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);


        saveChanges.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onSaveChangesClick();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent myintent=new Intent(EditProfileActivity.this, OptionsActivity.class).putExtra("user" , username);
                startActivity(myintent);
            }
        });

    }

    public void onSaveChangesClick(){

        EditText user = (EditText) findViewById(R.id.set_username);
        EditText pass = (EditText) findViewById(R.id.set_password);
        EditText mail = (EditText) findViewById(R.id.set_email);

        String user_name = user.getText().toString();
        String password = pass.getText().toString();
        String email = mail.getText().toString();

        // Make sure the user enters data in all the fields
        if(user_name.isEmpty() || password.isEmpty() || email.isEmpty()){
            if(user_name.isEmpty())
                user.setError("Username is required");
            else if(password.isEmpty())
                pass.setError("Password is required");
            else
                mail.setError("Email is required");
            return;
        }

        // Make sure the user enters a valid email address
        if(!isValidEmailAddress(email)){
            mail.setError("Please enter a valid email address");
            return;
        }

        helper.modifyClient(username , user_name , "uname");
        helper.modifyClient(user_name , password , "pass");
        helper.modifyClient(user_name , email , "email");

        Toast.makeText(this , "Your information has been updated." , Toast.LENGTH_LONG).show();
        Intent myintent=new Intent(this, OptionsActivity.class).putExtra("user", user_name);
        startActivity(myintent);
    }

    boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
