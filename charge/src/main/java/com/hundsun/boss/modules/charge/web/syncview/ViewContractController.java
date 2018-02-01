package com.hundsun.boss.modules.charge.web.syncview;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.HtmlUtils;

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.sync.SyncContdetail;
import com.hundsun.boss.modules.charge.entity.sync.SyncContract;
import com.hundsun.boss.modules.charge.entity.sync.SyncProduct;
import com.hundsun.boss.modules.charge.form.common.SyncContractDetailForm;
import com.hundsun.boss.modules.charge.form.common.SyncContractForm;
import com.hundsun.boss.modules.charge.service.sync.SyncContdetailService;
import com.hundsun.boss.modules.charge.service.sync.SyncContractService;
import com.hundsun.boss.modules.charge.service.sync.SyncProductService;
import com.hundsun.boss.modules.sys.service.OfficeService;

/**
 * 合同主表Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/sync/contract")
public class ViewContractController extends BaseController {
    
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private SyncContractService syncContractService;
    @Autowired
    private SyncContdetailService SyncContdetailService;
    @Autowired
    private SyncProductService syncProductService;
    @Autowired
    private OfficeService officeService;
/**
 * 获取合同主表
 * @param con_id
 * @return
 */
    @ModelAttribute
    public SyncContract get(@RequestParam(required = false) String con_id) {
        if (StringUtils.isNotBlank(con_id)) {
            return syncContractService.get(con_id);
        } else {
            return new SyncContract();
        }
    }
/**
 * 列表分页展示合同主表信息
 * @param form
 * @param request
 * @param response
 * @param model
 * @return
 */
    @RequiresPermissions("charge:sync:contract:view")
    @RequestMapping(value = { "list", "" })
    public String list(SyncContractForm form, HttpServletRequest request, HttpServletResponse response, Model model) {
//        User user = UserUtils.getUser();
        model.addAttribute("office", officeService.findByCode(form.getCompanyid()));
        Page<SyncContract> page = syncContractService.find(new Page<SyncContract>(request, response), form);        
        
        model.addAttribute("page", page);

        return "modules/" + "charge/sync/syncContractList";
    }
/**
 * 表单显示合同信息
 * @param form
 * @param model
 * @param redirectAttributes
 * @return
 */
    @RequiresPermissions("charge:sync:contract:view")
    @RequestMapping(value = "form")
    public String form(SyncContractForm form, Model model, RedirectAttributes redirectAttributes) {
        SyncContract syncContract = null;
        try {
            if (!CommonUtil.isNullorEmpty(form.getCon_id())) {
                //对参数进行处理
                String con_id=HtmlUtils.htmlUnescape(form.getCon_id());
                syncContract = syncContractService.get(URLDecoder.decode(con_id, "UTF-8"));
                
               
            }
            if(!CommonUtil.isNullorEmpty(form.getContractid())){
                //对参数进行处理
                String contractid=HtmlUtils.htmlUnescape(form.getContractid());
                syncContract = syncContractService.getByContractid(URLDecoder.decode(contractid, "UTF-8"));
            }
            FormUtils.setPropertyValue(form, syncContract);
            form.getDetails().addAll(convertSetToList(syncContract.getDetails()));
            model.addAttribute("syncContractForm", form);
            return "modules/" + "charge/sync/syncContractForm";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        //        model.addAttribute("syncContract", syncContract);
        return "modules/" + "charge/sync/syncContractForm";
    }

/**
 * 删除合同主表
 * @param con_id
 * @param redirectAttributes
 * @return
 */
    @RequiresPermissions("charge:setting:syncContract:edit")
    @RequestMapping(value = "delete")
    public String delete(String con_id, RedirectAttributes redirectAttributes) {
        syncContractService.delete(con_id);
        addMessage(redirectAttributes, "删除合同主表成功");
        return "redirect:" + Global.getAdminPath() + "/charge/sync/contract/?repage";
    }

    /**
     * 将set对象转成list对象
     * 
     * @param set
     * @return
     * @throws Exception
     */
    public List<SyncContractDetailForm> convertSetToList(Set<SyncContdetail> set) throws Exception {
        List<SyncContractDetailForm> list = new ArrayList<SyncContractDetailForm>();
        if (!CommonUtil.isNullorEmpty(set)) {
            Iterator<SyncContdetail> it = set.iterator();
            while (it.hasNext()) {
                SyncContdetail syncContdetail = it.next();
                SyncContractDetailForm detailForm = SyncContractDetailForm.class.newInstance();
                FormUtils.setPropertyValue(detailForm, syncContdetail);
                detailForm.setDetailid(syncContdetail.getDetailid());
                //获取产品名称
                SyncProduct syncProduct = syncProductService.get(syncContdetail.getSalprd_id());
                detailForm.setProductname(syncProduct.getProductname());
//                //一次性费用
//                if ("12471".equals(syncContdetail.getChargetype())) {
//                    detailForm.setChargetypename("一次性费用");
//                }
//                //不定期
//                if ("12472".equals(syncContdetail.getChargetype())) {
//                    detailForm.setChargetypename("不定期");
//                }
//                //年费
//                if ("12473".equals(syncContdetail.getChargetype())) {
//                    detailForm.setChargetypename("年费");
//                }
//                //条件延后
//                if ("12474".equals(syncContdetail.getChargetype())) {
//                    detailForm.setChargetypename("条件延后");
//                }
//                //技术服务费
//                if ("12475".equals(syncContdetail.getChargetype())) {
//                    detailForm.setChargetypename("技术服务费");
//                }
//                //免费
//                if ("12491".equals(syncContdetail.getChargetype())) {
//                    detailForm.setChargetypename("免费");
//                }

                list.add(detailForm);
            }
        }
        return list;
    }

}
