package com.example.colorpicker;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextWatcher;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener {

    private int[] listColor = new int[10];

    private EditText hexaCode, codeR, codeG, codeB;
    private Button colorButton, generationColor;
    private View colorView;

    private TextWatcher twHexa, twR, twG, twB;
    private ColorPickerView colorPickerView;

    private ColorController colorCtrl;

    private Rgb currentRgb = new Rgb(255, 255, 255);
    private String currentHexa = "#ffffff";
    private int currentColor = -1;

    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    String[][] tabColor = {{"F2C57C", "DDAE7E", "7FB685", "426A5A", "EF6F6C"},
    {"0A369D", "4472CA", "5E7CE2", "92B4F4", "CFDEE7"},
    {"ECBA82", "81C14B", "2E933C", "297045", "204E4A"},
    {"5D2A42", "FB6376", "FCB1A6", "FFDCCC", "FFF9EC"},
    {"25CED1", "FFFFFF", "FCEADE", "FF8A5B", "EA526F"}};
;

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

        generationColor = findViewById(R.id.generationColor);

        colorView = findViewById(R.id.circleColor);

        for(int i = 0; i < listColor.length; i++) {
            listColor[i] = -1;
        }
        readData();

        colorPickerView.setColorListener((ColorEnvelopeListener) (envelope, fromUser) -> {
            String hexa = colorCtrl.RgbToHex(new Rgb(envelope.getArgb()[1],envelope.getArgb()[2], envelope.getArgb()[3]));
            if(!hexa.equals(currentHexa)) {
                changeTextHexa();
            }
            if(envelope.getArgb()[1] != this.currentRgb.getRed() || envelope.getArgb()[2] != this.currentRgb.getGreen() || envelope.getArgb()[3] != this.currentRgb.getBlue()) {
                changeTextRGB(hexa, false);
            }
        });

        // set up the RecyclerView
        recyclerView = findViewById(R.id.rvColor);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        adapter = new RecyclerViewAdapter(this, listColor);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        colorButton.setOnClickListener(view -> {
            Color c = new Color(hexaCode.getText().toString(), new Rgb(Integer.parseInt(codeR.getText().toString()), Integer.parseInt(codeG.getText().toString()), Integer.parseInt(codeB.getText().toString())));
            if(checkColor(c)) {
                for(int i =0; i < listColor.length; i++) {
                    if(listColor[i] == -1) {
                        listColor[i] = android.graphics.Color.parseColor(c.getHexaCode());
                        refresh();
                        saveData();
                        return;
                    }
                }
            }
        });

        generationColor.setOnClickListener(view -> {
            getColor();
        });

    }

    public void changeTextRGB(String hexa, boolean needChangePickColor) {
        if(colorCtrl.hexToRgb(hexa).equals(currentRgb)) {
            return;
        }
        currentRgb = colorCtrl.hexToRgb(hexa);

        codeR.setText(String.valueOf(currentRgb.getRed()), TextView.BufferType.EDITABLE);
        codeG.setText(String.valueOf(currentRgb.getGreen()), TextView.BufferType.EDITABLE);
        codeB.setText(String.valueOf(currentRgb.getBlue()), TextView.BufferType.EDITABLE);

        if(needChangePickColor) {
            currentColor = getIntFromColor(Integer.valueOf(currentRgb.getRed()), Integer.valueOf(currentRgb.getGreen()), Integer.valueOf(currentRgb.getBlue()));
            changePickColor();
        }
        saveData();
    }

    public void changeTextHexa() {
        int r = this.currentRgb.getRed();
        int g = this.currentRgb.getGreen();
        int b = this.currentRgb.getBlue();
        if(colorCtrl.RgbToHex(new Rgb(r, g, b)).equals(currentHexa)) {
            return;
        }
        System.out.println("je passe après le return pour changer hexa");
        currentHexa = colorCtrl.RgbToHex(new Rgb(r, g, b));
        hexaCode.setText(currentHexa);

        currentColor = getIntFromColor(Integer.valueOf(currentRgb.getRed()), Integer.valueOf(currentRgb.getGreen()), Integer.valueOf(currentRgb.getBlue()));
        colorView.setBackgroundTintList(ColorStateList.valueOf(currentColor));
        changePickColor();
    }

    public void changePickColor() {
        colorPickerView.setInitialColor(currentColor);
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
    public void onItemClick(View v, int position) {
        if(listColor[position] != 0) {
            String hexa = "#"+ Integer.toHexString(listColor[position]).substring(2);
            hexaCode.setText(hexa);
            currentColor = listColor[position];
            changePickColor();
        }
    }

    public void getColor()  {
        Random r = new Random();
        int random = r.nextInt(tabColor.length);

        for(int i = 0; i < 5; i++) {
            Rgb rgb = colorCtrl.hexToRgb("#" + tabColor[random][i]);
            listColor[i] = getIntFromColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
        }
        refresh();
        saveData();

    }
    
    public void saveData(){
        File dir = new File(this.getFilesDir(), "save");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, "data");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(currentHexa);
            writer.append(";");
            for(int i : listColor) {
                String hexColor = String.format("#%06X", (0xFFFFFF & i));
                writer.append( hexColor + ";");
            }
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void readData() {
        try {
            File dir = new File(this.getFilesDir(), "save");
            File gpxfile = new File(dir, "data");
            FileInputStream fin = new FileInputStream(gpxfile);
            int c;
            String temp="";

            while( (c = fin.read()) != -1){ temp = temp + Character.toString((char)c); }
            initData(temp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initData(String s) {
        String[] colors = s.split(";");

        for(int i = 1; i < colors.length; i++) {
            Rgb rgb = colorCtrl.hexToRgb(colors[i]);
            listColor[i-1] = getIntFromColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
        }
    }

    public void refresh() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemLongClick(View v, int position) {
        listColor[position] = -1;
        refresh();
    }
}