package com.moxiaosan.both.common.utils;

import android.graphics.Bitmap;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.InfoWindow.OnInfoWindowClickListener;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.baidu.mapapi.utils.DistanceUtil;
import com.moxiaosan.both.common.model.LocationBean;
import com.moxiaosan.both.common.model.MyPoiInfo;
import com.utils.log.LLog;

import java.util.ArrayList;
import java.util.List;

public class BaiduMapUtil {
    // poi搜索
    public static SuggestionSearch mSuggestionSearch = null;
    public static SuggestionsGetListener mSuggestionsGetListener = null;
    public static LocationClient mLocationClient = null;
    public static LocationClientOption option = null;
    public static LocateListener mLocateListener = null;
    public static MyLocationListenner mMyLocationListenner = null;
    public static int locateTime = 500;

    public interface SuggestionsGetListener {
        void onGetSucceed(List<MyPoiInfo> searchPoiList);

        void onGetFailed();
    }

    public interface LocateListener {
        void onLocateSucceed(LocationBean locationBean);

        void onLocateFiled();

        void onLocating();
    }

    /**
     * 通过view得到位图
     */
    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;
    }

    /**
     * 通过resource来展示Marker
     */
    public static Marker showMarkerByResource(double lat, double lon,
                                              int resource, BaiduMap mBaiduMap, int distance, boolean isMoveTo) {
        BitmapDescriptor bdView = BitmapDescriptorFactory
                .fromResource(resource);
        OverlayOptions ooView = new MarkerOptions()
                .position(new LatLng(lat, lon)).icon(bdView).zIndex(distance)
                .draggable(true);
        if (isMoveTo) {
            moveToTarget(lat, lon, mBaiduMap);
        }
        return (Marker) (mBaiduMap.addOverlay(ooView));
    }

    /**
     * 通过bitmap来展示Marker
     */
    public static Marker showMarkerByBitmap(double lat, double lon,
                                            Bitmap mBitmap, BaiduMap mBaiduMap, int distance, boolean isMoveTo) {
        BitmapDescriptor bdView = BitmapDescriptorFactory.fromBitmap(mBitmap);
        OverlayOptions ooView = new MarkerOptions()
                .position(new LatLng(lat, lon)).icon(bdView).zIndex(distance)
                .draggable(true);
        if (isMoveTo) {
            moveToTarget(lat, lon, mBaiduMap);
        }
        return (Marker) (mBaiduMap.addOverlay(ooView));
    }

    public static void updateMarkerIcon(Marker mMarker, int resourceId) {
        BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(resourceId);
        mMarker.setIcon(bd);
    }

    /**
     * 通过view来展示Marker
     */
    public static Marker showMarkerByView(double lat, double lon, View mView,
                                          BaiduMap mBaiduMap, int distance, boolean isMoveTo) {
        BitmapDescriptor bdView = BitmapDescriptorFactory.fromView(mView);
        OverlayOptions ooView = new MarkerOptions()
                .position(new LatLng(lat, lon)).icon(bdView).zIndex(distance)
                .draggable(true);
        if (isMoveTo) {
            moveToTarget(lat, lon, mBaiduMap);
        }
        return (Marker) (mBaiduMap.addOverlay(ooView));
    }

    /**
     * 通过bitmap来展示InfoWindow
     */
    public static InfoWindow showInfoWindowByBitmap(double lat, double lon,
                                                    Bitmap mBitmap, BaiduMap mBaiduMap, int distance, boolean isMoveTo,
                                                    OnInfoWindowClickListener listener) {
        InfoWindow mInfoWindow = new InfoWindow(mBitmap == null ? null
                : BitmapDescriptorFactory.fromBitmap(mBitmap), new LatLng(lat,
                lon), distance, listener);
        mBaiduMap.showInfoWindow(mInfoWindow);
        if (isMoveTo) {
            moveToTarget(lat, lon, mBaiduMap);
        }
        return mInfoWindow;
    }

    /**
     * 通过view来展示InfoWindow
     */
    public static InfoWindow showInfoWindowByView(double lat, double lon,
                                                  View mView, BaiduMap mBaiduMap, int distance, boolean isMoveTo,
                                                  OnInfoWindowClickListener listener) {
        InfoWindow mInfoWindow = new InfoWindow(
                BitmapDescriptorFactory.fromView(mView), new LatLng(lat, lon),
                distance, listener);
        mBaiduMap.showInfoWindow(mInfoWindow);
        if (isMoveTo) {
            moveToTarget(lat, lon, mBaiduMap);
        }
        return mInfoWindow;
    }

    /**
     * 移动到该点
     */
    public static void moveToTarget(double lat, double lon, BaiduMap mBaiduMap) {
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(
                lat, lon)));
    }

    /**
     * 移动到该点
     */
    public static void moveToTarget(LatLng mLatLng, BaiduMap mBaiduMap) {
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(mLatLng));
    }

    /**
     * 定位SDK监听函数
     */
    public static class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (mLocateListener != null) {
                mLocateListener.onLocating();
            }
            // map view 销毁后不在处理新接收的位置
            if (location == null || location.getProvince() == null
                    || location.getCity() == null || mLocateListener == null) {
                if (mLocateListener != null) {
                    mLocateListener.onLocateFiled();
                }
                if (locateTime < 1000) {
                    stopAndDestroyLocate();
                }
                return;
            }
            LocationBean mLocationBean = new LocationBean();
            mLocationBean.setProvince(location.getProvince());
            mLocationBean.setCity(location.getCity());
            mLocationBean.setDistrict(location.getDistrict());
            mLocationBean.setStreet(location.getStreet());
            mLocationBean.setLatitude(location.getLatitude());
            mLocationBean.setLongitude(location.getLongitude());
            mLocationBean.setTime(location.getTime());
            mLocationBean.setLocType(location.getLocType());
            mLocationBean.setRadius(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {
                mLocationBean.setSpeed(location.getSpeed());
                mLocationBean.setSatellite(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                mLocationBean.setLocName(location.getStreet());
            }
            if (mLocateListener != null) {
                mLocateListener.onLocateSucceed(mLocationBean);
            }
            stopAndDestroyLocate();
        }
    }

    /**
     * 停止及置空mLocationClient
     */
    public static void stopAndDestroyLocate() {
        locateTime = 500;
        if (mLocationClient != null) {
            if (mMyLocationListenner != null) {
                mLocationClient.unRegisterLocationListener(mMyLocationListenner);
            }
            mLocationClient.stop();
        }
        mMyLocationListenner = null;
        mLocateListener = null;
        mLocationClient = null;
        option = null;
    }

    /**
     * 获取距离String类型
     */
    public static String getDistanceWithUtil(double mLat1, double mLon1,
                                             double mLat2, double mLon2) {
        if ((Double) mLat1 instanceof Double
                && (Double) mLon1 instanceof Double
                && (Double) mLat2 instanceof Double
                && (Double) mLon2 instanceof Double && mLat1 != 0 && mLon1 != 0
                && mLat2 != 0 && mLon2 != 0) {
            float distance = (float) DistanceUtil.getDistance(new LatLng(mLat1,
                    mLon1), new LatLng(mLat2, mLon2));
            return addUnit(distance);
        } else {
            return "0M";
        }
    }

    /**
     * 获取距离，int类型的
     */
    public static int getDistanceWithoutUtil(double mLat1, double mLon1,
                                             double mLat2, double mLon2) {
        if ((Double) mLat1 instanceof Double
                && (Double) mLon1 instanceof Double
                && (Double) mLat2 instanceof Double
                && (Double) mLon2 instanceof Double && mLat1 != 0 && mLon1 != 0
                && mLat2 != 0 && mLon2 != 0) {
            return (int) DistanceUtil.getDistance(new LatLng(mLat1, mLon1),
                    new LatLng(mLat2, mLon2));
        } else {
            return 0;
        }
    }

    /**
     * 获取距离，String类型
     */
    public static String getDistanceWithUtil(String mLat1, String mLon1,
                                             String mLat2, String mLon2) {
        if (mLat1 != null && !mLat1.equals("") && !mLat1.equals("null")
                && !mLat1.equals("0") && mLon1 != null && !mLon1.equals("")
                && !mLon1.equals("null") && !mLon1.equals("0") && mLat2 != null
                && !mLat2.equals("") && !mLat2.equals("null")
                && !mLat2.equals("0") && mLon2 != null && !mLon2.equals("")
                && !mLon2.equals("null") && !mLon2.equals("0")) {
            float distance = (float) DistanceUtil.getDistance(
                    new LatLng(Double.valueOf(mLat1), Double.valueOf(mLon1)),
                    new LatLng(Double.valueOf(mLat2), Double.valueOf(mLon2)));
            return addUnit(distance);
        } else {
            return "0M";
        }
    }

    /**
     * 为距离添加单位
     */
    public static String addUnit(float distance) {
        if (distance == 0) {
            return "0M";
        } else {
            if (distance > 1000) {
                distance = distance / 1000;
                distance = (float) ((double) Math.round(distance * 100) / 100);
                return distance + "KM";
            } else {
                distance = (float) ((double) Math.round(distance * 100) / 100);
                return distance + "M";
            }
        }
    }

    /**
     *  通过web导航
     */
