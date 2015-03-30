package mse.hqevaluator;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by aga on 3/8/15.
 */
public class Helpers {
    public static void showToast(String message, Context context) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }

    public static String inputStreamToString(InputStream inputStream) throws IOException {
        InputStream in = new BufferedInputStream(inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder response = new StringBuilder();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } catch (IOException e) {
            throw e;
        }
    }

    public static int inputStreamToInt(InputStream inputStream) throws IOException {
        InputStream in = new BufferedInputStream(inputStream);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        try {
            String strValue = reader.readLine();
            int intValue = Integer.parseInt(strValue);
            return intValue;
        } catch (IOException e) {
            throw e;
        }
    }
}
