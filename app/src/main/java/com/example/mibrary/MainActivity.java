package com.example.mibrary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        checkConnections();
    }

    private void checkConnections() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (NetworkUtils.isInternetAvailable(this)) {
                checkFirebaseConnection();
            } else {
                showError("İnternet bağlantısı yok!");
            }
        }, 1000);
    }

    private void checkFirebaseConnection() {
        FirebaseDatabase.getInstance().getReference(".info/connected")
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                        Boolean connected = snapshot.getValue(Boolean.class);
                        if (connected != null && connected) {
                            proceedToHome();
                        } else {
                            showError("Firebase bağlantı hatası! Tekrar Deneyin.");
                        }
                    }

                    @Override
                    public void onCancelled(com.google.firebase.database.DatabaseError error) {
                        showError("Bağlantı hatası: " + error.getMessage());
                    }
                });
    }

    private void proceedToHome() {
        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(this, BookListActivity.class));
        finish();
    }

    private void showError(String message) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
