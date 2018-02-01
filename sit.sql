/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.1.73 : Database - charge_sit
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/* Function  structure for function  `currval` */

/*!50003 DROP FUNCTION IF EXISTS `currval` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `currval`(seq_name VARCHAR(50)) RETURNS int(11)
BEGIN
  DECLARE VALUE INTEGER;
  SET VALUE = 0;
  SELECT current_value INTO VALUE
  FROM sequence
  WHERE NAME = seq_name;
  RETURN VALUE;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_charge_by_model` */

/*!50003 DROP FUNCTION IF EXISTS `fun_charge_by_model` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_charge_by_model`(
  param_data DECIMAL (25, 8),
  param_bill_id VARCHAR (42),
  param_feemodel_id VARCHAR (40),
  param_fee_formula VARCHAR (20)
) RETURNS decimal(25,8)
BEGIN
  DECLARE ret_charge_fee DECIMAL (25, 8) DEFAULT 0.0 ;
  IF param_fee_formula = '1' 
  THEN 
  SELECT 
    (
      CASE
        WHEN MAX(price) != 0 
        THEN MAX(price) 
        ELSE MAX(
          fee_ratio / (
            CASE
              WHEN fee_ratio_division IS NULL 
              OR fee_ratio_division = 0 
              THEN 1 
              ELSE fee_ratio_division 
            END
          )
        ) 
      END
    ) * param_data INTO ret_charge_fee 
  FROM
    ss_order_price src 
  WHERE src.bill_id = param_bill_id 
    AND src.feemodel_id = param_feemodel_id 
    and src.del_flag = '0';
  -- 普通阶梯
  ELSEIF param_fee_formula = '2' 
  THEN 
  SELECT 
    SUM(
      CASE
        WHEN max_consume IS NOT NULL 
        AND charge_amt > max_consume 
        THEN max_consume 
        ELSE charge_amt 
      END
    ) INTO ret_charge_fee 
  FROM
    (SELECT 
      -- 如果业务金额处于阶梯开始与终了之间,获取费率
      (
        CASE
          WHEN (
            step_interval = 1 
            AND step_begin < param_data 
            AND (
              param_data <= step_end 
              OR step_end IS NULL
            )
          ) 
          OR (
            step_interval = 2 
            AND step_begin <= param_data 
            AND (
              param_data < step_end 
              OR step_end IS NULL
            )
          ) 
          THEN fee_ratio / (
            CASE
              WHEN fee_ratio_division IS NULL 
              OR fee_ratio_division = 0 
              THEN 1 
              ELSE fee_ratio_division 
            END
          ) * param_data 
          ELSE 0 
        END
      ) AS charge_amt,
      max_consume 
    FROM
      ss_order_price src 
    WHERE src.bill_id = param_bill_id 
      AND src.feemodel_id = param_feemodel_id
      AND src.del_flag = '0') U ;
  -- 累进阶梯
  ELSEIF param_fee_formula = '3' 
  THEN 
  SELECT 
    SUM(
      CASE
        WHEN max_consume IS NOT NULL 
        AND charge_amt > max_consume 
        THEN max_consume 
        ELSE charge_amt 
      END
    ) INTO ret_charge_fee 
  FROM
    (SELECT 
      (
        CASE
          -- 如果业务金额处于阶梯开始与终了之间，取业务金额减去阶梯开始金额，然后计算结果
          WHEN step_begin <= param_data 
          AND (
            step_end IS NULL 
            OR param_data <= step_end
          ) 
          THEN (param_data - step_begin) -- 如果业务金额大于阶梯终了值，取阶梯终了与开始之差
          WHEN step_end IS NOT NULL 
          AND step_end < param_data 
          THEN (step_end - step_begin) 
          ELSE 0 
        END
      ) * (
        fee_ratio / (
          CASE
            WHEN fee_ratio_division IS NULL 
            OR fee_ratio_division = 0 
            THEN 1 
            ELSE fee_ratio_division 
          END
        )
      ) AS charge_amt,
      max_consume 
    FROM
      ss_order_price src 
    WHERE src.bill_id = param_bill_id 
      AND src.feemodel_id = param_feemodel_id
      AND src.del_flag = '0') U ;
  -- 日固定费用阶梯    
  ELSEIF param_fee_formula = '4' 
  THEN 
  SELECT 
    SUM(charge_amt) INTO ret_charge_fee 
  FROM
    (SELECT 
      -- 如果业务金额处于阶梯开始与终了之间,获取固定费用
      (
        CASE
          WHEN (
            step_interval = 1 
            AND step_begin < param_data 
            AND (
              param_data <= step_end 
              OR step_end IS NULL
            )
          ) 
          OR (
            step_interval = 2 
            AND step_begin <= param_data 
            AND (
              param_data < step_end 
              OR step_end IS NULL
            )
          ) 
          THEN fixed_charge 
          ELSE 0 
        END
      ) AS charge_amt 
    FROM
      ss_order_price src 
    WHERE src.bill_id = param_bill_id 
      AND src.feemodel_id = param_feemodel_id
      AND src.del_flag = '0') U ;
  END IF ;
  RETURN ret_charge_fee ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_charge_common_divid` */

/*!50003 DROP FUNCTION IF EXISTS `fun_charge_common_divid` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_charge_common_divid`(
  param_type VARCHAR (10),
  param_amount DECIMAL (25, 8),
  param_prod_begin_date VARCHAR (20),
  param_prod_end_date VARCHAR (20),
  param_charge_begin_time VARCHAR (20),
  param_charge_end_time VARCHAR (20)
) RETURNS decimal(25,8)
BEGIN
  DECLARE v_charge_complement DECIMAL (25, 8) ;
  DECLARE v_yearly_begin_date,
  v_yearly_end_date VARCHAR (20) ;
  DECLARE v_total_day_count INT ;
  -- 计算本年度的年度开始日期
  SET v_yearly_begin_date = fun_get_yearly_begin_date (
    param_charge_begin_time,
    param_prod_begin_date
  ) ;
  -- 如果拆分对象金额不为0，需要计算拆分对象 
  IF param_amount != 0 
  THEN -- 年拆分对象的 
  IF param_type = 1 
  THEN -- 计算本年度的年度结束日期
  SET v_yearly_end_date = fun_get_yearly_end_date (v_yearly_begin_date) ;
  -- 计算本年度的自然年天数
  SET v_total_day_count = fun_get_yearly_day_count (v_yearly_begin_date) ;
  -- 以下两种情况需要计算年拆分对象金额
  SET v_charge_complement = (
    CASE
      -- 如果计费期间落在年度终了月
      WHEN DATE_FORMAT(
        param_charge_end_time,
        '%Y-%m-%d'
      ) = DATE_FORMAT(v_yearly_end_date, '%Y-%m-%d') 
      THEN -- 年保底金额不需要拆分
      param_amount -- 如果计费期间落在合同终止月 
      WHEN DATE_FORMAT(
        param_charge_end_time,
        '%Y-%m-%d'
      ) = DATE_FORMAT(param_prod_end_date, '%Y-%m-%d') 
      THEN -- 年拆分对象金额 = 年拆分对象  * 年度实际发生天数 / 年度总天数
      param_amount * fun_get_date_diff (
        param_charge_end_time,
        v_yearly_begin_date
      ) / v_total_day_count 
      ELSE 0 
    END
  ) ;
  -- 不定期保底
  ELSEIF param_type = 4 
  THEN
  SET v_charge_complement = (
    CASE
      -- 如果计费期间落在不定期终了月，输出不定期保底金额
      WHEN DATE_FORMAT(
        param_charge_end_time,
        '%Y-%m-%d'
      ) = DATE_FORMAT(param_prod_end_date, '%Y-%m-%d') 
      THEN -- 不定期金额不需要拆分（如果提前终止，需要修改不定期费用）
      param_amount 
      ELSE 0 
    END
  ) ;
  -- 月拆分对象的 
  ELSEIF param_type = 2 
  THEN -- 计算自然月天数  
  SET v_total_day_count = (
    DAYOFMONTH(
      LAST_DAY(param_charge_begin_time)
    )
  ) ;
  SET v_charge_complement = (
    CASE
      -- 如果合同在本月终止 
      WHEN param_charge_begin_time <= param_prod_end_date 
      AND param_prod_end_date <= param_charge_end_time 
      THEN -- 月拆分对象金额 = 月拆分对象 * 当月实际发生天数 / 当月自然天数
      param_amount * fun_get_date_diff (
        param_prod_end_date,
        param_charge_begin_time
      ) / v_total_day_count -- 如果正常计费开始终了时间
      ELSE -- 月拆分对象金额 = 月拆分对象 * 当月实际发生天数 / 当月自然天数
      param_amount * fun_get_date_diff (
        param_charge_end_time,
        param_charge_begin_time
      ) / v_total_day_count 
    END
  ) ;
  END IF ;
  END IF ;
  RETURN v_charge_complement ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_charge_common_divid_by_month` */

/*!50003 DROP FUNCTION IF EXISTS `fun_charge_common_divid_by_month` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_charge_common_divid_by_month`(
  param_amount DECIMAL (25, 8),
  param_prod_end_date VARCHAR (20),
  param_charge_begin_time VARCHAR (20),
  param_charge_end_time VARCHAR (20)
) RETURNS decimal(25,8)
BEGIN
  DECLARE v_charge_complement DECIMAL (25, 8) ;
  DECLARE v_total_day_count INT ;
  -- 如果拆分对象金额不为0，需要计算拆分对象 
  IF param_amount != 0 
  THEN -- 计算自然月天数  
  SET v_total_day_count = (
    DAYOFMONTH(
      LAST_DAY(param_charge_begin_time)
    )
  ) ;
  SET v_charge_complement = (
    CASE
      -- 如果合同在本月终止 
      WHEN param_charge_begin_time <= param_prod_end_date 
      AND param_prod_end_date <= param_charge_end_time 
      THEN -- 月拆分对象金额 = 月拆分对象 * 当月实际发生天数 / 当月自然天数
      param_amount * fun_get_date_diff (
        param_prod_end_date,
        param_charge_begin_time
      ) / v_total_day_count -- 如果正常计费开始终了时间
      ELSE -- 月拆分对象金额 = 月拆分对象 * 当月实际发生天数 / 当月自然天数
      param_amount * fun_get_date_diff (
        param_charge_end_time,
        param_charge_begin_time
      ) / v_total_day_count 
    END
  ) ;
  END IF ;
  RETURN v_charge_complement ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_charge_daily_max` */

/*!50003 DROP FUNCTION IF EXISTS `fun_charge_daily_max` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_charge_daily_max`(
  param_data VARCHAR (20),
  param_max_type VARCHAR (20),
  param_max_consume DECIMAL (25, 8)
) RETURNS decimal(25,8)
BEGIN
  RETURN (
    -- 首先判断有没有日保底
    CASE
      WHEN param_max_type = 3 
      AND param_data > param_max_consume 
      THEN param_max_consume 
      ELSE param_data 
    END
  ) ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_charge_daily_min` */

/*!50003 DROP FUNCTION IF EXISTS `fun_charge_daily_min` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_charge_daily_min`(
  param_data VARCHAR (20),
  param_min_type VARCHAR (20),
  param_min_consume DECIMAL (25, 8)
) RETURNS decimal(25,8)
BEGIN
  RETURN (
    -- 首先判断有没有日保底
    CASE
      WHEN param_min_type = 3 
      AND param_data < param_min_consume 
      THEN param_min_consume 
      ELSE param_data 
    END
  ) ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_charge_get_fee_retio` */

/*!50003 DROP FUNCTION IF EXISTS `fun_charge_get_fee_retio` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_charge_get_fee_retio`(
  param_data DECIMAL (25, 8),
  param_bill_id VARCHAR (42),
  param_feemodel_id VARCHAR (40),
  param_fee_formula VARCHAR (20)
) RETURNS decimal(25,8)
BEGIN
  DECLARE ret_fee_retio DECIMAL (25, 8) DEFAULT 0.0 ;
  -- 普通费率
  IF param_fee_formula = 1 
  THEN 
  SELECT 
    (
      CASE
        WHEN MAX(price) != 0 
        THEN MAX(price) 
        ELSE MAX(
          fee_ratio / (
            CASE
              WHEN fee_ratio_division IS NULL 
              OR fee_ratio_division = 0 
              THEN 1 
              ELSE fee_ratio_division 
            END
          )
        ) 
      END
    ) INTO ret_fee_retio 
  FROM
    ss_order_price src 
  WHERE src.bill_id = param_bill_id 
    AND src.feemodel_id = param_feemodel_id 
    AND src.del_flag = '0';
  ELSEIF param_fee_formula = 2 
  THEN 
  SELECT 
    -- 如果业务金额处于阶梯开始与终了之间,获取费率
    MAX(
      CASE
        WHEN (
          src.step_interval = 1 
          AND src.step_begin < param_data 
          AND (
            param_data <= step_end 
            OR step_end IS NULL
          )
        ) 
        OR (
          src.step_interval = 2 
          AND src.step_begin <= param_data 
          AND (
            param_data < step_end 
            OR step_end IS NULL
          )
        ) 
        THEN src.fee_ratio / (
          CASE
            WHEN src.fee_ratio_division IS NULL 
            OR src.fee_ratio_division = 0 
            THEN 1 
            ELSE fee_ratio_division 
          END
        ) 
        ELSE 0 
      END
    ) INTO ret_fee_retio 
  FROM
    ss_order_price src 
  WHERE src.bill_id = param_bill_id 
    AND src.feemodel_id = param_feemodel_id 
    AND src.del_flag = '0';
  END IF ;
  RETURN ret_fee_retio ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_get_charge_amount` */

/*!50003 DROP FUNCTION IF EXISTS `fun_get_charge_amount` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_get_charge_amount`(
  -- 结算类型
  param_fix_charge_type VARCHAR (10),
  -- 计费对象金额
  param_data DECIMAL (25, 8),
  -- 计费金额
  param_charge_amount DECIMAL (25, 8),
  -- 下面的都是判断用的参数或者条件
  param_bill_id VARCHAR (50),
  param_feemodel_id VARCHAR (50),
  param_fee_formula VARCHAR (50),
  param_is_multiplied_actualdays VARCHAR (10),
  param_charge_end_time VARCHAR (20),
  param_charge_begin_time VARCHAR (20)
) RETURNS decimal(25,8)
BEGIN
  RETURN (
    CASE
      -- 如果是日结，返回日算出的结果
      WHEN param_fix_charge_type = 1 
      THEN param_charge_amount -- 如果是月结，在月末汇总结果上计算
      WHEN param_fix_charge_type = 2 
      THEN fun_charge_by_model (
        param_data,
        param_bill_id,
        param_feemodel_id,
        param_fee_formula
      ) * (
        CASE
          -- 某些情况下，月结结果要乘以实际发生天数
          WHEN param_is_multiplied_actualdays = 1 
          THEN fun_get_date_diff (
            param_charge_end_time,
            param_charge_begin_time
          ) 
          ELSE 1
        END
      ) 
      ELSE 0 
    END
  ) ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_get_date_diff` */

/*!50003 DROP FUNCTION IF EXISTS `fun_get_date_diff` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_get_date_diff`(end_date DATE, begin_date DATE) RETURNS varchar(20) CHARSET utf8
BEGIN
  RETURN (DATEDIFF(end_date, begin_date) + 1) ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_get_month_diff` */

/*!50003 DROP FUNCTION IF EXISTS `fun_get_month_diff` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_get_month_diff`(end_date VARCHAR(20), begin_date VARCHAR(20)) RETURNS varchar(20) CHARSET utf8
BEGIN
  RETURN (
    PERIOD_DIFF(
      DATE_FORMAT(end_date, '%Y%m'),
      DATE_FORMAT(begin_date, '%Y%m')
    ) + 1
  ) ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_get_payment_reason` */

/*!50003 DROP FUNCTION IF EXISTS `fun_get_payment_reason` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_get_payment_reason`(
  payment_type VARCHAR (10),
  nonrecurring_payment DECIMAL (25, 8),
  fare_per_year DECIMAL (25, 8),
  base_income DECIMAL (25, 8),
  payment_amount DECIMAL (25, 8),
  yearly_begin_date DATE
) RETURNS varchar(200) CHARSET utf8
BEGIN
  RETURN (
    CASE
      WHEN (payment_type = '1' AND nonrecurring_payment IS NULL) 
      THEN CONCAT('组合一次性费用未设置，','与预付一次性费用和',payment_amount,'不一致') 
      WHEN (payment_type = '2' AND fare_per_year IS NULL) 
      THEN CONCAT('组合年费未设置，','与预付年费和',payment_amount,'不一致') 
      WHEN (payment_type = '4' AND base_income IS NULL) 
      THEN CONCAT('组合基础服务费未设置，','与预付基础服务费和',payment_amount,'不一致')
      
      WHEN (payment_type = '1' AND nonrecurring_payment != payment_amount) 
      THEN CONCAT(DATE_FORMAT(yearly_begin_date, '%Y'),'一次性费用',nonrecurring_payment,'与预付一次性费用和',payment_amount,'不一致') 
      WHEN (payment_type = '2' AND fare_per_year != payment_amount) 
      THEN CONCAT(DATE_FORMAT(yearly_begin_date, '%Y'),'年费',fare_per_year,'与预付年费和',payment_amount,'不一致') 
      WHEN (payment_type = '4' AND base_income != payment_amount) 
      THEN CONCAT(DATE_FORMAT(yearly_begin_date, '%Y'),'基础服务费',base_income,'与预付基础服务费和',payment_amount,'不一致') 
    ELSE NULL 
  END
  ) ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_get_tech_service_fare` */

/*!50003 DROP FUNCTION IF EXISTS `fun_get_tech_service_fare` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_get_tech_service_fare`(
  -- 本期技术服务费
  param_tech_service_fare DECIMAL (25, 8),
  -- 期初累计技术服务费
  param_before_charge_amt DECIMAL (25, 8),
  -- 保底类型
  param_min_type VARCHAR (10),
  -- 保底金额
  param_min_consume DECIMAL (25, 8),
  -- 封顶类型
  param_max_type VARCHAR (10),
  -- 封顶金额
  param_max_consume DECIMAL (25, 8)
) RETURNS decimal(25,8)
BEGIN
  DECLARE ret_tech_service_fare DECIMAL (25, 8) ;
  SET ret_tech_service_fare = param_tech_service_fare ;
  -- 年保底,且年度年保底大于累计计费金额，取年保底 
  IF (
    param_min_type = '1' 
    OR param_min_type = '4'
  ) 
  AND (
    param_tech_service_fare + param_before_charge_amt
  ) < param_min_consume 
  THEN SET ret_tech_service_fare = param_min_consume - param_before_charge_amt ;
  -- 月保底,如果月技术服务费小于月保底，取月保底 
  ELSEIF param_min_type = '2' 
  AND param_tech_service_fare < param_min_consume 
  THEN SET ret_tech_service_fare = param_min_consume ;
  END IF ;
  -- 年封顶，且年封顶小于累计计费金额，超过封顶部分为0
  IF (
    param_max_type = '1' 
    OR param_max_type = '4'
  ) 
  AND param_max_consume < (
    param_before_charge_amt + ret_tech_service_fare
  ) 
  THEN SET ret_tech_service_fare = ret_tech_service_fare - (
    param_before_charge_amt + ret_tech_service_fare - param_max_consume
  ) ;
  -- 月封顶，且月封顶小于月计费金额，取月封顶
  ELSEIF param_max_type = '2' 
  AND param_max_consume < ret_tech_service_fare 
  THEN SET ret_tech_service_fare = param_max_consume ;
  END IF ;
  RETURN (
    CASE
      WHEN ret_tech_service_fare < 0 
      THEN 0 
      ELSE ret_tech_service_fare 
    END
  ) ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_get_yearly_begin_date` */

/*!50003 DROP FUNCTION IF EXISTS `fun_get_yearly_begin_date` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_get_yearly_begin_date`(
  current_begin_date VARCHAR(20),
  order_begin_date VARCHAR(20)
) RETURNS varchar(20) CHARSET utf8
BEGIN
  RETURN (
    CASE
      WHEN DATE_FORMAT(current_begin_date, '%m%d') >= DATE_FORMAT(order_begin_date, '%m%d') 
      THEN CONCAT(
        DATE_FORMAT(current_begin_date, '%Y'),
        DATE_FORMAT(order_begin_date, '-%m-%d')
      ) 
      ELSE CONCAT(
        CAST(
          DATE_FORMAT(current_begin_date, '%Y') AS SIGNED
        ) - 1,
        DATE_FORMAT(order_begin_date, '-%m-%d')
      ) 
    END
  ) ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_get_yearly_day_count` */

/*!50003 DROP FUNCTION IF EXISTS `fun_get_yearly_day_count` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_get_yearly_day_count`(yearly_begin_date DATE) RETURNS int(11)
BEGIN
  DECLARE year_varchar VARCHAR (20) ;
  SET year_varchar = YEAR(yearly_begin_date) ;
  RETURN (
    CASE
      WHEN -- 如果是闰年且计费年度开始时间小于2/29，则年度跨越天数因为2/29的存在而变成366天
      (
        fun_is_leap_year (year_varchar) = 1 
        AND yearly_begin_date <= DATE_FORMAT(yearly_begin_date, '%Y-02-29')
      ) -- 如果本年度不是闰年，下一年度是闰年，且开始时间大于等于3/1，是366
      OR (
        fun_is_leap_year (year_varchar) = 0 
        AND fun_is_leap_year (year_varchar + 1) = 1 
        AND DATE_FORMAT(yearly_begin_date, '%m-%d') >= '03-01'
      ) 
      THEN 366 
      ELSE 365 
    END
  ) ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_get_yearly_end_date` */

/*!50003 DROP FUNCTION IF EXISTS `fun_get_yearly_end_date` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_get_yearly_end_date`(yearly_begin_date VARCHAR(20)) RETURNS varchar(20) CHARSET utf8
BEGIN
  RETURN (
    DATE_ADD(
      yearly_begin_date,
      INTERVAL (
        CASE
          WHEN fun_get_yearly_day_count (yearly_begin_date) = 366 
          THEN 365 
          ELSE 364 
        END
      ) DAY
    )
  ) ;
END */$$
DELIMITER ;

/* Function  structure for function  `fun_is_leap_year` */

/*!50003 DROP FUNCTION IF EXISTS `fun_is_leap_year` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `fun_is_leap_year`(year_varchar VARCHAR (20)) RETURNS int(11)
BEGIN
  RETURN (
    CASE
      -- 如果是闰年的2月有29天
      WHEN DAYOFMONTH(
        LAST_DAY(CONCAT(year_varchar, '-02-01'))
      ) = 29 
      THEN 1 
      ELSE 0 
    END
  ) ;
END */$$
DELIMITER ;

/* Function  structure for function  `getYearlyBeginDate` */

/*!50003 DROP FUNCTION IF EXISTS `getYearlyBeginDate` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `getYearlyBeginDate`(
  current_begin_date DATE,
  order_begin_date DATE
) RETURNS varchar(6) CHARSET utf8
BEGIN
  RETURN (
    CASE
      WHEN DATE_FORMAT(current_begin_date, '%m%d') >= DATE_FORMAT(order_begin_date, '%m%d') 
      THEN CONCAT(
        DATE_FORMAT(current_begin_date, '%Y'),
        DATE_FORMAT(order_begin_date, '%m')
      ) 
      ELSE CONCAT(
        CAST(
          DATE_FORMAT(current_begin_date, '%Y') AS SIGNED
        ) - 1,
        DATE_FORMAT(order_begin_date, '%m')
      ) 
    END
  ) ;
END */$$
DELIMITER ;

/* Function  structure for function  `nextval` */

/*!50003 DROP FUNCTION IF EXISTS `nextval` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `nextval`(seq_name VARCHAR(50)) RETURNS int(11)
BEGIN
	DECLARE  currentVal INT;
	DECLARE maxVal INT;
  DECLARE minVal INT;
	SET currentVal = currval (seq_name);
	SELECT MAX_VALUE FROM sequence WHERE NAME = seq_name INTO maxVal;
	IF(currentVal < maxVal ) THEN
			 UPDATE sequence SET current_value = current_value + increment WHERE NAME = seq_name;
  ELSE
		SELECT min_value FROM sequence WHERE NAME = seq_name INTO minVal;
		UPDATE sequence SET current_value = minVal WHERE NAME = seq_name;
	END IF;
	 RETURN currval(seq_name);
 END */$$
DELIMITER ;

/* Function  structure for function  `setval` */

/*!50003 DROP FUNCTION IF EXISTS `setval` */;
DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` FUNCTION `setval`(seq_name VARCHAR(50), VALUE INTEGER) RETURNS int(11)
BEGIN
   UPDATE sequence
   SET          current_value = VALUE
   WHERE NAME = seq_name;
   RETURN currval(seq_name);
END */$$
DELIMITER ;

/* Procedure structure for procedure `add_Partition` */

/*!50003 DROP PROCEDURE IF EXISTS  `add_Partition` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `add_Partition`()
BEGIN
  -- 到系统表查出这个表的最大分区，得到最大分区的日期。在创建分区的时候，名称就以日期格式存放，方便后面维护 
  SELECT 
    REPLACE(partition_name, 'p', '') INTO @P12_Name 
  FROM
    INFORMATION_SCHEMA.PARTITIONS 
  WHERE table_name = 'console_log' 
  ORDER BY partition_ordinal_position DESC 
  LIMIT 1 ;
  SET @Max_date = DATE(
    DATE_ADD(@P12_Name + 0, INTERVAL 1 MONTH)
  ) + 0 ;
  -- 修改表，在最大分区的后面增加一个分区，时间范围加1天 
  SET @s1 = CONCAT(
    'ALTER TABLE console_log ADD PARTITION (PARTITION p',
    @Max_date,
    ' VALUES LESS THAN (TO_DAYS (''',
    DATE(@Max_date),
    ''')))'
  ) ;
  -- 输出查看增加分区语句
  SELECT 
    @s1 ;
  PREPARE stmt2 FROM @s1 ;
  EXECUTE stmt2 ;
  DEALLOCATE PREPARE stmt2 ;
  COMMIT ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `new_procedure` */

/*!50003 DROP PROCEDURE IF EXISTS  `new_procedure` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `new_procedure`()
BEGIN
  #定义订单产品相关参数
  DECLARE done INT DEFAULT 0 ;
  DECLARE v_order_id VARCHAR (40) ;
  DECLARE v_order_id1 VARCHAR (40) ;
  DECLARE v_origin_order_id VARCHAR (40) ;
  DECLARE count_num INT DEFAULT 0 ;
  #定义订单游标
  DECLARE order_cur CURSOR FOR 
  select 
    order_id,
    origin_order_id 
  from
    order_info 
  where change_type = '1' ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  #初始化数据
  SET SQL_SAFE_UPDATES = 0 ;
  select 
    order_id,
    origin_order_id 
  from
    order_info 
  where change_type = '1' ;
  #DELETE FROM merge_order_log; 
  OPEN order_cur ;
  cart_loop :
  LOOP
    FETCH order_cur INTO v_order_id,
    v_origin_order_id ;
    IF done 
    THEN LEAVE cart_loop ;
    END IF ;
    #select v_order_id;
    select 
      count(*),
      order_id into count_num,
      v_order_id1 
    from
      order_info 
    where origin_order_id = v_order_id ;
    update 
      order_info 
    set
      inter_id = v_order_id 
    where order_id = v_order_id ;
    WHILE
      count_num >= '1' DO 
      update 
        order_info 
      set
        inter_id = v_order_id 
      where order_id = v_order_id1 ;
      select 
        count(*),
        order_id into count_num,
        v_order_id1 
      from
        order_info 
      where origin_order_id = v_order_id1 ;
    END WHILE ;
  END LOOP cart_loop ;
  CLOSE order_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `schema_change` */

/*!50003 DROP PROCEDURE IF EXISTS  `schema_change` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `schema_change`()
BEGIN
  DECLARE CurrentDatabase VARCHAR (100) ;
  SELECT 
    DATABASE() INTO CurrentDatabase ;
  IF NOT EXISTS 
  (SELECT 
    * 
  FROM
    information_schema.statistics 
  WHERE table_schema = CurrentDatabase 
    AND table_name = 'charge_oper_list' 
    AND index_name = 'index_create_datetime') 
  THEN CREATE INDEX index_create_datetime 
  ON charge_oper_list (create_datetime ASC) ;
  END IF ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_call_income` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_call_income` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_call_income`()
BEGIN
  -- 合同编号（用于提高性能）
  DECLARE v_contract_id , v_sql VARCHAR (600) ;
  DECLARE done INT DEFAULT 0 ;
  DECLARE month_cur CURSOR FOR 
  SELECT DISTINCT 
    contract_id
  FROM
    charge_order_info ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  OPEN month_cur ;
  cursor_loop :
  LOOP
    FETCH month_cur INTO v_contract_id;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    SELECT CONCAT('CALL sp_finance_detail(','\'',v_contract_id,'\'', ');','CALL sp_count_yearly_product_income(', '\'','201712','\'',',','\'', v_contract_id,'\'', ');','CALL sp_count_yearly_contract_income(', '\'','201712','\'',',','\'', v_contract_id,'\'', ');') ;
   -- SELECT CONCAT('CALL sp_count_yearly_product_income(', '\'','201712','\'',',','\'', v_contract_id,'\'', ');') ;
   -- SELECT CONCAT('CALL sp_count_yearly_contract_income(', '\'','201712','\'',',','\'', v_contract_id,'\'', ');') ;
  END LOOP cursor_loop ;
  CLOSE month_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_charge_account_bill` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_charge_account_bill` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_charge_account_bill`(param_bill_id VARCHAR (100))
BEGIN
  -- 累计技术服务费(已经在组合中处理过保底预付)
  DECLARE v_total_tech_service_fare,
  -- 本期技术服务费
  v_current_tech_service_fare,
  -- 季度累计技术服务费
  v_season_tech_service_fare,
  -- 累计到款
  v_total_receive,
  -- 累计调账
  v_total_adjust_amt,
  -- 年度累计保底预付（技术服务费）
  v_yearly_advance_tech_service_fare,
  -- 本期调账
  v_adjust_amt,
  -- 本期到款
  v_amount_receivable,
  -- 应付
  v_payable,
  -- odd应付
  v_odd_payable,
  -- 累计应预付
  v_total_advance DECIMAL (25, 8) ;
  -- 本期计费开始时间
  DECLARE v_charge_begin_date,
  -- 本期计费结束时间
  v_charge_end_date,
  -- 计费开始时间
  v_prod_begin_date,
  v_bill_id,
  -- 协同合同号
  v_contract_id,
  -- 年度计费起始时间
  v_yearly_begin_date VARCHAR (50) ;
  DECLARE done INT DEFAULT 0 ;
  -- 通过游标计算出账内容
  DECLARE receipt_cur CURSOR FOR 
  SELECT 
    DATE_FORMAT(charge_begin_date, '%Y-%m-%d'),
    DATE_FORMAT(charge_end_date, '%Y-%m-%d'),
    DATE_FORMAT(prod_begin_date, '%Y-%m-%d'),
    bill_id,
    contract_id 
  FROM
    charge_bill_info 
  WHERE bill_id <= param_bill_id 
    AND contract_id = LEFT(
      param_bill_id,
      LOCATE('-', param_bill_id) - 1
    ) 
    AND belong_type = '1' 
    AND bill_flag = '1' 
  ORDER BY charge_begin_date ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  OPEN receipt_cur ;
  cursor_loop :
  LOOP
    FETCH receipt_cur INTO v_charge_begin_date,
    v_charge_end_date,
    v_prod_begin_date,
    v_bill_id,
    v_contract_id ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    -- 本期年度开始时间
    SET v_yearly_begin_date = (
      fun_get_yearly_begin_date (
        DATE_FORMAT(v_charge_begin_date, '%Y%m%d'),
        DATE_FORMAT(v_prod_begin_date, '%Y%m%d')
      )
    ) ;
    -- 合同级技术服务费累加
    SELECT 
      IFNULL(SUM(service_charge), 0) INTO v_current_tech_service_fare 
    FROM
      charge_bill_info elo 
    WHERE elo.belong_type = '2' 
      AND elo.bill_id = v_bill_id ;
    UPDATE 
      charge_bill_info 
    SET
      service_charge = v_current_tech_service_fare 
    WHERE belong_type = '1' 
      AND bill_id = v_bill_id ;
    -- 累计技术服务费
    SET v_total_tech_service_fare = 
    (SELECT 
      SUM(U.receivable) 
    FROM
      (SELECT 
        IFNULL(MAX(receivable), 0) AS receivable 
      FROM
        charge_bill_info elo 
      WHERE contract_id = v_contract_id 
        AND charge_end_date <= v_charge_end_date 
        AND belong_type = '2' 
      GROUP BY elo.combine_id) U) ;
    -- 季度技术服务费
    SET v_season_tech_service_fare = 
    (SELECT 
      IFNULL(SUM(season_charge), 0) 
    FROM
      charge_bill_info elo 
    WHERE belong_type = '2' 
      AND bill_id = v_bill_id) ;
    -- 累计到款
    SET v_total_receive = 
    (SELECT 
      IFNULL(SUM(bankreceipt_amount), 0) 
    FROM
      charge_bankreceipt 
    WHERE DATE_FORMAT(bankreceipt_month, '%Y%m%d') <= DATE_FORMAT(v_charge_end_date, '%Y%m%d') 
      AND contract_id = v_contract_id) ;
    -- 本期到款
    SET v_amount_receivable = 
    (SELECT 
      IFNULL(SUM(bankreceipt_amount), 0) 
    FROM
      charge_bankreceipt 
    WHERE DATE_FORMAT(bankreceipt_month, '%Y%m%d') <= DATE_FORMAT(v_charge_end_date, '%Y%m%d') 
      AND DATE_FORMAT(bankreceipt_month, '%Y%m%d') >= DATE_FORMAT(v_charge_begin_date, '%Y%m%d') 
      AND contract_id = v_contract_id) ;
    -- 累计调账
    SET v_total_adjust_amt = 
    (SELECT 
      IFNULL(SUM(adjust_amt), 0) 
    FROM
      charge_adjust_service_charge t 
    WHERE adjust_date <= v_charge_end_date 
      AND t.contract_id = v_contract_id 
      AND t.approve_status = '9'
      AND EXISTS 
      (SELECT 
        1 
      FROM
        ss_order_combine s 
      WHERE s.id = t.combine_id 
        AND s.del_flag = '0' 
        AND s.bill_id = param_bill_id)) ;
    -- 本期调账
    SET v_adjust_amt = 
    (SELECT 
      IFNULL(SUM(adjust_amt), 0) 
    FROM
      charge_adjust_service_charge t 
    WHERE adjust_date >= v_charge_begin_date 
      AND adjust_date <= v_charge_end_date 
      AND t.approve_status = '9'
      AND t.contract_id = v_contract_id 
      AND EXISTS 
      (SELECT 
        1 
      FROM
        ss_order_combine s 
      WHERE s.id = t.`combine_id` 
        AND s.`del_flag` = '0' 
        AND s.bill_id = param_bill_id)) ;
    -- 累计应预付
    SET v_total_advance = 
    (SELECT 
      IFNULL(SUM(amount), 0) 
    FROM
      ss_order_adv_payment 
    WHERE DATE_FORMAT(advance_date, '%Y-%m-%d') <= v_charge_end_date 
      AND bill_id = v_bill_id 
      AND contract_id = v_contract_id 
      AND payment_type != '10000' 
      AND del_flag = '0') ;
    -- 应付
    SET v_payable = v_total_receive -- 如果累计应预付大于技术服务费
    - v_total_tech_service_fare - v_total_advance ;
    IF v_payable >= 0 
    THEN SET v_payable = 0 ;
    ELSE SET v_payable = - 1 * v_payable ;
    END IF ;
    UPDATE 
      charge_order_info 
    SET
      total_service_charge = v_total_tech_service_fare,
      season_charge = v_season_tech_service_fare,
      total_bankreceipt = v_total_receive,
      total_adjust_amt = v_total_adjust_amt,
      adjust_amt = v_adjust_amt,
      receivable = v_amount_receivable,
      payable = v_payable,
      total_advance_charge = v_total_advance 
    WHERE bill_id = v_bill_id ;
    -- 是否首次出账
    UPDATE 
      charge_bill_info 
    SET
      bill_flag = '0' 
    WHERE belong_type = '1' 
      AND bill_id = v_bill_id ;
  END LOOP cursor_loop ;
  CLOSE receipt_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_charge_account_combine` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_charge_account_combine` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_charge_account_combine`(param_bill_id VARCHAR (100))
