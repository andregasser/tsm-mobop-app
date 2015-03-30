package mse.hqevaluator;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;


public class SettingsActivity extends ActionBarActivity {

    private CustomSeekBar seekBar_1;
    private CustomSeekBar seekBar_2;

    private Spinner spinner_1;
    private Spinner spinner_2;

    private TextView textView_1;
    private TextView textView_5;

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

        seekBar_1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
                progress = progresValue;
                textView_1.setTextColor(Color.GRAY);
                textView_1.setText("");
                textView_1.append(String.valueOf(progresValue)+"km");
                progress = (progress / 5) * 5;
                seekBar_1.setProgress(progress);
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
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
                textView_5.setTextColor(Color.GRAY);
                textView_5.setText("");
                textView_5.append(String.valueOf(progresValue)+"km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView_5.setTextColor(Color.BLACK);
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