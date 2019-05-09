package com.luoruiyong.caa.location;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.TranslateAnimation;
import com.google.gson.Gson;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.model.gson.GsonFactory;
import com.luoruiyong.caa.utils.DisplayUtils;
import com.luoruiyong.caa.utils.ListUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: luoruiyong
 * Date: 2019/4/8/008
 * Description:
 **/
public class LocationActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "LocationActivity";

    public static final String KEY_LOCATION_INFO = "key_location_info";

    private static final int MAX_POI_COUNT = 20;

    private ImageView mBackIv;
    private TextView mTitleTv;
    private TextView mNoLocationDataTv;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    private MapView mMapView;
    private AMap mAMap;
    private Marker mMarker;

    private OkHttpClient mClint;
    private List<SimplePoiData> mList;
    private ListAdapter mAdapter;

    private final static String ADDRESS_QUERY_URL = "https://restapi.amap.com/v3/geocode/regeo";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        initView();

        initAMap(savedInstanceState);

        location();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mNoLocationDataTv = findViewById(R.id.tv_no_location_data);
        mMapView = findViewById(R.id.map_view);
        mRecyclerView = findViewById(R.id.rv_recycler_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mBackIv.setOnClickListener(this);
        mBackIv.setImageResource(R.drawable.ic_clear_white);
        mTitleTv.setText(R.string.title_choose_location);
        mList = new ArrayList<>();
        mAdapter = new ListAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initAMap(Bundle savedInstanceState) {
        mMapView.onCreate(savedInstanceState);
        mAMap = mMapView.getMap();
        UiSettings uiSettings = mAMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setScaleControlsEnabled(true);
        uiSettings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        mAMap.setMyLocationEnabled(true);
        mAMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                startJumpAnimation();
                requestAddressDescription(cameraPosition.target.longitude, cameraPosition.target.latitude);
            }
        });
        mAMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                addMarkerInCenter();
            }
        });
    }

    private void addMarkerInCenter() {
        LatLng latLng = mAMap.getCameraPosition().target;
        Point point = mAMap.getProjection().toScreenLocation(latLng);
        mMarker = mAMap.addMarker(new MarkerOptions()
                .position(latLng)
                .anchor(0.5f, 0.5f)
                .zIndex(1)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_purple_pin)));
        mMarker.setPositionByPixels(point.x, point.y);
    }

    /**
     * 屏幕中心marker 跳动
     */
    public void startJumpAnimation(){
        if (mMarker != null ) {
            final LatLng latLng = mMarker.getPosition();
            Point point =  mAMap.getProjection().toScreenLocation(latLng);
            point.y -= DisplayUtils.dp2px(60);
            LatLng target = mAMap.getProjection().fromScreenLocation(point);
            Animation animation = new TranslateAnimation(target);
            animation.setInterpolator(new Interpolator() {
                @Override
                public float getInterpolation(float input) {
                    // 模拟重加速度的interpolator
                    if(input <= 0.5) {
                        return (float) (0.5f - 2 * (0.5 - input) * (0.5 - input));
                    } else {
                        return (float) (0.5f - Math.sqrt((input - 0.5f)*(1.5f - input)));
                    }
                }
            });
            animation.setDuration(500);
            mMarker.setAnimation(animation);
            mMarker.startAnimation();
        }
    }

    private void location() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.showMyLocation(true);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_circle));
        mAMap.setMyLocationStyle(myLocationStyle);
    }

    private void requestAddressDescription( double longitude, double latitude)  {
        if (mClint == null) {
            mClint = new OkHttpClient();
        }
        mList.clear();
        mAdapter.notifyDataSetChanged();
        showLoadingView();
        StringBuilder locationBuilder = new StringBuilder();
        locationBuilder.append(String.format("%.6f", longitude));
        locationBuilder.append(",");
        locationBuilder.append(String.format("%.6f", latitude));
        HttpUrl httpUrl = HttpUrl.parse(ADDRESS_QUERY_URL).newBuilder()
                .addQueryParameter("key", Enviroment.AMAP_WEB_API_KEY)
                .addQueryParameter("location", locationBuilder.toString())
                .addQueryParameter("radius", String.valueOf(3000))
                .addQueryParameter("extensions", "all")
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .build();
        mClint.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                hideProgressBar();
                showNoLocationDataTip();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String data = response.body().string();
                    Gson gson = GsonFactory.buildGson();
                    AMapLocationBean location = gson.fromJson(data, AMapLocationBean.class);
                    if (location.getStatus() == 1) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                handleLocationData(location);
                            }
                        });
                    } else {
                        showNoLocationDataTip();
                    }
                }
            }
        });
    }

    private void handleLocationData(AMapLocationBean location) {
        List<AMapLocationBean.Regeocode.Poi> list = location.getRegeocode().getPois();
        String province = location.getRegeocode().getAddressComponent().getProvince();
        String city = location.getRegeocode().getAddressComponent().getCity();
        String district = location.getRegeocode().getAddressComponent().getDistrict();
        int count = ListUtils.getSize(list);
        if (count == 0) {
            showNoLocationDataTip();
            return;
        }
        hideProgressBar();
        for (int i = 0; i < count && i < MAX_POI_COUNT; i++) {
            SimplePoiData data = new SimplePoiData();
            data.setProvince(province);
            data.setCity(city);
            data.setDistrict(district);
            data.setName(list.get(i).getName());
            data.setAddress(list.get(i).getAddress());
            mList.add(data);
        }
        mAdapter.notifyDataSetChanged();
    }

    @MainThread
    private void hideProgressBar() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    protected void showLoadingView() {
        mProgressBar.setVisibility(View.VISIBLE);
        mNoLocationDataTv.setVisibility(View.GONE);
    }

    private void showNoLocationDataTip() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                mNoLocationDataTv.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setResultDataAndFinish(SimplePoiData data) {
        Intent intent = new Intent();
        String locationInfo = null;
        if (data != null) {
            StringBuilder builder = new StringBuilder();
            builder.append(TextUtils.isEmpty(data.getCity()) ? data.getProvince() : data.getCity());
            builder.append("·");
            builder.append(data.getName());
            locationInfo = builder.toString();
        }
        intent.putExtra(KEY_LOCATION_INFO, locationInfo);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

        private final int TYPE_NO_APPEAR_LOCATION = 0;
        private final int TYPE_POI = 1;

        private List<SimplePoiData> mList;

        public ListAdapter(List<SimplePoiData> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            RecyclerView.ViewHolder holder = null;
            switch (viewType) {
                case TYPE_NO_APPEAR_LOCATION:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_no_poi, parent, false);
                    holder = new NoLocationViewHolder(view);
                    break;
                case TYPE_POI:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poi_list, parent, false);
                    holder = new PoiViewHolder(view);
                    break;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof NoLocationViewHolder) {
                holder.itemView.setOnClickListener(this);
            } else if (holder instanceof PoiViewHolder) {
                PoiViewHolder viewHolder = (PoiViewHolder) holder;
                int realPosition = position - 1;
                SimplePoiData data = mList.get(realPosition);
                viewHolder.bindData(data);
                viewHolder.itemView.setOnClickListener(this);
                viewHolder.mChooseTv.setOnClickListener(this);
                viewHolder.itemView.setTag(realPosition);
                viewHolder.mChooseTv.setTag(realPosition);
            }
        }

        @Override
        public int getItemCount() {
            return ListUtils.getSize(mList) + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_NO_APPEAR_LOCATION;
            }
            return TYPE_POI;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_poi_item_layout:
                case R.id.tv_choose:
                    SimplePoiData data = mList.get((Integer) v.getTag());
                    setResultDataAndFinish(data);
                case R.id.ll_no_poi_item_layout:
                    setResultDataAndFinish(null);
                    break;
            }
        }

        class PoiViewHolder extends RecyclerView.ViewHolder {
            private TextView mPoiNameTv;
            private TextView mPoiAddressTv;
            private TextView mChooseTv;

            public PoiViewHolder(View itemView) {
                super(itemView);
                mPoiNameTv = itemView.findViewById(R.id.tv_poi_name);
                mPoiAddressTv = itemView.findViewById(R.id.tv_poi_address);
                mChooseTv = itemView.findViewById(R.id.tv_choose);
            }

            public void bindData(SimplePoiData data) {
                mPoiNameTv.setText(data.getName());
                String address = data.getAddress();
                String province = data.getProvince();
                String city = data.getCity();
                String district = data.getDistrict();
                if (TextUtils.isEmpty(address) || TextUtils.equals(city, address) || TextUtils.equals(district, address)) {
                    address = province + city + district;
                } else {
                    address = province + city + district + address;
                }
                mPoiAddressTv.setText(address);
            }
        }

        class NoLocationViewHolder extends RecyclerView.ViewHolder{

            public NoLocationViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