BEGIN
  -- 1.计算本期技术服务费
  DECLARE -- 累计期初累计技术服务费（用于计算年补足） 
  v_before_total_tech_service_fare,
  -- 年度期初累计技术服务费
  v_before_yearly_tech_service_fare,
  -- 本期技术服务费
  v_current_tech_service_fare,
  -- 2.计算控制隐藏明细的列
  -- 累计技术服务费
  v_total_tech_service_fare,
  -- 年度累计技术服务费
  v_yearly_tech_service_fare,
  -- 季度累计技术服务费
  v_season_tech_service_fare,
  -- 自然季度累计技术服务费
  v_season_tech_service_fare_nature,
  -- 累计到款
  v_total_total_receive,
  -- 本期到款
  v_total_receive,
  -- 年度累计调账
  v_total_adjust_amt,
  -- 本期调账
  v_adjust_amt,
  -- 年度累计分期预付
  v_total_advance_amount,
  -- 技术服务费组合应付（经过保底预付判断处理）
  v_tech_service_fare_payable,
  -- 年度技术服务费预付
  v_total_tech_fare_advance DECIMAL (25, 8) ;
  -- 定义游标变量
  DECLARE v_bill_id,
  v_contract_id,
  v_combine_id,
  v_product_id,
  v_feemodel_id,
  v_charge_begin_date,
  v_charge_end_date,
  v_prod_begin_date,
  v_period_begin_date,
  v_min_type,
  v_fix_charge_type,
  v_bills VARCHAR (50) ;
  DECLARE done INT DEFAULT 0 ;
  -- 通过游标计算出账内容
  DECLARE receipt_cur CURSOR FOR 
  SELECT 
    bill_id,
    contract_id,
    combine_id,
    product_id,
    feemodel_id,
    charge_begin_date,
    charge_end_date,
    prod_begin_date,
    min_type 
  FROM
    charge_bill_info 
  WHERE bill_id <= param_bill_id 
    AND contract_id = LEFT(
      param_bill_id,
      LOCATE('-', param_bill_id) - 1
    ) 
    AND belong_type = '2' 
    AND bill_flag = '1' 
  ORDER BY charge_begin_date ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  OPEN receipt_cur ;
  cursor_loop :
  LOOP
    FETCH receipt_cur INTO v_bill_id,
    v_contract_id,
    v_combine_id,
    v_product_id,
    v_feemodel_id,
    v_charge_begin_date,
    v_charge_end_date,
    v_prod_begin_date,
    v_min_type ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    -- 如果保底类型为不定期，计费起始时间为组合起始时间
    IF v_min_type = '4' 
    THEN SET v_period_begin_date = v_prod_begin_date ;
    ELSE -- 如果是年保底，需要获取本期年度开始时间
    SET v_period_begin_date = (
      fun_get_yearly_begin_date (
        DATE_FORMAT(v_charge_begin_date, '%Y%m%d'),
        DATE_FORMAT(v_prod_begin_date, '%Y%m%d')
      )
    ) ;
    END IF ;
    -- 年度累计调账
    SET v_total_adjust_amt = 
    (SELECT 
      IFNULL(SUM(adjust_amt), 0) 
    FROM
      charge_adjust_service_charge t 
    WHERE adjust_date >= v_period_begin_date 
      AND adjust_date <= v_charge_end_date 
      AND approve_status = '9' 
      AND t.contract_id = v_contract_id 
      AND t.combine_id = v_combine_id) ;
    -- 本期调账
    SET v_adjust_amt = 
    (SELECT 
      IFNULL(SUM(adjust_amt), 0) 
    FROM
      charge_adjust_service_charge t 
    WHERE adjust_date >= v_charge_begin_date 
      AND adjust_date <= v_charge_end_date 
      AND approve_status = '9' 
      AND t.contract_id = v_contract_id 
      AND t.combine_id = v_combine_id) ;
    -- 计算累计期初累计技术服务费（用于计算补足）
    -- 计算累计期初累计
    SET v_before_total_tech_service_fare = 
    (SELECT 
      IFNULL(SUM(elo.service_charge), 0) 
    FROM
      charge_bill_info elo 
    WHERE elo.belong_type = '2' 
      AND elo.contract_id = v_contract_id 
      AND elo.combine_id = v_combine_id 
      AND DATE_FORMAT(elo.charge_begin_date, '%Y%m%d') >= DATE_FORMAT(v_prod_begin_date, '%Y%m%d') 
      AND DATE_FORMAT(elo.charge_end_date, '%Y%m%d') < DATE_FORMAT(v_charge_end_date, '%Y%m%d')) ;
    -- 计算年度期初累计
    SET v_before_yearly_tech_service_fare = 
    (SELECT 
      IFNULL(SUM(elo.service_charge), 0) 
    FROM
      charge_bill_info elo 
    WHERE elo.belong_type = '2' 
      AND elo.contract_id = v_contract_id 
      AND elo.combine_id = v_combine_id 
      AND DATE_FORMAT(elo.charge_begin_date, '%Y%m%d') >= DATE_FORMAT(v_period_begin_date, '%Y%m%d') 
      AND DATE_FORMAT(elo.charge_end_date, '%Y%m') < DATE_FORMAT(v_charge_end_date, '%Y%m')) ;
    -- 中间计费变量
    SELECT 
      (
        CASE
          WHEN srl.min_type = '3' 
          THEN IFNULL(MAX(elo.service_charge), 0) 
          ELSE IFNULL(SUM(elo.service_charge), 0) 
        END
      ) INTO v_current_tech_service_fare 
    FROM
      charge_bill_info elo,
      ss_order_model srl 
    WHERE elo.belong_type = '3' 
      AND srl.bill_id = elo.bill_id 
      AND srl.belong_type = '2' 
      AND srl.ref_id = elo.combine_id 
      AND srl.del_flag = '0' 
      AND elo.bill_id = v_bill_id 
      AND elo.combine_id = v_combine_id ;
    -- 技术服务费预付
    SET v_total_tech_fare_advance = 
    (SELECT 
      IFNULL(SUM(amount), 0) 
    FROM
      ss_order_adv_payment 
    WHERE DATE_FORMAT(advance_date, '%Y-%m-%d') <= v_charge_end_date 
      AND DATE_FORMAT(advance_date, '%Y-%m-%d') >= v_period_begin_date 
      AND bill_id = v_bill_id 
      AND combine_id = v_combine_id 
      AND payment_type = '10000' 
      AND del_flag = '0') ;
    -- 月结类型
    SET v_fix_charge_type = 
    (SELECT 
      IFNULL(MAX(fix_charge_type), 0) 
    FROM
      ss_order_model 
    WHERE bill_id = v_bill_id 
      AND del_flag = '0') ;
    -- 当月账单数
    SET v_bills = 
    (SELECT 
      COUNT(*) 
    FROM
      charge_bill_info 
    WHERE contract_id = v_contract_id 
      AND DATE_FORMAT(charge_begin_date, '%Y%m') = DATE_FORMAT(v_charge_begin_date, '%Y%m') 
      AND belong_type = '1') ;
    -- 根据时间段拆分金额
    IF v_fix_charge_type = '2' 
    AND v_bills = '2' 
    THEN IF DAY(v_charge_begin_date) = '1' 
    THEN SET v_current_tech_service_fare = ROUND(v_current_tech_service_fare, 4) * fun_get_date_diff (
      v_charge_end_date,
      v_charge_begin_date
    ) / DAY(LAST_DAY(v_charge_end_date)) ;
    ELSE SET v_current_tech_service_fare = ROUND(v_current_tech_service_fare, 4) * (
      1 - (DAY(v_charge_begin_date) - 1) / DAY(LAST_DAY(v_charge_end_date))
    ) ;
    END IF ;
    END IF ;
    -- 根据期初计算本期技术服务费
    SET v_current_tech_service_fare = 
    (SELECT 
      -- 计算技术服务费
      IFNULL(
        SUM(
          fun_get_tech_service_fare (
            -- 1.调账在本期技术服务费中直接处理
            -- 2.保底封顶都在技术服务费加调账的基础上进行
            v_current_tech_service_fare * IFNULL(srl.discount, 100) / 100 + v_adjust_amt,
            v_before_yearly_tech_service_fare,
            elo.min_type,
            elo.min_consume,
            elo.max_type,
            elo.max_consume
          )
        ),
        0
      ) 
    FROM
      charge_bill_info elo,
      ss_order_model srl 
    WHERE -- 产品，组合，合同都以产品的计费金额计算技术服务费
      elo.belong_type = '2' 
      AND elo.bill_id = v_bill_id 
      AND elo.contract_id = v_contract_id 
      AND elo.combine_id = v_combine_id 
      AND srl.ref_id = elo.combine_id 
      AND srl.bill_id = elo.bill_id 
      AND srl.belong_type = '2' 
      AND srl.del_flag = '0') ;
    -- 年度累计技术服务费
    SET v_yearly_tech_service_fare = v_before_yearly_tech_service_fare + v_current_tech_service_fare ;
    -- 累计技术服务费
    SET v_total_tech_service_fare = v_before_total_tech_service_fare + v_current_tech_service_fare ;
    -- 技术服务费组合应付（经过保底预付判断处理）
    SET v_tech_service_fare_payable = GREATEST(
      v_total_tech_service_fare,
      v_total_tech_fare_advance
    ) ;
    -- 累计到款
    SET v_total_total_receive = 
    (SELECT 
      IFNULL(SUM(bankreceipt_amount), 0) 
    FROM
      charge_bankreceipt et 
    WHERE bankreceipt_month >= v_period_begin_date 
      AND bankreceipt_month <= v_charge_end_date 
      AND et.contract_id = v_contract_id 
      AND et.ex_product_id IN 
      (SELECT DISTINCT 
        ex_product_id 
      FROM
        ss_order_combine sre,
        ss_order_product srt 
      WHERE sre.bill_id = v_bill_id 
        AND sre.id = v_combine_id 
        AND srt.bill_id = sre.bill_id 
        AND srt.combine_id = sre.id 
        AND sre.del_flag = '0' 
        AND srt.del_flag = '0')) ;
    -- 本期到款
    SET v_total_receive = 
    (SELECT 
      IFNULL(SUM(bankreceipt_amount), 0) 
    FROM
      charge_bankreceipt et 
    WHERE bankreceipt_month >= v_charge_begin_date 
      AND bankreceipt_month <= v_charge_end_date 
      AND et.contract_id = v_contract_id 
      AND et.ex_product_id IN 
      (SELECT DISTINCT 
        ex_product_id 
      FROM
        ss_order_combine sre,
        ss_order_product srt 
      WHERE sre.bill_id = v_bill_id 
        AND sre.id = v_combine_id 
        AND srt.bill_id = sre.bill_id 
        AND srt.combine_id = sre.id 
        AND sre.del_flag = '0' 
        AND srt.del_flag = '0')) ;
    -- 累计分期预付
    SET v_total_advance_amount = 
    (SELECT 
      IFNULL(SUM(amount), 0) 
    FROM
      order_adv_payment re 
    WHERE re.contract_id = v_contract_id 
      AND re.combine_id = v_combine_id 
      AND advance_date >= v_period_begin_date 
      AND advance_date <= v_charge_end_date 
      AND del_flag = '0') ;
    UPDATE 
      charge_bill_info 
    SET
      -- 本期技术服务费
      service_charge = v_current_tech_service_fare,
      -- 2.累计到款,累计调账,累计分期预付
      -- 年度累计技术服务费
      yearly_service_charge = v_yearly_tech_service_fare,
      -- 累计技术服务费
      total_service_charge = v_total_tech_service_fare,
      -- 累计到款
      yearly_total_receive = v_total_total_receive,
      -- 累计调账
      yearly_adjust_amt = v_total_adjust_amt,
      -- 累计分期预付
      yearly_advance_amount = v_total_advance_amount,
      -- 调账金额
      adjust_amt = v_adjust_amt,
      -- 本期到款
      total_bankreceipt = v_total_receive,
      -- 技术服务费组合应付（经过保底预付判断处理）
      receivable = v_tech_service_fare_payable,
      -- 出账完毕
      charge_off = '1' 
    WHERE belong_type = '2' 
      AND bill_id = v_bill_id 
      AND contract_id = v_contract_id 
      AND combine_id = v_combine_id ;
    -- 非自然及季度累计技术服务费
    SET v_season_tech_service_fare = 
    (SELECT 
      CASE
        -- 如果是年度第一个月，取当月的年度累计技术服务费
        WHEN fun_get_month_diff (
          charge_begin_date,
          yearly_begin_date
        ) = 3 
        THEN U.yearly_service_charge -- 如果是年度中间的季度，取当月的年度累计技术服务费减去之前季度的累计技术服务费
        WHEN fun_get_month_diff (
          charge_begin_date,
          yearly_begin_date
        ) % 3 = 0 
        THEN U.yearly_service_charge - IFNULL(
          (SELECT 
            SUM(elo.yearly_service_charge) 
          FROM
            charge_bill_info elo 
          WHERE elo.belong_type = U.belong_type 
            AND elo.contract_id = U.contract_id 
            AND elo.combine_id = U.combine_id 
            AND fun_get_month_diff (
              U.charge_begin_date,
              elo.charge_begin_date
            ) = 4),
          0
        ) -- 第13个月账单，取当月的年度累计技术服务费
        WHEN fun_get_month_diff (
          charge_begin_date,
          yearly_begin_date
        ) = 13 
        THEN service_charge -- 提前终止
        -- 如果在年度的头三个月，取当月的年度累计技术服务费
        WHEN U.charge_end_date = U.prod_end_date 
        AND fun_get_month_diff (
          charge_begin_date,
          yearly_begin_date
        ) < 3 
        THEN U.yearly_service_charge -- 如果在年度的三个月后，取当月的年度累计技术服务费减去之前季度的累计技术服务费
        WHEN U.charge_end_date = U.prod_end_date 
        AND fun_get_month_diff (
          charge_begin_date,
          yearly_begin_date
        ) > 3 
        THEN U.yearly_service_charge - IFNULL(
          (SELECT 
            SUM(elo.yearly_service_charge) 
          FROM
            charge_bill_info elo 
          WHERE elo.belong_type = U.belong_type 
            AND elo.contract_id = U.contract_id 
            AND elo.combine_id = U.combine_id 
            AND fun_get_month_diff (
              U.charge_begin_date,
              elo.charge_begin_date
            ) = (
              fun_get_month_diff (
                U.prod_end_date,
                U.yearly_begin_date
              ) % 3
            ) + 1),
          0
        ) 
        ELSE 0 
      END 
    FROM
      (SELECT 
        fun_get_yearly_begin_date (
          elo.charge_begin_date,
          elo.prod_begin_date
        ) yearly_begin_date,
        IFNULL(elo.service_charge, 0) service_charge,
        elo.prod_begin_date,
        elo.prod_end_date,
        elo.charge_begin_date,
        elo.charge_end_date,
        elo.bill_id,
        elo.combine_id,
        elo.product_id,
        elo.feemodel_id,
        elo.contract_id,
        IFNULL(elo.yearly_service_charge, 0) yearly_service_charge,
        elo.belong_type 
      FROM
        charge_bill_info elo,
        ss_order_info sro 
      WHERE elo.belong_type = '2' 
        AND elo.bill_id = sro.bill_id 
        AND sro.payment_cycle != '0' 
        AND sro.del_flag = '0' 
        AND elo.combine_id = v_combine_id 
        AND elo.bill_id = v_bill_id) U) ;
    -- 自然及季度累计技术服务费
    SET v_season_tech_service_fare_nature = 
    (SELECT 
      U.yearly_service_charge - IFNULL(
        (SELECT 
          SUM(elo.yearly_service_charge) 
        FROM
          charge_bill_info elo 
        WHERE elo.belong_type = U.belong_type 
          AND elo.contract_id = U.contract_id 
          AND elo.combine_id = U.combine_id 
          AND fun_get_month_diff (
            U.charge_begin_date,
            elo.charge_begin_date
          ) = 4),
        0
      ) 
    FROM
      (SELECT 
        fun_get_yearly_begin_date (
          elo.charge_begin_date,
          elo.prod_begin_date
        ) yearly_begin_date,
        IFNULL(elo.service_charge, 0) service_charge,
        elo.prod_begin_date,
        elo.prod_end_date,
        elo.charge_begin_date,
        elo.charge_end_date,
        elo.bill_id,
        elo.combine_id,
        elo.product_id,
        elo.feemodel_id,
        elo.contract_id,
        IFNULL(elo.yearly_service_charge, 0) yearly_service_charge,
        elo.belong_type 
      FROM
        charge_bill_info elo,
        ss_order_info sro 
      WHERE elo.belong_type = '2' 
        AND elo.bill_id = sro.bill_id 
        AND DATE_FORMAT(elo.charge_end_date, '%m') IN ('03', '06', '09', '12') 
        AND sro.payment_cycle = '0' 
        AND sro.del_flag = '0' 
        AND elo.combine_id = v_combine_id 
        AND elo.bill_id = v_bill_id) U) ;
    UPDATE 
      charge_bill_info 
    SET
      -- 季度累计技术服务费
      season_charge = GREATEST(
        IFNULL(v_season_tech_service_fare, 0),
        IFNULL(
          v_season_tech_service_fare_nature,
          0
        )
      ) 
    WHERE belong_type = '2' 
      AND bill_id = v_bill_id 
      AND contract_id = v_contract_id 
      AND combine_id = v_combine_id ;
    -- 出账完毕,合同级出账状态设为1   
    UPDATE 
      charge_bill_info 
    SET
      charge_off = '1' 
    WHERE belong_type = '1' 
      AND bill_id = v_bill_id 
      AND contract_id = v_contract_id ;
    -- 是否首次出账
    UPDATE 
      charge_bill_info 
    SET
      bill_flag = '0' 
    WHERE belong_type IN ('2', '3') 
      AND bill_id = v_bill_id ;
  END LOOP cursor_loop ;
  CLOSE receipt_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_charge_account_receitp` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_charge_account_receitp` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_charge_account_receitp`(
  param_bill_id VARCHAR (100),
  param_belong_type VARCHAR (10)
)
BEGIN
  -- 1.计算本期技术服务费
  DECLARE -- 年度期初累计技术服务费（用于计算年补足） 
  v_yearly_before_tech_service_fare,
  -- 本期技术服务费
  v_current_tech_service_fare,
  -- 2.计算控制隐藏明细的列
  -- 年度累计技术服务费
  v_yearly_tech_service_fare,
  -- 季度累计技术服务费
  v_season_tech_service_fare,
  -- 年度累计到款
  v_yearly_total_receive,
  -- 本期到款
  v_total_receive,
  -- 年度累计调账
  v_yearly_adjust_amt,
  -- 本期调账
  v_adjust_amt,
  -- 年度累计分期预付
  v_yearly_advance_amount DECIMAL (25, 8) ;
  -- 定义游标变量
  DECLARE v_bill_id,
  v_contract_id,
  v_hs_product_id,
  v_order_id,
  v_combine_id,
  v_product_id,
  v_feemodel_id,
  v_charge_begin_time,
  v_charge_end_time,
  v_prod_begin_date,
  v_yearly_begin_date VARCHAR (50) ;
  DECLARE done INT DEFAULT 0 ;
  -- 通过游标计算出账内容
  DECLARE receipt_cur CURSOR FOR 
  SELECT 
    bill_id,
    contract_id,
    hs_product_id,
    order_id,
    combine_id,
    product_id,
    feemodel_id,
    charge_begin_time,
    charge_end_time,
    prod_begin_date 
  FROM
    charge_bill_info 
  WHERE bill_id = param_bill_id 
    AND belong_type = param_belong_type ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  OPEN receipt_cur ;
  cursor_loop :
  LOOP
    FETCH receipt_cur INTO v_bill_id,
    v_contract_id,
    v_hs_product_id,
    v_order_id,
    v_combine_id,
    v_product_id,
    v_feemodel_id,
    v_charge_begin_time,
    v_charge_end_time,
    v_prod_begin_date ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    -- 本期年度开始时间
    SET v_yearly_begin_date = (
      fun_get_yearly_begin_date (
        DATE_FORMAT(v_charge_begin_time, '%Y%m%d'),
        DATE_FORMAT(v_prod_begin_date, '%Y%m%d')
      )
    ) ;
    -- 1.计算年度期初累计技术服务费（用于计算年补足）
    -- 首先计算年度期初累计
    SET v_yearly_before_tech_service_fare = 
    (SELECT 
      IFNULL(SUM(elo.tech_service_fare), 0) 
    FROM
      charge_bill_info elo 
    WHERE elo.belong_type = param_belong_type 
      AND elo.order_id = v_order_id 
      AND (
        param_belong_type = '1' 
        OR (
          elo.combine_id = v_combine_id 
          AND elo.product_id = v_product_id 
          AND elo.feemodel_id = v_feemodel_id
        )
      ) 
      AND DATE_FORMAT(elo.charge_begin_time, '%Y%m%d') >= DATE_FORMAT(v_yearly_begin_date, '%Y%m%d') 
      AND DATE_FORMAT(elo.charge_end_time, '%Y%m') < DATE_FORMAT(v_charge_end_time, '%Y%m')) ;
    -- 中间计费变量
    SELECT 
      IFNULL(SUM(tech_service_fare), 0) INTO v_current_tech_service_fare 
    FROM
      charge_bill_info elo 
    WHERE elo.belong_type = '3' 
      AND elo.bill_id = param_bill_id 
      AND (
        param_belong_type = '1' 
        OR (
          elo.combine_id = v_combine_id 
          AND elo.product_id = v_product_id 
          AND elo.feemodel_id = v_feemodel_id
        )
      ) ;
    -- 根据期初计算本期技术服务费
    SET v_current_tech_service_fare = 
    (SELECT 
      CASE
        -- 如果出账完毕，不会再改动技术服务费
        WHEN charge_off = '1' 
        THEN v_current_tech_service_fare 
        -- 否则计算技术服务费
        ELSE IFNULL(
          fun_get_tech_service_fare (
            v_current_tech_service_fare * IFNULL(srl.discount, 100) / 100,
            v_yearly_before_tech_service_fare,
            elo.min_type,
            elo.min_consume,
            elo.max_type,
            elo.max_consume
          ),
          0
        ) 
      END 
    FROM
      charge_bill_info elo,
      ss_order_model srl 
    WHERE -- 产品，组合，合同都以产品的计费金额计算技术服务费
      elo.belong_type = param_belong_type 
      AND elo.bill_id = param_bill_id 
      AND elo.order_id = v_order_id 
      AND (
        param_belong_type = '1' 
        OR (
          elo.combine_id = v_combine_id 
          AND elo.product_id = v_product_id 
          AND elo.feemodel_id = v_feemodel_id 
          AND srl.combine_id = elo.combine_id 
          AND srl.product_id = elo.product_id 
          AND srl.feemodel_id = elo.feemodel_id
        )
      ) 
      AND srl.bill_id = elo.bill_id 
      AND srl.order_id = elo.order_id 
      AND srl.belong_type = param_belong_type) ;
    -- 年度累计技术服务费
    SET v_yearly_tech_service_fare = v_yearly_before_tech_service_fare + v_current_tech_service_fare ;
    -- 年度累计到款
    SET v_yearly_total_receive = 
    (SELECT 
      IFNULL(SUM(bankreceipt_amount), 0) 
    FROM
      charge_bankreceipt 
    WHERE bankreceipt_month <= v_charge_end_time 
      AND bankreceipt_month >= v_yearly_begin_date 
      AND hs_contract_id = v_contract_id 
      AND (
        param_belong_type = '1' 
        OR (
          saleprdid = v_hs_product_id
        )
      ) ) ;
    -- 本期到款
    SET v_total_receive = 
    (SELECT 
      IFNULL(SUM(bankreceipt_amount), 0) 
    FROM
      charge_bankreceipt 
    WHERE bankreceipt_month >= v_charge_begin_time 
      AND bankreceipt_month <= v_charge_end_time 
      AND hs_contract_id = v_contract_id 
      AND (
        param_belong_type = '1' 
        OR (
          saleprdid = v_hs_product_id
        )
      )) ;
    -- 年度累计调账
    SET v_yearly_adjust_amt = 
    (SELECT 
      IFNULL(SUM(adjust_balance), 0) 
    FROM
      charge_adjust_bill 
    WHERE bill_date <= v_charge_end_time 
      AND bill_date >= v_yearly_begin_date 
      AND hs_contract_id = v_contract_id 
      AND (
        param_belong_type = '1' 
        OR (
          product_sale_id = v_hs_product_id
        )
      )) ;
    -- 本期调账
    SET v_adjust_amt = 
    (SELECT 
      IFNULL(SUM(adjust_balance), 0) 
    FROM
      charge_adjust_bill 
    WHERE bill_date >= v_charge_begin_time 
      AND bill_date <= v_charge_end_time 
      AND hs_contract_id = v_contract_id 
      AND (
        param_belong_type = '1' 
        OR (
          product_sale_id = v_hs_product_id
        )
      )) ;
    -- 年度累计分期预付
    SET v_yearly_advance_amount = 
    (SELECT 
      IFNULL(SUM(payment_amount), 0) 
    FROM
      order_paymentcycle 
    WHERE payment_month <= v_charge_end_time 
      AND payment_month >= v_yearly_begin_date 
      AND order_id = v_order_id 
      AND (
        param_belong_type = '1' 
        OR (
          product_code = v_hs_product_id
        )
      )) ;
    UPDATE 
      charge_bill_info 
    SET
      -- 本期技术服务费
      tech_service_fare = v_current_tech_service_fare,
      -- 2.年度累计到款,年度累计调账,年度累计分期预付
      -- 年度累计技术服务费
      yearly_tech_service_fare = v_yearly_tech_service_fare,
      -- 年度累计到款
      yearly_total_receive = v_yearly_total_receive,
      -- 年度累计调账
      yearly_adjust_amt = v_yearly_adjust_amt,
      -- 年度累计分期预付
      yearly_advance_amount = v_yearly_advance_amount,
      -- 本期调账金额
      adjust_amt = v_adjust_amt,
      -- 本期到款
      total_receive = v_total_receive,
      -- 出账完毕
      charge_off = '1' 
    WHERE belong_type = param_belong_type 
      AND bill_id = param_bill_id 
      AND order_id = v_order_id 
      AND (
        param_belong_type = '1' 
        OR (
          combine_id = v_combine_id 
          AND product_id = v_product_id 
          AND feemodel_id = v_feemodel_id
        )
      ) ;
    -- 季度累计技术服务费
    SET v_season_tech_service_fare = 
    (SELECT 
      CASE
        -- 如果是年度第一个月，取当月的年度累计技术服务费
        WHEN fun_get_month_diff (
          charge_begin_time,
          yearly_begin_date
        ) = 3 
        THEN U.yearly_tech_service_fare -- 如果是年度中间的季度，取当月的年度累计技术服务费减去之前季度的累计技术服务费
        WHEN fun_get_month_diff (
          charge_begin_time,
          yearly_begin_date
        ) % 3 = 0 
        THEN U.yearly_tech_service_fare - IFNULL(
          (SELECT 
            SUM(elo.yearly_tech_service_fare) 
          FROM
            charge_bill_info elo 
          WHERE elo.belong_type = U.belong_type 
            AND elo.order_id = U.order_id 
            AND (
              param_belong_type = '1' 
              OR (
                elo.combine_id = U.combine_id 
                AND elo.product_id = U.product_id 
                AND elo.feemodel_id = U.feemodel_id
              )
            ) 
            AND fun_get_month_diff (
              U.charge_begin_time,
              elo.charge_begin_time
            ) = 4),
          0
        ) -- 第13个月账单，取当月的年度累计技术服务费
        WHEN fun_get_month_diff (
          charge_begin_time,
          yearly_begin_date
        ) = 13 
        THEN tech_service_fare -- 提前终止
        -- 如果在年度的头三个月，取当月的年度累计技术服务费
        WHEN U.charge_end_time = U.prod_end_date 
        AND fun_get_month_diff (
          charge_begin_time,
          yearly_begin_date
        ) < 3 
        THEN U.yearly_tech_service_fare -- 如果在年度的三个月后，取当月的年度累计技术服务费减去之前季度的累计技术服务费
        WHEN U.charge_end_time = U.prod_end_date 
        AND fun_get_month_diff (
          charge_begin_time,
          yearly_begin_date
        ) > 3 
        THEN U.yearly_tech_service_fare - IFNULL(
          (SELECT 
            SUM(elo.yearly_tech_service_fare) 
          FROM
            charge_bill_info elo 
          WHERE elo.belong_type = U.belong_type 
            AND elo.order_id = U.order_id 
            AND (
              param_belong_type = '1' 
              OR (
                elo.combine_id = U.combine_id 
                AND elo.product_id = U.product_id 
                AND elo.feemodel_id = U.feemodel_id
              )
            ) 
            AND fun_get_month_diff (
              U.charge_begin_time,
              elo.charge_begin_time
            ) = (
              fun_get_month_diff (
                U.prod_end_date,
                U.yearly_begin_date
              ) % 3
            ) + 1),
          0
        ) 
        ELSE 0 
      END 
    FROM
      (SELECT 
        fun_get_yearly_begin_date (
          charge_begin_time,
          prod_begin_date
        ) yearly_begin_date,
        IFNULL(tech_service_fare, 0) tech_service_fare,
        prod_begin_date,
        prod_end_date,
        charge_begin_time,
        charge_end_time,
        bill_id,
        combine_id,
        product_id,
        feemodel_id,
        order_id,
        IFNULL(yearly_tech_service_fare, 0) yearly_tech_service_fare,
        belong_type 
      FROM
        charge_bill_info 
      WHERE belong_type = param_belong_type 
        AND (
          param_belong_type = '1' 
          OR (
            combine_id = v_combine_id 
            AND product_id = v_product_id 
            AND feemodel_id = v_feemodel_id
          )
        ) 
        AND bill_id = v_bill_id) U) ;
    UPDATE 
      charge_bill_info 
    SET
      -- 季度累计技术服务费
      season_paid = v_season_tech_service_fare 
    WHERE  belong_type = param_belong_type 
      AND bill_id = param_bill_id 
      AND order_id = v_order_id 
      AND (
        param_belong_type = '1' 
        OR (
          combine_id = v_combine_id 
          AND product_id = v_product_id 
          AND feemodel_id = v_feemodel_id
        )
      ) ;
  END LOOP cursor_loop ;
  CLOSE receipt_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_charge_bill_cmp` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_charge_bill_cmp` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_charge_bill_cmp`(IN TIME VARCHAR(10))
