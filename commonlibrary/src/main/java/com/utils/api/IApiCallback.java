package com.utils.api;

/**
 * 绑定数据
 * @author fengyongqiang
 * @des: 所有调用接口api的activity，如果需要获取接口返回数据的，都需要从该接口集成，数据将从 onData 中传回
 * @object：后台返回的数据类对象
 *
 */

public interface IApiCallback {
	/**
	 * @param output 输出结果
	 * @param input  输入，可能为null，根据调用api时输入来定,由传入的model解析器决定返回类型
	 */
    public abstract void onData(Object output, Object input);
}