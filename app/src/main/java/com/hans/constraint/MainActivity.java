package com.hans.constraint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hans.constraint.server.IAdditionService;

public class MainActivity extends AppCompatActivity {

    private Button bt_num;
    private TextView tv_content;
    IAdditionService mService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_num = findViewById(R.id.bt_num);
        tv_content = findViewById(R.id.tv_content);

        Intent intent = new Intent();
        /*服务端包名  服务端service 隐式启动 服务过滤器*/
        intent.setPackage("com.hans.constraint");
        intent.setAction("com.hans.constraint.server.action");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        bt_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int add = mService.add(10, 113);
                    int remove = mService.remove(113, 10);
                    tv_content.setText(add + "=="+remove);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IAdditionService.Stub.asInterface(service);
            if (mService == null) {
                Log.d("打印", "mService为空");
            } else {
                Log.d("打印", "mService不为空");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("打印", "mService连接失败");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnection != null) {
            unbindService(mConnection);
        }
    }
}
