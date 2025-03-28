package com.example;

import com.example.TrafficLight.LIGHT;



public class TrafficLight {
    public enum LIGHT{
        RED("red"),
        YELLOW("yellow"),
        GREEN("green");

        private final String description;

        private LIGHT(String description) {
            this.description = description;
        }

        public String getDescriotion(){
            return name();
        }

    };
    
    private LIGHT lightStatus = LIGHT.RED; 
    private LIGHT prevLightStatus = LIGHT.YELLOW;

    public void setLightStatus(LIGHT lightStatus_) {this.prevLightStatus = lightStatus; this.lightStatus = lightStatus_;}

    public LIGHT getLightStatus() {return lightStatus;}
    public LIGHT getPrevLightStatus() {return prevLightStatus;}
    
}
