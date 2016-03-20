package com.ragab.ahmed.educational.happenings.ui.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ragab.ahmed.educational.happenings.R;
public class LoginActivity extends AppCompatActivity implements
        OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SignInFragment())
                    .commit();
        }
    }


    @Override
    public void onFragmentInteraction(int REQUEST_CODE) {
        if (REQUEST_CODE == OnFragmentInteractionListener.LAUNCH_SIGN_IN) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_bottom)
                    .replace(R.id.container, new SignInFragment())
                    .commit();
        }
        else if (REQUEST_CODE == OnFragmentInteractionListener.LAUNCH_SIGN_UP)
        {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top)
                    .replace(R.id.container, new SignUpFragment())
                    .commit();
        }
    }
}
