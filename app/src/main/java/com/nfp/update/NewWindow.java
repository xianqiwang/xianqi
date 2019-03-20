package com.nfp.update;
import android.view.Window;
import com.nfp.update.nfpapp.app.core.SoftKey;
public class NewWindow extends Window {

    public boolean forceNoReFreshSoftkeyGuide = false;
    // added by fuying {
    private boolean  mHasNavigation = true;
    private SoftKey mSoftkey_sk1 = null;
    private SoftKey mSoftkey_sk2 = null;
    private SoftKey mSoftkey_cks = null;
    private int mFeatures;
    private Window mContainer;
    private int mLocalFeatures;
    public SoftKey getCsk(){
        return mSoftkey_cks;
    }
    public SoftKey getSk1(){
        return mSoftkey_sk1;
    }
    public SoftKey getSk2(){
        return mSoftkey_sk2;
    }

    public void setFeatures(int feature) {
        mFeatures = mFeatures | (1 << feature);
    }

    protected void removeFeature(int featureId) {
        final int flag = 1<<featureId;
        mFeatures &= ~flag;
        mLocalFeatures &= ~(mContainer != null ? (flag&~mFeatures) : flag);
    }
    public NewWindow (android.content.Context context) {
        super (context);
        mSoftkey_sk1 = new SoftKey(1,"@SK_AUTO_MENU",true);
        mSoftkey_sk2 = new SoftKey(2,"@SK_AUTO",true);
        mSoftkey_cks = new SoftKey(0,"@SK_AUTO_SELECT",true);
        mHasNavigation = true;
    }
    public void setNavigationStatus(boolean hasNavigation){
        mHasNavigation = hasNavigation;
    }
    /**
     * Take ownership of this window's surface.  The window's view hierarchy
     * will no longer draw into the surface, though it will otherwise continue
     * to operate (such as for receiving input events).  The given SurfaceHolder
     * callback will be used to tell you about state changes to the surface.
     *
     * @param callback
     */
    @Override
    public void takeSurface (android.view.SurfaceHolder.Callback2 callback) {

    }

    /**
     * Take ownership of this window's InputQueue.  The window will no
     * longer read and dispatch input events from the queue; it is your
     * responsibility to do so.
     *
     * @param callback
     */
    @Override
    public void takeInputQueue (android.view.InputQueue.Callback callback) {

    }

    /**
     * Return whether this window is being displayed with a floating style
     * (based on the {@link android.R.attr#windowIsFloating} attribute in
     * the style/theme).
     *
     * @return Returns true if the window is configured to be displayed floating
     * on top of whatever is behind it.
     */
    @Override
    public boolean isFloating () {
        return false;
    }


    @Override
    public void setContentView (int layoutResID) {

    }


    @Override
    public void setContentView (android.view.View view) {

    }

    /**
     * Set the screen content to an explicit view.  This view is placed
     * directly into the screen's view hierarchy.  It can itself be a complex
     * view hierarchy.
     * <p>
     * <p>Note that calling this function "locks in" various characteristics
     * of the window that can not, from this point forward, be changed: the
     * features that have been requested with {@link #requestFeature(int)},
     * and certain window flags as described in {@link #setFlags(int, int)}.</p>
     * <p>
     * <p>If {@link #FEATURE_CONTENT_TRANSITIONS} is set, the window's
     * TransitionManager will be used to animate content from the current
     * content View to view.</p>
     *
     * @param view   The desired content to display.
     * @param params Layout parameters for the view.
     * @see #getTransitionManager()
     * @see #setTransitionManager(android.transition.TransitionManager)
     */
    @Override
    public void setContentView (android.view.View view, android.view.ViewGroup.LayoutParams params) {

    }


    @Override
    public void addContentView (android.view.View view, android.view.ViewGroup.LayoutParams params) {

    }

    /**
     * Return the view in this Window that currently has focus, or null if
     * there are none.  Note that this does not look in any containing
     * Window.
     *
     * @return View The current View with focus or null.
     */
    @android.support.annotation.Nullable
    @Override
    public android.view.View getCurrentFocus () {
        return null;
    }


    @android.support.annotation.NonNull
    @Override
    public android.view.LayoutInflater getLayoutInflater () {
        return null;
    }

    @Override
    public void setTitle (CharSequence title) {

    }

    @Override
    public void setTitleColor (int textColor) {

    }

    @Override
    public void openPanel (int featureId, android.view.KeyEvent event) {

    }

    @Override
    public void closePanel (int featureId) {

    }

    @Override
    public void togglePanel (int featureId, android.view.KeyEvent event) {

    }

    @Override
    public void invalidatePanelMenu (int featureId) {

    }

