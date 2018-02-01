package com.hundsun.boss.common.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.persistence.Id;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CommonUtil {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    /**
     * 删除文件路径下所有文件夹及文件
     * 
     * @param path
     */
    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }

    /**
     * 异常处理
     * 
     * @param data
     * @param e
     */
    public static void exceptionHandler(Map data, Exception e, String businessName) {
        if (e.getMessage().toLowerCase().contains("timeoutexception")) {
            data.put("result", "timeoutexception");
            data.put("message", businessName + "正在处理，请稍后在操作日志中查看结果");
        } else {
            data.put("result", "fail");
            data.put("message", businessName + "失败，请稍后在操作日志中查看结果");
        }
        logger.error(e.getMessage());
    }

    /**
     * 异常处理
     * 
     * @param data
     * @param e
     */
    public static void exceptionHandler(Map data, Exception e) {
        if (e.getMessage().toLowerCase().contains("timeoutexception")) {
            data.put("result", "timeoutexception");
            data.put("message", "正在处理，请稍后在操作日志中查看结果");
        } else {
            data.put("result", "fail");
            data.put("message", "处理失败，请稍后在操作日志中查看结果");
        }
        logger.error(e.getMessage());
    }

    /**
     * 判断对象是否为空
     * 
     * @param obj
     * @return
     */
    public static boolean isNullorEmpty(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof String && "".equals(obj)) {
            return true;
        } else if (obj instanceof Number && ((Number) obj).doubleValue() == 0) {
            return true;
        } else if (obj instanceof Boolean && !((Boolean) obj)) {
            return true;
        } else if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        } else if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        } else if (obj instanceof Object[] && ((Object[]) obj).length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 读取报表的css文件
     * 
     * @param configName
     * @param key
     * @return
     */
    public static String readReportCSS(String cssFileName) {
        return CommonUtil.readReportCSS("/report/css/", cssFileName);
    }

    /**
     * 读取报表的css文件
     * 
     * @param configName
     * @param key
     * @return
     */
    public static String readReportCSS(String configPath, String cssFileName) {
        String retValue = "";
        try {
            URL url = CommonUtil.class.getResource(configPath + cssFileName);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = null;
            String line = null;
            StringBuilder result = new StringBuilder();
            try {
                inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                retValue = result.toString();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return retValue;
    }

    /**
     * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
     * 
     * @param sourceFilePath :待压缩的文件路径
     * @param zipFilePath :压缩后存放路径
     * @param fileName :压缩后文件的名称
     * @return
     */
    public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if (sourceFile.exists() == false) {
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName);
                if (zipFile.exists()) {
                } else {
                    File[] sourceFiles = sourceFile.listFiles();
                    if (null == sourceFiles || sourceFiles.length < 1) {
                    } else {
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024 * 10];
                        for (int i = 0; i < sourceFiles.length; i++) {
                            // 创建ZIP实体，并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                            zos.putNextEntry(zipEntry);
                            // 读取待压缩的文件并写进压缩包里
                            fis = new FileInputStream(sourceFiles[i]);
                            bis = new BufferedInputStream(fis, 1024 * 10);
                            int read = 0;
                            while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
                                zos.write(bufs, 0, read);
                            }
                            fis.close();
                        }
                        fos.close();
                        flag = true;
                    }
                }
            } catch (FileNotFoundException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            } finally {
                // 关闭流
                try {
                    if (null != zos)
                        zos.close();
                    if (null != bis)
                        bis.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return flag;
    }

    public static String changeDateFormat(String date) {
        if (date != null && date != "") {
            if (date.trim().length() == 10) {
                date = date.substring(0, 4) + "年" + date.substring(5, 7) + "月" + date.substring(8) + "日";
            }
        }

        return date;
    }

    public static String feeRatio(String fee_ratio) {
        if (fee_ratio != null) {
            boolean mm = fee_ratio.contains(".");
            if (mm) {
                int i;
                int len = fee_ratio.length();
                for (i = 0; i < len; i++) {
                    if (fee_ratio.charAt(len - 1 - i) != '0' && fee_ratio.charAt(len - 1 - i) != '.') {
                        break;
                    }
                    if (fee_ratio.charAt(len - i - 1) == '.') {
                        return fee_ratio.substring(0, len - i - 1) + ".00";
                    }
                }
                fee_ratio = fee_ratio.substring(0, len - i);
                String[] array = fee_ratio.split("\\.");
                if (array[1].length() == 1) {
                    return fee_ratio + "0";
                } else {
                    return fee_ratio;
                }
            }
            return fee_ratio + ".00";
        }
        return fee_ratio;
    }

    /**
     * 当月第一天
     * 
     * @param date
     * @return
     */
    public static String getMonthFirstDay(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(date);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);
        String day_first = df.format(gcLast.getTime());
        return day_first;

    }

    /**
     * 当月最后一天
     * 
     * @param date
     * @return
     */
    public static String getMonthLastDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        // 设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String lastDayOfMonth = sdf.format(cal.getTime());

        return lastDayOfMonth;
    }

    /**
     * Converts a JavaBean to a map.
     * 
     * @param bean JavaBean to convert
     * @return map converted
     * @throws IntrospectionException failed to get class fields
     * @throws IllegalAccessException failed to instant JavaBean
     * @throws InvocationTargetException failed to call setters
     */
    public static final Map toMap(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    /**
     * 将list bean对象转换为Map list
     * 
     * @param list
     * @return
     * @throws Exception
     */
    public static final List<Map> toMapList(List list) throws Exception {
        return toMapList(list, null);
    }

    /**
     * 将list bean对象转换为Map list
     * 
     * @param list
     * @return
     * @throws Exception
     */
    public static final List<Map> toMapList(List list, String flag) throws Exception {
        List<Map> result = new ArrayList<Map>();
        for (Object obj : list) {
            Map m = toMap(obj);
            if (!CommonUtil.isNullorEmpty(flag)) {
                m.put("flag", flag);
            }
            result.add(m);
        }
        return result;
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     * 
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map convertBean(Object bean) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    /**
     * 将list转换为数组
     * 
     * @param objList
     * @return
     */
    public static Object[] listToArray(List<?> objList, Class classType) {
        Object[] objArray = (Object[]) Array.newInstance(classType, objList.size());
        if (objList.size() == 0) {
            return objArray;
        }
        for (int i = 0; i < objList.size(); i++) {
            objArray[i] = objList.get(i);
        }
        return objArray;
    }

    /**
     * 根据map中的key注入相应的字符串中的形如{key}的内容
     * 
     * @param orgiString
     * @param injection
     * @return
     */
    public static String injectString(String orgiString, Map injection) {
        String destString = "";
        if (!CommonUtil.isNullorEmpty(orgiString)) {
            destString = orgiString;
        }
        Iterator it = injection.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            destString = destString.replaceAll("#" + key + "#", String.valueOf(injection.get(key)));
        }
        return destString;
    }

    /**
     * 判断对象是否为空
     * 
     * @param obj
     * @return
     */
    public static double getDoubleValue(Map map, String key) {
        if (!CommonUtil.isNullorEmpty(map.get(key))) {
            return Double.valueOf(String.valueOf(map.get(key)));
        }
        return 0;
    }

    public static Map<String, Object> parseJSON2Map(String jsonStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        //最外层解析
        JSONObject json = JSONObject.fromObject(jsonStr);
        for (Object k : json.keySet()) {
            Object v = json.get(k);
            //如果内层还是数组的话，继续解析
            if (v instanceof JSONArray) {
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Iterator<JSONObject> it = ((JSONArray) v).iterator();
                while (it.hasNext()) {
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }

    /**
     * 获取实体声明@id属性的字段
     * 
     * @param entity
     * @return
     */
    public static String getIdFiled(Class<?> entity) {
        //获取对象实体声明的属性字段
        Field[] fields = entity.getDeclaredFields();
        String idfiled = null;
        for (Field f : fields) {
            //获取字段中包含@id的注解 的字段
            if (f.getAnnotation(Id.class) != null) {
                idfiled = f.getName();
                break;
            }

        }
        //没有对象实体没有@id注解，则为id
        if (idfiled == null) {
            idfiled = "id";
        }

        return idfiled;
    }
}
