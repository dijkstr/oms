package com.hundsun.boss.modules.charge.web.setting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

import com.hundsun.boss.base.controller.BaseController;
import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.config.Global;
import com.hundsun.boss.common.report.PDFUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.common.chargeTypeUtils;
import com.hundsun.boss.modules.charge.entity.setting.ChargeModel;
import com.hundsun.boss.modules.charge.entity.setting.ChargeType;
import com.hundsun.boss.modules.charge.entity.setting.Classify;
import com.hundsun.boss.modules.charge.form.setting.ChargeModelForm;
import com.hundsun.boss.modules.charge.form.setting.ChargePriceForm;
import com.hundsun.boss.modules.charge.form.setting.ClassifyForm;
import com.hundsun.boss.modules.charge.service.setting.ChargeModelService;
import com.hundsun.boss.modules.charge.service.setting.ChargeTypeService;
import com.hundsun.boss.modules.charge.service.setting.ClassifyService;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.service.SysConfigService;
import com.hundsun.boss.modules.sys.utils.DictUtils;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 计费模式Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/setting/chargeModel")
@SuppressWarnings({"unchecked","rawtypes"})
public class ChargeModelController extends BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private ChargeModelService chargeModelService;

    @Autowired
    private ClassifyService classifyService;
    
    @Autowired
    private SysConfigService sysConfigService;
    
    @Autowired
    private ChargeTypeService chargeTypeService;
    
    @Autowired
    private OfficeService officeService;
    
    @ModelAttribute
    public ChargeModel get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            ChargeModel chargeModel = chargeModelService.get(id);
            return chargeModel;
        } else {
            return new ChargeModel();
        }
    }

    @RequiresPermissions("charge:setting:chargeModel:view")
    @RequestMapping(value = { "list", "" })
    public String list(ChargeModelForm chargeModel, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            chargeModel.setCreateBy(user.getId());
        }
        Page<ChargeModel> page = chargeModelService.find(new Page<ChargeModel>(request, response), chargeModel);
        model.addAttribute("page", page);
        model.addAttribute("chargeModel", chargeModel);
        if(!CommonUtil.isNullorEmpty(chargeModel.getClassify_id())){
            model.addAttribute("classify", classifyService.get(chargeModel.getClassify_id()));
        }
        return "modules/" + "charge/setting/chargeModelList";
    }

    @RequiresPermissions("charge:setting:chargeModel:view")
    @RequestMapping(value = { "chargeModelChooser" })
    public String chargeModelChooser(ChargeModelForm chargeModel, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            chargeModel.setCreateBy(user.getId());
        }
        Page defaultpage = new Page<ChargeModel>(request, response);
        defaultpage.setPageSize(5);
        Page<ChargeModel> page = chargeModelService.find(defaultpage, chargeModel);
        model.addAttribute("page", page);
        model.addAttribute("chargeModel", chargeModel);
        if(!CommonUtil.isNullorEmpty(chargeModel.getClassify_id())){
            model.addAttribute("classify", classifyService.get(chargeModel.getClassify_id()));
        }
        if(!CommonUtil.isNullorEmpty(chargeModel.getOffice_id())){
            model.addAttribute("office", officeService.findByCode(chargeModel.getOffice_id()));
        }
        return "modules/" + "charge/setting/chargeModelChooser";
    }

    @RequiresPermissions("charge:setting:chargeModel:edit")
    @RequestMapping(value = "form")
    public String form(ChargeModelForm form, Model model, RedirectAttributes redirectAttributes) {
        ChargeModel chargeModel = null;
        try {
            // 新增页面初始化
            if (form.getId()==null) {
                model.addAttribute("chargeModelForm", form);
            }
            // 更新页面初始化
            else if (!CommonUtil.isNullorEmpty(form.getId()) && CommonUtil.isNullorEmpty(form.getUpdateDate())) {
                chargeModel = chargeModelService.get(form.getId());
                form = convertEntityToForm(form,chargeModel);
                model.addAttribute("classify", classifyService.get(form.getClassify_id()));
                model.addAttribute("chargeModelForm", form);
            }
            // 如果校验出错
            else {
                model.addAttribute("classify", classifyService.get(form.getClassify_id()));
                model.addAttribute("chargeModelForm", form);
            }
            
            Map mValues = new HashMap();
            ReportConfig reportConfig = sysConfigService.getReportCongfig("chargeModelPrice");
            mValues.put("template", reportConfig.getTemplate());
            String content = StringEscapeUtils.unescapeHtml(reportConfig.getTemplate_content());
            content = content.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "");
            mValues.put("content", content);
            mValues.put("form", form);
            String pdfHtml = PDFUtil.generatePDFHtml(mValues);
            pdfHtml = pdfHtml.replaceAll("\'", "\\\\\'");
            model.addAttribute("html", pdfHtml);
            return "modules/" + "charge/setting/chargeModelForm";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "显示计费模式'" + form.getModel_name() + "'失败");
            model.addAttribute("classify", classifyService.get(form.getClassify_id()));
            model.addAttribute("chargeModelForm", form);
            return "modules/" + "charge/setting/chargeModelForm";
        }
    }

    @RequiresPermissions("charge:setting:chargeModel:edit")
    @RequestMapping(value = "save")
    public String save(ChargeModelForm form, Model model, RedirectAttributes redirectAttributes) throws Exception {
        try {
            // 确保画面list回显
            form.setChargePrices(FormUtils.getListByJson(form.getListJson(), ChargePriceForm.class));
            if (!beanValidator(model, form)) {
                return form(form, model, redirectAttributes);
            }
            List<ChargePriceForm> chargePrices = form.getChargePrices();
            for (int i = 0;!CommonUtil.isNullorEmpty(chargePrices) && i < chargePrices.size(); i++) {
                if (!beanValidator(model, chargePrices.get(i))) {
                    return form(form, model, redirectAttributes);
                }
            }
            Classify classify = classifyService.get(form.getClassify_id());
            ChargeType chargeType = chargeTypeService.getChargeType("fee_type", form.getFee_type());
            if(!CommonUtil.isNullorEmpty(classify) && !CommonUtil.isNullorEmpty(chargeType)){
                if(!(classify.getOffice_id()).equals(chargeType.getOffice_id())){
                    addMessage(model, "验证失败：<br/>计费分类所属部门与计费类型所属部门不一致，请确认");
                    return form(form, model, redirectAttributes);
                }
            }
            
            ChargeModel chargeModel = new ChargeModel();
            // 如果是更新，获取最新的计费模式信息
            if (!CommonUtil.isNullorEmpty(form.getId())) {
                chargeModel = chargeModelService.get(form.getId());

                // 如果有人更新内容，不允许修改
                if (!chargeModel.getUpdateDate().equals(form.getUpdateDate())) {
                    addMessage(model, "验证失败：<br/>计费模式：" + form.getModel_name() + "已经被人修改，请确认");
                    return form(form, model, redirectAttributes);
                }
            }
            
            for (int i = 0;!CommonUtil.isNullorEmpty(chargePrices) && i < chargePrices.size(); i++) {
                double step_unit = 1;
                if("1".equals(chargePrices.get(i).getStep_unit())){
                    step_unit = 10000;
                }else if("2".equals(chargePrices.get(i).getStep_unit())){
                    step_unit = 100000000;
                }
                if(!CommonUtil.isNullorEmpty(chargePrices.get(i).getStep_begin())){
                    chargePrices.get(i).setStep_begin(String.valueOf(Double.parseDouble(chargePrices.get(i).getStep_begin()) * step_unit));
                }
                if(!CommonUtil.isNullorEmpty(chargePrices.get(i).getStep_end())){
                    chargePrices.get(i).setStep_end(String.valueOf(Double.parseDouble(chargePrices.get(i).getStep_end()) * step_unit));
                }  
                if(CommonUtil.isNullorEmpty(chargePrices.get(i).getFee_ratio_division())){
                    chargePrices.get(i).setFee_ratio_division("365");
                }
            }

            form.setOffice_id(classify.getOffice_id());
            // 将表单对象更新入持久曾
            FormUtils.margeFormToEntity(form, chargeModel);
            chargeModel.setRemarks(form.getRemarks());
            chargeModelService.save(chargeModel);
            addMessage(redirectAttributes, "保存计费模式'" + form.getModel_name() + "'成功");
            return "redirect:" + Global.getAdminPath() + "/charge/setting/chargeModel/?repage";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "保存计费模式'" + form.getModel_name() + "'失败");
            model.addAttribute("classify", classifyService.get(form.getClassify_id()));
            model.addAttribute("chargeModel", form);
            return "modules/" + "charge/setting/chargeModelForm";
        }

    }

    @RequiresPermissions("charge:setting:chargeModel:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        chargeModelService.delete(id);
        addMessage(redirectAttributes, "删除计费模式成功");
        return "redirect:" + Global.getAdminPath() + "/charge/setting/chargeModel/?repage";
    }

    @RequestMapping("getChargeModelById")
    @ResponseBody
    public Map<String, Object> getChargeModelById(String id) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            if (!CommonUtil.isNullorEmpty(id)) {
                ChargeModel chargeModel = chargeModelService.get(id);
                ChargeModelForm form  = new ChargeModelForm();
                form = convertEntityToForm(form,chargeModel);
                Map mValues = new HashMap();
                ReportConfig reportConfig = sysConfigService.getReportCongfig("orderModelSelector");
                mValues.put("template", reportConfig.getTemplate());
                String content = StringEscapeUtils.unescapeHtml(reportConfig.getTemplate_content());
                content = content.replaceAll("\n", "").replaceAll("\r", "").replaceAll("\t", "");
                mValues.put("content", content);
                mValues.put("form", form);
                String pdfHtml = PDFUtil.generatePDFHtml(mValues);
                
                data.put("result", "success");
                data.put("html", pdfHtml);
            } else {
                data.put("result", "error");
                data.put("message", "失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            data.put("result", "error");
            data.put("message", "失败");
        }
        return data;

    }

   

    public ChargeModelForm convertEntityToForm(ChargeModelForm form, ChargeModel chargeModel) throws Exception {
        FormUtils.setPropertyValue(form, chargeModel);
        form.setUpdateDate(chargeModel.getUpdateDate());
        form.setRemarks(chargeModel.getRemarks());
        form.setChargePrices(FormUtils.convertSetToList(ChargePriceForm.class, chargeModel.getChargePrices()));
        form.setFee_formula_name(DictUtils.getDictLabel(form.getFee_formula(), "fee_formula", ""));
        form.setFee_type_name(chargeTypeUtils.getDictLabel(form.getFee_type(), "fee_type", ""));
        
        for (int i = 0;!CommonUtil.isNullorEmpty(form.getChargePrices()) && i < form.getChargePrices().size(); i++) {
            double step_unit = 1;
            if("1".equals(form.getChargePrices().get(i).getStep_unit())){
                step_unit = 10000;
            }else if("2".equals(form.getChargePrices().get(i).getStep_unit())){
                step_unit = 100000000;
            }
            if(!CommonUtil.isNullorEmpty(form.getChargePrices().get(i).getStep_begin())){
                //form.getChargePrices().get(i).setStep_begin(String.valueOf(Double.parseDouble(form.getChargePrices().get(i).getStep_begin()) / step_unit));
                form.getChargePrices().get(i).setStep_begin(Formatter.formatDecimal(Formatter.DECIMAL_FORMAT15, Double.parseDouble(form.getChargePrices().get(i).getStep_begin()) / step_unit));
            }
            if(!CommonUtil.isNullorEmpty(form.getChargePrices().get(i).getStep_end())){
                //form.getChargePrices().get(i).setStep_end(String.valueOf(Double.parseDouble(form.getChargePrices().get(i).getStep_end()) / step_unit));
                form.getChargePrices().get(i).setStep_end(Formatter.formatDecimal(Formatter.DECIMAL_FORMAT15, Double.parseDouble(form.getChargePrices().get(i).getStep_end()) / step_unit));
            }               
        }
        return form;
    }
    
    @RequiresPermissions("charge:setting:chargeModel:view")
    @RequestMapping(value = { "chargeModelClassifyChooser" })
    public String chargeModelClassifyChooser(Classify classifyForm, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            classifyForm.setCreateBy(user);
        }
        Page defaultpage = new Page<ClassifyForm>(request, response);
        defaultpage.setPageSize(5);
        Page<Classify> page = classifyService.find(defaultpage, classifyForm);
        model.addAttribute("page", page);
        model.addAttribute("classifyForm", classifyForm);
        if(!CommonUtil.isNullorEmpty(classifyForm.getId())){
            model.addAttribute("classify", classifyService.get(classifyForm.getId()));
        }
        if(!CommonUtil.isNullorEmpty(classifyForm.getOffice_id())){
            model.addAttribute("office", officeService.findByCode(classifyForm.getOffice_id()));
        }
        return "modules/" + "charge/setting/chargeModelClassifyChooser";
    }

}
