package wells.tools.flashcard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Joshua on 2/6/2015 with the help of gideon's answer here
 * http://stackoverflow.com/questions/10992411/how-to-add-pagelines-to-a-edittext-in-android
 */

public class LinedTextView extends TextView
{
    private Rect mRect;
    private Paint mPaint;

    public LinedTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getResources().getColor(R.color.blue_card_line));
    }

    /**
     * This is called to draw the LinedEditText object
     * @param canvas The canvas on which the background is drawn.
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        int height = canvas.getHeight();
        int curHeight = 0;
        Rect r = mRect;
        Paint paint = mPaint;
        int baseline = getLineBounds(0, r);

        for (curHeight = baseline + 1; curHeight < height;
             curHeight += getLineHeight())
        {
            canvas.drawLine(r.left, curHeight, r.right, curHeight, paint);
        }
        super.onDraw(canvas);
    }
}
