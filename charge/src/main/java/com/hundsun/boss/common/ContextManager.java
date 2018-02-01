package com.hundsun.boss.common;

import java.util.HashMap;
import java.util.Map;

public class ContextManager {
    /*
     * 账单模板缓存
     */
    public static final Map<String, ReportConfig> reportConfig = new HashMap<String, ReportConfig>();

    /*
     * 控制同一时间只有一个计费出账单逻辑在运行
     */
    public static boolean isChargeOrderOn = false;

}
