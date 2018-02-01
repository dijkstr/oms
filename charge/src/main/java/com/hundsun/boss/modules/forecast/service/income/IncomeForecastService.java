package com.hundsun.boss.modules.forecast.service.income;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundsun.boss.base.service.BaseService;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.common.utils.Formatter;
import com.hundsun.boss.common.utils.IdGen;
import com.hundsun.boss.common.utils.echart.CommonChartData;
import com.hundsun.boss.modules.charge.dao.finance.FinSummaryDao;
import com.hundsun.boss.modules.charge.dao.order.OrderInfoMyBatisDao;
import com.hundsun.boss.modules.forecast.bean.IncomeForecastDataBean;
import com.hundsun.boss.modules.forecast.bean.MinMaxConsumeDataBean;
import com.hundsun.boss.modules.forecast.bean.PeriodSequence;
import com.hundsun.boss.modules.forecast.common.ForecastProgress;
import com.hundsun.boss.modules.forecast.dao.IncomeForecastMybatisDao;
import com.hundsun.boss.modules.forecast.form.income.IncomeForecastForm;
import com.hundsun.boss.modules.sys.entity.User;
import com.hundsun.boss.modules.sys.utils.UserUtils;

/**
 * 收入预测Service
 */
@Component
public class IncomeForecastService extends BaseService {

    @Autowired
    private IncomeForecastMybatisDao incomeForecastMybatisDao;
    @Autowired
    private OrderInfoMyBatisDao orderInfoMyBatisDao;
    @Autowired
    private FinSummaryDao finSummaryDao;

    /**
     * 收入预测
     * 
     * @param incomeForecastForm
     * @return
     */
    public String refresh(IncomeForecastForm incomeForecastForm) {
        try {
            // 开始获取统一接口数据
            ForecastProgress.progressNomal("开始处理");
            // 刷新权限
            User currentUser = UserUtils.getUser();
            incomeForecastForm.setDept(getDept(currentUser, "office", ""));
            // 获取待预测的合同列表
            List<String> forecastContractIds = incomeForecastMybatisDao.getRefreshContractIds(incomeForecastForm);
            // 删除之前的预测结果
            incomeForecastMybatisDao.deletePreviousForecastResultsByCondition(incomeForecastForm);
            // 生成收入预测
            for (int i = 0; i < forecastContractIds.size(); i++) {
                ForecastProgress.progressNomal("预测合同数", i, forecastContractIds.size());
                // 根据协同合同编号,
                // 获取收入设置中的固定费用类型内容;
                List<IncomeForecastDataBean> forecastDatas = orderInfoMyBatisDao.queryFixedIncomeSettingList(forecastContractIds.get(i));
                // 获取技术服务费中的计费数据;
                forecastDatas.addAll(finSummaryDao.getFinsummaryForForecast(forecastContractIds.get(i)));
                for (IncomeForecastDataBean forecastData : forecastDatas) {
                    List<IncomeForecastDataBean> forecastResult = new ArrayList<IncomeForecastDataBean>();
                    // 无需预测的纪录直接计入预测结果
                    if ("0".equals(forecastData.getForecast_flag())) {
                        forecastData.setId(IdGen.uuid());
                        forecastResult.add(forecastData);
                    } else {
                        // 期间起始字段选取
                        String beginDate = "";
                        if ("12475".equals(forecastData.getPayment_type()) && forecastData.getCharge_begin_date().compareTo(forecastData.getIncome_begin_date()) > 0) {
                            beginDate = forecastData.getCharge_begin_date();
                            if (forecastData.getCharge_end_date().compareTo(forecastData.getIncome_end_date()) < 0) {
                                forecastData.setIncome(forecastDatas.get(forecastDatas.size() - 1).getIncomeDouble());
                            }
                        } else if ("12475".equals(forecastData.getPayment_type()) && forecastData.getCharge_begin_date().compareTo(forecastData.getIncome_begin_date()) <= 0) {
                            beginDate = forecastData.getIncome_begin_date();
                            // 用于第二年度的封顶
                            forecastData.setIncome(0.0);
                        } else {
                            beginDate = forecastData.getIncome_begin_date();
                        }
                        // 获取预测周期中的时间序列
                        List<PeriodSequence> periodSequences = getPeriodSequence(getDateByString(beginDate), getDateByString(forecastData.getIncome_end_date()));
                        // 收入预测
                        forecastResult.addAll(getForecastResults(forecastData, periodSequences));
                    }
                    // 保存预测结果
                    if (!CommonUtil.isNullorEmpty(forecastResult)) {
                        incomeForecastMybatisDao.saveBatchForeCastResult(forecastResult);
                    }
                }

            }
            ForecastProgress.progressFinish();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            ForecastProgress.progressWrong();
            return "系统异常，请重试或者联系系统管理员";
        }
        return "success";
    }

