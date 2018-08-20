package com.wk.wktransportation.rxbus;

import android.util.Log;


import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/7/17 22:46
 * **********************************
 **/
public class RxBus {
    private static final String TAG = "RxBus";
    private static final ArrayList<Subscriber> subscribers = new ArrayList<>();

    public static void send(final Event event){
        Observable.create(new ObservableOnSubscribe<Event>() {
            @Override
            public void subscribe(ObservableEmitter<Event> emitter) throws Exception {
                Log.d(TAG, "do==== RxBus subscribe:"+Thread.currentThread().getName());
                emitter.onNext(event);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Event>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Event o) {
                Log.d(TAG, "do==== RxBus onNext:"+Thread.currentThread().getName());
                Log.d(TAG, "do==== RxBus onNext:code:"+o.code());
                Log.d(TAG, "do==== RxBus onNext:object"+o.object());
                for (Subscriber subscriber:subscribers){
                    subscriber.onNotification(o);
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "do====RxBus.onError.: e: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "do==== RxBus onComplete:");
            }
        });
    }
    public static void register(Subscriber subscriber){
        if (!subscribers.contains(subscriber)){
            subscribers.add(subscriber);
        }else {
            Log.e(TAG, "do====RxBus.register.: dump register ");
        }
    }
    public static void unRegister(Subscriber subscriber){
        if (subscribers.contains(subscriber)){
            subscribers.remove(subscriber);
        }else {
            Log.e(TAG, "do====RxBus.unRegister.: no such subscriber ");
        }
    }
    public interface Subscriber {
        void onNotification(Event event);
    }
}
