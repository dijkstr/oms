package com.hundsun.boss.modules.charge.web.sql;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.hundsun.boss.base.page.Page;
import com.hundsun.boss.common.Formatter;
import com.hundsun.boss.common.utils.CommonUtil;
import com.hundsun.boss.modules.charge.form.sql.ChargeSqlForm;
import com.hundsun.boss.modules.charge.service.sql.ChargeSqlService;
import com.hundsun.boss.util.ZipCompressUtil;
/**
 * 计费sql数据导出controller
 * @author feigq
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/charge/manager/sql/export")
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ChargeSqlExportController extends ApplicationObjectSupport{
    @Autowired
    private  ChargeSqlService chargeSqlService;
    
   /**
    * sql查询界面
    * @param form
    * @param request
    * @param response
    * @param model
    * @return
    */
        @SuppressWarnings("deprecation")
        @RequiresPermissions("charge:manager:sql:view")
        @RequestMapping(value = "/download")
        public @ResponseBody Map<String, String> download(String  sql, HttpServletRequest request, HttpServletResponse response) 
                throws Exception{
            Map<String, String> data = new HashMap<String, String>();
            List list=null;
            //获取查询结果
            try {
             // 生成下载文件名称
                String zipFileName = "download" + Formatter.formatDate(Formatter.TIME_FORMAT2, new Date());
                String zipFilePath = request.getRealPath("/") + zipFileName;
                
                ServletOutputStream out = response.getOutputStream();
                response.setContentType("application/pdf;charset=UTF-8");
                // 注释下文直接在浏览器中打开文件，否则在浏览器中提示下载
                response.setHeader("Content-disposition", "attachment;filename=" + zipFileName + ".zip");
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;

                // pdf打印内容
                String filePath = request.getRealPath("/");
                
                try{
                    File zipPath = new File(filePath + "/" + zipFileName);
                    // 判断路径是否存在
                    if (!zipPath.exists()) {
                        File parent = zipPath.getParentFile();
                        if (parent != null && !parent.exists()) {
                            parent.mkdirs();
                        }
                    }
                  //对form的sql进行处理
                    String newsql=HtmlUtils.htmlUnescape(sql);
                    String decodesql=URLDecoder.decode(newsql, "UTF-8");
                    //重新设置form的sql方法
                    ChargeSqlForm form=new ChargeSqlForm();
                    form.setSql(decodesql);
                    Page<List> page = chargeSqlService.find(new Page<List>(request, response), form);
                    list=page.getList();   
                    
//                List list=page.getList();
                if(!CommonUtil.isNullorEmpty(list)){
                    String filepathStr=zipFilePath+System.getProperty("file.separator")+"脚本导出数据.xlsx";
                    File exportpath = new File(filepathStr);
                    // 判断路径是否存在
                    if (!exportpath.exists()) {
                        File parent = exportpath.getParentFile();
                        if (parent != null && !parent.exists()) {
                            parent.mkdirs();
                        }
                    }
                                       
                    //第一步,创建一个webbook文件,对应一个excel文件
                    SXSSFWorkbook wb = new SXSSFWorkbook ();
                    //第二部,在excel中添加一个sheet工作簿
                    Sheet sheet = wb.createSheet("导出数据");
                    //第三部,做sheet中添加表头第0行,注意老版本poi对excel的行数列数有限制short
                    Row row = sheet.createRow(0);
                    //第四部,创建单元格表头 设置表头居中
                    CellStyle style = wb.createCellStyle();
                    style.setAlignment(CellStyle.ALIGN_CENTER);//创建一个居中格式
                    
                    //创建具体盛放数据的单元格,可以考虑把cell抽成共通对象去使用
                    Cell cell = null;
                    //获取第一个map
                    Map dataMap= (Map)list.get(0);
                    int count=0;
                    Iterator<String> iterator = dataMap.keySet().iterator();
                    while(iterator.hasNext()){
                        //创建单元格
                        cell = row.createCell(count++);
                        //设置单元格值
                        cell.setCellValue(iterator.next());
                    }
                    
                    //第五部,写入实体数据 实际应用中这些数据应该是从数据库中得到
                    for(int j=0;j<list.size();j++){
                        
                        Map valuemap= (Map)list.get(j);
                        int num=0;
                        Iterator<String> iterator1 = valuemap.keySet().iterator();
                        //创建新行,从1开始
                        row = sheet.createRow(j+1);
                        while(iterator1.hasNext()){
                            //创建单元格,并设置
                          row.createCell(num++).setCellValue(valuemap.get(iterator1.next()).toString());
                            
                        }
                    }
                                
                    //第六部,将文件保存到指定位置
                    FileOutputStream fout = new FileOutputStream(filepathStr);
                    wb.write(fout);
                    fout.flush();
                    fout.close();
                    wb.dispose();
                    
                 // pdf打印内容
                    ZipCompressUtil.fileToZip(zipFilePath, zipFilePath + ".zip");

                    bis = new BufferedInputStream(new FileInputStream(zipFilePath + ".zip"));
                    bos = new BufferedOutputStream(out);
                    byte[] buff = new byte[2048];
                    int bytesRead;
                    try {
                        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                            bos.write(buff, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        data.put("message", "文件导出失败！");
                        logger.info(e.getMessage(), e);
                    } 
                    
                }
                    
                }catch(Exception e){
                    data.put("message", "文件导出失败！");
                    logger.error(e.getMessage());
                    throw e;
                }
             finally {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            }
            out.print("<script>window.close()</script>");
            // 生成完毕后，去掉临时文件夹以及文件
            // 因为同一个用户只有一个生成文件夹，而且生成文件后会将这个文件夹整个删除
            // 因此，即使由于错误或其他原因导致文件夹中出现临时文件，也会伴随下次成功
            // 导出被删除
            File zipfile = new File(zipFilePath);
            if (zipfile.exists()) {
                CommonUtil.deleteAllFilesOfDir(zipfile);
                CommonUtil.deleteAllFilesOfDir(new File(zipfile + ".zip"));
            }
            data.put("message", "文件导出成功！");
                
                
                
                
//                //对form的sql进行处理
//                String newsql=HtmlUtils.htmlUnescape(sql);
//                String decodesql=URLDecoder.decode(newsql, "UTF-8");
//                //重新设置form的sql方法
//                ChargeSqlForm form=new ChargeSqlForm();
//                form.setSql(decodesql);
//                Page<List> page = chargeSqlService.find(new Page<List>(request, response), form);
//                list=page.getList();   
//                
////            List list=page.getList();
//            if(!CommonUtil.isNullorEmpty(list)){
//                //第一步,创建一个webbook文件,对应一个excel文件
//                SXSSFWorkbook wb = new SXSSFWorkbook ();
//                //第二部,在excel中添加一个sheet工作簿
//                Sheet sheet = wb.createSheet("导出数据");
//                //第三部,做sheet中添加表头第0行,注意老版本poi对excel的行数列数有限制short
//                Row row = sheet.createRow(0);
//                //第四部,创建单元格表头 设置表头居中
//                CellStyle style = wb.createCellStyle();
//                style.setAlignment(CellStyle.ALIGN_CENTER);//创建一个居中格式
//                
//                //创建具体盛放数据的单元格,可以考虑把cell抽成共通对象去使用
//                Cell cell = null;
//                //获取第一个map
//                Map dataMap= (Map)list.get(0);
//                int count=0;
//                Iterator<String> iterator = dataMap.keySet().iterator();
//                while(iterator.hasNext()){
//                    //创建单元格
//                    cell = row.createCell(count++);
//                    //设置单元格值
//                    cell.setCellValue(iterator.next());
//                }
//                
//                //第五部,写入实体数据 实际应用中这些数据应该是从数据库中得到
//                for(int j=0;j<list.size();j++){
//                    
//                    Map valuemap= (Map)list.get(j);
//                    int num=0;
//                    Iterator<String> iterator1 = valuemap.keySet().iterator();
//                    //创建新行,从1开始
//                    row = sheet.createRow(j+1);
//                    while(iterator1.hasNext()){
//                        //创建单元格,并设置
//                      row.createCell(num++).setCellValue(valuemap.get(iterator1.next()).toString());
//                        
//                    }
//                }
//                            
//                //第六部,将文件保存到指定位置
//                FileOutputStream fout = new FileOutputStream("D:/student.xls");
//                wb.write(fout);
//                fout.close();
//            }
           
            
           
            
        }catch(Exception e){
            data.put("message", "文件导出失败！");
            logger.error("error:", e);
            CommonUtil.exceptionHandler(data, e);
        }
            return data;
        }

}
