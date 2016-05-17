package com.moxiaosan.both.mqtt;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.ibm.mqtt.IMqttClient;
import com.ibm.mqtt.MqttClient;
import com.ibm.mqtt.MqttException;
import com.ibm.mqtt.MqttPersistence;
import com.ibm.mqtt.MqttPersistenceException;
import com.ibm.mqtt.MqttSimpleCallback;
import com.moxiaosan.both.R;
import com.moxiaosan.both.carowner.ui.activity.BusinessMainActivity;
import com.moxiaosan.both.carowner.ui.activity.HitchhikingActivity;
import com.moxiaosan.both.carowner.ui.activity.ReceiveOrderActivity;
import com.moxiaosan.both.common.ui.activity.MessagesActivity;
import com.moxiaosan.both.common.ui.activity.MyWalletActivity;
import com.moxiaosan.both.consumer.ui.activity.GateOrderDetailActivity;
import com.moxiaosan.both.consumer.ui.activity.GateToGateActivity;
import com.moxiaosan.both.consumer.ui.activity.MyOrdersActivity;
import com.moxiaosan.both.consumer.ui.activity.ShunFengCheActivity;
import com.moxiaosan.both.consumer.ui.activity.ShunFengOrderDetailActivity;
import com.utils.common.AppData;
import com.utils.log.LLog;
import com.utils.ui.base.ActivityHolder;

import java.util.ArrayList;
import java.util.List;

import consumer.model.mqttobj.MQArrivaltime;
import consumer.model.mqttobj.MQCancelOrder;
import consumer.model.mqttobj.MQComment;
import consumer.model.mqttobj.MQDelivery;
import consumer.model.mqttobj.MQNewOrderNotify;
import consumer.model.mqttobj.MQNewsNotify;
import consumer.model.mqttobj.MQNotifyOwner;
import consumer.model.mqttobj.MQOrdernotify;
import consumer.model.mqttobj.MQPayment;
import consumer.model.mqttobj.MQPickup;
import consumer.model.mqttobj.MQRquestBecomeCarerStatus;
import consumer.model.obj.RespUserInfo;
import consumer.model.obj.RespUserOrder;

/**
 *
 */
public class MqttService extends Service {
    // this is the log tag
    public static final String TAG = "MoXiaoSanMqttService";
    // the IP address, where your MQTT broker is running.
    private static final String MQTT_HOST = "219.235.15.66";
    //    private static final String MQTT_HOST = "192.168.199.161";
    // the port at which the broker is running.
    private static int MQTT_BROKER_PORT_NUM = 1883;
    // Let's not use the MQTT persistence.
    private static MqttPersistence MQTT_PERSISTENCE = null;
    // We don't need to remember any state between the connections, so we use a
    // clean start.
    private static boolean MQTT_CLEAN_START = true;
    // Let's set the internal keep alive for MQTT to 15 mins. I haven't tested
    // this value much. It could probably be increased.
    private static short MQTT_KEEP_ALIVE = 60 * 15;
    // Set quality of services to 0 (at most once delivery), since we don't want
    // push notifications
    // arrive more than once. However, this means that some messages might get
    // lost (delivery is not guaranteed)
//    private static int[] MQTT_QUALITIES_OF_SERVICE = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static int MQTT_QUALITY_OF_SERVICE = 0;
    // The broker should not retain any messages.
    private static boolean MQTT_RETAINED_PUBLISH = false;

    // MQTT client ID, which is given the broker. In this example, I also use
    // this for the topic header.
    // You can use this to run push notifications for multiple apps with one
    // MQTT broker.
    public static String MQTT_CLIENT_ID = "moxiaosan";

    // These are the actions for the service (name are descriptive enough)
//    private static final String ACTION_START = MQTT_CLIENT_ID + ".START";
//    private static final String ACTION_STOP = MQTT_CLIENT_ID + ".STOP";
    private static final String ACTION_KEEPALIVE = MQTT_CLIENT_ID + ".KEEP_ALIVE";
    private static final String ACTION_RECONNECT = MQTT_CLIENT_ID + ".RECONNECT";

