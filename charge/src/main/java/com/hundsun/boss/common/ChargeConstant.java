package com.hundsun.boss.common;

public class ChargeConstant {
    private ChargeConstant() {
    }

    /** 结算周期-月. **/
    public final static String PAYMENT_CYLE_MONTH = "1";
    /** 结算周期-季. **/
    public final static String PAYMENT_CYLE_SEASON = "2";
    /** 结算周期-年. **/
    public final static String PAYMENT_CYLE_YEAR = "3";

    /** 计费模式-固定. **/
    public final static String PAYMENT_MODEL_FIXED = "1";
    /** 计费模式-流量. **/
    public final static String PAYMENT_MODEL_FLOW = "2";

    /** 阶梯收费. **/
    public final static String STEP_FEE = "1";
    /** 非阶梯收费. **/
    public final static String STEP_FEE_NOT = "0";

    /** 日均 **/
    public final static String STEP_TYPE_DAY = "1";
    /** 总量 **/
    public final static String STEP_TYPE_TOTLE = "2";

    /** 待计费 **/
    public final static String RECEIPT_STAUS_INIT = "1";
    /** 已计费 **/
    public final static String RECEIPT_STAUS_PAY = "2";
    /** 已出账 **/
    public final static String RECEIPT_STAUS_ACCOUNT = "3";
    /** 已合并 **/
    public final static String RECEIPT_STAUS_CHILD = "4";
    /** 不计费 **/
    public final static String RECEIPT_STAUS_NOT_CHARGE = "5";

    /** 阶梯区间后包含， MIN<N<=MAX **/
    public final static String STEP_INTERVAL_AFTER_INCLUDE = "1";
    /** 阶梯区间前包含， MIN<=N<MAX **/
    public final static String STEP_INTERVAL_FRONT_INCLUDE = "2";

    public final static String NOT_FIND_USER_ID = "客户编号未关联上";

    public final static String NOT_FIND_ORDER = "客户没有有效订单";

    public final static String NOT_FIND_PRODUCT_CODE = "产品编号未关联上";

    public final static String ID_NOT_FIND_USER_ID = "2";

    public final static String ID_NOT_FIND_ORDER = "3";

    public final static String ID_NOT_FIND_PRODUCT_CODE = "1";

    public final static String NOT_FIND_HS_USER_ID = "协同客户编号未关联上";

    public final static String CHARGE_VALUE_ERROR = "计费数值异常";

    public final static String NOT_FIND_BILL_ID = "无此账单编号";

    public final static String BILL_STATUS_ERROR = "账单状态异常，不允许进行单笔应收账款登记";

    public final static String GET_CREDIT_LOCK_ERROR = "正在进行应收账款登记，请等待";

    /** 按日均资产比例 **/
    public final static String AVG_ASSET_FEE_MODEL = "1";

    /** 按资产比例 **/
    public final static String ASSET_FEE_MODEL = "2";

    /** 按交易所手续费比例 **/
    public final static String FUTURES_FEE_MODEL = "3";

    /** 按流量比例 **/
    public final static String FLOW_FEE_MODEL = "4";

    public final static String ACCOUNT_COUNT_FEE_MODEL = "5";

    public final static String LINE_COUNT_FEE_MODEL = "6";

    /** 日均交易量 **/
    public final static String DEAL_SUM_FEE_MODEL = "7";

    /** 个税资产规模 **/
    public final static String GS_ASSET_FEE_MODEL = "8";

    /**
     * 错误数据来源
     */
    public final static String WRONG_SOURCE_PREPARE = "1";
    /**
     * 预处理需要更新数据
     */
    public final static Integer NEED_TO_UPDATE_DATA = 1;
    /**
     * 预处理不需要更新数据
     */
    public final static Integer NOT_NEED_TO_UPDATE_DATA = 0;

    /** bill_status:帐单状态-1:待审核 **/
    public static final String BILL_STATUS_PREPARE = "1";

    /** bill_status:帐单状态-2:审核通过 **/
    public static final String BILL_STATUS_APPROVE = "2";

    /** bill_status:帐单状态-3:审核未通过 **/
    public static final String BILL_STATUS_UNAPPROVE = "3";

    /** bill_status:帐单状态-4:挂帐中 **/
    public static final String BILL_STATUS_CREDITING = "4";

