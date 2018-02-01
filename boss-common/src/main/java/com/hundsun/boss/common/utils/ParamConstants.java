package com.hundsun.boss.common.utils;

/**
 * 参数常量.
 * 
 * @author chenhl.
 * 
 */
public class ParamConstants {

    //hsid 参数常量  --------- start
	/**
	 * .
	 */
	public static final String AUTH_ID = "auth_id";
	/**
	 * .
	 */
	public static final String USER_ID = "user_id";
	/**
	 * .
	 */
	public static final String USER_STATUS = "user_status";
	/**
	 * .
	 */
	public static final String USER_TOKEN = "user_token";
	/**
	 * .
	 */
	public static final String AUTH_USERNAME = "auth_username";
	/**
	 * .
	 */
	public static final String COMPANY_ID =  "company_id";
	/**
	 * .
	 */
	public static final String BUSINSYS_NO = "businsys_no";
	/**
	 * .
	 */
	public static final String MOBILE_TEL = "mobile_tel";
	/**
	 * .
	 */
	public static final String MOBILE_TEL_STR = "mobile_tel_str";
	/**
	 * .
	 */
	public static final String EMAIL = "email";
	
	/**
	 * .
	 */
	public static final String AUTHCHECK_ID = "authcheck_id";
	/**
	 * .
	 */
	public static final String AUTH_CHECK_CODE = "auth_check_code";
	/**
	 * .
	 */
    public static final  String LOCK_FLAG="lock_flag";
    /**
	 * .
	 */
    public static final  String REG_SOURCE="reg_source";
    /**
	 * .
	 */
	public static final String OUT_SEND_FLAG = "out_send_flag";
	/**
	 * .
	 */
	public static final String AUTHMSG_BUSI_TYPE = "authmsg_busi_type";
	/**
	 * .
	 */
	public static final String AUTH_CHECK_WAY = "auth_check_way";
	

	/**
	 * .
	 */
	public static final String EMAIL_TO = "email_to";
	/**
	 * .
	 */
	public static final String EMAIL_CC = "email_cc";
	/**
	 * .
	 */
	public static final String EMAIL_BCC = "email_bcc";
	/**
	 * .
	 */
	public static final String EMAIL_SUBJECT = "email_subject";
	/**
	 * .
	 */
	public static final String EMAIL_TEXT = "email_text";
	/**
	 * .
	 */
	public static final String BUSINSYS_ID = "businsys_id";
	/**
	 * .
	 */
	public static final String SMS_CONTENT = "sms_content";
	/**
	 * .
	 */
	public static final String EMAIL_ACTIVE_FLAG = "email_active_flag";
	/**
	 * .
	 */
	public static final String REALNAME_FLAG = "realname_flag";
	/**
	 * .
	 */
	public static final String OWN_COMPANY_ID = "own_company_id";
	/**
	 * .
	 */
	public static final String SMS_PRIORITY = "sms_priority";
	/**
	 * .
	 */
	public static final String EMAIL_PRIORITY = "email_priority";
	/**
	 * .
	 */
	public static final String SMS_ID = "sms_id";
	/**
	 * .
	 */
	public static final String EMAIL_ID ="email_id";
	/**
	 * .
	 */
	public static final String LOGIN_NAME = "login_name";
	/**
	 * .
	 */
	public static final String REAL_NAME = "real_name";
	/**
	 * .
	 */
	public static final String ID_KIND = "id_kind";
	/**
	 * .
	 */
	public static final String ID_NO = "id_no";
	/**
	 * 业务系统账号.
	 */
	public static final String BUSIN_ACCOUNT = "busin_account";
	/**
	 * 公司名.
	 */
	public static final String COMPANY_NAME = "company_name";
	/**
	 * 认证提供商机构代码.
	 */
	public static final String OAUTHPROV_CODE= "oauthprov_code";
	/**
	 * 关联账号--第三方关联账号（对方的OpenID）.
	 */
	public static final String RELATION_ACT = "relation_acct";
	/**
	 * 安全验证key.
	 */
	public static final String SMS_AUTHKEY = "sms_authkey";
	//hsid 参数常量  --------- end
	
