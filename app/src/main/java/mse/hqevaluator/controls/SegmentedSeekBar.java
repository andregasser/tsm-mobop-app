package mse.hqevaluator.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.SeekBar;

/**
 * This class is a customized version of the default Android seekbar.
 * It is based on a configurable number of segments and allows only a
 * specific set of values.
 *
 * More information related to developing custom components:
 * https://developer.android.com/guide/topics/ui/custom-components.html
 *
 * View class:
 * https://developer.android.com/reference/android/view/View.html#onDraw%28android.graphics.Canvas%29
 *
 * Paint class:
 * https://developer.android.com/reference/android/graphics/Paint.html
 *
 */
public class SegmentedSeekBar extends SeekBar {

    public SegmentedSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);

        canvas.

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2f);

        int dividerYStart = height / 2 - 10;
        int dividerYStop = height / 2 + 10;

        // Paint vertical line at the left edge
        canvas.drawLine(24, dividerYStart, 24, dividerYStop, paint);

        // Paint vertical line at the right edge
        canvas.drawLine(width - 24, dividerYStart, width - 24, dividerYStop, paint);

        // Paint vertical line in the center
        canvas.drawLine(width / 2, dividerYStart, width / 2, dividerYStop, paint);

        // Set texts
        String textNearby = "Nearby";
        String textDoesntMatter = "Doesn't matter";
        String textFarAway = "Far away";

        int textY = height;
        Paint paintText = new Paint();
        paintText.setTextSize(24);
        paintText.setColor(Color.BLACK);

        //float widthTextNearby = paintText.measureText(textNearby);
        canvas.drawText(textNearby, 24, textY, paintText);

        float widthTextDoesntMatter = paintText.measureText(textDoesntMatter);
        canvas.drawText(textDoesntMatter, width / 2 - widthTextDoesntMatter / 2, textY, paintText);

        float widthTextFarAway = paintText.measureText(textFarAway);
        canvas.drawText(textFarAway, width - widthTextFarAway - 24, textY, paintText);
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
