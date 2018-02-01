package com.hundsun.boss.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.databind.JavaType;
import com.hundsun.boss.base.form.BaseForm;
import com.hundsun.boss.base.hibernate.IdEntity;
import com.hundsun.boss.common.mapper.JsonMapper;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class FormUtils {

    /**
     * 将页面传来的json对象转换为list
     * 
     * @param form
     * @param classType
     * @return
     */
    public static List getListByJson(String json, Class classType) {
        String jsonstr = HtmlUtils.htmlUnescape(json);
        JavaType javaType = JsonMapper.getInstance().createCollectionType(ArrayList.class, classType);
        List list = (List) JsonMapper.getInstance().fromJson(jsonstr, javaType);
        return list;
    }

    /**
     * 将表单对象合并到hibernate持久对象
     * 
     * @param form
     * @param entity
     * @throws Exception
     */
    public synchronized static void margeFormToEntity(Object form, Object entity) throws Exception {
        // 将表单内容复制到持久对象中
        setPropertyValue(entity, form);
        // 新增
        if (CommonUtil.isNullorEmpty((((BaseForm) form).getId()))) {
            ((IdEntity) entity).setId(IdGen.uuid());
        }
        Field[] formFields = form.getClass().getDeclaredFields();
        for (int i = 0; i < formFields.length; i++) {
            if (formFields[i].getType().equals(List.class)) {
                Set subEntitys = (Set) getFieldValueByName(formFields[i].getName(), entity);
                List subForms = (List) getFieldValueByName(formFields[i].getName(), form);

                if (CommonUtil.isNullorEmpty(subForms) && CommonUtil.isNullorEmpty(subEntitys)) {
                    continue;
                }
                // 如果是删除
                if (CommonUtil.isNullorEmpty(subForms)) {
                    Iterator it = subEntitys.iterator();
                    while (it.hasNext()) {
                        IdEntity subEntity = (IdEntity) it.next();
                        updateDelFlag(subEntity);
                    }
                } else {
                    Set tempList = new HashSet();
                    // 如果可能有更新
                    Iterator it = subEntitys.iterator();
                    // 查找在Set中与表单对象相同的元素进行合并
                    while (it.hasNext()) {
                        IdEntity subEntity = (IdEntity) it.next();
                        for (int j = 0; j < subForms.size(); j++) {
                            Object subForm = subForms.get(j);
                            if (subEntity.getId().equals(((BaseForm) subForms.get(j)).getId())) {
                                margeFormToEntity(subForm, subEntity);
                                subForms.remove(j);
                                j--;
                                // 如果是更新对象，珊瑚标志设为已更新
                                subEntity.setDelFlag("9");
                                break;
                            }
                        }
                    }
                    // 如果Set中没有表单，表示是新增
                    for (int j = 0; j < subForms.size(); j++) {
                        tempList.add(convertFormToEntity(subForms.get(j)));
                    }
                    // 将新增的对象加入持久化类
                    subEntitys.addAll(tempList);
                }
                // 如果持久对象在表单中不存在，表示已经被删除
                Iterator it = subEntitys.iterator();
                while (it.hasNext()) {
                    IdEntity subEntity = (IdEntity) it.next();
                    // 如果对象是更新或者新增的，设置为未删除
                    if (subEntity.getDelFlag().equals("9")) {
                        subEntity.setDelFlag("0");
                    } else {
                        // 否则设置为已删除
                        updateDelFlag(subEntity);
                    }
                }
            }
        }
    }

    /**
     * 修改删除状态（级联删除）
     * @param subEntity
     * @return
     * @throws Exception
     */
    public static IdEntity updateDelFlag(IdEntity subEntity) throws Exception {
        subEntity.setDelFlag("1");
        Field[] fields = subEntity.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getType().equals(Set.class)) {
                Set entitys = (Set) getFieldValueByName(fields[i].getName(), subEntity);
                Iterator it = entitys.iterator();
                while (it.hasNext()) {
                    IdEntity entity = (IdEntity) it.next();
                    updateDelFlag(entity);
                } 
            }
        }
        return subEntity;
    }
    
    
    /**
     * 将表单对象转换为持久对象
     * 
     * @param form
     * @throws Exception
     */
    public static Object convertFormToEntity(Object form) throws Exception {
        Object entity = Class.forName(((BaseForm) form).getBindClass()).newInstance();
        setPropertyValue(entity, form);
        margeFormToEntity(form, entity);
        // 如果是更新对象，珊瑚标志设为已新增
        ((IdEntity) entity).setDelFlag("9");
        ((IdEntity) entity).setId(IdGen.uuid());
        return entity;
    }

    /**
     * 根据对象属性名称获取对象值
     * 
     * @param fieldName
     * @param o
     * @return
     * @throws Exception
     */
    public static Object getFieldValueByName(String fieldName, Object o) throws Exception {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        Method method = o.getClass().getMethod(getter, new Class[] {});
        Object value = method.invoke(o, new Object[] {});
        return value;
    }

    /**
     * 将set对象转换为list
     * 
     * @param classType
     * @param set
     * @return
     * @throws Exception
     */
    public static List convertSetToList(Class classType, Set set) throws Exception {
        List list = new ArrayList();
        if (!CommonUtil.isNullorEmpty(set)) {
            Iterator it = set.iterator();
            while (it.hasNext()) {
                IdEntity entity = (IdEntity) it.next();
                BaseForm baseForm = (BaseForm) classType.newInstance();
                setPropertyValue(baseForm, entity);
                baseForm.setId(entity.getId());
                baseForm.setUpdateDate(entity.getUpdateDate());
                list.add(baseForm);
            }
        }
        return list;
    }

    /**
     * 对象拷贝
     * 
     * @param obj
     * @return
     * @throws Exception
     */
    public static void setPropertyValue(Object dest, Object orgi) throws Exception {
        Field[] orgiFields = orgi.getClass().getDeclaredFields();
        Field[] destFields = dest.getClass().getDeclaredFields();
        for (int i = 0; i < orgiFields.length; i++) {
            if (orgiFields[i].getType().equals(List.class)) {
                break;
            }
            for (int j = 0; j < destFields.length; j++) {
                if (orgiFields[i].getName().equals(destFields[j].getName())) {

                    Object value = getFieldValueByName(orgiFields[i].getName(), orgi);
                    Double dValue = null;
                    if (destFields[j].getType().equals(String.class)) {
                        if ((CommonUtil.isNullorEmpty(value) && !(orgiFields[i].getType().equals(Double.class))) || (value==null)) {
                            value = "";
                        } else {
                            if (orgiFields[i].getType().equals(Double.class)) {
                                value = Formatter.formatDecimal(Formatter.DECIMAL_FORMAT15, value);
                            }
                            value = String.valueOf(value);
                        }
                    } else if (destFields[j].getType().equals(Double.class)) {
                        if (!CommonUtil.isNullorEmpty(value)) {
                            dValue = Double.valueOf((String) value);
                        }
                    }
                    String firstLetter = destFields[j].getName().substring(0, 1).toUpperCase();
                    String setter = "set" + firstLetter + destFields[j].getName().substring(1);
                    Method method = null;
                    if (destFields[j].getType().equals(String.class)) {
                        method = dest.getClass().getMethod(setter, new Class[] { String.class });
                        method.invoke(dest, new Object[] { value });
                    } else if (destFields[j].getType().equals(Double.class)) {
                        method = dest.getClass().getMethod(setter, new Class[] { Double.class });
                        method.invoke(dest, new Object[] { dValue });
                    }
                }
            }
        }
    }

}
