package com.ragab.ahmed.educational.happenings.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ragab.ahmed.educational.happenings.R;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class LoginActivity extends AppCompatActivity implements
        OnFragmentInteractionListener {

    public static String USER_ARG = "user";

    SignInFragment signInFragment;
    SignUpFragment signUpFragment;

    public static final String LOGIN_FILE = "login_file";
    public static final String USER_NAME_EMAIL = "useremail";
    public static final String USER_PASSWORD_KEY = "userpassword";
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            signInFragment = new SignInFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, signInFragment)
                    .commit();
        }

        sharedPreferences = getSharedPreferences(LOGIN_FILE, MODE_PRIVATE);
    }


    @Override
    public void onFragmentInteraction(int REQUEST_CODE) {
        if (REQUEST_CODE == OnFragmentInteractionListener.LAUNCH_SIGN_IN ) {
            if (signInFragment == null)
                signInFragment = new SignInFragment();

            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.container, signInFragment)
                    .commit();
        }
        else if (REQUEST_CODE == OnFragmentInteractionListener.LAUNCH_SIGN_UP)
        {
            if (signUpFragment == null)
                signUpFragment = new SignUpFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                    .replace(R.id.container, signUpFragment)
                    .commit();
        }
        else if (REQUEST_CODE == OnFragmentInteractionListener.FINISH_SIGN_IN) {
            Intent intent = new Intent();
            intent.putExtra(USER_ARG, signInFragment.mUser);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USER_NAME_EMAIL, signInFragment.emailText.getText().toString());
            editor.putString(USER_PASSWORD_KEY, signInFragment.passwordText.getText().toString());
            editor.commit();
            setResult(RESULT_OK, intent);
            finish();
        }
        else if (REQUEST_CODE == OnFragmentInteractionListener.FINISH_SIGN_UP)
        {
            if (signInFragment == null)
                signInFragment = new SignInFragment();

            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
                    .replace(R.id.container, signInFragment)
                    .commit();

        }
    }
}