    /** bill_status:帐单状态-5:已挂帐 **/
    public static final String BILL_STATUS_CREDITED = "5";

    /** bill_status:帐单状态-6:已销帐 **/
    public static final String BILL_STATUS_WRITE_OFF = "6";
    /** bill_status:帐单状态-7:部分销帐 **/
    public static final String BILL_STATUS_WRITE_PARTOFF = "7";
    /** bill_status:帐单状态-8:销帐失败 **/
    public static final String BILL_STATUS__FAIL = "8";

    /** bill_status:帐单状态-7:发送账单审核通过 **/
    public static final String BILL_STATUS_EMAIL_APPROVE = "7";

    /** bill_status:帐单状态-8:发送账单审核未通过 **/
    public static final String BILL_STATUS_EMAIL_UNAPPROVE = "8";

    /** bill_status:帐单状态-9:挂账失败 **/
    public static final String BILL_STATUS_CREDIT_FAIL = "9";

    /** bill_status:帐单状态-10:无需销账 **/
    public static final String BILL_STATUS_NOT_NEED_WRITE_OFF = "10";

    /**
     * 到款状态开始 0-未匹配.
     */
    public static final String MALL_STATUS_NO = "0";
    /**
     * 到款状态开始 1-匹配成功.
     */
    public static final String MALL_STATUS_PASS = "1";
    /**
     * 清分状态开始 0--未清分.
     */
    public static final String MALL_VERIFY_STATUS_NO = "0";
    /**
     * 清分状态开始 1--清分成功.
     */
    public static final String MALL_VERIFY_STATUS_YES = "1";
    /**
     * 清分状态开始 2--清分失败.
     */
    public static final String MALL_VERIFY_STATUS_FAIL = "2";
    /**
     * 清分类型 1--来款.
     */
    public static final String MALL_VERIFY_TYPE_BANK = "1";
    /**
     * 清分类型 2--余额.
     */
    public static final String MALL_VERIFY_TYPE_BALANCE = "2";
    /**
     * 到款状态开始 2-匹配失败.
     */
    public static final String MALL_STATUS_FAIL = "2";

    /** 错单状态-初始 **/
    public static final String WRONG_STATUS_NORMAL = "1";

    /*** 错单状态-审核未通过 **/
    public static final String WRONG_STATUS_INVALID = "2";

    /** 查询订单每次获取条数. */
    public static final Integer PAGESIZE_ACCOUNT = 500;
    /** 查询清分失败的获取条数. */
    public static final Integer PAGESIZE_CLEARING = 500;
    /** 客户类型 1-普通客户. */
    public static final String AGENCY_TYPE_1 = "1";
    /** 客户类型 2-渠道客户. */
    public static final String AGENCY_TYPE_2 = "2";
    /** 客户类型 3-渠道子客户. */
    public static final String AGENCY_TYPE_3 = "3";
    /** 是否需要扣费标志 0-否. */
    public static final String BILL_DEDUCT_FLAG_0 = "0";
    /** 是否需要扣费标志 1-是. */
    public static final String BILL_DEDUCT_FLAG_1 = "1";
    /** 账单发送审核状态 0:默认值. */
    public static final String BILL_SEND_STATUS_0 = "0";
    /** 账单发送审核状态 1:待审核. */
    public static final String BILL_SEND_STATUS_1 = "1";
    /** 账单发送审核状态 2:审核通过. */
    public static final String BILL_SEND_STATUS_2 = "2";
    /** 账单发送审核状态 3:审核未通过. */
    public static final String BILL_SEND_STATUS_3 = "3";
    /** 账单发送审核状态 4:已发送. */
    public static final String BILL_SEND_STATUS_4 = "4";
    /** 账单发送审核状态 5:无需发送. */
    public static final String BILL_SEND_STATUS_5 = "5";
    /** 组合类型 0-单品. */
    public static final String COMBINE_TYPE_0 = "0";
    /** 组合类型 1-组合. */
    public static final String COMBINE_TYPE_1 = "1";
    /** 组合类型 2-组合明细. */
    public static final String COMBINE_TYPE_2 = "2";
    /** 组合类型 3-阶梯. */
    public static final String COMBINE_TYPE_3 = "3";
    /** 组合类型 4-阶梯明细. */
    public static final String COMBINE_TYPE_4 = "4";
    public static final String WRONG_STATUS_PREPARE = "1";