//	public static boolean NaviByWebBaidu(double mLat1, double mLon1,
//										 double mLat2, double mLon2, Context mContext) {
//		if ((Double) mLat1 instanceof Double
//				&& (Double) mLon1 instanceof Double
//				&& (Double) mLat2 instanceof Double
//				&& (Double) mLon2 instanceof Double && mLat1 != 0 && mLon1 != 0
//				&& mLat2 != 0 && mLon2 != 0) {
//			LatLng pt1 = new LatLng(mLat1, mLon1);
//			LatLng pt2 = new LatLng(mLat2, mLon2);
//			// 构建 导航参数
//			NaviPara para = new NaviPara();
//			para.startPoint = pt1;
//			para.endPoint = pt2;
//			BaiduMapNavigation.openWebBaiduMapNavi(para, mContext);
//			return true;
//		} else {
//			return false;
//		}
//	}

    /**
     * 通过web导航
     */
//	public static boolean NaviByWebBaidu(String mLat1, String mLon1,
//										 String mLat2, String mLon2, Context mContext) {
//		if (mLat1 != null && !mLat1.equals("") && !mLat1.equals("null")
//				&& !mLat1.equals("0") && mLon1 != null && !mLon1.equals("")
//				&& !mLon1.equals("null") && !mLon1.equals("0") && mLat2 != null
//				&& !mLat2.equals("") && !mLat2.equals("null")
//				&& !mLat2.equals("0") && mLon2 != null && !mLon2.equals("")
//				&& !mLon2.equals("null") && !mLon2.equals("0")) {
//			LatLng pt1 = new LatLng(Double.valueOf(mLat1),
//					Double.valueOf(mLon1));
//			LatLng pt2 = new LatLng(Double.valueOf(mLat2),
//					Double.valueOf(mLon2));
//			// 构建 导航参数
//			NaviPara para = new NaviPara();
//			para.startPoint = pt1;
//			para.endPoint = pt2;
//			BaiduMapNavigation.openWebBaiduMapNavi(para, mContext);
//			return true;
//		} else {
//			return false;
//		}
//	}

    /**
     *  通过百度客户端导航
     */
