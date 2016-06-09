package com.ragab.ahmed.educational.happenings.ui.helpers.textwatcher;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.ragab.ahmed.educational.happenings.R;

/**
 * Created by Ragabov on 4/2/2016.
 */
public class MyTextWatcher implements TextWatcher {

    protected static Drawable errorIcon;
    protected EditText mEditText;
    Validator<Boolean, String> mValidator;
    public MyTextWatcher(EditText editText, Validator<Boolean, String> myValidationFunc)
    {
        mEditText = editText;
        editText.addTextChangedListener(this);
        mValidator = myValidationFunc;

        if (errorIcon == null) {
            errorIcon = DrawableCompat.wrap(Resources.getSystem().getDrawable(android.R.drawable.ic_dialog_alert));
            DrawableCompat.setTint(errorIcon, editText.getContext().getResources().getColor(R.color.color_accent));
            errorIcon.setBounds(0, 0, errorIcon.getMinimumWidth() - 17, errorIcon.getMinimumHeight() - 17);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!mValidator.execute(s.toString()))
        {
            mEditText.setError(mValidator.prompt, errorIcon);
        }
        else
        {
            mEditText.setError(null);
        }
    }
}
