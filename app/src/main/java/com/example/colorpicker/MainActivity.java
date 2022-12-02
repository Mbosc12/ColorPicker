package com.example.colorpicker;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.colorpicker.adaptateur.RecyclerViewAdapter;
import com.example.colorpicker.controller.ColorController;
import com.example.colorpicker.controller.CompleteHexacode;
import com.example.colorpicker.controller.CompleteRgb;
import com.example.colorpicker.model.Color;
import com.example.colorpicker.model.Rgb;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.ItemClickListener {

    private int[] listColor = new int[10];

    private EditText hexaCode, codeR, codeG, codeB;
    private Button colorButton;

    private TextWatcher twHexa, twR, twG, twB;
    private ColorPickerView colorPickerView;

    private ColorController colorCtrl;

    private Rgb currentRgb = null;
    private String currentHexa = "";
    private int currentColor = 0;

    RecyclerViewAdapter adapter;

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

        colorPickerView = findViewById(R.id.colorPickerView);
        colorButton = findViewById(R.id.addColorButton);

        colorPickerView.setColorListener((ColorEnvelopeListener) (envelope, fromUser) -> {
            if(getIntFromColor(envelope.getArgb()[1], envelope.getArgb()[2], envelope.getArgb()[3]) != currentColor) {
                codeR.setText(String.valueOf(envelope.getArgb()[1]), TextView.BufferType.EDITABLE);
                codeG.setText(String.valueOf(envelope.getArgb()[1]), TextView.BufferType.EDITABLE);
                codeB.setText(String.valueOf(envelope.getArgb()[1]), TextView.BufferType.EDITABLE);
                String hexa = colorCtrl.RgbToHex(new Rgb(envelope.getArgb()[1],envelope.getArgb()[2], envelope.getArgb()[3]));
                hexaCode.setText(hexa);
                currentColor = envelope.getColor();

                changeTextRGB(hexa);
                changeTextHexa();
            }
        });

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvColor);
        int numberOfColumns = 6;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        adapter = new RecyclerViewAdapter(this, listColor);
        adapter.setClickListener((RecyclerViewAdapter.ItemClickListener) this);
        recyclerView.setAdapter(adapter);


        colorButton.setOnClickListener(view -> {
            Color c = new Color(hexaCode.getText().toString(), new Rgb(Integer.parseInt(codeR.getText().toString()), Integer.parseInt(codeG.getText().toString()), Integer.parseInt(codeB.getText().toString())));

            System.out.println(listColor[0]);
            if(checkColor(c)) {
                for(int i =0; i < listColor.length; i++) {
                    if(listColor[i] == 0) {
                        listColor[i] = android.graphics.Color.parseColor(c.getHexaCode());
                        recyclerView.setAdapter(adapter);
                        return;
                    }
                }
            }
        });

    }

    public void changeTextRGB(String hexa) {
        if(colorCtrl.hexToRgb(hexa).equals(currentRgb)) {
            return;
        }
        currentRgb = colorCtrl.hexToRgb(hexa);

        codeR.setText(String.valueOf(currentRgb.getRed()), TextView.BufferType.EDITABLE);
        codeG.setText(String.valueOf(currentRgb.getGreen()), TextView.BufferType.EDITABLE);
        codeB.setText(String.valueOf(currentRgb.getBlue()), TextView.BufferType.EDITABLE);
    }

    public void changeTextHexa() {
        int r = Integer.parseInt(codeR.getText().toString());
        int g = Integer.parseInt(codeG.getText().toString());
        int b = Integer.parseInt(codeB.getText().toString());

        if(colorCtrl.RgbToHex(new Rgb(r, g, b)).equals(currentHexa)) {
            return;
        }
        currentHexa = colorCtrl.RgbToHex(new Rgb(r, g, b));
        hexaCode.setText(currentHexa);
    }

    public boolean checkColor(Color color) {
        for(int c : this.listColor) {
            if(c == android.graphics.Color.parseColor(color.getHexaCode())) {
                return false;
            }
        }
        return true;
    }

    public int getIntFromColor(int r, int g, int b) {
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;

        return 0xFF000000 | r | g | b;
    }

    @Override
    public void onItemClick(View view, int position) {
        String hexa = "#"+ Integer.toHexString(listColor[position]).substring(2);
        hexaCode.setText(hexa);
        currentColor = listColor[position];

        changeTextRGB(hexa);
        changeTextHexa();
    }
}