	//hsid 参数常量值 --------start
	/**
	 * .
	 */
	public static final String EMAIL_SUBJECT_OBLIGATE_QUESTION_VALUE = "【恒生网络】预留问题";
	/**
	 * .
	 */
	public static final String EMAIL_SUBJECT_REGISTER_VALUE = "【恒生网络】注册";
	/**
	 * .
	 */
	public static final String EMAIL_SUBJECT_LOGIN_VALUE = "【恒生网络】登录";
	/**
	 * .
	 */
	public static final String EMAIL_SUBJECT_CHANGE_CREDENTIALS_VALUE = "【恒生网络】认证信息修改";
	/**
	 * .
	 */
	public static final String EMAIL_SUBJECT_AUTH = "【恒生网络】实名认证-证件审核申请";
	/**
	 * .
	 */
	public static final String EMAIL_SUBJECT_VATINFO_NOT_PASS = "【恒生网络】专票信息不通过";
	/**
	 * .
	 */
	public static final String EMAIL_SUBJECT_BOUND ="【恒生网络】恒生账号-邮箱修改";
	/**
	 * .
	 */
	public static final String EMAIL_TEXT_OBLIGATE_QUESTION_VALUE = "【恒生网络】预留问题校验码";
	/**
	 * .
	 */
	public static final String EMAIL_TEXT_REGISTER_VALUE = "【恒生网络】注册校验码";
	/**
	 * .
	 */
	public static final String EMAIL_TEXT_LOGIN_VALUE = "【恒生网络】登录校验码";
	/**
	 * .
	 */
	public static final String EMAIL_TEXT_AUTH = "【恒生网络】实名认证-证件审核申请";

	/**
	 * .
	 */
	public static final String SMS_CONTENT_REALNAME_AUTH_SUCCESS_VALUE = "亲爱的用户，您在爱腾金融云网站（hs.net）的实名认证已通过，您可以通过网站获取更多信息 【恒生网络】";
	/**
	 * .
	 */
	public static final String SMS_CONTENT_REALNAME_AUTH_FAILD_VALUE = "亲爱的用户，您在爱腾金融云网站（hs.net）的实名认证未通过，请您补充或修改认证信息，或致电0571-28829888咨询 【恒生网络】。";
	/**
	 * .
	 */
	public static final String SMS_CONTENT_INVOICE_AUTH_FAILD_VALUE="亲爱的用户，您在爱腾金融云网站（hs.net）的专票信息不正确，无法完成开票，请您重新核对订单专票信息或与网站客服联系，客服电话0571-28829888。【恒生网络】";
	
	//hsid 参数常量值   -------- end
	
	/**
	 * 预留问题校验码.
	 */
	public static final String  AUTHMSG_BUSI_TYPE_1 = "1";
	/**
	 * 重置密码校验.
	 */
	public static final String  AUTHMSG_BUSI_TYPE_2 = "2";
	/**
	 * 注册校验.
	 */
	public static final String  AUTHMSG_BUSI_TYPE_3 = "3";
	/**
	 * 登录校验.
	 */
	public static final String  AUTHMSG_BUSI_TYPE_4 = "4";
	/**
	 * 修改密码校验.
	 */
	public static final String  AUTHMSG_BUSI_TYPE_5 = "5";
	/**
	 * 认证信息修改.
	 */
	public static final String  AUTHMSG_BUSI_TYPE_6 = "6";
	/**
	 * 绑定手机.
	 */
	public static final String  AUTHMSG_BUSI_TYPE_7 = "7";
	/**
	 * 修改手机.
	 */
	public static final String  AUTHMSG_BUSI_TYPE_8 = "8";
	/**
	 * 实名认证已通过.
	 */
	public static final String  AUTHMSG_BUSI_TYPE_9 = "9";
	/**
	 * 实名认证未通过.
	 */
	public static final String  AUTHMSG_BUSI_TYPE_10 = "10";
	/**
	 * 专票信息不正确.
	 */
	public static final String  AUTHMSG_BUSI_TYPE_11 = "11";
	
	
	/**
	 * .
	 */
	public static final String ERROR_INFO = "error_info";
	/**
	 * .
	 */
	public static final String ERROR_NO = "error_no";
	
	
	//public static final String COMPANY_ID_VALUE9100 =  "91000";
    // 2 协议必须字段缺失 报文中协议规范规定的字段缺失
	/**
	 * .
	 */
    public static final String ERROR_NO_2 = "2";
    // 3 根据规范，一个或多个字段的值无法识别或不符合格式要求 例如，非数字，或者不是有效的日期格式，数据字典不符，版本号格式错误等等
    /**
	 * .
	 */
    public static final String ERROR_NO_3 = "3";
    // 9 请求要求的功能不支持
    /**
	 * .
	 */
    public static final String ERROR_NO_9 = "9";

