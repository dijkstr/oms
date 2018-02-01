<#if form ??>
	<#if form.fee_formula?? && form.fee_formula=="1">
		<#if form.chargePrices ??>
			<#list form.chargePrices as chargePrice >
	    		<#if chargePrice ?? && chargePrice_index==0>
					<div id="formula_common" style="height: 40px; padding-left: 60px;" >
						<div class="form_tr_div">
							<label class="control-label" for="name">单价:</label>
							<input type="text" name="price" value="${chargePrice.price}" class="required" style="width:150px"/>
							<input name="id" value="${chargePrice.id}" style="display:none"/>
							<input name="feemodel_id" value="${chargePrice.feemodel_id}" style="display:none"/>
						</div>
						<div class="form_tr_div">
							<label class="control-label" for="name">费率(‰):</label>
							<input type="text"name="fee_ratio" value="${chargePrice.fee_ratio}" class="required" style="width:150px"/>
						</div>
						<div class="form_tr_div">
							<label class="control-label" for="name">费率拆分规则:</label>
							<input type="text" name="fee_ratio_division" value="${chargePrice.fee_ratio_division}" class="required" style="width:150px"/>
						</div>
					</div>
				</#if>    
			</#list>
		</#if>
	<#else>
		<div id="formula_common" style="height: 40px; padding-left: 60px;display:none;" >
			<div class="form_tr_div">
				<label class="control-label" for="name">单价:</label>
				<input type="text" name="price" value="" class="required" style="width:150px"/>
				<input name="id" value="" style="display:none"/>
				<input name="feemodel_id" value="" style="display:none"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">费率(‰):</label>
				<input type="text"name="fee_ratio" value="" class="required" style="width:150px"/>
			</div>
			<div class="form_tr_div">
				<label class="control-label" for="name">费率拆分规则:</label>
				<input type="text" name="fee_ratio_division" value="" class="required" style="width:150px"/>
			</div>
		</div>
	</#if>
	
	<div id="specs"  <#if !form.fee_formula ??||form.fee_formula==""||form.fee_formula=="1"> style="display:none;"</#if>>
		<table id="specsDataGrid" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th colspan="10" width="99%" valign="top" style="text-align: left;">
						<div>
							<a href="javascript:void(0)" class="table_button_alink" onclick="addSpecItem(this)">添加</a>
						</div>
					</th>
				</tr>
				<tr>
		  			<th>选项名称</th>
					<th>阶梯区间</th>
					<th>阶梯单位</th>
					<th>阶梯开始</th>
					<th>阶梯结束</th>
					<th>费率(‰)</th>
					<th>费率拆分规则</th>
					<th>封顶费用</th>
					<th>固定费用</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody id="body_specsDataGrid">
				<#if form.fee_formula ?? && (form.fee_formula=="2"||form.fee_formula=="3"||form.fee_formula=="4")>
					<#if form.chargePrices ??>
						<#list form.chargePrices as chargePrice >
				    		<#if chargePrice ??>
								<tr id="tr_specsDataGrid_add" style="HEIGHT:22px" class="tr_insertDataGrid_add">
								    <td ><input type="text" name="option_name" onMouseOver="this.title=this.value;" value="${chargePrice.option_name}" maxlength="50" class="table_input_string"/></td>
									<td >
										<select name="step_interval" onchange="itemSelectChange(this)" style="width:125px;">
											<option value="1" <#if chargePrice.step_interval=="1">selected="selected"</#if>>MIN&lt;N&lt;=MAX</option>
											<option value="2" <#if chargePrice.step_interval=="2">selected="selected"</#if>>MIN&lt;=N&lt;MAX</option>
										</select>
									</td>
									<td >
										<select name="step_unit" onchange="itemSelectChange(this)" style="width:60px;">
											<option value="0" <#if chargePrice.step_unit=="0">selected="selected"</#if>>无</option>
											<option value="1" <#if chargePrice.step_unit=="1">selected="selected"</#if>>万</option>
											<option value="2" <#if chargePrice.step_unit=="2">selected="selected"</#if>>亿</option>
										</select>
									</td>
									<td ><input type="text" name="step_begin" onMouseOver="this.title=this.value;" value="${chargePrice.step_begin}" class="table_input_num"/></td>
								    <td ><input type="text" name="step_end" onMouseOver="this.title=this.value;" value="${chargePrice.step_end}" class="table_input_num"/></td>
									<td ><input type="text" name="fee_ratio" onMouseOver="this.title=this.value;" value="${chargePrice.fee_ratio}" class="table_input_num"/></td>
									<td ><input type="text" name="fee_ratio_division" onMouseOver="this.title=this.value;" value="${chargePrice.fee_ratio_division}" class="table_input_num"/></td>
									<td ><input type="text" name="max_consume" onMouseOver="this.title=this.value;" value="${chargePrice.max_consume}" class="table_input_num"/></td>
									<td ><input type="text" name="fixed_charge" onMouseOver="this.title=this.value;" value="${chargePrice.fixed_charge}" class="table_input_num"/></td>
									<td style="display:none"><input type="text" name="id" value="${chargePrice.id}"/><input type="text" name="feemodel_id" value="${chargePrice.feemodel_id}"/></td>
									<td class="detailButton"><a onclick="delSpecItem(this)" class="table_button_alink" style="width: 55px;">删除阶梯</a></td>
							   </tr>
							</#if>    
						</#list>
					</#if>
				</#if>
			</tbody>
		</table>
	</div>
</#if>