BEGIN
	-- 执行对比之前先清除老数据
	DELETE
FROM
	charge_bill_compare;

INSERT INTO charge_bill_compare (
	batch_no,
	bill_id,
	charge_begin_date,
	charge_end_date,
	prod_begin_date,
	prod_end_date,
	contract_id,
	combine_id,
	product_id,
	feemodel_id,
	belong_type,
	fee_ratio,
	charge_amt,
	service_charge,
	org_amt,
	adjust_amt,
	min_type,
	min_consume,
	max_type,
	max_consume,
	season_charge,
	total_bankreceipt,
	yearly_service_charge,
	yearly_total_receive,
	yearly_adjust_amt,
	yearly_advance_amount,
	receivable,
	charge_off
) SELECT
	batch_no,
	bill_id,
	charge_begin_date,
	charge_end_date,
	prod_begin_date,
	prod_end_date,
	contract_id,
	combine_id,
	product_id,
	feemodel_id,
	belong_type,
	fee_ratio,
	charge_amt,
	service_charge,
	org_amt,
	adjust_amt,
	min_type,
	min_consume,
	max_type,
	max_consume,
	season_charge,
	total_bankreceipt,
	yearly_service_charge,
	yearly_total_receive,
	yearly_adjust_amt,
	yearly_advance_amount,
	receivable,
	charge_off
FROM
	charge_bill_info elo
WHERE
	(
		-- 对参数TIME进行null值的判断，后续所有判断雷同
		CASE
		WHEN TIME IS NOT NULL THEN
			(
				elo.charge_begin_date >= DATE_FORMAT(
					CONCAT(TIME, '01'),
					'%Y-%m-%d'
				)
				AND elo.charge_begin_date <= DATE_FORMAT(
					LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
					'%Y-%m-%d'
				)
			)
		ELSE
			1 = 1
		END
	)
AND NOT EXISTS (
	SELECT
		batch_no,
		bill_id,
		charge_begin_date,
		charge_end_date,
		prod_begin_date,
		prod_end_date,
		contract_id,
		combine_id,
		product_id,
		feemodel_id,
		belong_type,
		fee_ratio,
		charge_amt,
		service_charge,
		org_amt,
		adjust_amt,
		min_type,
		min_consume,
		max_type,
		max_consume,
		season_charge,
		total_bankreceipt,
		yearly_service_charge,
		yearly_total_receive,
		yearly_adjust_amt,
		yearly_advance_amount,
		receivable,
		charge_off
	FROM
		charge_bill_info_bk elok
	WHERE
		(
			CASE
			WHEN TIME IS NOT NULL THEN
				(
					elok.charge_begin_date >= DATE_FORMAT(
						CONCAT(TIME, '01'),
						'%Y-%m-%d'
					)
					AND elok.charge_begin_date <= DATE_FORMAT(
						LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
						'%Y-%m-%d'
					)
				)
			ELSE
				1 = 1
			END
		)
	AND elok.bill_id = elo.bill_id
	AND (
		CASE -- 对参数combine_id的null值进行判断
		WHEN elok.combine_id IS NOT NULL THEN
			elok.combine_id = elo.combine_id
		ELSE
			1 = 1
		END
	)
	AND (
		CASE -- 对参数product_id的null值进行判断
		WHEN elok.product_id IS NOT NULL THEN
			elok.product_id = elo.product_id
		ELSE
			1 = 1
		END
	)
	AND (
			CASE -- 对参数feemodel_id的null值进行判断
			WHEN elok.feemodel_id IS NOT NULL THEN
				elok.feemodel_id = elo.feemodel_id
			ELSE
				1 = 1
			END
		)
	AND elok.belong_type = elo.belong_type
)
UNION
	SELECT
		batch_no,
	bill_id,
	charge_begin_date,
	charge_end_date,
	prod_begin_date,
	prod_end_date,
	contract_id,
	combine_id,
	product_id,
	feemodel_id,
	belong_type,
	fee_ratio,
	charge_amt,
	service_charge,
	org_amt,
	adjust_amt,
	min_type,
	min_consume,
	max_type,
	max_consume,
	season_charge,
	total_bankreceipt,
	yearly_service_charge,
  yearly_total_receive,
	yearly_adjust_amt,
	yearly_advance_amount,
	receivable,
	charge_off
	FROM
		charge_bill_info_bk elok
	WHERE
		(
			CASE
			WHEN TIME IS NOT NULL THEN
				(
					elok.charge_begin_date >= DATE_FORMAT(
						CONCAT(TIME, '01'),
						'%Y-%m-%d'
					)
					AND elok.charge_begin_date <= DATE_FORMAT(
						LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
						'%Y-%m-%d'
					)
				)
			ELSE
				1 = 1
			END
		)
	AND NOT EXISTS (
		SELECT
			batch_no,
	bill_id,
	charge_begin_date,
	charge_end_date,
	prod_begin_date,
	prod_end_date,
	contract_id,
	combine_id,
	product_id,
	feemodel_id,
	belong_type,
	fee_ratio,
	charge_amt,
	service_charge,
	org_amt,
	adjust_amt,
	min_type,
	min_consume,
	max_type,
	max_consume,
	season_charge,
	total_bankreceipt,
	yearly_service_charge,
  yearly_total_receive,
	yearly_adjust_amt,
	yearly_advance_amount,
	receivable,
	charge_off
		FROM
			charge_bill_info elo
		WHERE
			(
				CASE
				WHEN TIME IS NOT NULL THEN
					(
						elo.charge_begin_date >= DATE_FORMAT(
							CONCAT(TIME, '01'),
							'%Y-%m-%d'
						)
						AND elo.charge_begin_date <= DATE_FORMAT(
							LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
							'%Y-%m-%d'
						)
					)
				ELSE
					1 = 1
				END
			)
		AND elok.bill_id = elo.bill_id
		AND (
			CASE
			WHEN elok.combine_id IS NOT NULL THEN
				elok.combine_id = elo.combine_id
			ELSE
				1 = 1
			END
		)
		AND (
			CASE
			WHEN elok.product_id IS NOT NULL THEN
				elok.product_id = elo.product_id
			ELSE
				1 = 1
			END
		)
		AND (
			CASE
			WHEN elok.feemodel_id IS NOT NULL THEN
				elok.feemodel_id = elo.feemodel_id
			ELSE
				1 = 1
			END
		)
		AND elok.belong_type = elo.belong_type
	);

INSERT INTO charge_bill_compare (
	batch_no,
	bill_id,
	charge_begin_date,
	charge_end_date,
	prod_begin_date,
	prod_end_date,
	contract_id,
	combine_id,
	product_id,
	feemodel_id,
	belong_type,
	fee_ratio,
	charge_amt,
	service_charge,
	org_amt,
	adjust_amt,
	min_type,
	min_consume,
	max_type,
	max_consume,
	season_charge,
	total_bankreceipt,
	yearly_service_charge,
  yearly_total_receive,
	yearly_adjust_amt,
	yearly_advance_amount,
	receivable,
	charge_off
) SELECT
	elo.batch_no,
	elo.bill_id,
	elo.charge_begin_date,
	elo.charge_end_date,
	elo.prod_begin_date,
	elo.prod_end_date,
	elo.contract_id,
	elo.combine_id,
	elo.product_id,
	elo.feemodel_id,
	elo.belong_type,
	elo.fee_ratio,
	elo.charge_amt,
	elo.service_charge,
	elo.org_amt,
	elo.adjust_amt,
	elo.min_type,
	elo.min_consume,
	elo.max_type,
	elo.max_consume,
	elo.season_charge,
	elo.total_bankreceipt,
	elo.yearly_service_charge,
	elo.yearly_total_receive,
	elo.yearly_adjust_amt,
	elo.yearly_advance_amount,
	elo.receivable,
	elo.charge_off
FROM
	charge_bill_info_bk elok
LEFT JOIN charge_bill_info elo ON (
	elok.bill_id = elo.bill_id
	AND (
		CASE
		WHEN elok.combine_id IS NOT NULL THEN
			elok.combine_id = elo.combine_id
		ELSE
			1 = 1
		END
	)
	AND (
		CASE
		WHEN elok.product_id IS NOT NULL THEN
			elok.product_id = elo.product_id
		ELSE
			1 = 1
		END
	)
	AND elok.feemodel_id = elo.feemodel_id
	AND elok.belong_type = elo.belong_type
)
WHERE
	(
		CASE
		WHEN TIME IS NOT NULL THEN
			(
				elok.charge_begin_date >= DATE_FORMAT(
					CONCAT(TIME, '01'),
					'%Y-%m-%d'
				)
				AND elok.charge_begin_date <= DATE_FORMAT(
					LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
					'%Y-%m-%d'
				)
			)
		ELSE
			1 = 1
		END
	)
AND (
	elok.charge_amt != elo.charge_amt
	OR elok.service_charge != elo.service_charge
	OR elok.org_amt != elo.org_amt
	OR elok.adjust_amt != elo.adjust_amt
	OR elok.season_charge != elo.season_charge
	OR elok.total_bankreceipt != elo.total_bankreceipt
	OR elok.yearly_service_charge != elo.yearly_service_charge
	OR elok.yearly_total_receive != elo.yearly_total_receive
	OR elok.yearly_adjust_amt != elo.yearly_adjust_amt
	OR elok.yearly_advance_amount != elo.yearly_advance_amount
	OR elok.receivable != elo.receivable
)
GROUP BY
	elok.bill_id,
	elok.combine_id,
	elok.product_id,
	elok.feemodel_id,
	elok.belong_type;


END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_charge_delete_old_record` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_charge_delete_old_record` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_charge_delete_old_record`(param_batch_no VARCHAR (40))
BEGIN
  -- 账单编号
  DECLARE v_bill_id VARCHAR (60) ;
  DECLARE done INT DEFAULT 0 ;
  DECLARE order_cur CURSOR FOR 
  SELECT 
    bill_id 
  FROM
    charge_period_temp 
  WHERE batch_no = param_batch_no ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  OPEN order_cur ;
  cursor_loop :
  LOOP
    FETCH order_cur INTO v_bill_id ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    DELETE 
    FROM
      ss_order_info 
    WHERE bill_id = v_bill_id ;
    DELETE 
    FROM
      ss_order_combine 
    WHERE bill_id = v_bill_id ;
    DELETE 
    FROM
      ss_order_product 
    WHERE bill_id = v_bill_id ;
    DELETE 
    FROM
      ss_order_model 
    WHERE bill_id = v_bill_id ;
    DELETE 
    FROM
      ss_order_price 
    WHERE bill_id = v_bill_id ;
    DELETE 
    FROM
      charge_bill_info 
    WHERE bill_id = v_bill_id ;
    DELETE 
    FROM
      charge_trans_detail 
    WHERE bill_id = v_bill_id ;
    DELETE 
    FROM
      charge_receipt 
    WHERE bill_id = v_bill_id ;
    DELETE 
    FROM
      charge_order_info 
    WHERE bill_id = v_bill_id ;
    DELETE 
    FROM
      charge_trans_detail 
    WHERE bill_id = v_bill_id ;
    DELETE 
    FROM
      ss_order_adv_payment 
    WHERE bill_id = v_bill_id ;
  END LOOP cursor_loop ;
  CLOSE order_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_charge_detail_cmp` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_charge_detail_cmp` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_charge_detail_cmp`(IN TIME VARCHAR(10))
BEGIN
	-- 执行对比之前先清除老数据
	DELETE
FROM
	charge_detail_compare;

INSERT INTO charge_detail_compare (
	office_id,
	con_id,
	contract_id,
	product_id,
	product_name,
	charge_begin_date,
	charge_end_date,
	payment_type,
	income,
	detailid,
	income_begin_date,
	income_end_date,
	split_amount
) SELECT
	office_id,
	con_id,
	contract_id,
	product_id,
	product_name,
	charge_begin_date,
	charge_end_date,
	payment_type,
	income,
	detailid,
	income_begin_date,
	income_end_date,
	split_amount
FROM
	charge_detail_temp elp
WHERE
	(
		-- 对参数TIME进行null值的判断，后续所有判断雷同
		CASE
		WHEN TIME IS NOT NULL THEN
			(
				elp.charge_begin_date >= DATE_FORMAT(
					CONCAT(TIME, '01'),
					'%Y-%m-%d'
				)
				AND elp.charge_begin_date <= DATE_FORMAT(
					LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
					'%Y-%m-%d'
				)
			)
		ELSE
			1 = 1
		END
	)
AND NOT EXISTS (
	SELECT
		office_id,
		con_id,
		contract_id,
		product_id,
		product_name,
		charge_begin_date,
		charge_end_date,
		payment_type,
		income,
		detailid,
		income_begin_date,
		income_end_date,
		split_amount
	FROM
		charge_detail_temp_bk elpk
	WHERE
		(
			CASE
			WHEN TIME IS NOT NULL THEN
				(
					elpk.charge_begin_date >= DATE_FORMAT(
						CONCAT(TIME, '01'),
						'%Y-%m-%d'
					)
					AND elpk.charge_begin_date <= DATE_FORMAT(
						LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
						'%Y-%m-%d'
					)
				)
			ELSE
				1 = 1
			END
		)
	AND elpk.contract_id = elp.contract_id
	AND elpk.product_id = elp.product_id
	AND elpk.payment_type = elp.payment_type
	AND elpk.charge_begin_date = elp.charge_begin_date
)
UNION
	SELECT
		office_id,
		con_id,
		contract_id,
		product_id,
		product_name,
		charge_begin_date,
		charge_end_date,
		payment_type,
		income,
		detailid,
		income_begin_date,
		income_end_date,
		split_amount
	FROM
		charge_detail_temp_bk elpk
	WHERE
		(
			CASE
			WHEN TIME IS NOT NULL THEN
				(
					elpk.charge_begin_date >= DATE_FORMAT(
						CONCAT(TIME, '01'),
						'%Y-%m-%d'
					)
					AND elpk.charge_begin_date <= DATE_FORMAT(
						LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
						'%Y-%m-%d'
					)
				)
			ELSE
				1 = 1
			END
		)
	AND NOT EXISTS (
		SELECT
			office_id,
			con_id,
			contract_id,
			product_id,
			product_name,
			charge_begin_date,
			charge_end_date,
			payment_type,
			income,
			detailid,
			income_begin_date,
			income_end_date,
			split_amount
		FROM
			charge_detail_temp elp
		WHERE
			(
				CASE
				WHEN TIME IS NOT NULL THEN
					(
						elp.charge_begin_date >= DATE_FORMAT(
							CONCAT(TIME, '01'),
							'%Y-%m-%d'
						)
						AND elp.charge_begin_date <= DATE_FORMAT(
							LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
							'%Y-%m-%d'
						)
					)
				ELSE
					1 = 1
				END
			)
		AND elpk.contract_id = elp.contract_id
		AND elpk.product_id = elp.product_id
		AND elpk.payment_type = elp.payment_type
		AND elpk.charge_begin_date = elp.charge_begin_date
	);

INSERT INTO charge_detail_compare (
	office_id,
	con_id,
	contract_id,
	product_id,
	product_name,
	charge_begin_date,
	charge_end_date,
	payment_type,
	income,
	detailid,
	income_begin_date,
	income_end_date,
	split_amount
) SELECT
	elp.office_id,
	elp.con_id,
	elp.contract_id,
	elp.product_id,
	elp.product_name,
	elp.charge_begin_date,
	elp.charge_end_date,
	elp.payment_type,
	elp.income,
	elp.detailid,
	elp.income_begin_date,
	elp.income_end_date,
	elp.split_amount
FROM
	charge_detail_temp_bk elpk
LEFT JOIN charge_detail_temp elp ON (
	elpk.contract_id = elp.contract_id
	AND elpk.product_id = elp.product_id
	AND elpk.payment_type = elp.payment_type
	AND elpk.charge_begin_date = elp.charge_begin_date
)
WHERE
	(
		CASE
		WHEN TIME IS NOT NULL THEN
			(
				elpk.charge_begin_date >= DATE_FORMAT(
					CONCAT(TIME, '01'),
					'%Y-%m-%d'
				)
				AND elpk.charge_begin_date <= DATE_FORMAT(
					LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
					'%Y-%m-%d'
				)
			)
		ELSE
			1 = 1
		END
	)
AND (
	elpk.income != elp.income
	OR elpk.income_begin_date != elp.income_begin_date
	OR elpk.income_end_date != elp.income_end_date
	OR elpk.split_amount != elp.split_amount
)
GROUP BY
	elpk.contract_id,
	elpk.product_id,
	elpk.payment_type,
	elpk.charge_begin_date;


END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_charge_income_confirm` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_charge_income_confirm` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_charge_income_confirm`(param_contract_id VARCHAR (40))
BEGIN
  -- 删除
  DELETE 
  FROM
    charge_income_confirm 
  WHERE param_contract_id IS NULL 
    OR contract_id = param_contract_id ;
  -- 复制数据
  INSERT INTO charge_income_confirm (
    contract_id,
    charge_month,
    income,
    service_charge,
    product_id,
    income_begin_date,
    income_end_date
  ) 
  SELECT 
    contract_id,
    DATE_FORMAT(charge_begin_date, '%Y%m'),
    SUM(total_finance_income) AS income,
    SUM(
      CASE
        WHEN payment_type = '12475' 
        THEN total_finance_income 
        ELSE 0 
      END
    ) AS service_charge,
    ex_product_id,
    income_begin_date,
    income_end_date 
  FROM
    charge_income_interface 
  WHERE (
      param_contract_id IS NULL 
      OR contract_id = param_contract_id
    ) 
    AND update_flag = '1' 
  GROUP BY contract_id,
    ex_product_id,
    income_begin_date,
    DATE_FORMAT(charge_begin_date, '%Y%m') ;
  -- 更新增量
  UPDATE 
    charge_income_confirm U,
    (SELECT 
      em.*,
      em.income - 
      (SELECT 
        IFNULL(MAX(income), 0) AS income 
      FROM
        charge_income_confirm 
      WHERE charge_month < em.charge_month 
        AND contract_id = em.contract_id 
        AND product_id = em.product_id 
        AND income_begin_date = em.income_begin_date) AS change_income_temp,
      em.service_charge - 
      (SELECT 
        IFNULL(MAX(service_charge), 0) AS income 
      FROM
        charge_income_confirm 
      WHERE charge_month < em.charge_month 
        AND contract_id = em.contract_id 
        AND product_id = em.product_id 
        AND income_begin_date = em.income_begin_date) AS change_service_charge_temp 
    FROM
      charge_income_confirm em) V 
  SET
    U.change_income = V.change_income_temp,
    U.change_service_charge = V.change_service_charge_temp 
  WHERE U.charge_month = V.charge_month 
    AND U.contract_id = V.contract_id 
    AND U.product_id = V.product_id 
    AND U.income_begin_date = V.income_begin_date 
    AND (
      param_contract_id IS NULL 
      OR U.contract_id = param_contract_id
    ) ;
  -- 更新到款 
  UPDATE 
    charge_income_confirm em 
  SET
    em.receivable = 
    (SELECT 
      SUM(bankreceipt_amount) 
    FROM
      charge_bankreceipt 
    WHERE contract_id = em.contract_id 
      AND DATE_FORMAT(bankreceipt_month, '%Y%m') <= em.charge_month) ;
  -- 查询
  SELECT 
    vtf.dept_name AS '所属公司',
    vrf.chinesename AS '客户名称',
    ro.customer_id AS '客户编号',
    em.contract_id AS '合同编号',
    em.charge_month AS '月份',
    vef.productid AS '产品编号',
    vef.productname AS '产品名称',
    em.income AS '收入(累计)',
    em.change_income AS '收入(增量)',
    em.service_charge AS '技术服务费(累计)',
    em.change_service_charge AS '技术服务费(增量)',
    em.receivable AS '到款(累计)',
    em.change_receivable AS '到款(增量)' 
  FROM
    charge_income_confirm em 
    LEFT JOIN order_info ro 
      ON em.contract_id = ro.contract_id 
    LEFT JOIN v_customer_jf vrf 
      ON ro.customer_id = vrf.customerid 
    LEFT JOIN v_department_jf vtf 
      ON ro.office_id = vtf.hs_dept_id 
    LEFT JOIN v_prdsale_jf vef 
      ON em.product_id = vef.prdid 
  WHERE param_contract_id IS NULL 
    OR em.contract_id = param_contract_id 
  ORDER BY em.contract_id,
    em.charge_month ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_charge_order` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_charge_order` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_charge_order`(param_batch_no VARCHAR (20))
