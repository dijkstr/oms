package com.hundsun.boss.modules.charge.web.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
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
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.FormUtils;
import com.hundsun.boss.common.utils.StringUtils;
import com.hundsun.boss.modules.charge.entity.setting.ChargeType;
import com.hundsun.boss.modules.charge.form.setting.ChargeTypeForm;
import com.hundsun.boss.modules.charge.service.setting.ChargeTypeService;
import com.hundsun.boss.modules.charge.service.setting.ClassifyService;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.service.OfficeService;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 业务字典Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/setting/chargeType")
public class ChargeTypeController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ChargeTypeService chargeTypeService;

    @Autowired
    private OfficeService officeService;
    
    @Autowired
    private ClassifyService classifyService;
    
    @ModelAttribute
    public ChargeType get(@RequestParam(required = false) String id) {
        if (StringUtils.isNotBlank(id)) {
            return chargeTypeService.get(id);
        } else {
            return new ChargeType();
        }
    }

    @RequiresPermissions("charge:setting:chargeType:view")
    @RequestMapping(value = { "list", "" })
    public String list(ChargeType chargeType, HttpServletRequest request, HttpServletResponse response, Model model) {
        User user = UserUtils.getUser();
        if (!user.isAdmin()) {
            chargeType.setCreateBy(user);
        }
        model.addAttribute("office", officeService.findByCode(chargeType.getOffice_id()));
        Page<ChargeType> page = chargeTypeService.find(new Page<ChargeType>(request, response), chargeType);
        model.addAttribute("page", page);
        return "modules/" + "charge/setting/chargeTypeList";
    }
    
    @RequestMapping("getChargeTypeList")
    @ResponseBody
    public Map<String, Object> getChargeTypeList(String id) {
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            List<String> officeIds = null;
            if(!CommonUtil.isNullorEmpty(id)){
                officeIds = classifyService.findOfficeCodesById(id);
            }else{
                data.put("result", "success");
                data.put("list", new ArrayList<ChargeTypeForm>());
                return data;
            }
            List<ChargeType> typeList = chargeTypeService.getDictList("fee_type",officeIds);
            List<ChargeTypeForm> list = new ArrayList<ChargeTypeForm>();
            if(!CommonUtil.isNullorEmpty(typeList)){
                for(int i=0;i<typeList.size();i++){
                    ChargeTypeForm form = new ChargeTypeForm();
                    form.setValue(typeList.get(i).getValue());
                    form.setLabel(typeList.get(i).getLabel());
                    list.add(form);
                }
            }
            data.put("result", "success");
            data.put("list", list);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            data.put("result", "error");
            data.put("message", "失败");
        }
        return data;

    }
    
    

    @RequiresPermissions("charge:setting:chargeType:view")
    @RequestMapping(value = "form")
    public String form(ChargeTypeForm chargeType, Model model, RedirectAttributes redirectAttributes) {
        try {
            if(chargeType.getId() == null){
                model.addAttribute("chargeType", chargeType );
            }else if(!CommonUtil.isNullorEmpty(chargeType.getId()) && CommonUtil.isNullorEmpty(chargeType.getUpdateDate())){
                ChargeType chargeTypeEntity = get(chargeType.getId());
                FormUtils.setPropertyValue(chargeType, chargeTypeEntity);
                chargeType.setUpdateDate(chargeTypeEntity.getUpdateDate());
                model.addAttribute("chargeType", chargeType);
            }else{
                model.addAttribute("chargeType", chargeType );
            }
             
             model.addAttribute("office", officeService.findByCode(chargeType.getOffice_id()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "显示分类失败");
            model.addAttribute("chargeType", chargeType);
        }
        return "modules/" + "charge/setting/chargeTypeForm";
    }

    @RequiresPermissions("charge:setting:chargeType:edit")
    @RequestMapping(value = "save")
    public String save(ChargeTypeForm chargeTypeForm, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (!beanValidator(model, chargeTypeForm)) {
                return form(chargeTypeForm, model, redirectAttributes);
            }
            List<ChargeType> list = chargeTypeService.getDictList(chargeTypeForm.getType(),null);
            int count=0;
            if(!CommonUtil.isNullorEmpty(list)){
                for (ChargeType dict : list) {
                    if ((chargeTypeForm.getValue()).equals(dict.getValue())) {
                        count++;
                    }
                }
            }
            ChargeType chargeType = new ChargeType();
            
            if(CommonUtil.isNullorEmpty(chargeTypeForm.getId())){
                //新增
                if (count>0) {
                    addMessage(model, "验证失败：<br/>业务字典键值已存在，请确认");
                    return form(chargeTypeForm,model,redirectAttributes);
                }
            }else{
                //修改
                chargeType = get(chargeTypeForm.getId());
                if(chargeTypeForm.getValue().equals(chargeType.getValue())){
                    if (count>1) {
                        addMessage(model, "验证失败：<br/>业务字典键值已存在，请确认");
                        return form(chargeTypeForm,model,redirectAttributes);
                    }
                }else{
                    if (count>0) {
                        addMessage(model, "验证失败：<br/>业务字典键值已存在，请确认");
                        return form(chargeTypeForm,model,redirectAttributes);
                    }
                }  
            }
            BeanUtils.copyProperties(chargeType, chargeTypeForm);
            chargeTypeService.save(chargeType);
            addMessage(redirectAttributes, "保存业务字典'" + chargeTypeForm.getType() + "'成功");
            return "redirect:" + Global.getAdminPath() + "/charge/setting/chargeType/";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            addMessage(redirectAttributes, "保存业务字典'" + chargeTypeForm.getType() + "'失败");
            model.addAttribute("chargeType", chargeTypeForm);
            return "modules/" + "charge/setting/chargeTypeForm";
        }
    }

    @RequiresPermissions("charge:setting:chargeType:edit")
    @RequestMapping(value = "delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        chargeTypeService.delete(id);
        addMessage(redirectAttributes, "删除业务字典成功");
        return "redirect:" + Global.getAdminPath() + "/charge/setting/chargeType/";
    }

}