    @Override
    public boolean performPanelShortcut (int featureId, int keyCode, android.view.KeyEvent event, int flags) {
        return false;
    }

    @Override
    public boolean performPanelIdentifierAction (int featureId, int id, int flags) {
        return false;
    }

    @Override
    public void closeAllPanels () {

    }

    @Override
    public boolean performContextMenuIdentifierAction (int id, int flags) {
        return false;
    }

    /**
     * Should be called when the configuration is changed.
     *
     * @param newConfig The new configuration.
     */
    @Override
    public void onConfigurationChanged (android.content.res.Configuration newConfig) {

    }

    /**
     * Change the background of this window to a custom Drawable. Setting the
     * background to null will make the window be opaque. To make the window
     * transparent, you can use an empty drawable (for instance a ColorDrawable
     * with the color 0 or the system drawable android:drawable/empty.)
     *
     * @param drawable The new Drawable to use for this window's background.
     */
    @Override
    public void setBackgroundDrawable (android.graphics.drawable.Drawable drawable) {

    }

    /**
     * Set the value for a drawable feature of this window, from a resource
     * identifier.  You must have called requestFeature(featureId) before
     * calling this function.
     *
     * @param featureId The desired drawable feature to change, defined as a
     *                  constant by Window.
     * @param resId     Resource identifier of the desired image.
     * @see android.content.res.Resources#getDrawable(int)
     */
    @Override
    public void setFeatureDrawableResource (int featureId, int resId) {

    }

    /**
     * Set the value for a drawable feature of this window, from a URI. You
     * must have called requestFeature(featureId) before calling this
     * function.
     * <p>
     * <p>The only URI currently supported is "content:", specifying an image
     * in a content provider.
     *
     * @param featureId The desired drawable feature to change. Features are
     *                  constants defined by Window.
     * @param uri       The desired URI.
     * @see android.widget.ImageView#setImageURI
     */
    @Override
    public void setFeatureDrawableUri (int featureId, android.net.Uri uri) {

    }

    /**
     * Set an explicit Drawable value for feature of this window. You must
     * have called requestFeature(featureId) before calling this function.
     *
     * @param featureId The desired drawable feature to change. Features are
     *                  constants defined by Window.
     * @param drawable  A Drawable object to display.
     */
    @Override
    public void setFeatureDrawable (int featureId, android.graphics.drawable.Drawable drawable) {

    }

    /**
     * Set a custom alpha value for the given drawable feature, controlling how
     * much the background is visible through it.
     *
     * @param featureId The desired drawable feature to change. Features are
     *                  constants defined by Window.
     * @param alpha     The alpha amount, 0 is completely transparent and 255 is
     */
    @Override
    public void setFeatureDrawableAlpha (int featureId, int alpha) {

    }

    /**
     * Set the integer value for a feature. The range of the value depends on
     * the feature being set. For {@link #FEATURE_PROGRESS}, it should go from
     * 0 to 10000. At 10000 the progress is complete and the indicator hidden.
     *
     * @param featureId The desired feature to change. Features are constants
     *                  defined by Window.
     * @param value     The value for the feature. The interpretation of this
     */
    @Override
    public void setFeatureInt (int featureId, int value) {

    }

    /**
     * Request that key events come to this activity. Use this if your
     * activity has no views with focus, but the activity still wants
     * a chance to process key events.
     *
     * @param get
     */
    @Override
    public void takeKeyEvents (boolean get) {

    }

    /**
     * Used by custom windows, such as Dialog, to pass the key press event
     * further down the view hierarchy. Application developers should
     * not need to implement or call this.
     *
     * @param event
     */
    @Override
    public boolean superDispatchKeyEvent (android.view.KeyEvent event) {
        return false;
    }

    /**
     * Used by custom windows, such as Dialog, to pass the key shortcut press event
     * further down the view hierarchy. Application developers should
     * not need to implement or call this.
     *
     * @param event
     */
    @Override
    public boolean superDispatchKeyShortcutEvent (android.view.KeyEvent event) {
        return false;
    }

    /**
     * Used by custom windows, such as Dialog, to pass the touch screen event
     * further down the view hierarchy. Application developers should
     * not need to implement or call this.
     *
     * @param event
     */
    @Override
    public boolean superDispatchTouchEvent (android.view.MotionEvent event) {
        return false;
    }

    /**
     * Used by custom windows, such as Dialog, to pass the trackball event
     * further down the view hierarchy. Application developers should
     * not need to implement or call this.
     *
     * @param event
     */
    @Override
    public boolean superDispatchTrackballEvent (android.view.MotionEvent event) {
        return false;
    }

