package com.example.colorpicker.controller;

import android.text.Editable;
import android.text.TextWatcher;

public class CompleteHexacode implements TextWatcher {

    private final String text;

    private int start, count, after;

    public CompleteHexacode(String text) {
        this.text = text;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        this.start = start;
        this.count = count;
        this.after = after;
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(!editable.toString().startsWith("#")) {
            editable.append('#');
        }
    }
}
