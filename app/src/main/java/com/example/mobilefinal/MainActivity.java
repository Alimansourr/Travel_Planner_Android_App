package com.example.mobilefinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.mobilefinal.DatabaseHandler;

public class MainActivity extends AppCompatActivity {
    public static final String MY_PREFS_NAME="MyPrefsFile";

    private boolean isLoginShown = false;
    private DatabaseHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new DatabaseHandler(this);
        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        String test = sharedPreferences.getString("username", "");
        if (test != "") {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
//        loadSignUpFragment();
        loadLoginFragment();
    }

    public void onAlreadyRegisteredClick(View view) {
        loadLoginFragment();
    }

    public void onNewUserClick(View view) {
        loadSignUpFragment();
    }



    public void onLoginbuttonClick(String username, String password) {
        boolean success = dbHandler.loginUser(username, password);
        if (success) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            showSuccessDialog();

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("username",username);
            editor.commit();
        } else {
            showFailureDialog();
        }
    }

    public void onSignupButtonClick(String username, String password) {
        boolean success = dbHandler.registerUser(username, password);
        if (success) {
            loadLoginFragment();
            showSuccessDialog();
        } else {
            showFailureDialog();
        }
    }
    private void loadSignUpFragment() {
        Fragment signUpFragment = new SignUpFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, signUpFragment);
        transaction.commit();
    }

    private void loadLoginFragment() {
        Fragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, loginFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        isLoginShown = true;
    }

    @Override
    public void onBackPressed() {
        if (isLoginShown) {
            loadSignUpFragment();
            isLoginShown = false;
        } else {
            super.onBackPressed();
        }
    }
    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Registration successful")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void showFailureDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Registration failed")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

}