    public static final String WRONG_STATUS_ABANDON = "2";
    /** 保底类型 1-按年保底. */
    public static final String MIN_TYPE_YEAR = "1";
    /** 保底类型 2-按月保底. */
    public static final String MIN_TYPE_MONTH = "2";
    /** 业务类型 1-预处理. */
    public static final String BUSI_TYPE_1 = "1";
    /** 业务类型 2-批价. */
    public static final String BUSI_TYPE_2 = "2";
    /** 业务类型 3-出账. */
    public static final String BUSI_TYPE_3 = "3";
    /** 操作结果 0-失败. */
    public static final String OPER_RESULT_FAIL = "0";
    /** 操作结果 1-成功. */
    public static final String OPER_RESULT_SUCCESS = "1";
    /** 单笔应收账款登记接口 **/
    public static final String CREDIT_SERVICE = "815001.servicejson";
    /** 单笔应收账款登记接口功能号 **/
    public static final String CREDIT_SERVICE_FUNCTIONID = "815001";
    /** 单笔应收账款登记接口 **/
    public static final String CANCEL_CREDIT_SERVICE = "815002.servicejson";
    /** 单笔应收账款登记接口功能号 **/
    public static final String CCANCEL_REDIT_SERVICE_FUNCTIONID = "815002";
    /**
     * 到款匹配指定合同号接口.
     */
    public static final String RECEIPT_CONTRACT_SERVICE = "815020.servicejson";
    /**
     * 挂账清分接口.
     */
    public static final String CREDIT_CLEARING_SERVICE = "825009.servicejson";
    /**
     * 客户可用余额接口.
     */
    public static final String CREDIT_USERAVAILBALANCE_SERVICE = "815011.servicejson";
    /**
     * 账务到款匹配指定合同号功能号.
     */
    public static final String RECEIPT_CONTRACT_SERVICE_FUNCTIONID = "815020";
    /**
     * 账务到款匹配指定合同号功能号.
     */
    public static final String CREDIT_CLEARING_SERVICE_FUNCTIONID = "825009";
    /**
     * 客户可用余额查询功能号.
     */
    public static final String CREDIT_USERAVAILBALANCE_SERVICE_FUNCTIONID = "815011";

    public static final String DC_QUERY_DATA = "query/data";

    public static final String DC_QUERY_ASSET_DATA = "query/sumassetdata";

    public static final String DC_QUERY_ZG_ASSET = "query/zgDayasset";

    public static final String DC_QUERY_ZG_AVG_ASSET = "query/zgDayavgsasset";

    public static final String DC_QUERY_ZG_AVG_DISPLAY = "query/zgDayavgDisplay";

    public static final String DC_QUERY_ZG_DEAL_SUM = "query/zgDealsum";

    public static final String DC_QUERY_ZG_ACCOUNT_COUNT = "query/zgAccountcount";

    public static final String DC_QUERY_ZG_LINE_COUNT = "query/zgLinecount";

    public static final String DC_QUERY_JY_API_COUNT = "query/jvyApicount";

    public static final String PROTOCOL = "http";

    public static final String OPER_SETP_1 = "账单审核";

    public static final String OPER_SETP_2 = "重新出账";

    public static final String OPER_SETP_3 = "详单合并";

    public static final String OPER_SETP_4 = "账单发送审核";

    public static final String OPER_SETP_5 = "账单发送";

    public static final String OPER_SETP_6 = "累账";

    public static final String OPER_SETP_7 = "预处理校验";

    public static final String OPER_SETP_8 = "应收账款登记";

    public static final String OPER_OPERATE_NO_ADMIN = "charge";

    public static final String OPER_OPERATE_NAME_ADMIN = "计费系统";

    public static final String ACCOUNT_DOCKING_SUCCESS = "000000";
    /** 没有此客户关联账户. */
    public static final String ACCOUNT_DOCKING_0 = "009999";
    /** 是否生产账单 0:否. */
    public static final String IS_BILL_0 = "0";
    /** 是否生产账单 1:是. */
    public static final String IS_BILL_1 = "1";
    /**
     * 普通用户
     */
    public static final String AGENT_TYPE_1 = "1";
    /**
     * 渠道用户
     */
    public static final String AGENT_TYPE_2 = "2";
    /**
     * 渠道子用户
     */
    public static final String AGENT_TYPE_3 = "3";

