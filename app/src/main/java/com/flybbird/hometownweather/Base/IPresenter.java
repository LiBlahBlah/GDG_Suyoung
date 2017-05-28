package com.flybbird.hometownweather.Base;


/**
 * Base Presenter Model
 * @param <T>
 */
public interface IPresenter<T extends IView> {
    /** AttachView */
    void attachView(T view);

    /** DetachView */
    void detachView();
}
