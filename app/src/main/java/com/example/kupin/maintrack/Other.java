package com.example.kupin.maintrack;

import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.Manifest;
import android.location.Geocoder;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class Other {
    String token = "";

    public void  GeneratedToken() {

        int c = 0;
        Random rand = new Random();
         String alpha= "ABCDEFGHIJKabcdefghijklLMNOPQRSTUVWXYZmnopqrstuvwxyz";
         for(int k = 1;k<=5;k++) {
             token += alpha.charAt(rand.nextInt(alpha.length())) ;

         }

    }



}
