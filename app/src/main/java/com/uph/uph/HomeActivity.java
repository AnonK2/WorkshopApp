package com.uph.uph;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

public class HomeActivity extends Activity {
    LocalDatabase localDatabase;
    TextView tvname , tvemail , tvusername , tvpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        localDatabase = new LocalDatabase(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(authenticate() == true)
        {

            displayContactDetails();

        }
        else{

            Intent intent = new Intent(HomeActivity.this , MainActivity.class);
            startActivity(intent);

        }

    }

    private boolean authenticate() {

        return  localDatabase.getUserLoggedIn();

    }

    private  void displayContactDetails() {

        Contact contact = localDatabase.getLoggedInUser();
        tvname.setText(contact.name);
        tvemail.setText(contact.email);
        tvusername.setText(contact.username);
        tvpassword.setText(contact.password);
    }


}
