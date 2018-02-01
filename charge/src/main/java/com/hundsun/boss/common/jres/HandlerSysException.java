package com.hundsun.boss.common.jres;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hundsun.boss.common.utils.ParamConstants;
import com.hundsun.jres.cep.filter.Handler;
import com.hundsun.jres.interfaces.share.event.EventReturnCode;
import com.hundsun.jres.interfaces.share.event.IEvent;

/**
 * 系统异常统一错误信息处理
 * 
 * @author chenhl
 * 
 */
public class HandlerSysException extends Handler {
    private static final Logger LOGGER = LoggerFactory.getLogger(HandlerSysException.class);

    @Override
    protected ReturnCode handlerInPipeline(IEvent event) {
        return ReturnCode.PASS;
    }

    @SuppressWarnings("unused")
    @Override
    protected ReturnCode handlerOutPipeline(IEvent event) {
        if (event.getReturnCode() != EventReturnCode.I_OK) {
            LOGGER.error(event.getServiceAlias() + event.getErrorInfo() + event.getReturnCode());
            String errorNo = event.getErrorNo();
            String errorInfo = event.getErrorInfo();
            if (ParamConstants.JRES_ERROR_NO_UNKNOW.equals(errorNo)) {
                errorInfo = "系统未知异常";
            } else if (ParamConstants.JRES_ERROR_NO_SERVICE_NOT_FOUND.equals(errorNo)) {
                errorNo = ParamConstants.ERROR_NO_9;
                errorInfo = "请求的功能不支持";
            }
            return ReturnCode.STOP;
        }
        return ReturnCode.PASS;
    }
}
