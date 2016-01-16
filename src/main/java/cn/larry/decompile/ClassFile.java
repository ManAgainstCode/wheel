package cn.larry.decompile;

import cn.larry.decompile.attribute.attribute_info;
import cn.larry.decompile.constant_pool.cp_info;

/**
 * Created by larryfu on 2016/1/9.
 *
 * @author larryfu
 */
public class ClassFile {
    int magic;
    short minor_version;
    short major_version;
    short constant_pool_count;
    cp_info[]  constant_pool;
    short access_flags;
    short this_class;
    short super_class;
    short interface_count;
    short interfaces;
    short fields_count;
    field_info[] fields;
    short method_count;
    method_info[] methods;
    short attribute_count;
    attribute_info[] attributes;
}
