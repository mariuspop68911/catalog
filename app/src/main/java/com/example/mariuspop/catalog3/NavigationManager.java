package com.example.mariuspop.catalog3;

import android.content.Context;
import android.content.Intent;

public class NavigationManager {

    public static void navigateToActivity(Context context, Class<?> classa){
        Intent intent = new Intent(context, classa);
        context.startActivity(intent);
    }


}
