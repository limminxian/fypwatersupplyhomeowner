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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.Iterator;
import java.util.Map;

public class editTicketActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imageMenuView;

    private EditText descriptionTxt;
    private Button editTicketBtn;
    private Spinner ticketTypeSpinner;

    private SharedPreferences sharedPreferencesHomeowner;
    private SharedPreferences sharedPreferencesTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ticket);
        sharedPreferencesHomeowner = getSharedPreferences("homeownerPref", MODE_PRIVATE);
        sharedPreferencesTicket = getSharedPreferences("ticketPref", MODE_PRIVATE);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        editTicketBtn = findViewById(R.id.editTicketBtn);
        ticketTypeSpinner = findViewById(R.id.ticketTypeSpinner);

        setSpinner();
        setDescription();

        editTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTicket();
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

    public void setSpinner(){
        //CONNECT DB
        //Set service types in spinner
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.168/fyp/getServiceTypeRequest.php";

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
                                JSONArray ticketTypesObj = jsonObject.getJSONArray("ticketTypes");
                                ArrayList<String> ticketType = new ArrayList<>();
                                for(int i = 0; i < ticketTypesObj.length(); i++ ){
                                    ticketType.add(ticketTypesObj.getString(i));
                                }
                                ArrayAdapter<String> ticketAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, ticketType);
                                ticketTypeSpinner.setAdapter(ticketAdapter);
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
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    public void setDescription(){
        //CONNECT DB
        //Set service types in spinner
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.168/fyp/ticketRequest.php";

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
                                String description = jsonObject.getString("description");
                                Log.d("ID: ",jsonObject.getString("id") );
                                if(description != "null"){
                                    descriptionTxt.setText(description);
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
//                paramV.put("ticketID", sharedPreferencesTicket.getString("ticketID",""));
                paramV.put("ticketID", "6");
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    public void editTicket(){
        //CONNECT DB
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.168/fyp/editTicketRequest.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("tagconvertstr", "["+response+"]");
                        if (response.equals("success")){
                            Log.d("success", response);
                            Toast.makeText(editTicketActivity.this, "Ticket successfully edited", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d("error", response);
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
//                paramV.put("ticketID", sharedPreferencesTicket.getString("ticketID",""));
                paramV.put("ticketID", "6");
                paramV.put("type", ticketTypeSpinner.getSelectedItem().toString());
                Log.d("Selected: ", ticketTypeSpinner.getSelectedItem().toString());
                paramV.put("description", descriptionTxt.getText().toString());
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