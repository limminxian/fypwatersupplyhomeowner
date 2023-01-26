package com.example.fyphomeowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
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
import android.widget.NumberPicker;
import android.widget.Spinner;
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

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class editProfileActivity extends AppCompatActivity {

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
    private EditText nameTxt;
    private EditText phoneNoTxt;
    private EditText emailTxt;
    private EditText streetTxt;
    private EditText blockNoTxt;
    private EditText unitNoTxt;
    private EditText postalCodeTxt;
    private Spinner houseTypesSpinner;
    private NumberPicker householdSizePicker;
    private Button changePasswordBtn;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        sharedPreferences = getSharedPreferences("homeownerPref", MODE_PRIVATE);

        //if user is logged out, redirect them to the login activity
        if (sharedPreferences.getString("logged", "false").equals("false")) {
            openLoginPage();
        }

        nameTxt = findViewById(R.id.nameTxt);
        phoneNoTxt = findViewById(R.id.phoneNoTxt);
        emailTxt = findViewById(R.id.emailTxt);
        streetTxt = findViewById(R.id.streetTxt);
        blockNoTxt = findViewById(R.id.blockNoTxt);
        unitNoTxt = findViewById(R.id.unitNoTxt);
        postalCodeTxt = findViewById(R.id.postalCodeTxt);
        houseTypesSpinner = findViewById(R.id.houseTypesSpinner);
        householdSizePicker = findViewById(R.id.householdSizePicker);
        changePasswordBtn = findViewById(R.id.changePasswordBtn);

        //SPINNER
        //Array list for spinner
        ArrayList<String> houseTypes = new ArrayList<>();
        houseTypes.add("1-Room Flat");
        houseTypes.add("2-Room Flat");
        houseTypes.add("3-Room Flat");
        houseTypes.add("4-Room Flat");
        houseTypes.add("5-Room Flat");
        houseTypes.add("Executive Flat");
        houseTypes.add("Executive Condo");
        houseTypes.add("Private Condo");
        houseTypes.add("Apartment");
        houseTypes.add("Semi Detached House");
        houseTypes.add("Terrace House");
        houseTypes.add("Shop House");
        houseTypes.add("Bungalow House");

        //Add the arraylist into the spinner
        ArrayAdapter<String> houseTypesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, houseTypes);
        houseTypesSpinner.setAdapter(houseTypesAdapter);

        //NUMBER PICKER
        //Set min max for the number picker
        householdSizePicker.setMinValue(1);
        householdSizePicker.setMaxValue(20);

        //CONNECT DB
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.168/fyp/profileRequest.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //changing the response to a JSONobject because the php file is response is a JSON file
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

                                nameTxt.setText(name);
                                phoneNoTxt.setText(phoneNo);
                                emailTxt.setText(email);
                                streetTxt.setText(street);
                                blockNoTxt.setText(blockNo);
                                unitNoTxt.setText(unitNo);
                                postalCodeTxt.setText(postalCode);
                                householdSizePicker.setValue(Integer.parseInt(householdSize));

                                int houseTypeArrayPos = houseTypesAdapter.getPosition(houseType);
                                houseTypesSpinner.setSelection(houseTypeArrayPos);

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

        changePasswordBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                AlertDialog.Builder builder = new AlertDialog.Builder(editProfileActivity.this);

                final View changePasswordLayout = getLayoutInflater().inflate(R.layout.change_password_alert_dialog, null);
                builder.setView(changePasswordLayout);

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                });

                AlertDialog alertDialog = builder.create();

                alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button posBtn = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        posBtn.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                // TODO Do something
                                EditText oldPasswordTxt =  changePasswordLayout.findViewById(R.id.oldPasswordTxt);
                                EditText newPasswordTxt = changePasswordLayout.findViewById(R.id.newPasswordTxt);
                                EditText cmfNewPasswordTxt = changePasswordLayout.findViewById(R.id.cmfNewPasswordTxt);
                                String oldPassword = oldPasswordTxt.getText().toString();
                                String newPassword = newPasswordTxt.getText().toString();
                                String cmfNewPassword = cmfNewPasswordTxt.getText().toString();

                                String url = "http://192.168.1.168/fyp/editPasswordRequest.php";
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equals("success")){
                                                    Toast.makeText(editProfileActivity.this, "password changed successfully", Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();
                                                }
                                                else {
                                                    Log.d("error", response);
                                                    Toast.makeText(editProfileActivity.this, response, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                    }
                                })
                                {
                                    protected Map<String, String> getParams() {
                                        Map<String, String> paramV = new HashMap<>();
                                        paramV.put("userID", sharedPreferences.getString("userID",""));
                                        paramV.put("oldPassword", oldPassword);
                                        paramV.put("newPassword", newPassword);
                                        paramV.put("cmfNewPassword", cmfNewPassword);
                                        return paramV;
                                    }
                                };
                                queue.add(stringRequest);
                            }
                        });

                        Button negBtn = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                        negBtn.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });
    }

    public void onSubmit(View view) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.168/fyp/editProfileRequest.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("success")) {
                            Toast.makeText(getApplicationContext(), "Successfully edited profile", Toast.LENGTH_SHORT).show();
                            openProfilePage();
                        }
                        else {
                            Log.d("Error", response);
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        }
                        Log.d("Error", response);
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
                paramV.put("userID", sharedPreferences.getString("userID",""));
                paramV.put("name", String.valueOf(nameTxt.getText()));
                paramV.put("email", String.valueOf(emailTxt.getText()));
                paramV.put("street", String.valueOf(streetTxt.getText()));
                paramV.put("blockNo", String.valueOf(blockNoTxt.getText()));
                paramV.put("unitNo", String.valueOf(unitNoTxt.getText()));
                paramV.put("postalCode", String.valueOf(postalCodeTxt.getText()));
                paramV.put("phoneNo", String.valueOf(phoneNoTxt.getText()));
                paramV.put("houseType", houseTypesSpinner.getSelectedItem().toString());
                paramV.put("householdSize", String.valueOf(householdSizePicker.getValue()));
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