    // jers异常
    /**
	 * .
	 */
    public static final String JRES_ERROR_NO_SERVICE_NOT_FOUND = "256";
    /**
	 * .
	 */
    public static final String JRES_ERROR_NO_UNKNOW = "-1";
    
    // 用户锁定标识  0 否 1 是
    /**
	 * .
	 */
    public static final String LOCK_FLAG_0 = "0";
    /**
	 * .
	 */
    public static final String LOCK_FLAG_1 = "1";
    /**
	 * .
	 */
    public static final String REG_SOURCE_VALUE="5000";
    // 邮箱激活标识 0 否 1 是
    /**
	 * .
	 */
	public static final String EMAIL_ACTIVE_FLAG_0 = "0";
	/**
	 * .
	 */
	public static final String EMAIL_ACTIVE_FLAG_1 = "1";
	//实名标识 0 否 1 是
	/**
	 * .
	 */
	public static final String REALNAME_FLAG_0 = "0";
	/**
	 * .
	 */
	public static final String REALNAME_FLAG_1 = "1";
	
	//session拦截处理异常未登录或者session过期
	/**
	 * .
	 */
	public static final String INTERCEPTOR_NO_LOGIN = "291";
	//拦截处理异常缺少必传参数
	/**
	 * .
	 */
	public static final String INTERCEPTOR_MISS_PARAMETER = "292";
	
	//在线商城 自定义参数类型.
	/**
	 * .
	 */
	public  static final String DEFINITION_TYPE ="1";
	
	// 用户状态  deal_status:处理状态-0:未审核  deal_status:处理状态-1:待审核deal_status:处理状态-8:审核成功deal_status:处理状态-9:审核失败
	/**
	 * .
	 */
	public  static final String DEAL_STATUS ="8";
	/**
	 * 处理状态审核失败.
	 */
	public  static final String DEAL_FAIL_STATUS ="9";
	/**
	 * 处理状态未审核.
	 */
	public  static final String DEAL_STATUS0 ="0";
	/**
	 * 处理状态待审核.
	 */
	public  static final String DEAL_STATUS1 ="1";
	//订单状态 ---------------------------------------- start 
	/**
	 * 订单状态-0:待生效.
	 */
	public static final String ORDER_STATUS0="0";
	/**
	 * 订单状态-1:待付款.
	 */
	public static final String ORDER_STATUS1="1";
	/**
	 * 订单状态-2:支付中.
	 */
	public static final String ORDER_STATUS2="2";
	/**
	 * 订单状态-3:已支付.
	 */
	public static final String ORDER_STATUS3="3";
	/**
	 * 订单状态-4:已交付.
	 */
	public static final String ORDER_STATUS4="4";
	//订单状态----------------------------------------- end
	
