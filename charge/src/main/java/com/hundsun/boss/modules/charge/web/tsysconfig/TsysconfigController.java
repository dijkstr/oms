package com.hundsun.boss.modules.charge.web.tsysconfig;

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
import com.hundsun.boss.modules.charge.entity.tsysconfig.Tsysconfig;
import com.hundsun.boss.modules.charge.service.tsysconfig.TsysconfigService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;
/**
 * 系统参数设置controller
 * @author feigq
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/tsysconfig")
public class TsysconfigController extends BaseController{
    
    @Autowired
    private TsysconfigService tsysconfigService;
    /**系统参数配置
     * 获取
     * @param prop_name
     * @return
     */
    @ModelAttribute
    public Tsysconfig get(@RequestParam(required = false) String prop_name) {
        if (StringUtils.isNotBlank(prop_name)) {
            return tsysconfigService.get(prop_name);
        } else {
            return new Tsysconfig();
        }
    }
    
    /**
     * 列表显示系统配置参数
     * @param tsysconfig
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:tsysconfig:view")
    @RequestMapping(value = { "list", "" })
    public String list(Tsysconfig tsysconfig, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {

        }       
        Page<Tsysconfig> page = tsysconfigService.find(new Page<Tsysconfig>(request, response), tsysconfig);    
        
        model.addAttribute("page", page);

        return "modules/" + "charge/tsysconfig/tsysconfigList";
    }
    /**
     * 系统参数配置form
     * @param tsysconfig
     * @param model
     * @return
     */
    @RequiresPermissions("charge:tsysconfig:view")
    @RequestMapping(value = "form")
    public String form(Tsysconfig tsysconfig, Model model) {
        model.addAttribute("tsysconfig", tsysconfig);
        return "modules/" + "charge/tsysconfig/tsysconfigForm";
    }
    
   /**
    * 保存系统参数配置 
    * @param tsysconfig
    * @param model
    * @param redirectAttributes
    * @return
    */
    @RequiresPermissions("charge:tsysconfig:edit")
    @RequestMapping(value = "save")
    public String save(String prop_name, String prop_code,String remarks, 
            Model model, RedirectAttributes redirectAttributes) {
     //查询系统参数配置对象
        Tsysconfig tsysconfig=tsysconfigService.get(prop_name);
        //系统参数存在，修改系统参数值，以及参数备注说明
        if(!CommonUtil.isNullorEmpty(tsysconfig)){
            tsysconfig.setProp_code(prop_code);
            tsysconfig.setRemarks(remarks);
        }else{
          //系统参数不存在，添加系统参数对象
            tsysconfig=new  Tsysconfig();
            tsysconfig.setProp_name(prop_name);
            tsysconfig.setProp_code(prop_code);
            tsysconfig.setRemarks(remarks);
        }
        tsysconfigService.save(tsysconfig);
        addMessage(redirectAttributes, "保存系统参数配置成功");
        return "redirect:" + Global.getAdminPath() + "/charge/tsysconfig/?repage";
    }
    
    
   /**
    * 删除系统参数配置 
    * @param prop_name
    * @param redirectAttributes
    * @return
    */
    @RequiresPermissions("charge:tsysconfig:edit")
    @RequestMapping(value = "delete")
    public String delete(String prop_name, RedirectAttributes redirectAttributes) {
        tsysconfigService.delete(prop_name);
        addMessage(redirectAttributes, "删除系统参数配置成功");
        return "redirect:" + Global.getAdminPath() + "/charge/tsysconfig/?repage";
    }

}
