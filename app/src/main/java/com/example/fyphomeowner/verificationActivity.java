package com.example.fyphomeowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class verificationActivity extends AppCompatActivity {

    Button verificationBtn;
    EditText verificationCode;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        verificationBtn = findViewById(R.id.verificationBtn);
        verificationCode = findViewById(R.id.verificationTxt);
        sharedPreferences = getSharedPreferences("verificationPref", MODE_PRIVATE);


        verificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.1.168/fyp/verificationRequest.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //checking response for success directly because php file echos "success"
                                if (response.equals("success")) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("email", "");
                                    editor.apply();

                                    Toast.makeText(verificationActivity.this, "Email has been verified", Toast.LENGTH_SHORT).show();
                                    openLoginPage();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    //Sends data towards the php file to process
                    protected Map<String, String> getParams() {
                        Map<String, String> paramV = new HashMap<>();
                        paramV.put("email", sharedPreferences.getString("email", ""));
                        Log.d("shared preference email", sharedPreferences.getString("email", ""));
                        paramV.put("verificationCode", String.valueOf(verificationCode.getText()));
                        Log.d("verification code", String.valueOf(verificationCode.getText()));
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }

    public void openLoginPage(){
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
    }
}
