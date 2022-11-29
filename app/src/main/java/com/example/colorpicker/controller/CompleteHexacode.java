package com.example.colorpicker.controller;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.colorpicker.MainActivity;

public class CompleteHexacode implements TextWatcher {

    private final String text;
    private CharSequence before;

    MainActivity app;

    public CompleteHexacode(MainActivity app, String text) {
        this.app = app;
        this.text = text;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        this.before = charSequence;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(!editable.toString().startsWith("#")) {
            editable.append('#');
        } else if(editable.length() > 7) {
            editable.delete(editable.length()-1, editable.length());
        }

        if(editable.length() == 7) {
            app.changeTextRGB(editable.toString());
        }
    }
}
