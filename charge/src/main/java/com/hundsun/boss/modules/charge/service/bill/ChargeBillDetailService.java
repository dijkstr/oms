package com.hundsun.boss.modules.charge.service.bill;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hundsun.boss.base.exception.ServiceException;
import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.CommonUtils;
import com.hundsun.boss.modules.charge.common.ChargeConstant;
import com.hundsun.boss.modules.charge.dao.bill.ChargeBillDao;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillInfo;
import com.hundsun.boss.modules.charge.form.bill.ChargeBillSearchForm;
import com.hundsun.boss.modules.charge.form.bill.ChargeOrderInfo;
import com.hundsun.boss.modules.charge.form.bill.ChargeTransDetail;
import com.hundsun.boss.modules.charge.form.bill.DownloadForm;
import com.hundsun.boss.modules.charge.form.bill.OrderAdvPaymentForm;
import com.hundsun.boss.modules.charge.form.bill.OrderCombineForm;
import com.hundsun.boss.modules.charge.form.bill.OrderInfoForm;
import com.hundsun.boss.modules.charge.form.bill.OrderModelForm;
import com.hundsun.boss.modules.charge.form.bill.OrderPriceForm;
import com.hundsun.boss.modules.charge.form.bill.OrderProductForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 账单详情Service
 */
@Component
@Transactional(readOnly = false)
public class ChargeBillDetailService extends BaseService {

    @Autowired
    private ChargeBillDao chargeBillDao;

    /**
     * 获取账单详情.
     * 
     * @param req 请求参数.
     * @return 返回某条账单的详情信息.
     */
    public OrderInfoForm queryChargeReceiptDetailReport(ChargeBillSearchForm req) {
        //权限逻辑
        User currentUser = UserUtils.getUser();
        req.setDept(getDept(currentUser, "office", ""));
        OrderInfoForm res = chargeBillDao.getChargeDetail(req);
        if (CommonUtil.isNullorEmpty(res)) {
            throw new ServiceException("403");
        }
        if (!CommonUtils.isNullorEmpty(res)) {
            ChargeOrderInfo chargeOrderInfo = new ChargeOrderInfo();
            chargeOrderInfo.setBill_id(res.getBill_id());
            chargeOrderInfo.setContract_id(res.getContract_id());
            res.setChargeOrderInfo(chargeBillDao.getChargeOrderInfo(chargeOrderInfo));
            // 合同计费信息
            ChargeBillInfo chargeBillInfo = new ChargeBillInfo();
            chargeBillInfo.setBill_id(res.getBill_id());
            chargeBillInfo.setContract_id(res.getContract_id());
            chargeBillInfo.setBelong_type(ChargeConstant.BELONG_TYPE_1);
            res.setChargeBillInfo(chargeBillDao.queryChargeBillInfo(chargeBillInfo));
            // 查询组合信息
            OrderCombineForm chargeCombine = new OrderCombineForm();
            chargeCombine.setBill_id(res.getBill_id());
            chargeCombine.setContract_id(res.getContract_id());
            chargeCombine.setCharge_begin_date(req.getCharge_begin_date());
            chargeCombine.setCharge_end_date(req.getCharge_end_date());
            res.setOrderCombines(queryChargeCombineByPeriod(chargeCombine));
        }
        return res;
    }

    /**
     * 获取账单组合及计费选项
     * 
     * @param orderCombine
     */
    private List<OrderCombineForm> queryChargeCombineByPeriod(OrderCombineForm chargeCombine) {
        // 查找早于指定帐单时间的账单
        DownloadForm downloadForm = new DownloadForm();
        downloadForm.setContract_id(chargeCombine.getContract_id());
        downloadForm.setCharge_begin_date(chargeCombine.getCharge_begin_date()); 
        downloadForm.setCharge_end_date(chargeCombine.getCharge_end_date());  
        List<String> billIds = chargeBillDao.getBillIds(downloadForm);
        List<OrderCombineForm> chargeCombines = new ArrayList<OrderCombineForm>();
        for (int j = 0; j < billIds.size(); j++) {
            OrderCombineForm param = new OrderCombineForm();
            param.setBill_id(billIds.get(j));
            List<OrderCombineForm> listChargeCombine = chargeBillDao.queryChargeCombine(param);
            chargeCombines.addAll(listChargeCombine);
        }
        List<String> combineIds = new ArrayList<String>();
        for (int j = 0; j < chargeCombines.size();) {
            if (combineIds.contains(chargeCombines.get(j).getCombine_id())) {
                chargeCombines.remove(j);
            } else {
                combineIds.add(chargeCombines.get(j).getCombine_id());
                j++;
            }
        }
        if (!CommonUtils.isNullorEmpty(chargeCombines)) {
            for (int i = 0; i < chargeCombines.size(); i++) {
                // 查询产品信息
                OrderProductForm chargeProductReq = new OrderProductForm();
                chargeProductReq.setBill_id(chargeCombines.get(i).getBill_id());
                chargeProductReq.setContract_id(chargeCombines.get(i).getContract_id());
                chargeProductReq.setCombine_id(chargeCombines.get(i).getId());
                chargeProductReq.setCharge_begin_date(chargeCombine.getCharge_begin_date());
                chargeProductReq.setCharge_end_date(chargeCombine.getCharge_end_date());
                chargeCombines.get(i).setOrderProducts(queryChargeCombineProduct(chargeProductReq, false));
            }
        }
        return chargeCombines;
    }

