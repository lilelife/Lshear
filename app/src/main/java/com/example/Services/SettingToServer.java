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
        map.put("n1",cn1); //�ǳ�
        map.put("n2",cn2);//����ǩ��
        map.put("n3",cn3);//������Ϣ
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
     * ����GET����
     * @param param �������
     * @param url ����·��
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
            sb.deleteCharAt(sb.length()-1);//ɾ���ַ������ һ���ַ���&��
        }
        byte[]data=sb.toString().getBytes();
        HttpURLConnection conn=(HttpURLConnection) new URL(sb.toString()).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);

        if (conn.getResponseCode()==200) {
            InputStream is = conn.getInputStream();
            // �����ֽ����������
            ByteArrayOutputStream message = new ByteArrayOutputStream();
            // �����ȡ�ĳ���
            int len = 0;
            // ���建����
            byte buffer[] = new byte[1024];
            // ���ջ������Ĵ�С��ѭ����ȡ
            while ((len = is.read(buffer)) != -1) {
                // ���ݶ�ȡ�ĳ���д�뵽os������
                message.write(buffer, 0, len);
            }

            String msg = new String(message.toByteArray());
            System.out.println("RESPONSE"+msg);
            return true;
        }
        return false;
    }
}


