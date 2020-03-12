package com.example.codeondroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codeondroid.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLData;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "";
    public static final int GET_FROM_GALLERY = 3;
    Button submitB,but1;
    EditText usernameET,passwordET,emailET;
    Spinner favLangS;
    String favLang = "";
    SQLiteDatabase db;
    ImageView img;

    Bitmap bitmap = null;
    private static final int SELECT_PICTURE = 0;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameET = findViewById(R.id.username);
        passwordET = findViewById(R.id.password);
        emailET = findViewById(R.id.email);
        submitB = findViewById(R.id.submit);
        img = (ImageView) findViewById(R.id.icon);
        but1 = (Button) findViewById(R.id.butProfile);



        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegistrationActivity.this,"Profile Photo",Toast.LENGTH_SHORT).show();
            }
        });


        favLangS = findViewById(R.id.favLang);
        ArrayAdapter adap1=ArrayAdapter.createFromResource(this,R.array.Favourite_Lang,android.R.layout.simple_list_item_activated_1);
        favLangS.setAdapter(adap1);
        favLangS.setOnItemSelectedListener(this);

        submitB.setOnClickListener(this);

        db = openOrCreateDatabase("CodeEditorDB",MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS users(username VARCHAR,password VARCHAR,email VARCHAR,favLang VARCHAR);"); //CHANGE FOR IMAGE   , profilePhoto BLOB
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                img.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();//CHANGE FOR IMAGE IN DB
    }




    @Override
    public void onClick(View v) {
        String username = usernameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String email = emailET.getText().toString().trim();

//        byte[] profilePhoto = getBitmapAsByteArray(bitmap); //CHANGE FOR IMAGE IN DB

//        Toast.makeText(getApplicationContext(),username + " " + password,Toast.LENGTH_LONG).show();

        if(username.equals("") || password.equals("") || email.equals("") || favLang.equals("") || favLang.equals("Choose Your Favourite Language")){
            Toast.makeText(getApplicationContext(),"Enter All Fields",Toast.LENGTH_LONG).show();
            return;
        }
        Cursor c = db.rawQuery("SELECT * FROM users WHERE username ='" + username + "'", null);
//
        if(c.getCount() != 0){
            Toast.makeText(getApplicationContext(),"Username already exits",Toast.LENGTH_LONG).show();
            return;
        }
        db.execSQL("INSERT INTO users VALUES('" + username+ "','" + password + "','" + email  + "','" + favLang + "');");//,profilePhoto

        String output = "";
        c = db.rawQuery("SELECT * FROM users", null);
//        while (c.moveToNext())
//        {
//            output += "Username: " + c.getString(0) + "\n";
//            output += "Email: " + c.getString(2) + "\n";
//        }
//        showMessage("Student Details", output);

        Toast.makeText(getApplicationContext(),"Successfully Registered",Toast.LENGTH_LONG).show();
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getApplicationContext(),"Clicked " ,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView txt = (TextView) view;
        favLang = txt.getText().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void goBack(View v){
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
    }

}



