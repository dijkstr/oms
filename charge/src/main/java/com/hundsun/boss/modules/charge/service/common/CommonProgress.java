package com.hundsun.boss.modules.charge.service.common;

import java.util.HashMap;
import java.util.Map;

import com.hundsun.boss.common.utils.CommonUtils;

/**
 * @author whl
 * @version 创建时间：2016年8月9日 下午2:15:55
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CommonProgress {
    private CommonProgress() {
    }

    public static Map chargeOrderrogress = new HashMap();

    /**
     * 处理正常进度
     */
    public static void progressNomal(String contant, Integer percentage, Integer percent) {
        chargeOrderrogress.put("CONTENT", contant);
        if (!CommonUtils.isNullorEmpty(percent)) {
            chargeOrderrogress.put("PERCENT", ": " + percentage + "/" + percent);
        } else {
            chargeOrderrogress.put("PERCENT", "");
        }
        chargeOrderrogress.put("PROGRAM_STATUS", "1");
    }

    /**
     * 处理正常进度
     */
    public static void progressNomal(String contant) {
        progressNomal(contant, null, null);
    }

    /**
     * 处理正常进度
     */
    public static void progressFinish() {
        chargeOrderrogress.put("PROGRAM_STATUS", "9");
    }

    /**
     * 处理异常进度
     */
    public static void progressWrong() {
        chargeOrderrogress.put("PROGRAM_STATUS", "-1");
    }
}
