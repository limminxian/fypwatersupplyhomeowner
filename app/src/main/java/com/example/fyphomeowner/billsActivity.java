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
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class billsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imageMenuView;
    private SharedPreferences sharedPreferences;
    private TextView currBillTxt;
    private TextView prevBillTxt;
    private TextView billTitle;
    private Spinner billMonthSpinner;
    private Spinner billYearSpinner;
    private Button makePaymentBtn;
    private Button prevBillsBtn;
    private LocalDate currDate;
    private Month currMonthStr;
    private int currMonth;
    private int currYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills);
        sharedPreferences = getSharedPreferences("homeownerPref", MODE_PRIVATE);
        billTitle = findViewById(R.id.billTitle);
        currBillTxt = findViewById(R.id.currBillTxt);
        prevBillTxt = findViewById(R.id.prevBillTxt);
        billMonthSpinner = findViewById(R.id.billMonthSpinner);
        billYearSpinner = findViewById(R.id.billYearSpinner);
        makePaymentBtn = findViewById(R.id.makePaymentBtn);
        prevBillsBtn = findViewById(R.id.prevBillsBtn);

        //Get current date
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currDate = LocalDate.now();
            currMonthStr = currDate.getMonth();
            currMonth = currDate.getMonthValue();
            currYear = currDate.getYear();
        }
        billTitle.setText("Bills for " + currMonthStr);

        //SPINNER
        //bills month spinner
        ArrayList<String> months = new ArrayList<>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, months);
        billMonthSpinner.setAdapter(monthsAdapter);
        //bills year spinner
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2015; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        billYearSpinner.setAdapter(yearAdapter);
        int yearArrayPos = yearAdapter.getPosition("2023");
        billYearSpinner.setSelection(yearArrayPos);

        //set current bills text
        getBills(currBillTxt, currMonth, currYear);

        makePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPaymentPage();
            }
        });
        prevBillsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBills(prevBillTxt, billMonthSpinner.getSelectedItemPosition()+1, Integer.parseInt(billYearSpinner.getSelectedItem().toString()));
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
                                int total = 0;
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
                                    total += Integer.parseInt(amount);
                                }
                                bills += "Total: " + String.valueOf(total);
                                textView.setText(bills);
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

    public void openPaymentPage(){
        Intent intent = new Intent(this, paymentActivity.class);
        startActivity(intent);
    }

}