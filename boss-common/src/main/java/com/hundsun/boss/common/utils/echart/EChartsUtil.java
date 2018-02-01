package com.hundsun.boss.common.utils.echart;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.sys.utils.DictUtils;

/**
 * 图表共用类
 * 
 * @author Administrator
 *
 */
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class EChartsUtil {

    /**
     * 生成echart图表
     * 
     * @param statTable
     * @param graphId
     * @param commonChartDatas
     * @return
     */
    public static String generateGraph(StatTable statTable, String graphId, List<CommonChartData> commonChartDatas) {
        commonChartDatas = chartDatas(commonChartDatas);
        Map<String, List> datasByStatDimension = new LinkedHashMap<String, List>();
        List<Map> datasByObjectiveLapses = new ArrayList<Map>();
        List<String> statDimensions = new ArrayList<String>();
        List<String> ObjectiveLapses = new ArrayList<String>();
        int periodIndex = 0;
        Map datasByObjectiveLapse = new HashMap<String, Double>();
        Double statValue = 0.0;
        DecimalFormat df = new DecimalFormat("###0.###");
        Map dimensionChartType = new HashMap<String, String>();
        for (CommonChartData commonChartData : commonChartDatas) {
            if (CommonUtil.isNullorEmpty(datasByStatDimension.get(commonChartData.getStat_dimension()))) {
                statDimensions.add(commonChartData.getStat_dimension());
                dimensionChartType.put(commonChartData.getStat_dimension(), commonChartData.getChart_type());
                datasByStatDimension.put(commonChartData.getStat_dimension(), new ArrayList());
            }
            periodIndex = ObjectiveLapses.indexOf(commonChartData.getObjective_lapse());

            if ("1".equals(statTable.getNumber())) {
                statValue = Double.parseDouble(df.format(commonChartData.getStat_result() / 10000));
            } else if ("2".equals(statTable.getNumber())) {
                statValue = Double.parseDouble(df.format(commonChartData.getStat_result() / 1000000));
            } else if ("3".equals(statTable.getNumber())) {
                statValue = Double.parseDouble(df.format(commonChartData.getStat_result() / 100000000));
            } else {
                statValue = commonChartData.getStat_result();
            }
            if (periodIndex < 0) {
                ObjectiveLapses.add(commonChartData.getObjective_lapse());
                datasByObjectiveLapse = new HashMap<String, Double>();
                datasByObjectiveLapse.put(commonChartData.getStat_dimension(), statValue);
                datasByObjectiveLapses.add(datasByObjectiveLapse);
            } else {
                datasByObjectiveLapses.get(periodIndex).put(commonChartData.getStat_dimension(), statValue);
            }
            datasByStatDimension.get(commonChartData.getStat_dimension()).add(statValue);
        }
        String unit = DictUtils.getDictLabel(statTable.getUnit(), "stat_unit", "");
        String number = DictUtils.getDictLabel(statTable.getNumber(), "stat_number", "");
        StringBuffer sbGraph = new StringBuffer("var " + graphId + " = echarts.init(document.getElementById('" + graphId + "'));");
        if ("Line".equals(statTable.getChart_type()) || "Bar".equals(statTable.getChart_type())) {
            generateLineAndBar(statTable, graphId, commonChartDatas, datasByStatDimension, statDimensions, ObjectiveLapses, unit, number, sbGraph);
        } else if ("Area".equals(statTable.getChart_type())) {
            generateArea(statTable, graphId, datasByStatDimension, statDimensions, ObjectiveLapses, unit, number, sbGraph);
        } else if ("Pie".equals(statTable.getChart_type()) && ObjectiveLapses.size() > 0) {
            generateDynamicPie(statTable, graphId, datasByObjectiveLapses, statDimensions, ObjectiveLapses, unit, number, sbGraph);
        } else if ("Radar".equals(statTable.getChart_type()) && ObjectiveLapses.size() > 0) {
            generateDynamicPie(statTable, graphId, datasByObjectiveLapses, statDimensions, ObjectiveLapses, unit, number, sbGraph);
        } else if ("LineBar".equals(statTable.getChart_type()) && ObjectiveLapses.size() > 0) {
            generateLineBar(statTable, graphId, commonChartDatas, datasByStatDimension, statDimensions, ObjectiveLapses, dimensionChartType, unit, number, sbGraph);
        }
        sbGraph.append("window.addEventListener('resize',function(){" + graphId + ".resize();});");
        return sbGraph.toString();
    }

    /**
     * 处理多条折线，其中折线少几期数据的问题
     * 
     * @param commonChartDatas
     * @return
     */
    public static List chartDatas(List<CommonChartData> commonChartDatas) {
        List<CommonChartData> newList = new ArrayList<CommonChartData>();
        Map commonChartDatasMap = new LinkedHashMap();
        HashSet<String> statdimensions = new HashSet<String>();
        HashSet<String> objectiveLapses = new HashSet<String>();
        //把数据放入map当中
        for (CommonChartData commonChartData : commonChartDatas) {
            commonChartDatasMap.put(commonChartData.getStat_dimension() + "-" + commonChartData.getObjective_lapse(), commonChartData.getStat_result());
            commonChartDatasMap.put(commonChartData.getStat_dimension(), commonChartData.getChart_type());
            statdimensions.add(commonChartData.getStat_dimension());
            objectiveLapses.add(commonChartData.getObjective_lapse());
        }
        //判断key是否存在，不存在则添加
        for (String statdimension : statdimensions) {
            for (String objectiveLapse : objectiveLapses) {
                if (!commonChartDatasMap.containsKey(statdimension + "-" + objectiveLapse)) {
                    CommonChartData chartData = new CommonChartData();
                    chartData.setStat_dimension(statdimension);
                    chartData.setObjective_lapse(objectiveLapse);
                    chartData.setStat_result(0.0);
                    chartData.setChart_type((String) commonChartDatasMap.get(statdimension));
                    newList.add(chartData);
                }
            }
        }
        commonChartDatas.addAll(newList);
        if (newList.size() > 0) {
            Collections.sort(commonChartDatas, new Comparator<CommonChartData>() {
                @Override
                public int compare(CommonChartData o1, CommonChartData o2) {
                    SimpleDateFormat format = null;
                    if (o1.getObjective_lapse().length() == 6) {
                        format = new SimpleDateFormat("yyyyMM");
                    } else {
                        format = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    try {
                        Date dt1 = format.parse(o1.getObjective_lapse());
                        Date dt2 = format.parse(o2.getObjective_lapse());
                        if (dt1.getTime() > dt2.getTime()) {
                            return 1;
                        } else if (dt1.getTime() < dt2.getTime()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return 0;
                }
            });
        }

        return commonChartDatas;
    }

    /**
     * s 生成动态饼图
     * 
     * @param statTable
     * @param graphId
     * @param datasByObjectiveLapses
     * @param statDimensions
     * @param ObjectiveLapses
     * @param unit
     * @param number
     * @param sbGraph
     */
    private static void generateDynamicPie(StatTable statTable, String graphId, List<Map> datasByObjectiveLapses, List<String> statDimensions, List<String> ObjectiveLapses, String unit, String number, StringBuffer sbGraph) {
        sbGraph.append(graphId + ".setOption({");
        sbGraph.append("timeline : {");
        sbGraph.append("data : [");

        for (int i = 0; i < ObjectiveLapses.size(); i++) {
            if (i == ObjectiveLapses.size() - 1) {
                sbGraph.append("{ name:'" + new StringBuilder(ObjectiveLapses.get(ObjectiveLapses.size() - 1)) + "', symbol:'emptyStar6', symbolSize:8 }");
                break;
            }
            sbGraph.append("'");
            sbGraph.append(new StringBuilder(ObjectiveLapses.get(i)));
            sbGraph.append("'");
            if (i < ObjectiveLapses.size() - 1) {
                sbGraph.append(",");
            }
        }
        sbGraph.append("],");
        sbGraph.append("label : {");
        sbGraph.append("formatter : function(s) {");
        sbGraph.append("return s.slice(0, 10);");
        sbGraph.append("}");
        sbGraph.append("}");
        sbGraph.append("},");
        sbGraph.append("options : [");
        sbGraph.append("{");
        sbGraph.append("title : {");
        sbGraph.append("text: '" + statTable.getName() + "'");
        sbGraph.append("},");
        sbGraph.append("tooltip : {");
        sbGraph.append("trigger: 'item',");
        sbGraph.append("formatter: '{a} <br/>{b}:{c}" + number + unit + "({d}%)'");
        sbGraph.append("},");
        sbGraph.append("legend: {");
        sbGraph.append("data:[");
        for (int i = 0; i < statDimensions.size(); i++) {
            sbGraph.append("'");
            sbGraph.append(statDimensions.get(i));
            sbGraph.append("'");
            if (i < statDimensions.size() - 1) {
                sbGraph.append(",");
            }
        }
        sbGraph.append("]");
        sbGraph.append("},");
        sbGraph.append("toolbox: {");
        sbGraph.append("show : true,");
        sbGraph.append("feature : {");
        sbGraph.append("magicType : {");
        sbGraph.append("show: true, ");
        sbGraph.append("type: ['pie', 'funnel'],");
        sbGraph.append("option: {");
        sbGraph.append("funnel: {");
        sbGraph.append("x: '25%',");
        sbGraph.append("width: '50%',");
        sbGraph.append("funnelAlign: 'left',");
        sbGraph.append("max: 1700");
        sbGraph.append("}");
        sbGraph.append("}");
        sbGraph.append("},");
        sbGraph.append("restore : {show: true},");
        sbGraph.append("saveAsImage : {show: true}");
        sbGraph.append("}");
        sbGraph.append("},");
        sbGraph.append("series : [");
        sbGraph.append("{");
        sbGraph.append("name:'" + statTable.getName() + "',");
        sbGraph.append("type:'pie',");
        sbGraph.append("center: ['50%', '45%'],");
        sbGraph.append("radius: '50%',");
        sbGraph.append("data:[");
        for (int i = 0; i < datasByObjectiveLapses.size(); i++) {
            for (int j = 0; j < statDimensions.size(); j++) {
                double data = 0.0;
                if (datasByObjectiveLapses.get(i).get(statDimensions.get(j)) == null) {
                } else {
                    data = (double) datasByObjectiveLapses.get(i).get(statDimensions.get(j));
                }
                sbGraph.append("{value: " + data + ",  name:'" + statDimensions.get(j) + "'}");
                if (j < statDimensions.size() - 1) {
                    sbGraph.append(",");
                }
            }
            // 这里是最为初始值，因此只需要显示一个元素
            break;
        }
        sbGraph.append("]");
        sbGraph.append("}");
        sbGraph.append("]");
        sbGraph.append("}");
        for (int i = 0; i < datasByObjectiveLapses.size(); i++) {
            sbGraph.append(" ,");
            sbGraph.append("{");
            sbGraph.append("series : [");
            sbGraph.append("{");
            sbGraph.append("name:'" + statTable.getName() + "',");
            sbGraph.append("type:'pie',");
            sbGraph.append("data:[");
            for (int j = 0; j < statDimensions.size(); j++) {
                if (CommonUtil.isNullorEmpty(datasByObjectiveLapses.get(i).get(statDimensions.get(j)))) {
                    sbGraph.append("{value: 0,  name:'" + statDimensions.get(j) + "'}");
                } else {
                    sbGraph.append("{value: " + datasByObjectiveLapses.get(i).get(statDimensions.get(j)) + ",  name:'" + statDimensions.get(j) + "'}");
                }
                if (j < statDimensions.size() - 1) {
                    sbGraph.append(",");
                }
            }
            sbGraph.append("]");
            sbGraph.append("}");
            sbGraph.append("]");
            sbGraph.append("}");
        }
        sbGraph.append("]");
        sbGraph.append("});");
    }

    /**
     * 生成面积图
     * 
     * @param statTable
     * @param graphId
     * @param datasByStatDimension
     * @param statDimensions
     * @param ObjectiveLapses
     * @param sbGraph
     */
    private static void generateArea(StatTable statTable, String graphId, Map<String, List> datasByStatDimension, List<String> statDimensions, List<String> ObjectiveLapses, String unit, String number, StringBuffer sbGraph) {
        sbGraph.append(graphId + ".setOption({");
        sbGraph.append("title : {");
        sbGraph.append("text: '" + statTable.getName() + "'");
        sbGraph.append("},");
        sbGraph.append("tooltip : {");
        sbGraph.append("trigger: 'axis'");
        sbGraph.append("},");
        sbGraph.append("legend: {");
        sbGraph.append("y:'bottom',");
        sbGraph.append("padding:0,");
        sbGraph.append("data:[");
        for (int i = 0; i < statDimensions.size(); i++) {
            sbGraph.append("'");
            sbGraph.append(statDimensions.get(i));
            sbGraph.append("'");
            if (i < statDimensions.size() - 1) {
                sbGraph.append(",");
            }
        }
        sbGraph.append("]");
        sbGraph.append("},");
        sbGraph.append("toolbox: {");
        sbGraph.append("show : true,");
        sbGraph.append("feature : {");
        sbGraph.append("magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},");
        sbGraph.append("restore : {show: true},");
        sbGraph.append("saveAsImage : {show: true}");
        sbGraph.append("}");
        sbGraph.append("},");
        sbGraph.append("calculable : true,");
        sbGraph.append("xAxis : [");
        sbGraph.append("{");
        sbGraph.append("type : 'category',");
        sbGraph.append("boundaryGap : false,");
        sbGraph.append("data : [");
        for (int i = 0; i < ObjectiveLapses.size(); i++) {
            sbGraph.append("'");
            sbGraph.append(ObjectiveLapses.get(i));
            sbGraph.append("'");
            if (i < ObjectiveLapses.size() - 1) {
                sbGraph.append(",");
            }
        }
        sbGraph.append("]");
        sbGraph.append("}");
        sbGraph.append("],");
        sbGraph.append("yAxis : [");
        sbGraph.append("{");
        sbGraph.append("type : 'value',");
        if (datasByStatDimension != null && datasByStatDimension.size() > 0) {
            sbGraph.append(" axisLabel : { formatter: '{value} " + number + unit + "'},");
        }
        sbGraph.append("}");
        sbGraph.append("],");
        sbGraph.append("series : [");
        Iterator<Entry<String, List>> it = datasByStatDimension.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, List> entry = it.next();
            sbGraph.append("{");
            sbGraph.append("name:'" + entry.getKey() + "',");
            sbGraph.append("smooth:true,");
            sbGraph.append("type:'line',");
            sbGraph.append("stack: '总量',");
            sbGraph.append("itemStyle: {normal: {areaStyle: {type: 'default'}}},");
            sbGraph.append("data:[");
            for (int i = 0; i < entry.getValue().size(); i++) {
                sbGraph.append(entry.getValue().get(i));
                if (i < entry.getValue().size() - 1) {
                    sbGraph.append(",");
                }
            }
            sbGraph.append("]");
            sbGraph.append("}");
            if (it.hasNext()) {
                sbGraph.append(",");
            }
        }
        sbGraph.append("]");
        sbGraph.append("});");
    }

    /**
     * 生成线或柱图
     * 
     * @param statTable
     * @param graphId
     * @param commonChartDatas
     * @param datasByStatDimension
     * @param statDimensions
     * @param ObjectiveLapses
     * @param unit
     * @param number
     * @param sbGraph
     */
    private static void generateLineAndBar(StatTable statTable, String graphId, List<CommonChartData> commonChartDatas, Map<String, List> datasByStatDimension, List<String> statDimensions, List<String> ObjectiveLapses, String unit, String number, StringBuffer sbGraph) {
        sbGraph.append(graphId + ".setOption({");
        sbGraph.append("title : {");
        sbGraph.append("text: '" + statTable.getName() + "'");
        sbGraph.append("},");
        sbGraph.append("tooltip : {");
        sbGraph.append("trigger: 'axis'");
        sbGraph.append("},");
        sbGraph.append("legend: {");
        sbGraph.append("y:'bottom',");
        sbGraph.append("padding:0,");
        sbGraph.append("data:[");
        for (int i = 0; i < statDimensions.size(); i++) {
            sbGraph.append("'");
            sbGraph.append(statDimensions.get(i));
            sbGraph.append("'");
            if (i < statDimensions.size() - 1) {
                sbGraph.append(",");
            }
        }
        sbGraph.append("]");
        sbGraph.append("},");
        sbGraph.append("toolbox: {");
        sbGraph.append("show : true,");
        sbGraph.append("feature : {");
        if ("Line".equals(statTable.getChart_type())) {
            sbGraph.append("magicType : {show: true, type: ['line', 'bar']},");
        } else {
            sbGraph.append("magicType : {show: true, type: ['bar', 'line']},");
        }
        sbGraph.append("restore : {show: true},");
        sbGraph.append("saveAsImage : {show: true}");
        sbGraph.append("}");
        sbGraph.append("},");
        sbGraph.append("calculable : true,");
        sbGraph.append("xAxis : [");
        sbGraph.append("{");
        sbGraph.append("type : 'category',");
        sbGraph.append("data : [");
        for (int i = 0; i < ObjectiveLapses.size(); i++) {
            sbGraph.append("'");
            sbGraph.append(ObjectiveLapses.get(i));
            sbGraph.append("'");
            if (i < ObjectiveLapses.size() - 1) {
                sbGraph.append(",");
            }
        }
        sbGraph.append("]");
        sbGraph.append("}");
        sbGraph.append("],");
        sbGraph.append("yAxis : [");
        sbGraph.append("{");
        sbGraph.append("type : 'value',");
        if (commonChartDatas != null && commonChartDatas.size() > 0) {
            sbGraph.append(" axisLabel : { formatter: '{value} " + number + unit + "'},");
        }
        sbGraph.append("splitArea : {show : true}");
        sbGraph.append("}");
        sbGraph.append("],");
        sbGraph.append("series : [");
        Iterator<Entry<String, List>> it = datasByStatDimension.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, List> entry = it.next();
            sbGraph.append("{");
            sbGraph.append("name:'" + entry.getKey() + "',");
            sbGraph.append("smooth:true,");
            sbGraph.append("type:'line',");
            sbGraph.append("data:[");
            for (int i = 0; i < entry.getValue().size(); i++) {
                sbGraph.append(entry.getValue().get(i));
                if (i < entry.getValue().size() - 1) {
                    sbGraph.append(",");
                }
            }
            sbGraph.append("]");
            sbGraph.append("}");
            if (it.hasNext()) {
                sbGraph.append(",");
            }
        }
        sbGraph.append("]");
        sbGraph.append("});");
    }

    /**
     * 生成雷达图
     * 
     * @param statTable
     * @param graphId
     * @param commonChartDatas
     * @param datasByStatDimension
     * @param statDimensions
     * @param ObjectiveLapses
     * @param unit
     * @param number
     * @param sbGraph
     */
    private static void generateRadar(StatTable statTable, String graphId, List<CommonChartData> commonChartDatas, Map<String, List> datasByStatDimension, List<String> statDimensions, List<String> ObjectiveLapses, String unit, String number, StringBuffer sbGraph) {
    }

    /**
     * 生成柱线混合图
     * 
     * @param statTable
     * @param graphId
     * @param commonChartDatas
     * @param datasByStatDimension
     * @param statDimensions
     * @param ObjectiveLapses
     * @param dimensionChartType
     * @param unit
     * @param number
     * @param sbGraph
     */
    private static void generateLineBar(StatTable statTable, String graphId, List<CommonChartData> commonChartDatas, Map<String, List> datasByStatDimension, List<String> statDimensions, List<String> ObjectiveLapses, Map dimensionChartType, String unit, String number, StringBuffer sbGraph) {
        sbGraph.append(graphId + ".setOption({");
        sbGraph.append("title : {");
        sbGraph.append("text: '" + statTable.getName() + "'");
        sbGraph.append("},");
        sbGraph.append("tooltip : {");
        sbGraph.append("trigger: 'axis'");
        sbGraph.append("},");
        sbGraph.append("toolbox: {");
        sbGraph.append("show : true,");
        sbGraph.append("feature : {");
        sbGraph.append("mark : {show: true},");
        sbGraph.append("dataView : {show: true, readOnly: false},");
        sbGraph.append("magicType: {show: true, type: ['line', 'bar']},");
        sbGraph.append("restore : {show: true},");
        sbGraph.append("saveAsImage : {show: true}");
        sbGraph.append("}");
        sbGraph.append("},");
        sbGraph.append("calculable : true,");
        sbGraph.append("legend: {");
        sbGraph.append("data:[");
        for (int i = 0; i < statDimensions.size(); i++) {
            sbGraph.append("'");
            sbGraph.append(statDimensions.get(i));
            sbGraph.append("'");
            if (i < statDimensions.size() - 1) {
                sbGraph.append(",");
            }
        }
        sbGraph.append("]");
        sbGraph.append("},");
        sbGraph.append("xAxis : [");
        sbGraph.append("{");
        sbGraph.append("type : 'category',");
        sbGraph.append("data : [");
        for (int i = 0; i < ObjectiveLapses.size(); i++) {
            sbGraph.append("'");
            sbGraph.append(ObjectiveLapses.get(i));
            sbGraph.append("'");
            if (i < ObjectiveLapses.size() - 1) {
                sbGraph.append(",");
            }
        }
        sbGraph.append("]");
        sbGraph.append("}");
        sbGraph.append("],");
        sbGraph.append("yAxis : [");
        sbGraph.append("{");
        sbGraph.append("type : 'value',");
        if (commonChartDatas != null && commonChartDatas.size() > 0) {
            sbGraph.append(" axisLabel : { formatter: '{value} " + number + unit + "'}");
        }
        sbGraph.append("},");
        sbGraph.append("{");
        sbGraph.append("type : 'value',");
        if (commonChartDatas != null && commonChartDatas.size() > 0) {
            sbGraph.append(" axisLabel : { formatter: '{value} " + number + unit + "'}");
        }
        sbGraph.append("}");
        sbGraph.append("],");
        sbGraph.append("series : [");
        Iterator<Entry<String, List>> it = datasByStatDimension.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, List> entry = it.next();
            sbGraph.append("{");
            sbGraph.append("name:'" + entry.getKey() + "',");
            sbGraph.append("smooth:true,");
            sbGraph.append("type:'" + dimensionChartType.get(entry.getKey()) + "',");
            sbGraph.append("data:[");
            for (int i = 0; i < entry.getValue().size(); i++) {
                sbGraph.append(entry.getValue().get(i));
                if (i < entry.getValue().size() - 1) {
                    sbGraph.append(",");
                }
            }
            sbGraph.append("]");
            sbGraph.append("}");
            if (it.hasNext()) {
                sbGraph.append(",");
            }
        }
        sbGraph.append("]");
        sbGraph.append("});");
    }

    /**
     * 生成2维图表
     * 
     * @param statTable
     * @param graphId
     * @param plainTableDatas
     * @return
     */
    public static String generatePlain(StatTable statTable, String graphId, List<Map<String, Object>> plainTableDatas) {
        StringBuffer sbTable = new StringBuffer();
        Vector<String> columnHeader = new Vector<String>();
        for (int i = 0; i < plainTableDatas.size(); i++) {
            if (i == 0) {
                sbTable.append("<span style='margin:0px;font-size: 18px;font-family: 微软雅黑;'>" + statTable.getName() + "</span><br/>");
                sbTable.append("<table class='table table-striped table-bordered table-condensed' style='width:" + statTable.getWidth() + "px;height:" + statTable.getHeight() + "px'>");
                sbTable.append("<thead>");
                for (Map.Entry<String, Object> entry : plainTableDatas.get(0).entrySet()) {
                    columnHeader.add(entry.getKey());
                    sbTable.append("<th width='80px'>" + entry.getKey() + "</th>");
                }
                sbTable.append("</thead>");
                sbTable.append("<tbody>");
            }
            sbTable.append("<tr>");
            for (int j = 0; j < columnHeader.size(); j++) {
                sbTable.append("<td>").append(plainTableDatas.get(i).get(columnHeader.get(j))).append("</td>");
            }
            sbTable.append("</tr>");
        }
        return sbTable.toString();
    }
}
