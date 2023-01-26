package com.example.fyphomeowner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class registerActivity extends AppCompatActivity {
    //Define variables
    private EditText passwordTxt;
    private EditText retypePasswordTxt;     //need validate against pwd
    private EditText emailTxt;
    private EditText streetTxt;
    private EditText blockNoTxt;
    private EditText unitNoTxt;
    private EditText postalCodeTxt;
    private EditText phoneNoTxt;
    private EditText nameTxt;
    private String houseTypeVal;
    private int householdSizeVal;
    private Spinner houseTypesSpinner;              //need to implement get the information
    private NumberPicker householdSizePicker;
    //public static ArrayList<Homeowner> tempHomeownerDB;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        houseTypesSpinner = findViewById(R.id.houseTypesSpinner);
        householdSizePicker = findViewById(R.id.householdSizePicker);
        sharedPreferences = getSharedPreferences("verificationPref", MODE_PRIVATE);

        //RE-ENTER PASSWORD LISTENER        [prompts user for same re-enter password]
        //Define password edittext variables
        passwordTxt = findViewById(R.id.passwordTxt);
        retypePasswordTxt = findViewById(R.id.retypePasswordTxt);
        //Set onFocusChange listener on re-enter password prompt
        retypePasswordTxt.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View view, boolean bool){
                String pwd = passwordTxt.getText().toString();
                String repwd = retypePasswordTxt.getText().toString();

                if(!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(repwd)){
                    if(!bool){
                        //When re-enter password not in focus
                        verifyReEnterPassword(pwd, repwd);
                    }
                }
            }
        });

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
        //set what to when the number changes
        householdSizePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                //textView.setText(String.format("User's Number: %s", newVal))
            }
        });
    }

    public void onRegisterBtnClick(View view) {
        passwordTxt = findViewById(R.id.passwordTxt);                   //implement retype password verification in php file
        retypePasswordTxt = findViewById(R.id.retypePasswordTxt);
        emailTxt = findViewById(R.id.emailTxt);
        streetTxt = findViewById(R.id.streetTxt);
        blockNoTxt = findViewById(R.id.blockNoTxt);
        unitNoTxt = findViewById(R.id.unitNoTxt);
        postalCodeTxt = findViewById(R.id.postalCodeTxt);
        phoneNoTxt = findViewById(R.id.phoneNoTxt);
        nameTxt = findViewById(R.id.nameTxt);
        houseTypeVal = houseTypesSpinner.getSelectedItem().toString();
        householdSizeVal = householdSizePicker.getValue();

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.168/fyp/registerRequest.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //checking response for success directly because php file echos "success"
                        if (response.equals("success")) {
                            //Store the email of user to the verification page
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", String.valueOf(emailTxt.getText()));
                            editor.apply();
                            Toast.makeText(getApplicationContext(), "Registrations successful", Toast.LENGTH_SHORT).show();
                            showPopupWindowClick(view);
                            Log.d("tag", "verification page opening");
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
                paramV.put("name", String.valueOf(nameTxt.getText()));
                paramV.put("password", String.valueOf(passwordTxt.getText()));
                paramV.put("retypePassword", String.valueOf(retypePasswordTxt.getText()));
                paramV.put("email", String.valueOf(emailTxt.getText()));
                paramV.put("street", String.valueOf(streetTxt.getText()));
                paramV.put("blockNo", String.valueOf(blockNoTxt.getText()));
                paramV.put("unitNo", String.valueOf(unitNoTxt.getText()));
                paramV.put("postalCode", String.valueOf(postalCodeTxt.getText()));
                paramV.put("phoneNo", String.valueOf(phoneNoTxt.getText()));
                paramV.put("houseType", houseTypeVal);
                paramV.put("householdSize", String.valueOf(householdSizeVal));
                return paramV;
            }
        };
        queue.add(stringRequest);
    }

    public boolean verifyReEnterPassword(String pwd, String repwd){
        boolean bool;

        if (!(pwd.equals(repwd))){
            Toast.makeText(getApplicationContext(),"please enter the same password", Toast.LENGTH_SHORT).show();
            bool = false;
        }
        else{
            bool = true;
        }
        return bool;
    }

    //Popup window method
    public void showPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.verification_popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window token
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener(){
            @Override
            public void onDismiss(){
                openVerificationPage();
            }
        });
    }

    public void openLoginPage(){
        Intent intent = new Intent(this, loginActivity.class);
        startActivity(intent);
    }

    public void openVerificationPage(){
        Intent intent = new Intent(this, verificationActivity.class);
        startActivity(intent);
    }
}