package com.example.fyphomeowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;

public class businessReviewActivity extends AppCompatActivity {

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

    private String companyName;
    private TextView reviewTitle;
    private RatingBar ratingBar;
    private Button reviewBtn;
    private EditText reviewTxt;

    private SharedPreferences sharedPreferencesHomeowner;
    private SharedPreferences sharedPreferencesCompany;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_review);
        //Shared preferences
        sharedPreferencesHomeowner = getSharedPreferences("homeownerPref", MODE_PRIVATE);
        sharedPreferencesCompany = getSharedPreferences("companyPref", MODE_PRIVATE);
        companyName = sharedPreferencesCompany.getString("companyName","");

        //Views
        reviewTitle = findViewById(R.id.reviewTitle);
        ratingBar = findViewById(R.id.ratingBar);
        reviewTxt = findViewById(R.id.currBill);
        reviewBtn = findViewById(R.id.reviewBtn);
        reviewTitle.setText("Rate and review " + companyName);
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Homeowner ID:", sharedPreferencesHomeowner.getString("userID",""));
                Log.d("Company ID:", sharedPreferencesCompany.getString("companyID",""));
                Log.d("review :", reviewTxt.getText().toString());
                Log.d("Rating :", String.valueOf((int)ratingBar.getRating()));
                Toast.makeText(businessReviewActivity.this, String.valueOf((int)ratingBar.getRating()), Toast.LENGTH_SHORT).show();
                //CONNECT DB
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url = "http://192.168.1.168/fyp/businessReviewRequest.php";

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("tagconvertstr", "["+response+"]");
                                //checking response for success directly because php file echos "success"
                                if (response.equals("success")) {
                                    Toast.makeText(businessReviewActivity.this, "Company successfully reviewed", Toast.LENGTH_SHORT).show();
                                    Log.d("tag", "successfully reviewed company");
                                    finish();
                                }
                                else {
                                    Log.d("Error", response);
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
                        paramV.put("homeownerID", sharedPreferencesHomeowner.getString("userID",""));
                        paramV.put("companyID", sharedPreferencesCompany.getString("companyID",""));
                        paramV.put("review", reviewTxt.getText().toString());
                        paramV.put("noOfStars", String.valueOf((int)ratingBar.getRating()));
                        return paramV;
                    }
                };
                queue.add(stringRequest);
            }
        });

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