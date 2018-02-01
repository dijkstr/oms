<#if form ?? && form.orderCombines ??>
	<#list form.orderCombines as orderCombine >
		<#if orderCombine ??>
		    <div id="combine${orderCombine_index+1}" name="combineDiv">
		    	<table id="combine${orderCombine_index+1}_in">
		    		<tbody>
		    			<tr>
							<td colspan="3" align="right" style="border-style: none; background: #f5f5f5;">
								<a href="javascript:void(0)" class="table_button_alink" onclick="deleteCombine(combine${orderCombine_index+1})">删除组合</a>
							</td>
						</tr>
						<tr>
							<td style="border-style: none; background: #f5f5f5;">
								<div class="specsHead">
									<span>计费开始时间</span>
									<input class="Wdate" type="text" name="charge_begin_time" readonly="true" value="<#if orderCombine.combine_begin_date ?? && orderCombine.combine_begin_date !=''>${orderCombine.combine_begin_date?substring(0,10)}</#if>" onfocus="WdatePicker({})">
								</div>
								<input name="combine_id" type="hidden" value="${orderCombine.id}">
								<#if orderCombine.orderModels??>
									<#list orderCombine.orderModels as orderModel >
										<#if orderModel_index==0>
											<input name="combine_orderModel_id" type="hidden" value="${orderModel.id}">
										</#if>
									</#list>
								</#if>
							</td>
							<td style="border-style: none; background: #f5f5f5;">
								<div class="specsHead">
									<span>计费结束时间</span>
									<input class="Wdate" type="text" name="charge_end_time" readonly="true" value="<#if orderCombine.combine_end_date ?? && orderCombine.combine_end_date !=''>${orderCombine.combine_end_date?substring(0,10)}</#if>" onfocus="WdatePicker({})">
								</div>
							</td>
							<td style="border-style: none; background: #f5f5f5;">
								<#if orderCombine.orderModels??>
									<#list orderCombine.orderModels as orderModel >
										<#if orderModel_index==0>
											<div id="" class="specsHead"><span>折扣（%）</span><input class="table_input_num" name="discount" type="text" value="${orderModel.discount}"></div>
										</#if>
									</#list>
								</#if>
							</td>
						</tr>
						<#if orderCombine.orderModels??>
							<#list orderCombine.orderModels as orderModel >
								<#if orderModel_index==0>
								<tr>
									<td style="border-style: none; background: #f5f5f5;">
										<div id="" class="specsHead"><span>保底</span><input class="table_input_num" name="min_consume" type="text" value="${orderModel.min_consume}"></div>
									</td>
									<td style="border-style: none; background: #f5f5f5;">
										<div id="" class="specsHead"><span>保底类型</span>
											<select class="table_select" name="min_type">
												<option value ="" <#if orderModel.min_type=="">selected="selected"</#if>>无</option>
										  		<option value ="1" <#if orderModel.min_type=="1">selected="selected"</#if>>按年</option>
										  		<option value ="2" <#if orderModel.min_type=="2">selected="selected"</#if>>按月</option>
											</select>
										</div>
									</td>
									<td style="border-style: none; background: #f5f5f5;"></td>
								</tr>
								<tr>
									<td style="border-style: none; background: #f5f5f5;">
										<div id="" class="specsHead"><span>封顶</span><input class="table_input_num" name="max_consume" type="text" value="${orderModel.max_consume}"></div>
									</td>
									<td style="border-style: none; background: #f5f5f5;">
										<div id="" class="specsHead"><span>封顶类型</span>
											<select class="table_select" name="max_type">
												<option value ="" <#if orderModel.max_type=="">selected="selected"</#if>>无</option>
										  		<option value ="1" <#if orderModel.max_type=="1">selected="selected"</#if>>按年</option>
										  		<option value ="2" <#if orderModel.max_type=="2">selected="selected"</#if>>按月</option>
											</select>
										</div>
									</td>
									<td style="border-style: none; background: #f5f5f5;"></td>
								</tr>
								</#if>
							</#list>
						</#if>
						<tr>
							<td colspan="3" style="border-style: none; background: #f5f5f5;" align="middle">
								<table id="orderFeecombine${orderCombine_index+1}" style="width:99%;" class="table table-striped table-bordered table-condensed">
									<thead>
		                				<tr class="detailButton">
		                					<th colspan="10" width="99%" valign="top" style="text-align: left;">
		                						<div><a href="javascript:void(0)" class="table_button_alink" onclick="addOrderFee(this)">添加预付</a></div>
		                					</th>
		                				</tr>
		                				<tr><th>固定费用类型</th><th>显示名称</th><th>预付日期</th><th>展示日期</th><th>预付金额</th><th class="detailButton">操作</th></tr>
	                				</thead>
	                				<tbody id="body_orderFee">
									<#if orderCombine.orderAdvPayments??>
										<#list orderCombine.orderAdvPayments as orderAdvPayment >
											<tr style="HEIGHT:22px">
												<td>
													<select name="payment_type" style="width:150px;">
														<option value="10000" <#if orderAdvPayment.payment_type=="10000">selected="selected"</#if>>保底预付</option>
														<option value="12471" <#if orderAdvPayment.payment_type=="12471">selected="selected"</#if>>一次性费用</option>
														<option value="12473" <#if orderAdvPayment.payment_type=="12473">selected="selected"</#if>>年费</option>
														<option value="12472" <#if orderAdvPayment.payment_type=="12472">selected="selected"</#if>>不定期</option>
														<option value="12474" <#if orderAdvPayment.payment_type=="12474">selected="selected"</#if>>条件延后</option>
													</select>
												</td>
		                        				<td><input type="text" name="display_name" onmouseover="this.title=this.value;" value="${orderAdvPayment.display_name}" class="table_input_string"></td>
												<td><input type="text" name="advance_date" class="Wdate" readonly="true" value="${orderAdvPayment.advance_date}" onfocus="WdatePicker()"></td>
												<td><input type="text" name="display_date" class="Wdate" readonly="true" value="${orderAdvPayment.display_date}" onfocus="WdatePicker()"></td>
												<td><input type="text" name="amount" onmouseover="this.title=this.value;" value="${orderAdvPayment.amount}" class="table_input_num"></td>
												<td style="display:none"><input type="text" name="id" title="id" value="${orderAdvPayment.id}"></td>
												<td class="detailButton"><a onclick="delOrderFee(this)" class="table_button_alink" style="width: 30px;">删除</a></td>
											</tr>
										</#list>
									</#if>
									</tbody>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="3" style="border-style: none; background: #f5f5f5;" align="left">
								<div name="orderValidation_2" class="orderValidation">
									<div class="orderValidation_text">
										<#if orderCombine.orderValidates??>
											<#list orderCombine.orderValidates as orderValidate >
												<P><span>${orderValidate_index + 1}.</span><em>${orderValidate.reason}</em></P>
											</#list>
										</#if>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="3" style="border-style: none; background: #f5f5f5;padding-left: 10px;" align="left">
								<a href="javascript:void(0)" class="table_button_alink" onclick="creatProductTable(combine${orderCombine_index+1})">添加产品</a>
							</td>
						</tr>
						<#if orderCombine.orderProducts??>
							<#list orderCombine.orderProducts as orderProduct >
								<tr>
									<td colspan="3" style="border-style: none; background: #f5f5f5;" align="middle">
                        				<table id="" name="prodTable" style="width: 99%;background: white;">
                        				<tbody>
				                        	<tr>
				                        		<td colspan="3" align="right" style="border-style: none;"><a href="javascript:void(0)" class="table_button_alink" onclick="deleteProduct(this)">删除产品</a></td>
				                        	</tr>
				                        	<tr>
				                        		<td style="border-style: none;">
				                        			<div class="specsHead">
				                        				<span>协同销售产品</span>
				                        				<input class="table_input_num" name="product_id" type="text" value="${orderProduct.product_id}" readonly="readonly" onclick="xtProdChooserShow(this)">
				                        			</div>
				                        			<input name="ex_product_id" type="hidden" value="${orderProduct.ex_product_id}">
				                        			<input name="prod_id" type="hidden" value="${orderProduct.id}">
				                        		</td>
				                        		<td style="border-style: none;">
				                        			<div class="specsHead"><span>产品名称</span><span style="width:220px;" name="product_name_span">${orderProduct.product_name}</span></div>
				                        		</td>
				                        		<td style="border-style: none;"></td>
				                        	</tr>
				                        	<#if orderProduct.orderModels??>
												<#list orderProduct.orderModels as orderModel >
													<#if orderModel_index==0>
							                        	<tr class="order_model_tr">
							                        		<td>
								                        		<div class="specsHead">
								                        			<span>计费模式</span>
									                        		<input class="table_input_string" name="feemodel_name" type="text" value="${orderModel.feemodel_name}" readonly="readonly" onclick="modelChooserShow(this)">
								                        		</div>
								                        		<input name="fee_type" type="hidden" value="${orderModel.fee_type}">
								                        		<input name="fee_formula" type="hidden" value="${orderModel.fee_formula}">
								                        		<input name="prod_orderModel_id" type="hidden" value="${orderModel.id}">
							                        		</td>
							                        		<td><div class="specsHead"><span>计费类型</span><span style="width:220px;" name="fee_type_name">${orderModel.fee_type_name}</span></div></td>
							                        		<td><div class="specsHead"><span>计费公式</span><span style="width:220px;" name="fee_formula_name">${orderModel.fee_formula_name}</span></div></td>
							                        	</tr>
							                        	<tr class="order_model_tr">
							                        		<td>
							                        			<div class="specsHead"><span>结算类型 </span>
							                        				<select class="table_select" name="fix_charge_type" onchange="fixChargeTypeCharge(this)">
								                        				<option value="1" <#if orderModel.fix_charge_type=="1">selected="selected"</#if>>日结</option>
								                        				<option value="2" <#if orderModel.fix_charge_type=="2">selected="selected"</#if>>月结</option>
							                        				</select>
							                        			</div>
							                        		</td>
							                        		<td>
							                        			<div class="specsHead"><span>是否乘月天数 </span>
							                        				<select class="table_select" name="is_multiplied_actualdays" <#if orderModel.fix_charge_type=="1">disabled="disabled"</#if>>
								                        				<option value="0" <#if orderModel.is_multiplied_actualdays=="0">selected="selected"</#if>>否</option>
								                        				<option value="1" <#if orderModel.is_multiplied_actualdays=="1">selected="selected"</#if>>是</option>
							                        				</select>
							                        			</div>
							                        		</td>
							                        		<td></td>
							                        	</tr>
						                        		<#if orderModel.fee_formula=="1">
							                        		<#if orderModel.orderPrices??>
							                        		<#list orderModel.orderPrices as orderPrice >
							                        		<#if orderPrice_index==0>
							                        			<tr class="order_model_tr">
																	<td>
																		<div class="specsHead"><span>单价:</span><input class="table_input_num" name="price" type="text" value="${orderPrice.price}"></div>
																	</td>
	                            									<td>
	                            										<div class="specsHead"><span>费率(‰):</span><input class="table_input_num" name="fee_ratio" type="text" value="${orderPrice.fee_ratio}"></div>
	                            									</td>
																	<td>
																		<div class="specsHead"><span>费率拆分规则:</span><input class="table_input_num" name="fee_ratio_division" type="text" value="${orderPrice.fee_ratio_division}"></div>
																	</td>
																</tr>
															</#if>
															</#list>
															</#if>
						                        		</#if>
							                        	<#if orderModel.fee_formula=="2"||orderModel.fee_formula=="3"||orderModel.fee_formula=="4">
							                        		<tr class="order_model_tr">
								                        		<td colspan="3" align="middle">
								                        			<table style="width:99%;" class="table table-striped table-bordered table-condensed">
										                        	<thead>
										                        		<tr class="detailButton">
										                        			<th colspan="10" width="99%" valign="top" style="text-align: left;">
										                        				<div><a href="javascript:void(0)" class="table_button_alink" onclick="addSpecItem(this)">添加阶梯</a></div>
										                        			</th>
										                        		</tr>
										                        		<tr>
										                        			<th>选项名称</th><th>阶梯区间</th><th>阶梯单位</th><th>阶梯开始</th><th>阶梯结束</th><th>费率(‰)</th><th>费率拆分规则</th><th>封顶费用</th><th>固定费用</th><th class="detailButton">操作</th>
										                        		</tr>
										                        	</thead>
										                        	<tbody>
										                        		<#if orderModel.orderPrices??>
																			<#list orderModel.orderPrices as orderPrice >
												                        		<tr style="HEIGHT:22px">
												                        			<td><input type="text" name="option_name" onmouseover="this.title=this.value;" value="${orderPrice.option_name}" maxlength="50" class="table_input_string"></td>
												                        			<td>
												                        				<select name="step_interval" onchange="itemSelectChange(this)" style="width:122px;">
												                        					<option value="1" <#if orderPrice.step_interval=="1">selected="selected"</#if>>MIN&lt;N&lt;=MAX</option>
												                        					<option value="2" <#if orderPrice.step_interval=="2">selected="selected"</#if>>MIN&lt;=N&lt;MAX</option>
												                        				</select>
												                        			</td>
												                        			<td>
												                        				<select name="step_unit" onchange="itemSelectChange(this)" style="width:50px;">
												                        					<option value="0" <#if orderPrice.step_unit=="0">selected="selected"</#if>>无</option>
												                        					<option value="1" <#if orderPrice.step_unit=="1">selected="selected"</#if>>万</option>
												                        					<option value="2" <#if orderPrice.step_unit=="2">selected="selected"</#if>>亿</option>
												                        				</select>
												                        			</td>
												                        			<td><input type="text" name="step_begin" onmouseover="this.title=this.value;" value="${orderPrice.step_begin}" class="table_input_num"></td>
												                        			<td><input type="text" name="step_end" onmouseover="this.title=this.value;" value="${orderPrice.step_end}" class="table_input_num"></td>
												                        			<td><input type="text" name="fee_ratio" onmouseover="this.title=this.value;" value="${orderPrice.fee_ratio}" class="table_input_num"></td>
												                        			<td><input type="text" name="fee_ratio_division" onmouseover="this.title=this.value;" value="${orderPrice.fee_ratio_division}" class="table_input_num"></td>
												                        			<td><input type="text" name="max_consume" onmouseover="this.title=this.value;" value="${orderPrice.max_consume}" class="table_input_num"></td>
												                        			<td><input type="text" name="fixed_charge" onmouseover="this.title=this.value;" value="${orderPrice.fixed_charge}" class="table_input_num"></td>
												                        			<td style="display:none"><input type="text" name="id" value="${orderPrice.id}"><input type="text" name="feemodel_id" value="${orderPrice.feemodel_id}"></td>
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
						                         </#list>
						                    </#if>
				                       		<tr>
				                       			<td colspan="3" align="left" style="border-style: none;">
				                       				<div name="orderValidation_3" class="orderValidation">
				                       					<div class="orderValidation_text">
															<#if orderProduct.orderValidates??>
																<#list orderProduct.orderValidates as orderValidate >
																	<P><span>${orderValidate_index + 1}.</span><em>${orderValidate.reason}</em></P>
																</#list>
															</#if>
														</div>
				                       				</div>
				                       			</td>
				                       		</tr>
                        				</tbody>
                        				</table>
                        			</td>
                        		</tr>		
							</#list>
						</#if>
		    		</tbody>
                </table>
           	</div>         
		</#if>
	</#list>
</#if>