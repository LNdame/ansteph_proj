package ansteph.com.materialshowcase;

/**
 * Created by loicStephan on 16/09/16.
 */
public interface IShowcaseListener {
    void onShowcaseDisplayed(MaterialShowcaseView showcaseView);
    void onShowcaseDismissed(MaterialShowcaseView showcaseView);
}
