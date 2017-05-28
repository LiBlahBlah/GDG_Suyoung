package com.flybbird.hometownweather.Base;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.widget.Toast;


/**
 * Super Fragment Class
 *
 * Created by flybbird on 2017. 3. 9..
 */
public class BaseFragment extends Fragment implements IFragmentView {
    protected String TAG = this.getClass().getSimpleName();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
    }




    public boolean onBackPressed(){
        return true;
    }


    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public Activity getActivityInstance() {
        return getActivity();
    }

    @Override
    public void showToast(String message) {
        // 공용 토스트 TEST
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOneConfirmPopup(String message) {
        // 원버튼 팝업
    }

    @Override
    public void showProgress(boolean isCancelable, DialogInterface.OnCancelListener listener) {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void closeSideMenu() {
    }

    @Override
    public void openSideMenu() {
    }

    @Override
    public void closeKeyPad() {
//        KeyboardUtility.hideSoftKeyboard(getActivity());
    }
}