    /** sku产品类型 0：普通 1：组合 2：组合明细 **/
    public static final String ORDER_PRODUCT_COMBINE_TYPE = "0";

    /** sku产品类型 0：普通 1：组合 2：组合明细 3：阶梯组合 4：阶梯明细 **/
    public static final String ORDER_PRODUCT_COMBINE_TYPE_GROUP = "1";

    /** sku产品类型 0：普通 1：组合 2：组合明细 3：阶梯组合 4：阶梯明细 **/
    public static final String ORDER_PRODUCT_COMBINE_TYPE_GROUP_DETAIL = "2";

    /** sku产品类型 0：普通 1：组合 2：组合明细 3：阶梯组合 4：阶梯明细 **/
    public static final String ORDER_PRODUCT_COMBINE_TYPE_GROUP_LADDER = "3";

    /** sku产品类型 0：普通 1：组合 2：组合明细 3：阶梯组合 4：阶梯明细 **/
    public static final String ORDER_PRODUCT_COMBINE_TYPE_GROUP_DETAIL_LADDER = "4";

    /** 计算上月欠款. */
    public static final String ACCOUNT_FLAG_1 = "1";
    /** 计算本月欠款. */
    public static final String ACCOUNT_FLAG_2 = "2";

    /** 是否产生流量时计费 0-否. */
    public static final String PRODUCE_FLOW_FLAG_0 = "0";
    /** 是否产生流量时计费 1-是. */
    public static final String PRODUCE_FLOW_FLAG_1 = "1";
    /** 最迟付款日相对计费结束时间延后天数. */
    public static final int PAYMENT_DEADLINE = 15;
    /** 强制出账值标志 0-否. */
    public static final String CONSTRAINT_ACCOUNT_FLAG_0 = "0";
    /** 强制出账值标志 1-是. */
    public static final String CONSTRAINT_ACCOUNT_FLAG_1 = "1";
    /** 是否封顶 0-否. */
    public static final String MAX_FLAG_NO = "0";
    /** 是否封顶 1-是. */
    public static final String MAX_FLAG_YES = "1";
    /** 是否保底 0-否. */
    public static final String MIN_FLAG_NO = "0";
    /** 是否保底 1-是. */
    public static final String MIN_FLAG_YES = "1";

    /** 阶梯单位 1-个 **/
    public static final String STEP_INTERVAL_GE = "1";
    /** 阶梯单位 2-万 **/
    public static final String STEP_INTERVAL_WAN = "2";
    /** 阶梯单位 3-亿 **/
    public static final String STEP_INTERVAL_YI = "3";

    /** 阶梯区间 右 -1 **/
    public static final String STEP_UINT_RIGHT = "1";
    /** 阶梯区间 左-2 **/
    public static final String STEP_UINT_LEFT = "2";

    /** prod_type:产品（组合）类型-0:普通. */
    public static final String PROD_TYPE_0 = "0";
    /** prod_type:产品（组合）类型-1:阶梯. */
    public static final String PROD_TYPE_1 = "1";

    /** 费率模式 1-日均资产规模 **/
    public static final String FEE_MODEL_RIJUN = "1";
    /** 费率模式 2-日资产规模 **/
    public static final String FEE_MODEL_RIZICHAN = "2";
    /** 费率模式 3-交易所手续费 **/
    public static final String FEE_MODEL_POUNDAGE = "3";
    /** 费率模式 4-交易量 **/
    public static final String FEE_MODEL_VOLUME = "4";
    /** 费率模式 5-产品个数 **/
    public static final String FEE_MODEL_NUMBER = "5";
    /** 费率模式 6-分仓线路 **/
    public static final String FEE_MODEL_FCXL = "6";
    /** 费率模式 7-日均交易量 **/
    public static final String FEE_MODEL_RIJUNVOLUME = "7";
    /** 费率模式 8-日资产规模（个税） **/
    public static final String FEE_MODEL_RIZIGS = "8";