    // Connectivity manager to determining, when the phone loses connection
    private ConnectivityManager mConnMan;
    // Notification manager to displaying arrived push notifications
    private NotificationManager mNotifMan;

    // Whether or not the service has been started.
    private boolean mStarted;

    // This the application level keep-alive interval, that is used by the
    // AlarmManager
    // to keep the connection active, even when the device goes to sleep.
    private static final long KEEP_ALIVE_INTERVAL = 1000 * 60 * 28;

    // Retry intervals, when the connection is lost.
    private static final long INITIAL_RETRY_INTERVAL = 1000 * 10;
    private static final long MAXIMUM_RETRY_INTERVAL = 1000 * 60 * 30;

    // Preferences instance
    private static SharedPreferences mPrefs;
    // We store in the preferences, whether or not the service has been started
    public static final String PREF_STARTED = "isStarted";
    // We also store the deviceID (target)
    public static final String PREF_DEVICE_ID = "deviceID";
    // We store the last retry interval
    public static final String PREF_RETRY = "retryInterval";

    // Notification id
    private static int NOTIF_CONNECTED = 0;

    // This is the instance of an MQTT connection.
    private static MQTTConnection mConnection;
    IMqttClient mqttClient = null;
    private long mStartTime;

    private Gson gson = new Gson();
    private SimpleBinder simpleBinder = null;

    public MqttService() {
    }


    @Override
    public void onCreate() {
        simpleBinder = new SimpleBinder();
        LLog.i("===MqttService=====onCreate()");
        super.onCreate();
        mStartTime = System.currentTimeMillis();

        // Get instances of preferences, connectivity manager and notification
        // manager
        mPrefs = getSharedPreferences(TAG, MODE_PRIVATE);
        mConnMan = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mNotifMan = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
         /*
         * If our process was reaped by the system for any reason we need to
		 * restore our state with merely a call to onCreate. We record the last
		 * "started" value and restore it here if necessary.
		 */
        handleCrashedService();
    }


    // This method does any necessary clean-up need in case the server has been
    // destroyed by the system
    // and then restarted
    private void handleCrashedService() {
        if (wasStarted() == true) {
            LLog.i("Handling crashed service...");
            // stop the keep alives
            stopKeepAlives();
        }
        // Do a clean start
        start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        LLog.i("===MqttService=====onBind()");
        return simpleBinder;
    }

    public class SimpleBinder extends Binder {
        public MqttService getService() {
            return MqttService.this;
        }

