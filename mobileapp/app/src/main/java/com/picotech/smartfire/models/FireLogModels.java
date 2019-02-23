package com.picotech.smartfire.models;

public class FireLogModels {

    public String state, time, image_data, reading_lpg, reading_smoke,
            reading_gas, reading_temp, reading_heat;

    public FireLogModels(String time, String state, String image_data, String reading_temp,
                         String reading_gas, String reading_lpg, String reading_smoke) {

        this.state = state;
        this.image_data = image_data;
        this.time = time;
        this.reading_lpg = reading_lpg;
        this.reading_smoke = reading_smoke;
        this.reading_gas = reading_gas;
        this.reading_temp = reading_temp;
        this.reading_heat = reading_heat;

    }

}
