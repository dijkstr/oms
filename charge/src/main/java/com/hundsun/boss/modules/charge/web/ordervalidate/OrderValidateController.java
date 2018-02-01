package com.hundsun.boss.modules.charge.web.ordervalidate;

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
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.ordervalidate.OrderValidate;
import com.hundsun.boss.modules.charge.form.ordervalidate.OrderValidateForm;
import com.hundsun.boss.modules.charge.service.ordervalidate.OrderValidateService;
import com.hundsun.boss.modules.charge.service.sync.SyncContractService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.service.SysConfigService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 合同校验Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/order/validate")

public class OrderValidateController extends BaseController{
    @Autowired
    private OrderValidateService orderValidateService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private SyncContractService syncContractService;
    @Autowired
    private SysConfigService sysConfigService;
/**
 * 获取合同校验信息
 * @param serial_no
 * @return
 */
    @ModelAttribute
    public OrderValidate get(@RequestParam(required = false) String serial_no) {       
        
        if (StringUtils.isNotBlank(serial_no)) {
            return orderValidateService.get(serial_no);
        } else {
            return new OrderValidate();
        }
    }
/**
 * 列表分页显示合同校验信息
 * @param orderValidate
 * @param request
 * @param response
 * @param model
 * @return
 */
    @RequiresPermissions("charge:order:validate:view")
    @RequestMapping(value = { "list", "" })
    public String list(OrderValidateForm orderValidate, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
           
        }
        model.addAttribute("office", officeService.findByCode(orderValidate.getOrder_source()));
        Page<OrderValidate> page = orderValidateService.find(new Page<OrderValidate>(request, response), orderValidate);
        model.addAttribute("page", page);
        return "modules/" + "charge/validate/orderValidateList";
    }
/**
 * 表单显示合同校验信息
 * @param orderValidate
 * @param model
 * @return
 */
    @RequiresPermissions("charge:order:validate:view")
    @RequestMapping(value = "form")
    public @ResponseBody Map<String, Object> form(String serial_no,String status,String remark,
            Model model,RedirectAttributes redirectAttributes) {
        Map<String, Object> data = new HashMap<String, Object>();
        //原来的对象
        try{
        OrderValidate orderValidate1= orderValidateService.get(serial_no);
        if(!CommonUtil.isNullorEmpty(orderValidate1)){
            //设置备注
            orderValidate1.setRemark(remark);
            //设置显示状态
            orderValidate1.setShow_status(status);
            //更新对象
            orderValidateService.save(orderValidate1);
            data.put("result", "success");
            if(status.equals("0")){
                data.put("message", "校验信息显示成功！");
            }else{
                data.put("message", "校验信息过滤成功！");
            }
           
        }
           
        }catch(Exception e){
            logger.error(e.getMessage());
            data.put("result", "fail");
            if(status.equals("0")){
                data.put("message", "校验信息显示失败！");
            }else{
                data.put("message", "校验信息过滤失败！");
            }
        }
        
        return data;
    }
    /**
     * 合同校验调用存储过程
     * @param model
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("charge:order:validate:view")
    @RequestMapping(value = "ordervalidate")
    public @ResponseBody Map<String, Object> orderValidate(Model model,RedirectAttributes redirectAttributes) {
        Map<String, Object> data = new HashMap<String, Object>();
        //原来的对象
        try{
            //调用合同校验存储过程，全量校验
            syncContractService.validateContract(null);
            
            data.put("result", "success");
          
            data.put("message", "调用合同校验存储过程成功，稍后会刷新界面！");
           
        }catch(Exception e){
            logger.error(e.getMessage());
            data.put("result", "fail");
           
            data.put("message", "调用合同校验存储过程失败！");
           
        }
        
        return data;
    }
   

  /**
   * 删除合同校验信息  
   * @param serial_no
   * @param redirectAttributes
   * @return
   */
    @RequiresPermissions("charge:order:validate:edit")
    @RequestMapping(value = "delete")
    public String delete(String serial_no, RedirectAttributes redirectAttributes) {
        orderValidateService.delete(serial_no);
        addMessage(redirectAttributes, "删除合同校验");
        return "redirect:" + Global.getAdminPath() + "/charge/order/validate/?repage";
    }


}