        //自定义方法

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LLog.i("Service destroyed (started=" + mStarted + ")");
        // Stop the services, if it has been started
        if (mStarted == true) {
            stop();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getAction() != null) {
                LLog.i("==onStartCommand()==Service started with intent=" + intent + "==intent.getAction()===" + intent.getAction());
                // Do an appropriate action based on the intent.
                if (intent.getAction().equals(ACTION_KEEPALIVE) == true) {
                    keepAlive();
                } else if (intent.getAction().equals(ACTION_RECONNECT) == true) {
                    if (isNetworkAvailable()) {
                        reconnectIfNecessary();
                    }
                }
            }
        }
        return START_STICKY;  //如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。  START_REDELIVER_INTENT：重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
    }

    // Reads whether or not the service has been started from the preferences
    public static boolean wasStarted() {
        return mPrefs.getBoolean(PREF_STARTED, false);
    }

    // Sets whether or not the services has been started in the preferences.
    public void setStarted(boolean started) {
        mPrefs.edit().putBoolean(PREF_STARTED, started).commit();
        mStarted = started;
    }

    private synchronized void start() {
        LLog.i("Starting service...");
        // Do nothing, if the service is already running.
        if (mStarted == true) {
            Log.w(TAG, "Attempt to start connection that is already active");
            return;
        }
        // Establish an MQTT connection
        new ConnectionTask().execute();
        // Register a connectivity listener
        registerReceiver(mConnectivityChanged, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private synchronized void stop() {
        // Do nothing, if the service is not running.
        if (mStarted == false) {
            Log.w(TAG, "Attempt to stop connection not active.");
            return;
        }

        // Save stopped state in the preferences
        setStarted(false);

        // Remove the connectivity receiver
        unregisterReceiver(mConnectivityChanged);
        // Any existing reconnect timers should be removed, since we explicitly
        // stopping the service.
        cancelReconnect();
        // Destroy the MQTT connection if there is one
        if (mConnection != null) {
            mConnection.disconnect();
            mConnection = null;
        }
    }


    class ConnectionTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            connect();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

    //
    private synchronized void connect() {
        LLog.i("Connecting...");
        // fetch the device ID from the preferences.
        final String deviceID = mPrefs.getString(PREF_DEVICE_ID, null);
        // Create a new connection only if the device id is not NULL
        if (deviceID == null) {
            LLog.i("Device ID not found.");
        } else {
            try {
                mConnection = new MQTTConnection(MQTT_HOST, deviceID);
            } catch (Exception e) {
                e.printStackTrace();
                // Schedule a reconnect, if we failed to connect
                LLog.i("MqttException: " + (e.getMessage() != null ? e.getMessage() : "NULL"));
                if (isNetworkAvailable()) {
                    scheduleReconnect(mStartTime);
                }
            }
            setStarted(true);
        }
    }

    private synchronized void keepAlive() {
        try {
            // Send a keep alive, if there is a connection.
            if (mStarted == true && mConnection != null) {
                mConnection.sendKeepAlive();
            }
        } catch (MqttException e) {
            LLog.i("MqttException: " + (e.getMessage() != null ? e.getMessage() : "NULL"));
            mConnection.disconnect();
            mConnection = null;
            cancelReconnect();
        }
    }

    // Schedule application level keep-alives using the AlarmManager
    private void startKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, MqttService.class);
        i.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + KEEP_ALIVE_INTERVAL, KEEP_ALIVE_INTERVAL, pi);
    }

    // Remove all scheduled keep alives
    private void stopKeepAlives() {
        Intent i = new Intent();
        i.setClass(this, MqttService.class);
        i.setAction(ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.cancel(pi);
    }

    // We schedule a reconnect based on the starttime of the service
    public void scheduleReconnect(long startTime) {
        // the last keep-alive interval
        long interval = mPrefs.getLong(PREF_RETRY, INITIAL_RETRY_INTERVAL);

        // Calculate the elapsed time since the start
        long now = System.currentTimeMillis();
        long elapsed = now - startTime;

        // Set an appropriate interval based on the elapsed time since start
        if (elapsed < interval) {
            interval = Math.min(interval * 4, MAXIMUM_RETRY_INTERVAL);
        } else {
            interval = INITIAL_RETRY_INTERVAL;
        }

        LLog.i("Rescheduling connection in " + interval + "ms.");

        // Save the new internval
        mPrefs.edit().putLong(PREF_RETRY, interval).commit();

        // Schedule a reconnect using the alarm manager.
        Intent i = new Intent();
        i.setClass(this, MqttService.class);
        i.setAction(ACTION_RECONNECT);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC_WAKEUP, now + interval, pi);
    }

    // Remove the scheduled reconnect
    public void cancelReconnect() {
        Intent i = new Intent();
        i.setClass(this, MqttService.class);
        i.setAction(ACTION_RECONNECT);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.cancel(pi);
    }

    private synchronized void reconnectIfNecessary() {
        if (mStarted == true && mConnection == null) {
            LLog.i("Reconnecting...");
            new ConnectionTask().execute();
//            connect();
        }
    }

    // This receiver listeners for network changes and updates the MQTT
    // connection
    // accordingly
    private BroadcastReceiver mConnectivityChanged = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get network info
            NetworkInfo info = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

            // Is there connectivity?
            boolean hasConnectivity = (info != null && info.isConnected()) ? true : false;

            LLog.i("Connectivity changed: connected=" + hasConnectivity);

            if (hasConnectivity) {
                reconnectIfNecessary();
            } else if (mConnection != null) {
                // if there no connectivity, make sure MQTT connection is
                // destroyed
                mConnection.disconnect();
                cancelReconnect();
                mConnection = null;
            }
        }
    };

    // isplay the topbar notification
    private void showNotification(String message, PendingIntent pi) {
        Notification n = new Notification();

        n.flags |= Notification.FLAG_SHOW_LIGHTS;
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        n.defaults = Notification.DEFAULT_ALL;

        n.icon = R.mipmap.ic_launcher;
        n.when = System.currentTimeMillis();
        // Simply open the parent activity
        //PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, PushActivity.class), 0);

        // Change the name of the notification here
        n.setLatestEventInfo(this, getResources().getString(R.string.app_name), message, pi);
        mNotifMan.notify(NOTIF_CONNECTED++, n);
    }

    // Check if we are online
    private boolean isNetworkAvailable() {
        NetworkInfo info = mConnMan.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isConnected();
    }

    // This inner class is a wrapper on top of MQTT client.
    public class MQTTConnection implements MqttSimpleCallback {

        // Creates a new connection given the broker address and initial topic
        public MQTTConnection(String brokerHostName, String deviceIdTopic) throws MqttException {
            // Create connection spec
            String mqttConnSpec = "tcp://" + brokerHostName + "@" + MQTT_BROKER_PORT_NUM;
            // Create the client and connect
            mqttClient = MqttClient.createMqttClient(mqttConnSpec, MQTT_PERSISTENCE);
            mqttClient.connect(deviceIdTopic, MQTT_CLEAN_START, MQTT_KEEP_ALIVE);

            // register this client app has being able to receive messages
            mqttClient.registerSimpleHandler(this);

            // Subscribe to an initial topic, which is combination of client ID
            // and device ID.
            List<String> topicList = new ArrayList<String>();
            topicList.add("moxsan/arrivaltime/" + AppData.getInstance().getUserEntity().getUsername());//1、	预计司机到达时间（用户端）
            topicList.add("moxsan/notifyowner/" + AppData.getInstance().getUserEntity().getUsername());//   2、	已通知多少位车主（用户端）
            topicList.add("moxsan/ordernotify/" + AppData.getInstance().getUserEntity().getUsername());//3、	有司机接单（用户端）
            topicList.add("moxsan/pickup/" + AppData.getInstance().getUserEntity().getUsername());//4、	已取货/接人（用户端）
            topicList.add("moxsan/delivery/" + AppData.getInstance().getUserEntity().getUsername());//5、	已送达（用户端）
            topicList.add("moxsan/cancelorder/" + AppData.getInstance().getUserEntity().getUsername());//	6、	取消订单（车主端）
            topicList.add("moxsan/payment/" + AppData.getInstance().getUserEntity().getUsername());//7、	已付款（车主端）
            topicList.add("moxsan/comment/" + AppData.getInstance().getUserEntity().getUsername()); //8、	收到评价（车主端）
            topicList.add("moxsan/newsnotify/" + AppData.getInstance().getUserEntity().getUsername());//9、	收到消息（用户、车主端）
            topicList.add("moxsan/newordernotify/" + AppData.getInstance().getUserEntity().getUsername());//10、	新订单通知车主（车主端）
            topicList.add("moxsan/setowner/" + AppData.getInstance().getUserEntity().getUsername());//11、	成为运营车主（车主端）
            topicList.add("moxsan/arm/" + AppData.getInstance().getUserEntity().getIMEI());//12、	设置防盗模式
            topicList.add("moxsan/mmlieage/" + AppData.getInstance().getUserEntity().getIMEI()); //13、	设置保养里程
            topicList.add("moxsan/sos/" + AppData.getInstance().getUserEntity().getIMEI());  //14、	设置报警电话
            topicList.add("moxsan/cut/" + AppData.getInstance().getUserEntity().getIMEI());  //15、	丢失找回
            topicList.add("moxsan/vbsen/" + AppData.getInstance().getUserEntity().getIMEI());  //16、	设置灵敏度
            topicList.add("moxsan/circle/" + AppData.getInstance().getUserEntity().getIMEI());  //17、	电子围栏
            topicList.add("moxsan/power/" + AppData.getInstance().getUserEntity().getIMEI());  //18、	取电
            topicList.add("moxsan/factory/" + AppData.getInstance().getUserEntity().getIMEI());  //19、	回复出厂设置
            String[] topics = topicList.toArray(new String[topicList.size()]);
            subscribeToTopic(topics);

            LLog.i("Connection established to " + brokerHostName + " on deviceIdTopic== " + deviceIdTopic);
            // Save start time
            mStartTime = System.currentTimeMillis();
            // Star the keep-alives  即是心跳包
            startKeepAlives();
        }

        /*
         * Called when we receive a message from the message broker.
         */
        @Override
        public void publishArrived(String topicName, byte[] payload, int qos,
                                   boolean retained) {
            String s = new String(payload);
            Intent intent = new Intent();
            LLog.i("Got message: " + "===topicName====" + topicName + "==content===" + s);
            if (topicName.equals("moxsan/arrivaltime/" + AppData.getInstance().getUserEntity().getUsername())) { //1
                MQArrivaltime mqArrivaltime = gson.fromJson(s, MQArrivaltime.class);
                intent.setAction(ShunFengCheActivity.ARRIVAL_TIME);
                intent.putExtra("mqArrivaltime", mqArrivaltime);

            } else if (topicName.equals("moxsan/notifyowner/" + AppData.getInstance().getUserEntity().getUsername())) { //2
                MQNotifyOwner mqNotifyOwner = gson.fromJson(s, MQNotifyOwner.class);
                intent.setAction(ShunFengCheActivity.NOTIFY_CAROWER_NUM);
                intent.putExtra("mqNotifyOwner", mqNotifyOwner);

            } else if (topicName.equals("moxsan/ordernotify/" + AppData.getInstance().getUserEntity().getUsername())) {  //3
                MQOrdernotify mqOrdernotify = gson.fromJson(s, MQOrdernotify.class);
                if (ActivityHolder.getInstance().getTop() instanceof ShunFengCheActivity ||
                        ActivityHolder.getInstance().getTop() instanceof GateToGateActivity) {
                    intent.setAction(ShunFengCheActivity.ORDER_NOTIFY);
                    intent.putExtra("mqOrdernotify", mqOrdernotify);
                } else {
                    if (mqOrdernotify.getType().equals("1")) { //门到门
                        Intent intent1 = new Intent(MqttService.this, GateOrderDetailActivity.class);
                        RespUserOrder respUserOrder = new RespUserOrder();
                        respUserOrder.setOrderid(mqOrdernotify.getOrderid());
                        intent1.putExtra("respUserOrder", respUserOrder);
                        intent1.putExtra("isPayed", false);
                        PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, intent1, 0);
                        showNotification("订单号：" + mqOrdernotify.getOrderid() + "已被接单啦", pi);
                    } else if (mqOrdernotify.getType().equals("2")) {  //顺风车
                        Intent intent1 = new Intent(MqttService.this, ShunFengOrderDetailActivity.class);
                        RespUserOrder respUserOrder = new RespUserOrder();
                        respUserOrder.setOrderid(mqOrdernotify.getOrderid());
                        intent1.putExtra("respUserOrder", respUserOrder);
                        intent1.putExtra("isPayed", false);
                        PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, intent1, 0);
                        showNotification("订单号：" + mqOrdernotify.getOrderid() + "已被接单啦", pi);
                    }
                }

            } else if (topicName.equals("moxsan/pickup/" + AppData.getInstance().getUserEntity().getUsername())) {  //4
                MQPickup mqPickup = gson.fromJson(s, MQPickup.class);
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, MyOrdersActivity.class), 0);
                showNotification("订单号：" + mqPickup.getOrderid() + "已被取货", pi);

            } else if (topicName.equals("moxsan/delivery/" + AppData.getInstance().getUserEntity().getUsername())) {  //5
                MQDelivery mqDelivery = gson.fromJson(s, MQDelivery.class);
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, MyOrdersActivity.class), 0);
                showNotification("订单号：" + mqDelivery.getOrderid() + mqDelivery.getData(), pi);

            } else if (topicName.equals("moxsan/cancelorder/" + AppData.getInstance().getUserEntity().getUsername())) {  //6
                MQCancelOrder mqCancelOrder = gson.fromJson(s, MQCancelOrder.class);
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, MyOrdersActivity.class), 0);
                showNotification("订单号：" + mqCancelOrder.getOrderid() + "被取消啦", pi);

            } else if (topicName.equals("moxsan/payment/" + AppData.getInstance().getUserEntity().getUsername())) {  //7
                MQPayment mqPayment = gson.fromJson(s, MQPayment.class);
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, MyWalletActivity.class), 0);
                showNotification("订单号：" + mqPayment.getOrderid() + "已付款", pi);

            } else if (topicName.equals("moxsan/comment/" + AppData.getInstance().getUserEntity().getUsername())) {  //8
                MQComment mqComment = gson.fromJson(s, MQComment.class);
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, MessagesActivity.class), 0);
                showNotification("订单号：" + mqComment.getId() + "已被评价", pi);

            } else if (topicName.equals("moxsan/newsnotify/" + AppData.getInstance().getUserEntity().getUsername())) {  //9
                MQNewsNotify mqNewsNotify = gson.fromJson(s, MQNewsNotify.class);
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, MessagesActivity.class), 0);
                showNotification("订单号：" + mqNewsNotify.getOrderid() + "收到消息:" + mqNewsNotify.getData(), pi);

            } else if (topicName.equals("moxsan/newordernotify/" + AppData.getInstance().getUserEntity().getUsername())) {  //10
                MQNewOrderNotify mqNewOrderNotify = gson.fromJson(s, MQNewOrderNotify.class);
                if (mqNewOrderNotify.getType().equals("1")) {
                    PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, ReceiveOrderActivity.class).
                            putExtra("name", "直达速递").putExtra("orderId", mqNewOrderNotify.getOrderid()), 0);
                    showNotification("有新订单啦，订单编号：" + mqNewOrderNotify.getOrderid(), pi);

                } else {
                    PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, HitchhikingActivity.class).
                            putExtra("name", "顺风车").putExtra("orderId", mqNewOrderNotify.getOrderid()), 0);
                    showNotification("有新订单啦，订单编号：" + mqNewOrderNotify.getOrderid(), pi);

                }

            } else if (topicName.equals("moxsan/setowner/" + AppData.getInstance().getUserEntity().getUsername())) {  //11
                MQRquestBecomeCarerStatus status = gson.fromJson(s, MQRquestBecomeCarerStatus.class);
                if (status.getRes().equals("0")) {
                    PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, BusinessMainActivity.class), 0);
                    showNotification(status.getType(), pi);

                    RespUserInfo userInfo = AppData.getInstance().getUserEntity();
                    userInfo.setType(3);
                    AppData.getInstance().saveUserEntity(userInfo);

                }else{
                    PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, BusinessMainActivity.class), 0);
                    showNotification(status.getType(), pi);

                    SharedPreferences sp =getSharedPreferences("request", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor =sp.edit();
                    editor.putBoolean("carer",false);
                    editor.commit();

                }

            } else if (topicName.equals("moxsan/arm/" + AppData.getInstance().getUserEntity().getIMEI())) {  //12
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, BusinessMainActivity.class), 0);
                showNotification("设置防盗模式", pi);

            } else if (topicName.equals("moxsan/mmlieage/" + AppData.getInstance().getUserEntity().getIMEI())) {  //13
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, BusinessMainActivity.class), 0);
                showNotification("设置保养里程", pi);

            } else if (topicName.equals("moxsan/sos/" + AppData.getInstance().getUserEntity().getIMEI())) {  //14
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, BusinessMainActivity.class), 0);
                showNotification("设置报警电话", pi);

            } else if (topicName.equals("moxsan/cut/" + AppData.getInstance().getUserEntity().getIMEI())) {  //15
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, BusinessMainActivity.class), 0);
                showNotification("丢失找回", pi);

            } else if (topicName.equals("moxsan/vbsen/" + AppData.getInstance().getUserEntity().getIMEI())) {  //16
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, BusinessMainActivity.class), 0);
                showNotification("设置灵敏度", pi);

            } else if (topicName.equals("moxsan/circle/" + AppData.getInstance().getUserEntity().getIMEI())) {  //17
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, BusinessMainActivity.class), 0);
                showNotification("电子围栏", pi);
            } else if (topicName.equals("moxsan/power/" + AppData.getInstance().getUserEntity().getIMEI())) {  //18
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, BusinessMainActivity.class), 0);
                showNotification("取电", pi);
            } else if (topicName.equals("moxsan/factory/" + AppData.getInstance().getUserEntity().getIMEI())) {  //19
                PendingIntent pi = PendingIntent.getActivity(MqttService.this, 0, new Intent(MqttService.this, BusinessMainActivity.class), 0);
                showNotification("恢复出厂设置", pi);
            }

            sendBroadcast(intent);
        }

        // Disconnect
        public void disconnect() {
            try {
                stopKeepAlives();
                mqttClient.disconnect();
            } catch (MqttPersistenceException e) {
                LLog.i("MqttException" + (e.getMessage() != null ? e.getMessage() : " NULL"));
            }
        }

        /*
         * Send a request to the message broker to be sent messages published
         * with the specified topic name. Wildcards are allowed.
         */
        private void subscribeToTopic(String[] topicName) throws MqttException {

            if ((mqttClient == null) || (mqttClient.isConnected() == false)) {
                // quick sanity check - don't try and subscribe if we don't have a connection
                LLog.i("Connection error" + "No connection");
            } else {
                List<Integer> intList = new ArrayList<Integer>();
                for (int j = 0; j < topicName.length; j++) {
                    intList.add(0);
                }
                int[] MQTT_QUALITIES_OF_SERVICE = new int[intList.size()];
                for (int i = 0; i < intList.size(); i++) {
                    MQTT_QUALITIES_OF_SERVICE[i] = intList.get(i);
                }

                mqttClient.subscribe(topicName, MQTT_QUALITIES_OF_SERVICE);
                for (int i = 0; i < topicName.length; i++) {
                    LLog.i("subscribeToTopic==name==" + topicName[i]);
                }
            }
        }

        /*
         * Sends a message to the message broker, requesting that it be
         * published to the specified topic.
         */
        public void publishToTopic(String topicName, String message) throws MqttException {
            if ((mqttClient == null) || (mqttClient.isConnected() == false)) {
                // quick sanity check - don't try and publish if we don't have
                // a connection
                LLog.i("No connection to public to");
            } else {
                mqttClient.publish(topicName, message.getBytes(), MQTT_QUALITY_OF_SERVICE, MQTT_RETAINED_PUBLISH);
                LLog.i("publishTopic===name==" + topicName + "==message==" + message);
            }
        }

        /*
         * Called if the application loses it's connection to the message
         * broker.
         */
        @Override
        public void connectionLost() throws Exception {
            LLog.i("Loss of connection" + "connection downed");
            stopKeepAlives();
            // null itself
            mConnection = null;
            if (isNetworkAvailable() == true) {
                reconnectIfNecessary();
            }
        }

        public void sendKeepAlive() throws MqttException {
            LLog.i("Sending keep alive");
            // publish to a keep-alive topic
            publishToTopic(MQTT_CLIENT_ID + "/keepalive", mPrefs.getString(PREF_DEVICE_ID, ""));
        }
    }
}
