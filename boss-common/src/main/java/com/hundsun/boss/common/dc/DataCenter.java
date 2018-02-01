package com.hundsun.boss.common.dc;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.common.utils.SynchronizationHttpClient;

/**
 * 从数据中心获取数据
 */
@SuppressWarnings("unchecked")
public class DataCenter {

    /**
     * 日志对象
     */
    protected static Logger logger = LoggerFactory.getLogger(DataCenter.class);

    /**
     * 到数据中心查询结果
     * 
     * @param param
     * @return
     */
    public static <T> T getRemoteQueryResult(Map<String, String> param, Class<T> destClass) {
        try {
            String receiveString = getRemoteQueryResult(param);
            Map<String, String> resultMap = JSON.parseObject(receiveString, Map.class);
            return JSON.parseObject(resultMap.get("json"), destClass);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    /**
     * 到数据中心查询结果
     * 
     * @param param
     * @return
     * @throws Exception
     */
    public static String getRemoteQueryResult(Map<String, String> param) throws Exception {
        param.putAll(getSettingByInterfaceNo(param.get("interfaceNo")));
        SynchronizationHttpClient httpClinet = new SynchronizationHttpClient();
        String receiveString = httpClinet.sendPost(param.get("cacheUrl"), JSON.toJSONString(param));
        return receiveString;
    }

    /**
     * 通过接口编号获得参数
     * 
     * @param interfaceNo
     * @return
     * @throws IOException
     */
    private static Map<String, String> getSettingByInterfaceNo(String interfaceNo) throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        String serverConfigPath = BaseController.class.getResource("/server-config.xml").getPath();
        Document document = Jsoup.parse(new File(serverConfigPath), "UTF-8");
        Elements services = document.getElementsByTag("service");
        for (Element service : services) {
            if (StringUtils.equals(interfaceNo, getStringAttribute(service, "interfaceNo", ""))) {
                map.put("token", getStringAttribute(service, "token", ""));
                map.put("serviceClass", getStringAttribute(service, "serviceClass", ""));
                map.put("cacheUrl", getStringAttribute(service, "cacheUrl", ""));
                return map;
            }
        }
        return map;
    }

    /**
     * 获取数字元素属性
     * 
     * @param element
     * @param attributeName
     * @return
     */
    private static String getStringAttribute(Element element, String attributeName, String defaultValue) {
        if (!CommonUtil.isNullorEmpty(element.attr(attributeName))) {
            return element.attr(attributeName);
        }
        return defaultValue;
    }
}