//	public static boolean NaviByBaidu(double mLat1, double mLon1, double mLat2,
//									  double mLon2, final Context mContext) {
//		if ((Double) mLat1 instanceof Double
//				&& (Double) mLon1 instanceof Double
//				&& (Double) mLat2 instanceof Double
//				&& (Double) mLon2 instanceof Double && mLat1 != 0 && mLon1 != 0
//				&& mLat2 != 0 && mLon2 != 0) {
//			LatLng pt1 = new LatLng(mLat1, mLon1);
//			LatLng pt2 = new LatLng(mLat2, mLon2);
//			// 构建 导航参数
//			NaviPara para = new NaviPara();
//			para.startPoint = pt1;
//			para.startName = "从这里开始";
//			para.endPoint = pt2;
//			para.endName = "到这里结束";
//			try {
//				BaiduMapNavigation.openBaiduMapNavi(para, mContext);
//			} catch (BaiduMapAppNotSupportNaviException e) {
//				e.printStackTrace();
//				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//				builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
//				builder.setTitle("提示");
//				builder.setPositiveButton("确认", new OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						BaiduMapNavigation.getLatestBaiduMapApp(mContext);
//					}
//				});
//				builder.setNegativeButton("取消", new OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//					}
//				});
//				builder.create().show();
//			}
//			return true;
//		} else {
//			return false;
//		}
//	}

    /**
     * 通过关键字及城市名搜索对应地区 city城市名-district区名-locName热点名
     */
    static String suggestionCifyName = null;

    public static void getSuggestion(String cityName, String keyName, SuggestionsGetListener listener) {
        mSuggestionsGetListener = listener;
        suggestionCifyName = cityName;
        if (cityName == null || keyName == null) {
            if (mSuggestionsGetListener != null) {
                mSuggestionsGetListener.onGetFailed();
            }
            destroySuggestion();
            return;
        }
        if (mSuggestionSearch == null) {
            // 初始化搜索模块，注册事件监听
            mSuggestionSearch = SuggestionSearch.newInstance();
        }
        mSuggestionSearch
                .setOnGetSuggestionResultListener(new MySuggestionListener());
        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
                .keyword(keyName.toString()).city(cityName));
    }

    /**
     * 关键字搜索监听回调
     */
    public static class MySuggestionListener implements
            OnGetSuggestionResultListener {

        @Override
        public void onGetSuggestionResult(SuggestionResult res) {
            if (res == null || res.getAllSuggestions() == null) {
                if (mSuggestionsGetListener != null) {
                    mSuggestionsGetListener.onGetFailed();
                }
                destroySuggestion();
                return;
            }
            List<MyPoiInfo> searchPoiList = new ArrayList<MyPoiInfo>();
            for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
                if (info.key != null && info.pt != null) {
                    MyPoiInfo myPoiInfo = new MyPoiInfo();
                    String address = "";
                    if (info.city != null) {
                        address = address + info.city;
                        myPoiInfo.setCity(info.city);
                    }else {
                        myPoiInfo.setCity(suggestionCifyName);
                    }
                    if (info.district != null) {
                        address = address + info.district;
                    }
                    myPoiInfo.setAddress(address + info.key);
                    myPoiInfo.setLatitude(info.pt.latitude);
                    myPoiInfo.setLongitude(info.pt.longitude);
                    searchPoiList.add(myPoiInfo);
                }
            }
            if (mSuggestionsGetListener != null) {
                mSuggestionsGetListener.onGetSucceed(searchPoiList);
            }
            destroySuggestion();
        }
    }

    /**
     * 销毁及置空搜索相关对象
     */
    public static void destroySuggestion() {
        if (mSuggestionSearch != null) {
            mSuggestionSearch.destroy();
            mSuggestionSearch = null;
        }
        mSuggestionsGetListener = null;
    }

    public static GeoCoder mGeoCoder = null;
    public static GeoCodeListener mGeoCodeListener = null;

    public interface GeoCodeListener {
        void onGetSucceed(LocationBean locationBean);

        void onGetFailed();
    }

    /**
     * 获取坐标通过geo搜索
     */
    public static void getLocationByGeoCode(LocationBean mLocationBean,
                                            GeoCodeListener listener) {
        mGeoCodeListener = listener;
        if (mLocationBean == null || mLocationBean.getCity() == null
                || mLocationBean.getLocName() == null) {
            if (mGeoCodeListener != null) {
                mGeoCodeListener.onGetFailed();
            }
            destroyGeoCode();
            return;
        }
        if (mGeoCoder == null) {
            mGeoCoder = GeoCoder.newInstance();
        }
        mGeoCoder.setOnGetGeoCodeResultListener(new MyGeoCodeListener());
        // Geo搜索
        mGeoCoder.geocode(new GeoCodeOption().city(mLocationBean.getCity())
                .address(mLocationBean.getLocName()));
    }

    public static GeoCodePoiListener mGeoCodePoiListener = null;

    public interface GeoCodePoiListener {
        void onGetSucceed(LocationBean locationBean, List<MyPoiInfo> poiList);

        void onGetFailed();
    }

    /**
     * 根据经纬度获取周边热点名
     */
    public static void getPoisByGeoCode(double lat, double lon,
                                        GeoCodePoiListener listener) {
        mGeoCodePoiListener = listener;
        if (mGeoCoder == null) {
            mGeoCoder = GeoCoder.newInstance();
        }
        mGeoCoder.setOnGetGeoCodeResultListener(new MyGeoCodeListener());
        // 反Geo搜索
        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(new LatLng(lat, lon)));
    }

    /**
     * geo搜索的回调
     */
    public static class MyGeoCodeListener implements
            OnGetGeoCoderResultListener {

        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                if (mGeoCodeListener != null) {
                    mGeoCodeListener.onGetFailed();
                }
                if (mGeoCodePoiListener != null) {
                    mGeoCodePoiListener.onGetFailed();
                }
                destroyGeoCode();
                return;
            }
            // 反Geo搜索
            mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(result
                    .getLocation()));
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            try {
                LocationBean mLocationBean = new LocationBean();
                mLocationBean.setProvince(result.getAddressDetail().province);

                mLocationBean.setCity(result.getAddressDetail().city);
                mLocationBean.setDistrict(result.getAddressDetail().district);
                mLocationBean.setLocName(result.getAddressDetail().street);
                mLocationBean.setStreet(result.getAddressDetail().street);
                mLocationBean.setStreetNum(result.getAddressDetail().streetNumber);
                mLocationBean.setLatitude(result.getLocation().latitude);
                mLocationBean.setLongitude(result.getLocation().longitude);
                if (mGeoCodeListener != null) {
                    mGeoCodeListener.onGetSucceed(mLocationBean);
                }
                if (mGeoCodePoiListener != null) {
                    if (result.getPoiList() != null) {
                        List<MyPoiInfo> myPoiInfoList = new ArrayList<MyPoiInfo>();
                        for (PoiInfo poiInfo : result.getPoiList()) {
                            MyPoiInfo myPoiInfo = new MyPoiInfo();
                            myPoiInfo.setAddress(poiInfo.address);
                            myPoiInfo.setLatitude(poiInfo.location.latitude);
                            myPoiInfo.setLongitude(poiInfo.location.longitude);
                            myPoiInfo.setCity(poiInfo.city);
                            myPoiInfoList.add(myPoiInfo);
                        }
                        mGeoCodePoiListener.onGetSucceed(mLocationBean, myPoiInfoList);
                    }
                }
                destroyGeoCode();
            } catch (Exception e) {
                if (mGeoCodeListener != null) {
                    mGeoCodeListener.onGetFailed();
                }
                if (mGeoCodePoiListener != null) {
                    mGeoCodePoiListener.onGetFailed();
                }
                LLog.e(e);
            }
        }
    }

    /**
     * 销毁及置空geo搜索相关对象
     */
    public static void destroyGeoCode() {
        if (mGeoCoder != null) {
            mGeoCoder.destroy();
            mGeoCoder = null;
        }
        mGeoCodeListener = null;
        mGeoCodePoiListener = null;
    }

    public static PoiSearch mPoiSearch = null;
    public static PoiSearchListener mPoiSearchListener = null;

    public interface PoiSearchListener {
        void onGetSucceed(List<LocationBean> locationList, PoiResult res);

        void onGetFailed();
    }

    /**
     * 通过关键字及城市名搜索对应点区，
     * city城市名-addstr地址-locName热点名-latlon经纬度-uid百度自定义id
     */
    public static void getPoiByPoiSearch(String cityName, String keyName,
                                         int pageNum, PoiSearchListener listener) {
        mPoiSearchListener = listener;
        if (cityName == null || keyName == null) {
            if (mPoiSearchListener != null) {
                mPoiSearchListener.onGetFailed();
            }
            destroyPoiSearch();
            return;
        }
        if (mPoiSearch == null) {
            mPoiSearch = PoiSearch.newInstance();
        }
        mPoiSearch.setOnGetPoiSearchResultListener(new MyPoiSearchListener());
        mPoiSearch.searchInCity((new PoiCitySearchOption()).city(cityName)
                .keyword(keyName).pageNum(pageNum));
    }

    public static PoiDetailSearchListener mPoiDetailSearchListener = null;

    public interface PoiDetailSearchListener {
        void onGetSucceed(LocationBean locationBean);

        void onGetFailed();
    }

    /**
     * 获得所谓的详细信息
     */
    public static void getPoiDetailByPoiSearch(String uid,
                                               PoiDetailSearchListener listener) {
        mPoiDetailSearchListener = listener;
        if (mPoiSearch == null) {
            mPoiSearch = PoiSearch.newInstance();
        }
        mPoiSearch.setOnGetPoiSearchResultListener(new MyPoiSearchListener());
        mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(uid));
    }

    /**
     * poisearch搜索的回调
     */
    public static class MyPoiSearchListener implements
            OnGetPoiSearchResultListener {

        @Override
        public void onGetPoiDetailResult(PoiDetailResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                if (mPoiDetailSearchListener != null) {
                    mPoiDetailSearchListener.onGetFailed();
                }
                destroyPoiSearch();
                return;
            }
            LocationBean mLocationBean = new LocationBean();
            mLocationBean.setLocName(result.getName());
            mLocationBean.setAddStr(result.getAddress());
            mLocationBean.setLatitude(result.getLocation().latitude);
            mLocationBean.setLongitude(result.getLocation().longitude);
            mLocationBean.setUid(result.getUid());
            if (mPoiDetailSearchListener != null) {
                mPoiDetailSearchListener.onGetSucceed(mLocationBean);
            }
            destroyPoiSearch();
        }

        @Override
        public void onGetPoiResult(PoiResult res) {
            if (res == null
                    || res.error == SearchResult.ERRORNO.RESULT_NOT_FOUND
                    || res.getAllPoi() == null) {
                if (mPoiSearchListener != null) {
                    mPoiSearchListener.onGetFailed();
                }
                destroyPoiSearch();
                return;
            }
            List<LocationBean> searchPoiList = new ArrayList<LocationBean>();
            if (res.getAllPoi() != null) {
                for (PoiInfo info : res.getAllPoi()) {
                    LocationBean cityPoi = new LocationBean();
                    cityPoi.setAddStr(info.address);
                    cityPoi.setCity(info.city);
                    if (info.location != null) {
                        cityPoi.setLatitude(info.location.latitude);
                        cityPoi.setLongitude(info.location.longitude);
//						Log.i("huan", "lat==" + info.location.latitude + "--lon=="
//								+ info.location.longitude + "--热点名==" + info.name);
                    }
                    cityPoi.setUid(info.uid);
                    cityPoi.setLocName(info.name);
                    searchPoiList.add(cityPoi);
                }
            }
            if (mPoiSearchListener != null) {
                mPoiSearchListener.onGetSucceed(searchPoiList, res);
            }
            destroyPoiSearch();
        }
    }

    /**
     * 销毁及置空poisearch搜索相关对象
     */
    public static void destroyPoiSearch() {
        if (mPoiSearch != null) {
            mPoiSearch.destroy();
            mPoiSearch = null;
        }
        mPoiSearchListener = null;
        mPoiDetailSearchListener = null;
    }

}