	/**应答返回——错误编号,标志是正确应答还是错误应答.*/
	public static final String RESPONSE_ERROR_NO = "error_no";
	/**应答返回——错误信息,对错误信息的描述.*/
	public static final String RESPONSE_ERROR_INFO = "error_info";
	/**应答返回——错误编号,具体系统定义错误码.*/
	public static final String RESPONSE_ERROR_CODE = "error_code";
	/**应答返回——系统辅助信息,用于排查定位问题.*/
	public static final String RESPONSE_ERROR_EXTINFO = "error_extinfo";
	/**discount_type:折扣类型-1: level2产品，按月 40 ，按年  360        折扣类型-2:期限满减      折扣类型-3:价格满减       折扣类型-4:满送. 折扣类型-5  每接入5家券商，月接入费用优惠1000元.  折扣类型-6  券商基础包 5家券商  为基础.*/
	public static final String DISCOUNT_TYPE1="1";
	/**discount_type:折扣类型-1: level2产品，按月 40 ，按年  360        折扣类型-2:期限满减      折扣类型-3:价格满减       折扣类型-4:满送.  折扣类型-5  每接入5家券商，月接入费用优惠1000元.  折扣类型-6  券商基础包 5家券商  为基础.*/
	public static final String DISCOUNT_TYPE2="2";
	/**discount_type:折扣类型-1: level2产品，按月 40 ，按年  360        折扣类型-2:期限满减      折扣类型-3:价格满减       折扣类型-4:满送.  折扣类型-5  每接入5家券商，月接入费用优惠1000元.  折扣类型-6  券商基础包 5家券商  为基础.*/
	public static final String DISCOUNT_TYPE3="3";
	/**discount_type:折扣类型-1: level2产品，按月 40 ，按年  360        折扣类型-2:期限满减      折扣类型-3:价格满减       折扣类型-4:满送. 折扣类型-5  每接入5家券商，月接入费用优惠1000元.  折扣类型-6  券商基础包 5家券商  为基础.*/
	public static final String DISCOUNT_TYPE4="4";
	/**discount_type:折扣类型-1: level2产品，按月 40 ，按年  360       折扣类型-2:期限满减      折扣类型-3:价格满减     折扣类型-5  每接入5家券商，月接入费用优惠1000元.  折扣类型-6  券商基础包 5家券商  为基础.*/
	public static final String DISCOUNT_TYPE5="5";
	
	/**discount_type:折扣类型-1: level2产品，按月 40 ，按年  360        折扣类型-2:期限满减      折扣类型-3:价格满减       折扣类型-4:满送.   折扣类型-4:满送.  折扣类型-5  每接入5家券商，月接入费用优惠1000元.  折扣类型-6  券商基础包 5家券商  为基础.*/
	public static final String DISCOUNT_TYPE6="6";
	
	/**cart_modi:购物车是否可以编辑  0 否   ， 1 是.   */
	public static final String CART_MODI="0";
	/**激活.*/
	public static final String ACTIVE = "1";
	/**未激活.*/
	public static final String NOT_ACTIVE ="0";
	/**手机号码格式不对提示.*/
	public static final String ERROR_INPUT_MOBILE = "不是有效手机号码";
	/**地址数量过多提示.*/
	public static final String TOO_MUCH_ADDRESS = "亲，您的地址数量已经够多了";
	/**删除默认地址提示.*/
	public static final String DELETE_DEFAULT_ADDRESS = "默认地址不可删除";
	/**请先激活错误号.*/
	public static final String TO_ACTIVE_ERROR_NO = "290";
	/**请先激活消息.*/
	public static final String TO_ACTIVE_ERROR_MSG = "请先激活您的邮箱";
	
	
	/**邮件中参数type类型(1、注册激活;2、绑定邮箱;3、修改邮箱;4、重置密码)======start.*/
	/**type值,注册激活.*/
	public static final String TYPE_VALUE1 = "1";
	/**type值,绑定邮箱.*/
	public static final String TYPE_VALUE2 = "2";
	/**type值,修改邮箱.*/
	public static final String TYPE_VALUE3 = "3";
	/**type值,重置密码.*/
	public static final String TYPE_VALUE4 = "4";
	/**邮件中参数type类型(1、注册激活;2、绑定邮箱;3、修改邮箱;4、重置密码)======end.*/
	
	/**邮箱已被注册code.*/
	public static final String REGERIED_ERROR_CODE ="5103022";
	/**邮箱已被注册message.*/
	public static final String REGERIED_ERROR_MESSAGE ="邮箱已被注册";
	/**手机已被注册code.*/
	public static final String REGERIED_ERROR_MOBILE_CODE ="5103023";
	/**手机已被注册message.*/
	public static final String REGERIED_ERROR_MOBILE_MESSAGE ="手机号码已被注册";
	
	/**默认地址.*/
	public static final String DEFALUT_ADDRESS = "1";
	
	/**
	 * 支付流水支付状态 payment_status 0  未支付 .
	 */
	public static final String PAYMENT_STATUS_UNPAY = "0";
	
	/**
	 * 支付流水支付状态 payment_status 1  已支付.
	 */
	public static final String PAYMENT_STATUS_PAYED = "1";
	
