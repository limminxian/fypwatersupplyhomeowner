package com.example.fyphomeowner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

public class settingsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imageMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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

}