    /**
     * 根据期间序列获取预测结果
     * 
     * @param forecastData
     * @param periodSequences
     * @return
     * @throws Exception
     */
    public List<IncomeForecastDataBean> getForecastResults(IncomeForecastDataBean forecastData, List<PeriodSequence> periodSequences) throws Exception {
        List<IncomeForecastDataBean> forecastResults = new ArrayList<IncomeForecastDataBean>();
        IncomeForecastDataBean forecastResut = null;
        // 需要预测的纪录
        // 是否超年保底
        boolean flag = true;
        Double max = 0.0;
        for (PeriodSequence period : periodSequences) {

            forecastResut = new IncomeForecastDataBean(forecastData);
            forecastResut.setId(IdGen.uuid());
            forecastResut.setCharge_begin_date(Formatter.formatDate(Formatter.DATE_FORMAT1, period.getCurBeginDate()));
            forecastResut.setCharge_end_date(Formatter.formatDate(Formatter.DATE_FORMAT1, period.getCurEndDate()));
            // 年费
            if ("12473".equals(forecastData.getPayment_type())) {
                forecastResut.setIncome(forecastData.getIncomeDouble() * getDateDiff(period.getCurEndDate(), period.getYearlyBeginDate()) / getYearylDayCount(period.getYearlyBeginDate()));
                forecastResut.setChange_income(forecastData.getIncomeDouble() * getDateDiff(period.getCurEndDate(), period.getCurBeginDate()) / getYearylDayCount(period.getYearlyBeginDate()));
                forecastResults.add(forecastResut);
            } else if ("12472".equals(forecastData.getPayment_type())) {
                // 不定期
                forecastResut.setIncome(forecastData.getIncomeDouble() * getDateDiff(period.getCurEndDate(), period.getYearlyBeginDate())
                        / getDateDiff(period.getYearlyEndDate(), period.getYearlyBeginDate()));
                forecastResut.setChange_income(forecastData.getIncomeDouble() * getDateDiff(period.getCurEndDate(), period.getCurBeginDate())
                        / getDateDiff(period.getYearlyEndDate(), period.getYearlyBeginDate()));
                forecastResults.add(forecastResut);
            } else if ("12471".equals(forecastData.getPayment_type()) || "12474".equals(forecastData.getPayment_type())) {
                if (period.getCurEndDate().compareTo(getDateByString(forecastData.getIncome_end_date())) >= 0) {
                    forecastResut.setCharge_begin_date(Formatter.formatDate(Formatter.DATE_FORMAT1, period.getYearlyBeginDate()));
                    forecastResut.setCharge_end_date(Formatter.formatDate(Formatter.DATE_FORMAT1, period.getYearlyBeginDate()));
                    forecastResut.setIncome(forecastData.getIncome());
                    forecastResut.setChange_income(forecastData.getIncome());
                    forecastResults.add(forecastResut);
                }
            } else if ("12475".equals(forecastData.getPayment_type())) {
                // 技术服务费
                if (period.getCurBeginDate().compareTo(getDateByString(forecastData.getCharge_begin_date())) >= 0) {
                    Double sumMinConsume = 0.0;
                    Double sumMaxConsume = 0.0;
                    Double serviceCharge = forecastData.getService_charge_weightDouble() * getDateDiff(period.getCurEndDate(), period.getCurBeginDate());
                    Double totalCharge = forecastData.getService_charge_weightDouble() * getDateDiff(period.getCurEndDate(), period.getYearlyBeginDate()) + forecastData.getIncomeDouble();
                    // 根据合同、产品、本期时间获得对应的保底、封顶
                    List<MinMaxConsumeDataBean> minMaxList = orderInfoMyBatisDao.queryMinMaxConsumeList(forecastResut);
                    if (!CommonUtil.isNullorEmpty(minMaxList)) {
                        for (MinMaxConsumeDataBean minMax : minMaxList) {
                            // 封顶计算
                            if (!CommonUtil.isNullorEmpty(minMax.getMax_consume())) {
                                Double maxConsume = minMax.getMax_consume() * minMax.getSplit_ratio() / 100;
                                if ("2".equals(minMax.getMax_type())) {
                                    // 月封顶
                                    sumMaxConsume += maxConsume;
                                } else if ("3".equals(minMax.getMax_type())) {
                                    // 日封顶
                                    sumMaxConsume += maxConsume * getDateDiff(period.getCurEndDate(), period.getCurBeginDate());
                                } else {
                                    // 年封顶、不定期封顶，如果超出年、不定期封顶则不必计算保底
                                    Double yearConsume = 0.0;
                                    for (MinMaxConsumeDataBean year : minMaxList) {
                                        if (!CommonUtil.isNullorEmpty(year.getMax_consume())) {
                                            yearConsume += year.getMax_consume() * year.getSplit_ratio() / 100;
                                        }
                                    }
                                    if (totalCharge > yearConsume && flag == true) {
                                        max = yearConsume;
                                        // 预测时第一个月就超封顶
                                        if (forecastResults.size() == 0) {
                                            forecastResut.setChange_income(yearConsume - forecastData.getIncomeDouble());
                                        } else {
                                            forecastResut.setChange_income(yearConsume - (forecastResults.get(forecastResults.size() - 1)).getIncomeDouble());
                                        }
                                        forecastResut.setIncome(yearConsume);
                                        flag = false;
                                        break;
                                    }
                                }
                            }
                            // 保底计算
                            sumMinConsume += getMinConsume(minMax, period, forecastData);
                        }
                    }
                    // 比较保底、封顶、技术服务费
                    if (flag == true) {
                        if (serviceCharge > sumMaxConsume && sumMaxConsume > 0) {
                            forecastResut.setChange_income(sumMaxConsume);
                        } else {
                            forecastResut.setChange_income(Math.max(serviceCharge, sumMinConsume));
                        }
                        forecastResut.setIncome(totalCharge);
                    } else {
                        forecastResut.setIncome(max);
                    }
                    forecastResults.add(forecastResut);
                }
            }
        }
        return forecastResults;
    }