	/**
	 * 支付流水号 前缀.
	 */
	public static final String PAY_PREFIX ="p";
	
	/**
	 * 企业类别1.
	 */
	public static final String COMPANY_TYPE1="1";
	/**
	 * 企业类别2.
	 */
	public static final String COMPANY_TYPE2="2";
	/**
	 * 合约类型1.
	 */
	public static final String AGREE_TYPE1="1";
	/**
	 * 合约类型2.
	 */
	public static final String AGREE_TYPE2="2";
	/**
	 *业务上架1.
	 */
	public static final String SHELVES_STATUS1="1";
	/**
	 * 业务对接未通过.
	 */
	public static final String REQUEST_NO_PASS="9";
	
	/**
	 * 证件类别——身份证.
	 */
	public static final String ID_KIND0 = "0";
	
	/**
	 * 证件类别——营业执照.
	 */
	public static final String ID_KIND2 = "2";
	/**
	 * 用户类型——企业.
	 */
	public static final String USER_TYPE_VALUE1 = "1";
	/**
	 * 用户类型——个人.
	 */
	public static final String USER_TYPE_VALUE2 = "2";
	/**
	 * 业务状态——已申请.
	 */
	public static final String SERVICE_STATUS1 = "1";
	/**
	 * 业务状态——已同意.
	 */
	public static final String SERVICE_STATUS2 = "2";
	/**
	 * 业务状态——对接中.
	 */
	public static final String SERVICE_STATUS3 = "3";
	/**
	 * 业务状态——待上线.
	 */
	public static final String SERVICE_STATUS4 = "4";
	/**
	 * 业务状态——已上线.
	 */
	public static final String SERVICE_STATUS5 = "5";
	/**
	 * 业务状态——下线.
	 */
	public static final String SERVICE_STATUS6 = "6";
	/**
	 * 业务状态——已驳回.
	 */
	public static final String SERVICE_STATUS9 = "9";
	
	/**
	 * 报表类型 1-主账户信息查询表 .
	 */
	public static final String REPORT_TYPE_UPACCOUNT = "1";
	/**
	 * 报表类型 2-子账户信息查询表 .
	 */
	public static final String REPORT_TYPE_SUBACCOUNT = "2";
	/**
	 * 报表类型 3-持仓查询表 .
	 */
	public static final String REPORT_TYPE_POSITION = "3";
	/**
	 * 报表类型 4-委托查询表 .
	 */
	public static final String REPORT_TYPE_ENTRUST = "4";
	/**
	 * 报表类型 5-成交查询表 .
	 */
	public static final String REPORT_TYPE_DEAL = "5";
	/**
	 * 短信验证码长度.
	 */
	public static final String LENGTH_NEED = "length_need";
	/**
	 * 短信验证码长度值.
	 */
	public static final String LENGTH_NEED_VALUE = "6";
	/**
	 * 用户来源 1-网站.
	 */
	public static final String USER_SOURCE_1 = "1";
	/**
	 * 用户来源 2-HOMES.
	 */
	public static final String USER_SOURCE_2 = "2";
	/**
	 * 用户来源 3-决战股海.
	 */
	public static final String USER_SOURCE_3 = "3";
	/**创建免密码标志 0-不创建.*/
	public static final String CREATE_FLAG_0 = "0";
	/**创建免密码标志 1-创建.*/
	public static final String CREATE_FLAG_1 = "1";
	/**绑定匿名码与用户的关系返回 0-匿名码之前已经被绑定.*/
	public static final String RETURN_CODE_0 = "0";
	/**绑定匿名码与用户的关系返回 1-绑定成功.*/
	public static final String RETURN_CODE_1 = "1";
	/**绑定匿名码与用户的关系返回 2-无效匿名码.*/
	public static final String RETURN_CODE_2 = "2";
	/**绑定匿名码与用户的关系返回 3-用户已经绑定过匿名码.*/
	public static final String RETURN_CODE_3 = "3";
	/**查询历史标志.*/
	public static final String HIS_STATUS = "1";
	/**外部发送标志,1-调用者自身发送短信或邮件.*/
	public static final String OUT_SEDN_FLAG_1 = "1";
	
	/**秘钥对.*/
	public static final String KEYPAIR = "KeyPair";
}
