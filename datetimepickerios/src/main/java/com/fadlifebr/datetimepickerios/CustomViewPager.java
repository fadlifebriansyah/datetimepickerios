package com.fadlifebr.datetimepickerios;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class CustomViewPager extends ViewPager
{
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private float x1, y1, x2, y2;
    private float mTouchSlop;

    public CustomViewPager(Context context)
    {
        super(context);

        init(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        init(context);
    }

    private void init(Context context)
    {
        mTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }

    /**
     * Setting wrap_content on a ViewPager's layout_height in XML
     * doesn't seem to be recognized and the ViewPager will fill the
     * height of the screen regardless. We'll force the ViewPager to
     * have the same height as its immediate child.
     *
     * Thanks to alexrainman for the bugfix!
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
        // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            // super has to be called in the beginning so the child views can be initialized.
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) height = h;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        // super has to be called again so the new specs are treated as exact measurements
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        mDatePicker = (DatePicker) findViewById(R.id.datePicker);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
    }

    /**
     * When the user swipes their finger horizontally, dispatch
     * those touch events to the ViewPager. When they swipe
     * vertically, dispatch those touch events to the date or
     * time picker (depending on which page we're currently on).
     *
     * @param event
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();

                break;

           case MotionEvent.ACTION_MOVE:
               x2 = event.getX();
               y2 = event.getY();

               if (isScrollingHorizontal(x1, y1, x2, y2))
               {
                   // When the user is scrolling the ViewPager horizontally,
                   // block the pickers from scrolling vertically.
                   return super.dispatchTouchEvent(event);
               }

               break;
         }

         // As long as the ViewPager isn't scrolling horizontally,
         // dispatch the event to the DatePicker or TimePicker,
         // depending on which page the ViewPager is currently on.

         switch (getCurrentItem())
         {
         case 0:

             if (mDatePicker != null)
                 mDatePicker.dispatchTouchEvent(event);

             break;

         case 1:

             if (mTimePicker != null)
                 mTimePicker.dispatchTouchEvent(event);

             break;
         }

         // need this for the ViewPager to scroll horizontally at all
         return super.onTouchEvent(event);
    }

    /**
     * Determine whether the distance between the user's ACTION_DOWN
     * event (x1, y1) and the current ACTION_MOVE event (x2, y2) should
     * be interpreted as a horizontal swipe.
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private boolean isScrollingHorizontal(float x1, float y1, float x2, float y2)
    {
        float deltaX = x2 - x1;
        float deltaY = y2 - y1;

        if (Math.abs(deltaX) > mTouchSlop &&
            Math.abs(deltaX) > Math.abs(deltaY))
        {

            return true;
        }

        return false;
    }
}
