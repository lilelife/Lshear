package com.example.lshare;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.PersonInfoSettings.MPoPuWindow;
import com.example.PersonInfoSettings.settings_activity;
import com.example.Services.GetFreinds;
import com.example.Services.GetInfos;
import com.example.Services.SendLocToUser_Now;
import com.example.chat.WeixinChatDemoActivity;
import com.example.login.LoginActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.lshare.R.id.localposition;


public class MainActivity extends Activity {
    private MapView mMapView;
    private LocationClient locationClient;
    private BaiduMap baiduMap;
    private boolean firstLocation;
    private BitmapDescriptor mCurrentMarker;
    private Button btn_loc;
    private Button addFriend;
    private ImageView picture;

    private String userName_now=null;//当前登录用户
    Intent intent=null;
    private  String friendName=null;//添加的朋友名称

    private File file;
    private Uri ImgUri;
    private Type type;
    private Bitmap head;
    private MPoPuWindow puWindow;
    private TextView nickname;
    private TextView signature;
    private TextView personalinfo;
    private TextView introduce;
    private TextView about;

    private Button outLogin;
    private Button settings;
    ArrayAdapter adapter;

    public enum Type {
        PHONE, CAMERA
    }


    ArrayList<Map<String,String>> items1=null;
    ArrayList<String> arrayList=new ArrayList<String>();
    ArrayList<String> itemsNames1=null;
    private ListView list1;
    //好友的
    final Handler handler=new Handler(){
        public void handleMessage(Message msg) {

            ArrayList<String> itemsNames1=new ArrayList<String>();

            String[] arrs=null;
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    // msg.getData();
//                    adapter.notifyDataSetChanged();
                    arrayList.clear();
                    adapter=new ArrayAdapter(getApplicationContext(),R.layout.myexampleitem, arrayList);
                    list1.setAdapter(adapter);

                    Bundle b =msg.getData();
                    arrs=b.getStringArray("sArrs");
                    System.out.println("______________________"+arrs.length);

                    //TODO
                    // System.out.print("length--"+arrs.length);
                    items1=new ArrayList<Map<String, String>>();

                    for(int i=0;i<arrs.length;i++){
                        System.out.print("---"+arrs[i]);
                        Map<String,String> map1=new HashMap<String, String>() ;
                        map1.put("str3","好友"+(i+1));
                        map1.put("str4",arrs[i]);
                        items1.add(map1);
                        arrayList.add(arrs[i]);
                    }
                    SimpleAdapter adapter2=new SimpleAdapter(getApplicationContext(),items1,R.layout.listitem,new String[]{"str3","str4"},
                            new int[]{R.id.itemName,R.id.str2}
                    );
                    adapter=new ArrayAdapter(getApplicationContext(),R.layout.myexampleitem, arrayList);

                    list1.setAdapter(adapter);
            }
        };
    };
    //昵称等
    final Handler handler2=new Handler(){
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Bundle bundle=msg.getData();
                bundle.getString("str");
                String str=bundle.getString("str");
               // System.out.println("--------------{"+str);
                String [] arrs=str.split(":");
                nickname.setText(arrs[0]);
                signature.setText(arrs[1]);
                personalinfo.setText(arrs[2]);
            }
        };
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());//初始化地图
        setContentView(R.layout.activity_main);

        introduce=(TextView)findViewById(R.id.introduce);
        about=(TextView)findViewById(R.id.about);
        introduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, introduce_activity.class);
                startActivity(intent);

            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,about_activity.class);
                startActivity(intent);
            }
        });

        nickname=(TextView)findViewById(R.id.nickname);
        signature=(TextView)findViewById(R.id.signature);
        personalinfo=(TextView)findViewById(R.id.personalinfo) ;
        IntentFilter filter = new IntentFilter(settings_activity.action);
        registerReceiver(broadcastReceiver, filter);

        picture=(ImageView) findViewById(R.id.picture);
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                puWindow = new MPoPuWindow(MainActivity.this, MainActivity.this);
                puWindow.showPopupWindow(findViewById(R.id.activity_main));
                puWindow.setOnGetTypeClckListener(new MPoPuWindow.onGetTypeClckListener() {
                    @Override
                    public void getType(Type type) {
                        MainActivity.this.type = type;
                    }
                    @Override
                    public void getImgUri(Uri ImgUri, File file) {
                        MainActivity.this.ImgUri = ImgUri;
                        MainActivity.this.file = file;
                    }
                });
            }
        });

        //获取当前登录的用户信息
        intent=this.getIntent();
        userName_now=intent.getStringExtra("username");//当前用户名
        friendName=intent.getStringExtra("Addname");

        //根据当前用户获取其签名等信息，并初始化左界面
        new GetInfos(handler2).getInfos(userName_now);


        //左边应用列表
        outLogin=(Button)findViewById(localposition);
        settings=(Button)findViewById(R.id.settings);
        outLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstLocation=true;
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, settings_activity.class);
                intent.putExtra("userName",userName_now);
                startActivity(intent);
            }
        });

        introduce=(TextView)findViewById(R.id.introduce);
        about=(TextView)findViewById(R.id.about);

        //右边好友列表

        list1=(ListView) findViewById(R.id.right_listview);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String select=items1.get(i).get("str4");
                System.out.print("选择了"+items1.get(i).get("str4"));
               // items1.get(i);
                Intent intent =new Intent(MainActivity.this,WeixinChatDemoActivity.class);
                intent.putExtra("chatname",select);
                intent.putExtra("username_now",userName_now);
                startActivity(intent);
            }
        });


        addFriend=(Button)findViewById(R.id.addFriend);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,addFriendsActivity.class);
                intent.putExtra("userName",userName_now);//////////////////
                //MainActivity.this.finish();
                startActivity(intent);
            }
        });


        //百度地图
        mMapView =(MapView)findViewById(R.id.baiDuMv);
        baiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15f);
        baiduMap.setMapStatus(msu);

        locationClient = new LocationClient(this); //
        firstLocation =true;

        LocationClientOption option = new LocationClientOption(); //设置定位方式
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);// 设置定位间隔时间
        locationClient.setLocOption(option);




        //TODO
      /*  MyLocationOverlay myLocationOverlay = new MyLocationOverlay(mMapView);
        LocationData locData = new LocationData();
//手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要使用百度经纬度坐标（bd09ll）
        locData.latitude = 39.945;
        locData.longitude = 116.404;
        locData.direction = 2.0f;
        myLocationOverlay.setData(locData);
        mMapView.getOverlays().add(myLocationOverlay);
        mMapView.refresh();
        mMapView.getController().animateTo(new GeoPoint((int)(locData.latitude*1e6),
                (int)(locData.longitude* 1e6)));*/


        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {

                if (location == null || mMapView == null)
                    return;

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .direction(100).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                baiduMap.setMyLocationData(locData);

                double latitude=location.getLatitude();
                double longitude=location.getLongitude();

                //TODO
                GetFreinds gf=new GetFreinds(handler);// 从服务器获取朋友
                gf.getFreinds(userName_now);//刷新好友列表
                new SendLocToUser_Now().sendLocToUser_now(userName_now,latitude,longitude); // 调用函数将当前用户的位置发送到服务器上面
                //System.out.println("！！！经纬度："+location.getAddrStr()+location.getCity()+";"+location.getLatitude()+";"+location.getLongitude());
                if (firstLocation)
                {
                    firstLocation = false;
                    LatLng xy = new LatLng(location.getLatitude(),
                            location.getLongitude());
                    MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(xy);
                    baiduMap.animateMapStatus(status);
                }
            }
        });
        //TODO
       // initLocation();
        btn_loc=(Button) findViewById(R.id.btn_loc);
        btn_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstLocation = true;

            }
        });
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            nickname.setText(intent.getExtras().getString("data"));
            signature.setText(intent.getExtras().getString("data1"));
            personalinfo.setText(intent.getExtras().getString("data2"));
        }
    };


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", type + "");
        if (requestCode == 1) {
            if (ImgUri != null) {
                puWindow.onPhoto(ImgUri, 300, 300);
            }
        } else if (requestCode == 2) {
            if (data != null) {
                Uri uri = data.getData();
                puWindow.onPhoto(uri, 300, 300);
            }
        } else if (requestCode == 3) {
            if (type == Type.PHONE) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if(head!=null){
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);//保存在SD卡中
                        picture.setImageBitmap(head);//用ImageView显示出来
                    }
                }
            } else if (type == Type.CAMERA) {
                picture.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
            }
        }
    }
    private static String path="/sdcard/myHead/";
    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName =path + "head.jpg";//图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        baiduMap.setMyLocationEnabled(true);
        if (!locationClient.isStarted())
        {
            locationClient.start();
        }
    }

//TODO
    private void initLocation() {

        locationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setScanSpan(1000);
        option.setIsNeedLocationPoiList(true);
        locationClient.setLocOption(option);
    }

    @Override
    protected void onStop()
    {
        baiduMap.setMyLocationEnabled(false);
        locationClient.stop();
        super.onStop();
    }



    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
         // 从addFriendsActivity 转到Mainactivity
        super.onNewIntent(intent);
        setIntent(intent);
        getIntent();
        String addFriendName=intent.getStringExtra("addFriendName");
        String userName=intent.getStringExtra("userName");
        list1=(ListView) findViewById(R.id.right_listview);
        list1.invalidateViews();
    }
}