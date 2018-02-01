<#if form ??>
	<tr class="order_model_tr">
		<td>
    		<div class="specsHead">
    			<span>计费模式</span>
        		<input class="table_input_string" name="feemodel_name" type="text" value="${form.model_name}" readonly="readonly" onclick="modelChooserShow(this)">
    		</div>
    		<input name="fee_type" type="hidden" value="${form.fee_type}">
    		<input name="fee_formula" type="hidden" value="${form.fee_formula}">
    		<input name="prod_orderModel_id" type="hidden" value="">
		</td>
		<td>
			<div class="specsHead"><span>计费类型</span><span style="width:220px;" name="fee_type_name">${form.fee_type_name}</span></div>
		</td>
		<td>
			<div class="specsHead"><span>计费公式</span><span style="width:220px;" name="fee_formula_name">${form.fee_formula_name}</span></div>
		</td>
	</tr>
	<tr class="order_model_tr">
		<td>
			<div class="specsHead"><span>结算类型 </span>
				<select class="table_select" name="fix_charge_type" onchange="fixChargeTypeCharge(this)">
    				<option value="1" <#if form.fix_charge_type=="1">selected="selected"</#if>>日结</option>
    				<option value="2" <#if form.fix_charge_type=="2">selected="selected"</#if>>月结</option>
				</select>
			</div>
		</td>
		<td>
			<div class="specsHead"><span>是否乘月天数 </span>
				<select class="table_select" name="is_multiplied_actualdays" <#if form.fix_charge_type=="1">disabled="disabled"</#if>>
    				<option value="0" <#if form.is_multiplied_actualdays=="0">selected="selected"</#if>>否</option>
    				<option value="1" <#if form.is_multiplied_actualdays=="1">selected="selected"</#if>>是</option>
				</select>
			</div>
		</td>
		<td></td>
	</tr>
	<#if form.fee_formula=="1">
		<#if form.chargePrices??>
		<#list form.chargePrices as chargePrice >
		<#if chargePrice_index==0>
			<tr class="order_model_tr">
				<td>
					<div class="specsHead"><span>单价:</span><input class="table_input_num" name="price" type="text" value="${chargePrice.price}"></div>
				</td>
				<td>
					<div class="specsHead"><span>费率(‰):</span><input class="table_input_num" name="fee_ratio" type="text" value="${chargePrice.fee_ratio}"></div>
				</td>
				<td>
					<div class="specsHead"><span>费率拆分规则:</span><input class="table_input_num" name="fee_ratio_division" type="text" value="${chargePrice.fee_ratio_division}"></div>
				</td>
			</tr>
		</#if>
		</#list>
		</#if>
	</#if>
	<#if form.fee_formula=="2"||form.fee_formula=="3"||form.fee_formula=="4">
		<tr class="order_model_tr">
    		<td colspan="3" align="middle">
    			<table style="width:99%;" class="table table-striped table-bordered table-condensed">
            	<thead>
            		<tr class="detailButton">
            			<th colspan="10" width="99%" valign="top" style="text-align: left;">
            				<div>
            					<a href="javascript:void(0)" class="table_button_alink" onclick="addSpecItem(this)">添加阶梯</a>
            				</div>
            			</th>
            		</tr>
            		<tr>
            			<th>选项名称</th><th>阶梯区间</th><th>阶梯单位</th><th>阶梯开始</th><th>阶梯结束</th><th>费率(‰)</th><th>费率拆分规则</th><th>封顶费用</th><th>固定费用</th><th class="detailButton">操作</th>
            		</tr>
            	</thead>
            	<tbody>
            		<#if form.chargePrices??>
						<#list form.chargePrices as chargePrice >
                    		<tr style="HEIGHT:22px">
                    			<td><input type="text" name="option_name" onmouseover="this.title=this.value;" value="${chargePrice.option_name}" maxlength="50" class="table_input_string"></td>
                    			<td>
                    				<select name="step_interval" onchange="itemSelectChange(this)" style="width:122px;">
                    					<option value="1" <#if chargePrice.step_interval=="1">selected="selected"</#if>>MIN&lt;N&lt;=MAX</option>
                    					<option value="2" <#if chargePrice.step_interval=="2">selected="selected"</#if>>MIN&lt;=N&lt;MAX</option>
                    				</select>
                    			</td>
                    			<td>
                    				<select name="step_unit" onchange="itemSelectChange(this)" style="width:50px;">
                    					<option value="0" <#if chargePrice.step_unit=="0">selected="selected"</#if>>无</option>
                    					<option value="1" <#if chargePrice.step_unit=="1">selected="selected"</#if>>万</option>
                    					<option value="2" <#if chargePrice.step_unit=="2">selected="selected"</#if>>亿</option>
                    				</select>
                    			</td>
                    			<td><input type="text" name="step_begin" onmouseover="this.title=this.value;" value="${chargePrice.step_begin}" class="table_input_num"></td>
                    			<td><input type="text" name="step_end" onmouseover="this.title=this.value;" value="${chargePrice.step_end}" class="table_input_num"></td>
                    			<td><input type="text" name="fee_ratio" onmouseover="this.title=this.value;" value="${chargePrice.fee_ratio}" class="table_input_num"></td>
                    			<td><input type="text" name="fee_ratio_division" onmouseover="this.title=this.value;" value="${chargePrice.fee_ratio_division}" class="table_input_num"></td>
                    			<td><input type="text" name="max_consume" onmouseover="this.title=this.value;" value="${chargePrice.max_consume}" class="table_input_num"></td>
                    			<td><input type="text" name="fixed_charge" onmouseover="this.title=this.value;" value="${chargePrice.fixed_charge}" class="table_input_num"></td>
                    			<td style="display:none"><input type="text" name="id" value=""><input type="text" name="feemodel_id" value=""></td>
                    			<td class="detailButton"><a onclick="delSpecItem(this)" class="table_button_alink" style="width: 55px;">删除阶梯</a></td>
                    		</tr>
            			</#list>
            		</#if>
             	</tbody>
             	</table>
     		</td>
     	</tr>
 	</#if>
</#if>       