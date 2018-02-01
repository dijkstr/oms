package com.hundsun.boss.modules.charge.web.bill;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.bill.OrderRelation;
import com.hundsun.boss.modules.charge.form.bill.RelationForm;
import com.hundsun.boss.modules.charge.service.bill.RelationService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 账单联系人Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/bill/relation")
public class RelationController extends BaseController {

    @Autowired
    private RelationService relationService;

    @Autowired
    private OfficeService officeService;

    @ModelAttribute
    public OrderRelation get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return relationService.get(id);
        } else {
            return new OrderRelation();
        }
    }

    @RequiresPermissions("charge:bill:relation:view")
    @RequestMapping(value = { "list", "" })
    public String list(RelationForm relationForm, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            relationForm.setCreateBy(user.getId());
        }
        Page<OrderRelation> page = relationService.find(new Page<OrderRelation>(request, response), relationForm);
        model.addAttribute("page", page);
        model.addAttribute("office", officeService.findByCode(relationForm.getOffice_id()));
        return "modules/" + "charge/bill/relationList";
    }

    @RequiresPermissions("charge:bill:relation:view")
    @RequestMapping(value = "form")
    public String form(RelationForm relationForm, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (relationForm.getId() == null) {
            } else if (!CommonUtil.isNullorEmpty(relationForm.getId()) && CommonUtil.isNullorEmpty(relationForm.getUpdateDate())) {
                OrderRelation orderRelation = get(relationForm.getId());
                FormUtils.setPropertyValue(relationForm, orderRelation);
                orderRelation.setUpdateDate(relationForm.getUpdateDate());
            } else {
            }
            model.addAttribute("relationForm", relationForm);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "显示联系方式失败");
            model.addAttribute("relationForm", relationForm);
        }
        return "modules/" + "charge/bill/relationForm";
    }

    @RequiresPermissions("charge:bill:relation:edit")
    @RequestMapping(value = "save")
    public String save(RelationForm relation, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (!beanValidator(model, relation)) {
                return form(relation, model, redirectAttributes);
            }
            OrderRelation orderRelation = new OrderRelation();
            // 如果是更新，获取最新的计费模式信息
            if (!CommonUtil.isNullorEmpty(relation.getId())) {
                orderRelation = relationService.get(relation.getId());
                // 如果有人更新内容，不允许修改
                if (!CommonUtil.isNullorEmpty(orderRelation.getUpdateDate()) && !orderRelation.getUpdateDate().equals(relation.getUpdateDate())) {
                    addMessage(model, "验证失败：<br/>联络方式：" + relation.getRelation_name() + "已经被人修改，请确认");
                    return form(relation, model, redirectAttributes);
                }
            }
            FormUtils.margeFormToEntity(relation, orderRelation);
            relationService.save(orderRelation);
            addMessage(redirectAttributes, "保存账单联系人成功");
            return "redirect:" + Global.getAdminPath() + "/charge/bill/relation/?repage";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "联络方式：" + relation.getRelation_name() + "'失败");
            model.addAttribute("relation", relation);
            return "modules/" + "charge/bill/relationForm";
        }
    }

    @RequiresPermissions("charge:bill:relation:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        relationService.delete(id);
        addMessage(redirectAttributes, "删除账单联系人成功");
        return "redirect:" + Global.getAdminPath() + "/charge/bill/relation/?repage";
    }

    /**
     * 根据协同合同编号，查看联系方式是否存在
     * 
     * @param contract_id
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequiresPermissions("charge:bill:relation:view")
    @RequestMapping(value = "/exist")
    public @ResponseBody Map<String, String> exist(String contract_id, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        try {
           if(!CommonUtil.isNullorEmpty(relationService.getByContract_id(contract_id))){
               data.put("result", "success");
           }else{
               data.put("result", "fail");
           }
        } catch (Exception e) {
            logger.error("error:", e);
            CommonUtil.exceptionHandler(data, e);
        } finally {
        }
        return data;
    }
}
