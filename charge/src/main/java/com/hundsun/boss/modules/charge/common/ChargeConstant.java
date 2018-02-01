package com.hundsun.boss.modules.charge.common;

public class ChargeConstant {
    /* 导出pdf用sessionkey */
    public final static String CONST_EXPORT_PDF_SESSION_KEY = "_export_pdf_key";
    /* 导出过程中存放sessionkey的名称 */
    public final static String PDF_SESSION_KEY = "PDF_SESSION_KEY";
    /* 导出pdf文件后缀名， 关键属性，不可修改 */
    public final static String CONST_DEFAULT_PDF_FILE_SUFFIX = "pdf";
    /* 到处过程中临时文件的后缀 */
    public final static String CONST_TEMP_FILE_SUFFIX = ".temp";
    /* pdf导出模板文件名称 */
    public final static String CONST_PDF_TEMPLATE_NAME = "CommonPdf.ftl";

    public final static String PDF_PAGE_TYPE = "A4 landscape";
    // 显示在页未，当前页数前的文字内容
    public final static String PAGE_COUNT_PREFIX = "";
    // 分隔当前页与总页数之间的文字内容
    public final static String PAGE_COUNT_MIDDLE = "/";
    // 显示在总页数后的文字内容
    public final static String PAGE_COUNT_SUFFIX = "";
    // 标题显示文字
    public final static String PDF_TITLE = "";

    /*
     * 一次性收费
     */
    public static final String PAY_MODEL_ONCE = "1";

    /*
     * 非一次性收费
     */
    public static final String PAY_MODEL_NOT_ONCE = "2";

    /*
     * 费率模式按产品个数
     */
    public static final String FEE_MODEL_BY_PROD_COUNT = "5";

    /*
     * combine_type:组合类型-0:单品
     */
    public static final String COMBINE_TYPE_SINGLE = "0";
    /*
     * combine_type:组合类型-1:普通组合 废弃
     */
    public static final String COMBINE_TYPE_NORMAL = "1";
    /*
     * combine_type:组合类型-2:普通组合明细 废弃
     */
    public static final String COMBINE_TYPE_NORMAL_DETAIL = "2";
    /*
     * combine_type:组合类型-3:阶梯组合 废弃
     */
    public static final String COMBINE_TYPE_LADDER = "3";
    /*
     * combine_type:组合类型-4:阶梯组合明细
     */
    public static final String COMBINE_TYPE_LADDER_DETAIL = "4";

    // ///////////////////////////////模板键值/////////////////////////////////////////////

    /*
     * 处理内容
     */
    public static final String CHARGE_ORDER_PROGRESS_CONTENT = "CONTENT";

    /*
     * 处理进度
     */
    public static final String CHARGE_ORDER_PROGRESS_PERCENT = "PERCENT";

    /*
     * 处理状态
     */
    public static final String CHARGE_ORDER_PROGRAM_STATUS = "PROGRAM_STATUS";

    /*
     * 数据中心 T2接口功能号
     */
    public static final String UNIFY_INTERFACE_FUNCTION_ID = "400500";

    /*
     * 数据中心 T2 接口视图名
     */
    public static final String UNIFY_INTERFACE_VIEW_NAME = "Common_Billing_interface_v";
    /*
     * 数据中心 T2接口 查询到达末尾
     */
    public static final String UNIFY_INTERFACE_QUERY_END_CODE = "52";

    public static final String UNTIL_MONTH = "201812";

    /*
     * 20 未开始
     */
    public static final String ORDER_STATUS_NOT_BEGIN = "20";
    /*
     * 29 不通过
     */
    public static final String ORDER_STATUS_FAIL = "29";
    /*
     * 30 合同未审核
     */
    public static final String ORDER_STATUS_UNVERIFY = "30";
    /*
     * 40 已审核
     */
    public static final String ORDER_STATUS_VERIFIED = "40";
    /*
     * 50 未计费
     */
    public static final String ORDER_STATUS_UNCHARGE = "50";
    /*
     * 60 未出账
     */
    public static final String ORDER_STATUS_NO_ACCOUNT = "60";
    /*
     * 70 未确认收入
     */
    public static final String ORDER_STATUS_INCOME_NOT_COMFIRM = "70";
    /*
     * 80 收入未推送
     */
    public static final String ORDER_STATUS_INCOME_NOT_POST = "80";
    /*
     * 90 正常计费
     */
    public static final String ORDER_STATUS_CHARGE_NORMALY = "90";
    /*
     * 95 计费结束
     */
    public static final String ORDER_STATUS_CHARGE_OUT_OF_TIME = "95";
    /*
     * 归属类型-合同.
     */
    public final static String BELONG_TYPE_1 = "1";
    /*
     * 归属类型-组合.
     */
    public final static String BELONG_TYPE_2 = "2";
    /*
     * 归属类型-产品.
     */
    public final static String BELONG_TYPE_3 = "3";
    /*
     * 发送标志-新增.
     */
    public final static String SEND_FLAG_CREATE = "create";
    /*
     * 发送标志-待发送.
     */
    public final static String SEND_FLAG_PENDING = "pending";
    /*
     * 发送标志-已发送.
     */
    public final static String SEND_FLAG_SENT = "sent";
}
