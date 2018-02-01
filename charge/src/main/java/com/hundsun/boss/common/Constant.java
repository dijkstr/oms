package com.hundsun.boss.common;

public class Constant {
    /* 导出pdf用sessionkey */
    public final static String CONST_EXPORT_PDF_SESSION_KEY = "_export_pdf_key";
    /* 导出过程中存放sessionkey的名称 */
    public final static String PDF_SESSION_KEY = "PDF_SESSION_KEY";
    /* 导出pdf文件后缀名， 关键属性，不可修改 */
    public final static String CONST_DEFAULT_PDF_FILE_SUFFIX = "pdf";
    /* 到处过程中临时文件的后缀 */
    public final static String CONST_TEMP_FILE_SUFFIX = ".temp";

    /* pdf导出共同模板文件名称 */
    public final static String CONST_COMMON_PDF_TEMPLATE_NAME = "CommonPdf.ftl";
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

    /* xlsx导出用常量 */
    /* 每次查询数据条数 */
    public final static int CONST_EXPORT_SELECT_DATA_LIMIT = 100;
    /* 每次读入文件内容行数 */
    public final static int CONST_EXPORT_READ_FILE_ROW_COUNT = 1000;
    /* 每次读文件字节数 */
    public final static int CONST_READ_FILE_BYTE_COUNT = 1;
    /* 每次写文件字节数 */
    public final static int CONST_WRITE_FILE_BYTE_COUNT = 1;
    /* 导出文件后缀名， 关键属性，不可修改 */
    public final static String CONST_DEFAULT_FILE_SUFFIX = "xlsx";
    /* 导出相对路径 */
    public final static String CONST_EXPORT_ROOT_PATH = "/export";

    /* 保存导出文件相对路径的sessionkey内容后缀 */
    public final static String CONST_EXPORT_XLSX_SESSION_KEY = "_export_xlsx_key";
    /* 导出过程中错误信息前缀 */
    public final static String CONST_ERROR_CODE = "错误:";
    /* 导出过程中校验错误信息前缀 */
    public final static String CONST_VALID_ERROR_CODE = "校验错误:";
    /* 导出过程中业务错误信息前缀 */
    public final static String CONST_BUSINESS_ERROR_CODE = "业务错误:";
    /* 导出过程中提示信息前缀 */
    public final static String CONST_MESSAGE_CODE = "提示:";
    /* 导出过程中存放sessionkey的名称 */
    public final static String XLSX_SESSION_KEY = "XLSX_SESSION_KEY";
    /* 导出文件列头的字体（暂时没用） */
    public final static short CONST_EXPORT_XLSX_HEADER_FONT = 10;
    /* 导出文件内容的字体（暂时没用） */
    public final static short CONST_EXPORT_XLSX_CONTENT_FONT = 10;
    /* 为了防止过度读取，写入硬盘，设置的时间间隔 */
    public final static int CONST_SLEEP_TIME_FOR_EXPORT = 1000;
    /* 最大保留3位小数，金额类型 */
    public final static String CONST_XLSX_NUMBER = "0.###";
    /* xlsx导出用常量 */

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

    /////////////////////////////////模板键值/////////////////////////////////////////////
   /*
     * 聚源包年
     */
    public static final String JYBN = "jybn";

   /*
     * 聚源按次数
     */
    public static final String JYACS = "jyacs";

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
     * PDF字体文件存放路径
     */
    public static String pdf_font_file_path = "";
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
    
    public static final String UNTIL_MONTH = "201712";
    
    /*
     * 20	未开始
     */
    public static final String ORDER_STATUS_NOT_BEGIN = "20";
    /*
     * 30	合同未审核
     */
    public static final String ORDER_STATUS_UNVERIFY = "30";
    /*
     * 40	已审核
     */
    public static final String ORDER_STATUS_VERIFIED = "40";
    /*
     * 50	未计费
     */
    public static final String ORDER_STATUS_UNCHARGE = "50";
    /*
     * 60	未出账
     */
    public static final String ORDER_STATUS_NO_ACCOUNT = "60";
    /*
     * 70	未确认收入
     */
    public static final String ORDER_STATUS_INCOME_NOT_COMFIRM = "70";
    /*
     * 80	收入未推送
     */
    public static final String ORDER_STATUS_INCOME_NOT_POST = "80";
    /*
     * 90	正常计费
     */
    public static final String ORDER_STATUS_CHARGE_NORMALY = "90";
    /*
     * 95	计费结束
     */
    public static final String ORDER_STATUS_CHARGE_OUT_OF_TIME = "95";
    
}
