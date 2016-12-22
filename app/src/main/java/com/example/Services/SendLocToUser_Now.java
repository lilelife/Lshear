package com.example.Services;

import com.example.Utils.IPUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2016/12/13.
 */


public class SendLocToUser_Now {
    private String url= IPUtil.IP+IPUtil.PORT+"/MyServlet/senLoc";

    public  void sendLocToUser_now(String userName,double latitude,double longitude){
        final Map<String,String> map=new HashMap<String,String>();
        map.put("userName_now",userName);
        map.put("latitude",Double.toString(latitude));
       // System.out.println("______________map的PUTPUT"+Double.toString(latitude)+userName);
        map.put("longitude",Double.toString(longitude));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   sendGetRequest(map,url,"UTF-8");
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
               // System.out.println("___________________"+entry.getKey()+entry.getValue());
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

            return true;
        }

        return false;
    }

}
