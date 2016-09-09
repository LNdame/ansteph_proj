package ansteph.com.beecab.card;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by loicStephan on 06/09/16.
 */
public class Card {

    @NonNull
    private final CardProvider mProvider;

    @Nullable
    private Object mTag;
    private boolean mDismissible;

    public Card(@NonNull final Builder builder) {
        this.mProvider = builder.mProvider;
        mTag = builder.mTag;
        mDismissible=builder.mDismissible;
    }

    /**
     * Get the card content.
     *
     * @return the card content.
     */
    @NonNull
    public CardProvider getProvider() {
        return mProvider;
    }

    /**
     * Set the tag.
     *
     * @param object as tag.
     */
    public void setTag(@Nullable final Object object) {
        mTag = object;
    }

    /**
     * Get the tag.
     *
     * @return the tag.
     */
    @Nullable
    public Object getTag() {
        return mTag;
    }

    /**
     * Set the card dismissible.
     *
     * @param dismissible {@code true} to be able to remove the card or {@code false} otherwise.
     */
    public void setDismissible(final boolean dismissible) {
        mDismissible = dismissible;
    }

    /**
     * Is the card dismissible.
     *
     * @return {@code true} if the card is removeable or {@code false} otherwise.
     */
    public boolean isDismissible() {
        return mDismissible;
    }

    /**
     * Removes the card.
     */
    public void dismiss() {
        getProvider().notifyDataSetChanged(new DismissEvent(this));
    }

    /**
     * The Card Builder configures the card.
     */

    public static class Builder {

        @NonNull
        private final Context mContext;
        @Nullable
        private Object mTag;
        private boolean mDismissible;
        private CardProvider mProvider;

        public Builder(@NonNull final Context context) {
            mContext = context;
        }


        @NonNull
        public Builder setTag(@Nullable final Object object)
        {
            mTag = object;
            return this;
        }

        /**
         * Set the card dismissible - it is then removable.
         */
        @NonNull
        public Builder setDismissible() {
            mDismissible = true;
            return this;
        }

        @NonNull
        public <T extends CardProvider> T withProvider (T content){
            mProvider = content;
            content.setContext(mContext);
            content.setBuilder(this);
            return content;
        }


        /**
         * Builds the card.
         *
         * @return the card.
         */
        @NonNull
        public Card build() {
            if (mProvider == null) {
                throw new IllegalStateException("You have to define the Card Provider");
            }
            return new Card(this);
        }

    }
}
