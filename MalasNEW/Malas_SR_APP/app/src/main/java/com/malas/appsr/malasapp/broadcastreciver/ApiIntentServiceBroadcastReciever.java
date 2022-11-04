package com.malas.appsr.malasapp.broadcastreciver;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Amit on 11/07/2016.
 */
public class ApiIntentServiceBroadcastReciever extends ResultReceiver {
    private Receiver mReceiver;

    public ApiIntentServiceBroadcastReciever(Handler handler) {
        super(handler);
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public interface Receiver {
        void onReceiveResult(int resultCode, Bundle resultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            mReceiver.onReceiveResult(resultCode, resultData);
        }
    }
}
