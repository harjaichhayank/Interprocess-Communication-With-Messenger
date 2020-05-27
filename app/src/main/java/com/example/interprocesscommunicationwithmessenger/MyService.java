package com.example.interprocesscommunicationwithmessenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

public class MyService extends Service {
    Messenger messenger = new Messenger(new IncomingHandler());
    final int JOB_1 = 1;
    final int JOB_2 = 2;
    final int JOB_1_RESPONSE = 3;
    final int JOB_2_RESPONSE = 4;

    class IncomingHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            Message MSG;
            Bundle bundle = new Bundle();
            String Message;
            switch (msg.what){
                case JOB_1:
                    Message = "First Message From Service...";
                    MSG = android.os.Message.obtain(null, JOB_1_RESPONSE);
                    bundle.putString("response message",Message);
                    MSG.setData(bundle);
                    try {
                        msg.replyTo.send(MSG);
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                    break;
                case JOB_2:
                    Message = "Second Message From Service...";
                    MSG = android.os.Message.obtain(null, JOB_2_RESPONSE);
                    bundle.putString("response message",Message);
                    MSG.setData(bundle);
                    try {
                        msg.replyTo.send(MSG);
                    }catch (RemoteException e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