    /**
     * 获取账单详情.
     * 
     * @param req 请求参数.
     * @return 返回某条账单的详情信息.
     */
    public OrderInfoForm queryChargeDetail(ChargeBillSearchForm req) {
        //权限逻辑
        User currentUser = UserUtils.getUser();
        req.setDept(getDept(currentUser, "office", ""));
        OrderInfoForm res = chargeBillDao.getChargeDetail(req);
        if (CommonUtil.isNullorEmpty(res)) {
            throw new ServiceException("403");
        }
        if (!CommonUtils.isNullorEmpty(res)) {
            ChargeOrderInfo chargeOrderInfo = new ChargeOrderInfo();
            chargeOrderInfo.setBill_id(res.getBill_id());
            chargeOrderInfo.setContract_id(res.getContract_id());
            res.setChargeOrderInfo(chargeBillDao.getChargeOrderInfo(chargeOrderInfo));
            // 合同计费信息
            ChargeBillInfo chargeBillInfo = new ChargeBillInfo();
            chargeBillInfo.setBill_id(res.getBill_id());
            chargeBillInfo.setContract_id(res.getContract_id());
            chargeBillInfo.setBelong_type(ChargeConstant.BELONG_TYPE_1);
            res.setChargeBillInfo(chargeBillDao.queryChargeBillInfo(chargeBillInfo));
            // 查询组合信息
            OrderCombineForm chargeCombine = new OrderCombineForm();
            chargeCombine.setBill_id(res.getBill_id());
            chargeCombine.setContract_id(res.getContract_id());
            res.setOrderCombines(queryChargeCombine(chargeCombine));
        }
        return res;
    }

    /**
     * 获取账单组合及计费选项
     * 
     * @param orderCombine
     */
    private List<OrderCombineForm> queryChargeCombine(OrderCombineForm chargeCombine) {
        List<OrderCombineForm> listChargeCombine = chargeBillDao.queryChargeCombine(chargeCombine);
        if (!CommonUtils.isNullorEmpty(listChargeCombine)) {
            for (int i = 0; i < listChargeCombine.size(); i++) {
                // 组合计费模式
                OrderModelForm modelReq = new OrderModelForm();
                modelReq.setBill_id(listChargeCombine.get(i).getBill_id());
                modelReq.setRef_id(listChargeCombine.get(i).getId());
                modelReq.setBelong_type(ChargeConstant.BELONG_TYPE_2);
                listChargeCombine.get(i).setSsOrderModel(getSSOrderModel(modelReq));
                // 组合预付信息
                OrderAdvPaymentForm cycleReq = new OrderAdvPaymentForm();
                cycleReq.setBill_id(listChargeCombine.get(i).getBill_id());
                cycleReq.setContract_id(listChargeCombine.get(i).getContract_id());
                cycleReq.setCombine_id(listChargeCombine.get(i).getId());
                listChargeCombine.get(i).setOrderAdvPayments(chargeBillDao.querySSOrderPaymentCycle(cycleReq));
                // 组合计费信息
                ChargeBillInfo chargeBillInfo = new ChargeBillInfo();
                chargeBillInfo.setBill_id(listChargeCombine.get(i).getBill_id());
                chargeBillInfo.setContract_id(listChargeCombine.get(i).getContract_id());
                chargeBillInfo.setCombine_id(listChargeCombine.get(i).getId());
                chargeBillInfo.setBelong_type(ChargeConstant.BELONG_TYPE_2);
                listChargeCombine.get(i).setChargeBillInfo(chargeBillDao.queryChargeBillInfo(chargeBillInfo));
                // 查询产品信息
                OrderProductForm chargeProductReq = new OrderProductForm();
                chargeProductReq.setBill_id(listChargeCombine.get(i).getBill_id());
                chargeProductReq.setContract_id(listChargeCombine.get(i).getContract_id());
                chargeProductReq.setCombine_id(listChargeCombine.get(i).getId());

                if (!CommonUtils.isNullorEmpty(listChargeCombine.get(i).getChargeBillInfo())) {
                    Double yearlyService = Double.parseDouble(listChargeCombine.get(i).getChargeBillInfo().getYearly_service_charge());
                    Double service = Double.parseDouble(listChargeCombine.get(i).getChargeBillInfo().getService_charge());
                    // 判断是否需要隐藏套餐与计费信息，客户交易明细
                    if (!CommonUtils.isNullorEmpty(listChargeCombine.get(i).getSsOrderModel())
                    // 年保底预付判断是否隐藏
                            && ("1".equals(listChargeCombine.get(i).getSsOrderModel().getMin_type())
                            // 不定期预付判断是否隐藏
                            || "4".equals(listChargeCombine.get(i).getSsOrderModel().getMin_type()))) {
                        Double minConsume = Double.parseDouble(listChargeCombine.get(i).getSsOrderModel().getMin_consume());
                        // 如果是超过之后的第二个月以及以后
                        if (yearlyService - service > minConsume) {
                            listChargeCombine.get(i).setView_flag(1);
                            // 如果是超过的当月
                        } else if (yearlyService > minConsume) {
                            listChargeCombine.get(i).setView_flag(1);
                            // 需要显示当年度历史上隐藏的交易明细
                            chargeProductReq.setView_flag(1);
                        }
                        // 如果没超，隐藏组合信息
                    } else {
                        // 正常情况下，一直显示套餐与计费信息，客户交易明细
                        listChargeCombine.get(i).setView_flag(1);
                    }
                    listChargeCombine.get(i).setOrderProducts(queryChargeCombineProduct(chargeProductReq, true));
                }
            }
        }
        return listChargeCombine;
    }

