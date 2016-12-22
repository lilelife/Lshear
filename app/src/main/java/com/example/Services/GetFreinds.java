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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/12/14.
 * 获取当前用户的好友名单 并在Maintivity中的好友列表中显示
 */

public class GetFreinds {
    private String url= IPUtil.IP+IPUtil.PORT+"/MyServlet/getFriends";
    public  static ArrayList<String> list=new ArrayList<String>();

    final  ArrayList<String> arr=new ArrayList<String>();
    public static final int SEND_SUCCESS=0x123;
    public static final int SEND_FAIL=0x124;
    private Handler handler;
    public GetFreinds(Handler handler) {
        // TODO Auto-generated constructor stub
        this.handler=handler;
    }

    public ArrayList<String> getFreinds(String userName){
        final Map<String,String> map=new HashMap<String, String>();
        map.put("name",userName);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    list.clear();//
                    list=sendGetRequest(map,url,"utf-8");
                    handler.sendEmptyMessage(SEND_SUCCESS);
                    Message msg = handler.obtainMessage();

                    //msg.arg1 = 123;//传递整型数据
                    //msg.obj = "asd";传递object类型

                    //利用bundle对象来传值
                    Bundle b = new Bundle();
                    String []strings=new String[list.size()];

                    for(int i=0;i<list.size();i++){
                        strings[i]=list.get(i);
                    }
                    b.putStringArray("sArrs",strings);

                    b.putString("str","lilewn");
                    msg.what=1;
                    msg.setData(b);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
        return arr;
    }
    public   ArrayList<String> sendGetRequest(Map<String,String> params,String url,String encoding)throws  Exception{
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
            String result=new String(by.toByteArray());
            String [] arrs=result.split(":");
            for(String str:arrs){
                list.add(str);
            }

        }

        return list;
    }

}