    /**
     * 根据字符串获取日期
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    private static Date getDateByString(String date) throws ParseException {
        return Formatter.parseUtilDate(Formatter.DATE_FORMAT1, date);
    }

    /**
     * 获取年度天数
     * 
     * @param curDate
     * @return
     * @throws Exception
     */
    private int getYearylDayCount(Date beginDate) throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(beginDate);
        int year = calendar.get(Calendar.YEAR);
        // 如果跨越了闰年的2月29日，年度日数为366天
        if ((isLeapYear(year) && beginDate.before(Formatter.parseUtilDate(Formatter.DATE_FORMAT1, year + "-02-29")))
                || (!isLeapYear(year) && isLeapYear(year + 1) && beginDate.after(Formatter.parseUtilDate(Formatter.DATE_FORMAT1, year + "-03-01")))) {
            return 366;
        } else {
            // 否认则年度日数为365天
            return 365;
        }
    }

    /**
     * 获取日期之间天数差
     * 
     * @param beginDay
     * @param endDay
     * @return
     * @throws Exception
     */
    private long getDateDiff(Date endDate, Date beginDate) throws Exception {
        long diff = endDate.getTime() - beginDate.getTime();
        long days = diff / (1000 * 60 * 60 * 24) + 1;
        return days;
    }

    /**
     * 判断是否闰年
     * 
     * @param curDate
     * @return
     */
    private boolean isLeapYear(int year) {
        return (year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 根据一段时间区间，按月份拆分成多个时间段
     * 
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return
     */
    public List<PeriodSequence> getPeriodSequence(Date beginDate, Date endDate) {
        List<PeriodSequence> periodSequences = new ArrayList<PeriodSequence>();
        PeriodSequence periodSequence = null;
        // 定义中间时间变量
        Date curDate = beginDate;
        // 如果当前时间小于结束时间
        while (curDate.compareTo(endDate) <= 0) {
            periodSequence = new PeriodSequence();
            // 最早起始日期
            periodSequence.setYearlyBeginDate(beginDate);
            // 本期起始日期
            periodSequence.setCurBeginDate(curDate);
            periodSequence.setCurEndDate(getLastDateOfMonth(curDate, endDate));
            periodSequence.setYearlyEndDate(endDate);
            // 设定下个期间开始日期是下月一号
            curDate = getNextMonthFirstDate(curDate);
            periodSequences.add(periodSequence);
        }
        return periodSequences;
    }

    /**
     * 获取本月最后一天
     * 
     * @param curDate
     * @param endDate
     * @return
     */
    private Date getLastDateOfMonth(Date curDate, Date endDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (!CommonUtil.isNullorEmpty(endDate) && calendar.getTime().before(endDate)) {
            return calendar.getTime();
        } else {
            return endDate;
        }
    }

    /**
     * 获取下月第一天
     * 
     * @return
     */
    private Date getNextMonthFirstDate(Date curDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获取当月天数
     * 
     * @param date
     * @return
     */
    public static long getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 计费进度
     * 
     * @return
     */
    public Map<String, String> getIncomeForecastProgress() {
        return ForecastProgress.forecastProgress;

    }

    /**
     * 重置变量
     */
    public void resetIncomeForecastStatus() {
        ForecastProgress.forecastProgress = new HashMap<String, String>();

    }

    /**
     * 获取通用报表查询数据
     * 
     * @param form
     * @return
     */
    public List<CommonChartData> getForecastResult(IncomeForecastForm form) {
        User currentUser = UserUtils.getUser();
        form.setDept(getDept(currentUser, "office", ""));
        return incomeForecastMybatisDao.getForecastResult(form);
    }

    /**
     * 保底计算
     * 
     * @param minMax
     * @param period
     * @param forecastData
     * @return
     * @throws Exception
     */
    public Double getMinConsume(MinMaxConsumeDataBean minMax, PeriodSequence period, IncomeForecastDataBean forecastData) throws Exception {
        // 保底计算
        Double minConsume = 0.0;
        if (!CommonUtil.isNullorEmpty(minMax.getMin_consume())) {
            minConsume = minMax.getMin_consume() * minMax.getSplit_ratio() / 100;
            if ("2".equals(minMax.getMin_type())) {
                // 月保底
                minConsume = minConsume * getDateDiff(period.getCurEndDate(), period.getCurBeginDate()) / getDaysOfMonth(period.getCurBeginDate());
            } else if ("3".equals(minMax.getMin_type())) {
                // 日保底  
                minConsume = minConsume * getDateDiff(period.getCurEndDate(), period.getCurBeginDate());
            } else {
                // 年保底、不定期保底
                long diffDays = getDateDiff(getDateByString(minMax.getProd_end_date()), getDateByString(minMax.getProd_begin_date()));
                minConsume = minConsume * getDateDiff(period.getCurEndDate(), period.getCurBeginDate()) / diffDays;
            }
        }
        return minConsume;
    }

}