    /** 费率模式 1-日均资产规模 **/
    public static final int FEE_MODEL_RIJUN_INT = 1;
    /** 费率模式 2-日资产规模 **/
    public static final int FEE_MODEL_RIZICHAN_INT = 2;
    /** 费率模式 3-交易所手续费 **/
    public static final int FEE_MODEL_POUNDAGE_INT = 3;
    /** 费率模式 4-交易量 **/
    public static final int FEE_MODEL_VOLUME_INT = 4;
    /** 费率模式 5-产品个数 **/
    public static final int FEE_MODEL_NUMBER_INT = 5;
    /** 费率模式 6-分仓线路 **/
    public static final int FEE_MODEL_FCXL_INT = 6;
    /** 费率模式 7-日均交易量 **/
    public static final int FEE_MODEL_RIJUNVOLUME_INT = 7;
    /** 费率模式 8-日资产规模（个税） **/
    public static final int FEE_MODEL_RIZIGS_INT = 8;

    /** 查询历史数据. */
    public static final String HIS_STATUS = "1";
    /**
     * 查询当前数据.
     */
    public static final String CURR_STATUS = "0";
    /** 调用fetchOriginalData的调用方 1-表示重新出账. */
    public static final String FETCH_ORI_DATA_FLAG_1 = "1";

    /** 是否需要账单审核 0-不需要. */
    public static final String IS_NEED_AUDIT_0 = "0";
    /** 是否需要账单审核 1-需要. */
    public static final String IS_NEED_AUDIT_1 = "1";

    /**
     * 订单来源2--HAMP .
     */
    public static final String ORDER_SOURCE_HAMP = "2";
    /**
     * 订单来源4--资管云 .
     */
    public static final String ORDER_SOUCRCE_ASSET = "4";

    /**
     * 订单来源5--聚源 .
     */
    public static final String ORDER_SOURCE_JVYUAN = "5";

    /** 是否打折 1-打折. */
    public static final String IS_DISCOUNT_1 = "1";
    /** 是否打折 0-不打折. */
    public static final String IS_DISCOUNT_0 = "0";
    /** 折扣小数位数. */
    public static final int DISCOUNT_DIG = 5;
    /** 费率模式 5:产品个数. */
    public static final String FEE_MODEL_5 = "5";
    /** 费率模式 6:分仓线路. */
    public static final String FEE_MODEL_6 = "6";

    public static final String TYPE_FLOW = "01";

    public static final String TYPE_ASSET = "02";

    public static final String TYPE_PRODUCT_COUNT = "03";

    public static final String TYPE_LINE_COUNT = "04";

    public static final String TYPE_AVG_ASSET = "05";

    public static final String TYPE_AVG_DISPLAY = "06";

    public static final String SOURCE_HOMS = "02";

    public static final String SOURCE_ZG = "04";

    public static final String SOURCE_JY = "05";
    /** 收款周期-1:月. */
    public static final String COLLECT_CYCLE_MONTH = "1";
    /** 收款周期-2:季度. */
    public static final String COLLECT_CYCLE_SEASON = "2";

    /** 内部标志 1-被使用. */
    public static final String INNER_FLAG = "1";
    /** 收费标志 1-是. */
    public static final String SEASON_PAID_FLAG_1 = "1";
    /** 收费标志 0-否. */
    public static final String SEASON_PAID_FLAG_0 = "0";

    /** double类型的0.00 */
    public static final Double DOUBLE_0 = 0.00;

    /** double类型的1000.00 */
    public static final Double DOUBLE_1000 = 1000.00;

    /** 一年的天数365 */
    public static final int ONE_YEAR_DAY_INT = 365;

    /** 计费单位-天 */
    public static final String EXTRA_UNIT_DAY = "0";

    /** 计费单位-月 */
    public static final String EXTRA_UNIT_MONTH = "1";

    /** 计费单位-年 */
    public static final String EXTRA_UNIT_YEAR = "2";

    public static final String DATA_SOURCE_GDAAS = "0500";
    /**
     * 拼装插入语句头.
     */
    public static final String INSERTSQLHEADER = "insert into ";
    /**
     * 计费状态正常.
     */
    public static final String FEEMODEL_STATUS_OK = "1";
    /**
     * 计费状态取消.
     */
    public static final String FEEMODEL_STATUS_CANCEL = "2";
    /** 归属类型-合同. **/
    public final static String BELONG_TYPE_1 = "1";
    /** 归属类型-组合. **/
    public final static String BELONG_TYPE_2 = "2";
    /** 归属类型-产品. **/
    public final static String BELONG_TYPE_3 = "3";
}
