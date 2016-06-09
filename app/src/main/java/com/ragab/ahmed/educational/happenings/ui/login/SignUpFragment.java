package com.ragab.ahmed.educational.happenings.ui.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
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


public class SignUpFragment extends android.support.v4.app.Fragment {

    private OnFragmentInteractionListener mListener;

    private ProgressDialog mDialog;
    private EditText firstNameText;
    private EditText lastNameText;
    private EditText emailText;
    private EditText passwordText;

    IseeApi mApi;

    public SignUpFragment() {
        // Required empty public constructor
        mApi = ApiHelper.buildApi();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        mDialog = new ProgressDialog(getActivity());
        mDialog.setIndeterminate(true);
        mDialog.setMessage(getString(R.string.sign_up_progress));
        mDialog.setCancelable(false);

        firstNameText = (EditText)view.findViewById(R.id.input_first_name);
        lastNameText = (EditText)view.findViewById(R.id.input_last_name);
        emailText = (EditText)view.findViewById(R.id.input_email);
        passwordText = (EditText)view.findViewById(R.id.input_password);

        initValidators();

        TextView LaunchSignIn = (TextView)view.findViewById(R.id.link_login);

        LaunchSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(OnFragmentInteractionListener.LAUNCH_SIGN_IN);
            }
        });

        AppCompatButton signUpBtn = (AppCompatButton)view.findViewById(R.id.btn_signup);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        return view;
    }

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
    }

    public void initValidators ()
    {
        String namePrompt = getString(R.string.name_validation);
        String emailPrompt = getString(R.string.email_validation);
        String passwordPrompt = getString(R.string.password_validation);

        firstNameText.addTextChangedListener(new MyTextWatcher(firstNameText, new Validator<Boolean, String>(namePrompt) {
            @Override
            public Boolean execute(String s) {
                return User.isValidName(s);
            }
        }));

        lastNameText.addTextChangedListener(new MyTextWatcher(lastNameText, new Validator<Boolean, String>(namePrompt) {
            @Override
            public Boolean execute(String s) {
                return User.isValidName(s);
            }
        }));

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

    public void signUp()
    {

        emailText.setText(emailText.getText().toString().trim());
        passwordText.setText(passwordText.getText().toString().trim());
        firstNameText.setText(firstNameText.getText().toString().trim());
        lastNameText.setText(lastNameText.getText().toString().trim());
        if (emailText.getError() != null || passwordText.getError() != null ||
                firstNameText.getError() != null || lastNameText.getError() != null)
        {
            return;
        }

        mDialog.show();
        final String successPrompt = getString(R.string.sign_up_success);
        final String invalidPrompt = getString(R.string.sign_up_invalid);
        final String failedPrompt = getString(R.string.sign_up_failed);

        mApi.signUp(firstNameText.getText().toString(),
                lastNameText.getText().toString(),
                emailText.getText().toString(),
                passwordText.getText().toString()
        ).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == HttpURLConnection.HTTP_CREATED)
                {
                    Toast.makeText(getActivity(), successPrompt, Toast.LENGTH_SHORT).show();
                    mListener.onFragmentInteraction(OnFragmentInteractionListener.FINISH_SIGN_UP);
                }
                else
                {
                    Toast.makeText(getActivity(), invalidPrompt, Toast.LENGTH_SHORT).show();
                }
                mDialog.hide();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mDialog.hide();
                Toast.makeText(getActivity(), failedPrompt, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
