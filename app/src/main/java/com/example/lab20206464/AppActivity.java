package com.example.lab20206464;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lab20206464.fragments.ForecastFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppActivity extends AppCompatActivity {

    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_locations) {
                clearBackStackAndNavigate(R.id.locationsFragment);
                return true;
            } else if (itemId == R.id.nav_forecast) {
                clearBackStackAndNavigate(R.id.forecastFragment);
                return true;
            } else if (itemId == R.id.nav_sports) {
                clearBackStackAndNavigate(R.id.sportsFragment);
                return true;
            }
            return false;
        });
    }

    private void clearBackStackAndNavigate(int destinationId) {
        navController.popBackStack(navController.getGraph().getStartDestination(), false);
        navController.navigate(destinationId);
    }
    
    public void navigateToForecastTab() {
        try {
            bottomNavigationView.setSelectedItemId(R.id.nav_forecast);
        } catch (Exception e) {
            clearBackStackAndNavigate(R.id.forecastFragment);
        }
    }
    
    public void navigateToForecastWithLocationData(Bundle bundle) {
        try {
            bottomNavigationView.setSelectedItemId(R.id.nav_forecast);
            
            new android.os.Handler().postDelayed(() -> {
                try {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    if (currentFragment instanceof NavHostFragment) {
                        NavHostFragment navHostFragment = (NavHostFragment) currentFragment;
                        Fragment forecastFragment = navHostFragment.getChildFragmentManager().getPrimaryNavigationFragment();
                        if (forecastFragment instanceof ForecastFragment) {
                            ((ForecastFragment) forecastFragment).setArgumentsAndUpdate(bundle);
                        }
                    }
                } catch (Exception e) {
                    // Silenciar errores de timing
                }
            }, 200);
            
        } catch (Exception e) {
            bottomNavigationView.setSelectedItemId(R.id.nav_forecast);
        }
    }

    @Override
    public void onBackPressed() {
        if (!navController.popBackStack()) {
            super.onBackPressed();
        }
    }
}