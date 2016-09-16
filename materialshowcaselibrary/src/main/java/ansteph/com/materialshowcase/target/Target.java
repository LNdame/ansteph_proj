package ansteph.com.materialshowcase.target;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by loicStephan on 16/09/16.
 */
public interface Target {
    Target NONE = new Target() {
        @Override
        public Point getPoint() {
            return new Point(1000000, 1000000);
        }

        @Override
        public Rect getBounds() {
            Point p = getPoint();
            return new Rect(p.x - 190, p.y - 190, p.x + 190, p.y + 190);
        }
    };

    Point getPoint();

    Rect getBounds();
}
