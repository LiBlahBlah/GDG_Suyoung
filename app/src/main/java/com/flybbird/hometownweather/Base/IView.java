package com.flybbird.hometownweather.Base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

/**
 * View Common Interface
 */
public interface IView {
    /***
     * Common Get  Activity Context
     * @return COntext Activity Context.
     */
    Context getActivityContext();


    /**
     * Base Get Activity object
     * // getActivity 랑 혼용되서 이름바꿈
     *
     * @return Activty .
     */
    Activity getActivityInstance();


    /**
     * 테스트 Toast Message Popup
     *
     * @param message 토스트 표시할 메시지.
     */
    void showToast(String message);

    /**
     * "확인" 원버튼을 가진 팝업 SHOW ~~~
     *
     * @param message
     */
    void showOneConfirmPopup(String message);

    /**
     * Show Network Loading Progress
     */
    void showProgress(boolean isCancelable, DialogInterface.OnCancelListener listener);

    /**
     * Hide Network Loading Progress
     */
    void hideProgress();


    /**
     * 사이드 메뉴 닫기
     */
    void closeSideMenu();

    /**
     * 사이드 메뉴 열기
     */
    void openSideMenu();

    /**
     * 키보드 닫기
     */
    void closeKeyPad();
}
