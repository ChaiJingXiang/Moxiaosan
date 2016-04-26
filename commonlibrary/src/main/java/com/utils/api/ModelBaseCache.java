package com.utils.api;

/**
 * 所有需要缓存的model类都需要继承该基类
 * @author fengyongqiang
 * @des: 该基类所有的子类需要实现统一的json解析器， 将相关json/XML字符串解析到对应的modelbaseCache子类中
 * @object：后台返回的数据类对象
 *
 */

public abstract class ModelBaseCache extends ModelBase {
    /**
     *
     * @return 返回 key-value缓存的key，用model类的某个具备唯一性的成员变量，比如user类的userid，mobile等
     */
    public abstract String getKeyValue();
}