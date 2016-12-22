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
 * Created by dell on 2016/12/11.
 */

public class SendDateToLogin {
    private static String url= IPUtil.IP+IPUtil.PORT+"/MyServlet/login";
    public static final int SEND_SUCCESS=0x123;
    public static final int SEND_FAIL=0x124;
    private Handler handler;
    public SendDateToLogin(Handler handler) {
        // TODO Auto-generated constructor stub
        this.handler=handler;
    }
    /**
     * 通过GET方式向服务器发送数据
     * @param name 用户名
     * @param pwd  密码
     */
    public void SendDataToLogin(String name, String pwd) {
        // TODO Auto-generated method stub
        final Map<String, String> map=new HashMap<String, String>();
        map.put("name", name);
        map.put("pass", pwd);
        System.out.println("____>"+map.get("name"));
        System.out.println("--->"+map.get("pass"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    if (sendPostRequest(map,url,"utf-8")) {
                        handler.sendEmptyMessage(SEND_SUCCESS);//通知主线程数据发送成功
                    }else {
                        //将数据发送给服务器失败
                        handler.sendEmptyMessage(SEND_FAIL);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
    /**
     * 发送GET请求
     * @param param 请求参数
     * @param url 请求路径
     * @return
     * @throws Exception
     */
    private  boolean sendPostRequest(Map<String,String> param, String url, String encoding) throws Exception {
        // TODO Auto-generated method stub
        //http://10.219.61.117:8080/ServerForPOSTMethod/ServletForPOSTMethod?name=aa&pwd=124
        StringBuffer sb=new StringBuffer(url);
        if (!url.equals("")&!param.isEmpty()) {
            sb.append("?");
            for (Map.Entry<String,String>entry:param.entrySet()) {
                sb.append(entry.getKey()+"=");
                sb.append(URLEncoder.encode(entry.getValue(), encoding));
                sb.append("&");
            }
            sb.deleteCharAt(sb.length()-1);//删除字符串最后 一个字符“&”
        }

        byte[]data=sb.toString().getBytes();
        HttpURLConnection conn=(HttpURLConnection) new URL(sb.toString()).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);

        if (conn.getResponseCode()==200) {
            InputStream is = conn.getInputStream();
            // 创建字节输出流对象
            ByteArrayOutputStream message = new ByteArrayOutputStream();
            // 定义读取的长度
            int len = 0;
            // 定义缓冲区
            byte buffer[] = new byte[1024];
            // 按照缓冲区的大小，循环读取
            while ((len = is.read(buffer)) != -1) {
                // 根据读取的长度写入到os对象中
                message.write(buffer, 0, len);
            }

            String msg = new String(message.toByteArray());
            System.out.println("RESPONSE"+msg);
            int d= Integer.parseInt(msg.trim());
            System.out.println(d==1);
            if(d==1){
                System.out.print("sss");
                return true;

            }

        }
        System.out.print("NNNN");
        return false;
    }

}
