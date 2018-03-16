package com.xindu.talkfx_new;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xindu.talkfx_new.base.MJsonCallBack;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.hello).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("userName", "15638559970");
                    obj.put("passWord", "5201314Zel");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OkGo.<LoginResponse>post("http://192.168.1.24:3000/talkfx-new/customer/login")
                        .upJson(obj)
                        .execute(new MJsonCallBack<LoginResponse>() {
                            @Override
                            public void onSuccess(Response<LoginResponse> response) {
                                LoginResponse loginResponse = response.body();
                                if (loginResponse.getCode() == 0) {
                                    if (loginResponse.getMsg().equals("000203")) {
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "状态码：" + loginResponse.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Response<LoginResponse> response) {
                                Log.e("========", "请求失败");
                            }
                        });
            }
        });
    }

    class LoginResponse {

        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
