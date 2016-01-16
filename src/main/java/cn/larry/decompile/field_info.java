package cn.larry.decompile;

import cn.larry.decompile.attribute.attribute_info;

/**
 * Created by larryfu on 2016/1/9.
 *
 * @author larryfu
 */
public class field_info {
    short access_flags;
    short name_index;
    short descriptor_index;
    short attribute_count;
    attribute_info[] attributes;
}
