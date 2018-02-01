package com.hundsun.boss.modules.charge.web.bill;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.modules.charge.form.bill.ChargeUnifyInterface;
import com.hundsun.boss.modules.charge.service.bill.ChargeUnifyInterfaceService;
import com.hundsun.boss.modules.sys.service.OfficeService;

/**
 * 统一接口数据Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/bill/chargeUnifyInterface")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ChargeUnifyInterfaceController extends BaseController {

    @Autowired
    private ChargeUnifyInterfaceService chargeUnifyInterfaceService;

    @Autowired
    private OfficeService officeService;

    @RequiresPermissions("charge:bill:chargeUnifyInterface:view")
    @RequestMapping(value = {"" })
    public String enterList(ChargeUnifyInterface chargeUnifyInterface, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ChargeUnifyInterface> page = new Page<ChargeUnifyInterface>(request, response);
        model.addAttribute("page", page);
        model.addAttribute("chargeUnifyInterface", chargeUnifyInterface);
        model.addAttribute("office", officeService.findByCode(chargeUnifyInterface.getOffice_id()));
        return "modules/" + "charge/bill/chargeUnifyInterfaceList";
    }
    
    @RequiresPermissions("charge:bill:chargeUnifyInterface:view")
    @RequestMapping(value = { "list"})
    public String queryList(ChargeUnifyInterface chargeUnifyInterface, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page defaultpage = new Page<ChargeUnifyInterface>(request, response);
        chargeUnifyInterface.setPageNo(defaultpage.getPageNo());
        chargeUnifyInterface.setPageSize(defaultpage.getPageSize());
        Page<ChargeUnifyInterface> page = chargeUnifyInterfaceService.queryBusiDataPage(defaultpage, chargeUnifyInterface);
        model.addAttribute("page", page);
        model.addAttribute("chargeUnifyInterface", chargeUnifyInterface);
        model.addAttribute("office", officeService.findByCode(chargeUnifyInterface.getOffice_id()));
        return "modules/" + "charge/bill/chargeUnifyInterfaceList";
    }

}
