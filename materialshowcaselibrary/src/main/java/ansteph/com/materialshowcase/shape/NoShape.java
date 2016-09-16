package ansteph.com.materialshowcase.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import ansteph.com.materialshowcase.target.Target;

/**
 * Created by loicStephan on 16/09/16.
 */
public class NoShape implements Shape{

    @Override
    public void updateTarget(Target target) {
        // do nothing
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int x, int y, int padding) {
        // do nothing
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
