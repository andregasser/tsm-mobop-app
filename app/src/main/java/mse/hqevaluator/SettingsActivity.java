

package mse.hqevaluator;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;

import com.google.android.gms.maps.model.Circle;


public class SettingsActivity extends ActionBarActivity {

    private SeekBar seekBar_1;
    private SeekBar seekBar_2;
    private TextView textView_1;
    private TextView textView_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        seekBar_1 = (SeekBar) findViewById(R.id.seekBar1);
        seekBar_2 = (SeekBar) findViewById(R.id.seekBar2);
        textView_1 = (TextView) findViewById(R.id.tV_seekBar_1);
        textView_2 = (TextView) findViewById(R.id.tV_seekBar_2);




        seekBar_1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
                textView_1.setTextColor(Color.GREEN);
                textView_1.setText("");
                textView_1.append(String.valueOf(progresValue));
                progress = (progress/5)*5;
                seekBar_1.setProgress(progress);
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //textView_1.setText("Covered: " + progress + "/" + seekBar_1.getMax());
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
                textView_1.setTextColor(Color.BLUE);
            }
        });


        seekBar_2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
                textView_2.setTextColor(Color.GREEN);
                textView_2.setText("");
                textView_2.append(String.valueOf(progresValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Toast.makeText(getApplicationContext(), "Started tracking seekbar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //textView_1.setText("Covered: " + progress + "/" + seekBar_1.getMax());
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar", Toast.LENGTH_SHORT).show();
                textView_2.setTextColor(Color.BLUE);
            }

        });
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

class CustomSeekBar extends SeekBar {

    public CustomSeekBar(Context context) {
        super(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        Paint paintCircle = new Paint();
        paintCircle.setColor(Color.rgb(142, 196, 0));
        canvas.drawCircle(2,2,4, paintCircle);
    }
}