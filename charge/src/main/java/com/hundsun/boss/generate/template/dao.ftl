package ${packageName}.${moduleName}.dao${subModuleName};

import org.springframework.stereotype.Repository;

import com.hundsun.boss.base.hibernate.HibernateDao;
import com.hundsun.boss.base.hibernate.Parameter;
import ${packageName}.${moduleName}.entity${subModuleName}.${ClassName};

/**
 * ${functionName}DAO接口
 */
@Repository
public class ${ClassName}Dao extends HibernateDao<${ClassName}> {
	
}
