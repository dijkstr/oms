package com.hundsun.boss.modules.charge.web.sql;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.form.sql.ChargeSqlForm;
import com.hundsun.boss.modules.charge.service.sql.ChargeSqlService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 计费sql语句查询controller
 * 
 * @author feigq
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/manager/sql")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ChargeSqlController extends BaseController {
    @Autowired
    private ChargeSqlService chargeSqlService;

    /**
     * sql查询界面
     * 
     * @param form
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:manager:sql:view")
    @RequestMapping(value = { "list", "" })
    public String list(ChargeSqlForm chargeSqlForm, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {

        }
        //只能执行select查询语句，insert,update,delete，存储过程(call)
        //将sql语句转成大写
        if (!CommonUtil.isNullorEmpty(chargeSqlForm.getSql())) {
            String upperSql = chargeSqlForm.getSql().toUpperCase();
            if (upperSql.contains("INSERT") || upperSql.contains("UPDATE") 
                    || upperSql.contains("DELETE") || upperSql.contains("CALL") 
                    || upperSql.contains("DROP") || upperSql.contains("ALTER")
                    || upperSql.contains("TRUNCATE")) {
                addMessage(model, "只能执行select语句！");
                return "modules/" + "charge/manager/chargeSqlList";
            }
        }
        
        Page defaultpage = new Page<ChargeSqlForm>(request, response);
        chargeSqlForm.setPageNo(defaultpage.getPageNo());
        chargeSqlForm.setPageSize(defaultpage.getPageSize());
        Page<List> page = chargeSqlService.find(defaultpage, chargeSqlForm);
        model.addAttribute("page", page);
        return "modules/" + "charge/manager/chargeSqlList";
    }

}
