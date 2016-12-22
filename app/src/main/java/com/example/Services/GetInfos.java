package com.example.Services;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.Utils.IPUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/12/19.
 * 用户登录时，根据其名称获取其昵称等信息
 */

public class GetInfos {
    private String url= IPUtil.IP+IPUtil.PORT+"/MyServlet/getInfo";
    public static final int SEND_SUCCESS=0x123;
    public static final int SEND_FAIL=0x124;
    private Handler handler;
    public static String   str;
    public GetInfos(Handler handler) {
        // TODO Auto-generated constructor stub
        this.handler=handler;
    }
    public void getInfos(String name){
        final Map<String,String> map=new HashMap<String, String>();

        map.put("name",name);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    str=sendGetRequest(map,url,"utf-8");
                    handler.sendEmptyMessage(SEND_SUCCESS);
                    Message msg = handler.obtainMessage();

                    //msg.arg1 = 123;//传递整型数据
                    //msg.obj = "asd";传递object类型

                    //利用bundle对象来传值
                    Bundle b = new Bundle();

                    b.putString("str",str);
                    msg.what=1;
                    msg.setData(b);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public String sendGetRequest(Map<String,String> params, String url, String encoding)throws  Exception{
        String result=null;
        StringBuffer sb=new StringBuffer(url);
        if(!url.equals("")&&!params.isEmpty()){
            sb.append("?");
            for(Map.Entry<String,String> entry:params.entrySet()){
                sb.append(entry.getKey()+"=");
                sb.append(URLEncoder.encode(entry.getValue(), encoding)); //这里当时写成了equels 然后boom！！！！！！！！！！！
                sb.append("&");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        // System.out.println("________________________________"+sb.toString());
        HttpURLConnection conn=(HttpURLConnection) new URL(sb.toString()).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);

        if(conn.getResponseCode()==200){
            InputStream in=conn.getInputStream();
            ByteArrayOutputStream by=new ByteArrayOutputStream();
            int len=0;
            byte [] buffer=new byte[1024];
            while((len=in.read(buffer))!=-1){
                by.write(buffer,0,len);
            }
            System.out.println(new String(by.toByteArray()));
             result=new String(by.toByteArray());
        }

        return result;
    }

}
