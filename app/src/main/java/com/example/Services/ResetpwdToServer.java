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
 * Created by dell on 2016/12/12.
 */

public class ResetpwdToServer {
    private String url= IPUtil.IP+IPUtil.PORT+"/MyServlet/resetmm";
    private Handler handler;
    public static final int SEND_SUCCESS=0x123;
    public static final int SEND_FAIL=0x124;
    public ResetpwdToServer(Handler handler){
        this.handler=handler;
    }

    public void resetpwdToServer(String name,String pass_old,String pass_new){
           final Map<String,String> map=new HashMap<String,String>();
            map.put("name",name);
            map.put("pass_old",pass_old);
            map.put("pass_new",pass_new);
             new Thread(new Runnable() {
                 @Override
                 public void run() {
                     try {
                         if (sendGetRequest(map,url,"utf-8")) {
                             handler.sendEmptyMessage(SEND_SUCCESS);//通知主线程数据发送成功
                         }else {
                             //将数据发送给服务器失败
                         }
                     } catch (Exception e) {
                         // TODO Auto-generated catch block
                         e.printStackTrace();
                     };
             }
             }).start();


    }

    public boolean sendGetRequest(Map<String,String> params,String url,String encoding)throws Exception{
        StringBuffer sb=new StringBuffer(url);
        if(!url.isEmpty()&&!params.isEmpty()){
            sb.append("?");
            for(Map.Entry<String,String> entry :params.entrySet()){
                sb.append(entry.getKey()+"=");
                sb.append(URLEncoder.encode(entry.getValue(), encoding));
                sb.append("&");
            }
            sb.deleteCharAt(sb.length()-1);
        }
        HttpURLConnection conn=(HttpURLConnection)new URL(sb.toString()).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);

        if(conn.getResponseCode()==200){
            ByteArrayOutputStream byo=new ByteArrayOutputStream();
            InputStream is=conn.getInputStream();
            int len=0;
            byte buffer[] = new byte[1024];// 定义缓冲区
            while((len=is.read(buffer))!=-1){
                byo.write(buffer,0,len);

            }
            is.close();
            byo.flush();
            byo.close();
            return true;
        }


        return false;
    }

}
