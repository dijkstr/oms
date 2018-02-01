package com.hundsun.boss.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.sys.entity.Sysconfig;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.SysConfigService;
import com.hundsun.boss.modules.sys.utils.UserUtils;
/**
 * 系统参数设置controller
 * @author feigq
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/sysconfig")
public class SysconfigController extends BaseController{
    
    @Autowired
    private SysConfigService sysconfigService;
    /**系统参数配置
     * 获取
     * @param prop_name
     * @return
     */
    @ModelAttribute
    public Sysconfig get(@RequestParam(required = false) String prop_name) {
        if (StringUtils.isNotBlank(prop_name)) {
            return sysconfigService.get(prop_name);
        } else {
            return new Sysconfig();
        }
    }
    
    /**
     * 列表显示系统配置参数
     * @param sysconfig
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("sys:sysconfig:view")
    @RequestMapping(value = { "list", "" })
    public String list(Sysconfig sysconfig, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {

        }       
        Page<Sysconfig> page = sysconfigService.find(new Page<Sysconfig>(request, response), sysconfig);    
        
        model.addAttribute("page", page);

        return "modules/" + "sys/sysconfig/sysconfigList";
    }
    /**
     * 系统参数配置form
     * @param sysconfig
     * @param model
     * @return
     */
    @RequiresPermissions("sys:sysconfig:view")
    @RequestMapping(value = "form")
    public String form(Sysconfig sysconfig, Model model) {
        model.addAttribute("sysconfig", sysconfig);
        return "modules/" + "sys/sysconfig/sysconfigForm";
    }
    
   /**
    * 保存系统参数配置 
    * @param sysconfig
    * @param model
    * @param redirectAttributes
    * @return
    */
    @RequiresPermissions("sys:sysconfig:edit")
    @RequestMapping(value = "save")
    public String save(String prop_name, String prop_code,String remarks, 
            Model model, RedirectAttributes redirectAttributes) {
     //查询系统参数配置对象
        Sysconfig sysconfig=sysconfigService.get(prop_name);
        //系统参数存在，修改系统参数值，以及参数备注说明
        if(!CommonUtil.isNullorEmpty(sysconfig)){
            sysconfig.setProp_code(prop_code);
            sysconfig.setRemarks(remarks);
        }else{
          //系统参数不存在，添加系统参数对象
            sysconfig=new  Sysconfig();
            sysconfig.setProp_name(prop_name);
            sysconfig.setProp_code(prop_code);
            sysconfig.setRemarks(remarks);
        }
        sysconfigService.save(sysconfig);
        addMessage(redirectAttributes, "保存系统参数配置成功");
        return "redirect:" + Global.getAdminPath() + "/sys/sysconfig/?repage";
    }
    
    
   /**
    * 删除系统参数配置 
    * @param prop_name
    * @param redirectAttributes
    * @return
    */
    @RequiresPermissions("sys:sysconfig:edit")
    @RequestMapping(value = "delete")
    public String delete(String prop_name, RedirectAttributes redirectAttributes) {
    	sysconfigService.delete(prop_name);
        addMessage(redirectAttributes, "删除系统参数配置成功");
        return "redirect:" + Global.getAdminPath() + "/sys/sysconfig/?repage";
    }

}
