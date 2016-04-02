package com.ragab.ahmed.educational.happenings.ui.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ragab.ahmed.educational.happenings.R;
import com.ragab.ahmed.educational.happenings.data.models.User;
import com.ragab.ahmed.educational.happenings.network.ApiHelper;
import com.ragab.ahmed.educational.happenings.network.IseeApi;
import com.ragab.ahmed.educational.happenings.ui.helpers.textwatcher.MyTextWatcher;
import com.ragab.ahmed.educational.happenings.ui.helpers.textwatcher.Validator;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;

    EditText emailText;
    EditText passwordText;

    ProgressDialog mDialog;
    IseeApi mApi;

    public User mUser;

    public SignInFragment() {
        // Required empty public constructor
        mApi = ApiHelper.buildApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signin, container, false);

        mDialog = new ProgressDialog(getActivity());
        mDialog.setIndeterminate(true);
        mDialog.setMessage(getString(R.string.sign_in_progress));
        mDialog.setCancelable(false);

        emailText = (EditText)view.findViewById(R.id.input_email);
        passwordText = (EditText)view.findViewById(R.id.input_password);
        initValidators();

        AppCompatButton loginBtn = (AppCompatButton)view.findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        TextView launchSignUp = (TextView)view.findViewById(R.id.link_signup);
        launchSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(OnFragmentInteractionListener.LAUNCH_SIGN_UP);
            }
        });

        return view;
    }

/*    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        emailText = null;
        passwordText = null;
    }

    public void signIn()
    {

        if (emailText.getError() != null || passwordText.getError() != null) {
            emailText.setText(emailText.getText());
            passwordText.setText(passwordText.getText());
            return;
        }

        mDialog.show();

        mApi.signIn(emailText.getText().toString(), passwordText.getText().toString()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED)
                {
                    Toast.makeText(getActivity(), "Invalid email/password combination", Toast.LENGTH_SHORT).show();
                }
                else {
                    mUser = response.body();
                    mListener.onFragmentInteraction(OnFragmentInteractionListener.FINISH_SIGN_IN);
                }
                mDialog.hide();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mDialog.hide();
                Toast.makeText(getActivity(), "Failed to sign in", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void initValidators ()
    {
        String emailPrompt = getString(R.string.email_validation);
        String passwordPrompt = getString(R.string.password_validation);

        emailText.addTextChangedListener(new MyTextWatcher(emailText, new Validator<Boolean, String>(emailPrompt) {
            @Override
            public Boolean execute(String s) {
                return User.isValidEmail(s);
            }
        }));

        passwordText.addTextChangedListener(new MyTextWatcher(passwordText, new Validator<Boolean, String>(passwordPrompt) {
            @Override
            public Boolean execute(String s) {
                return User.isValidPassword(s);
            }
        }));
    }
}
