package com.ragab.ahmed.educational.happenings.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ragab.ahmed.educational.happenings.R;
public class LoginActivity extends AppCompatActivity implements
        OnFragmentInteractionListener {

    public static String USER_ARG = "user";

    SignInFragment signInFragment;
    SignUpFragment signUpFragment;

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
    }


    @Override
    public void onFragmentInteraction(int REQUEST_CODE) {
        if (REQUEST_CODE == OnFragmentInteractionListener.LAUNCH_SIGN_IN) {
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
        else if (REQUEST_CODE == OnFragmentInteractionListener.FINISH_SIGN_IN)
        {
            Intent intent = new Intent();
            intent.putExtra(USER_ARG, signInFragment.mUser);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
