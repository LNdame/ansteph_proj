package ansteph.com.beecab.card;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by loicStephan on 06/09/16.
 */
public abstract class Action {
    private final Context mContext;
    private CardProvider mProvider;

    public Action(@NonNull final Context context) {
        mContext = context;
    }

    public void setProvider(CardProvider mProvider) {
        this.mProvider = mProvider;
    }

    public Context getContext() {
        return mContext;
    }

    protected void notifyActionChanged()
    {
        if(mProvider !=null){
            // Only notify if something changed at runtime
            mProvider.notifyDataSetChanged();
        }
    }

    protected abstract void onRender(@NonNull final View view, @NonNull final Card card);

}
