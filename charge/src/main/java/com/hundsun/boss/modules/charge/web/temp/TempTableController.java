package com.hundsun.boss.modules.charge.web.temp;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.temp.TempTable;
import com.hundsun.boss.modules.charge.service.temp.TempTableService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 中间表Controller
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Controller
@RequestMapping(value = "${adminPath}/charge/temp/tempTable")
public class TempTableController extends BaseController {

    @Autowired
    private TempTableService tempTableService;

    @Autowired
    private OfficeService officeService;

    @ModelAttribute
    public TempTable get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return tempTableService.get(id);
        } else {
            return new TempTable();
        }
    }

    @RequiresPermissions("charge:temp:tempTable:view")
    @RequestMapping(value = { "list", "" })
    public String list(TempTable tempTable, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (CommonUtil.isNullorEmpty(tempTable)) {
            tempTable = new TempTable();
        }
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            tempTable.setCreateBy(user);
        }
        Page<TempTable> page = tempTableService.find(new Page<TempTable>(request, response), tempTable);
        model.addAttribute("tempTable", tempTable);
        model.addAttribute("office", officeService.findByCode(tempTable.getOffice_id()));
        model.addAttribute("page", page);
        return "modules/" + "charge/temp/tempTableList";
    }

    @RequiresPermissions("charge:temp:tempTable:view")
    @RequestMapping(value = "form")
    public String form(TempTable tempTable, Model model) {
        if (CommonUtil.isNullorEmpty(tempTable)) {
            tempTable = new TempTable();
        }
        model.addAttribute("office", officeService.findByCode(tempTable.getOffice_id()));
        model.addAttribute("tempTable", tempTable);
        return "modules/" + "charge/temp/tempTableForm";
    }

    @RequiresPermissions("charge:temp:tempTable:edit")
    @RequestMapping(value = "save")
    public String save(TempTable tempTable, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, tempTable)) {
            return form(tempTable, model);
        }
        tempTableService.save(tempTable);
        model.addAttribute("office", officeService.findByCode(tempTable.getOffice_id()));
        model.addAttribute("tempTable", tempTable);
        addMessage(model, "保存中间表'" + tempTable.getName() + "'成功");
        //return "redirect:" + Global.getAdminPath() + "/charge/temp/tempTable/?repage";
        return "modules/" + "charge/temp/tempTableForm";
    }

    @RequiresPermissions("charge:temp:tempTable:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        tempTableService.delete(id);
        addMessage(redirectAttributes, "删除中间表成功");
        return "redirect:" + Global.getAdminPath() + "/charge/temp/tempTable/?repage";
    }

    @RequestMapping(value = { "chooser" })
    public String chooser(TempTable tempTable, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (CommonUtil.isNullorEmpty(tempTable)) {
            tempTable = new TempTable();
        }
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            tempTable.setCreateBy(user);
        }
        Page<TempTable> page = tempTableService.find(new Page<TempTable>(request, response), tempTable);
        model.addAttribute("page", page);
        model.addAttribute("tempTable", tempTable);
        return "modules/" + "charge/temp/tempTableChooser";
    }

    @RequiresPermissions("charge:temp:tempTable:view")
    @RequestMapping(value = "plain")
    public @ResponseBody Map<String, String> plain(String id, String query_sql, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> data = new HashMap<String, String>();
        try {
            Map params = new HashMap();
            String query_sql_upper_case = query_sql.toUpperCase();
            //只能执行select查询语句，insert,update,delete，存储过程(call)
            //将sql语句转成大写
            if (!CommonUtil.isNullorEmpty(query_sql)) {
                if (query_sql_upper_case.contains("INSERT") || query_sql_upper_case.toUpperCase().contains("UPDATE") || query_sql_upper_case.contains("DELETE")
                        || query_sql_upper_case.contains("CALL") || query_sql_upper_case.contains("DROP") || query_sql_upper_case.contains("ALTER") || query_sql_upper_case.contains("TRUNCATE")) {
                    data.put("result", "fail");
                    data.put("message", "只能执行select语句！");
                    return data;
                }
            }
            params.put("query_sql", StringEscapeUtils.unescapeHtml(query_sql));
            // 获取通用报表查询数据
            List<LinkedHashMap<String, Object>> plainTableDatas = tempTableService.getPlainTableData(params);
            if (CommonUtil.isNullorEmpty(plainTableDatas)) {
                data.put("result", "fail");
                data.put("message", "查询结果为0条记录");
                return data;
            }
            StringBuffer sbTable = getPlainChart(plainTableDatas);
            data.put("table", sbTable.toString());
        } catch (Exception e) {
            data.put("result", "fail");
            data.put("message", e.getMessage());
        }
        return data;
    }

    /**
     * 根据数据库查询结果生成2维表
     * 
     * @param plainTableDatas
     * @return
     */
    private static StringBuffer getPlainChart(List<LinkedHashMap<String, Object>> plainTableDatas) {
        StringBuffer sbTable = new StringBuffer();
        Object displayValue = null;
        for (int i = 0; i < plainTableDatas.size(); i++) {
            if (i == 0) {
                sbTable.append("<br>");
                sbTable.append("<table class='table table-striped table-bordered table-condensed'>");
                sbTable.append("<thead>");
                for (Map.Entry<String, Object> entry : plainTableDatas.get(0).entrySet()) {
                    sbTable.append("<th width='80px'>" + entry.getKey() + "</th>");
                }
                sbTable.append("</thead>");
                sbTable.append("<tbody>");
            }
            sbTable.append("<tr>");
            for (Map.Entry<String, Object> entry : plainTableDatas.get(0).entrySet()) {
                displayValue = plainTableDatas.get(i).get(entry.getKey());
                if (displayValue instanceof Number) {
                    sbTable.append("<td>").append(new DecimalFormat("#.#").format(displayValue)).append("</td>");
                } else {
                    sbTable.append("<td>").append(displayValue).append("</td>");
                }
            }
            sbTable.append("</tr>");
        }
        sbTable.append("</tbody>");
        sbTable.append("</table>");
        return sbTable;
    }
}
