package com.hundsun.boss.modules.charge.service.ordervalidate;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.report.XlsxUtil;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.modules.charge.entity.ordervalidate.OrderValidate;
import com.hundsun.boss.modules.charge.form.ordervalidate.OrderValidateForm;
import com.hundsun.boss.modules.sys.entity.ReportConfig;
import com.hundsun.boss.modules.sys.utils.DictUtils;

/**
 * 合同校验信息导出service
 * 
 * @author feigq
 *
 */
@Service("exportValidateService")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExportValidateService extends ApplicationObjectSupport {

    @Autowired
    private OrderValidateService orderValidateService;

    public void generateReport(String zipFileName, ReportConfig reportConfig, OrderValidateForm downloadForm, String filePath) throws Exception {
        try {
            File zipPath = new File(filePath + "/" + zipFileName);
            // 判断路径是否存在
            if (!zipPath.exists()) {
                File parent = zipPath.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
            }
            // 如果是excel导出 
            if ("xlsx".equals(reportConfig.getFile_type())) {
                Map mValues = getData(reportConfig, downloadForm);
                XlsxUtil.generate(mValues, zipPath + "/" + mValues.get("filename"));
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    public Map getData(ReportConfig reportConfig, OrderValidateForm downloadForm) throws Exception {
        Map mValues = new HashMap();
        String content = StringEscapeUtils.unescapeHtml(reportConfig.getTemplate_content());
        try {
            mValues.put("template", reportConfig.getTemplate());
            mValues.put("content", content);
            mValues.put("filename", Formatter.formatDate(Formatter.DATE_FORMAT3, new Date()) + reportConfig.getFile_name());

            //不分页 
            downloadForm.setPageSize(-1);

            //对查询条件作解码处理
            //合同编号
            if (!CommonUtil.isNullorEmpty(downloadForm.getHs_contract_id())) {
                String namestr = HtmlUtils.htmlUnescape(downloadForm.getHs_contract_id());
                String namestr1 = URLDecoder.decode(namestr, "UTF-8");
                downloadForm.setHs_contract_id(namestr1);
            }

            //销售产品编号
            if (!CommonUtil.isNullorEmpty(downloadForm.getHs_product_id())) {
                String namestr = HtmlUtils.htmlUnescape(downloadForm.getHs_product_id());
                String namestr1 = URLDecoder.decode(namestr, "UTF-8");
                downloadForm.setHs_product_id(namestr1);
            }

            Page<OrderValidate> page = orderValidateService.find(new Page<OrderValidate>(), downloadForm);
            List<Map> list = new ArrayList<Map>();
            if (page.getList() != null && page.getList().size() > 0) {
                for (int i = 0; i < page.getList().size(); i++) {
                    Map map = new HashMap();
                    OrderValidate orderValidate = page.getList().get(i);
                    PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
                    PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(orderValidate);
                    for (int j = 0; j < descriptors.length; j++) {
                        String name = descriptors[j].getName();
                        if (name.compareToIgnoreCase("class") == 0) {
                            continue;
                        }
                        //公司
                        if ("office".equals(name)) {
                            if (CommonUtil.isNullorEmpty(orderValidate.getOffice())) {
                                map.put("department", "");
                            } else {
                                map.put("department", orderValidate.getOffice().getName());
                            }
                            continue;
                        }
                        //产品名称
                        if ("syncProduct".equals(name)) {
                            if (CommonUtil.isNullorEmpty(orderValidate.getSyncProduct())) {
                                map.put("product_name", "");
                            } else {
                                map.put("product_name", orderValidate.getSyncProduct().getProductname());
                            }
                            continue;
                        }
                        //校验类型
                        if ("hs_check_status".equals(name)) {
                            //获取数字字典
                            String lable = DictUtils.getDictLabel((String) propertyUtilsBean.getNestedProperty(orderValidate, name), "hs_check_status", "");
                            map.put(name, lable);
                            continue;
                        }
                        //校验级别
                        if ("belong_type".equals(name)) {
                            //获取数字字典
                            String lable = DictUtils.getDictLabel((String) propertyUtilsBean.getNestedProperty(orderValidate, name), "belong_type", "");
                            map.put(name, lable);
                            continue;
                        }
                        //显示状态
                        if ("show_status".equals(name)) {
                            //获取数字字典
                            String lable = DictUtils.getDictLabel((String) propertyUtilsBean.getNestedProperty(orderValidate, name), "show_status", "");
                            map.put(name, lable);
                            continue;

                        }
                        map.put(name, propertyUtilsBean.getNestedProperty(orderValidate, name));

                    }
                    list.add(map);
                }
            }
            mValues.put("griddata", list);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return mValues;
    }

}