BEGIN
  -- 根据统一接口临时表计费产品id，协同合同号，协同客户号，进行错单判断，并将错误信息插入错单表
  CALL sp_gen_charge_wrong (param_batch_no) ;
  -- 根据错单表内容，删除统一接口表的错误记录(没做)
  -- 对合同内不同期间(月份)的数据进行拆分，插入计费子账单期间临时表
  CALL sp_gen_charge_period_temp (param_batch_no) ;
  -- 计算各个区间的实际终了时间（可能会发生年度末月拆分）
  CALL sp_gen_period_end_time (param_batch_no) ;
  -- 删除本期间内出过的账单
  CALL sp_charge_delete_old_record(param_batch_no);
  -- 按批次号，合同号，期间的开始、终了日，按照现有合同内容拍快照  
  CALL sp_gen_charge_snapshot (param_batch_no) ;
  -- 进行计费，生成产品级计费信息,组合级计费信息，合同级计费信息 
  CALL sp_gen_charge_fee(param_batch_no);
  -- 删除中间表对应的记录(没做)
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_charge_order_cmp` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_charge_order_cmp` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_charge_order_cmp`(IN TIME VARCHAR(10))
BEGIN
	-- 执行对比之前先清除老数据
	DELETE
FROM
	charge_order_compare;

INSERT INTO charge_order_compare (
	batch_no,
	bill_id,
	contract_id,
	bill_display_date,
	customer_name,
	pay_deadline,
	collec_comp_name,
	total_service_charge,
	total_bankreceipt,
	total_adjust_amt,
	total_advance_service_charge,
	adjust_amt,
	receivable,
	season_charge,
	payable,
	total_advance_charge
) SELECT
	batch_no,
	bill_id,
	contract_id,
	bill_display_date,
	customer_name,
	pay_deadline,
	collec_comp_name,
	total_service_charge,
	total_bankreceipt,
	total_adjust_amt,
	total_advance_service_charge,
	adjust_amt,
	receivable,
	season_charge,
	payable,
	total_advance_charge
FROM
	charge_order_info ero
WHERE
	(
		-- 对参数TIME进行null值的判断，后续所有判断雷同
		CASE
		WHEN TIME IS NOT NULL THEN
			(
				ero.bill_display_date >= DATE_FORMAT(
					CONCAT(TIME, '01'),
					'%Y-%m-%d'
				)
				AND ero.bill_display_date <= DATE_FORMAT(
					LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
					'%Y-%m-%d'
				)
			)
		ELSE
			1 = 1
		END
	)
AND NOT EXISTS (
	SELECT
		batch_no,
		bill_id,
		contract_id,
		bill_display_date,
		customer_name,
		pay_deadline,
		collec_comp_name,
		total_service_charge,
		total_bankreceipt,
		total_adjust_amt,
		total_advance_service_charge,
		adjust_amt,
		receivable,
		season_charge,
		payable,
		total_advance_charge
	FROM
		charge_order_info_bk erok
	WHERE
		(
			CASE
			WHEN TIME IS NOT NULL THEN
				(
					erok.bill_display_date >= DATE_FORMAT(
						CONCAT(TIME, '01'),
						'%Y-%m-%d'
					)
					AND erok.bill_display_date <= DATE_FORMAT(
						LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
						'%Y-%m-%d'
					)
				)
			ELSE
				1 = 1
			END
		)
	AND erok.bill_id = ero.bill_id
)
UNION
	SELECT
		batch_no,
		bill_id,
		contract_id,
		bill_display_date,
		customer_name,
		pay_deadline,
		collec_comp_name,
		total_service_charge,
		total_bankreceipt,
		total_adjust_amt,
		total_advance_service_charge,
		adjust_amt,
		receivable,
		season_charge,
		payable,
		total_advance_charge
	FROM
		charge_order_info_bk erok
	WHERE
		(
			CASE
			WHEN TIME IS NOT NULL THEN
				(
					erok.bill_display_date >= DATE_FORMAT(
						CONCAT(TIME, '01'),
						'%Y-%m-%d'
					)
					AND erok.bill_display_date <= DATE_FORMAT(
						LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
						'%Y-%m-%d'
					)
				)
			ELSE
				1 = 1
			END
		)
	AND NOT EXISTS (
		SELECT
			batch_no,
			bill_id,
			contract_id,
			bill_display_date,
			customer_name,
			pay_deadline,
			collec_comp_name,
			total_service_charge,
			total_bankreceipt,
			total_adjust_amt,
			total_advance_service_charge,
			adjust_amt,
			receivable,
			season_charge,
			payable,
			total_advance_charge
		FROM
			charge_order_info ero
		WHERE
			(
				CASE
				WHEN TIME IS NOT NULL THEN
					(
						ero.bill_display_date >= DATE_FORMAT(
							CONCAT(TIME, '01'),
							'%Y-%m-%d'
						)
						AND ero.bill_display_date <= DATE_FORMAT(
							LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
							'%Y-%m-%d'
						)
					)
				ELSE
					1 = 1
				END
			)
		AND erok.bill_id = ero.bill_id
	);

INSERT INTO charge_order_compare (
	batch_no,
	bill_id,
	contract_id,
	bill_display_date,
	customer_name,
	pay_deadline,
	collec_comp_name,
	total_service_charge,
	total_bankreceipt,
	total_adjust_amt,
	total_advance_service_charge,
	adjust_amt,
	receivable,
	season_charge,
	payable,
	total_advance_charge
) SELECT
	ero.batch_no,
	ero.bill_id,
	ero.contract_id,
	ero.bill_display_date,
	ero.customer_name,
	ero.pay_deadline,
	ero.collec_comp_name,
	ero.total_service_charge,
	ero.total_bankreceipt,
	ero.total_adjust_amt,
	ero.total_advance_service_charge,
	ero.adjust_amt,
	ero.receivable,
	ero.season_charge,
	ero.payable,
	ero.total_advance_charge
FROM
	charge_order_info_bk erok
LEFT JOIN charge_order_info ero ON erok.bill_id = ero.bill_id
WHERE
	(
		CASE
		WHEN TIME IS NOT NULL THEN
			(
				erok.bill_display_date >= DATE_FORMAT(
					CONCAT(TIME, '01'),
					'%Y-%m-%d'
				)
				AND erok.bill_display_date <= DATE_FORMAT(
					LAST_DAY(STR_TO_DATE(TIME, '%Y%m%d')),
					'%Y-%m-%d'
				)
			)
		ELSE
			1 = 1
		END
	)
AND (
	erok.total_service_charge != ero.total_service_charge
	OR erok.total_bankreceipt != ero.total_bankreceipt
	OR erok.total_adjust_amt != ero.total_adjust_amt
	OR erok.total_advance_service_charge != ero.total_advance_service_charge
	OR erok.adjust_amt != ero.adjust_amt
	OR erok.receivable != ero.receivable
	OR erok.season_charge != ero.season_charge
	OR erok.payable != ero.payable
	OR erok.total_advance_charge != ero.total_advance_charge
)
GROUP BY
	erok.bill_id;


END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_charge_validate_order` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_charge_validate_order` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_charge_validate_order`(param_hs_contract_id VARCHAR (40))
BEGIN
  -- 计费比较协同
  -- 删除订单验证表信息(隐藏的验证不删除)
  DELETE 
  FROM
    order_validation_info 
  WHERE (
      hs_contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) 
    AND order_id != '' 
    AND show_status = '0' 
    AND (remark IS NULL 
      OR remark = '') ;
      
      
  -- 插入订单验证表:存在固定费用的费用金额未设置情况
  INSERT INTO order_validation_info (
  serial_no,
    hs_contract_id,
    order_id,
    combine_id,
    reason,
    belong_type
  ) 
  (SELECT 
  REPLACE(UUID(), '-', ''),
    opc.contract_id,
    oi.id,
    opc.combine_id,
    '存在固定费用的费用金额未设置' reason,
    '2' 
  FROM
    order_info oi,
    order_adv_payment opc 
  WHERE opc.contract_id = oi.contract_id 
    AND (
      oi.contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) 
    -- 删除标志为0
    AND oi.del_flag=0 AND opc.del_flag=0
    AND opc.amount IS NULL) ;
    
  -- 插入订单验证表:计费开始、计费结束时间有误情况
  INSERT INTO order_validation_info (
  serial_no,
    hs_contract_id,
    order_id,
    combine_id,
    reason,
    belong_type
  ) 
  (SELECT 
  REPLACE(UUID(), '-', ''),
    om.contract_id,
    om.id,
    om.combine_id,
    om.reason,
    '2' 
  FROM
    (SELECT 
      oc.contract_id,
      oi.id,
      oc.id combine_id,
      CASE
        WHEN (
          DATE_FORMAT(
            oc.combine_begin_date,
            '%Y-%m-%d'
          ) = '0000-00-00' 
          AND DATE_FORMAT(oc.combine_end_date, '%Y-%m-%d') = '0000-00-00'
        ) 
        THEN '计费开始,结束时间不能为空' 
        WHEN DATE_FORMAT(
          oc.combine_begin_date,
          '%Y-%m-%d'
        ) = '0000-00-00' 
        THEN '计费开始时间不能为空' 
        WHEN DATE_FORMAT(oc.combine_end_date, '%Y-%m-%d') = '0000-00-00' 
        THEN '计费结束时间不能为空' 
        WHEN DATE_FORMAT(
          oc.combine_begin_date,
          '%Y-%m-%d'
        ) > DATE_FORMAT(oc.combine_end_date, '%Y-%m-%d') 
        THEN '计费开始时间不能大于计费结束时间' 
        ELSE NULL 
      END reason 
    FROM
      order_info oi,
      order_combine oc 
    WHERE oc.contract_id = oi.contract_id 
      AND (
        (oi.contract_id = param_hs_contract_id 
        OR param_hs_contract_id IS NULL)
         -- 删除标志为0
        AND oi.del_flag=0 AND oc.del_flag=0
      )) om 
  WHERE om.reason IS NOT NULL) ;
  
  
  -- 插入订单验证表:预付日期超出计费开始、计费结束时间情况
  INSERT INTO order_validation_info (
  serial_no,
    hs_contract_id,
    order_id,
    combine_id,
    reason,
    belong_type
  ) 
  (SELECT 
   REPLACE(UUID(), '-', ''),
    om.contract_id,
    om.id,
    om.combine_id,
    om.reason,
    '2' 
  FROM
    (SELECT 
      opc.contract_id,
      oi.id,
      opc.combine_id,
      CASE
        WHEN DATE_FORMAT(opc.advance_date, '%Y-%m-%d') < DATE_FORMAT(
          oc.combine_begin_date,
          '%Y-%m-%d'
        ) 
        THEN CONCAT(
          '预付日期:',
          opc.advance_date,
          '不能小于计费开始时间'
        ) 
        WHEN DATE_FORMAT(opc.advance_date, '%Y-%m-%d') > DATE_FORMAT(oc.combine_end_date, '%Y-%m-%d') 
        THEN CONCAT(
          '预付日期:',
          opc.advance_date,
          '不能大于计费结束时间'
        ) 
        ELSE NULL 
      END reason 
    FROM
      order_adv_payment opc,
      order_combine oc,
      order_info oi 
    WHERE (
        oi.contract_id = param_hs_contract_id 
        OR param_hs_contract_id IS NULL
      ) 
      AND oc.contract_id = oi.contract_id 
      AND oc.contract_id = opc.contract_id 
      AND oc.id = opc.combine_id 
      AND DATE_FORMAT(
        oc.combine_begin_date,
        '%Y-%m-%d'
      ) != '0000-00-00' 
      AND DATE_FORMAT(oc.combine_end_date, '%Y-%m-%d') != '0000-00-00' 
       -- 删除标志为0
       AND opc.del_flag=0 AND oc.del_flag=0 AND oi.del_flag=0
      AND opc.advance_date IS NOT NULL) om 
  WHERE om.reason IS NOT NULL) ;
  
  
  -- 插入订单验证表:存在费用类型为年费的计费时间段不是一个整年情况
  INSERT INTO order_validation_info (
   serial_no,
    hs_contract_id,
    order_id,
    combine_id,
    reason,
    belong_type
  ) 
  (SELECT 
  REPLACE(UUID(), '-', ''),
    oi.contract_id,
    oi.id,
    oc.id combine_id,
    '存在费用类型为年费的计费时间段不是一个整年',
    '2' 
  FROM
    order_info oi,
    order_combine oc,
    order_adv_payment opc 
  WHERE oc.contract_id = oi.contract_id
    AND opc.contract_id = oi.contract_id
    AND opc.combine_id = oc.id 
    AND (
      oi.contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) 
    AND opc.payment_type = '12473' 
     -- 删除标志为0
       AND opc.del_flag=0 AND oc.del_flag=0 AND oi.del_flag=0
    AND DATE_FORMAT(
      fun_get_yearly_end_date (oc.combine_begin_date),
      '%Y-%m-%d'
    ) != DATE_FORMAT(oc.combine_end_date, '%Y-%m-%d')) ;
    
    
  -- 插入订单验证表:协同销售产品为空情况
  INSERT INTO order_validation_info (
   serial_no,
    hs_contract_id,
    order_id,
    combine_id,
    product_id,
    reason,
    belong_type
  ) 
  (SELECT 
   REPLACE(UUID(), '-', ''),
    op.contract_id,
    oi.id,
    op.combine_id,
    op.ex_product_id,
    '协同销售产品不能为空' reason,
    '3' 
  FROM
    order_product op,
    order_info oi 
  WHERE op.contract_id = oi.contract_id 
    AND (
      oi.contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) 
     -- 删除标志为0
       AND op.del_flag=0  AND oi.del_flag=0
    AND (
      op.product_id IS NULL 
      OR op.product_id = ''
    )) ;
    
    
   -- 插入订单验证表:协同销售产品存在重叠的有效时间情况(不同组合同产品)
  INSERT INTO order_validation_info (
serial_no,
    hs_contract_id,
    order_id,
    hs_product_id,
    reason,
    belong_type
  ) 
  (SELECT DISTINCT 
  REPLACE(UUID(), '-', ''),
  t1.contract_id,
  t1.id,
  t1.product_id,
  CONCAT(
    '协同销售产品:',
    t1.product_id,
    '存在重叠的有效时间'
  ) reason,
  '1' 
FROM
  (SELECT 
    oc.contract_id,
    oi.id,
    oc.id combine_id,
    rt.product_id,
    oc.combine_begin_date,
    oc.combine_end_date 
  FROM
    order_info oi,
    order_combine oc,
    order_product rt 
  WHERE oc.contract_id = oi.contract_id 
    AND rt.contract_id = oc.contract_id 
    AND rt.combine_id = oc.id 
    AND (
      oi.contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) -- 删除标志为0
    AND oi.del_flag = 0 
    AND oc.del_flag = 0 
    AND rt.del_flag = 0) t1,
  (SELECT 
    oc.contract_id,
    oi.id,
    oc.id combine_id,
    rt.product_id,
    oc.combine_begin_date,
    oc.combine_end_date 
  FROM
    order_info oi,
    order_combine oc,
    order_product rt 
  WHERE oc.contract_id = oi.contract_id 
    AND rt.contract_id = oc.contract_id 
    AND rt.combine_id = oc.id 
    AND (
      oi.contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) -- 删除标志为0
    AND oi.del_flag = 0 
    AND oc.del_flag = 0 
    AND rt.del_flag = 0) t2 
WHERE t1.contract_id = t2.contract_id 
  AND t1.combine_id != t2.combine_id 
  AND t1.product_id = t2.product_id 
  AND t1.combine_begin_date <= t2.combine_end_date 
  AND t1.combine_end_date >= t2.combine_begin_date) ; 
    
    
 -- 插入订单验证表:协同销售产品存在重叠的收入设置时间情况
  INSERT INTO order_validation_info (
serial_no,
    hs_contract_id,
    order_id,
    hs_product_id,
    reason,
    belong_type,
    hs_check_status
  ) 
  (SELECT DISTINCT 
REPLACE(UUID(), '-', ''),
  t1.contract_id,
  t1.id,
  t1.product_id,
  CONCAT(
    '协同销售产品:',
    t1.product_id,
    '存在重叠的收入设置时间'
  ) reason,
  '1',
  '2'
FROM
  (SELECT 
    oi.contract_id,
    oi.id,
    ois.product_id,
    ois.payment_type,
    ois.income_begin_date,
    ois.income_end_date 
  FROM
    order_info oi,
    order_income_setting ois 
  WHERE ois.contract_id = oi.contract_id 
  AND (
      oi.contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    )
  -- 删除标志为0
    AND oi.del_flag = 0 
    AND ois.del_flag = 0) t1,
  (SELECT 
    oi.contract_id,
    oi.id,
    ois.product_id,
    ois.payment_type,
    ois.income_begin_date,
    ois.income_end_date 
  FROM
    order_info oi,
    order_income_setting ois 
  WHERE ois.contract_id = oi.contract_id 
AND (
      oi.contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    )  
  -- 删除标志为0
    AND oi.del_flag = 0 
    AND ois.del_flag = 0) t2 
WHERE t1.contract_id = t2.contract_id
  AND t1.product_id = t2.product_id 
  AND t1.payment_type != t2.payment_type
  AND t1.income_begin_date <= t2.income_end_date 
  AND t1.income_end_date >= t2.income_begin_date ) ; 
    
  -- 插入订单验证表:协同销售产品存在重叠的有效时间情况(不同组合同产品同计费类型)
  INSERT INTO order_validation_info (
  serial_no,
    hs_contract_id,
    order_id,
    hs_product_id,
    reason,
    belong_type
  ) 
  (SELECT DISTINCT 
   REPLACE(UUID(), '-', ''),
    t1.contract_id,
    t1.id,
    t1.product_id,
    CONCAT(
      '协同销售产品:',
      t1.product_id,
      '存在同计费类型的重叠有效时间'
    ) reason,
    '1' 
  FROM
    (SELECT 
      oc.contract_id,
      oi.id,
      oc.id combine_id,
      rt.product_id,
      rl.fee_type,
      oc.combine_begin_date,
      oc.combine_end_date 
    FROM
      order_info oi,
      order_combine oc,
      order_product rt,
      order_model rl 
    WHERE oc.contract_id = oi.contract_id 
      AND rt.contract_id = oc.contract_id 
      AND rt.combine_id = oc.id 
      AND rl.ref_id = rt.id
      AND rl.belong_type = '3' 
      -- 删除标志为0
       AND oi.del_flag=0  AND oc.del_flag=0 
       AND rt.del_flag=0 AND rl.del_flag=0
      AND (
        oi.contract_id = param_hs_contract_id 
        OR param_hs_contract_id IS NULL
      )) t1,
    (SELECT 
      oc.contract_id,
      oi.id,
      oc.id combine_id,
      rt.product_id,
      rl.fee_type,
      oc.combine_begin_date,
      oc.combine_end_date 
    FROM
      order_info oi,
      order_combine oc,
      order_product rt,
      order_model rl 
    WHERE oc.contract_id = oi.contract_id 
      AND rt.contract_id = oc.contract_id 
      AND rt.combine_id = oc.id 
      AND rl.ref_id = rt.id 
      AND rl.belong_type = '3' 
       -- 删除标志为0
       AND oi.del_flag=0  AND oc.del_flag=0 
       AND rt.del_flag=0  AND rl.del_flag=0
       
      AND (
        oi.contract_id = param_hs_contract_id 
        OR param_hs_contract_id IS NULL
      )) t2 
  WHERE t1.contract_id = t2.contract_id 
    AND t1.combine_id != t2.combine_id 
    AND t1.product_id = t2.product_id 
    AND t1.fee_type = t2.fee_type 
    AND t1.combine_begin_date <= t2.combine_end_date 
    AND t1.combine_end_date >= t2.combine_begin_date) ;
     
    
  -- 插入订单验证表:协同合同号在协同系统不存在情况
  INSERT INTO order_validation_info (
  serial_no,
    hs_contract_id,
    order_id,
    reason,
    belong_type,
    hs_check_status
  ) 
  (SELECT 
   REPLACE(UUID(), '-', ''),
    oi.contract_id,
    oi.id,
    '协同合同号在协同系统不存在' reason,
    '1',
    '1' 
  FROM
    order_info oi 
    LEFT JOIN v_contract_jf vcj 
      ON vcj.contractid = oi.contract_id 
  WHERE (
      oi.contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) 
     -- 删除标志为0
       AND oi.del_flag=0
    AND vcj.contractid IS NULL) ;
    
    
  -- 插入订单验证表:协同产品编号在协同系统不存在情况
  INSERT INTO order_validation_info (
   serial_no,
    hs_contract_id,
    order_id,
    combine_id,
    product_id,
    reason,
    belong_type,
    hs_check_status
  ) 
  (SELECT 
  REPLACE(UUID(), '-', ''),
    op.contract_id,
    oi.id,
    op.combine_id,
    op.ex_product_id,
    '协同产品编号在协同系统不存在' reason,
    '3',
    '1' 
  FROM
    order_info oi,
    order_product op 
    LEFT JOIN v_prdsale_jf vpj 
      ON vpj.productid = op.product_id 
  WHERE op.contract_id = oi.contract_id 
    AND (
      oi.contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) 
     -- 删除标志为0
       AND oi.del_flag=0 AND op.del_flag=0
    AND vpj.productid IS NULL) ;
    
    
  -- 插入订单验证表：协同产品固定费用类型在协同系统不存在情况
  -- (check_type:1,check_column:费用类型)
  INSERT INTO order_validation_info (
  serial_no,
    hs_contract_id,
    order_id,
    combine_id,
    hs_product_id,
    reason,
    belong_type,
    hs_check_status,
    check_type,
    check_column
  ) 
  (SELECT 
   REPLACE(UUID(), '-', ''),
    opt.contract_id,
    opt.id,
    opt.combine_id,
    opt.product_id,
    CONCAT(
      '费用类型:',
      CASE
        opt.payment_type 
        WHEN '12471' 
        THEN '一次性费用' 
        WHEN '12473' 
        THEN '年费' 
        WHEN '12472' 
        THEN '不定期' 
        WHEN '12474' 
        THEN '条件延后' 
        ELSE NULL 
      END,
      '在协同系统不存在'
    ) reason,
    '3',
    '1',
    '1',
    opt.payment_type 
  FROM
    (SELECT 
      oi.contract_id,
      oi.id,
      op.combine_id,
      op.product_id,
      op.ex_product_id,
      opc.payment_type 
    FROM
      order_product op,
      order_combine oc,
      order_adv_payment opc,
      order_info oi 
    WHERE (
        oi.contract_id = param_hs_contract_id 
        OR param_hs_contract_id IS NULL
      ) 
      -- 删除标志为0
       AND oi.del_flag=0 AND op.del_flag=0
       AND oc.del_flag=0 AND opc.del_flag=0
       
      AND opc.contract_id = op.contract_id 
      AND oi.contract_id = op.contract_id 
      AND oc.contract_id = oi.contract_id 
      AND oc.id = opc.combine_id 
      AND opc.combine_id = op.combine_id 
      AND opc.payment_type != '10000' 
      AND EXISTS 
      (SELECT 
        contractid 
      FROM
        v_contract_jf 
      WHERE contractid = oi.contract_id) 
      AND EXISTS 
      (SELECT 
        prdid 
      FROM
        v_prdsale_jf 
      WHERE prdid = op.ex_product_id)) opt 
    LEFT JOIN 
      (SELECT DISTINCT 
        TRIM(vcj.contractid) AS contract_id,
        TRIM(vcdj.salprd_id) AS ex_product_id,
        TRIM(vcdj.chargetype) AS payment_type 
      FROM
        v_contract_jf vcj,
        v_contract_detail_jf vcdj 
      WHERE vcj.con_id = vcdj.con_id) vopt 
      ON vopt.contract_id = opt.contract_id 
      AND vopt.ex_product_id = opt.ex_product_id 
      AND vopt.payment_type = opt.payment_type 
  WHERE vopt.payment_type IS NULL) ;
  
  
  
  -- 插入订单验证表：协同产品固定费用类型在协同系统不存在情况
  -- (check_type:1,check_column:费用类型)
  INSERT INTO order_validation_info (
  serial_no,
    hs_contract_id,
    order_id,
    hs_product_id,
    reason,
    belong_type,
    hs_check_status,
    check_type,
    check_column
  ) 
  (SELECT
   REPLACE(UUID(), '-', ''),
    opt.contract_id,
    opt.id,
    vpj.productid,
    CONCAT(
      '产品:',
      IFNULL(vpj.productid, ''),
      '费用类型:技术服务费在协同系统不存在'
    ) reason,
    '3',
    '2',
    '1',
    opt.payment_type 
  FROM
    (SELECT 
      oi.contract_id,
      oi.id,
      ois.ex_product_id,
      ois.payment_type 
    FROM
      order_income_setting ois,
      order_info oi 
    WHERE (
        oi.contract_id = param_hs_contract_id 
        OR param_hs_contract_id IS NULL
      ) 
       -- 删除标志为0
      AND oi.del_flag=0 AND ois.del_flag=0
      
      AND ois.contract_id = oi.contract_id 
      AND ois.payment_type = '12475' 
      AND EXISTS 
      (SELECT 
        contractid 
      FROM
        v_contract_jf 
      WHERE contractid = oi.contract_id) 
      AND EXISTS 
      (SELECT 
        prdid 
      FROM
        v_prdsale_jf 
      WHERE prdid = ois.ex_product_id)) opt 
    LEFT JOIN 
      (SELECT DISTINCT 
        TRIM(vcj.contractid) AS contract_id,
        TRIM(vcdj.salprd_id) AS ex_product_id,
        TRIM(vcdj.chargetype) AS payment_type 
      FROM
        v_contract_jf vcj,
        v_contract_detail_jf vcdj 
      WHERE vcj.con_id = vcdj.con_id 
        AND vcdj.chargetype = '12475') vopt 
      ON vopt.contract_id = opt.contract_id 
      AND vopt.ex_product_id = opt.ex_product_id 
      AND vopt.payment_type = opt.payment_type 
    LEFT JOIN v_prdsale_jf vpj 
      ON vpj.prdid = opt.ex_product_id 
  WHERE vopt.payment_type IS NULL) ;
  
  
  
  -- 插入订单验证表:一次性费用，年费、不定期、延后费用收入时间与协同系统不一致情况
  -- (check_type:2,check_column:费用类型)
  INSERT INTO order_validation_info (
  serial_no,
    hs_contract_id,
    order_id,
    hs_product_id,
    reason,
    belong_type,
    hs_check_status,
    check_type,
    check_column
  ) 
  (SELECT 
   REPLACE(UUID(), '-', ''),
    opt.contract_id,
    opt.id,
    vpj.productid,
    CONCAT(
      '产品:',
      IFNULL(vpj.productid, ''),
      '费用类型:',
      CASE
        opt.payment_type 
        WHEN '12471' 
        THEN '一次性费用' 
        WHEN '12473' 
        THEN '年费' 
        WHEN '12472' 
        THEN '不定期' 
        WHEN '12474' 
        THEN '条件延后' 
        WHEN '12475' 
        THEN '技术服务费' 
        ELSE '' 
      END,
      '的收入开始日期:',
      IFNULL(
        DATE_FORMAT(opt.begin_date, '%Y-%m-%d'),
        ''
      ),
      ',结束日期:',
      IFNULL(
        DATE_FORMAT(opt.end_date, '%Y-%m-%d'),
        ''
      ),
      '与协同系统中不一致'
    ) reason,
    '3',
    '2',
    '2',
    opt.payment_type 
  FROM
    (SELECT 
      oi.contract_id,
      oi.id,
      ois.ex_product_id,
      ois.payment_type,
      DATE_FORMAT(
        ois.income_begin_date,
        '%Y-%m-%d'
      ) AS begin_date,
      DATE_FORMAT(ois.income_end_date, '%Y-%m-%d') AS end_date 
    FROM
      order_income_setting ois,
      order_info oi 
    WHERE (
        oi.contract_id = param_hs_contract_id 
        OR param_hs_contract_id IS NULL
      ) 
       -- 删除标志为0
      AND oi.del_flag=0 AND ois.del_flag=0
      
      AND ois.contract_id = oi.contract_id 
      AND ois.payment_type != '10000' 
      AND EXISTS 
      (SELECT 
        contractid 
      FROM
        v_contract_jf 
      WHERE contractid = oi.contract_id) 
      AND EXISTS 
      (SELECT 
        prdid 
      FROM
        v_prdsale_jf 
      WHERE prdid = ois.ex_product_id)) opt 
    LEFT JOIN 
      (SELECT 
        TRIM(vcj.contractid) AS contract_id,
        TRIM(vcdj.salprd_id) AS ex_product_id,
        TRIM(vcdj.servicestartdate) AS begin_date,
        TRIM(vcdj.serviceenddate) AS end_date,
        TRIM(vcdj.chargetype) AS payment_type 
      FROM
        v_contract_jf vcj,
        v_contract_detail_jf vcdj 
      WHERE vcj.con_id = vcdj.con_id) vopt 
      ON vopt.contract_id = opt.contract_id 
      AND vopt.ex_product_id = opt.ex_product_id 
      AND vopt.payment_type = opt.payment_type 
      AND opt.begin_date = vopt.begin_date 
      AND opt.end_date = vopt.end_date 
    LEFT JOIN v_prdsale_jf vpj 
      ON vpj.prdid = opt.ex_product_id 
  WHERE vopt.payment_type IS NULL) ;
  
  
  
  -- 将协同合同号,所属部门赋值给校验信息表
  UPDATE 
    order_validation_info rno,
    order_info oi 
  SET
    rno.hs_contract_id = oi.contract_id,
    rno.order_source = oi.office_id 
  WHERE rno.order_id = oi.id 
    AND (
      oi.contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) 
     -- 删除标志为0
      AND oi.del_flag=0 ;
    
    
  -- 将产品号赋值给校验信息表
  UPDATE 
    order_validation_info rno 
  SET
    hs_product_id = IFNULL(
      (SELECT 
        DISTINCT rt.product_id 
      FROM
        order_product rt 
      WHERE rt.ex_product_id = rno.product_id 
       -- 删除标志为0
      AND rt.del_flag=0 ),
      ''
    ) 
  WHERE (
      rno.hs_contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) 
    AND rno.product_id != '' ;
    
    
  -- 验证状态是隐藏或者有备注：验证通过则删除隐藏或者有备注的校验，验证不通过则删除新增的
  DELETE 
    rno.* 
  FROM
    order_validation_info rno,
    (SELECT 
      ovi2.serial_no,
      ovi1.serial_no AS serial_no0 
    FROM
      order_validation_info ovi2 
      LEFT JOIN order_validation_info ovi1 
        ON ovi1.order_id = ovi2.order_id 
        AND ovi1.combine_id = ovi2.combine_id 
        AND ovi1.product_id = ovi2.product_id 
        AND ovi1.reason = ovi2.reason 
        AND ovi1.show_status = '0' 
        AND (
          ovi1.remark IS NULL 
          OR ovi1.remark = ''
        ),
      order_info oi 
    WHERE ovi2.order_id = oi.id 
      AND (
        oi.contract_id = param_hs_contract_id 
        OR param_hs_contract_id IS NULL
      ) 
       -- 删除标志为0
       AND oi.del_flag=0
      AND (
        ovi2.show_status = '1' 
        OR (
          ovi2.remark IS NOT NULL 
          AND ovi2.remark != ''
        )
      )) U 
  WHERE (
      U.serial_no0 IS NULL 
      AND rno.serial_no = U.serial_no
    ) 
    OR (
      U.serial_no0 IS NOT NULL 
      AND rno.serial_no = U.serial_no0
    ) ;
    
    
    
  -- 删除订单验证表信息(隐藏的验证不删除)
  DELETE 
  FROM
    order_validation_info 
  WHERE (
      hs_contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) 
    AND order_id = '' 
    AND show_status = '0' 
    AND (remark IS NULL 
      OR remark = '') ;
      
      
  -- 插入订单验证表:协同合同号在协同系统存在的,计费系统中不存在情况
  INSERT INTO order_validation_info (
  serial_no,
    hs_contract_id,
    order_source,
    reason,
    belong_type,
    hs_check_status
  ) 
  (SELECT 
  REPLACE(UUID(), '-', ''),
    vcj.contractid,
    vcj.companyid,
    '协同合同号在协同系统存在的,计费系统中不存在' reason,
    '1',
    '1' 
  FROM
    v_contract_jf vcj 
    LEFT JOIN order_info oi 
      ON vcj.contractid = oi.contract_id 
  WHERE (
      vcj.contractid = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) 
    -- 删除标志为0
       AND oi.del_flag=0
    AND oi.contract_id IS NULL) ;
    
    
    
  -- 插入订单验证表:协同产品在协同系统存在的,计费系统中不存在情况
  INSERT INTO order_validation_info (
   serial_no,
    hs_contract_id,
    order_source,
    hs_product_id,
    reason,
    belong_type,
    hs_check_status
  ) 
  (SELECT DISTINCT 
  REPLACE(UUID(), '-', ''),
    vopt.hs_contract_id,
    vopt.order_source,
    vopt.product_sale_id,
    CONCAT(
      '产品:',
      IFNULL(vopt.product_sale_id, ''),
      '在协同系统存在,计费系统不存在'
    ) reason,
    '3',
    '1' 
  FROM
    (SELECT 
      TRIM(vcj.contractid) AS hs_contract_id,
      TRIM(vcj.companyid) AS order_source,
      TRIM(vcdj.salprd_id) AS salprd_id,
      vpj.productid AS product_sale_id 
    FROM
      v_contract_jf vcj,
      v_contract_detail_jf vcdj 
      LEFT JOIN v_prdsale_jf vpj 
        ON vpj.prdid = vcdj.salprd_id 
    WHERE vcj.con_id = vcdj.con_id 
      AND (
        TRIM(vcj.contractid) = param_hs_contract_id 
        OR param_hs_contract_id IS NULL
      ) 
      AND EXISTS 
      (SELECT 
        contract_id 
      FROM
        order_info 
      WHERE contract_id = TRIM(vcj.contractid)
      -- 删除标志为0
       AND del_flag=0)) vopt 
    LEFT JOIN 
      (SELECT 
        oi.contract_id,
        op.combine_id,
        op.product_id,
        op.ex_product_id 
      FROM
        order_product op,
        order_combine oc,
        order_info oi 
      WHERE (
          oi.contract_id = param_hs_contract_id 
          OR param_hs_contract_id IS NULL
        ) 
        -- 删除标志为0
       AND oi.del_flag=0  AND oc.del_flag=0 
        AND op.del_flag=0
       
        AND oi.contract_id = op.contract_id 
        AND oc.contract_id = oi.contract_id 
        AND oc.id = op.combine_id) opt 
      ON vopt.hs_contract_id = opt.contract_id 
      AND vopt.salprd_id = opt.ex_product_id 
  WHERE opt.ex_product_id IS NULL) ;
  
  
  
  -- 插入订单验证表:协同产品固定费用在协同系统存在的,计费系统中不存在情况
  -- (check_type:3,check_column:费用类型)
  INSERT INTO order_validation_info (
  serial_no,
    hs_contract_id,
    order_source,
    hs_product_id,
    reason,
    belong_type,
    hs_check_status,
    check_type,
    check_column
  ) 
  (SELECT DISTINCT 
  REPLACE(UUID(), '-', ''),
    vopt.hs_contract_id,
    vopt.order_source,
    vopt.product_sale_id,
    CONCAT(
      '产品:',
      IFNULL(vopt.product_sale_id, ''),
      '费用类型:',
      CASE
        vopt.payment_type 
        WHEN '12471' 
        THEN '一次性费用' 
        WHEN '12473' 
        THEN '年费' 
        WHEN '12472' 
        THEN '不定期' 
        WHEN '12474' 
        THEN '条件延后' 
        ELSE '' 
      END,
      '在协同系统存在,计费系统不存在'
    ) reason,
    '3',
    '1',
    '3',
    vopt.payment_type 
  FROM
    (SELECT 
      TRIM(vcj.contractid) AS hs_contract_id,
      TRIM(vcj.companyid) AS order_source,
      TRIM(vcdj.salprd_id) AS salprd_id,
      vpj.productid AS product_sale_id,
      TRIM(vcdj.chargetype) AS payment_type 
    FROM
      v_contract_jf vcj,
      v_contract_detail_jf vcdj 
      LEFT JOIN v_prdsale_jf vpj 
        ON vpj.prdid = vcdj.salprd_id 
    WHERE vcj.con_id = vcdj.con_id 
      AND vcdj.chargetype IS NOT NULL 
      AND vcdj.chargetype != '' 
      AND (
        TRIM(vcj.contractid) = param_hs_contract_id 
        OR param_hs_contract_id IS NULL
      ) 
      AND EXISTS 
      (SELECT 
        contract_id 
      FROM
        order_info 
      WHERE contract_id = TRIM(vcj.contractid)
       -- 删除标志为0
       AND del_flag=0) 
      AND EXISTS 
      (SELECT 
        oi.contract_id,
        op.ex_product_id
      FROM
        order_product op,
        order_info oi 
      WHERE oi.contract_id = op.contract_id
       -- 删除标志为0
       AND oi.del_flag=0 AND op.del_flag=0
        AND oi.contract_id = TRIM(vcj.contractid) 
        AND op.ex_product_id = TRIM(vcdj.salprd_id))) vopt 
    LEFT JOIN 
      (SELECT 
        oi.contract_id,
        op.combine_id,
        op.product_id,
        op.ex_product_id,
        opc.payment_type 
      FROM
        order_product op,
        order_combine oc,
        order_adv_payment opc,
        order_info oi 
      WHERE (
          oi.contract_id = param_hs_contract_id 
          OR param_hs_contract_id IS NULL
        ) 
        -- 删除标志为0
       AND oi.del_flag=0 AND op.del_flag=0
       AND oc.del_flag=0 AND opc.del_flag=0
       
        AND opc.contract_id = op.contract_id 
        AND oi.contract_id = op.contract_id 
        AND oc.contract_id = oi.contract_id 
        AND oc.id = opc.combine_id 
        AND opc.combine_id = op.combine_id) opt 
      ON vopt.hs_contract_id = opt.contract_id 
      AND vopt.salprd_id = opt.ex_product_id 
      AND vopt.payment_type = opt.payment_type 
  WHERE vopt.payment_type != '12475' 
    AND opt.payment_type IS NULL) ;
    
    
    
  -- 插入订单验证表:协同产品技术服务费在协同系统存在的,计费系统中不存在情况
  INSERT INTO order_validation_info (
  serial_no,
    hs_contract_id,
    order_source,
    hs_product_id,
    reason,
    belong_type,
    hs_check_status,
    check_type,
    check_column
  ) 
  (SELECT DISTINCT 
  REPLACE(UUID(), '-', ''),
    vopt.hs_contract_id,
    vopt.order_source,
    vopt.product_sale_id,
    CONCAT(
      '产品:',
      IFNULL(vopt.product_sale_id, ''),
      '费用类型:技术服务费在协同系统存在,计费系统不存在'
    ) reason,
    '3',
    '1',
    '3',
    vopt.payment_type 
  FROM
    (SELECT 
      TRIM(vcj.contractid) AS hs_contract_id,
      TRIM(vcj.companyid) AS order_source,
      TRIM(vcdj.salprd_id) AS salprd_id,
      vpj.productid AS product_sale_id,
      TRIM(vcdj.chargetype) AS payment_type 
    FROM
      v_contract_jf vcj,
      v_contract_detail_jf vcdj 
      LEFT JOIN v_prdsale_jf vpj 
        ON vpj.prdid = vcdj.salprd_id 
    WHERE vcj.con_id = vcdj.con_id 
      AND TRIM(vcdj.chargetype) = '12475' 
      AND (
        TRIM(vcj.contractid) = param_hs_contract_id 
        OR param_hs_contract_id IS NULL
      ) 
      AND EXISTS 
      (SELECT 
        contract_id 
      FROM
        order_info 
      WHERE contract_id = TRIM(vcj.contractid)
       -- 删除标志为0
       AND del_flag=0 ) 
      AND EXISTS 
      (SELECT 
        oi.contract_id,
        op.ex_product_id
      FROM
        order_product op,
        order_info oi 
      WHERE oi.contract_id = op.contract_id
       -- 删除标志为0
       AND oi.del_flag=0 AND op.del_flag=0
        AND oi.contract_id = TRIM(vcj.contractid) 
        AND op.ex_product_id = TRIM(vcdj.salprd_id))) vopt 
    LEFT JOIN 
      (SELECT 
        oi.contract_id,
        op.combine_id,
        op.product_id,
        op.ex_product_id,
        '12475' payment_type 
      FROM
        order_product op,
        order_combine oc,
        order_info oi 
      WHERE (
          oi.contract_id = param_hs_contract_id 
          OR param_hs_contract_id IS NULL
        ) 
         -- 删除标志为0
       AND oi.del_flag=0 AND op.del_flag=0
       AND oc.del_flag=0 
       
        AND oi.contract_id = op.contract_id
        AND oc.contract_id = oi.contract_id
        AND op.combine_id = oc.id) opt 
      ON vopt.hs_contract_id = opt.contract_id 
      AND vopt.salprd_id = opt.ex_product_id 
      AND vopt.payment_type = opt.payment_type 
  WHERE opt.payment_type IS NULL) ;
  
  
  
  -- 插入订单验证表:协同产品费用类型收入时间与计费系统中不一致情况
  INSERT INTO order_validation_info (
  serial_no,
    hs_contract_id,
    order_source,
    hs_product_id,
    reason,
    belong_type,
    hs_check_status,
    check_type,
    check_column
  ) 
  (SELECT 
  REPLACE(UUID(), '-', ''),
    opt.hs_contract_id,
    opt.order_source,
    opt.product_sale_id,
    CONCAT(
      '产品:',
      IFNULL(opt.product_sale_id, ''),
      '费用类型:',
      CASE
        opt.payment_type 
        WHEN '12471' 
        THEN '一次性费用' 
        WHEN '12473' 
        THEN '年费' 
        WHEN '12472' 
        THEN '不定期' 
        WHEN '12474' 
        THEN '条件延后' 
        WHEN '12475' 
        THEN '技术服务费' 
        ELSE '' 
      END,
      '的收入开始日期:',
      IFNULL(
        DATE_FORMAT(opt.begin_date, '%Y-%m-%d'),
        ''
      ),
      ',结束日期:',
      IFNULL(
        DATE_FORMAT(opt.end_date, '%Y-%m-%d'),
        ''
      ),
      '与计费系统中不一致'
    ) reason,
    '3',
    '2',
    '4',
    opt.payment_type 
  FROM
    (SELECT 
      TRIM(vcj.contractid) AS hs_contract_id,
      TRIM(vcj.companyid) AS order_source,
      TRIM(vcdj.salprd_id) AS salprd_id,
      vpj.productid AS product_sale_id,
      TRIM(vcdj.chargetype) AS payment_type,
      TRIM(vcdj.servicestartdate) AS begin_date,
      TRIM(vcdj.serviceenddate) AS end_date 
    FROM
      v_contract_jf vcj,
      v_contract_detail_jf vcdj 
      LEFT JOIN v_prdsale_jf vpj 
        ON vpj.prdid = vcdj.salprd_id 
    WHERE vcj.con_id = vcdj.con_id 
      AND vcdj.chargetype IS NOT NULL 
      AND vcdj.chargetype != '' 
      AND (
        TRIM(vcj.contractid) = param_hs_contract_id 
        OR param_hs_contract_id IS NULL
      ) 
      AND EXISTS 
      (SELECT 
        contract_id 
      FROM
        order_info 
      WHERE contract_id = TRIM(vcj.contractid)
       -- 删除标志为0
       AND del_flag=0
      ) 
      AND EXISTS 
      (SELECT 
        oi.contract_id,
        op.ex_product_id 
      FROM
        order_product op,
        order_info oi 
      WHERE oi.contract_id = op.contract_id 
       -- 删除标志为0
       AND oi.del_flag=0 AND op.del_flag=0
       
        AND oi.contract_id = TRIM(vcj.contractid) 
        AND op.ex_product_id = TRIM(vcdj.salprd_id))) opt 
    LEFT JOIN 
      (SELECT 
        oi.contract_id,
        ois.ex_product_id,
        ois.payment_type,
        DATE_FORMAT(
          ois.income_begin_date,
          '%Y-%m-%d'
        ) AS begin_date,
        DATE_FORMAT(ois.income_end_date, '%Y-%m-%d') AS end_date 
      FROM
        order_income_setting ois,
        order_info oi 
      WHERE (
          oi.contract_id = param_hs_contract_id 
          OR param_hs_contract_id IS NULL
        ) 
         -- 删除标志为0
       AND oi.del_flag=0 AND ois.del_flag=0
       
        AND ois.contract_id = oi.contract_id
        AND ois.payment_type != '10000') vopt 
      ON vopt.contract_id = opt.hs_contract_id 
      AND vopt.ex_product_id = opt.salprd_id 
      AND vopt.payment_type = opt.payment_type 
      AND opt.begin_date = vopt.begin_date 
      AND opt.end_date = vopt.end_date 
  WHERE vopt.payment_type IS NULL) ;
  
  
  
  -- 验证状态是隐藏或者有备注：验证通过则删除隐藏或者有备注的校验，验证不通过则删除新增的
  DELETE 
    rno.* 
  FROM
    order_validation_info rno,
    (SELECT 
      ovi2.serial_no,
      ovi1.serial_no AS serial_no0 
    FROM
      order_validation_info ovi2 
      LEFT JOIN order_validation_info ovi1 
        ON ovi1.hs_contract_id = ovi2.hs_contract_id 
        AND ovi1.reason = ovi2.reason 
        AND ovi1.show_status = '0' 
        AND (
          ovi1.remark IS NULL 
          OR ovi1.remark = ''
        ) 
    WHERE (
        ovi2.hs_contract_id = param_hs_contract_id 
        OR param_hs_contract_id IS NULL
      ) 
      AND (
        ovi2.show_status = '1' 
        OR (
          ovi2.remark IS NOT NULL 
          AND ovi2.remark != ''
        )
      ) 
      AND ovi2.order_id = '') U 
  WHERE (
      U.serial_no0 IS NULL 
      AND rno.serial_no = U.serial_no
    ) 
    OR (
      U.serial_no0 IS NOT NULL 
      AND rno.serial_no = U.serial_no0
    ) ;
    
    
    
  -- 费用类型不存在，则删除此费用类型下收入时间校验(计费比较协同)
  DELETE 
    ovi2.* 
  FROM
    order_validation_info ovi2,
    order_validation_info ovi1 
  WHERE ovi1.hs_contract_id = ovi2.hs_contract_id 
    AND ovi1.hs_product_id = ovi2.hs_product_id 
    AND ovi1.check_type = '1' 
    AND ovi2.check_type = '2' 
    AND ovi1.check_column = ovi2.check_column 
    AND (
      ovi2.hs_contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) ;
    
    
  -- 费用类型不存在，则删除此费用类型下收入时间校验（协同比较计费）
  DELETE 
    ovi2.* 
  FROM
    order_validation_info ovi2,
    order_validation_info ovi1 
  WHERE ovi1.hs_contract_id = ovi2.hs_contract_id 
    AND ovi1.hs_product_id = ovi2.hs_product_id 
    AND ovi1.check_type = '3' 
    AND ovi2.check_type = '4' 
    AND ovi1.check_column = ovi2.check_column 
    AND (
      ovi2.hs_contract_id = param_hs_contract_id 
      OR param_hs_contract_id IS NULL
    ) ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_count_yearly_contract_income` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_count_yearly_contract_income` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_count_yearly_contract_income`(
  param_until_month VARCHAR (20),
  param_contract_id VARCHAR (40)
)
BEGIN
  -- 合同编号（用于提高性能）
  DECLARE v_contract_id VARCHAR (60) ;
  -- 统计年月
  DECLARE v_current_month VARCHAR (10) ;
  DECLARE done INT DEFAULT 0 ;
  DECLARE month_cur CURSOR FOR 
  SELECT 
    vm.months,
    ro.contract_id 
  FROM
    v_month vm,
    order_info ro 
  WHERE vm.months <= param_until_month 
    AND ro.contract_id = param_contract_id 
    AND ro.del_flag = '0'
  ORDER BY contract_id,
    months ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  -- 删除导出年度的数据
  DELETE 
  FROM
    charge_monthly_contract_income 
  WHERE contract_id = param_contract_id ;
  OPEN month_cur ;
  cursor_loop :
  LOOP
    FETCH month_cur INTO v_current_month,
    v_contract_id ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    INSERT INTO charge_monthly_contract_income (
      contract_id,
      charge_month,
      income,
      service_charge,
      receiveable,
      payable,
      change_income,
      change_service_charge
    ) 
    SELECT 
      v_contract_id,
      v_current_month,
      SUM(change_income),
      SUM(change_service_charge),
      (SELECT 
        SUM(bankreceipt_amount) 
      FROM
        charge_bankreceipt 
      WHERE contract_id = v_contract_id 
        AND DATE_FORMAT(bankreceipt_month, '%Y%m') = v_current_month) AS receiveable,
      (SELECT 
        IFNULL(ero.payable, 0) - IFNULL(
          (SELECT 
            SUM(IFNULL(bankreceipt_amount, 0)) 
          FROM
            charge_bankreceipt 
          WHERE contract_id = v_contract_id 
            AND DATE_FORMAT(bankreceipt_month, '%Y%m') > DATE_FORMAT(ero.bill_display_date, '%Y%m') 
            AND DATE_FORMAT(bankreceipt_month, '%Y%m') <= v_current_month),
          0
        ) 
      FROM
        charge_order_info ero 
      WHERE ero.bill_id =
        (SELECT 
          MAX(bill_id) 
        FROM
          charge_order_info 
        WHERE 1 = 1 
          AND contract_id = v_contract_id 
          AND DATE_FORMAT(bill_display_date, '%Y%m') <= v_current_month
        GROUP BY contract_id)) AS payable,
      (SELECT 
        SUM(change_income) 
      FROM
        charge_monthly_product_income 
      WHERE contract_id = v_contract_id 
        AND charge_month = v_current_month) AS change_income,
      (SELECT 
        SUM(change_service_charge) 
      FROM
        charge_monthly_product_income 
      WHERE contract_id = v_contract_id 
        AND charge_month = v_current_month) AS change_service_charge 
    FROM
      charge_monthly_product_income 
    WHERE contract_id = v_contract_id 
      AND charge_month <= v_current_month ;
  END LOOP cursor_loop ;
  CLOSE month_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_count_yearly_product_income` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_count_yearly_product_income` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_count_yearly_product_income`(
  param_until_month VARCHAR (20),
  param_contract_id VARCHAR (40)
)
BEGIN
  -- 合同编号（用于提高性能）
  DECLARE v_contract_id,
  v_combine_id,
  v_product_id,
  v_last_contract_id,
  v_last_combine_id,
  v_last_product_id VARCHAR (60) ;
  -- 统计年月
  DECLARE v_current_month VARCHAR (10) ;
  DECLARE v_last_month_income,
  v_last_month_service_charge DECIMAL (25, 8) ;
  DECLARE done INT DEFAULT 0 ;
  DECLARE month_cur CURSOR FOR 
  SELECT DISTINCT 
    vm.months,
    rt.contract_id,
    rt.combine_id,
    rt.product_id 
  FROM
    v_month vm,
    order_product rt 
  WHERE vm.months <= param_until_month 
    AND contract_id = param_contract_id 
    AND rt.del_flag = '0' 
  ORDER BY rt.contract_id,
    rt.combine_id,
    rt.product_id,
    months ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  -- 删除导出年度的数据
  DELETE 
  FROM
    charge_monthly_product_income 
  WHERE contract_id = param_contract_id ;
  OPEN month_cur ;
  cursor_loop :
  LOOP
    FETCH month_cur INTO v_current_month,
    v_contract_id,
    v_combine_id,
    v_product_id ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    INSERT INTO charge_monthly_product_income (
      contract_id,
      charge_month,
      income,
      service_charge,
      combine_id,
      product_id
    ) 
    SELECT 
      v_contract_id,
      v_current_month,
      SUM(V.income) AS income,
      SUM(
        CASE
          WHEN V.payment_type = '12475' 
          THEN (V.income) 
          ELSE 0 
        END
      ) AS service_charge,
      v_combine_id,
      v_product_id 
    FROM
      (SELECT 
        elp.income,
        elp.payment_type 
      FROM
        charge_detail_temp elp 
      WHERE elp.contract_id = param_contract_id 
        AND elp.combine_id = v_combine_id 
        AND elp.product_id = v_product_id 
        AND DATE_FORMAT(elp.charge_end_date, '%Y%m') = v_current_month 
      GROUP BY elp.payment_type,
        elp.income_begin_date) V ;
    -- 计算累计收入、应付、技术服务费增量
    UPDATE 
      charge_monthly_product_income 
    SET
      change_income = 
      CASE
        WHEN contract_id = v_last_contract_id 
        AND combine_id = v_last_combine_id 
        AND product_id = v_last_product_id 
        THEN income - IFNULL(v_last_month_income, 0) 
        ELSE income 
      END,
      change_service_charge = 
      CASE
        WHEN contract_id = v_last_contract_id 
        AND combine_id = v_last_combine_id 
        AND product_id = v_last_product_id 
        THEN service_charge - IFNULL(v_last_month_service_charge, 0) 
        ELSE service_charge 
      END 
    WHERE contract_id = v_contract_id 
      AND combine_id = v_combine_id 
      AND product_id = v_product_id 
      AND charge_month = v_current_month ;
    -- 获取上月的累计收入、应付、技术服务费，为计算增量准备
    SELECT 
      contract_id,
      combine_id,
      product_id,
      income,
      service_charge INTO v_last_contract_id,
      v_last_combine_id,
      v_last_product_id,
      v_last_month_income,
      v_last_month_service_charge 
    FROM
      charge_monthly_product_income 
    WHERE contract_id = v_contract_id 
      AND combine_id = v_combine_id 
      AND product_id = v_product_id 
      AND charge_month = v_current_month ;
  END LOOP cursor_loop ;
  CLOSE month_cur ;
  -- 删除空值
  DELETE 
  FROM
    charge_monthly_product_income 
  WHERE income IS NULL ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_db_mysql` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_db_mysql` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_db_mysql`()
BEGIN
  DECLARE v_rowcount INT ;
  SELECT 
    COUNT(1) INTO v_rowcount 
  FROM
    information_schema.STATISTICS 
  WHERE table_name = 'v_contract_jf' 
    AND index_name = 'idx_contractif_id' ;
  IF v_rowcount = 0 
  THEN ALTER TABLE v_contract_jf 
    ADD INDEX `idx_contractif_id` (
      `contractid` (255),
      `customerid` (255)
    ) USING HASH ;
  END IF ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_delete_bill` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_delete_bill` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_delete_bill`(param_contract_id VARCHAR (20))
