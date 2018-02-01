package com.hundsun.boss.common.utils.echart;

public class CommonChartData {
    //统计维度（公司、部门、产品、客户）
    private String stat_dimension;
    //客观推移量（日、周、月、季度、年）
    private String objective_lapse;
    //统计结果
    private Double stat_result;
    // 混合图用
    private String chart_type;

    public String getStat_dimension() {
        return stat_dimension;
    }

    public void setStat_dimension(String stat_dimension) {
        this.stat_dimension = stat_dimension;
    }

    public String getObjective_lapse() {
        return objective_lapse;
    }

    public void setObjective_lapse(String objective_lapse) {
        this.objective_lapse = objective_lapse;
    }

    public Double getStat_result() {
        return stat_result;
    }

    public void setStat_result(Double stat_result) {
        this.stat_result = stat_result;
    }

    public String getChart_type() {
        return chart_type;
    }

    public void setChart_type(String chart_type) {
        this.chart_type = chart_type;
    }
}
