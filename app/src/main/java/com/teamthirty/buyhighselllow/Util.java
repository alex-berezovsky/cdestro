package com.teamthirty.buyhighselllow;

import android.content.Context;
import android.widget.Toast;

public final class Util {

    /**
     * This method sanitizes the name input for the start menu when you enter your name.
     * It checks for null, empty, and whitespace only inputs
     *
     * @param input   the string you are checking
     * @param context the context of the application
     *
     * @return true if name follows proper formatting, false otherwise
     */
    public static boolean sanitizeNameInput(String input, Context context) {

        if (input == null) { //null input check

            String errorMessage = "Invalid Name: You cannot input null values for your name";
            int popUpDuration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, errorMessage, popUpDuration);
            toast.show();
            return false;

        } else if (input.equals("")
            || input.trim().isEmpty()) { //empty and whitespace only input check

            String errorMessage =
                "Invalid Name: Your name cannot be empty or consist of only spaces";
            int popUpDuration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, errorMessage, popUpDuration);
            toast.show();
            return false;

        }

        return true;
    }
}