BEGIN
  DELETE 
  FROM
    charge_period_temp 
  WHERE LEFT(bill_id, LOCATE('-', bill_id) - 1) = param_contract_id ;
  DELETE 
  FROM
    charge_wrong 
  WHERE contract_id = param_contract_id ;
  DELETE 
  FROM
    ss_order_info 
  WHERE LEFT(bill_id, LOCATE('-', bill_id) - 1) = param_contract_id ;
  DELETE 
  FROM
    ss_order_combine 
  WHERE LEFT(bill_id, LOCATE('-', bill_id) - 1) = param_contract_id ;
  DELETE 
  FROM
    ss_order_product 
  WHERE LEFT(bill_id, LOCATE('-', bill_id) - 1) = param_contract_id ;
  DELETE 
  FROM
    ss_order_model 
  WHERE LEFT(bill_id, LOCATE('-', bill_id) - 1) = param_contract_id ;
  DELETE 
  FROM
    ss_order_price 
  WHERE LEFT(bill_id, LOCATE('-', bill_id) - 1) = param_contract_id ;
  DELETE 
  FROM
    charge_bill_info 
  WHERE LEFT(bill_id, LOCATE('-', bill_id) - 1) = param_contract_id ;
  DELETE 
  FROM
    charge_trans_detail 
  WHERE LEFT(bill_id, LOCATE('-', bill_id) - 1) = param_contract_id ;
  DELETE 
  FROM
    charge_receipt 
  WHERE LEFT(bill_id, LOCATE('-', bill_id) - 1) = param_contract_id ;
  DELETE 
  FROM
    charge_order_info 
  WHERE LEFT(bill_id, LOCATE('-', bill_id) - 1) = param_contract_id ;
  DELETE 
  FROM
    ss_order_adv_payment 
  WHERE LEFT(bill_id, LOCATE('-', bill_id) - 1) = param_contract_id ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_delete_order` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_delete_order` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_delete_order`(param_contract_id VARCHAR (40))
BEGIN
  DELETE 
  FROM
    order_info 
  WHERE contract_id = param_contract_id ;
  DELETE 
  FROM
    order_model 
  WHERE ref_id IN 
    (SELECT 
      id 
    FROM
      order_combine 
    WHERE contract_id = param_contract_id) ;
  DELETE 
  FROM
    order_combine 
  WHERE contract_id = param_contract_id ;
  DELETE 
  FROM
    order_model 
  WHERE ref_id IN 
    (SELECT 
      id 
    FROM
      order_product 
    WHERE contract_id = param_contract_id) ;
  DELETE 
  FROM
    order_product 
  WHERE contract_id = param_contract_id ;
  DELETE 
  FROM
    order_model 
  WHERE ref_id = param_contract_id ;
  DELETE 
  FROM
    order_adv_payment 
  WHERE contract_id = param_contract_id ;
  DELETE 
  FROM
    order_price 
  WHERE contract_id = param_contract_id ;
  DELETE 
  FROM
    order_income_setting 
  WHERE contract_id = param_contract_id ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_finance_detail` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_finance_detail` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_finance_detail`(param_contract_id VARCHAR (20))
BEGIN
  DELETE 
  FROM
    charge_detail_temp 
  WHERE contract_id = param_contract_id ;
  INSERT INTO charge_detail_temp (
    office_id,
    con_id,
    contract_id,
    product_id,
    product_name,
    charge_begin_date,
    charge_end_date,
    payment_type,
    income,
    detailid,
    income_begin_date,
    income_end_date,
    split_amount,
    combine_id
  ) 
  SELECT 
    V.office_id,
    V.con_id,
    V.contract_id,
    V.product_id,
    V.product_name,
    V.charge_begin_date,
    V.charge_end_date,
    V.payment_type,
    ROUND(
      IFNULL(
        CASE
          WHEN V.charge_end_date = DATE_FORMAT(V.income_end_date, '%Y-%m-%d') 
          AND V.split_amount IS NOT NULL 
          AND V.split_amount != '0' 
          THEN V.split_amount 
          ELSE V.finance_current_income 
        END,
        0
      ),
      2
    ) AS income,
    vtlf.detailid,
    V.income_begin_date,
    V.income_end_date,
    V.split_amount,
    V.combine_id 
  FROM
    (SELECT 
      U.office_id,
      U.con_id,
      U.contract_id,
      U.customer_id,
      U.user_name,
      U.product_id,
      U.ex_product_id,
      U.product_name,
      DATE_FORMAT(U.charge_begin_date, '%Y-%m-%d') AS charge_begin_date,
      DATE_FORMAT(U.charge_end_date, '%Y-%m-%d') AS charge_end_date,
      U.payment_type,
      U.display_name,
      ROUND(
        (
          GREATEST(
            (
              CASE
                WHEN U.min_type = '1' 
                THEN U.min_consume * fun_get_date_diff (
                  U.charge_end_date,
                  yearly_begin_date
                ) / fun_get_date_diff (
                  fun_get_yearly_end_date (yearly_begin_date),
                  yearly_begin_date
                ) + U.min_consume * (
                  FLOOR(
                    fun_get_date_diff (
                      U.charge_begin_date,
                      U.income_begin_date
                    ) / 365
                  )
                ) 
                ELSE U.current_min_income * fun_get_date_diff (
                  U.charge_end_date,
                  U.income_begin_date
                ) / fun_get_date_diff (
                  U.income_end_date,
                  U.income_begin_date
                ) 
              END
            ),
            U.total_service_charge
          )
        ) * U.split_ratio / 100,
        2
      ) AS finance_current_income,
      U.income_begin_date,
      U.income_end_date,
      U.split_amount,
      U.combine_id 
    FROM
      (SELECT 
        ro.office_id,
        vtf.con_id,
        elo.contract_id,
        ro.customer_id,
        vrf.chinesename AS user_name,
        reg.product_id,
        srt.product_name,
        elo.charge_begin_date,
        elo.charge_end_date,
        '12475' AS payment_type,
        '技术服务费' AS display_name,
        (
          CASE
            WHEN elo.min_type = '1' 
            THEN FLOOR(
              fun_get_date_diff (
                reg.income_end_date,
                reg.income_begin_date
              ) / 365
            ) * IFNULL(srl.min_consume, 0) 
            ELSE IFNULL(srl.min_consume, 0) 
          END
        ) AS current_min_income,
        IFNULL(elo.total_service_charge, 0) AS total_service_charge,
        reg.split_ratio,
        reg.income_begin_date,
        reg.income_end_date,
        reg.split_amount,
        reg.ex_product_id,
        elo.combine_id,
        srl.min_consume,
        elo.min_type,
        fun_get_yearly_begin_date (
          elo.charge_begin_date,
          reg.income_begin_date
        ) AS yearly_begin_date 
      FROM
        charge_bill_info elo,
        order_income_setting reg,
        ss_order_product srt,
        ss_order_model srl,
        v_contract_jf vtf,
        order_info ro 
        LEFT JOIN v_customer_jf vrf 
          ON vrf.customerid = ro.customer_id 
      WHERE elo.belong_type = '2' 
        AND reg.contract_id = elo.contract_id 
        AND reg.payment_type = '12475' 
        AND elo.charge_end_date <= elo.prod_end_date 
        AND elo.charge_begin_date >= elo.prod_begin_date 
        AND elo.prod_begin_date = DATE_FORMAT(
          reg.income_begin_date,
          '%Y-%m-%d'
        ) 
        AND elo.prod_end_date = DATE_FORMAT(reg.income_end_date, '%Y-%m-%d') 
        AND srt.bill_id = elo.bill_id 
        AND srt.contract_id = elo.contract_id 
        AND srt.combine_id = elo.combine_id 
        AND srt.product_id = reg.product_id 
        AND srl.bill_id = elo.bill_id 
        AND srl.ref_id = srt.combine_id 
        AND srl.belong_type = '2' 
        AND vtf.contractid = elo.contract_id 
        AND ro.contract_id = elo.contract_id 
        AND srl.del_flag = '0' 
        AND srt.del_flag = '0' 
        AND ro.del_flag = '0' 
        AND ro.contract_id = param_contract_id 
      GROUP BY elo.charge_begin_date,
        elo.charge_end_date,
        elo.contract_id,
        elo.combine_id,
        reg.product_id) U 
    UNION
    ALL 
    SELECT DISTINCT 
      ro.office_id,
      vtf.con_id,
      ro.contract_id,
      ro.customer_id,
      vrf.chinesename AS user_name,
      re.product_id,
      re.ex_product_id,
      re.product_name,
      DATE_FORMAT(
        elo.charge_begin_date,
        '%Y-%m-%d'
      ) AS charge_begin_date,
      DATE_FORMAT(elo.charge_end_date, '%Y-%m-%d') AS charge_end_date,
      re.payment_type,
      re.display_name,
      ROUND(
        IFNULL(
          CASE
            WHEN DATE_FORMAT(elo.charge_end_date, '%Y-%m') >= DATE_FORMAT(ois.income_end_date, '%Y-%m') 
            AND ois.split_amount IS NOT NULL 
            AND ois.split_amount != '0' 
            THEN ois.split_amount 
            WHEN fun_get_date_diff (
              elo.charge_end_date,
              ois.income_begin_date
            ) < 0 
            THEN 0 
            WHEN re.payment_type IN ('12471', '12474') 
            AND ois.split_amount IS NOT NULL 
            THEN ois.split_amount 
            WHEN re.payment_type IN ('12471', '12474') 
            AND ois.income_end_date >= elo.charge_end_date 
            AND elo.charge_end_date >= ois.income_begin_date 
            THEN re.amount * ois.split_ratio / 100 
            WHEN re.payment_type IN ('12472', '12473') 
            AND ois.income_end_date >= elo.charge_end_date 
            THEN (re.amount * ois.split_ratio / 100) * fun_get_date_diff (
              elo.charge_end_date,
              ois.income_begin_date
            ) / fun_get_date_diff (
              ois.income_end_date,
              ois.income_begin_date
            ) 
            ELSE re.amount * ois.split_ratio / 100 
          END,
          0
        ),
        2
      ) AS finance_current_income,
      ois.income_begin_date,
      ois.income_end_date,
      ois.split_amount,
      re.combine_id 
    FROM
      (SELECT 
        op.contract_id,
        op.combine_id,
        rt.prod_begin_date,
        rt.prod_end_date,
        op.payment_type,
        rt.product_name,
        CASE
          WHEN payment_type = '12471' 
          THEN '一次性费用' 
          WHEN payment_type = '12472' 
          THEN '不定期费用' 
          WHEN payment_type = '12473' 
          THEN '年费' 
          WHEN payment_type = '12474' 
          THEN '条件延后' 
          ELSE '' 
        END display_name,
        rt.product_id,
        rt.ex_product_id,
        SUM(amount) AS amount,
        rt.id 
      FROM
        order_adv_payment op 
        LEFT JOIN 
          (SELECT 
            t.contract_id,
            t.combine_id,
            t.product_name,
            t.product_id,
            t.ex_product_id,
            t.prod_begin_date,
            t.prod_end_date,
            t.id 
          FROM
            order_product t 
          WHERE t.del_flag = '0' 
          GROUP BY t.contract_id,
            t.combine_id,
            t.product_id) rt 
          ON rt.contract_id = op.contract_id 
          AND rt.combine_id = op.combine_id 
      WHERE op.payment_type IN ('12471', '12472', '12473', '12474') 
        AND op.del_flag = '0' 
      GROUP BY op.contract_id,
        op.combine_id,
        op.payment_type,
        rt.product_id) re 
      LEFT JOIN order_income_setting ois 
        ON ois.contract_id = re.contract_id 
        AND ois.payment_type = re.payment_type 
        AND ois.product_id = re.product_id 
        AND (
          CASE
            WHEN ois.payment_type IN ('12473', '12472') 
            THEN DATE_FORMAT(
              ois.income_begin_date,
              '%Y-%m-%d'
            ) = re.prod_begin_date 
            AND DATE_FORMAT(ois.income_end_date, '%Y-%m-%d') = re.prod_end_date 
            WHEN ois.payment_type IN ('12471', '12474') 
            THEN DATE_FORMAT(ois.income_end_date, '%Y-%m-%d') >= re.prod_begin_date 
            AND DATE_FORMAT(
              ois.income_begin_date,
              '%Y-%m-%d'
            ) <= re.prod_end_date 
          END
        ),
      charge_bill_info elo,
      order_info ro 
      LEFT JOIN v_customer_jf vrf 
        ON vrf.customerid = ro.customer_id,
      v_contract_jf vtf 
    WHERE elo.belong_type = '3' 
      AND elo.contract_id = re.contract_id 
      AND elo.combine_id = re.combine_id 
      AND elo.product_id = re.id 
      AND (
        CASE
          WHEN ois.payment_type IN ('12473', '12472') 
          THEN elo.charge_begin_date >= DATE_FORMAT(
            ois.income_begin_date,
            '%Y-%m-%d'
          ) 
          AND elo.charge_end_date <= DATE_FORMAT(ois.income_end_date, '%Y-%m-%d') 
          WHEN ois.payment_type IN ('12471', '12474') 
          THEN elo.charge_end_date >= DATE_FORMAT(
            ois.income_begin_date,
            '%Y-%m-%d'
          ) 
        END
      ) 
      AND ro.contract_id = re.contract_id 
      AND vtf.contractid = ro.contract_id 
      AND ro.del_flag = '0' 
      AND ro.contract_id = param_contract_id) V 
    LEFT JOIN 
      (SELECT 
        * 
      FROM
        v_contract_detail_jf 
      GROUP BY con_id,
        salprd_id,
        chargetype,
        servicestartdate,
        serviceenddate) vtlf 
      ON V.con_id = vtlf.con_id 
      AND V.ex_product_id = vtlf.salprd_id 
      AND V.payment_type = vtlf.chargetype 
      AND DATE_FORMAT(V.income_begin_date, '%Y-%m-%d') = vtlf.servicestartdate 
      AND DATE_FORMAT(V.income_end_date, '%Y-%m-%d') = vtlf.serviceenddate 
  WHERE V.contract_id = param_contract_id ;
  -- 更新增量
  UPDATE 
    charge_detail_temp U,
    (SELECT 
      em.*,
      em.income - 
      (SELECT 
        IFNULL(MAX(income), 0) AS income 
      FROM
        charge_detail_temp 
      WHERE charge_begin_date < em.charge_begin_date 
        AND contract_id = em.contract_id 
        AND product_id = em.product_id 
        AND payment_type = em.payment_type 
        AND combine_id = em.combine_id 
        AND income_begin_date = em.income_begin_date 
        AND income_end_date = em.income_end_date) AS change_income_temp 
    FROM
      charge_detail_temp em 
    WHERE (
        param_contract_id IS NULL 
        OR em.contract_id = param_contract_id
      )) V 
  SET
    U.change_income = V.change_income_temp 
  WHERE U.charge_begin_date = V.charge_begin_date 
    AND U.contract_id = V.contract_id 
    AND U.product_id = V.product_id 
    AND U.payment_type = V.payment_type 
    AND IFNULL(U.detailid, '') = IFNULL(V.detailid, '') 
    AND U.combine_id = V.combine_id 
    AND U.income_begin_date = V.income_begin_date 
    AND (
      param_contract_id IS NULL 
      OR U.contract_id = param_contract_id
    ) ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_finance_income_interface` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_finance_income_interface` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_finance_income_interface`(
  param_contract_id VARCHAR (20),
  param_batch_no VARCHAR (20),
  param_dept VARCHAR (20)
)
BEGIN
  IF param_contract_id = '' 
  THEN SET param_contract_id = NULL ;
  END IF ;
  IF param_dept = '' 
  THEN SET param_dept = NULL ;
  END IF ;
  INSERT INTO charge_income_interface (
    office_id,
    con_id,
    contract_id,
    ex_product_id,
    charge_begin_date,
    charge_end_date,
    payment_type,
    total_finance_income,
    change_income,
    detailid,
    income_begin_date,
    income_end_date,
    update_flag,
    batch_no,
    product_id
  ) 
  SELECT 
    elp.office_id,
    con_id,
    elp.contract_id,
    vef.prdid,
    charge_begin_date,
    charge_end_date,
    payment_type,
    SUM(IFNULL(income, 0)) income,
    SUM(IFNULL(change_income, 0)) change_income,
    detailid,
    income_begin_date,
    income_end_date,
    '0',
    param_batch_no,
    product_id 
  FROM
    charge_detail_temp elp,
    v_prdsale_jf vef,
    order_info ro 
  WHERE elp.product_id = vef.productid 
    AND ro.contract_id = elp.contract_id 
    AND ro.income_source = 'charge' 
    AND (
      param_contract_id IS NULL 
      OR elp.contract_id = param_contract_id
    ) 
    AND (
      param_dept IS NULL 
      OR elp.office_id = param_dept
    ) 
  GROUP BY elp.contract_id,
    elp.product_id,
    elp.payment_type,
    elp.charge_begin_date,
    elp.charge_end_date ;
  DELETE 
  FROM
    charge_income_interface 
  WHERE detailid IS NULL ;
  DELETE 
    a.* 
  FROM
    charge_income_interface a,
    charge_income_interface b 
  WHERE b.update_flag = '1' 
    AND b.con_id = a.con_id 
    AND b.ex_product_id = a.ex_product_id 
    AND b.charge_begin_date = a.charge_begin_date 
    AND b.charge_end_date = a.charge_end_date 
    AND b.income_begin_date = a.income_begin_date 
    AND b.income_end_date = a.income_end_date 
    AND b.batch_no > a.batch_no ;
  DELETE 
    a.* 
  FROM
    charge_income_interface a,
    charge_income_interface b 
  WHERE b.update_flag = '1' 
    AND b.con_id = a.con_id 
    AND b.ex_product_id = a.ex_product_id 
    AND b.charge_begin_date = a.charge_begin_date 
    AND b.charge_end_date = a.charge_end_date 
    AND b.income_begin_date = a.income_begin_date 
    AND b.income_end_date = a.income_end_date 
    AND b.total_finance_income = a.total_finance_income 
    AND a.batch_no = param_batch_no 
    AND (
      param_contract_id IS NULL 
      OR a.contract_id = param_contract_id
    ) ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_gen_charge_fee` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_gen_charge_fee` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_gen_charge_fee`(param_batch_no VARCHAR (20))
