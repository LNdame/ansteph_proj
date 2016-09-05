package ansteph.com.beecab.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by loicStephan on 02/07/16.
 */
public class FixedViewPager extends ViewPager {

    private boolean enabled;
    public FixedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled= true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.enabled)
            return super.onTouchEvent(ev);

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(this.enabled)
        return super.onInterceptTouchEvent(ev);

        return false;
    }

    public void setPagingEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
