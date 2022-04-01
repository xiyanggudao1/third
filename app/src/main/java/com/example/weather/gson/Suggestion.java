package com.example.weather.gson;

import com.google.gson.annotations.SerializedName;

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    public class Comfort {
        @SerializedName("text")
        public String info;
    }
    @SerializedName("cw")
    public CarWash carWash;

    public class CarWash {
        @SerializedName("text")
        public String info;
    }

    public Sport sport;

    public class Sport {
        @SerializedName("text")
        public String info;
    }
}
