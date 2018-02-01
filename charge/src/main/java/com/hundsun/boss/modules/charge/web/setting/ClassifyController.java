package com.hundsun.boss.modules.charge.web.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.setting.ChargeModel;
import com.hundsun.boss.modules.charge.entity.setting.Classify;
import com.hundsun.boss.modules.charge.form.setting.ClassifyForm;
import com.hundsun.boss.modules.charge.service.setting.ChargeModelService;
import com.hundsun.boss.modules.charge.service.setting.ClassifyService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 计费分类Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/setting/classify")
public class ClassifyController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ClassifyService classifyService;

    @Autowired
    private OfficeService officeService;
    
    @Autowired
    private ChargeModelService chargeModelService;
    
    @ModelAttribute
    public Classify get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return classifyService.get(id);
        } else {
            return new Classify();
        }
    }

    @RequiresPermissions("charge:setting:classify:view")
    @RequestMapping(value = { "list", "" })
    public String list(ClassifyForm classify, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            classify.setCreateBy(user.getId());
        }
        List<Classify> list = Lists.newArrayList();
        List<Classify> sourcelist = classifyService.findAllMenu();
        List<Classify> commonMenulist = classifyService.findCommonMenu();
        sourcelist.addAll(commonMenulist);
        
        Classify.sortList(list, sourcelist, "1");
        model.addAttribute("list", list);
        return "modules/" + "charge/setting/classifyList";
    }

    @RequiresPermissions("charge:setting:classify:view")
    @RequestMapping(value = "form")
    public String form(ClassifyForm classifyForm, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (!CommonUtil.isNullorEmpty(classifyForm.getId())) {
                //如果存在id，在画面上再现数据
                Classify classify = classifyService.get(classifyForm.getId());
                BeanUtils.copyProperties(classifyForm, classify);
                model.addAttribute("classify", classifyForm);
                if (!CommonUtil.isNullorEmpty(classify) && !CommonUtil.isNullorEmpty(classify.getClassify_parent())) {
                    model.addAttribute("classify_parent", classifyService.get(classify.getClassify_parent()));
                }
                if (!CommonUtil.isNullorEmpty(classify) && !CommonUtil.isNullorEmpty(classify.getOffice_id())) {
                    model.addAttribute("office",officeService.findByCode(classify.getOffice_id()));
                }
            } else if (!CommonUtil.isNullorEmpty(classifyForm.getClassify_parent())) {
                //新增下级分类
                model.addAttribute("classify", classifyForm);
                Classify classify_parent = classifyService.get(classifyForm.getClassify_parent());
                model.addAttribute("classify_parent",classify_parent );
                model.addAttribute("office",classify_parent.getOffice());
            } else {
                //新增
                model.addAttribute("classify", new ClassifyForm());
                model.addAttribute("classify_parent", classifyService.get("1"));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "显示分类失败");
            model.addAttribute("classify", classifyForm);
        }
        return "modules/" + "charge/setting/classifyForm";
    }

    @RequiresPermissions("charge:setting:classify:edit")
    @RequestMapping(value = "save")
    public String save(ClassifyForm classifyForm, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (!beanValidator(model, classifyForm)) {
                return form(classifyForm, model, redirectAttributes);
            }
            
            Classify classify = new Classify();
            // 如果是更新，获取最新的计费模式信息
            if (!CommonUtil.isNullorEmpty(classifyForm.getId())) {
                classify = classifyService.get(classifyForm.getId());
                // 如果有人更新内容，不允许修改
                if (!classify.getUpdateDate().equals(classifyForm.getUpdateDate())) {
                    addMessage(model, "验证失败：<br/>计费分类：" + classifyForm.getClassify_name() + "已经被人修改，请确认");
                    return form(classifyForm, model, redirectAttributes);
                }
                //分类下存在计费模式，不能修改所属部门
                List<String> ids = new ArrayList<String>();
                ids.add(classifyForm.getId());
                List<ChargeModel> chargeModelList = chargeModelService.findListByClassify(ids);
                if(!CommonUtil.isNullorEmpty(chargeModelList) && !(classifyForm.getOffice_id()).equals(classify.getOffice_id())){
                    addMessage(model, "验证失败：<br/>计费分类下存在计费模式，不能修改所属部门");
                    return form(classifyForm, model, redirectAttributes);
                }
            }
            
         // 设定画面提交内容
            BeanUtils.copyProperties(classify, classifyForm);
            Classify parentClassify = classifyService.get(classifyForm.getClassify_parent());
            classify.setParent_ids(parentClassify.getParent_ids()+parentClassify.getId()+",");
            classifyService.save(classify);
            addMessage(redirectAttributes, "保存计费分类'" + classify.getClassify_name() + "'成功");
            return "redirect:" + Global.getAdminPath() + "/charge/setting/classify/";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "保存计费分类'" + classifyForm.getClassify_name() + "'失败");
            model.addAttribute("classify", classifyForm);
            return "modules/" + "charge/setting/classifyForm";
        }
    }

    @RequiresPermissions("charge:setting:classify:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        List<String> ids = classifyService.findClassifysById(id);
        List<ChargeModel> chargeModelList = chargeModelService.findListByClassify(ids);
        if(CommonUtil.isNullorEmpty(chargeModelList)){
            classifyService.delete(id);
            addMessage(redirectAttributes, "删除计费分类成功");
        }else{
            addMessage(redirectAttributes, "验证失败：<br/>计费分类下存在计费模式，请确认");
        }
        return "redirect:" + Global.getAdminPath() + "/charge/setting/classify/";
    }

    @RequiresUser
    @ResponseBody
    @RequestMapping("treeData")
    public List<Map<String, Object>> treeData(HttpServletResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Classify> list = Lists.newArrayList();
        List<Classify> sourcelist = classifyService.findAllMenu();
        List<Classify> commonMenulist = classifyService.findCommonMenu();
        sourcelist.addAll(commonMenulist);
        Classify.sortList(list,sourcelist, "1");
        for (int i = 0; i < list.size(); i++) {
            Classify e = list.get(i);
            Map<String, Object> map = Maps.newHashMap();
            map.put("id", e.getId());
            map.put("pId", e.getClassify_parent());
            map.put("name", e.getClassify_name());
            mapList.add(map);
        }
        return mapList;
    }
  
}
