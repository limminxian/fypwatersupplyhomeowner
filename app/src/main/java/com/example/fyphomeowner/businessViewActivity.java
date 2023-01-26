package com.example.fyphomeowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class businessViewActivity extends AppCompatActivity implements businessRecyclerAdapter.BusinessClickListner {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imageMenuView;
    private SearchView searchView;
    private ArrayList<Company> businessList;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferencesHomeowner;
    private SharedPreferences sharedPreferencesCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_view);

        //SHARED PREFERENCES
        sharedPreferencesHomeowner = getSharedPreferences("homeownerPref", MODE_PRIVATE);
        sharedPreferencesCompany = getSharedPreferences("companyPref", MODE_PRIVATE);
        //reset chosen company
        SharedPreferences.Editor editor = sharedPreferencesCompany.edit();
        editor.putString("companyID", "");
        editor.apply();

        //RECYCLER VIEW
        recyclerView = findViewById(R.id.businessRecyclerView);
        businessList = new ArrayList<>();
        setUpBusinessViews();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //SEARCH VIEW
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                //filterList(s, adapter);
                return true;
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

    //Passes the company object into the profile activity by implementing the Parcelable on the Company class
    public void openBusinessProfilePage(Company company){
        Intent intent = new Intent(this, businessProfileActivity.class);
        intent.putExtra("companyKey", company);
        startActivity(intent);
    }

    //SET UP BUSINESS RECYCLER views
    private void setUpBusinessViews(){
        ArrayList<String> attrNameList = new ArrayList<>();
        attrNameList.add("companyID");
        attrNameList.add("name");
        attrNameList.add("phoneNo");
        attrNameList.add("street");
        attrNameList.add("postalCode");
        attrNameList.add("description");
        attrNameList.add("noOfStars");
        attrNameList.add("noOfRate");

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.168/fyp/businessViewRequest.php";
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
                                int rowNo = 1;
                                boolean EOF = false;
                                //while loop to loop through all the company objects created in php
                                while(!EOF){
                                    String companyRow = "Company row " + String.valueOf(rowNo);
                                    //Check if that row no. exists if not set EOF as true
                                    if(jsonObject.has(companyRow)){
                                        //Get company json object from the array
                                        JSONObject companyObj = jsonObject.getJSONObject(companyRow);

                                        //Create a company object
                                        Integer companyID;
                                        Integer phoneNo;
                                        Integer postalCode;
                                        Integer noOfStars;
                                        Integer noOfRate;
                                        String name = companyObj.getString("name");
                                        String street = companyObj.getString("street");
                                        String description = companyObj.getString("description");

                                        if(!companyObj.isNull("companyID")){
                                            companyID = Integer.parseInt(companyObj.getString("companyID"));
                                        }else{
                                            companyID = null;
                                        }
                                        if(!companyObj.isNull("phoneNo")){
                                            phoneNo = Integer.parseInt(companyObj.getString("phoneNo"));
                                        }else{
                                            phoneNo = null;
                                        }
                                        if(!companyObj.isNull("postalCode")){
                                            postalCode = Integer.parseInt(companyObj.getString("postalCode"));
                                        }else{
                                            postalCode = null;
                                        }
                                        if(!companyObj.isNull("noOfStars")){
                                            noOfStars = Integer.parseInt(companyObj.getString("noOfStars"));
                                        }else{
                                            noOfStars = null;
                                        }
                                        if(!companyObj.isNull("noOfRate")){
                                            noOfRate = Integer.parseInt(companyObj.getString("noOfRate"));
                                        }else{
                                            noOfRate = null;
                                        }

                                        Company company = new Company(companyID, name, phoneNo, street, postalCode, description, noOfStars);
                                        businessList.add(company);
                                    }else{
                                        EOF = true;
                                    }
                                    rowNo++;
                                } //end of while loop

                                businessRecyclerAdapter adapter = new businessRecyclerAdapter(businessViewActivity.this, businessList, businessViewActivity.this);
                                recyclerView.setAdapter(adapter);
                                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String s) {
                                        return false;
                                    }
                                    @Override
                                    public boolean onQueryTextChange(String s) {
                                        filterList(s, adapter);
                                        return true;
                                    }
                                });
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
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    //SEARCH FLITER
    private void filterList(String text, businessRecyclerAdapter adapter){
        ArrayList<Company> filteredList = new ArrayList<>();

        for(int i = 0; i < businessList.size(); i++){
            Company company = businessList.get(i);
            String cname = company.getName().toLowerCase();
            text = text.toLowerCase();
            if(text.equals(cname)){
                filteredList.add(businessList.get(i));
            }
        }
        adapter.setFilteredList(filteredList);
    }

    //RECYCLER ONCLICK
    @Override
    public void selectedBusiness(Company company) {
        SharedPreferences.Editor editor = sharedPreferencesCompany.edit();
        editor.putString("companyID", String.valueOf(company.getID()));
        editor.apply();
        openBusinessProfilePage(company);
    }
}
