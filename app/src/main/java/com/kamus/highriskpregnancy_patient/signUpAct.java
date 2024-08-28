package com.kamus.highriskpregnancy_patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class signUpAct extends AppCompatActivity {
    TextView Nama;
    EditText usrname;
    Button signup;
    MySQLiteHelper dbhelper;
    dbActivities db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        db = new dbActivities();
        dbhelper = new MySQLiteHelper(this);

        Nama = findViewById(R.id.textNama);
        usrname = findViewById(R.id.editTextNama);
        signup = findViewById(R.id.buttonsignup);
        saveinfo();

    }

    public void saveinfo(){
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText txtname = findViewById(R.id.editTextNama);
                db.SaveDB(dbhelper,0,txtname.getText().toString());
                Intent intent = new Intent(signUpAct.this, loginPage.class);
                startActivity(intent);


            }
        });
    }
}