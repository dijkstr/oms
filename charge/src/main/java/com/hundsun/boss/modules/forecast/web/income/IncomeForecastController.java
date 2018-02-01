package com.hundsun.boss.modules.forecast.web.income;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.common.utils.IdGen;
import com.hundsun.boss.common.utils.echart.CommonChartData;
import com.hundsun.boss.common.utils.echart.EChartsUtil;
import com.hundsun.boss.common.utils.echart.StatTable;
import com.hundsun.boss.modules.forecast.common.ForecastConstant;
import com.hundsun.boss.modules.forecast.form.income.IncomeForecastForm;
import com.hundsun.boss.modules.forecast.service.income.IncomeForecastService;
import com.hundsun.boss.modules.forecast.thread.income.IncomeForecastThread;

/**
 * 收入预测Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/forecast/income/incomeForecast")
public class IncomeForecastController extends BaseController {

    @Autowired
    private IncomeForecastService incomeForecastService;

    @RequiresPermissions("forecast:income:view")
    @RequestMapping(value = { "list", "" })
    public String list(IncomeForecastForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        return "modules/" + "forecast/income/incomeForecast";
    }

    /**
     * 获取收入预测进度
     * 
     * @return
     */

    @RequestMapping(value = "/getForecastProgress")
    public @ResponseBody Map<String, Object> getForecastProgress() {

        Map<String, Object> data = new HashMap<String, Object>();
        // status:1--正在处理，9--处理终了，-1--异常
        // msg:"开始处理"，"处理终了"，"系统异常，请联系管理员"
        try {
            data.putAll(incomeForecastService.getIncomeForecastProgress());
            if ("1".equals(data.get(ForecastConstant.FORECAST_PROGRAM_STATUS))) {
            } else if ("9".equals(data.get(ForecastConstant.FORECAST_PROGRAM_STATUS))) {
                data.put("status", "9");
                data.put("msg", "收入预测成功");
            } else if ("-1".equals(data.get(ForecastConstant.FORECAST_PROGRAM_STATUS))) {
                data.put("status", "-1");
                data.put("msg", "系统异常，请联系管理员");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            data.put("status", "-1");
            data.put("msg", "系统异常，请联系管理员");
        }
        return data;
    }

    /**
     * 刷新
     * 
     * @param form
     * @param request
     * @return
     * @throws Exception
     */
    @RequiresPermissions("forecast:income:refresh")
    @RequestMapping(value = "/refresh")
    public @ResponseBody Map<String, String> refresh(IncomeForecastForm form, HttpServletRequest request) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        Map<String, String> progress = incomeForecastService.getIncomeForecastProgress();
        if (!CommonUtil.isNullorEmpty(progress) && "1".equals(progress.get(ForecastConstant.FORECAST_PROGRAM_STATUS))) {
            data.put("result", "success");
            data.put("wait", "wait");
            return data;
        }
        incomeForecastService.resetIncomeForecastStatus();

        IncomeForecastThread thread = new IncomeForecastThread(form, incomeForecastService);
        thread.start();
        data.put("result", "success");
        return data;

    }

    /**
     * 显示图表
     * 
     * @param id
     * @param timeQuantum
     * @param startDate
     * @param endDate
     * @param redirectAttributes
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "graph")
    public @ResponseBody Map<String, String> graph(IncomeForecastForm form, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> data = new HashMap<String, String>();
        try {
            StatTable statTable = new StatTable();
            statTable.setChart_type("Area");
            statTable.setHeight(290);
            statTable.setName("收入增量预测");
            Double statResult = 0.0;
            String chart = null;
            String graphId = null;
            String html = "";
            graphId = "div_" + IdGen.uuid();
            // 获取通用报表查询数据
            List<CommonChartData> commonChartDatas = incomeForecastService.getForecastResult(form);
            chart = EChartsUtil.generateGraph(statTable, graphId, commonChartDatas);
            DecimalFormat df = new DecimalFormat(Formatter.DECIMAL_FORMAT10);
            for (int i = 0; i < commonChartDatas.size(); i++) {
                if (!CommonUtil.isNullorEmpty(commonChartDatas.get(i).getStat_result())) {
                    if ("1".equals(statTable.getNumber())) {
                        statResult += Double.parseDouble(df.format(commonChartDatas.get(i).getStat_result() / 10000));
                    } else if ("2".equals(statTable.getNumber())) {
                        statResult += Double.parseDouble(df.format(commonChartDatas.get(i).getStat_result() / 1000000));
                    } else if ("3".equals(statTable.getNumber())) {
                        statResult += Double.parseDouble(df.format(commonChartDatas.get(i).getStat_result() / 100000000));
                    } else {
                        statResult += commonChartDatas.get(i).getStat_result();
                    }
                }
            }

            html = "<div class='.bg-light' id='" + graphId + "' style='padding:15px;height:" + statTable.getHeight() + "px;width:100%' ></div>";

            // 生成报表图片
            data.put("result", "success");
            data.put("graphId", "graphId");
            data.put("graph", chart);
            data.put("html", html);
        } catch (Exception e) {
            data.put("result", "fail");
            data.put("message", e.getMessage());
        }
        return data;
    }
}
