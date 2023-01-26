package com.example.fyphomeowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ticketsActivity extends AppCompatActivity implements ticketRecyclerAdapter.TicketClickListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imageMenuView;
    private ConstraintLayout constraintLayout;
    private Button raiseTicketBtn;
    private SharedPreferences sharedPreferencesHomeowner;
    private SharedPreferences sharedPreferencesTicket;
    private RecyclerView recyclerView;
    private ArrayList<Ticket> ticketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        sharedPreferencesHomeowner = getSharedPreferences("homeownerPref", MODE_PRIVATE);
        sharedPreferencesTicket = getSharedPreferences("ticketPref", MODE_PRIVATE);

        raiseTicketBtn = findViewById(R.id.raiseTicketBtn);

        //RECYCLER VIEW
        recyclerView = findViewById(R.id.ticketRecyclerView);
        ticketList = new ArrayList<>();
        setUpTickets();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        raiseTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRaiseTicketPage();
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

    public void setUpTickets(){
        //CONNECT DB
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.168/fyp/viewTicketsRequest.php";

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
                                JSONObject ticketTypesObj = jsonObject.getJSONObject("tickets");
                                //ArrayList<TextView> textViews = new ArrayList<>();

                                Iterator<String> keys = ticketTypesObj.keys();
                                while(keys.hasNext()){
                                    String key = keys.next();
                                    String ticketID = key;
                                    JSONObject ticketObj = ticketTypesObj.getJSONObject(key);
                                    String date = ticketObj.getString("date");
                                    String serviceType = ticketObj.getString("serviceType");
                                    String description = ticketObj.getString("description");
                                    String ticketStatus = ticketObj.getString("status");
                                    String ticketStr = date +"\n"+ serviceType +"\n"+ description +"\n"+ ticketStatus;
                                    Ticket ticket = new Ticket(Integer.parseInt(ticketID), date, description, ticketStatus);
                                    ticketList.add(ticket);
//                                    TextView textView = new TextView(getApplicationContext());
//                                    textView.setText(ticketStr);
//                                    textViews.add(textView);
                                }
                                ticketRecyclerAdapter adapter = new ticketRecyclerAdapter(ticketsActivity.this, ticketList, ticketsActivity.this);
                                if(adapter != null){
                                    recyclerView.setAdapter(adapter);
                                }


//                                TextView selectTicketMsg = findViewById(R.id.selectTicketMsg);
//                                constraintLayout = findViewById(R.id.constraintLayout);
//                                for(int i = 0; i < textViews.size(); i++){
//                                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                                    layoutParams.setMargins(75, 75, 75, 75);
//                                    if(i == 0){
//                                        layoutParams.startToStart = selectTicketMsg.getId();
//                                        layoutParams.endToEnd = selectTicketMsg.getId();
//                                        layoutParams.topToBottom = selectTicketMsg.getId();
//                                        System.out.println(selectTicketMsg.getId());
//                                    }else{
//                                        System.out.println(textViews.get(i-1).getId());
//                                        layoutParams.startToStart = textViews.get(i-1).getId();
//                                        layoutParams.endToEnd = textViews.get(i-1).getId();
//                                        layoutParams.topToBottom = textViews.get(i-1).getId();
//                                    }
//                                    textViews.get(i).setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.review_style));
//                                    textViews.get(i).setLayoutParams(layoutParams);
//                                    constraintLayout.addView(textViews.get(i));
//                                    textViews.get(i).setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            openEditTicketPage();
//                                        }
//                                    });
//                                }
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

    public void openRaiseTicketPage(){
        Intent intent = new Intent(this, raiseTicketActivity.class);
        startActivity(intent);
    }
    public void openEditTicketPage(){
        Intent intent = new Intent(this, editTicketActivity.class);
        startActivity(intent);
    }

    //RECYCLER ONCLICK
    @Override
    public void selectedTicket(Ticket ticket) {
        SharedPreferences.Editor editor = sharedPreferencesTicket.edit();
        editor.putString("ticketID", String.valueOf(ticket.getID()));
        editor.apply();
        openEditTicketPage();
    }
}