    /**
     * Used by custom windows, such as Dialog, to pass the generic motion event
     * further down the view hierarchy. Application developers should
     * not need to implement or call this.
     *
     * @param event
     */
    @Override
    public boolean superDispatchGenericMotionEvent (android.view.MotionEvent event) {
        return false;
    }

    @Override
    public android.view.View getDecorView () {
        return null;
    }

    /**
     * Retrieve the current decor view, but only if it has already been created;
     * otherwise returns null.
     *
     * @return Returns the top-level window decor or null.
     * @see #getDecorView
     */
    @Override
    public android.view.View peekDecorView () {
        return null;
    }

    @Override
    public android.os.Bundle saveHierarchyState () {
        return null;
    }

    @Override
    public void restoreHierarchyState (android.os.Bundle savedInstanceState) {

    }

    @Override
    protected void onActive () {

    }

    @Override
    public void setChildDrawable (int featureId, android.graphics.drawable.Drawable drawable) {

    }

    @Override
    public void setChildInt (int featureId, int value) {

    }

    /**
     * Is a keypress one of the defined shortcut keys for this window.
     *
     * @param keyCode the key code from {@link android.view.KeyEvent} to check.
     * @param event   the {@link android.view.KeyEvent} to use to help check.
     */
    @Override
    public boolean isShortcutKey (int keyCode, android.view.KeyEvent event) {
        return false;
    }

    /**
     * @param streamType
     * @see android.app.Activity#setVolumeControlStream(int)
     */
    @Override
    public void setVolumeControlStream (int streamType) {

    }

    /**
     * @see android.app.Activity#getVolumeControlStream()
     */
    @Override
    public int getVolumeControlStream () {
        return 0;
    }

    /**
     * @return the color of the status bar.
     */
    @Override
    public int getStatusBarColor () {
        return 0;
    }

    /**
     * Sets the color of the status bar to {@code color}.
     * <p>
     * For this to take effect,
     * the window must be drawing the system bar backgrounds with
     * {@link android.view.WindowManager.LayoutParams#FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS} and
     * {@link android.view.WindowManager.LayoutParams#FLAG_TRANSLUCENT_STATUS} must not be set.
     * <p>
     * If {@code color} is not opaque, consider setting
     * {@link android.view.View#SYSTEM_UI_FLAG_LAYOUT_STABLE} and
     * {@link android.view.View#SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN}.
     * <p>
     * The transitionName for the view background will be "android:status:background".
     * </p>
     *
     * @param color
     */
    @Override
    public void setStatusBarColor (int color) {

    }

    /**
     * @return the color of the navigation bar.
     */
    @Override
    public int getNavigationBarColor () {
        return 0;
    }

    /**
     * Sets the color of the navigation bar to {@param color}.
     * <p>
     * For this to take effect,
     * the window must be drawing the system bar backgrounds with
     * {@link android.view.WindowManager.LayoutParams#FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS} and
     * {@link android.view.WindowManager.LayoutParams#FLAG_TRANSLUCENT_NAVIGATION} must not be set.
     * <p>
     * If {@param color} is not opaque, consider setting
     * {@link android.view.View#SYSTEM_UI_FLAG_LAYOUT_STABLE} and
     * {@link android.view.View#SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION}.
     * <p>
     * The transitionName for the view background will be "android:navigation:background".
     * </p>
     *
     * @param color
     * @attr ref android.R.styleable#Window_navigationBarColor
     */
    @Override
    public void setNavigationBarColor (int color) {

    }

    /**
     * Set what color should the caption controls be. By default the system will try to determine
     * the color from the theme. You can overwrite this by using {@link #DECOR_CAPTION_SHADE_DARK},
     * {@link #DECOR_CAPTION_SHADE_LIGHT}, or {@link #DECOR_CAPTION_SHADE_AUTO}.
     *
     * @param decorCaptionShade
     * @see #DECOR_CAPTION_SHADE_DARK
     * @see #DECOR_CAPTION_SHADE_LIGHT
     * @see #DECOR_CAPTION_SHADE_AUTO
     */
    @Override
    public void setDecorCaptionShade (int decorCaptionShade) {

    }

    /**
     * Set the drawable that is drawn underneath the caption during the resizing.
     * <p>
     * During the resizing the caption might not be drawn fast enough to match the new dimensions.
     * There is a second caption drawn underneath it that will be fast enough. By default the
     * caption is constructed from the theme. You can provide a drawable, that will be drawn instead
     * to better match your application.
     *
     * @param drawable
     */
    @Override
    public void setResizingCaptionDrawable (android.graphics.drawable.Drawable drawable) {

    }
}
