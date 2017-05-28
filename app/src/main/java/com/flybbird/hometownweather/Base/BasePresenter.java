package com.flybbird.hometownweather.Base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;



/**
 * BasePresenter Class
 * <p>
 * Created by flybbird on 2017. 2. 22..
 */
public class BasePresenter<T extends IView> implements IPresenter<T> {
    public final static String TAG = BasePresenter.class.getSimpleName();

    private T attachView;

    @Override
    public void attachView(T view) {
        this.attachView = view;
    }

    @Override
    public void detachView() {
        this.attachView = null;
    }

    /**
     * Presenter에 attaching 된 View Interface 객체
     *
     * @return .
     */
    public T getAttachView() {
        return this.attachView;
    }

    /**
     * Presenter에 Attaching 된 View가 있는지
     *
     * @return .
     */
    public Boolean isAttachView() {
        if (this.attachView != null)
            return true;
        return false;
    }

    /**
     * Context 객체를 가져온다
     *
     * @return Context .
     */
    public Context getContext() {
        return getAttachView().getActivityContext();
    }

    /**
     * Activity 객체를 돌려준다.
     *
     * @return Activity .
     */
    public Activity getIActivity() {
        return getAttachView().getActivityInstance();
    }

//    /**
//     * 네트워크 인터페이스 객체
//     *
//     * @return APICommand 인터페이스(프로토콜 체이닝 리스트) .
//     */
//    protected APICommand Fetcher() {
//        return APIClient.getInstance().getApiCommand();
//    }

    /**
     * 사이드 메뉴 닫기
     */
    public void closeSideMenu() {
        if (getAttachView() != null)
            getAttachView().closeSideMenu();
    }

    /**
     * 사이드 메뉴 열기
     */
    public void openSideMenu() {
        if (getAttachView() != null)
            getAttachView().openSideMenu();
    }

    /**
     * SHOW TOAST MESSAGE
     *
     * @param message 내용.
     */
    public void showToast(String message) {
        if (getAttachView() != null) {
            getAttachView().showToast(message);
        }
    }



    private void launchPage(Context context, Class launchClass, Bundle bundle) {
        Intent intent = new Intent(context, launchClass);

        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }
}
