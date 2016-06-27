package com.ragab.ahmed.educational.happenings.data.models;

import android.text.TextUtils;
import android.util.Patterns;

import java.io.Serializable;

/**
 * Created by Ragabov on 3/24/2016.
 */
public class User implements Serializable{

    public long id;
    public String fname;
    public String lname;
    public String email;
    public String password;
    public long point;
    public String profilepic;
    public String rankId;


    public User(String firstName, String lastName, String email, String password)
    {
        this.fname = firstName;
        this.lname = lastName;
        this.email = email;
        this.password = password;
    }

    public static boolean isValidName(String name)
    {
        return !TextUtils.isEmpty(name);
    }

    public static boolean isValidEmail(String email)
    {
        if (TextUtils.isEmpty(email))
            return false;

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password)
    {
        if (TextUtils.isEmpty(password))
            return false;

        if (password.length() < 6)
            return false;

        return true;
    }
}
