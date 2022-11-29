package com.example.colorpicker.model;

public class Color {

    private String hexaCode;
    private Rgb rgb;

    public Color(String hexaCode, Rgb rgb) {
        this.rgb = rgb;
        this.hexaCode = hexaCode;
    }

    public String getHexaCode() {
        return hexaCode;
    }

    public void setHexaCode(String hexaCode) {
        this.hexaCode = hexaCode;
    }

    public Rgb getRgb() {
        return rgb;
    }

    public void setRgb(Rgb rgb) {
        this.rgb = rgb;
    }
}
