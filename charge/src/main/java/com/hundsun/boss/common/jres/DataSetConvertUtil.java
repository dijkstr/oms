package com.hundsun.boss.common.jres;

import java.util.Collection;
import java.util.Map;

import com.hundsun.jres.common.share.dataset.DatasetService;
import com.hundsun.jres.interfaces.share.dataset.IDataset;

public class DataSetConvertUtil {

    @SuppressWarnings("rawtypes")
    public static IDataset collection2DataSet(Collection instanceCollection, Class clazz, int totalCount) {
        IDataset ds = DatasetService.getDefaultInstance().getDataset(instanceCollection, clazz);
        ds.setTotalCount(totalCount);
        ds.setDatasetName("result");
        return ds;
    }

    public static IDataset map2DataSet(Map<String, Object> oneRow) {
        return DatasetService.getDefaultInstance().getDataset(oneRow);
    }
}
