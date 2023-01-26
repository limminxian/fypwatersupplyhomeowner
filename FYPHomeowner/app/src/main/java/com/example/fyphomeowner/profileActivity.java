package com.example.fyphomeowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class profileActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imageMenuView;
    private String name;
    private String phoneNo;
    private String email;
    private String street;
    private String blockNo;
    private String unitNo;
    private String postalCode;
    private String houseType;
    private String householdSize;
    private TextView profileTitle;
    private TextView nameTxt;
    private TextView numberTxt;
    private TextView emailTxt;
    private TextView streetTxt;
    private TextView blockNoTxt;
    private TextView unitNoTxt;
    private TextView postalCodeTxt;
    private TextView houseTypeTxt;
    private TextView householdSizeTxt;
    private Button editProfileBtn;
    private SwitchCompat alertToggle;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPreferences = getSharedPreferences("homeownerPref", MODE_PRIVATE);

        //if user is logged out, redirect them to the login activity
        if (sharedPreferences.getString("logged", "false").equals("false")) {
            openLoginPage();
        }

        editProfileBtn = findViewById(R.id.editProfileBtn);
        alertToggle = findViewById(R.id.alertToggle);
        profileTitle = findViewById(R.id.profileTitle);
        nameTxt = findViewById(R.id.nameTxt);
        numberTxt = findViewById(R.id.numberTxt);
        emailTxt = findViewById(R.id.emailTxt);
        streetTxt = findViewById(R.id.streetTxt);
        blockNoTxt = findViewById(R.id.blockNoTxt);
        unitNoTxt = findViewById(R.id.unitNoTxt);
        postalCodeTxt = findViewById(R.id.postalCodeTxt);
        houseTypeTxt = findViewById(R.id.houseTypeTxt);
        householdSizeTxt = findViewById(R.id.householdSizeTxt);

        //CONNECT DB
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.168/fyp/profileRequest.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //changing the response to a JSONobject because the php file is response is a JSON file
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("message");
                            if (status.equals("success")){
                                name = jsonObject.getString("name");
                                phoneNo = jsonObject.getString("phoneNo");
                                email = jsonObject.getString("email");
                                street = jsonObject.getString("street");
                                blockNo = jsonObject.getString("blockNo");
                                unitNo = jsonObject.getString("unitNo");
                                postalCode = jsonObject.getString("postalCode");
                                houseType = jsonObject.getString("houseType");
                                householdSize = jsonObject.getString("householdSize");

                                profileTitle.setText(profileTitle.getText().toString().concat(name));
                                nameTxt.setText(name);
                                numberTxt.setText(phoneNo);
                                emailTxt.setText(email);
                                streetTxt.setText(street);
                                blockNoTxt.setText(blockNo);
                                unitNoTxt.setText(unitNo);
                                postalCodeTxt.setText(postalCode);
                                houseTypeTxt.setText(houseType);
                                householdSizeTxt.setText(householdSize);
                            }
                            else {
                                Log.d("error", message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> paramV = new HashMap<>();
                paramV.put("userID", sharedPreferences.getString("userID",""));
                return paramV;
            }
        };
        queue.add(stringRequest);

        //NAVIGATION MENU
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        imageMenuView = findViewById(R.id.imageMenu);
        imageMenuView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.home:
                        openHomePage();
                        break;
                    case R.id.profile:
                        openProfilePage();
                        break;
                    case R.id.waterUsage:
                        openWaterUsagePage();
                        break;
                    case R.id.paymentMethods:
                        openPaymentMethodsPage();
                        break;
                    case R.id.bills:
                        openBillsPage();
                        break;
                    case R.id.tickets:
                        openTicketsPage();
                        break;
                    case R.id.business:
                        openBusinessPage();
                        break;
                    case R.id.aboutUs:
                        openAboutPage();
                        break;
                    case R.id.logout:
                        openLoginPage();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(), editProfileActivity.class));
            }
        });
    }

    //REDIRECT METHODS
    public void openHomePage(){
        Intent intent = new Intent(this, dashboardActivity.class);
        startActivity(intent);
    }

    public void openProfilePage(){
        Intent intent = new Intent(this, profileActivity.class);
        startActivity(intent);
    }

    public void openWaterUsagePage(){
        Intent intent = new Intent(this, waterUsageActivity.class);
        startActivity(intent);
    }
    public void openPaymentMethodsPage(){
        Intent intent = new Intent(this, paymentMethodsActivity.class);
        startActivity(intent);
    }

    public void openBillsPage(){
        Intent intent = new Intent(this, billsActivity.class);
        startActivity(intent);
    }
    public void openTicketsPage(){
        Intent intent = new Intent(this, ticketsActivity.class);
        startActivity(intent);
    }

    public void openBusinessPage(){
        Intent intent = new Intent(this, businessViewActivity.class);
        startActivity(intent);
    }
    public void openSettingsPage(){
        Intent intent = new Intent(this, settingsActivity.class);
        startActivity(intent);
    }

    public void openAboutPage(){
        Intent intent = new Intent(this, aboutActivity.class);
        startActivity(intent);
    }

    public void openLoginPage(){
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
    }
}