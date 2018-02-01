
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html;" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	<style type="text/css">
	${a4landscape}
	${acclayout}
	</style>
	</head>
	<body>
		<div class="order_gb">
            <h2 class="title">${billAccount.user_name}${billAccount.bill_date}账单</h2>
            <div class="content">
                <h3>账户信息</h3>
                <div class="clearfix">
                    <div class="comp_name">${billAccount.user_name}</div>
                    <div class="order_time">账单日期：${billAccount.charge_begin_time}－${billAccount.charge_end_time}</div>
                </div>
                <ul class="qgray clearfix">
                    <li>
                        <p>本月账户余额：</p>
                        <p class="num">¥ ${billAccount.current_month_balance?number?string(',##0.00')}</p>
                    </li>
                    <li>
                        <p>本期应付：</p>
                        <p class="num">¥ ${billAccount.payable?number?string(',##0.00')}</p>
                    </li>
                    <li>
                        <p>最迟付款日：</p>
                        <p class="num">${billAccount.payment_deadline}</p>
                    </li>
                </ul>
                <div class="clearfix">
                    <div class="qgray_d">付款方式</div>
                    <ul class="qgray_l">
                        <li>收款方： 杭州证投网络科技有限公司</li>
                        <li>开户银行： 招商银行杭州分行营业部</li>
                        <li>银行账号：  5719 0910 6810 608</li>
                    </ul>
                </div>
                <h3>费用详情</h3>
                <table class="order_gb_table">
                	<tbody>
                	<tr>
						<td>上月余额：</td>																
						<td>¥ ${billAccount.last_month_balance?number?string(',##0.00')}</td> 
					</tr>
					<tr>
						<td>本期技术服务费：</td>																
						<td>¥ ${feeDetail.tech_service_fare?number?string(',##0.00')}</td> 
					</tr>
					<tr>
						<td>未付预付款：</td>																
						<td>¥ ${feeDetail.unpaid_payment?number?string(',##0.00')}</td> 
					</tr>
					<tr>
						<td>本月余额：</td>																
						<td>¥ ${billAccount.current_month_balance?number?string(',##0.00')}</td> 
					</tr>
					<tr>
						<td>本期应付：</td>																
						<td>¥ ${billAccount.payable?number?string(',##0.00')}</td> 
					</tr>
					</tbody>
                </table>
                <h3>套餐及计费信息</h3>
				<table class="order_gb_table">
					<#if paymentModel=="1">			
						<thead>
							<tr>
								<th width="20%">组合名称</th>
								<th width="20%">产品名称</th>
								<th width="20%">产品规格</th>
								<th width="20%">折扣（%）</th>					
								<th width="20%">应计技术服务费</th>
							</tr>
						</thead>
						<tbody>
						 <#list combFeeList as subBillBean>
							<#if subBillBean.count == "1">
							<tr>
								<td>${subBillBean.combine_name}</td>
								<td>${subBillBean.product_name}</td>
								<td>${subBillBean.spec_name}</td>
								<td>${subBillBean.discount}</td>																
								<td>¥ ${subBillBean.accrued_tech_service_fare?number?string(',##0.00')}</td> 
							</tr>  
							</#if>
							<#if subBillBean.count != "1" && subBillBean.count != "0">
							<tr>
								<td rowspan="${subBillBean.count}">${subBillBean.combine_name}</td>
								<td>${subBillBean.product_name}</td>
								<td>${subBillBean.spec_name}</td>
								<td rowspan="${subBillBean.count}">${subBillBean.discount}</td>
								<td rowspan="${subBillBean.count}">¥ ${subBillBean.accrued_tech_service_fare?number?string(',##0.00')}</td> 
							</tr>
							</#if> 
							<#if subBillBean.count == "0">
							<tr>
								<td>${subBillBean.product_name}</td>
								<td>${subBillBean.spec_name}</td>       						
							</tr>
							</#if>
						</#list>
						</tbody>
					<#else>
						<thead>
							<tr>
								<th width="8%">组合名称</th>
								<th width="10%">产品名称</th>
								<th width="10%">产品规格</th>
								<th width="10%">产品费率(‰)</th>
								<th width="10%">技术服务费</th>
								<th width="12%">保底</th>
								<th width="8%">折扣（%）</th>        							
								<th width="14%">当年累计技术服务费</th>        						
								<th width="8%">补足</th>
								<th width="10%">应计技术服务费</th>
							</tr>
						</thead>
						<tbody>
						<#list combFeeList as subBillBean>
							<#if subBillBean.count == "1">
							<tr>
								<td>${subBillBean.combine_name}</td>
								<td>${subBillBean.product_name}</td>
								<td>${subBillBean.spec_name}</td>
								<td>${subBillBean.fee_ratio}</td>
								<td>¥ ${subBillBean.tech_service_fare?number?string(',##0.00')}</td>
								<td>¥ ${subBillBean.min_consume?number?string(',##0.00')}/${subBillBean.min_type_name}</td>
								<td>${subBillBean.discount}</td> 
								<#if subBillBean.min_type == "2">
									<td>--</td>
									<td>--</td>
								<#else>
									<td>¥ ${subBillBean.sum_account?number?string(',##0.00')}</td>							
									<td>¥ ${subBillBean.makeup_account?number?string(',##0.00')}</td>	
								</#if>	
								<td>¥ ${subBillBean.accrued_tech_service_fare?number?string(',##0.00')}</td> 
							</tr>  
							</#if>
						
							<#if subBillBean.count!= "1" && subBillBean.count != "0">
							<tr>
								<td rowspan="${subBillBean.count}">${subBillBean.combine_name}</td>
								<td>${subBillBean.product_name}</td>
								<td>${subBillBean.spec_name}</td>
								<td>${subBillBean.fee_ratio}</td>
								<td>¥ ${subBillBean.tech_service_fare?number?string(',##0.00')}</td>
								<td rowspan="${subBillBean.count}">¥ ${subBillBean.min_consume?number?string(',##0.00')}/${subBillBean.min_type_name}</td>
								<td rowspan="${subBillBean.count}">${subBillBean.discount}</td>
								<#if subBillBean.min_type == "2">
									<td rowspan="${subBillBean.count}">--</td>
									<td rowspan="${subBillBean.count}">--</td>
								<#else>
									<td rowspan="${subBillBean.count}">¥ ${subBillBean.sum_account?number?string(',##0.00')}</td>        							
									<td rowspan="${subBillBean.count}">¥ ${subBillBean.makeup_account?number?string(',##0.00')}</td>	
								</#if>								
								<td rowspan="${subBillBean.count}">¥ ${subBillBean.accrued_tech_service_fare?number?string(',##0.00')}</td>  
							</tr>
							</#if>
							<#if subBillBean.count == "0">
							<tr>
								<td>${subBillBean.product_name}</td>
								<td>${subBillBean.spec_name}</td>
								<td>${subBillBean.fee_ratio}</td>
								<td>¥ ${subBillBean.tech_service_fare?number?string(',##0.00')}</td>
							</tr>
							</#if>
						</#list>
						</tbody>
					</#if>
                </table>
                <table class="order_gb_table">
                <tfoot>					
					<#if collect_cycle == "2" && season_paid_flag == "1">
						<tr><td><p>本月技术服务费：  ¥ ${sumCombFee?number?string(',##0.00')}</p></td></tr>
						<tr><td><p>本季累计技术服务费：  ¥ ${season_paid?number?string(',##0.00')}</p></td></tr>
					<#else>
						<tr><td><p>本月技术服务费：  ¥ ${sumCombFee?number?string(',##0.00')}</p></td></tr>
					</#if>
				</tfoot>	
				</table>
             
             <#if paymentModel!="1">	
                <h3>客户交易明细</h3>
                <#list custTradeDetailMap?keys as key>
                <#assign products = custTradeDetailMap[key]>

					<#if key?substring(0,1)=="2">
					  <h4>${key?substring(1)}</h4>
					   <table class="order_gb_table">
						<thead>
                        <tr>
                            <th>序号</th>
                            <th>日期</th>
                            <th>资产规模</th>
                            <th>技术服务费</th>
                        </tr>
						</thead>
					
						<tbody>
						<#if products??>
						<#list products?keys as product >	
							<#if products[product]??>
							<#list products[product] as detail >
							<tr>
								<td>${detail_index?if_exists+1}</td>
								<td>${detail.date}</td>
								<td>${detail.asset_balance}</td>
								<td>¥ ${detail.tech_service_fare?number?string(',##0.00')}</td>
							</tr>
							</#list>
							</#if>
							<tr>
								<td>合计</td>
								<td></td>
								<td></td> 
								<td>¥ ${product?number?string(',##0.00')}</td>					 
							</tr>
						</#list>
						</#if>
						</tbody>
						
					</table>
					
					<#elseif key?substring(0,1)=="3"||key?substring(0,1)=="4">
					  <h4>${key?substring(1)}</h4>
					   <table class="order_gb_table">
						<thead>
                        <tr>
                            <th>序号</th>
                            <th>日期</th>
                            <th>委托笔数</th>
                            <th>委托金额</th>
                            <th>成交笔数</th>
                            <th>成交金额</th>
                            <th>技术服务费</th>
                        </tr>
						</thead>
						
						<tbody>
						<#if products??>
						<#list products?keys as product >	
							<#if products[product]??>
							<#list products[product] as detail >
							<tr>
								<td>${detail_index?if_exists+1}</td>
								<td>${detail.date}</td>
								<td>${detail.entrust_count}</td>
								<td>${detail.entrust_balance}</td>
								<td>${detail.business_count}</td>
								<td>${detail.business_balance}</td>
								<td>${detail.tech_service_fare}</td>
							</tr>
							</#list>
							</#if>
							<tr>
								<td>合计</td>
								<td></td>
								<#list product?split(";") as element>
									<td>${element}</td>					 
								</#list>					 
							</tr>
						</#list>
						</#if>
						</tbody>
					</table>

					</#if>
			  	</#list>
			</#if>	
			<#if paymentModel!="1">
               <div class="z_title">--说明--</div>
			     <ul class="zd_zn"><li>补足（年保底需补足）：</li>		
				 <div class="normal_pro">
				    <p>(1)保底（年）需补足，一个计费年最后一个账单月进行计算</p>
				    <p>(2)当年累计技术服务费 &lt; 保底（年）时，补足 = （保底（年）- 当年累计技术服务费）&gt; 0 </p>
				    <p>(3)当年累计技术服务费  ≥ 保底（年）时，补足 =  0 </p>
				 </div>
		        <li>页面计费金额单位统一为：元</li>
				</ul> 
			<#else>
				<div class="z_title">--说明--</div>
			     <ul class="zd_zn">
		         <li>页面计费金额单位统一为：元</li>
				</ul> 
			</#if>		
			<div class="z_title">--账单服务指南--</div>
			<ul class="zd_zn">
				<li>HAMP投资管理平台 7*24小时客户服务热线：0571-28829999（转6）</li>
				<li>请您留意账单送达日期提示，如您在每月账单最后送达之日前尚未收到，可拨打客服热线咨询。</li>
				<li>您收到账单后，请按照合同约定付费日期在限定日期范围内付费。付费后请妥善保管付费凭证，以备核查。</li>
				<li>为保证账单每月能及时准确寄送给您，请在电子账单地址变更时（投递地址变更时），及时致电客户服务热线，或通过服务邮箱提前办理地址变更（含电子账单地址）。</li>
				<li>欢迎访问HAMP金融投资管理平台：http://www.ihoms.com，我们将竭诚为您服务！</li>
				<li>联系地址：杭州市滨江区江南大道3588号恒生大厦7楼 </li>
				<li>邮编：310053</li>
			</ul>
			<div class="z_title">--常见问题--</div>
			<div class="normal_pro">
				<div class="t_title">1. 交易系统里的成交金额和账单上不一致？</div>
				<p>A. 该操作员只有部分账户权限，或者查询时没有选择全部账户。</p>
				<p>B. 证券类别选择不正确，账单里的成交金额包括股票、封闭式基金、开放式基金（指在交易所买卖），部分用户还包括债券。</p>
				<p>C. 查询日期是否正确。</p>
				<p>D. 查询结果存在分页情况。</p>
				<div class="t_title">2. 已经打款为何账单上没有体现？</div>
				<p>A. 没有正式签订合同，打款记录我司财务无法入账，待合同签订后会入账，会体现在下个月的账单上。</p>
				<p>B. 打款人和合同签订人不是同一个，我司财务无法入账，可以联系客户经理协调解决，打款金额体现在下个月账单上。</p>
				<p>C. 不是账单月月底前打款，这部分打款记录会体现在下月账单上。</p>
				<div class="t_title">3. 如何调整我的套餐？</div>
				<p>可以通过邮件向我司客户经理或者服务邮箱发送套餐变更申请，我司接收到套餐变更申请后的2个工作日内会与贵司确定套餐变更事宜。</p>
				<p>原则上，新套餐生效日期为申请的下月第一个交易日开始生效。如有特殊情况，需要调整本月套餐的，可以在收到账单的3个工作日内向客户经理发起申请，需要由</p>
				<p>客户经理在公司内部提交特批申请后才可以调整本月套餐。</p>
			</div>
            </div>	

		</div>
	</body>
</html>

