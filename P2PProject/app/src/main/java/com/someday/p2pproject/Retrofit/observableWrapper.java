package com.someday.p2pproject.Retrofit;

import android.util.Log;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;
import java.util.Observable;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

public abstract class observableWrapper <T> extends DisposableObserver<T> {

        //private WeakReference weakReference;

        public observableWrapper() {
            //this.weakReference = new WeakReference();
        }

        protected abstract void onSuccess(T t);

        @Override
        public void onNext(T t) {
            //You can return StatusCodes of different cases from your API and handle it here.
            // I usually include these cases on BaseResponse and iherit it from every Response
            onSuccess(t);
        }

        @Override
        public void onError(Throwable e) {
            //  BaseView view = weakReference.get();
            if (e instanceof HttpException) {
                // ResponseBody responseBody = ((HttpException)e).response().errorBody();
                //view.onUnknownError(getErrorMessage(responseBody));
                Log.i("HttpException", e.toString());
            } else if (e instanceof SocketTimeoutException) {
                // view.onTimeout();
                Log.i("SocketTimeOout", e.toString());
            } else if (e instanceof IOException) {
                //  view.onNetworkError();
                Log.i("IOException", e.toString());
            } else {
                // view.onUnknownError(e.getMessage());
                Log.i("unknownException", e.toString());
            }
        }

        @Override
        public void onComplete() {

        }

       /* private String getErrorMessage(ResponseBody responseBody) {
            try {
                JSONObject jsonObject = new JSONObject(responseBody.string());
                return jsonObject.getString("message");
            } catch (Exception e) {
                return e.getMessage();
            }
        }*/

}

