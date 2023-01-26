package com.example.fyphomeowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class paymentActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imageMenuView;

    private SharedPreferences sharedPreferences;
    private Button makePaymentBtn;
    private Month currMonthStr;
    private int currYear;
    private int currMonth;
    private LocalDate currDate;
    private TextView billTitle;
    private TextView billTxt;

    private PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;
    private PaymentSheet.CustomerConfiguration configuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        sharedPreferences = getSharedPreferences("homeownerPref", MODE_PRIVATE);
        billTitle = findViewById(R.id.billTitle);
        billTxt = findViewById(R.id.billTxt);
        //Get current date
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currDate = LocalDate.now();
            currMonthStr = currDate.getMonth();
            currMonth = currDate.getMonthValue();
            currYear = currDate.getYear();
        }
        billTitle.setText("Bills for " + currMonthStr);

        getBills(billTxt, currMonth, currYear);

        makePaymentBtn = findViewById(R.id.makePaymentBtn);
        makePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(paymentIntentClientSecret != null){
                    paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, new PaymentSheet.Configuration("Codes easy", configuration));
                }else{
                    Toast.makeText(paymentActivity.this, "loading", Toast.LENGTH_SHORT).show();
                }
            }
        });
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

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

    public void onPaymentSheetResult(final PaymentSheetResult paymentSheetResult){
        if(paymentSheetResult instanceof PaymentSheetResult.Canceled){
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        }
        if(paymentSheetResult instanceof PaymentSheetResult.Failed){
            Toast.makeText(this, ((PaymentSheetResult.Failed) paymentSheetResult).getError().getMessage(), Toast.LENGTH_SHORT).show();
        }
        if(paymentSheetResult instanceof PaymentSheetResult.Completed){
            Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        }
    }

    //address line (street, blkno, unitno)

    public void fetchAPI(String name, String addressLine, String postalCode, String amount, String description){
        //CONNECT DB
        //Set service types in spinner
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.168/stripe-android-api/vendor/";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //changing the response to a JSONobject because the php file is response is a JSON file
                            Log.i("tagconvertstr", "["+response+"]");
                            JSONObject jsonObject = new JSONObject(response);
                            configuration = new PaymentSheet.CustomerConfiguration(
                              jsonObject.getString("customer"),
                              jsonObject.getString("ephemeralKey")
                            );
                            paymentIntentClientSecret = jsonObject.getString("paymentIntent");
                            PaymentConfiguration.init(getApplicationContext(), jsonObject.getString("publishableKey"));

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
                paramV.put("name", name);
                paramV.put("addressLine", addressLine);
                paramV.put("postalCode", postalCode);
                paramV.put("amount", amount);
                paramV.put("description", description);
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    public void getBills(TextView textView, int month, int year){
        //CONNECT DB
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.168/fyp/viewBillsRequest.php";

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
                                String bills = "";
                                JSONObject billsObj = jsonObject.getJSONObject("serviceBills");
                                Iterator<String> keys = billsObj.keys();
                                while(keys.hasNext()){
                                    String key = keys.next();
                                    String serviceName = key;
                                    JSONObject serviceBillObj = billsObj.getJSONObject(key);
                                    String amount = serviceBillObj.getString("amount");
                                    String payment = serviceBillObj.getString("payment");
                                    String paymentDate = serviceBillObj.getString("paymentDate");
                                    bills += serviceName +": "+ amount + "\n";
                                }
                                bills += "Total: " + jsonObject.getString("totalAmount");
                                textView.setText(bills);

                                //fetch API
                                String name = jsonObject.getString("name");
                                String addressLine = jsonObject.getString("addressLine");
                                String postalCode = jsonObject.getString("postalCode");
                                String totalAmount = jsonObject.getString("totalAmount");
                                String description = "Bills for: " + currMonthStr;
                                fetchAPI(name, addressLine, postalCode, totalAmount, description);
                            }
                            else {
                                Log.d("error", message);
                                textView.setText("no bills found");
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
                paramV.put("month", String.valueOf(month));
                paramV.put("year", String.valueOf(year));
                return paramV;
            }
        };
        queue.add(stringRequest);
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