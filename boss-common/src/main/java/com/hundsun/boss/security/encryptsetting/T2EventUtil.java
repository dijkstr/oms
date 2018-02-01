package com.hundsun.boss.security.encryptsetting;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.hundsun.boss.base.exception.ServiceException;
import com.hundsun.boss.common.utils.ParamConstants;
import com.hundsun.jres.common.cep.context.ContextUtil;
import com.hundsun.jres.common.share.dataset.DatasetService;
import com.hundsun.jres.interfaces.cep.exception.TimeoutException;
import com.hundsun.jres.interfaces.share.dataset.IDataset;
import com.hundsun.jres.interfaces.share.event.EventReturnCode;
import com.hundsun.jres.interfaces.share.event.EventTagdef;
import com.hundsun.jres.interfaces.share.event.EventType;
import com.hundsun.jres.interfaces.share.event.IEvent;

/**
 * T2请求
 * 
 * @author chenhl
 *
 */
public class T2EventUtil {
    private static Logger logger = Logger.getLogger(T2EventUtil.class);

    public static void call(String funcNo, IDataset req) {
        IEvent event = ContextUtil.getServiceContext().getEventFactory().getEventByAlias(funcNo, EventType.ET_REQUEST);
        event.putEventData(req);
        IEvent result;
        try {
            result = ContextUtil.getServiceContext().getServiceClient().sendReceive(event);
        } catch (TimeoutException e) {
            logger.error(e.getMessage(), e);
            logger.error("errorNo:" + e.getErrorNo() + " errorInfo:" + e.getErrorInfo() + e.getErrorMessage());
            throw new ServiceException("调用服务异常");
        }
        if (result.getReturnCode() != EventReturnCode.I_OK) {
            logger.error("returnCode:" + result.getReturnCode() + " errorNo:" + result.getErrorNo() + " errorInfo:" + result.getErrorInfo());
            if ("-1".equals(result.getErrorNo())) {
                throw new ServiceException("调用服务异常");
            }
            throw new ServiceException(result.getErrorInfo());
        }
    }

    public static IDataset callResult(String funcNo, IDataset req) {
        IEvent event = ContextUtil.getServiceContext().getEventFactory().getEventByAlias(funcNo, EventType.ET_REQUEST);
        if (req != null) {
            event.putEventData(req);
        }
        IEvent result;
        try {
            result = ContextUtil.getServiceContext().getServiceClient().sendReceive(event);
        } catch (TimeoutException e) {
            logger.error(e.getMessage(), e);
            logger.error("errorNo:" + e.getErrorNo() + " errorInfo:" + e.getErrorInfo() + e.getErrorMessage());
            throw new ServiceException("调用服务异常");
        }
        if (result.getReturnCode() != EventReturnCode.I_OK) {
            String errorNo = result.getErrorNo();
            String errorInfo = result.getErrorInfo();
            logger.error("returnCode:" + result.getReturnCode() + " errorNo:" + result.getErrorNo() + " errorInfo:" + result.getErrorInfo());
            if ("-1".equals(result.getErrorNo())) {
                throw new ServiceException("调用服务异常");
            } else if ("0".equals(errorNo)) {
                if (result.getEventDatas() != null && result.getEventDatas().getDatasetCount() > 0) {
                    IDataset dataset = result.getEventDatas().getDataset(0);
                    Map<String, Object> map = DatasetService.getDefaultInstance().transformToMap(dataset);
                    errorNo = map.get(ParamConstants.ERROR_NO) + "";
                    errorInfo = map.get(ParamConstants.ERROR_INFO) + "";
                }
            }
            throw new ServiceException(errorInfo);
        }
        if (result.getEventDatas() != null && result.getEventDatas().getDatasetCount() > 0) {
            return result.getEventDatas().getDataset(0);
        }
        return null;
    }

    /**
     * 调用T2请求返回Map
     * 
     * @param funcNo
     * @param oneRow
     * @return
     */
    public static Map<String, Object> callResultMap(String funcNo, Map<String, Object> oneRow) {
        IEvent event = ContextUtil.getServiceContext().getEventFactory().getEventByAlias(funcNo, EventType.ET_REQUEST);

        return sendReceiveMap(event, oneRow);
    }

    /**
     * 调用T2请求返回Map 带子系统号
     * 
     * @param funcNo
     * @param oneRow
     * @param subSystemNo
     * @return
     */
    public static Map<String, Object> callResultMap(String funcNo, Map<String, Object> oneRow, String subSystemNo) {
        IEvent event = ContextUtil.getServiceContext().getEventFactory().getEventByAlias(funcNo, EventType.ET_REQUEST);

        if (StringUtils.isNotBlank(subSystemNo)) {
            event.setStringAttributeValue(EventTagdef.TAG_SUB_SYSTEM_NO, subSystemNo);
        }

        return sendReceiveMap(event, oneRow);
    }

