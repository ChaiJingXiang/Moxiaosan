package com.utils.ui.base;
/** 
 * @author fengyongqiang
 * @E-mail: 
 * @version 1.0
 * @创建时间：2016-2-25
 * @类说明 activity维持栈， 项目中所有activity create时，push到栈中， finish，出栈
 */

import android.app.Activity;

import com.utils.log.LLog;

import java.util.ArrayList;
import java.util.List;

public class ActivityHolder {

    private List<Activity> activityList;
    private static ActivityHolder activityHolder;

    private ActivityHolder() {
        activityList = new ArrayList<Activity>();
    }

    public static synchronized ActivityHolder getInstance() {
        if (activityHolder == null) {
            activityHolder = new ActivityHolder();
        }
        return activityHolder;
    }

    /**
     * add the activity in to a list
     * 
     * @param activity
     */
    public void push(Activity activity) {
        if (activity != null) {
            activityList.add(activity);
            int size = activityList.size();
            for(int i=0; i<size; i++) {
            		//LLog.e(activityList.get(i) + "");
            }
        }
    }
    
    public Activity getTop(){
    	 try {
    		 Activity top = null;
    		 if(activityList.size() > 0){
    			 top = activityList.get(activityList.size() - 1);
    		 }
    		 return top;
    	 } 
    	 catch (Exception e) {
             LLog.e(e);
         	return null;
         }
    }

    /**
     * finish all the activity in the list.
     */
    public void finishAllActivity() {
        int size = activityList.size();
        for (int i = size - 1; i >= 0; i--) {
            Activity activity = activityList.get(i);
            if (!activity.isFinishing()) {
                activity.finish();
            }
            activityList.remove(activity);
        }
    }
    
    /**
     * remove the finished activity in the list.
     * @param activity  the activity is removed from activityList
     */
    public void pop(Activity activity) {
        try {
            activityList.remove(activity);
        } catch (Exception e) {
            LLog.e(e);
        }
    }
    
    public boolean checkActivityIsVasivle(Activity activity) {
    	return activityList.contains(activity);
    }
}