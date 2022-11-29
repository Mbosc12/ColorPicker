package com.example.colorpicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.colorpicker.controller.ColorController;
import com.example.colorpicker.controller.CompleteHexacode;
import com.example.colorpicker.controller.CompleteRgb;
import com.example.colorpicker.model.Color;
import com.example.colorpicker.model.Rgb;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Color> listColor = new ArrayList<>();
    EditText hexaCode, codeR, codeG, codeB;

    TextWatcher twHexa, twR, twG, twB;

    private ColorController colorCtrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colorCtrl = new ColorController();

        //initialisation
        hexaCode = findViewById(R.id.inputHex);
        twHexa = new CompleteHexacode(this, hexaCode.getText().toString());
        hexaCode.addTextChangedListener(twHexa);

        codeR = findViewById(R.id.inputR);
        twR = new CompleteRgb(this, codeR.getText().toString());
        codeR.addTextChangedListener(twR);

        codeG = findViewById(R.id.inputG);
        twG = new CompleteRgb(this, codeG.getText().toString());
        codeG.addTextChangedListener(twG);

        codeB = findViewById(R.id.inputB);
        twB = new CompleteRgb(this, codeB.getText().toString());
        codeB.addTextChangedListener(twB);

    }

    public void changeTextRGB(String hexa) {
        Rgb rgb = colorCtrl.hexToRgb(hexa);

        codeR.setText(String.valueOf(rgb.getRed()), TextView.BufferType.EDITABLE);
        codeG.setText(String.valueOf(rgb.getGreen()), TextView.BufferType.EDITABLE);
        codeB.setText(String.valueOf(rgb.getBlue()), TextView.BufferType.EDITABLE);
    }

    public void changeTextHexa() {
        int r = Integer.parseInt(codeR.getText().toString());
        int g = Integer.parseInt(codeG.getText().toString());
        int b = Integer.parseInt(codeB.getText().toString());
        String hexa = colorCtrl.RgbToHex(new Rgb(r, g, b));

        if(hexaCode.getText().toString().equals(hexa)) {
            return;
        }

        hexaCode.setText(hexa);
    }

}