    /**
     * 调用T2请求返回List<Map<String,Object>>
     * 
     * @param funcNo
     * @param oneRow
     * @return
     */
    public static List<Map<String, Object>> callResultListMap(String funcNo, Map<String, Object> oneRow) {
        IEvent event = ContextUtil.getServiceContext().getEventFactory().getEventByAlias(funcNo, EventType.ET_REQUEST);

        return sendReceiveListMap(event, oneRow);
    }

    /**
     * 调用T2请求返回List<Map<String,Object>> 带子系统号
     * 
     * @param funcNo
     * @param oneRow
     * @param subSystemNo
     * @return
     */
    public static List<Map<String, Object>> callResultListMap(String funcNo, Map<String, Object> oneRow, String subSystemNo) {
        IEvent event = ContextUtil.getServiceContext().getEventFactory().getEventByAlias(funcNo, EventType.ET_REQUEST);

        if (StringUtils.isNotBlank(subSystemNo)) {
            event.setStringAttributeValue(EventTagdef.TAG_SUB_SYSTEM_NO, subSystemNo);
        }
        return sendReceiveListMap(event, oneRow);
    }

    /**
     * 发送请求返回Map<String,Object>
     * 
     * @param event
     * @param oneRow
     * @return
     */
    private static Map<String, Object> sendReceiveMap(IEvent event, Map<String, Object> oneRow) {
        if (oneRow != null) {
            event.putEventData(DataSetConvertUtil.map2DataSet(oneRow));
        }
        IEvent result;
        try {
            result = ContextUtil.getServiceContext().getServiceClient().sendReceive(event);
        } catch (TimeoutException e) {
            logger.error("errorNo:" + e.getErrorNo() + " errorInfo:" + e.getErrorInfo() + e.getErrorMessage());
            logger.error(e.getMessage(), e);
            throw new ServiceException("调用服务异常 TimeoutException");
        }
        if (result.getReturnCode() != EventReturnCode.I_OK) {
            String errorNo = result.getErrorNo();
            String errorInfo = result.getErrorInfo();
            logger.error("returnCode:" + result.getReturnCode() + " errorNo:" + result.getErrorNo() + " errorInfo:" + result.getErrorInfo());
            if ("-1".equals(errorNo)) {
                throw new ServiceException("调用服务异常");
            } else if ("0".equals(errorNo)) {
                if (result.getEventDatas() != null && result.getEventDatas().getDatasetCount() > 0) {
                    IDataset dataset = result.getEventDatas().getDataset(0);
                    Map<String, Object> map = DatasetService.getDefaultInstance().transformToMap(dataset);
                    errorNo = map.get(ParamConstants.ERROR_NO) + "";
                    errorInfo = map.get(ParamConstants.ERROR_INFO) + "";
                }
            }
            throw new ServiceException(errorInfo);
        }
        if (result.getEventDatas() != null && result.getEventDatas().getDatasetCount() > 0) {
            return DatasetService.getDefaultInstance().transformToMap(result.getEventDatas().getDataset(0));
        }
        return null;
    }

    /**
     * 发送请求返回list<Map<String,Object>>
     * 
     * @param event
     * @param oneRow
     * @return
     */
    private static List<Map<String, Object>> sendReceiveListMap(IEvent event, Map<String, Object> oneRow) {
        if (oneRow != null) {
            event.putEventData(DataSetConvertUtil.map2DataSet(oneRow));
        }
        IEvent result;
        try {
            result = ContextUtil.getServiceContext().getServiceClient().sendReceive(event);
        } catch (TimeoutException e) {
            logger.error("errorNo:" + e.getErrorNo() + " errorInfo:" + e.getErrorInfo() + e.getErrorMessage());
            logger.error(e.getMessage(), e);
            throw new ServiceException("调用服务异常 TimeoutException");
        }
        if (result.getReturnCode() != EventReturnCode.I_OK) {
            String errorNo = result.getErrorNo();
            String errorInfo = result.getErrorInfo();
            logger.error("returnCode:" + result.getReturnCode() + " errorNo:" + result.getErrorNo() + " errorInfo:" + result.getErrorInfo());
            if ("-1".equals(result.getErrorNo())) {
                throw new ServiceException("调用服务异常");
            } else if ("0".equals(errorNo)) {
                if (result.getEventDatas() != null && result.getEventDatas().getDatasetCount() > 0) {
                    IDataset dataset = result.getEventDatas().getDataset(0);
                    Map<String, Object> map = DatasetService.getDefaultInstance().transformToMap(dataset);
                    errorNo = map.get(ParamConstants.ERROR_NO) + "";
                    errorInfo = map.get(ParamConstants.ERROR_INFO) + "";
                }
            }
            throw new ServiceException(errorInfo);
        }
        if (result.getEventDatas() != null && result.getEventDatas().getDatasetCount() > 0) {
            return DatasetService.getDefaultInstance().transformToListMap(result.getEventDatas().getDataset(0));
        }
        return null;
    }
}
