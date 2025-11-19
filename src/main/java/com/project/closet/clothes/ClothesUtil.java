package com.project.closet.clothes;


import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ClothesUtil {


    public String typeofcategory(Long categorycode){

        String result = "";

        if (categorycode == 1){
            result = "HAT";
        }else if (categorycode == 2){
            result = "OUTER";
        }else if (categorycode == 3){
            result = "TOP";
        }else if (categorycode == 4){
            result = "SHIRT";
        }else if (categorycode == 5){
            result = "BOTTOM";
        }else if (categorycode == 6){
            result = "SHOES";
        }else if (categorycode == 7){
            result = "ACC";
        }
        return result;
    }
}
