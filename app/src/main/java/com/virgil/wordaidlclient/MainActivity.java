package com.virgil.wordaidlclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.virgil.wordaidl.IWordAidlInterface;

public class MainActivity extends AppCompatActivity {

    private IWordAidlInterface mIwordAidlInterface;
    private ServiceConnection mConn = null;
    private TextView mTextView_showData = null;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //调用绑定服务的方法
        bindService();
    }


    /**
     * 绑定服务
     */
    private void bindService() {
        Intent intent = new Intent();
        //填写服务端清单文件中，注册Service时自定义的字符串
        intent.setPackage("com.virgil.wordaidlservice");
        intent.setAction("com.virgil.wordaidlservice.WordAIDLService");

        mConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mIwordAidlInterface = IWordAidlInterface.Stub.asInterface(service);
                Log.i("------>>>", "aaa");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIwordAidlInterface = null;
            }
        };
        flag = bindService(intent, mConn, BIND_AUTO_CREATE);
    }

    private void initView() {
        mTextView_showData = (TextView) findViewById(R.id.textView_showData);
    }

    public void clickView(View view) {
        if (flag && mIwordAidlInterface != null) {
            try {
                String value = mIwordAidlInterface.getValue();
                mTextView_showData.setText(value);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "连接断开", Toast.LENGTH_SHORT).show();
        }
    }
}
