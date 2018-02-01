package com.hundsun.boss.modules.sys.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
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
import com.hundsun.boss.modules.sys.entity.Office;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.service.SystemService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 机构Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/office")
public class OfficeController extends BaseController {

    @Autowired
    private OfficeService officeService;

    @Autowired
    private SystemService SystemService;

    @ModelAttribute("office")
    public Office get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return officeService.get(id);
        } else {
            return new Office();
        }
    }

    @RequiresPermissions("sys:office:view")
    @RequestMapping({ "list", "" })
    public String list(Office office, Model model) {
        //		User user = UserUtils.getUser();
        //		if(user.isAdmin()){
        office.setId("1");
        //		}else{
        //			office.setId(user.getOffice().getId());
        //		}
        model.addAttribute("office", office);
        List<Office> list = Lists.newArrayList();
        List<Office> sourcelist = officeService.findAll();
        Office.sortList(list, sourcelist, office.getId());
        model.addAttribute("list", list);
        return "modules/sys/officeList";
    }

    @RequiresPermissions("sys:office:view")
    @RequestMapping("form")
    public String form(Office office, Model model) {
        User user = UserUtils.getUser();
        if (office.getParent() == null || office.getParent().getId() == null) {
            office.setParent(user.getOffice());
        }
        office.setParent(officeService.get(office.getParent().getId()));
        if (office.getArea() == null) {
            office.setArea(office.getParent().getArea());
        }
        model.addAttribute("office", office);
        return "modules/sys/officeForm";
    }

    @RequiresPermissions("sys:office:edit")
    @RequestMapping("save")
    public String save(Office office, Model model, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + Global.getAdminPath() + "/sys/office/";
        }

        if (!beanValidator(model, office)) {
            return form(office, model);
        }
        officeService.save(office);
        addMessage(redirectAttributes, "保存机构'" + office.getName() + "'成功");
        return "redirect:" + Global.getAdminPath() + "/sys/office/";
    }

    @RequiresPermissions("sys:office:edit")
    @RequestMapping("delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + Global.getAdminPath() + "/sys/office/";
        }

        if (Office.isRoot(id)) {
            addMessage(redirectAttributes, "删除机构失败, 不允许删除顶级机构或编号空");
        } else {
            officeService.delete(id);
            addMessage(redirectAttributes, "删除机构成功");
        }
        return "redirect:" + Global.getAdminPath() + "/sys/office/";
    }

    @RequiresUser
    @ResponseBody
    @RequestMapping("treeData")
    public List<Map<String, Object>> treeData(HttpServletResponse response, @RequestParam(required = false) String extId, @RequestParam(required = false) Long type, @RequestParam(required = false) Long grade) {

        response.setContentType("application/json; charset=UTF-8");
        List<Map<String, Object>> mapList = Lists.newArrayList();

        //		User user = UserUtils.getUser();
        List<Office> list = officeService.findAll();
        for (int i = 0; i < list.size(); i++) {
            Office e = list.get(i);

            if ((extId == null || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1))
                    && (type == null || (type != null && Integer.parseInt(e.getType()) <= type.intValue())) && (grade == null || (grade != null && Integer.parseInt(e.getGrade()) <= grade.intValue()))) {

                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                //				map.put("pId", !user.isAdmin() && e.getId().equals(user.getOffice().getId())?0:e.getParent()!=null?e.getParent().getId():0);
                map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
                map.put("name", e.getName());
                map.put("code", e.getCode());
                mapList.add(map);
            }
        }
        return mapList;
    }
    
    @ResponseBody
    @RequestMapping("codeUniqueValidate")
    public Map<String, Object> codeUniqueValidate( @RequestParam(required = false) String id, @RequestParam(required = false) String code) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result", "success");
        if(CommonUtil.isNullorEmpty(code)){
            map.put("result", "failure");
            map.put("message", "机构编码长度需要在1和100之间");
            return map;
        }
        Office codeOffice = officeService.findByCode(code);
        //新增
        if(CommonUtil.isNullorEmpty(id)){
            if(!CommonUtil.isNullorEmpty(codeOffice)){
                map.put("result", "failure");
                map.put("message", "机构编码'" + code + "'已经存在");
            }
        }
        //修改
        else{
            if(!CommonUtil.isNullorEmpty(codeOffice) && !(codeOffice.getId()).equals(id)){
                map.put("result", "failure");
                map.put("message", "机构编码'" + code + "'已经存在");
            }
        }
        return map;
    }

    @RequestMapping("deleteOfficeValidate")
    @ResponseBody
    public Map<String, Object> deleteOfficeValidate(@RequestParam(required = false) String id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("result", "success");
        if (!CommonUtil.isNullorEmpty(id)) {
            //查询机构及子机构
            List<Office> offices = officeService.findByParentIdsLike(id);
            Office officeSelf = officeService.get(id);
            offices.add(officeSelf);
            if (!CommonUtil.isNullorEmpty(offices)) {
                for (int i = 0; i < offices.size(); i++) {
                    Office office = offices.get(i);
                    //通过office_id查用户
                    List<User> userList = SystemService.findUsersByOffice(office.getId());
                    if (!CommonUtil.isNullorEmpty(userList)) {
                        map.put("result", "failure");
                        map.put("message", "所属机构下存在用户，请确认！");
                        return map;
                    }
                    //通过office_code查分类
//                    if (!CommonUtil.isNullorEmpty(classifyService.findListByOfficeCode(office.getCode()))) {
//                        map.put("result", "failure");
//                        map.put("message", "所属机构下存在分类，请确认！");
//                        return map;
//                    }
//                    //通过office_code查计费类型
//                    if (!CommonUtil.isNullorEmpty(chargeTypeService.findListByOfficeCode(office.getCode()))) {
//                        map.put("result", "failure");
//                        map.put("message", "所属机构下存在计费类型，请确认！");
//                        return map;
//                    }
//                    
//                    //通过office_code查合同
//                    if (!CommonUtil.isNullorEmpty(orderInfoService.findListByOfficeCode(office.getCode()))) {
//                        map.put("result", "failure");
//                        map.put("message", "所属机构下存在合同，请确认！");
//                        return map;
//                    } 
                }
            }
        }
        return map;
    }
}