BEGIN
  -- 计算计费期间的 产品 计费金额与技术服务费
  -- 生成详单级别（产品，计费类型，日期）账单计费信息
  INSERT INTO charge_receipt (
    bill_id,
    batch_no,
    charge_begin_date,
    combine_id,
    product_id,
    feemodel_id,
    contract_id,
    charge_amt,
    service_charge,
    org_amt,
    fee_ratio
  ) 
  SELECT 
    U.bill_id,
    U.batch_no,
    U.charge_begin_date,
    U.combine_id,
    U.product_id,
    U.id,
    U.contract_id,
    U.charge_amt,
    -- 如果存在日封顶，进行处理，生成技术服务费
    -- 详单级别（产品/日）的技术服务费计算，目前只需要考虑日封顶。
    -- 不需要考虑技术服务费/年费的分摊逻辑。
    fun_charge_daily_max (
      -- 如果存在日保底，进行处理
      fun_charge_daily_min (
        U.charge_amt,
        srl.min_type,
        srl.min_consume
      ),
      srl.max_type,
      srl.max_consume
    ),
    U.data1,
    -- 普通阶梯的费率
    U.fee_retio 
  FROM
    (SELECT 
      edp.bill_id,
      edp.batch_no,
      CASE
        WHEN srl.fix_charge_type = 2 
        THEN edp.charge_end_date 
        ELSE DATE_FORMAT(eye.oc_date, '%Y-%m-%d') 
      END charge_begin_date,
      (SELECT 
        srt.combine_id 
      FROM
        ss_order_product srt 
      WHERE srt.bill_id = edp.bill_id 
        AND srt.id = edp.id 
        AND srt.del_flag = '0') combine_id,
      edp.id AS product_id,
      -- 根据计费公式对每天的业务数据进行计费计算
      (
        CASE
          WHEN srl.fix_charge_type = 1 
          THEN fun_charge_by_model (
            -- 这里只对业务接口传来的第一个值进行了处理
            eye.data1,
            edp.bill_id,
            srl.id,
            srl.fee_formula
          ) 
          WHEN eye.data1 IS NULL 
          THEN -- 月结的情况下，取月末最后一天计费
          (SELECT 
            eye.data1 
          FROM
            charge_unify_interface eye 
          WHERE eye.contract_id = edp.contract_id 
            AND eye.product_id = edp.product_id 
            AND eye.fee_type = edp.fee_type 
            AND DATE_FORMAT(eye.oc_date, '%Y%m') = DATE_FORMAT(edp.charge_begin_date, '%Y%m') 
            AND eye.batch_no = edp.batch_no 
          ORDER BY eye.oc_date DESC 
          LIMIT 0, 1) 
          ELSE eye.data1 
        END
      ) AS charge_amt,
      CASE
        WHEN eye.data1 IS NULL 
        THEN -- 月结的情况下，取月末最后一天计费
        (SELECT 
          eye.data1 
        FROM
          charge_unify_interface eye 
        WHERE eye.contract_id = edp.contract_id 
          AND eye.product_id = edp.product_id 
          AND eye.fee_type = edp.fee_type 
          AND DATE_FORMAT(eye.oc_date, '%Y%m') = DATE_FORMAT(edp.charge_begin_date, '%Y%m') 
          AND eye.batch_no = edp.batch_no 
        ORDER BY eye.oc_date DESC 
        LIMIT 0, 1) 
        ELSE eye.data1 
      END data1,
      -- 计算产品的费率
      fun_charge_get_fee_retio (
        eye.data1,
        edp.bill_id,
        srl.id,
        srl.fee_formula
      ) fee_retio,
      srl.id,
      edp.contract_id 
    FROM
      charge_period_temp edp 
      LEFT JOIN 
        (SELECT 
          batch_no,
          contract_id,
          product_id,
          fee_type,
          oc_date,
          SUM(data1) AS data1 
        FROM
          charge_unify_interface 
        WHERE batch_no = param_batch_no 
        GROUP BY batch_no,
          contract_id,
          product_id,
          fee_type,
          oc_date) eye 
        ON eye.batch_no = edp.batch_no 
        AND eye.contract_id = edp.contract_id 
        AND eye.product_id = edp.product_id 
        AND eye.fee_type = edp.fee_type 
        AND DATE_FORMAT(eye.oc_date, '%Y-%m-%d') >= edp.charge_begin_date 
        AND DATE_FORMAT(eye.oc_date, '%Y-%m-%d') <= edp.charge_end_date 
        AND edp.prod_begin_date <= edp.charge_begin_date 
        AND edp.prod_end_date >= edp.charge_end_date 
      LEFT JOIN ss_order_model srl 
        ON srl.bill_id = edp.bill_id 
        AND srl.fee_type = edp.fee_type 
        AND srl.ref_id = edp.id 
        AND srl.belong_type = '3' 
        AND srl.del_flag = '0' 
    WHERE edp.batch_no = param_batch_no 
    GROUP BY edp.bill_id,
      edp.contract_id,
      edp.id,
      edp.fee_type,
      eye.oc_date) U 
    LEFT JOIN ss_order_model srl 
      ON srl.bill_id = U.bill_id 
      AND srl.ref_id = U.combine_id 
      AND srl.belong_type = '2' 
      AND srl.del_flag = '0' ;
  -- 根据详单生成客户交易明细
  INSERT INTO charge_trans_detail (
    bill_id,
    charge_begin_date,
    charge_end_date,
    contract_id,
    combine_id,
    product_id,
    feemodel_id,
    service_charge,
    occur_date,
    org_amt,
    fee_ratio
  ) 
  SELECT 
    et.bill_id,
    et.charge_begin_date,
    REPLACE(UUID(), '-', ''),
    et.contract_id,
    et.combine_id,
    et.product_id,
    et.feemodel_id,
    et.charge_amt,
    et.charge_begin_date,
    et.org_amt,
    et.fee_ratio 
  FROM
    charge_receipt et 
  WHERE et.batch_no = param_batch_no 
    AND et.charge_begin_date IS NOT NULL ;
  -- 同一组合有两个产品的日保底数据更新
  UPDATE 
    charge_receipt rt,
    (SELECT 
      cr.*,
      GREATEST(
        cr.sum_charge_amt,
        IFNULL(srl.min_consume, 0)
      ) AS sum_service_charge 
    FROM
      (SELECT 
        *,
        SUM(charge_amt) AS sum_charge_amt 
      FROM
        charge_receipt 
      WHERE batch_no = param_batch_no 
      GROUP BY batch_no,
        bill_id,
        combine_id,
        charge_begin_date) cr,
      ss_order_model srl 
    WHERE srl.bill_id = cr.bill_id 
      AND srl.ref_id = cr.combine_id 
      AND srl.belong_type = '2' 
      AND srl.del_flag = '0' 
      AND srl.min_type = '3') U 
  SET
    rt.service_charge = U.sum_service_charge 
  WHERE rt.batch_no = U.batch_no 
    AND rt.bill_id = U.bill_id 
    AND rt.combine_id = U.combine_id 
    AND rt.charge_begin_date = U.charge_begin_date ;
  -- 生成子账单  
  INSERT INTO charge_bill_info (
    bill_id,
    batch_no,
    charge_begin_date,
    charge_end_date,
    prod_begin_date,
    prod_end_date,
    combine_id,
    product_id,
    belong_type,
    feemodel_id,
    service_charge,
    charge_amt,
    org_amt,
    max_type,
    max_consume,
    charge_off,
    contract_id,
    fee_ratio,
    min_type,
    min_consume
  ) 
  SELECT 
    U.bill_id,
    U.batch_no,
    U.charge_begin_date,
    U.charge_end_date,
    U.prod_begin_date,
    U.prod_end_date,
    U.combine_id,
    U.product_id,
    U.belong_type,
    U.id,
    fun_get_charge_amount (
      U.fix_charge_type,
      -- 计费对象金额
      CASE
        WHEN U.fix_charge_type = 1 
        THEN U.org_amt 
        WHEN U.fix_charge_type = 2 
        THEN U.last_org_amt 
        ELSE U.org_amt 
      END,
      -- 下面的都是判断用的参数或者条件
      U.service_charge,
      U.bill_id,
      U.id,
      U.fee_formula,
      U.is_multiplied_actualdays,
      U.charge_end_date,
      U.charge_begin_date
    ) service_charge,
    fun_get_charge_amount (
      U.fix_charge_type,
      -- 计费对象金额
      CASE
        WHEN U.fix_charge_type = 1 
        THEN U.org_amt 
        WHEN U.fix_charge_type = 2 
        THEN U.last_org_amt 
        ELSE U.org_amt 
      END,
      -- 下面的都是判断用的参数或者条件
      U.charge_amt,
      U.bill_id,
      U.id,
      U.fee_formula,
      U.is_multiplied_actualdays,
      U.charge_end_date,
      U.charge_begin_date
    ) charge_amt,
    CASE
      WHEN U.fix_charge_type = 1 
      THEN U.org_amt 
      WHEN U.fix_charge_type = 2 
      THEN U.last_org_amt 
      ELSE U.org_amt 
    END,
    U.max_type,
    U.max_consume,
    U.charge_off,
    U.contract_id,
    -- 计算月结产品的费率
    fun_charge_get_fee_retio (
      U.last_org_amt,
      U.bill_id,
      U.id,
      U.fee_formula
    ) fee_retio,
    U.min_type,
    -- 保底拆分
    fun_charge_common_divid (
      U.min_type,
      U.orgi_min_consume,
      U.prod_begin_date,
      U.prod_end_date,
      U.charge_begin_date,
      U.charge_end_date
    ) min_consume 
  FROM
    (SELECT 
      edp.bill_id,
      edp.batch_no,
      MIN(edp.charge_begin_date) charge_begin_date,
      MAX(edp.charge_end_date) charge_end_date,
      edp.prod_begin_date,
      edp.prod_end_date,
      edp.contract_id,
      et.combine_id,
      et.product_id,
      '3' AS belong_type,
      srl.id,
      SUM(et.service_charge) service_charge,
      SUM(et.charge_amt) charge_amt,
      srl.min_type,
      srl.min_consume orgi_min_consume,
      SUM(et.org_amt) org_amt,
      -- 月结的情况下，取月末最后一天计费
      (SELECT 
        eye.data1 
      FROM
        charge_unify_interface eye 
      WHERE eye.contract_id = edp.contract_id 
        AND eye.product_id = edp.product_id 
        AND eye.fee_type = edp.fee_type 
        AND DATE_FORMAT(eye.oc_date, '%Y%m') = DATE_FORMAT(edp.charge_begin_date, '%Y%m') 
        AND eye.batch_no = edp.batch_no 
      ORDER BY eye.oc_date DESC 
      LIMIT 0, 1) last_org_amt,
      NULL AS fee_ratio,
      srl.max_type,
      srl.max_consume,
      -- 年度终了日
      fun_get_yearly_end_date (MIN(et.charge_begin_date)) AS yearly_end_date,
      -- 未出账
      0 AS charge_off,
      srl.fix_charge_type,
      srl.fee_formula,
      srl.is_multiplied_actualdays 
    FROM
      charge_period_temp edp 
      LEFT JOIN charge_receipt et 
        ON et.batch_no = edp.batch_no 
        AND et.bill_id = edp.bill_id 
        AND et.contract_id = edp.contract_id 
        AND et.product_id = edp.id 
      LEFT JOIN ss_order_model srl 
        ON srl.bill_id = edp.bill_id 
        AND srl.id = et.feemodel_id 
        AND srl.fee_type = edp.fee_type 
        AND srl.belong_type = '3' 
        AND srl.del_flag = '0' 
      LEFT JOIN ss_order_product srt 
        ON srt.bill_id = srl.bill_id 
        AND srt.id = srl.ref_id 
        AND srt.combine_id = et.combine_id 
        AND srt.id = edp.id 
        AND srt.del_flag = '0' 
    WHERE edp.batch_no = param_batch_no 
    GROUP BY edp.bill_id,
      edp.contract_id,
      edp.id,
      srl.id) U ;
  -- 删除可能的垃圾数据
  DELETE 
  FROM
    charge_bill_info 
  WHERE bill_id IS NULL 
    OR batch_no IS NULL ;
  -- 生成组合账单  
  INSERT INTO charge_bill_info (
    bill_id,
    batch_no,
    charge_begin_date,
    charge_end_date,
    prod_begin_date,
    prod_end_date,
    combine_id,
    product_id,
    belong_type,
    feemodel_id,
    org_amt,
    max_type,
    max_consume,
    charge_off,
    contract_id,
    charge_amt,
    min_type,
    min_consume,
    bill_flag
  ) 
  SELECT 
    U.bill_id,
    U.batch_no,
    U.charge_begin_date,
    U.charge_end_date,
    U.prod_begin_date,
    U.prod_end_date,
    U.combine_id,
    U.product_id,
    U.belong_type,
    U.id,
    U.org_amt,
    U.max_type,
    U.max_consume,
    U.charge_off,
    U.contract_id,
    U.charge_amt,
    U.min_type,
    -- 保底拆分
    fun_charge_common_divid (
      U.min_type,
      U.orgi_min_consume,
      U.prod_begin_date,
      U.prod_end_date,
      U.charge_begin_date,
      U.charge_end_date
    ) min_consume,
    '1' 
  FROM
    (SELECT 
      elo.bill_id,
      elo.batch_no,
      MIN(elo.charge_begin_date) charge_begin_date,
      MAX(elo.charge_end_date) charge_end_date,
      MIN(elo.prod_begin_date) prod_begin_date,
      MAX(elo.prod_end_date) prod_end_date,
      srl.ref_id AS combine_id,
      NULL product_id,
      '2' AS belong_type,
      srl.id,
      srl.fee_formula,
      SUM(elo.charge_amt) charge_amt,
      SUM(elo.org_amt) org_amt,
      srl.max_type,
      srl.max_consume,
      srl.min_type,
      srl.min_consume AS orgi_min_consume,
      -- 未出账
      0 AS charge_off,
      elo.contract_id,
      srl.fix_charge_type,
      srl.is_multiplied_actualdays 
    FROM
      charge_bill_info elo 
      LEFT JOIN ss_order_model srl 
        ON srl.bill_id = elo.bill_id 
        AND srl.ref_id = elo.combine_id 
        AND srl.belong_type = '2' 
        AND srl.del_flag = '0' 
    WHERE elo.batch_no = param_batch_no 
      AND elo.belong_type = '3' 
    GROUP BY elo.bill_id,
      elo.contract_id,
      elo.combine_id) U ;
  -- 生成合同账单
  INSERT INTO charge_bill_info (
    bill_id,
    batch_no,
    charge_begin_date,
    charge_end_date,
    prod_begin_date,
    prod_end_date,
    combine_id,
    product_id,
    belong_type,
    feemodel_id,
    org_amt,
    max_type,
    max_consume,
    charge_off,
    contract_id,
    charge_amt,
    fee_ratio,
    min_type,
    min_consume,
    bill_flag
  ) 
  SELECT 
    U.bill_id,
    U.batch_no,
    U.charge_begin_date,
    U.charge_end_date,
    U.prod_begin_date,
    U.prod_end_date,
    U.combine_id,
    U.product_id,
    U.belong_type,
    U.id,
    U.org_amt,
    U.max_type,
    U.max_consume,
    U.charge_off,
    U.contract_id,
    fun_get_charge_amount (
      U.fix_charge_type,
      -- 计费对象金额
      U.org_amt,
      -- 下面的都是判断用的参数或者条件
      0,
      U.bill_id,
      U.id,
      U.fee_formula,
      U.is_multiplied_actualdays,
      U.charge_end_date,
      U.charge_begin_date
    ) charge_amt,
    -- 计算月结产品的费率
    (
      CASE
        WHEN U.fix_charge_type = 2 
        THEN fun_charge_get_fee_retio (
          U.org_amt,
          U.bill_id,
          U.id,
          U.fee_formula
        ) 
        ELSE 0 
      END
    ) fee_ratio,
    U.min_type,
    -- 保底拆分
    fun_charge_common_divid (
      U.min_type,
      U.orgi_min_consume,
      U.prod_begin_date,
      U.prod_end_date,
      U.charge_begin_date,
      U.charge_end_date
    ) min_consume,
    '1' 
  FROM
    (SELECT 
      elo.bill_id,
      elo.batch_no,
      MIN(elo.charge_begin_date) charge_begin_date,
      MAX(elo.charge_end_date) charge_end_date,
      sro.order_begin_date prod_begin_date,
      sro.order_end_date prod_end_date,
      NULL combine_id,
      NULL product_id,
      '1' AS belong_type,
      srl.id,
      srl.fee_formula,
      SUM(elo.org_amt) org_amt,
      SUM(elo.charge_amt) charge_amt,
      srl.max_type,
      srl.max_consume,
      srl.min_type,
      srl.min_consume AS orgi_min_consume,
      -- 未出账
      0 AS charge_off,
      elo.contract_id,
      srl.fix_charge_type,
      srl.is_multiplied_actualdays 
    FROM
      charge_bill_info elo 
      LEFT JOIN ss_order_model srl 
        ON srl.bill_id = elo.bill_id 
        AND srl.ref_id = elo.contract_id 
        AND srl.belong_type = '1' 
        AND srl.del_flag = '0' 
      LEFT JOIN ss_order_info sro 
        ON sro.bill_id = elo.bill_id 
        AND sro.contract_id = elo.contract_id 
        AND sro.del_flag = '0' 
    WHERE elo.batch_no = param_batch_no 
      AND elo.belong_type = '2' 
    GROUP BY elo.bill_id,
      elo.contract_id) U ;
  -- 生成账单显示信息
  INSERT INTO charge_order_info (
    bill_id,
    batch_no,
    contract_id,
    bill_display_date,
    pay_deadline,
    customer_name
  ) 
  SELECT 
    elo.bill_id,
    elo.batch_no,
    elo.contract_id,
    elo.charge_begin_date,
    sro.pay_deadline,
    vrf.chinesename 
  FROM
    charge_bill_info elo,
    ss_order_info sro 
    LEFT JOIN v_customer_jf vrf 
      ON vrf.customerid = sro.customer_id 
  WHERE elo.batch_no = param_batch_no 
    AND elo.belong_type = '1' 
    AND sro.contract_id = elo.contract_id 
    AND sro.del_flag = '0' 
    AND sro.bill_id = elo.bill_id ;
  DELETE 
  FROM
    charge_unify_interface 
  WHERE batch_no = param_batch_no ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_gen_charge_period_temp` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_gen_charge_period_temp` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_gen_charge_period_temp`(param_batch_no VARCHAR (20))
BEGIN
  -- 删除同批次的期间数据
  DELETE 
  FROM
    charge_period_temp 
  WHERE batch_no = param_batch_no ;
  -- 对合同内不同期间(月份)的数据进行拆分，插入计费子账单期间临时表
  -- 记录每个合同中子账单的产品计费期间开始日期
  INSERT INTO charge_period_temp (
    batch_no,
    contract_id,
    product_id,
    fee_type,
    prod_begin_date,
    prod_end_date,
    charge_begin_date,
    id,
    charge_end_date,
    bill_id
  ) 
  SELECT 
    V.*,
    CASE
      WHEN DATE_FORMAT(V.charge_begin_date, '%d') = '01' 
      AND DATE_FORMAT(V.prod_begin_date, '%d') = '01' 
      THEN LAST_DAY(V.charge_begin_date) 
      ELSE NULL 
    END,
    CONCAT(
      V.contract_id,
      '-',
      DATE_FORMAT(V.charge_begin_date, '%Y%m')
    ) 
  FROM
    (SELECT 
      batch_no,
      contract_id,
      MAX(product_id),
      MAX(fee_type) fee_type,
      MAX(prod_begin_date) prod_begin_date,
      MAX(prod_end_date) prod_end_date,
      charge_begin_date,
      MAX(id) id 
    FROM
      (SELECT DISTINCT 
        cui.batch_no,
        cui.contract_id,
        cui.product_id,
        cui.fee_type,
        tl.prod_begin_date,
        tl.prod_end_date,
        -- 当月第一天日期
        CONCAT(
          DATE_FORMAT(cui.oc_date, '%Y-%m'),
          '-01'
        ) AS charge_begin_date,
        tl.id 
      FROM
        charge_unify_interface cui 
        LEFT JOIN order_info ro 
          ON ro.contract_id = cui.contract_id 
        LEFT JOIN 
          (SELECT DISTINCT 
            rt.contract_id,
            rt.product_id,
            rt.combine_id,
            rl.fee_type,
            rt.prod_begin_date,
            rt.prod_end_date,
            rt.id 
          FROM
            order_product rt 
            LEFT JOIN order_model rl 
              ON rl.ref_id = rt.id 
              AND rl.belong_type = '3') tl 
          ON tl.contract_id = ro.contract_id 
          AND tl.product_id = cui.product_id 
          AND tl.fee_type = cui.fee_type 
          AND DATE_FORMAT(cui.oc_date, '%Y%m') >= DATE_FORMAT(tl.prod_begin_date, '%Y%m') 
          AND DATE_FORMAT(cui.oc_date, '%Y%m') <= DATE_FORMAT(tl.prod_end_date, '%Y%m') 
      WHERE cui.batch_no = param_batch_no 
      GROUP BY cui.batch_no,
        ro.contract_id,
        tl.id,
        cui.fee_type,
        tl.prod_begin_date,
        DATE_FORMAT(oc_date, '%Y%m')) U 
    GROUP BY batch_no,
      contract_id,
      charge_begin_date,
      id) V ;
  -- 删除空值
  DELETE 
    t.* 
  FROM
    charge_period_temp t,
    (SELECT 
      aoa.charge_begin_date,
      aoa.contract_id 
    FROM
      charge_period_temp aoa 
    WHERE aoa.batch_no = param_batch_no 
    GROUP BY aoa.charge_begin_date,
      aoa.contract_id 
    HAVING COUNT(aoa.charge_begin_date) > 1) U 
  WHERE t.charge_begin_date IN (U.charge_begin_date) 
    AND t.batch_no = param_batch_no 
    AND t.contract_id = U.contract_id 
    AND t.id IS NULL ;
  -- 根据合同里子账单的产品计费开始日期，判断是否当月会发生拆分  
  -- 如果会，插入计费子账单期间临时表，确保年度正确
  INSERT INTO charge_period_temp (
    batch_no,
    contract_id,
    product_id,
    fee_type,
    prod_begin_date,
    prod_end_date,
    charge_begin_date,
    id,
    bill_id
  ) 
  SELECT 
    U.*,
    CONCAT(
      U.contract_id,
      '-',
      DATE_FORMAT(U.charge_begin_date, '%Y%m'),
      '-odd'
    ) 
  FROM
    (SELECT DISTINCT 
      cpt.batch_no,
      cpt.contract_id,
      cpt.product_id,
      cpt.fee_type,
      rt.prod_begin_date,
      rt.prod_end_date,
      DATE_FORMAT(
        CONCAT(
          getYearlyBeginDate (
            CONCAT(
              DATE_FORMAT(charge_begin_date, '%Y%m'),
              DATE_FORMAT(rt.prod_begin_date, '%d')
            ),
            rt.prod_begin_date
          ),
          DATE_FORMAT(rt.prod_begin_date, '%d')
        ),
        '%Y-%m-%d'
      ) AS charge_begin_date,
      rt.id 
    FROM
      charge_period_temp cpt 
      LEFT JOIN order_info ro 
        ON ro.contract_id = cpt.contract_id 
      LEFT JOIN order_product rt 
        ON rt.contract_id = ro.contract_id 
        AND rt.id = cpt.id 
        AND cpt.prod_begin_date = rt.prod_begin_date 
        AND cpt.prod_end_date = rt.prod_end_date 
    WHERE batch_no = param_batch_no 
      AND cpt.charge_begin_date IS NOT NULL) U 
  WHERE U.charge_begin_date IS NOT NULL 
    AND U.charge_begin_date >= U.prod_begin_date 
    AND DATE_FORMAT(U.charge_begin_date, '%Y-%m') >= 
    (SELECT 
      DATE_FORMAT(MIN(oc_date), '%Y-%m') 
    FROM
      charge_unify_interface 
    WHERE batch_no = param_batch_no) ;
  -- 根据计费开始时间计算计费结束时间
  -- 因为一个月中最大有2个计费期间，如果当前记录的计费开始日期不是1号，那么结束日期就是最后一天
  UPDATE 
    charge_period_temp 
  SET
    charge_end_date = (
      -- 当计费开始日期小于产品开始计费日期的月份，取当月最后一天
      CASE
        WHEN DATE_FORMAT(charge_begin_date, '%Y-%m') < DATE_FORMAT(prod_end_date, '%Y-%m') 
        THEN LAST_DAY(charge_begin_date) -- 当计费开始日期处于产品结束月，分两种情况：
        -- 1.如果计费开始日期位于产品结束日期以内，表示有效计费期间，取产品结束日期为计费结束日期
        WHEN DATE_FORMAT(charge_begin_date, '%Y-%m') = DATE_FORMAT(prod_end_date, '%Y-%m') 
        AND prod_end_date > charge_begin_date 
        THEN prod_end_date -- 2.如果计费开始日期位于产品结束日期以外，表示落入有效计费期间之外，取当月结束日期（凑催款单）
        WHEN DATE_FORMAT(charge_begin_date, '%Y-%m') = DATE_FORMAT(prod_end_date, '%Y-%m') 
        AND prod_end_date < charge_begin_date 
        THEN LAST_DAY(charge_begin_date) 
      END
    ) 
  WHERE batch_no = param_batch_no 
    AND DATE_FORMAT(charge_begin_date, '%d') != '01' ;
  -- 处理处于合同期内，处于各个组合计费期间的数据
  UPDATE 
    charge_period_temp t 
  SET
    charge_end_date = IFNULL(
      (SELECT 
        DATE_SUB(
          re.combine_begin_date,
          INTERVAL 1 DAY
        ) 
      FROM
        order_combine re 
      WHERE re.contract_id = t.contract_id 
        AND DATE_FORMAT(re.combine_begin_date, '%Y%m') = DATE_FORMAT(t.charge_begin_date, '%Y%m')),
      LAST_DAY(t.charge_begin_date)
    ) 
  WHERE batch_no = param_batch_no 
    AND product_id IS NULL 
    AND EXISTS 
    (SELECT 
      1 
    FROM
      order_info ro 
    WHERE t.charge_begin_date >= ro.order_begin_date 
      AND t.charge_begin_date <= ro.order_end_date 
      AND ro.contract_id = t.contract_id 
      AND ro.del_flag = '0') ;
  -- 删除多余的拆分单
  DELETE 
    edp.* 
  FROM
    charge_period_temp edp,
    order_info ro 
  WHERE edp.batch_no = param_batch_no 
    AND edp.charge_end_date IS NULL 
    AND edp.bill_id LIKE '%odd' 
    AND ro.contract_id = edp.contract_id 
    AND ro.order_begin_date != ro.order_end_date ;
  -- 删除多余的拆分单
  DELETE 
    t.* 
  FROM
    charge_period_temp t 
  WHERE batch_no = param_batch_no 
    AND EXISTS 
    (SELECT 
      1 
    FROM
      order_info ro 
    WHERE ro.contract_id = t.contract_id 
      AND t.charge_begin_date < ro.order_begin_date) ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_gen_charge_snapshot` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_gen_charge_snapshot` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_gen_charge_snapshot`(param_batch_no VARCHAR (20))
BEGIN
  -- 对合同表取快照生成计费合同表
  INSERT INTO ss_order_info (
    bill_id,
    id,
    contract_id,
    frame_contract_id,
    office_id,
    order_begin_date,
    order_end_date,
    customer_id,
    payment_cycle,
    pay_deadline,
    income_status,
    is_send,
    create_date,
    create_by,
    update_date,
    update_by,
    del_flag,
    remarks
  ) 
  SELECT 
    edp.bill_id,
    ro.id,
    ro.contract_id,
    ro.frame_contract_id,
    ro.office_id,
    ro.order_begin_date,
    ro.order_end_date,
    ro.customer_id,
    ro.payment_cycle,
    ro.pay_deadline,
    ro.income_status,
    ro.is_send,
    ro.create_date,
    ro.create_by,
    ro.update_date,
    ro.update_by,
    ro.del_flag,
    ro.remarks 
  FROM
    order_info ro,
    charge_period_temp edp 
  WHERE ro.contract_id = edp.contract_id 
    AND edp.batch_no = param_batch_no 
  GROUP BY edp.bill_id ;
  -- 对组合表取快照
  INSERT INTO ss_order_combine (
    bill_id,
    id,
    contract_id,
    combine_begin_date,
    combine_end_date,
    create_date,
    create_by,
    update_date,
    update_by,
    del_flag,
    remarks
  ) 
  SELECT DISTINCT 
    edp.bill_id,
    re.id,
    re.contract_id,
    re.combine_begin_date,
    re.combine_end_date,
    re.create_date,
    re.create_by,
    re.update_date,
    re.update_by,
    re.del_flag,
    re.remarks 
  FROM
    order_combine re,
    charge_period_temp edp 
  WHERE re.contract_id = edp.contract_id 
    AND edp.batch_no = param_batch_no 
    AND re.combine_begin_date = edp.prod_begin_date 
    AND re.combine_end_date = edp.prod_end_date ;
  -- 对产品表取快照
  INSERT INTO ss_order_product (
    bill_id,
    id,
    contract_id,
    combine_id,
    product_id,
    product_name,
    ex_product_id,
    prod_begin_date,
    prod_end_date,
    create_date,
    create_by,
    update_date,
    update_by,
    del_flag,
    remarks
  ) 
  SELECT DISTINCT 
    edp.bill_id,
    rt.id,
    rt.contract_id,
    rt.combine_id,
    rt.product_id,
    rt.product_name,
    rt.ex_product_id,
    rt.prod_begin_date,
    rt.prod_end_date,
    rt.create_date,
    rt.create_by,
    rt.update_date,
    rt.update_by,
    rt.del_flag,
    rt.remarks 
  FROM
    order_product rt,
    charge_period_temp edp 
  WHERE rt.contract_id = edp.contract_id 
    AND edp.batch_no = param_batch_no 
    AND rt.prod_begin_date = edp.prod_begin_date 
    AND rt.prod_end_date = edp.prod_end_date ;
  -- 对合同计费模式取快照
  INSERT INTO ss_order_model (
    bill_id,
    id,
    feemodel_name,
    ref_id,
    belong_type,
    fee_formula,
    fee_type,
    min_type,
    min_consume,
    max_type,
    max_consume,
    discount,
    fix_charge_type,
    is_multiplied_actualdays,
    create_date,
    create_by,
    update_date,
    update_by,
    del_flag,
    remarks
  ) 
  SELECT DISTINCT 
    edp.bill_id,
    rl.id,
    rl.feemodel_name,
    rl.ref_id,
    rl.belong_type,
    rl.fee_formula,
    rl.fee_type,
    rl.min_type,
    rl.min_consume,
    rl.max_type,
    rl.max_consume,
    rl.discount,
    rl.fix_charge_type,
    rl.is_multiplied_actualdays,
    rl.create_date,
    rl.create_by,
    rl.update_date,
    rl.update_by,
    rl.del_flag,
    rl.remarks 
  FROM
    charge_period_temp edp,
    order_model rl 
  WHERE rl.belong_type = '1' 
    AND rl.ref_id = edp.contract_id 
    AND edp.batch_no = param_batch_no ;
  INSERT INTO ss_order_model (
    bill_id,
    id,
    feemodel_name,
    ref_id,
    belong_type,
    fee_formula,
    fee_type,
    min_type,
    min_consume,
    max_type,
    max_consume,
    discount,
    fix_charge_type,
    is_multiplied_actualdays,
    create_date,
    create_by,
    update_date,
    update_by,
    del_flag,
    remarks
  ) 
  SELECT DISTINCT 
    edp.bill_id,
    rl.id,
    rl.feemodel_name,
    rl.ref_id,
    rl.belong_type,
    rl.fee_formula,
    rl.fee_type,
    rl.min_type,
    rl.min_consume,
    rl.max_type,
    rl.max_consume,
    rl.discount,
    rl.fix_charge_type,
    rl.is_multiplied_actualdays,
    rl.create_date,
    rl.create_by,
    rl.update_date,
    rl.update_by,
    rl.del_flag,
    rl.remarks 
  FROM
    order_model rl 
    LEFT JOIN order_combine re 
      ON rl.ref_id = re.id,
    charge_period_temp edp 
  WHERE rl.belong_type = '2' 
    AND re.contract_id = edp.contract_id 
    AND edp.batch_no = param_batch_no ;
  INSERT INTO ss_order_model (
    bill_id,
    id,
    feemodel_name,
    ref_id,
    belong_type,
    fee_formula,
    fee_type,
    min_type,
    min_consume,
    max_type,
    max_consume,
    discount,
    fix_charge_type,
    is_multiplied_actualdays,
    create_date,
    create_by,
    update_date,
    update_by,
    del_flag,
    remarks
  ) 
  SELECT DISTINCT 
    edp.bill_id,
    rl.id,
    rl.feemodel_name,
    rl.ref_id,
    rl.belong_type,
    rl.fee_formula,
    rl.fee_type,
    rl.min_type,
    rl.min_consume,
    rl.max_type,
    rl.max_consume,
    rl.discount,
    rl.fix_charge_type,
    rl.is_multiplied_actualdays,
    rl.create_date,
    rl.create_by,
    rl.update_date,
    rl.update_by,
    rl.del_flag,
    rl.remarks 
  FROM
    order_model rl 
    LEFT JOIN order_product rt 
      ON rl.ref_id = rt.id,
    charge_period_temp edp 
  WHERE rl.belong_type = '3' 
    AND rt.contract_id = edp.contract_id 
    AND rt.prod_begin_date = edp.prod_begin_date 
    AND rt.prod_end_date = edp.prod_end_date 
    AND edp.batch_no = param_batch_no ;
  -- 对合同计费定价取快照
  INSERT INTO ss_order_price (
    bill_id,
    id,
    feemodel_id,
    price,
    fee_ratio,
    fee_ratio_division,
    fixed_charge,
    max_consume,
    option_name,
    step_interval,
    step_begin,
    step_end,
    create_date,
    create_by,
    update_date,
    update_by,
    del_flag,
    remarks
  ) 
  SELECT DISTINCT 
    edp.bill_id,
    re.id,
    re.feemodel_id,
    re.price,
    re.fee_ratio,
    re.fee_ratio_division,
    re.fixed_charge,
    re.max_consume,
    re.option_name,
    re.step_interval,
    re.step_begin,
    re.step_end,
    re.create_date,
    re.create_by,
    re.update_date,
    re.update_by,
    re.del_flag,
    re.remarks 
  FROM
    charge_period_temp edp 
    LEFT JOIN order_product rt 
      ON rt.contract_id = edp.contract_id 
      AND rt.prod_begin_date = edp.prod_begin_date 
      AND rt.prod_end_date = edp.prod_end_date 
    LEFT JOIN order_model rl 
      ON rl.belong_type = '3' 
      AND rl.ref_id = rt.id 
    LEFT JOIN order_price re 
      ON re.feemodel_id = rl.id 
  WHERE edp.batch_no = param_batch_no ;
  -- 预付信息
  INSERT INTO ss_order_adv_payment (
    bill_id,
    id,
    contract_id,
    combine_id,
    payment_type,
    display_name,
    amount,
    advance_date,
    display_date,
    fee_unit,
    create_date,
    create_by,
    update_date,
    update_by,
    del_flag,
    remarks
  ) 
  SELECT DISTINCT 
    edp.bill_id,
    rvt.id,
    rvt.contract_id,
    rvt.combine_id,
    rvt.payment_type,
    rvt.display_name,
    rvt.amount,
    rvt.advance_date,
    rvt.display_date,
    rvt.fee_unit,
    rvt.create_date,
    rvt.create_by,
    rvt.update_date,
    rvt.update_by,
    rvt.del_flag,
    rvt.remarks 
  FROM
    charge_period_temp edp,
    order_adv_payment rvt 
  WHERE edp.batch_no = param_batch_no 
 
    AND rvt.contract_id = edp.contract_id  ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_gen_charge_unify_interface` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_gen_charge_unify_interface` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_gen_charge_unify_interface`(param_batch_no VARCHAR (20))
BEGIN
  DECLARE v_contract_id,
  v_product_id,
  v_fee_type,
  v_charge_begin_date,
  v_charge_end_date VARCHAR (64) ;
  DECLARE done INT DEFAULT 0 ;
  DECLARE receipt_cur CURSOR FOR 
  SELECT 
    edp.contract_id,
    edp.product_id,
    edp.fee_type,
    MIN(edp.charge_begin_date) AS charge_begin_date,
    MAX(edp.charge_end_date) AS charge_end_date 
  FROM
    charge_period_temp edp,
    ss_order_model srl 
  WHERE batch_no = param_batch_no 
    AND srl.bill_id = edp.bill_id 
    AND srl.belong_type = '2' 
    AND srl.del_flag = '0' 
    AND srl.min_type = '3' 
  GROUP BY contract_id,
    product_id ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  OPEN receipt_cur ;
  cursor_loop :
  LOOP
    FETCH receipt_cur INTO v_contract_id,
    v_product_id,
    v_fee_type,
    v_charge_begin_date,
    v_charge_end_date ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    INSERT INTO charge_unify_interface (
      batch_no,
      oc_date,
      contract_id,
      product_id,
      fee_type,
      data1
    ) 
    SELECT 
      param_batch_no,
      DATE_FORMAT(oc_date, '%Y%m%d'),
      v_contract_id,
      v_product_id,
      v_fee_type,
      0 
    FROM
      v_day 
    WHERE oc_date >= v_charge_begin_date 
      AND oc_date <= v_charge_end_date ;
  END LOOP cursor_loop ;
  CLOSE receipt_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_gen_charge_wrong` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_gen_charge_wrong` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_gen_charge_wrong`(param_batch_no VARCHAR (20))
