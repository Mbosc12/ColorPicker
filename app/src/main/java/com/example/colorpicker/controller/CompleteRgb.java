package com.example.colorpicker.controller;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.colorpicker.MainActivity;

public class CompleteRgb implements TextWatcher {

    private final String text;

    private CharSequence before;
    private MainActivity app;

    public CompleteRgb(MainActivity app, String text) {
        this.text = text; this.app = app;
    }
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        before = charSequence;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(!intVerification(editable)) {
            editable.replace(0, editable.length(), "0");
        } else if(editable.length() > 3 || (editable.length() > 0 && Integer.parseInt(editable.toString()) > 255)) {
            editable.replace(0, editable.length(), "255");
        }
        app.changeTextHexa();
    }

    public boolean intVerification(Editable editable) {
        boolean check = true;
        for(char c : editable.toString().toCharArray()) {
            if(!Character.isDigit(c)) return false;
        }
        return check;
    }
}
