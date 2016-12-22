package com.example.lshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.Services.GetFriendLoc;

public class locshearActivity extends Activity {
    private MapView mMapView;
    private LocationClient locationClient;
    private BaiduMap baiduMap;
    private boolean firstLocation;
    private BitmapDescriptor mCurrentMarker;
    private String friendName;
    private  String userName;
    private TextView textView;

    //g:34.130677:108.839215
    final Handler handler=new Handler(){
        public void handleMessage(Message msg) {

            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    textView.setText(userName+"��"+friendName+"���ڹ���λ��...");
                    Bundle bundle=msg.getData();
                    bundle.getString("str");
                    String str=bundle.getString("str");
                    System.out.println("--------------{"+str);
                    String []strs=str.split(":");
                    double lat=Double.valueOf(strs[1]);
                    double lon=Double.valueOf(strs[2]);
                    //����Maker�����
                    LatLng point = new LatLng(lat, lon);
//����Markerͼ��
                    BitmapDescriptor bitmap = BitmapDescriptorFactory
                            .fromResource(R.drawable.qipo);
//����MarkerOption�������ڵ�ͼ�����Marker
                    OverlayOptions option2 = new MarkerOptions()
                            .position(point)
                            .icon(bitmap)
                            .title(friendName)
                            .perspective(true)
                            ;
//�ڵ�ͼ�����Marker������ʾ
                    baiduMap.addOverlay(option2);

            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());//��ʼ����ͼ
        setContentView(R.layout.activity_locshear);
        textView=(TextView)findViewById(R.id.textview) ;
        mMapView =(MapView)findViewById(R.id.lshearMap);
        baiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15f);
        baiduMap.setMapStatus(msu);
        Intent intent=this.getIntent();
        friendName=intent.getStringExtra("friendName");
        System.out.println("--2222-"+friendName);
        userName=intent.getStringExtra("name");

        locationClient = new LocationClient(this); //
        firstLocation =true;

        LocationClientOption option = new LocationClientOption(); //���ö�λ��ʽ
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);// ���ö�λ���ʱ��
        locationClient.setLocOption(option);



      /*  //����Maker�����
        LatLng point = new LatLng(34.13118, 108.839411);
//����Markerͼ��
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.delete);
//����MarkerOption�������ڵ�ͼ�����Marker
        OverlayOptions option2 = new MarkerOptions()
                .position(point)
                .icon(bitmap);
//�ڵ�ͼ�����Marker������ʾ
        baiduMap.addOverlay(option2);
        */
        //TODO
        /*GetFriendLoc gfl=new GetFriendLoc(handler);
        gfl.getFriendLoc(friendName);
*/
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

                GetFriendLoc gfl=new GetFriendLoc(handler);
                gfl.getFriendLoc(friendName);

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
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mMapView.onDestroy();
        mMapView = null;
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
}
