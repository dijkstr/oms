package com.hundsun.boss.modules.charge.web.order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.modules.charge.entity.order.OrderCombine;
import com.hundsun.boss.modules.charge.entity.order.OrderInfo;
import com.hundsun.boss.modules.charge.entity.sync.SyncContract;
import com.hundsun.boss.modules.charge.entity.sync.SyncCustomer;
import com.hundsun.boss.modules.charge.entity.sync.SyncProduct;
import com.hundsun.boss.modules.charge.form.common.SyncContractForm;
import com.hundsun.boss.modules.charge.form.common.SyncCustomerForm;
import com.hundsun.boss.modules.charge.form.common.SyncProductForm;
import com.hundsun.boss.modules.charge.form.order.OrderInfoForm;
import com.hundsun.boss.modules.charge.service.common.SyncChooserService;
import com.hundsun.boss.modules.charge.service.order.OrderInfoService;
import com.hundsun.boss.modules.charge.service.setting.ChargeModelService;
import com.hundsun.boss.modules.charge.service.setting.ChargeTypeService;
import com.hundsun.boss.modules.charge.service.setting.ClassifyService;
import com.hundsun.boss.modules.charge.service.sync.SyncContractService;
import com.hundsun.boss.modules.charge.service.sync.SyncCustomerService;
import com.hundsun.boss.modules.charge.service.sync.SyncProductService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/common")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CommonController extends BaseController {

    @Autowired
    private SyncChooserService syncChooserService;

    @Autowired
    private SyncContractService syncContractService;

    @Autowired
    private SyncCustomerService syncCustomerService;

    @Autowired
    private OfficeService officeService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private ClassifyService classifyService;

    @Autowired
    private ChargeTypeService chargeTypeService;

    @Autowired
    private ChargeModelService chargeModelService;
    
    @Autowired
    private SyncProductService syncProductService;

    /**
     * 协同产品选择器
     * 
     * @param form
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:order:orderInfo:edit")
    @RequestMapping(value = { "syncProductChooser" })
    public String syncProductChooser(SyncProductForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page defaultpage = new Page<SyncProductForm>(request, response);
        defaultpage.setPageSize(5);
        Page<SyncProductForm> page = syncChooserService.queryXtProds(defaultpage, form);
        model.addAttribute("page", page);
        model.addAttribute("syncProductForm", form);
        return "modules/" + "charge/order/syncProductChooser";
    }

    /**
     * 协同客户选择器
     * 
     * @param form
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:order:orderInfo:edit")
    @RequestMapping(value = { "syncCustomerChooser" })
    public String syncCustomerChooser(SyncCustomerForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page defaultpage = new Page<SyncCustomer>(request, response);
        defaultpage.setPageSize(5);
        Page<SyncCustomer> page = syncCustomerService.find(defaultpage, form);
        model.addAttribute("page", page);
        model.addAttribute("syncCustomerForm", form);
        return "modules/" + "charge/order/syncCustomerChooser";
    }

    /**
     * 协同合同选择器
     * 
     * @param form
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:order:orderInfo:edit")
    @RequestMapping(value = { "syncContractChooser" })
    public String syncContractChooser(SyncContractForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<SyncContract> defaultpage = new Page<SyncContract>(request, response);
        defaultpage.setPageSize(5);
        Page<SyncContract> page = syncContractService.find(defaultpage, form);
        model.addAttribute("page", page);
        model.addAttribute("syncContractForm", form);
        return "modules/" + "charge/order/syncContractChooser";
    }

    /**
     * 计费合同选择器
     * 
     * @param form
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:order:orderFrameInfo:view")
    @RequestMapping(value = { "chargeContractChooser" })
    public String list(OrderInfoForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            form.setCreateBy(user.getId());
        }
        Page<OrderInfo> defaultpage = new Page<OrderInfo>(request, response);
        defaultpage.setPageSize(5);
        Page<OrderInfo> page = orderInfoService.find(defaultpage, form);
        model.addAttribute("page", page);
        model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        return "modules/" + "charge/order/chargeContractChooser";
    }

    /**
     * 合同组合选择器
     * 
     * @param form
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:order:orderInfo:edit")
    @RequestMapping(value = { "orderCombineChooser" })
    public String orderCombineChooser(OrderInfoForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<OrderCombine> defaultpage = new Page<OrderCombine>(request, response);
        defaultpage.setPageSize(5);
        Page<OrderCombine> page = orderInfoService.getOrderCombine(defaultpage, form);
        model.addAttribute("page", page);
        model.addAttribute("office", officeService.findByCode(form.getOffice_id()));
        return "modules/" + "charge/order/orderCombineChooser";
    }
    
    /**
     * 协同产品选择器
     * 
     * @param form
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequiresPermissions("charge:order:orderInfo:edit")
    @RequestMapping(value = { "productChooser" })
    public String productChooser(SyncProductForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<SyncProduct> defaultpage = new Page<SyncProduct>(request, response);
        defaultpage.setPageSize(5);
        Page<SyncProduct> page = syncProductService.find(defaultpage, form);
        model.addAttribute("page", page);
        model.addAttribute("syncProductForm", form);
        return "modules/" + "charge/order/productChooser";
    }
}
