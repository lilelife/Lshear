package com.example.Services;

import android.os.Handler;

import com.example.Utils.IPUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/12/14.
 */

public class QueryFriend {
    private String url= IPUtil.IP+IPUtil.PORT+"/MyServlet/friendHas";
    private Handler handler;
    public static final int SEND_SUCCESS=0x123;
    public static final int SEND_FAIL=0x124;
    public QueryFriend(Handler handler){

        this.handler=handler;
    }

    public void queryFriendToServer(String userName,String FriendName){
        final Map<String,String> map=new HashMap<String, String>();
        map.put("name",userName);
        map.put("friendName",FriendName);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(sendGetRequest(map,url,"UTF-8")){
                        handler.sendEmptyMessage(SEND_SUCCESS);//通知主线程数据发送成功
                    }else{
                        handler.sendEmptyMessage(SEND_FAIL);
                    }

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
            }
        }).start();


    }
    public  boolean sendGetRequest(Map<String,String> params,String url,String encoding)throws  Exception{
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
            int i=Integer.parseInt(result.trim());
            if(i==0){
                return true;
            }
        }
        return false;
    }

}
