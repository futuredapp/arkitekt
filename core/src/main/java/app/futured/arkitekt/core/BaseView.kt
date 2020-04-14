package app.futured.arkitekt.core

/**
 * Represents actions which are meant to be executed on Activity/Fragment.
 * Make sure Activity/Fragment implements overridden version of this interface.
 * Interface is usually used as a data variable in layout file with intention to
 * call methods on Activity or Fragment respectively.
 *
 * Usage:
 *   <data>
 *       <variable
 *           name="view"
 *           type="app.futured.arkitekt.sample.ui.SomeActivityView"/>
 *   </data>
 *   ...
 *   <View
 *        android:onClick="@{() -> view.onTutorialLoginClick()}" />
 *
 */
interface BaseView
