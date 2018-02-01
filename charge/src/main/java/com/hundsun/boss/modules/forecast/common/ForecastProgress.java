package com.hundsun.boss.modules.forecast.common;

import java.util.HashMap;
import java.util.Map;

import com.hundsun.boss.common.utils.CommonUtils;

/**
 * 通用进度管理
 */
public class ForecastProgress {
    private ForecastProgress() {
    }

    public static Map<String, String> forecastProgress = new HashMap<String, String>();

    /**
     * 处理正常进度
     */
    public static void progressNomal(String contant, Integer percentage, Integer percent) {
        forecastProgress.put(ForecastConstant.FORECAST_PROGRESS_CONTENT, contant);
        if (!CommonUtils.isNullorEmpty(percent)) {
            forecastProgress.put(ForecastConstant.FORECAST_PROGRESS_PERCENT, ": " + percentage + "/" + percent);
        } else {
            forecastProgress.put(ForecastConstant.FORECAST_PROGRESS_PERCENT, "");
        }
        forecastProgress.put(ForecastConstant.FORECAST_PROGRAM_STATUS, "1");
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
        forecastProgress.put(ForecastConstant.FORECAST_PROGRAM_STATUS, "9");
    }

    /**
     * 处理异常进度
     */
    public static void progressWrong() {
        forecastProgress.put(ForecastConstant.FORECAST_PROGRAM_STATUS, "-1");
    }
}
