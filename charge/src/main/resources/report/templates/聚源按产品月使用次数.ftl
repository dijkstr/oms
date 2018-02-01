
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
	<style type="text/css">
	${a4landscape}
	${accountstyle}
	</style>
</head>
<body>
	<div class="h_totalwidth">
		<div class="h_screen">
			<div class="order_gb">
				<h2 class="title">${title}</h2>
				<div class="content">
					<h3>账户信息</h3>
					<div class="clearfix">
						<div class="comp_name">${custname}</div>
						<div class="order_time">${receiveduration}</div>
					</div>
					<table class="qgray_table">
						<tr>
							<td width="20%" align="left">计费方式：</td>
							<td width="20%" align="left">账号状态：</td>
							<td width="20%" align="left">上期余额：</td>
							<td width="20%" align="left">本期应付：</td>
							<td width="20%" align="left">本期余额</td>
						</tr>
						<tr>
							<td align="left">${billtype}</td>
							<td align="left">${accountstatus}</td>
							<td align="left">${lastbalance}</td>
							<td align="left">${receiveamt}</td>
							<td align="left">${currentbalance}</td>
						</tr>
					</table>

					<table class="qgray_table" style="background: #e4e4e4;">
						<tr>
							<td width="10%">付款方式</td>
							<td width="10%">收款方：</td>
							<td width="20%">上海恒生聚源数据服务有限公司</td>
							<td width="10%">开户银行：</td>
							<td width="20%">招商银行上海大宁支行</td>
							<td width="10%">银行账号:</td>
							<td width="20%">0219 0047 1710 201</td>								
						</tr>
					</table>
					<h4>访问明细</h4>
					<table class="gridtable">
						<thead>
							<tr>
								<th width="10%">序号</th>
								<th width="45%">接口名称</th>
								<th width="45%">请求成功次数</th>
								<th width="45%">接口单价(元/次)</th>
								<th width="45%">费用(元)</th>
							</tr>
						</thead>
						<#assign total_charge_amount=0.0 />
						<tbody>
							<#list griddata as gridata>
							<tr>
								<td>${gridata_index?if_exists+1}</td>
								<td>${gridata.api_chi_name}</td>
								<td>${gridata.charge_value}</td>
								<td>${gridata.extra_price}</td>
								<td>${gridata.charge_amount}</td>
								<#assign total_charge_amount=total_charge_amount+gridata.charge_amount />					
							</tr>
							</#list>
						</tbody>
						<tfoot>
							<tr>
								<td colspan="4" style="text-align:right">本期总技术服务费</td>
								<td>${total_charge_amount}</td>						
							</tr>
						</tfoot>
					</table>
					
					<div class="z_title">--账单服务指南--</div>
					<ul class="zd_zn">
						<li>――账单服务指南――</li>
						<li>•为保证账单每月能及时准确地寄送给您，请在电子账单地址变更时，及时致电客户服务热线，或通过客户服务邮箱提前办理地址变更。</li>
						<li>•联系地址：上海市浦东新区峨山路91弄61号陆家嘴软件园10号楼7-8楼 邮编：200127</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