BEGIN
  -- 多年组合拆分中间年度
  CALL sp_insert_yearly_period_temp (param_batch_no) ;
  INSERT INTO charge_wrong (
    id,
    batch_no,
    oc_date,
    contract_id,
    customer_id,
    product_id,
    fee_type,
    wrong_reason,
    wrong_status,
    create_datetime
  ) 
  SELECT 
    REPLACE(UUID(), '-', ''),
    cui.batch_no,
    cui.oc_date,
    cui.contract_id,
    cui.customer_id,
    cui.product_id,
    cui.office_id,
    CASE
      WHEN ro.contract_id IS NULL 
      AND rt.product_id IS NULL 
      THEN CONCAT(
        '合同号',
        cui.contract_id,
        '不存在,',
        '产品',
        cui.product_id,
        '也不存在'
      ) 
      WHEN ro.contract_id IS NULL 
      THEN CONCAT(
        '合同号',
        cui.contract_id,
        '不存在'
      ) 
      WHEN ro.customer_id != cui.customer_id 
      THEN CONCAT(
        '客户号不正确，合同上的客户号为',
        ro.customer_id,
        ',接口中为',
        cui.customer_id
      ) 
      WHEN rt.product_id IS NULL 
      THEN CONCAT(
        '产品',
        cui.product_id,
        '不存在'
      ) 
    END,
    0,
    NOW() 
  FROM
    charge_unify_interface cui 
    LEFT JOIN order_info ro 
      ON cui.contract_id = ro.contract_id 
    LEFT JOIN order_product rt 
      ON rt.contract_id = ro.contract_id 
      AND rt.product_id = cui.product_id 
  WHERE batch_no = param_batch_no ;
  -- 删除没错的错单
  DELETE 
  FROM
    charge_wrong 
  WHERE batch_no = param_batch_no 
    AND (
      wrong_reason IS NULL 
      OR wrong_reason = ''
    ) ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_gen_contract_income` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_gen_contract_income` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_gen_contract_income`(param_year VARCHAR (20))
BEGIN
  DECLARE v_yearly_begin_month,
  v_current_month VARCHAR (10) ;
  DECLARE done INT DEFAULT 0 ;
  DECLARE month_cur CURSOR FOR 
  SELECT 
    CONCAT(param_year, months) 
  FROM
    v_month ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  DROP TABLE IF EXISTS income_contract_temp ;
  CREATE TABLE income_contract_temp (
    order_source VARCHAR (40),
    hs_customername VARCHAR (40),
    hs_user_id VARCHAR (40),
    hs_contract_id VARCHAR (40),
    charge_begin_date VARCHAR (20),
    charge_end_date VARCHAR (20),
    charge_month VARCHAR (20),
    income DECIMAL (25, 8),
    receiveable DECIMAL (25, 8),
    payable DECIMAL (25, 8),
    service_charge DECIMAL (25, 8)
  ) ;
  DROP TABLE IF EXISTS v_month ;
  CREATE TABLE v_month (months VARCHAR (2)) ;
  INSERT INTO v_month 
  VALUES
    ('01') ;
  INSERT INTO v_month 
  VALUES
    ('02') ;
  INSERT INTO v_month 
  VALUES
    ('03') ;
  INSERT INTO v_month 
  VALUES
    ('04') ;
  INSERT INTO v_month 
  VALUES
    ('05') ;
  INSERT INTO v_month 
  VALUES
    ('06') ;
  INSERT INTO v_month 
  VALUES
    ('07') ;
  INSERT INTO v_month 
  VALUES
    ('08') ;
  INSERT INTO v_month 
  VALUES
    ('09') ;
  INSERT INTO v_month 
  VALUES
    ('10') ;
  INSERT INTO v_month 
  VALUES
    ('11') ;
  INSERT INTO v_month 
  VALUES
    ('12') ;
  SELECT 
    CONCAT(param_year, '01') INTO v_yearly_begin_month ;
  OPEN month_cur ;
  cursor_loop :
  LOOP
    FETCH month_cur INTO v_current_month ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    INSERT INTO income_contract_temp (
      order_source,
      hs_customername,
      hs_user_id,
      hs_contract_id,
      charge_month,
      charge_begin_date,
      charge_end_date,
      income
    ) 
    SELECT 
      V.order_source,
      V.hs_customername,
      V.hs_user_id,
      V.hs_contract_id,
      v_current_month AS charge_month,
      V.charge_begin_time,
      V.charge_end_time,
      SUM(
        ROUND(
          IFNULL(
            CASE
              WHEN V.charge_end_time = DATE_FORMAT(V.income_end_date, '%Y-%m-%d') 
              AND V.split_amount IS NOT NULL 
              AND V.split_amount != '0' 
              THEN V.split_amount 
              WHEN V.charge_end_time != DATE_FORMAT(V.income_end_date, '%Y-%m-%d') 
              THEN V.total_income 
              ELSE V.total_income 
            END,
            0
          ),
          2
        )
      ) AS finance_total_income 
    FROM
      (SELECT 
        U.order_source,
        U.con_id,
        U.contract_id AS hs_contract_id,
        U.order_id,
        U.hs_user_id,
        U.hs_customername,
        U.hs_product_id,
        U.product_name,
        DATE_FORMAT(U.charge_begin_time, '%Y-%m-%d') AS charge_begin_time,
        DATE_FORMAT(U.charge_end_time, '%Y-%m-%d') AS charge_end_time,
        U.payment_type,
        U.display_name,
        ROUND(
          (
            GREATEST(
              U.current_min_income * fun_get_date_diff (
                U.charge_end_time,
                U.yearly_begin_date
              ) / fun_get_yearly_day_count (U.yearly_begin_date),
              U.yearly_tech_service_fare
            ) + U.adjust_amt
          ) * U.split_ratio / 100,
          2
        ) AS total_income,
        U.income_start_date,
        U.income_end_date,
        U.split_amount 
      FROM
        (SELECT 
          ro.order_source,
          vtf.con_id,
          elo.contract_id,
          elo.order_id,
          ro.hs_user_id,
          vrf.chinesename AS hs_customername,
          reg.hs_product_id,
          srt.product_name,
          elo.charge_begin_time,
          elo.charge_end_time,
          '12475' AS payment_type,
          '技术服务费' AS display_name,
          IFNULL(srl.min_consume, 0) AS current_min_income,
          fun_get_yearly_begin_date (
            elo.charge_begin_time,
            elo.prod_begin_date
          ) AS yearly_begin_date,
          IFNULL(elo.yearly_tech_service_fare, 0) AS yearly_tech_service_fare,
          IFNULL(
            (SELECT 
              SUM(adjust_balance) 
            FROM
              charge_adjust_bill etl 
            WHERE 1 = 1 
              AND bill_date <= elo.charge_end_time 
              AND etl.hs_contract_id = elo.contract_id 
              AND etl.product_sale_id = srt.product_sale_id),
            0
          ) AS adjust_amt,
          reg.split_ratio,
          reg.income_start_date,
          reg.income_end_date,
          reg.split_amount 
        FROM
          charge_bill_info elo 
          LEFT JOIN 
            (SELECT 
              order_id,
              MAX(charge_end_time) charge_end_time 
            FROM
              charge_bill_info 
            WHERE belong_type = '1' 
              AND DATE_FORMAT(charge_begin_time, '%Y%m') >= v_yearly_begin_month 
              AND DATE_FORMAT(charge_end_time, '%Y%m') <= v_current_month 
            GROUP BY order_id,
              fun_get_yearly_end_date (
                fun_get_yearly_begin_date (
                  charge_begin_time,
                  prod_begin_date
                )
              )) cbi 
            ON elo.order_id = cbi.order_id 
            AND elo.charge_end_time = cbi.charge_end_time,
          order_income_setting reg,
          ss_order_product srt,
          ss_order_model srl,
          v_contract_jf vtf,
          order_info ro 
          LEFT JOIN v_customer_jf vrf 
            ON vrf.customerid = ro.hs_user_id 
        WHERE elo.charge_end_time <= cbi.charge_end_time 
          AND elo.belong_type = '2' 
          AND reg.order_id = elo.order_id 
          AND reg.payment_type = '12475' 
          AND fun_get_yearly_begin_date (
            elo.charge_begin_time,
            elo.prod_begin_date
          ) <= DATE_FORMAT(
            reg.income_start_date,
            '%Y-%m-%d'
          ) 
          AND fun_get_yearly_end_date (
            fun_get_yearly_begin_date (
              elo.charge_begin_time,
              elo.prod_begin_date
            )
          ) >= DATE_FORMAT(reg.income_end_date, '%Y-%m-%d') 
          AND srt.bill_id = elo.bill_id 
          AND srt.order_id = elo.order_id 
          AND srt.combine_id = elo.combine_id 
          AND srt.hs_product_id = reg.hs_product_id 
          AND srl.bill_id = elo.bill_id 
          AND srl.combine_id = srt.combine_id 
          AND srl.belong_type = '2' 
          AND vtf.contractid = elo.contract_id 
          AND ro.order_id = elo.order_id 
          AND DATE_FORMAT(cbi.charge_end_time, '%Y%m') >= v_yearly_begin_month 
          AND DATE_FORMAT(cbi.charge_end_time, '%Y%m') <= v_current_month 
        GROUP BY elo.charge_begin_time,
          elo.charge_end_time,
          elo.order_id,
          elo.combine_id,
          reg.hs_product_id 
        ORDER BY elo.charge_end_time DESC) U 
      UNION
      ALL 
      SELECT DISTINCT 
        ro.order_source,
        vtf.con_id,
        ro.hs_contract_id,
        re.order_id,
        ro.hs_user_id,
        vrf.chinesename AS hs_customername,
        re.product_sale_id AS hs_product_id,
        re.product_name,
        DATE_FORMAT(
          MAX(elo.charge_begin_time),
          '%Y-%m-%d'
        ) AS charge_begin_time,
        DATE_FORMAT(
          MAX(elo.charge_end_time),
          '%Y-%m-%d'
        ) AS charge_end_time,
        re.payment_type,
        re.display_name,
        MAX(
          ROUND(
            IFNULL(
              CASE
                WHEN fun_get_date_diff (
                  elo.charge_end_time,
                  ois.income_start_date
                ) < 0 
                THEN 0 
                WHEN re.payment_type IN (12471, 12474) 
                AND ois.income_end_date >= elo.charge_end_time 
                AND elo.charge_end_time >= ois.income_start_date 
                THEN re.payment_amount * ois.split_ratio / 100 
                WHEN re.payment_type IN (12472, 12473) 
                AND ois.income_end_date >= elo.charge_end_time 
                THEN (
                  re.payment_amount * ois.split_ratio / 100
                ) * fun_get_date_diff (
                  elo.charge_end_time,
                  ois.income_start_date
                ) / fun_get_date_diff (
                  ois.income_end_date,
                  ois.income_start_date
                ) 
                ELSE re.payment_amount * ois.split_ratio / 100 
              END,
              0
            ),
            2
          )
        ) AS total_income,
        ois.income_start_date,
        ois.income_end_date,
        ois.split_amount
      FROM
        (SELECT 
          op.order_id,
          op.combine_id,
          rt.prod_begin_date,
          rt.prod_end_date,
          op.payment_type,
          rt.product_name,
          CASE
            WHEN payment_type = '12471' 
            THEN '一次性费用' 
            WHEN payment_type = '12472' 
            THEN '不定期' 
            WHEN payment_type = '12473' 
            THEN '年费' 
            WHEN payment_type = '12474' 
            THEN '条件延后' 
            ELSE '' 
          END display_name,
          rt.product_sale_id,
          vpj.prdid,
          SUM(payment_amount) AS payment_amount 
        FROM
          order_paymentcycle op 
          LEFT JOIN 
            (SELECT 
              t.order_id,
              t.combine_id,
              t.product_sale_id,
              t.product_name,
              t.hs_product_id,
              t.prod_begin_date,
              t.prod_end_date 
            FROM
              order_product t 
            GROUP BY t.order_id,
              t.combine_id,
              t.product_sale_id) rt 
            ON rt.order_id = op.order_id 
            AND rt.combine_id = op.combine_id 
          LEFT JOIN v_prdsale_jf vpj 
            ON vpj.productid = rt.product_sale_id 
        WHERE op.payment_type != '10000' 
        GROUP BY op.order_id,
          op.combine_id,
          op.payment_type,
          rt.hs_product_id) re 
        LEFT JOIN order_income_setting ois 
          ON ois.order_id = re.order_id 
          AND ois.payment_type = re.payment_type 
          AND ois.hs_product_id = re.prdid 
          AND ois.income_end_date >= re.prod_begin_date 
          AND ois.income_start_date <= re.prod_end_date,
        charge_bill_info elo,
        order_info ro 
        LEFT JOIN v_customer_jf vrf 
          ON vrf.customerid = ro.hs_user_id,
        v_contract_jf vtf 
      WHERE elo.belong_type = '3' 
        AND elo.order_id = re.order_id 
        AND elo.combine_id = re.combine_id 
        AND elo.hs_product_id = re.product_sale_id 
        AND ro.order_id = re.order_id 
        AND vtf.contractid = ro.hs_contract_id 
        AND DATE_FORMAT(elo.charge_end_time, '%Y%m') >= v_yearly_begin_month 
        AND DATE_FORMAT(elo.charge_end_time, '%Y%m') <= v_current_month 
      GROUP BY con_id,
        ois.income_start_date,
        ois.income_end_date,
        payment_type,
        hs_product_id) V 
    GROUP BY V.hs_contract_id ;
    -- 技术服务费
    UPDATE 
      income_contract_temp etp,
      (SELECT 
        V.order_source,
        V.hs_customername,
        V.hs_user_id,
        V.hs_contract_id,
        v_current_month AS charge_month,
        V.charge_begin_time,
        V.charge_end_time,
        SUM(
          ROUND(
            IFNULL(
              CASE
                WHEN V.charge_end_time = DATE_FORMAT(V.income_end_date, '%Y-%m-%d') 
                AND V.split_amount IS NOT NULL 
                AND V.split_amount != '0' 
                THEN V.split_amount 
                WHEN V.charge_end_time != DATE_FORMAT(V.income_end_date, '%Y-%m-%d') 
                THEN V.total_income 
                ELSE V.total_income 
              END,
              0
            ),
            2
          )
        ) AS service_charge 
      FROM
        (SELECT 
          U.order_source,
          U.con_id,
          U.contract_id AS hs_contract_id,
          U.order_id,
          U.hs_user_id,
          U.hs_customername,
          U.hs_product_id,
          U.product_name,
          DATE_FORMAT(U.charge_begin_time, '%Y-%m-%d') AS charge_begin_time,
          DATE_FORMAT(U.charge_end_time, '%Y-%m-%d') AS charge_end_time,
          U.payment_type,
          U.display_name,
          ROUND(
            (
              GREATEST(
                U.current_min_income * fun_get_date_diff (
                  U.charge_end_time,
                  U.yearly_begin_date
                ) / fun_get_yearly_day_count (U.yearly_begin_date),
                U.yearly_tech_service_fare
              ) + U.adjust_amt
            ) * U.split_ratio / 100,
            2
          ) AS total_income,
          U.income_start_date,
          U.income_end_date,
          U.split_amount 
        FROM
          (SELECT 
            ro.order_source,
            vtf.con_id,
            elo.contract_id,
            elo.order_id,
            ro.hs_user_id,
            vrf.chinesename AS hs_customername,
            reg.hs_product_id,
            srt.product_name,
            elo.charge_begin_time,
            elo.charge_end_time,
            '12475' AS payment_type,
            '技术服务费' AS display_name,
            IFNULL(srl.min_consume, 0) AS current_min_income,
            fun_get_yearly_begin_date (
              elo.charge_begin_time,
              elo.prod_begin_date
            ) AS yearly_begin_date,
            IFNULL(elo.yearly_tech_service_fare, 0) AS yearly_tech_service_fare,
            IFNULL(
              (SELECT 
                SUM(adjust_balance) 
              FROM
                charge_adjust_bill etl 
              WHERE 1 = 1 
                AND bill_date <= elo.charge_end_time 
                AND etl.hs_contract_id = elo.contract_id 
                AND etl.product_sale_id = srt.product_sale_id),
              0
            ) AS adjust_amt,
            reg.split_ratio,
            reg.income_start_date,
            reg.income_end_date,
            reg.split_amount 
          FROM
            charge_bill_info elo 
            LEFT JOIN 
              (SELECT 
                order_id,
                MAX(charge_end_time) charge_end_time 
              FROM
                charge_bill_info 
              WHERE belong_type = '1' 
                AND DATE_FORMAT(charge_begin_time, '%Y%m') >= v_yearly_begin_month 
                AND DATE_FORMAT(charge_end_time, '%Y%m') <= v_current_month 
              GROUP BY order_id,
                fun_get_yearly_end_date (
                  fun_get_yearly_begin_date (
                    charge_begin_time,
                    prod_begin_date
                  )
                )) cbi 
              ON elo.order_id = cbi.order_id 
              AND elo.charge_end_time = cbi.charge_end_time,
            order_income_setting reg,
            ss_order_product srt,
            ss_order_model srl,
            v_contract_jf vtf,
            order_info ro 
            LEFT JOIN v_customer_jf vrf 
              ON vrf.customerid = ro.hs_user_id 
          WHERE elo.charge_end_time <= cbi.charge_end_time 
            AND elo.belong_type = '2' 
            AND reg.order_id = elo.order_id 
            AND reg.payment_type = '12475' 
            AND fun_get_yearly_begin_date (
              elo.charge_begin_time,
              elo.prod_begin_date
            ) <= DATE_FORMAT(
              reg.income_start_date,
              '%Y-%m-%d'
            ) 
            AND fun_get_yearly_end_date (
              fun_get_yearly_begin_date (
                elo.charge_begin_time,
                elo.prod_begin_date
              )
            ) >= DATE_FORMAT(reg.income_end_date, '%Y-%m-%d') 
            AND srt.bill_id = elo.bill_id 
            AND srt.order_id = elo.order_id 
            AND srt.combine_id = elo.combine_id 
            AND srt.hs_product_id = reg.hs_product_id 
            AND srl.bill_id = elo.bill_id 
            AND srl.combine_id = srt.combine_id 
            AND srl.belong_type = '2' 
            AND vtf.contractid = elo.contract_id 
            AND ro.order_id = elo.order_id 
            AND DATE_FORMAT(cbi.charge_end_time, '%Y%m') >= v_yearly_begin_month 
            AND DATE_FORMAT(cbi.charge_end_time, '%Y%m') <= v_current_month 
          GROUP BY elo.charge_begin_time,
            elo.charge_end_time,
            elo.order_id,
            elo.combine_id,
            reg.hs_product_id 
          ORDER BY elo.charge_end_time DESC) U) V 
      GROUP BY V.hs_contract_id) W 
    SET
      etp.service_charge = W.service_charge 
    WHERE etp.hs_contract_id = W.hs_contract_id 
      AND etp.charge_month = W.charge_month ;
  END LOOP cursor_loop ;
  CLOSE month_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_gen_contract_list` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_gen_contract_list` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_gen_contract_list`(
  param_batch_no VARCHAR (40),
  param_contract_id VARCHAR (40)
)
BEGIN
  -- 合同编号（用于提高性能）
  DECLARE v_contract_id VARCHAR (60) ;
  DECLARE done INT DEFAULT 0 ;
  DECLARE order_cur CURSOR FOR 
  SELECT 
    contract_id 
  FROM
    order_import_list 
  WHERE (
      batch_no = param_batch_no 
      OR param_batch_no IS NULL
    ) 
    AND (
      param_contract_id IS NULL 
      OR contract_id = param_contract_id
    ) 
  GROUP BY contract_id ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  OPEN order_cur ;
  cursor_loop :
  LOOP
    FETCH order_cur INTO v_contract_id ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    CALL sp_delete_order (v_contract_id) ;
    -- 合同
    INSERT INTO order_info (
      id,
      contract_id,
      office_id,
      order_begin_date,
      order_end_date,
      customer_id,
      payment_cycle,
      del_flag,
      order_status,
      pay_deadline,
      create_date,
      update_date
    ) 
    SELECT 
      REPLACE(UUID(), '-', ''),
      v_contract_id,
      MAX(se.code),
      MIN(rtt.product_begin_date),
      MAX(rtt.product_end_date),
      vtf.customerid,
      st.value,
      '0',
      '30',
      '',
      NOW(),
      NOW() 
    FROM
      order_import_list rtt 
      LEFT JOIN sys_office se 
        ON se.name = rtt.office_name 
      LEFT JOIN v_contract_jf vtf 
        ON vtf.contractid = rtt.contract_id 
      LEFT JOIN sys_dict st 
        ON st.type = 'payment_cycle' 
        AND st.label = rtt.payment_cycle 
    WHERE rtt.contract_id = v_contract_id 
    GROUP BY rtt.contract_id ;
    -- 组合
    INSERT INTO order_combine (
      id,
      contract_id,
      combine_begin_date,
      combine_end_date,
      del_flag,
      remarks,
      tech_charge_income_flag
    ) 
    SELECT 
      REPLACE(UUID(), '-', ''),
      v_contract_id,
      MIN(product_begin_date),
      MAX(product_end_date),
      '0',
      group_no,
      (
        CASE
          WHEN tech_charge_income_flag = '是' 
          THEN '1' 
          ELSE '0' 
        END
      ) 
    FROM
      order_import_list 
    WHERE contract_id = v_contract_id 
    GROUP BY contract_id,
      product_begin_date,
      product_end_date,
      group_no ;
    -- 产品
    INSERT INTO order_product (
      id,
      contract_id,
      combine_id,
      product_id,
      product_name,
      ex_product_id,
      prod_begin_date,
      prod_end_date,
      del_flag,
      remarks
    ) 
    SELECT 
      REPLACE(UUID(), '-', ''),
      rtt.contract_id,
      re.id,
      rtt.product_id,
      vef.productname,
      vef.prdid,
      rtt.product_begin_date,
      rtt.product_end_date,
      '0',
      rtt.charge_model 
    FROM
      order_import_list rtt 
      LEFT JOIN order_combine re 
        ON re.contract_id = rtt.contract_id 
        AND re.combine_begin_date = rtt.product_begin_date 
        AND re.combine_end_date = rtt.product_end_date 
        AND re.remarks = rtt.group_no 
      LEFT JOIN v_prdsale_jf vef 
        ON vef.productid = rtt.product_id 
    WHERE rtt.contract_id = v_contract_id 
    GROUP BY rtt.contract_id,
      re.id,
      rtt.product_id,
      rtt.charge_model ;
    -- 计费模式（组合）
    INSERT INTO order_model (
      id,
      ref_id,
      belong_type,
      min_type,
      min_consume,
      del_flag
    ) 
    SELECT DISTINCT 
      REPLACE(UUID(), '-', ''),
      re.id,
      '2',
      (
        CASE
          WHEN rtt.min_type = '年' 
          THEN '1' 
          WHEN rtt.min_type = '月' 
          THEN '2' 
          WHEN rtt.min_type = '日' 
          THEN '3' 
          WHEN rtt.min_type = '不定期' 
          THEN '4' 
          ELSE NULL 
        END
      ),
      (
        CASE
          WHEN rtt.min_type IS NOT NULL 
          THEN rtt.amount 
          ELSE NULL 
        END
      ),
      '0' 
    FROM
      order_import_list rtt 
      LEFT JOIN order_combine re 
        ON re.contract_id = rtt.contract_id 
        AND re.combine_begin_date = rtt.product_begin_date 
        AND re.combine_end_date = rtt.product_end_date 
        AND re.remarks = rtt.group_no 
    WHERE rtt.contract_id = v_contract_id 
      AND rtt.payment_type = '技术服务费' ;
    -- 计费模式2（组合），固定费用占用一个组合
    INSERT INTO order_model (
      id,
      ref_id,
      belong_type,
      min_type,
      min_consume,
      del_flag
    ) 
    SELECT 
      REPLACE(UUID(), '-', ''),
      id,
      '2',
      NULL,
      NULL,
      '0' 
    FROM
      order_combine 
    WHERE contract_id = v_contract_id 
      AND id NOT IN 
      (SELECT 
        ref_id 
      FROM
        order_model 
      WHERE belong_type = '2' 
        AND del_flag = '0') ;
    -- 计费模式（产品）
    INSERT INTO order_model (
      id,
      ref_id,
      feemodel_name,
      fee_formula,
      fee_type,
      belong_type,
      del_flag
    ) 
    SELECT 
      REPLACE(UUID(), '-', ''),
      rt.id,
      rt.remarks,
      el.fee_formula,
      el.fee_type,
      '3',
      '0' 
    FROM
      order_product rt 
      LEFT JOIN charge_model el 
        ON el.model_name = rt.remarks 
    WHERE rt.contract_id = v_contract_id ;
    -- 预付
    INSERT INTO order_adv_payment (
      id,
      contract_id,
      combine_id,
      payment_type,
      amount,
      advance_date,
      del_flag,
      display_name,
      display_date
    ) 
    SELECT 
      REPLACE(UUID(), '-', ''),
      rtt.contract_id,
      re.id,
      (
        CASE
          WHEN rtt.payment_type = '保底预付' 
          THEN '10000' 
          WHEN rtt.payment_type = '一次性费用' 
          THEN '12471' 
          WHEN rtt.payment_type = '不定期' 
          THEN '12472' 
          WHEN rtt.payment_type = '年费' 
          THEN '12473' 
          WHEN rtt.payment_type = '条件延后' 
          THEN '12474' 
          ELSE NULL 
        END
      ),
      rtt.amount,
      rtt.payment_date,
      '0',
      rtt.payment_type,
      rtt.payment_date 
    FROM
      order_import_list rtt 
      LEFT JOIN order_combine re 
        ON re.contract_id = rtt.contract_id 
        AND re.combine_begin_date = rtt.product_begin_date 
        AND re.combine_end_date = rtt.product_end_date 
        AND re.remarks = rtt.group_no 
    WHERE rtt.contract_id = v_contract_id 
      AND rtt.payment_type != '技术服务费' ;
    -- 收入设置
    CALL sp_order_set_split_retio (v_contract_id) ;
    -- 合同校验
    CALL sp_charge_validate_order (v_contract_id) ;
  END LOOP cursor_loop ;
  CLOSE order_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_gen_insertManyDate` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_gen_insertManyDate` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_gen_insertManyDate`(
  param_begin_date VARCHAR (20),
  param_end_date VARCHAR (20)
)
BEGIN
  DECLARE cur_date,
  end_date VARCHAR (20) ;
  SET cur_date = DATE_FORMAT(param_begin_date, '%Y-%m-%d') ;
  SET end_date = DATE_FORMAT(param_end_date, '%Y-%m-%d') ;
  WHILE
    cur_date < end_date DO 
    INSERT INTO v_day (oc_date) 
    VALUES
      (cur_date) ;
    SET cur_date = DATE_ADD(cur_date, INTERVAL 1 DAY) ;
  END WHILE ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_gen_order_data_transfer` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_gen_order_data_transfer` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_gen_order_data_transfer`(param_order_id VARCHAR (32))
BEGIN
  -- 定义变量
  DECLARE v_order_id,
  v_combine_id VARCHAR (32) ;
  -- 各自保底：0 ,共用保底：1
  DECLARE v_min_consume_flag VARCHAR (1) ;
  DECLARE done INT DEFAULT 0 ;
  -- 通过游标计算计费周期的确切截至时间
  DECLARE order_data_cur CURSOR FOR 
  SELECT 
    order_id 
  FROM
    order_info 
  WHERE order_id NOT IN 
    (SELECT DISTINCT 
      order_id 
    FROM
      order_model 
    WHERE belong_type = '2') 
    AND (
      param_order_id IS NULL 
      OR order_id = param_order_id
    ) ;
  -- 合同id的游标结束标志
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  OPEN order_data_cur ;
  cursor_loop :
  LOOP
    FETCH order_data_cur INTO v_order_id ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    SELECT 
      (
        CASE
          WHEN min_consume IS NULL 
          THEN '0' 
          ELSE '1' 
        END
      ) INTO v_min_consume_flag 
    FROM
      order_model 
    WHERE order_id = v_order_id 
      AND belong_type = '1' ;
    -- 共用保底
    IF v_min_consume_flag = '1' 
    THEN -- 取最小combine_id
    SELECT 
      MIN(combine_id) INTO v_combine_id 
    FROM
      order_model 
    WHERE belong_type = '3' 
      AND order_id = v_order_id ;
    INSERT INTO order_model (
      create_datetime,
      modi_datetime,
      feemodel_id,
      feemodel_name,
      order_id,
      combine_id,
      belong_type,
      nonrecurring_payment,
      fare_per_year,
      min_type,
      min_consume,
      max_type,
      max_consume,
      base_income_type,
      base_income,
      discount,
      remark
    ) 
    SELECT 
      create_datetime,
      modi_datetime,
      feemodel_id,
      feemodel_name,
      order_id,
      v_combine_id,
      '2',
      nonrecurring_payment,
      fare_per_year,
      min_type,
      min_consume,
      max_type,
      max_consume,
      base_income_type,
      base_income,
      discount,
      remark 
    FROM
      order_model 
    WHERE belong_type = '1' 
      AND order_id = v_order_id ;
    -- 合同相应字段置为空
    UPDATE 
      order_model 
    SET
      nonrecurring_payment = NULL,
      fare_per_year = NULL,
      min_type = NULL,
      min_consume = NULL,
      max_type = NULL,
      max_consume = NULL,
      base_income_type = NULL,
      base_income = NULL,
      discount = NULL,
      remark = NULL 
    WHERE belong_type = '1' 
      AND order_id = v_order_id ;
    -- 更新产品的组合id
    UPDATE 
      order_product 
    SET
      combine_id = v_combine_id 
    WHERE order_id = v_order_id ;
    -- 更新产品的计费模式组合id
    UPDATE 
      order_model 
    SET
      combine_id = v_combine_id 
    WHERE order_id = v_order_id ;
    -- 更新产品的计费价格组合id
    UPDATE 
      order_price 
    SET
      combine_id = v_combine_id 
    WHERE order_id = v_order_id ;
    -- 删除不需要的组合
    DELETE 
    FROM
      order_combine 
    WHERE order_id = v_order_id 
      AND combine_id != v_combine_id ;
    ELSE -- 各自保底
    INSERT INTO order_model (
      create_datetime,
      modi_datetime,
      feemodel_id,
      feemodel_name,
      order_id,
      combine_id,
      belong_type,
      nonrecurring_payment,
      fare_per_year,
      min_type,
      min_consume,
      max_type,
      max_consume,
      base_income_type,
      base_income,
      discount,
      remark,
      fare_per_year_type
    ) 
    SELECT 
      create_datetime,
      modi_datetime,
      feemodel_id,
      feemodel_name,
      order_id,
      combine_id,
      '2',
      nonrecurring_payment,
      fare_per_year,
      min_type,
      min_consume,
      max_type,
      max_consume,
      base_income_type,
      base_income,
      discount,
      remark,
      fare_per_year_type 
    FROM
      order_model 
    WHERE belong_type = '3' 
      AND order_id = v_order_id 
    GROUP BY combine_id ;
    -- 产品相应字段置为空
    UPDATE 
      order_model 
    SET
      nonrecurring_payment = NULL,
      fare_per_year = NULL,
      min_type = NULL,
      min_consume = NULL,
      max_type = NULL,
      max_consume = NULL,
      base_income_type = NULL,
      base_income = NULL,
      discount = NULL,
      remark = NULL 
    WHERE belong_type = '3' 
      AND order_id = v_order_id ;
    END IF ;
    UPDATE 
      order_combine re 
      LEFT JOIN order_product rt 
        ON re.order_id = rt.order_id 
        AND re.combine_id = rt.combine_id SET re.combine_begin_date = rt.prod_begin_date,
      re.combine_end_date = rt.prod_end_date 
    WHERE re.order_id = v_order_id ;
  END LOOP cursor_loop ;
  CLOSE order_data_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_gen_period_end_time` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_gen_period_end_time` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_gen_period_end_time`(param_batch_no VARCHAR (20))
BEGIN
  -- 定义变量
  DECLARE v_contract_id,
  v_product_id,
  v_id,
  v_fee_type,
  v_charge_begin_date,
  v_charge_middle_date VARCHAR (60) ;
  DECLARE done INT DEFAULT 0 ;
  -- 通过游标计算计费周期的确切截至时间
  DECLARE product_period_cur CURSOR FOR 
  SELECT 
    contract_id,
    product_id,
    id,
    fee_type,
    charge_begin_date 
  FROM
    charge_period_temp 
  WHERE batch_no = param_batch_no 
    AND charge_end_date IS NULL 
  ORDER BY charge_begin_date DESC ;
  -- 产品的游标结束标志
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  OPEN product_period_cur ;
  cursor_loop :
  LOOP
    FETCH product_period_cur INTO v_contract_id,
    v_product_id,
    v_id,
    v_fee_type,
    v_charge_begin_date ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    -- 计算计费终了日非月末的日期
    SELECT 
      DATE_FORMAT(
        DATE_SUB(
          MAX(edp.charge_begin_date),
          INTERVAL 1 DAY
        ),
        '%Y-%m-%d'
      ) INTO v_charge_middle_date 
    FROM
      charge_period_temp edp 
    WHERE edp.batch_no = param_batch_no 
      AND edp.contract_id = v_contract_id 
      AND edp.product_id = v_product_id 
      AND edp.id = v_id 
      AND edp.fee_type = v_fee_type 
      AND edp.charge_end_date IS NOT NULL 
      AND DATE_FORMAT(edp.charge_end_date, '%Y%m') = DATE_FORMAT(v_charge_begin_date, '%Y%m') ;
    -- 更新记录中的计费终了时间
    -- 1.如果有月中的计费开始时间，取月中的开始日的前一天
    -- 2.如果没有月中的开始时间，取月末日
    UPDATE 
      charge_period_temp 
    SET
      charge_end_date = (
        CASE
          WHEN v_charge_middle_date IS NOT NULL 
          THEN v_charge_middle_date 
          ELSE LAST_DAY(charge_begin_date) 
        END
      ) 
    WHERE batch_no = param_batch_no 
      AND contract_id = v_contract_id 
      AND product_id = v_product_id 
      AND id = v_id 
      AND fee_type = v_fee_type 
      AND charge_begin_date = v_charge_begin_date ;
    -- 如果产品终了日处于正常的计费开始终了日之间，以产品终了日为计费终了日
    UPDATE 
      charge_period_temp 
    SET
      charge_end_date = (
        CASE
          WHEN prod_end_date >= charge_begin_date 
          AND prod_end_date <= charge_end_date 
          THEN DATE_FORMAT(prod_end_date, '%Y-%m-%d') 
          ELSE charge_end_date 
        END
      ) 
    WHERE batch_no = param_batch_no 
      AND contract_id = v_contract_id 
      AND product_id = v_product_id 
      AND id = v_id 
      AND fee_type = v_fee_type 
      AND charge_begin_date = v_charge_begin_date ;
  END LOOP cursor_loop ;
  CLOSE product_period_cur ;
  -- 关闭计费产品游标
  -- 删除不在计费期间的账单
  DELETE 
    t.* 
  FROM
    charge_period_temp t 
  WHERE batch_no = param_batch_no 
    AND EXISTS 
    (SELECT 
      1 
    FROM
      order_info ro 
    WHERE (
        t.charge_end_date < ro.order_begin_date 
        OR t.charge_begin_date > ro.order_end_date
      ) 
      AND ro.contract_id = t.contract_id 
      AND ro.del_flag = '0') ;
  -- 不在计费期间的账单2
  DELETE 
  FROM
    charge_period_temp 
  WHERE batch_no = param_batch_no 
    AND  charge_begin_date < prod_begin_date
    AND  charge_end_date > prod_begin_date;
    
  UPDATE 
    charge_period_temp 
  SET
    charge_end_date = prod_end_date 
  WHERE batch_no = param_batch_no 
    AND charge_end_date > prod_end_date
    AND charge_begin_date <= prod_end_date;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_gen_update_advance_payment` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_gen_update_advance_payment` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_gen_update_advance_payment`(param_batch_no VARCHAR (20))
