package com.uph.uph;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
        EditText home_username , home_password;
        LocalDatabase localDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home_username = (EditText) findViewById(R.id.Username);
        home_password = (EditText) findViewById(R.id.Password);
        localDatabase = new LocalDatabase(this);
    }


    public void OnSignInClick (View view) {


            String username = home_username.getText().toString();
            String password = home_password.getText().toString();

            Contact contact = new Contact(username , password);
            authenticate(contact);


        }

    private void authenticate(Contact contact) {

        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.fetchDataInBackground(contact, new GetUserCallback() {
            @Override
            public void done(Contact returnedContact) {

                if(returnedContact == null) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Username & Password don't match!");
                    builder.setPositiveButton("OK", null);
                    builder.show();

                }else {

                    localDatabase.storeData(returnedContact);
                    localDatabase.setUserLoggedIn(true);

                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    String username = home_username.getText().toString();
                    String password = home_password.getText().toString();
                    i.putExtra("Username", username);
                    startActivity(i);

                }

            }
        });

    }

}
