package mse.hqevaluator;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;


public class SettingsActivity extends ActionBarActivity {

    private CustomSeekBar seekBar_1;
    private CustomSeekBar seekBar_2;

    private Spinner spinner_1;
    private Spinner spinner_2;
    private Spinner spinner_3;

    private TextView textView_1;
    private TextView textView_5;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CustomSeekBar seekbar = new CustomSeekBar(this);

        textView_1 = (TextView) findViewById(R.id.tV_seekBar1_3);
        textView_5 = (TextView) findViewById(R.id.tV_seekBar2_3);

        seekBar_1 = (CustomSeekBar) findViewById(R.id.seekBar1);
        seekBar_2 = (CustomSeekBar) findViewById(R.id.seekBar2);

        seekBar_1.setMax(100);
        seekBar_2.setMax(30);

        spinner_1 = (Spinner) findViewById(R.id.spinner_1);
        spinner_2 = (Spinner) findViewById(R.id.spinner_2);
        spinner_3 = (Spinner) findViewById(R.id.spinner_3);


        String seekbar_prefs = null;

        editor = getSharedPreferences(seekbar_prefs, MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences(seekbar_prefs, MODE_PRIVATE);

        if(!prefs.contains("nuclear_power_plant")){
            editor.putInt("nuclear_power_plant",0);
            editor.commit();
        }

        if(!prefs.contains("motorway_ramp")){
            editor.putInt("motorway_ramp",0);
            editor.commit();
        }

        if(!prefs.contains("spinner_select")){
            editor.putInt("spinner_select",0);
            editor.commit();
        }

        if(!prefs.contains("spinner_nearfar1")){
            editor.putInt("spinner_nearfar1",0);
            editor.commit();
        }

        if(!prefs.contains("spinner_nearfar2")){
            editor.putInt("spinner_nearfar2",0);
            editor.commit();
        }

        int oldVal = prefs.getInt("nuclear_power_plant",0);
        seekBar_1.setProgress(oldVal);
        textView_1.append(String.valueOf(oldVal)+"km");
        oldVal = prefs.getInt("motorway_ramp",0);
        seekBar_2.setProgress(oldVal);
        textView_5.append(String.valueOf(oldVal) + "km");

        seekBar_1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                textView_1.setTextColor(Color.GRAY);
                textView_1.setText("");
                textView_1.append(String.valueOf(progresValue) + "km");
                progress = (progress / 5) * 5;
                seekBar_1.setProgress(progress);

                editor.putInt("nuclear_power_plant",progress);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_1.setTextColor(Color.BLACK);
            }
        });
        seekBar_2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                textView_5.setTextColor(Color.GRAY);
                textView_5.setText("");
                textView_5.append(String.valueOf(progresValue)+"km");

                editor.putInt("motorway_ramp",progress);
                editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_5.setTextColor(Color.BLACK);
            }
        });

        spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // An spinnerItem was selected. You can retrieve the selected item using
                String str = parent.getItemAtPosition(pos).toString();
                editor.putInt("spinner_nearfar1", pos);
                editor.commit();
                //  Toast.makeText(getApplicationContext(), "Item Selected: "+str,Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        });

        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // An spinnerItem was selected. You can retrieve the selected item using
                String str = parent.getItemAtPosition(pos).toString();

                editor.putInt("spinner_nearfar2", pos);
                editor.commit();
                //  Toast.makeText(getApplicationContext(), "Item Selected: "+str,Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        });


        spinner_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when a new item is selected (in the Spinner)
             */
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                // An spinnerItem was selected. You can retrieve the selected item using
                String str = parent.getItemAtPosition(pos).toString();
        
                editor.putString("spinner_select", str);
                editor.commit();
                //  Toast.makeText(getApplicationContext(), "Item Selected: "+str,Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}

/*
 /  Class Custom Seekbar
*/
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

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        SeekBar sb = this;
        int width = sb.getWidth();
        int height = sb.getHeight()/2;
        System.out.println("Seekbar width: "+width+"\n"+"Seekbar height: "+height);

        Paint paint = new Paint();
        paint.setColor(Color.rgb(0,0, 255));
        canvas.drawRect(22,12,24,36,paint);
        canvas.drawRect(width-26,12,width-24,36,paint);

    }
}