package Model;

public class Miscellaneous {
    public static int getScaleRange(double input){
        if(input <= 500){
            return 90;
        } else if(input <= 1000){
            return 240;
        } else if(input <= 1500){
            return 390;
        } else if (input <= 2000){
            return 600;
        } else if (input <= 2500){
            return 750;
        } else if (input <= 3000){
            return 960;
        } else if (input <= 3500){
            return 1200;
        } else{
            return 1500;
        }
    }
}
