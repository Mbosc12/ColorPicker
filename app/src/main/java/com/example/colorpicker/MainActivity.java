package com.example.colorpicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.example.colorpicker.controller.CompleteHexacode;
import com.example.colorpicker.controller.CompleteRgb;
import com.example.colorpicker.model.Color;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Color> listColor = new ArrayList<>();
    EditText hexaCode, codeR, codeG, codeB;

    private int start, count, after;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialisation
        hexaCode = findViewById(R.id.inputHex);
        codeR = findViewById(R.id.inputR);
        codeG = findViewById(R.id.inputG);
        codeB = findViewById(R.id.inputB);

        hexaCode.addTextChangedListener(new CompleteHexacode(hexaCode.getText().toString()));
        codeR.addTextChangedListener(new CompleteRgb(codeR.getText().toString()));
        codeG.addTextChangedListener(new CompleteRgb(codeG.getText().toString()));
        codeB.addTextChangedListener(new CompleteRgb(codeB.getText().toString()));
    }

}