package com.example.fyphomeowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class subscriptionActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imageMenuView;

    private String name;
    private Integer PhoneNo;
    private String street;
    private Integer postalCode;
    private String Description;
    private Integer noOfStars;
    private TextView companyTitle;
    private TextView emailTxt;
    private TextView phoneTxt;
    private TextView addressTxt;
    private TextView descriptionTxt;
    private TextView serviceTxt;
    private TextView serviceRateTxt;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private Button subBtn;
    private Button unsubBtn;
    private Button reviewBtn;

    private SharedPreferences sharedPreferencesHomeowner;
    private SharedPreferences sharedPreferencesCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        //Shared preferences
        sharedPreferencesHomeowner = getSharedPreferences("homeownerPref", MODE_PRIVATE);
        sharedPreferencesCompany = getSharedPreferences("companyPref", MODE_PRIVATE);

        //Views
        companyTitle = findViewById(R.id.companyTitle);
        emailTxt = findViewById(R.id.emailTxt);
        phoneTxt = findViewById(R.id.phoneTxt);
        addressTxt = findViewById(R.id.addressTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        serviceTxt = findViewById(R.id.serviceTxt);
        serviceRateTxt = findViewById(R.id.serviceRateTxt);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        subBtn = findViewById(R.id.subscriptionBtn);
        unsubBtn = findViewById(R.id.unsubscriptionBtn);
        reviewBtn = findViewById(R.id.reviewBtn);

        //CONNECT DB
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.168/fyp/businessProfileRequest.php";

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
                                companyTitle.setText(jsonObject.getString("name"));
                                emailTxt.setText(jsonObject.getString("email"));
                                phoneTxt.setText(jsonObject.getString("phoneNo"));
                                addressTxt.setText(jsonObject.getString("address"));
                                descriptionTxt.setText(jsonObject.getString("description"));
                                serviceTxt.setText(jsonObject.getString("services"));
                                serviceRateTxt.setText(jsonObject.getString("serviceRates"));
                                if(!jsonObject.isNull("noOfStars")){
                                    noOfStars = jsonObject.getInt("noOfStars");
                                }else{
                                    noOfStars = 0;
                                }
                                switch (noOfStars){
                                    case 1:
                                        star1.setImageResource(R.drawable.ic_star);
                                        star2.setImageResource(R.drawable.ic_empty_star);
                                        star3.setImageResource(R.drawable.ic_empty_star);
                                        star4.setImageResource(R.drawable.ic_empty_star);
                                        star5.setImageResource(R.drawable.ic_empty_star);
                                        break;
                                    case 2:
                                        star1.setImageResource(R.drawable.ic_star);
                                        star2.setImageResource(R.drawable.ic_star);
                                        star3.setImageResource(R.drawable.ic_empty_star);
                                        star4.setImageResource(R.drawable.ic_empty_star);
                                        star5.setImageResource(R.drawable.ic_empty_star);
                                        break;
                                    case 3:
                                        star1.setImageResource(R.drawable.ic_star);
                                        star2.setImageResource(R.drawable.ic_star);
                                        star3.setImageResource(R.drawable.ic_star);
                                        star4.setImageResource(R.drawable.ic_empty_star);
                                        star5.setImageResource(R.drawable.ic_empty_star);
                                        break;
                                    case 4:
                                        star1.setImageResource(R.drawable.ic_star);
                                        star2.setImageResource(R.drawable.ic_star);
                                        star3.setImageResource(R.drawable.ic_star);
                                        star4.setImageResource(R.drawable.ic_star);
                                        star5.setImageResource(R.drawable.ic_empty_star);
                                        break;
                                    case 5:
                                        star1.setImageResource(R.drawable.ic_star);
                                        star2.setImageResource(R.drawable.ic_star);
                                        star3.setImageResource(R.drawable.ic_star);
                                        star4.setImageResource(R.drawable.ic_star);
                                        star5.setImageResource(R.drawable.ic_star);
                                        break;
                                    default:
                                        star1.setImageResource(R.drawable.ic_empty_star);
                                        star2.setImageResource(R.drawable.ic_empty_star);
                                        star3.setImageResource(R.drawable.ic_empty_star);
                                        star4.setImageResource(R.drawable.ic_empty_star);
                                        star5.setImageResource(R.drawable.ic_empty_star);
                                        break;
                                }
                                boolean subscribed = jsonObject.getBoolean("subscribed");
                                if(subscribed){
                                    subBtn.setVisibility(View.GONE);
                                    unsubBtn.setVisibility(View.VISIBLE);
                                    reviewBtn.setVisibility(View.VISIBLE);
                                    unsubBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//                                            openSubscriptionPage();
                                        }
                                    });
                                    reviewBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
//                                            openSubscriptionPage();
                                        }
                                    });
                                }else{
                                    subBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            openSubscriptionPage();
                                        }
                                    });
                                }
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
                paramV.put("userID", sharedPreferencesHomeowner.getString("userID",""));
                paramV.put("companyID", sharedPreferencesCompany.getString("companyID",""));
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

    public void openSubscriptionPage(){
        Intent intent = new Intent(this, subscriptionActivity.class);
        startActivity(intent);
    }
}