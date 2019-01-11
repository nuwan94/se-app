package me.nuwan.seofficial.Widgets;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class MovableFAB extends FloatingActionButton implements View.OnTouchListener {

    private static float CLICK_DRAG_TOLERANCE = 10;
    private int margin = 10;

    private float downRawX, downRawY;
    private float dX, dY;
    float newX = 0, newY = 0;

    public MovableFAB(Context context) {
        super(context);
        init();
    }

    public MovableFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MovableFAB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();

        View viewParent = (View) view.getParent();
        int parentHeight = viewParent.getHeight();
        int parentWidth = viewParent.getWidth();

        int action = motionEvent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {

            downRawX = motionEvent.getRawX();
            downRawY = motionEvent.getRawY();
            dX = view.getX() - downRawX;
            dY = view.getY() - downRawY;

            return true;

        } else if (action == MotionEvent.ACTION_MOVE) {


            newX = motionEvent.getRawX() + dX;
            newX = Math.max(margin, newX);
            newX = Math.min(parentWidth - viewWidth - margin, newX);

            newY = motionEvent.getRawY() + dY;
            newY = Math.max(margin, newY);
            newY = Math.min(parentHeight - viewHeight - margin, newY);

            view.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start();

            return true;

        } else if (action == MotionEvent.ACTION_UP) {

            float upRawX = motionEvent.getRawX();
            float upRawY = motionEvent.getRawY();

            float upDX = upRawX - downRawX;
            float upDY = upRawY - downRawY;

            if (newX * 2 > parentWidth) {
                newX = parentWidth - viewHeight - margin;
            } else {
                newX = 5;
            }

            view.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(50)
                    .start();

            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) {
                return performClick();
            } else {
                return true;
            }


        } else {
            return super.onTouchEvent(motionEvent);
        }

    }

    public void setMargin(int margin) {
        this.margin = margin;
    }
}