    /**
     * 获取产品及产品计费模式
     * 
     * @param orderProductReq 请求参数
     * @return 返回大对象产品及计费模式，计费定价
     */
    private List<OrderProductForm> queryChargeCombineProduct(OrderProductForm chargeProductReq, Boolean needBillId) {
        List<OrderProductForm> listprod = chargeBillDao.queryChargeCombineProduct(chargeProductReq);
        if (!CommonUtils.isNullorEmpty(listprod)) {
            for (int i = 0; i < listprod.size(); i++) {
                // 产品计费模式
                OrderModelForm modelReq = new OrderModelForm();
                modelReq.setBill_id(listprod.get(i).getBill_id());
                modelReq.setRef_id(listprod.get(i).getId());
                modelReq.setId(listprod.get(i).getFeemodel_id());
                modelReq.setBelong_type(ChargeConstant.BELONG_TYPE_3);
                listprod.get(i).setSsOrderModel(getSSOrderModel(modelReq));
                // 产品计费信息
                ChargeBillInfo chargeBillInfo = new ChargeBillInfo();
                chargeBillInfo.setBill_id(listprod.get(i).getBill_id());
                chargeBillInfo.setContract_id(listprod.get(i).getContract_id());
                chargeBillInfo.setCombine_id(listprod.get(i).getCombine_id());
                chargeBillInfo.setProduct_id(listprod.get(i).getProduct_id());
                chargeBillInfo.setFeemodel_id(listprod.get(i).getFeemodel_id());
                chargeBillInfo.setBelong_type(ChargeConstant.BELONG_TYPE_3);
                listprod.get(i).setChargeBillInfo(chargeBillDao.queryChargeBillInfo(chargeBillInfo));

                // 产品交易明细
                ChargeTransDetail chargeTransDetail = new ChargeTransDetail();
                if (needBillId) {
                    chargeTransDetail.setBill_id(listprod.get(i).getBill_id());
                }
                chargeTransDetail.setContract_id(listprod.get(i).getContract_id());
                chargeTransDetail.setCombine_id(listprod.get(i).getCombine_id());
                chargeTransDetail.setProduct_id(listprod.get(i).getProduct_id());
                chargeTransDetail.setFeemodel_id(listprod.get(i).getFeemodel_id());
                chargeTransDetail.setView_flag(chargeProductReq.getView_flag());
                chargeTransDetail.setCharge_begin_date(listprod.get(i).getCharge_begin_date());
                chargeTransDetail.setCharge_end_date(listprod.get(i).getCharge_end_date());
                chargeTransDetail.setProd_begin_date(listprod.get(i).getProd_begin_date());
                chargeTransDetail.setBegin_date(chargeProductReq.getCharge_begin_date());
                chargeTransDetail.setEnd_date(chargeProductReq.getCharge_end_date());
                listprod.get(i).setListTransDetail(chargeBillDao.queryChargeTransDetail(chargeTransDetail));

                // 产品每月计费金额累计
                listprod.get(i).setListChargeProductMonthlyCharge(chargeBillDao.getChargeProductMonthlyCharge(chargeTransDetail));
            }
        }
        return listprod;
    }

    /**
     * 获取计费模式。
     * 
     * @param modelReq 请求参数
     * @return 返回计费模式对象，包括计费模式，定价选项
     */
    private OrderModelForm getSSOrderModel(OrderModelForm modelReq) {
        OrderModelForm moderRes = chargeBillDao.getSSChargeModel(modelReq);
        if (null != moderRes) {
            OrderPriceForm priceReq = new OrderPriceForm();
            if (!CommonUtils.isNullorEmpty(modelReq.getBill_id())) {
                priceReq.setBill_id(modelReq.getBill_id());
            }
            if (!CommonUtils.isNullorEmpty(modelReq.getId())) {
                priceReq.setFeemodel_id(modelReq.getId());
            }
            List<OrderPriceForm> listPrice = chargeBillDao.querySSChargePrice(priceReq);
            for (OrderPriceForm res : listPrice) {
                res.setOption_name(res.getOption_name().replaceAll("<", "&lt;").replaceAll("<=", "&le;").replaceAll(">", "&gt;").replaceAll(">=", "&ge;"));
            }
            moderRes.setOrderPrices(listPrice);
        }
        return moderRes;
    }

}
