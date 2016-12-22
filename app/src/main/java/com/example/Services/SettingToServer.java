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
 * Created by dell on 2016/12/19.
 */

public class SettingToServer {
    private static String url= IPUtil.IP+IPUtil.PORT+"/MyServlet/setPersion";
    public static final int SEND_SUCCESS=0x123;
    public static final int SEND_FAIL=0x124;


    public void SendDataToServer(String name,String cn1, String cn2,String cn3) {
        // TODO Auto-generated method stub
        final Map<String, String> map=new HashMap<String, String>();
        map.put("name",name);
        map.put("n1",cn1); //昵称
        map.put("n2",cn2);//个性签名
        map.put("n3",cn3);//个人信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    sendPostRequest(map,url,"utf-8");
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
            return true;
        }
        return false;
    }
}


