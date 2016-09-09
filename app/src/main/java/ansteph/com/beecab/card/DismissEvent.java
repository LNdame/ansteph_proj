package ansteph.com.beecab.card;

import android.support.annotation.NonNull;

/**
 * Created by loicStephan on 06/09/16.
 */
public class DismissEvent {

    private final Card mCard;

    public DismissEvent(@NonNull final Card card) {
        mCard = card;
    }

    public Card getCard() {
        return mCard;
    }
}
