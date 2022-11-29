package com.example.colorpicker.controller;

import com.example.colorpicker.model.Color;
import com.example.colorpicker.model.Rgb;

public class ColorController {

    public Rgb hexToRgb(String color) {
        return new Rgb(
                Integer.valueOf( color.substring( 1, 3 ), 16 ),
                Integer.valueOf( color.substring( 3, 5 ), 16 ),
                Integer.valueOf( color.substring( 5, 7 ), 16 )
        );
    }

    public String RgbToHex(Rgb rgb) {
        return String.format("#%02x%02x%02x", rgb.getRed(), rgb.getGreen(), rgb.getBlue());
    }

}
