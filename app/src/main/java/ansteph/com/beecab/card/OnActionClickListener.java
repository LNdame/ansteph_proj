package ansteph.com.beecab.card;

import android.view.View;

/**
 * Created by loicStephan on 06/09/16.
 */
public interface OnActionClickListener {
    /**
     * An action view is clicked.
     *
     * @param view
     *         which was clicked.
     * @param card
     *         where the view was clicked on.
     */
    void onActionClicked(View view, Card card);
}
