package com.utils.api;

import java.io.Serializable;

/**
 * 所有不需要缓存的model类都继承该基类，需要做简单缓存的集成ModelBaseCache类
 * @author fengyongqiang
 * @des: 该基类所有的子类需要实现统一的json解析器， 将相关json/XML字符串解析到对应的modelbase子类中
 * @object：后台返回的数据类对象
 *
 */

public abstract class ModelBase implements Serializable {
    /**
     *
      * @param strData json or xml数据， 具体数据类型和数据结构由子类决定
     * @return
     */
    public abstract ModelBase parseData(String strData);
}