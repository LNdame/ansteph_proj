package ansteph.com.materialshowcase.shape;

import android.graphics.Canvas;
import android.graphics.Paint;

import ansteph.com.materialshowcase.target.Target;

/**
 * Created by loicStephan on 16/09/16.
 */
public interface Shape {
    /**
     * Draw shape on the canvas with the center at (x, y) using Paint object provided.
     */
    void draw(Canvas canvas, Paint paint, int x, int y, int padding);

    /**
     * Get width of the shape.
     */
    int getWidth();

    /**
     * Get height of the shape.
     */
    int getHeight();

    /**
     * Update shape bounds if necessary
     */
    void updateTarget(Target target);
}
