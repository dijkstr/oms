package com.hundsun.boss.modules.charge.common;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.hundsun.boss.common.utils.SpringContextHolder;
import com.hundsun.boss.modules.charge.entity.setting.ChargeType;
import com.hundsun.boss.modules.charge.service.setting.ChargeTypeService;

/**
 * 字典工具类
 */
public class chargeTypeUtils {

    private static ChargeTypeService chargeTypeService = SpringContextHolder.getBean(ChargeTypeService.class);

    public static String getDictLabel(String value, String type, String defaultValue) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)) {
            for (ChargeType dict : getDictList(type)) {
                if (type.equals(dict.getType()) && value.equals(dict.getValue())) {
                    return dict.getLabel();
                }
            }
        }
        return defaultValue;
    }

    public static String getDictValue(String label, String type, String defaultLabel) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)) {
            for (ChargeType dict : getDictList(type)) {
                if (type.equals(dict.getType()) && label.equals(dict.getLabel())) {
                    return dict.getValue();
                }
            }
        }
        return defaultLabel;
    }

    public static List<ChargeType> getDictList(String type) {
        List<ChargeType> list =  chargeTypeService.getDictList(type,null);
        return list;
    }

}
