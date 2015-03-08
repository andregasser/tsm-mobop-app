package mse.hqevaluator;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by aga on 3/8/15.
 */
public class Helpers {
    public static void showToast(String message, Context context) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
