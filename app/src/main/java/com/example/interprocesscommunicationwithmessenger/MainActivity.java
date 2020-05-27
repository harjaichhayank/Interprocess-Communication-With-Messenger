package com.example.interprocesscommunicationwithmessenger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    final int JOB_1 = 1;
    final int JOB_2 = 2;
    final int JOB_1_RESPONSE = 3;
    final int JOB_2_RESPONSE = 4;
    boolean isBind = false;
    Messenger messenger;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, MyService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        textView = findViewById(R.id.textView);
    }

    class ResponseHandler extends Handler{
        String message;
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case JOB_1_RESPONSE:
                case JOB_2_RESPONSE:
                    message = msg.getData().getString("response message");
                    textView.setText(message);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
            messenger = null;
        }
    };

    @Override
    protected void onStop() {
        unbindService(serviceConnection);
        messenger = null;
        isBind = false;
        super.onStop();
    }

    public void getMessage(View view) {
        String button_text =(String) ((Button)view).getText();
        if (button_text.equals("GET FIRST MESSAGE")){
            Message msg = Message.obtain(null,JOB_1);
            msg.replyTo = new Messenger(new ResponseHandler());
            try {
                messenger.send(msg);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
        else if(button_text.equals("GET SECOND MESSAGE")){
            Message msg = Message.obtain(null,JOB_2);
            msg.replyTo = new Messenger(new ResponseHandler());
            try {
                messenger.send(msg);
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
    }
}
