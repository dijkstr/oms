package com.hundsun.boss.modules.sys.web;

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
import com.hundsun.boss.modules.sys.entity.Area;
import com.hundsun.boss.modules.sys.entity.Office;
import com.hundsun.boss.modules.sys.service.AreaService;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 区域Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/area")
public class AreaController extends BaseController {

    @Autowired
    private AreaService areaService;
    
    @Autowired
    private OfficeService officeService;

    @ModelAttribute("area")
    public Area get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return areaService.get(id);
        } else {
            return new Area();
        }
    }

    @RequiresPermissions("sys:area:view")
    @RequestMapping(value = { "list", "" })
    public String list(Area area, Model model) {
        //		User user = UserUtils.getUser();
        //		if(user.isAdmin()){
        area.setId("1");
        //		}else{
        //			area.setId(user.getArea().getId());
        //		}
        model.addAttribute("area", area);
        List<Area> list = Lists.newArrayList();
        List<Area> sourcelist = areaService.findAll();
        Area.sortList(list, sourcelist, area.getId());
        model.addAttribute("list", list);
        return "modules/sys/areaList";
    }

    @RequiresPermissions("sys:area:view")
    @RequestMapping(value = "form")
    public String form(Area area, Model model) {
        if (area.getParent() == null || area.getParent().getId() == null) {
            area.setParent(UserUtils.getUser().getOffice().getArea());
        }
        area.setParent(areaService.get(area.getParent().getId()));
        model.addAttribute("area", area);
        return "modules/sys/areaForm";
    }

    @RequiresPermissions("sys:area:edit")
    @RequestMapping(value = "save")
    public String save(Area area, Model model, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + Global.getAdminPath() + "/sys/area";
        }
        if (!beanValidator(model, area)) {
            return form(area, model);
        }
        areaService.save(area);
        addMessage(redirectAttributes, "保存区域'" + area.getName() + "'成功");
        return "redirect:" + Global.getAdminPath() + "/sys/area/";
    }

    @RequiresPermissions("sys:area:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + Global.getAdminPath() + "/sys/area";
        }
        if (Area.isAdmin(id)) {
            addMessage(redirectAttributes, "删除区域失败, 不允许删除顶级区域或编号为空");
        } else {
            List<String> ids= areaService.findAreasById(id);
            List<Office> offices = officeService.findListByAreaIds(ids);
            if(CommonUtil.isNullorEmpty(offices)){
                areaService.delete(id);
                addMessage(redirectAttributes, "删除区域成功");
            }else{
                addMessage(redirectAttributes, "区域下存在机构，请确认"); 
            } 
        }
        return "redirect:" + Global.getAdminPath() + "/sys/area/";
    }

    @RequiresUser
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, HttpServletResponse response) {
        response.setContentType("application/json; charset=UTF-8");
        List<Map<String, Object>> mapList = Lists.newArrayList();
        //		User user = UserUtils.getUser();
        List<Area> list = areaService.findAll();
        for (int i = 0; i < list.size(); i++) {
            Area e = list.get(i);
            if (extId == null || (extId != null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                //				map.put("pId", !user.isAdmin()&&e.getId().equals(user.getArea().getId())?0:e.getParent()!=null?e.getParent().getId():0);
                map.put("pId", e.getParent() != null ? e.getParent().getId() : 0);
                map.put("name", e.getName());
                mapList.add(map);
            }
        }
        return mapList;
    }
}