BEGIN
  UPDATE 
    charge_bill_info dest 
    LEFT JOIN 
      (SELECT 
        cbi.batch_no,
        cbi.bill_id,
        cbi.order_id,
        cbi.combine_id,
        belong_type,
        SUM(
          CASE
            WHEN op.payment_type = '1' 
            THEN op.payment_amount 
            ELSE 0 
          END
        ) AS nonrecurring_payment,
        SUM(
          CASE
            WHEN op.payment_type = '2' 
            THEN op.payment_amount 
            ELSE 0 
          END
        ) AS fare_per_year,
        SUM(
          CASE
            WHEN op.payment_type = '4' 
            AND cbi.base_income_type = '1' 
            THEN op.payment_amount 
            ELSE NULL 
          END
        ) AS base_income 
      FROM
        order_paymentcycle op,
        charge_bill_info cbi 
      WHERE cbi.order_id = op.order_id
        AND cbi.batch_no = param_batch_no 
        AND cbi.belong_type = '2' 
        AND cbi.charge_begin_time <= op.payment_month 
        AND cbi.charge_end_time >= op.payment_month 
        AND op.`product_code` IN 
        (SELECT DISTINCT 
          product_sale_id 
        FROM
          ss_order_combine sre,
          ss_order_product srt 
        WHERE sre.bill_id = cbi.bill_id 
          AND srt.combine_id = cbi.combine_id 
          AND srt.bill_id = sre.bill_id 
          AND srt.combine_id = sre.combine_id) 
      GROUP BY cbi.batch_no,
        cbi.bill_id,
        cbi.order_id,
        cbi.combine_id) orgi 
      ON dest.batch_no = orgi.batch_no 
      AND dest.bill_id = orgi.bill_id 
      AND dest.order_id = orgi.order_id 
      AND dest.combine_id = orgi.combine_id 
      AND dest.belong_type = '2' SET dest.nonrecurring_payment = orgi.nonrecurring_payment,
    dest.fare_per_year = orgi.fare_per_year,
    dest.base_income = orgi.base_income 
  WHERE dest.batch_no = param_batch_no ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_insert_split_temp` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_insert_split_temp` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_insert_split_temp`(
  param_contract_id VARCHAR (20),
  param_batch_no VARCHAR (20)
)
BEGIN
  INSERT INTO charge_period_temp (
    id,
    bill_id,
    batch_no,
    contract_id,
    product_id,
    fee_type,
    prod_begin_date,
    prod_end_date,
    charge_begin_date,
    charge_end_date
  ) 
  (SELECT 
    U.id,
    LEFT(
      U.bill_id,
      LOCATE('-', U.bill_id) + 6
    ) AS bill_id,
    U.batch_no,
    U.contract_id,
    U.product_id,
    U.fee_type,
    U.prod_begin_date,
    U.prod_end_date,
    DATE_ADD(
      U.charge_begin_date,
      INTERVAL - DAY(U.charge_begin_date) + 1 DAY
    ) AS charge_begin_date,
    DATE_SUB(
      U.charge_begin_date,
      INTERVAL 1 DAY
    ) AS charge_end_date 
  FROM
    (SELECT 
      id,
      bill_id,
      batch_no,
      contract_id,
      product_id,
      fee_type,
      prod_begin_date,
      prod_end_date,
      CASE
        WHEN DAYOFMONTH(MIN(charge_begin_date)) != '1' 
        THEN charge_begin_date 
        ELSE 0 
      END charge_begin_date,
      charge_end_date 
    FROM
      charge_period_temp 
    WHERE contract_id = param_contract_id 
      AND batch_no = param_batch_no 
    GROUP BY DATE_FORMAT(charge_begin_date, '%Y%m') 
    ORDER BY charge_end_date) U 
    LEFT JOIN order_info ro 
      ON ro.contract_id = U.contract_id 
  WHERE U.charge_begin_date != 0 
    AND U.charge_begin_date > ro.order_begin_date) 
  UNION
  ALL 
  (SELECT 
    U.id,
    CONCAT(U.bill_id, '-odd') AS bill_id,
    U.batch_no,
    U.contract_id,
    U.product_id,
    U.fee_type,
    U.prod_begin_date,
    U.prod_end_date,
    DATE_SUB(
      U.charge_end_date,
      INTERVAL - 1 DAY
    ) AS charge_begin_date,
    LAST_DAY(U.charge_end_date) AS charge_end_date 
  FROM
    (SELECT 
      id,
      bill_id,
      batch_no,
      contract_id,
      product_id,
      fee_type,
      prod_begin_date,
      prod_end_date,
      charge_begin_date,
      CASE
        WHEN MAX(charge_end_date) != LAST_DAY(charge_end_date) 
        THEN charge_end_date 
        ELSE 0 
      END charge_end_date 
    FROM
      charge_period_temp 
    WHERE contract_id = param_contract_id 
      AND batch_no = param_batch_no 
    GROUP BY DATE_FORMAT(charge_end_date, '%Y%m') 
    ORDER BY charge_begin_date) U 
    LEFT JOIN order_info ro 
      ON ro.contract_id = U.contract_id 
  WHERE U.charge_end_date != 0 
    AND U.charge_end_date < ro.order_end_date) ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_insert_yearly_period_temp` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_insert_yearly_period_temp` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_insert_yearly_period_temp`(param_batch_no VARCHAR (40))
BEGIN
  DECLARE v_end_date,
  v_id,
  v_batch_no,
  v_product_id,
  v_fee_type,
  v_charge_begin_date,
  v_charge_end_date,
  v_bill_id VARCHAR (40) ;
  DECLARE v_contract_id,
  v_prod_begin_date,
  v_prod_end_date VARCHAR (60) ;
  DECLARE done INT DEFAULT 0 ;
  DECLARE order_cur CURSOR FOR 
  SELECT 
    contract_id,
    id,
    prod_begin_date,
    prod_end_date 
  FROM
    charge_period_temp 
  WHERE batch_no = param_batch_no 
    AND prod_end_date > fun_get_yearly_end_date (prod_begin_date) 
  GROUP BY contract_id,
    id ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  OPEN order_cur ;
  cursor_loop :
  LOOP
    FETCH order_cur INTO v_contract_id,
    v_id,
    v_prod_begin_date,
    v_prod_end_date ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    SET v_end_date = fun_get_yearly_end_date (v_prod_begin_date) ;
    WHILE
      v_end_date < v_prod_end_date DO 
      SELECT 
        id,
        batch_no,
        contract_id,
        product_id,
        fee_type,
        prod_begin_date,
        prod_end_date,
        charge_begin_date,
        charge_end_date,
        bill_id INTO v_id,
        v_batch_no,
        v_contract_id,
        v_product_id,
        v_fee_type,
        v_prod_begin_date,
        v_prod_end_date,
        v_charge_begin_date,
        v_charge_end_date,
        v_bill_id 
      FROM
        charge_period_temp 
      WHERE contract_id = v_contract_id 
        AND id = v_id 
        AND fun_get_yearly_end_date (prod_begin_date) > charge_begin_date 
        AND fun_get_yearly_end_date (prod_begin_date) < charge_end_date ;
      IF v_id IS NOT NULL 
      THEN 
      INSERT INTO charge_period_temp (
        id,
        bill_id,
        batch_no,
        contract_id,
        product_id,
        fee_type,
        prod_begin_date,
        prod_end_date,
        charge_begin_date,
        charge_end_date
      ) 
      SELECT 
        v_id,
        v_bill_id,
        v_batch_no,
        v_contract_id,
        v_product_id,
        v_fee_type,
        v_prod_begin_date,
        v_prod_end_date,
        v_charge_begin_date,
        v_end_date 
      UNION
      ALL 
      SELECT 
        v_id,
        CONCAT(v_bill_id, '-odd'),
        v_batch_no,
        v_contract_id,
        v_product_id,
        v_fee_type,
        v_prod_begin_date,
        v_prod_end_date,
        DATE_ADD(v_end_date, INTERVAL 1 DAY),
        v_charge_end_date ;
      DELETE 
      FROM
        charge_period_temp 
      WHERE batch_no = param_batch_no 
        AND bill_id = v_bill_id 
        AND id = v_id
        AND charge_begin_date = v_charge_begin_date 
        AND charge_end_date = v_charge_end_date ;
      END IF ;
      SET v_end_date = fun_get_yearly_end_date (
        DATE_ADD(v_end_date, INTERVAL 1 DAY)
      ) ;
    END WHILE ;
  END LOOP cursor_loop ;
  CLOSE order_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_order_addinter_data` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_order_addinter_data` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_order_addinter_data`()
BEGIN
  #定义订单产品相关参数
  DECLARE done INT DEFAULT 0 ;
  DECLARE v_order_id VARCHAR (40) ;
  DECLARE v_order_id1 VARCHAR (40) ;
  DECLARE v_order_ids VARCHAR (4000) ;
  DECLARE v_order_result VARCHAR (4000) ;
  DECLARE v_origin_order_id VARCHAR (40) ;
  DECLARE v_target_order_id VARCHAR (40) ;
  DECLARE count_num INT DEFAULT 0 ;
  DECLARE v_checkorder_id VARCHAR (65532) ;
  DECLARE v_check_count INT DEFAULT 0 ;
  #定义订单游标
  DECLARE order_cur CURSOR FOR 
  select 
    order_id,
    origin_order_id 
  from
    order_info 
  where is_change = '0' ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  #初始化数据
  SET SQL_SAFE_UPDATES = 0 ;
  #select order_id,origin_order_id from order_info where change_type='1';
  OPEN order_cur ;
  cart_loop :
  LOOP
    FETCH order_cur INTO v_order_id,
    v_origin_order_id ;
    SET v_order_ids = '' ;
    IF done 
    THEN LEAVE cart_loop ;
    END IF ;
    #select v_order_id;
    IF trim(v_origin_order_id) = '' 
    THEN 
    update 
      order_info 
    set
      inter_id = v_order_id 
    where order_id = v_order_id ;
    END IF ;
    SET v_order_ids = v_order_id ;
    WHILE
      trim(IFNULL(v_origin_order_id, '')) != '' DO 
      select 
        IFNULL(order_id, ''),
        IFNULL(origin_order_id, ''),
        count(*) into v_order_id1,
        v_origin_order_id,
        count_num 
      from
        order_info 
      where order_id = v_origin_order_id ;
      SET v_order_ids = concat(v_order_ids, ',', v_order_id1) ;
      if trim(IFNULL(v_origin_order_id, '')) = '' 
      then 
      update 
        order_info 
      set
        inter_id = v_order_id1 
      where instr(v_order_ids, order_id) > 0 ;
      #select concat(v_order_id1,'|',v_order_ids);
      end if ;
    END WHILE ;
    #select concat(v_order_id1,'|',v_order_ids);
  END LOOP cart_loop ;
  CLOSE order_cur ;
  select 
    group_concat(order_id),
    count(*) into v_checkorder_id,
    v_check_count 
  from
    order_info 
  where inter_id = '' ;
  if v_check_count = 0 
  then 
  select 
    '内部编号增加完成' ;
  end if ;
  if v_check_count >= 1 
  then 
  select 
    concat(
      '请人工处理以下订单',
      v_checkorder_id
    ) ;
  end if ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_order_mergedata` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_order_mergedata` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_order_mergedata`()
BEGIN
  #定义规格相关参数
  DECLARE v_batch_no INT DEFAULT FLOOR(RAND() * 10000) ;
  DECLARE done INT DEFAULT 0 ;
  DECLARE v_sku_id VARCHAR (20) ;
  DECLARE v_product_codestr VARCHAR (10) ;
  DECLARE v_in_product_code VARCHAR (10) ;
  DECLARE v_prod_name VARCHAR (100) ;
  DECLARE v_prod_option VARCHAR (10) ;
  DECLARE v_protion_name VARCHAR (200) ;
  DECLARE v_front_str VARCHAR (20) ;
  DECLARE v_end_str VARCHAR (20) ;
  DECLARE v_split_str VARCHAR (10) DEFAULT ',' ;
  DECLARE v_megre_str VARCHAR (8000) DEFAULT '' ;
  DECLARE i_split_str_length INT ;
  #定义订单产品相关参数
  DECLARE v_serial_no VARCHAR (40) ;
  DECLARE v_order_product_code VARCHAR (40) ;
  DECLARE v_order_procuct_parameters VARCHAR (40) ;
  DECLARE count_num INT DEFAULT 0 ;
  #定义购物车相关参数
  DECLARE v_cart_serial_no VARCHAR (40) ;
  DECLARE v_cart_code VARCHAR (40) ;
  DECLARE v_cart_parameters VARCHAR (40) ;
  DECLARE cartCount_num INT DEFAULT 0 ;
  #定义购物车游标
  DECLARE orderCart_cur CURSOR FOR 
  SELECT 
    a.serial_no,
    a.product_code,
    CASE
      WHEN product_time = 0 
      THEN CONCAT(param_desc, '/', prod_unit) 
      ELSE CONCAT(
        param_desc,
        ',',
        product_time,
        '个月'
      ) 
    END AS procuct_parameters 
  FROM
    order_cart a 
    JOIN prod_param b 
  WHERE a.product_code = b.product_code 
    AND a.param_value = b.param_value ;
  #定义订单产品游标
  DECLARE orderProduct_cur CURSOR FOR 
  SELECT 
    serial_no,
    product_code,
    CASE
      WHEN product_time = 0 
      THEN CONCAT(
        product_class_content,
        '/',
        prod_unit
      ) 
      ELSE CONCAT(
        product_class_content,
        ',',
        product_time,
        '个月'
      ) 
    END AS procuct_parameters 
  FROM
    order_product ;
  #定义产品规格选项游标
  DECLARE option_cursor CURSOR FOR 
  SELECT 
    serial_no,
    product_code,
    prod_option 
  FROM
    prod_price 
  WHERE prod_option != '' ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  #初始化数据
  SET SQL_SAFE_UPDATES = 0 ;
  DELETE 
  FROM
    merge_order_log ;
  UPDATE 
    order_cart 
  SET
    combine_type = '0',
    quantity = '1' 
  WHERE 1 = 1 ;
  UPDATE 
    order_product 
  SET
    combine_type = '0',
    quantity = '1' 
  WHERE 1 = 1 ;
  UPDATE 
    order_info 
  SET
    deal_status = '8',
    order_source = '1' 
  WHERE 1 = 1 ;
  OPEN option_cursor ;
  cursor_loop :
  LOOP
    FETCH option_cursor INTO v_sku_id,
    v_product_codestr,
    v_prod_option ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    SET v_megre_str = '' ;
    SET v_front_str = SUBSTRING(
      v_prod_option,
      1,
      LOCATE(v_split_str, v_prod_option) - 1
    ) ;
    SELECT 
      option_name INTO v_prod_name 
    FROM
      spec_option sop 
      JOIN specifications spec 
        ON sop.spec_id = spec.spec_id 
    WHERE option_id = v_front_str ;
    SET v_megre_str = CONCAT(v_megre_str, v_prod_name, ',') ;
    SET v_end_str = SUBSTRING(
      v_prod_option,
      1+ LOCATE(v_split_str, v_prod_option)
    ) ;
    SELECT 
      option_name INTO v_prod_name 
    FROM
      spec_option sop 
      JOIN specifications spec 
        ON sop.spec_id = spec.spec_id 
    WHERE option_id = v_end_str ;
    SET v_megre_str = CONCAT(v_megre_str, v_prod_name) ;
    INSERT merge_prodtion 
    VALUES
      (
        v_sku_id,
        v_product_codestr,
        v_prod_option,
        v_megre_str
      ) ;
    COMMIT ;
  END LOOP cursor_loop ;
  CLOSE option_cursor ;
  SET done = 0 ;
  #订单产品数据合并
  OPEN orderProduct_cur ;
  order_loop :
  LOOP
    FETCH orderProduct_cur INTO v_serial_no,
    v_order_product_code,
    v_order_procuct_parameters ;
    IF done 
    THEN LEAVE order_loop ;
    END IF ;
    SELECT 
      COUNT(*),
      sku_id INTO count_num,
      v_sku_id 
    FROM
      merge_prodtion 
    WHERE product_code = v_order_product_code 
      AND prod_name = v_order_procuct_parameters ;
    #select count_num;
    IF(count_num >= 1) 
    THEN 
    UPDATE 
      order_product 
    SET
      prod_price_id = v_sku_id 
    WHERE serial_no = v_serial_no ;
    INSERT merge_order_log 
    VALUES
      (
        v_batch_no,
        NOW(),
        v_serial_no,
        v_order_product_code,
        v_order_procuct_parameters,
        '订单产品更新成功'
      ) ;
    COMMIT ;
    ELSE 
    INSERT merge_order_log 
    VALUES
      (
        v_batch_no,
        NOW(),
        v_serial_no,
        v_order_product_code,
        v_order_procuct_parameters,
        '订单产品更新不成功,请检查'
      ) ;
    COMMIT ;
    END IF ;
  END LOOP order_loop ;
  CLOSE orderProduct_cur ;
  SET done = 0 ;
  #订单产品数据合并
  OPEN orderCart_cur ;
  cart_loop :
  LOOP
    FETCH orderCart_cur INTO v_cart_serial_no,
    v_cart_code,
    v_cart_parameters ;
    IF done 
    THEN LEAVE cart_loop ;
    END IF ;
    SELECT 
      COUNT(*),
      sku_id INTO cartCount_num,
      v_sku_id 
    FROM
      merge_prodtion 
    WHERE product_code = v_cart_code 
      AND prod_name = v_cart_parameters ;
    #select cartCount_num;
    IF(cartCount_num >= 1) 
    THEN 
    UPDATE 
      order_cart 
    SET
      prod_price_id = v_sku_id 
    WHERE serial_no = v_serial_no ;
    INSERT merge_order_log 
    VALUES
      (
        v_batch_no,
        NOW(),
        v_cart_serial_no,
        v_cart_code,
        v_order_procuct_parameters,
        '购物车更新成功'
      ) ;
    COMMIT ;
    ELSE 
    INSERT merge_order_log 
    VALUES
      (
        v_batch_no,
        NOW(),
        v_cart_serial_no,
        v_cart_code,
        v_order_procuct_parameters,
        '购物车更新不成功,请检查'
      ) ;
    COMMIT ;
    END IF ;
  END LOOP cart_loop ;
  CLOSE orderCart_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_order_set_split_retio` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_order_set_split_retio` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_order_set_split_retio`(param_contract_id VARCHAR (40))
BEGIN
  -- 删除既存的合同收入拆分比例
  DELETE 
  FROM
    order_income_setting 
  WHERE contract_id = param_contract_id 
    OR param_contract_id IS NULL ;
  -- 根据合同组合信息与固定费用的预付，技术服务费生成收入拆分记录
  INSERT INTO order_income_setting (
    id,
    contract_id,
    payment_type,
    product_id,
    ex_product_id,
    income_begin_date,
    income_end_date,
    split_ratio,
    del_flag
  ) -- 非技术服务费
  SELECT 
    REPLACE(UUID(), '-', ''),
    re.contract_id,
    op.payment_type,
    rt.product_id,
    rt.ex_product_id,
    re.combine_begin_date,
    re.combine_end_date,
    CASE
      WHEN 
      (SELECT 
        COUNT(DISTINCT op.product_id) 
      FROM
        order_product op 
      WHERE op.contract_id = re.contract_id 
        AND op.combine_id = re.id) = 1 -- 且只有一种产品的,比例设为100%
      THEN 100 -- 多种产品的,比例设为100%
      ELSE NULL 
    END ,
    '0'
  FROM
    order_combine re,
    order_adv_payment op,
    order_product rt 
  WHERE op.contract_id = re.contract_id 
    AND op.combine_id = re.id 
    AND rt.contract_id = re.contract_id 
    AND rt.combine_id = re.id 
    AND re.del_flag = '0'
    AND op.del_flag = '0'
    AND rt.del_flag = '0'
    AND (
      re.contract_id = param_contract_id 
      OR param_contract_id IS NULL
    ) 
    AND op.payment_type != '10000' 
  GROUP BY re.contract_id,
    re.id,
    op.payment_type,
    rt.product_id 
  UNION
  ALL -- 技术服务费、且只有一条产品的
  SELECT 
    REPLACE(UUID(), '-', ''),
    re.contract_id,
    '12475',
    rt.product_id,
    rt.ex_product_id,
    re.combine_begin_date,
    re.combine_end_date,
    CASE
      WHEN 
      (SELECT 
        COUNT(DISTINCT op.product_id) 
      FROM
        order_product op 
      WHERE op.contract_id = re.contract_id 
        AND op.combine_id = re.id) = 1 -- 且只有一种产品的,比例设为100%
      THEN 100 -- 多种产品的,比例设为100%
      ELSE NULL 
    END ,
    '0'
  FROM
    order_combine re,
    order_product rt 
  WHERE rt.contract_id = re.contract_id 
    AND rt.combine_id = re.id 
    AND re.del_flag = '0'
    AND rt.del_flag = '0'
    AND re.tech_charge_income_flag = '1'
    AND (
      re.contract_id = param_contract_id 
      OR param_contract_id IS NULL
    ) 
  GROUP BY re.contract_id,
    re.id,
    rt.product_id ;
  -- 更新合同的收入设置状态为未确认
  UPDATE 
    order_info ro 
  SET
    income_status = 
    CASE
      WHEN IFNULL(
        (SELECT 
          COUNT(1) 
        FROM
          order_income_setting reg 
        WHERE reg.contract_id = ro.contract_id 
          AND (
            reg.split_ratio != 100 
            OR reg.split_ratio IS NULL
          )),
        0
      ) > 0 
      THEN '0' 
      WHEN IFNULL(
        (SELECT 
          COUNT(1) 
        FROM
          order_income_setting reg 
        WHERE reg.contract_id = ro.contract_id 
          AND reg.`payment_type` IN ('12471', '12474')),
        0
      ) > 0 
      THEN '0' 
      ELSE '1' 
    END 
  WHERE contract_id = param_contract_id 
    OR param_contract_id IS NULL ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_test_findorder_data` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_test_findorder_data` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_test_findorder_data`()
BEGIN
  #定义订单产品相关参数
  DECLARE done INT DEFAULT 0 ;
  DECLARE v_order_id VARCHAR (40) ;
  DECLARE v_order_id1 VARCHAR (40) ;
  DECLARE v_order_ids VARCHAR (4000) ;
  #DECLARE v_order_result varchar(4000);
  DECLARE v_origin_order_id VARCHAR (40) ;
  DECLARE v_target_order_id VARCHAR (40) ;
  DECLARE count_num INT DEFAULT 0 ;
  DECLARE cnt INT ;
  DECLARE i INT ;
  DECLARE result VARCHAR (4000) ;
  #定义订单游标
  DECLARE order_cur CURSOR FOR 
  SELECT 
    order_id,
    origin_order_id 
  FROM
    order_info 
  WHERE is_change = '0' ;
  -- and order_id='20160128171135000049';
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  #初始化数据
  SET SQL_SAFE_UPDATES = 0 ;
  #select order_id,origin_order_id from order_info where change_type='1';
  OPEN order_cur ;
  cart_loop :
  LOOP
    FETCH order_cur INTO v_order_id,
    v_origin_order_id ;
    SET v_order_ids = '' ;
    IF done 
    THEN LEAVE cart_loop ;
    END IF ;
    SET v_order_ids = v_order_id ;
    WHILE
      TRIM(v_origin_order_id) != '' DO 
      SELECT 
        order_id,
        origin_order_id INTO v_order_id1,
        v_origin_order_id 
      FROM
        order_info 
      WHERE order_id = v_origin_order_id ;
      SET v_order_ids = CONCAT(v_order_ids, ',', v_order_id1) ;
      SELECT 
        CONCAT(v_order_id1, '|', v_order_ids) ;
    END WHILE ;
  END LOOP cart_loop ;
  CLOSE order_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_test_order_data` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_test_order_data` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_test_order_data`()
BEGIN
  #定义订单产品相关参数
  DECLARE done INT DEFAULT 0 ;
  DECLARE v_order_id VARCHAR (40) ;
  DECLARE v_order_id1 VARCHAR (40) ;
  DECLARE v_order_ids VARCHAR (4000) ;
  DECLARE v_order_result VARCHAR (4000) ;
  DECLARE v_origin_order_id VARCHAR (40) ;
  DECLARE v_target_order_id VARCHAR (40) ;
  DECLARE count_num INT DEFAULT 0 ;
  DECLARE cnt INT ;
  DECLARE i INT ;
  DECLARE result VARCHAR (4000) ;
  #定义订单游标
  DECLARE order_cur CURSOR FOR 
  select 
    order_id,
    origin_order_id 
  from
    order_info1 
  where is_change = '0' ;
  #and order_id='20160114143705000009';
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  #初始化数据
  SET SQL_SAFE_UPDATES = 0 ;
  #select order_id,origin_order_id from order_info1 where change_type='1';
  OPEN order_cur ;
  cart_loop :
  LOOP
    FETCH order_cur INTO v_order_id,
    v_origin_order_id ;
    SET v_order_ids = '' ;
    IF done 
    THEN LEAVE cart_loop ;
    END IF ;
    #select v_order_id;
    IF trim(v_origin_order_id) = '' 
    THEN 
    update 
      order_info1 
    set
      inter_id = v_order_id 
    where order_id = v_order_id ;
    END IF ;
    SET v_order_ids = v_order_id ;
    WHILE
      trim(v_origin_order_id) != '' DO 
      select 
        order_id,
        origin_order_id into v_order_id1,
        v_origin_order_id 
      from
        order_info1 
      where order_id = v_origin_order_id ;
      SET v_order_ids = concat(v_order_ids, ',', v_order_id1) ;
      #if trim(v_origin_order_id)='' then    
      #end if; 
      update 
        order_info1 
      set
        inter_id = v_order_id1 
      where instr(v_order_ids, order_id) > 0 ;
    END WHILE ;
    #set v_order_result=concat(v_order_result,';',concat(v_order_id1,'|',v_order_ids));
    #select v_order_result;
    select 
      concat(v_order_id1, '|', v_order_ids) ;
  END LOOP cart_loop ;
  select 
    v_order_result ;
  CLOSE order_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_val_customer_monthly_score` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_val_customer_monthly_score` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_val_customer_monthly_score`(
  param_office_id VARCHAR (40),
  param_contract_id VARCHAR (40)
)
BEGIN
  -- 合同编号（用于提高性能）
  DECLARE v_contract_id VARCHAR (40) ;
  -- 统计年月
  DECLARE v_val_date VARCHAR (10) ;
  DECLARE v_last_growth_amount,
  v_total_growth_amount,
  v_bankreceipt,
  v_total_charge,
  v_total_min_service_charge,
  v_total_advance_charge DECIMAL (25, 8) ;
  DECLARE done INT DEFAULT 0 ;
  DECLARE contract_cur CURSOR FOR 
  SELECT 
    val_date,
    contract_id 
  FROM
    val_cust_score 
  ORDER BY contract_id,
    val_date DESC ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  -- 删除导出年度的数据
  DELETE 
  FROM
    val_cust_score ;
  INSERT INTO val_cust_score (
    contract_id,
    val_type,
    val_date,
    growth_amount
  ) 
  SELECT 
    elo.contract_id AS '协同合同号',
    '1' AS '评估类型:合同价值',
    DATE_FORMAT(elo.charge_begin_date, '%Y%m'),
    SUM(elo.`service_charge`) AS '本期技术服务费' 
  FROM
    charge_bill_info elo,
    order_info ro 
  WHERE ro.contract_id = elo.contract_id 
    AND (
      param_office_id IS NULL 
      OR ro.office_id = param_office_id
    ) 
    AND elo.belong_type = '3' 
    AND (
      param_contract_id IS NULL 
      OR elo.contract_id = param_contract_id
    ) 
  GROUP BY elo.contract_id,
    DATE_FORMAT(elo.charge_begin_date, '%Y%m') ;
  OPEN contract_cur ;
  cursor_loop :
  LOOP
    FETCH contract_cur INTO v_val_date,
    v_contract_id ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    -- 获取上月技术服务费增长值
    SET v_last_growth_amount = 
    (SELECT 
      IFNULL(MAX(growth_amount), 0) 
    FROM
      val_cust_score 
    WHERE contract_id = v_contract_id 
      AND val_date = DATE_FORMAT(
        DATE_SUB(
          CONCAT(v_val_date, '01'),
          INTERVAL 1 MONTH
        ),
        '%Y%m'
      )) ;
    -- 根据每月技术服务费计算累计技术服务费
    SET v_total_growth_amount = 
    (SELECT 
      IFNULL(SUM(growth_amount), 0) 
    FROM
      val_cust_score 
    WHERE contract_id = v_contract_id 
      AND val_date <= v_val_date) ;
    -- 累计到款
    SET v_bankreceipt = 
    (SELECT 
      IFNULL(SUM(bankreceipt_amount), 0) 
    FROM
      charge_bankreceipt 
    WHERE contract_id = v_contract_id 
      AND DATE_FORMAT(bankreceipt_month, '%Y%m') <= v_val_date) ;
    -- 累计发生费用
    SELECT 
      IFNULL(MAX(total_service_charge), 0) + IFNULL(MAX(total_advance_charge), 0),
      IFNULL(MAX(total_service_charge), 0),
      IFNULL(MAX(total_advance_charge), 0) INTO v_total_charge,
      v_total_min_service_charge,
      v_total_advance_charge 
    FROM
      charge_order_info 
    WHERE contract_id = v_contract_id 
      AND DATE_FORMAT(bill_display_date, '%Y%m') = v_val_date ;
    -- 更新估值表的累计技术服务费
    UPDATE 
      val_cust_score 
    SET
      total_service_charge = v_total_growth_amount,
      growth_percent = growth_amount / v_last_growth_amount - 1,
      total_bankreceipt = v_bankreceipt,
      total_charge = v_total_charge,
      total_min_service_charge = v_total_min_service_charge,
      total_advance_charge = v_total_advance_charge 
    WHERE contract_id = v_contract_id 
      AND val_date = v_val_date ;
  END LOOP cursor_loop ;
  CLOSE contract_cur ;
END */$$
DELIMITER ;

/* Procedure structure for procedure `sp_val_product_charge_score` */

/*!50003 DROP PROCEDURE IF EXISTS  `sp_val_product_charge_score` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`%` PROCEDURE `sp_val_product_charge_score`(
  param_office_id VARCHAR (40),
  param_product_id VARCHAR (40)
)
BEGIN
  -- 产品编号
  DECLARE v_product_id VARCHAR (40) ;
  -- 统计年月
  DECLARE v_val_date VARCHAR (10) ;
  DECLARE v_last_growth_amount,
  v_total_growth_amount DECIMAL (25, 8) ;
  DECLARE done INT DEFAULT 0 ;
  DECLARE product_cur CURSOR FOR 
  SELECT 
    product_id,
    val_date 
  FROM
    val_product_charge_score 
  ORDER BY product_id,
    val_date DESC ;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1 ;
  -- 删除导出年度的数据
  DELETE 
  FROM
    val_product_charge_score ;
  INSERT INTO val_product_charge_score (
    office_id,
    product_id,
    val_type,
    val_date,
    growth_amount
  ) 
  SELECT 
    '',
    elo.product_id AS '协同合同号',
    '1' AS '评估类型:产品计费价值',
    DATE_FORMAT(elo.charge_begin_date, '%Y%m'),
    SUM(elo.`service_charge`) AS '本期技术服务费' 
  FROM
    charge_bill_info elo 
  WHERE elo.belong_type = '3' 
    AND (
      param_product_id IS NULL 
      OR elo.product_id = param_product_id
    ) 
  GROUP BY elo.product_id,
    DATE_FORMAT(elo.charge_begin_date, '%Y%m') ;
  OPEN product_cur ;
  cursor_loop :
  LOOP
    FETCH product_cur INTO v_val_date,
    v_product_id ;
    IF done = 1 
    THEN LEAVE cursor_loop ;
    END IF ;
    -- 获取上月技术服务费增长值
    SET v_last_growth_amount = 
    (SELECT 
      IFNULL(MAX(growth_amount), 0) 
    FROM
      val_product_charge_score 
    WHERE product_id = v_product_id 
      AND val_date = DATE_FORMAT(
        DATE_SUB(
          CONCAT(v_val_date, '01'),
          INTERVAL 1 MONTH
        ),
        '%Y%m'
      )) ;
    -- 根据每月技术服务费计算累计技术服务费
    SET v_total_growth_amount = 
    (SELECT 
      IFNULL(SUM(growth_amount), 0) 
    FROM
      val_product_charge_score 
    WHERE product_id = v_product_id 
      AND val_date <= v_val_date) ;
    -- 更新估值表的累计技术服务费
    UPDATE 
      val_product_charge_score 
    SET
      total_growth_amount = v_total_growth_amount,
      growth_percent = growth_amount / v_last_growth_amount - 1 
    WHERE product_id = v_product_id 
      AND val_date = v_val_date ;
  END LOOP cursor_loop ;
  CLOSE product_cur ;
END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
