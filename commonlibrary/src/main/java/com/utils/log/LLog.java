package com.utils.log;

import android.annotation.SuppressLint;
import android.util.Log;

import com.utils.file.FileConstants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * 日志文件， 所有日志用LLog，用法跟Log一样
 */
public class LLog {
	private static final String TAG ="moxiaosanLog";
	private static LLog gLog 		 = null;
	private boolean 		mOutputLog   = false;
	private String			mTagName	 = "moxiaosanLog";
	private FileWriter		mLogFileWrite= null;
	private static String	mLogFileName = FileConstants.getApiSaveFilePath() + "/moxiaosanlog.txt";

	private  static final LLog getLog() {
		if (gLog==null) {
			gLog =new LLog();
			gLog.mTagName    = TAG;
			
			if (gLog.mOutputLog==true) {
				try {
					File logFile = new File(mLogFileName);
					if (logFile.exists()) logFile.delete();
					//防止空父目录
					mkDir(new File(FileConstants.getApiSaveFilePath()));

					boolean sucessed = logFile.createNewFile();
					if (sucessed==true) gLog.mLogFileWrite= new FileWriter(mLogFileName, true);
				} catch (IOException e) {
					gLog.mLogFileWrite=null;
					e.printStackTrace();
				}
			}
		}
		return gLog;
	}

	//防止空父目录
	public static void mkDir(File file){
		if(file.getParentFile().exists()){
			file.mkdir();
		}else{
			mkDir(file.getParentFile());
			file.mkdir();
		}
	}

	static public boolean needLog(){
		return LLog.getLog().mOutputLog;
	}
	
	@SuppressLint("SimpleDateFormat")
	static private void internal_log(String msg, int level ) {
		if (LLog.getLog().mOutputLog==false) return;
		String writeMSG = msg;
		
		StackTraceElement ste = new Throwable().getStackTrace()[2]; 
		String methodName     = new Exception().getStackTrace()[2].getMethodName();
		String fileName       = ste.getFileName();
		fileName              = fileName.substring(0,fileName.indexOf("."));
		writeMSG = "["+fileName+":"+methodName+":"+ste.getLineNumber()+"] " +msg;
        switch(level){
//            case Log.VERBOSE:
//                Log.v(gLog.mTagName, writeMSG);
//                break;
//            case Log.INFO:
//                Log.i(gLog.mTagName, writeMSG);
//                break;
//            case Log.DEBUG:
//                Log.d(gLog.mTagName, writeMSG);
//                break;
//            case Log.WARN:
//                Log.w(gLog.mTagName, writeMSG);
//                break;
            case Log.ERROR:
            default:
                Log.e(gLog.mTagName, writeMSG);
                break;
        }

		if (gLog.mLogFileWrite!=null) {
			Date currentDate 	  = new Date(System.currentTimeMillis());
			String currentTime    = new SimpleDateFormat("HH:mm:ss:sss").format(currentDate);  
			writeMSG = "["+currentTime+"] "+writeMSG+"\n";

			try {
				gLog.mLogFileWrite.write(writeMSG);
				gLog.mLogFileWrite.flush();
			} catch (IOException e) {
				gLog.mLogFileWrite=null;
			}
		}
	}

    //日志接口，使用方法跟android系统的Log对应
    static public void v(String msg) {
        internal_log(msg, Log.VERBOSE);
    }
    static public void w(String msg) {
        internal_log(msg, Log.WARN);
    }
    static public void i(String msg) {
        internal_log(msg, Log.INFO);
    }
    static public void d(String msg) {
        internal_log(msg, Log.DEBUG);
    }
	static public void e(String msg) {
		internal_log(msg, Log.ERROR);
	}
	static public void e(Exception e){
		try {
	        StringWriter sw = new StringWriter(); 
	        PrintWriter  pw = new PrintWriter(sw);
	        e.printStackTrace(pw);
		    internal_log(" ERROR:"+e.getMessage(), Log.ERROR);
		    internal_log("\r\n" + sw.toString() + "\r\n", Log.ERROR);
	    } catch (Exception e2) {
	    	internal_log(" bad getErrorInfoFromException", Log.ERROR);
	    }
	}
}
