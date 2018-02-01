package com.hundsun.boss.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hundsun.boss.common.Constant;
import com.hundsun.boss.common.Formatter;
import com.hundsun.boss.common.ReportColumn;
import com.hundsun.boss.common.utils.CommonUtil;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class XlsxUtil {

    private static final Logger logger = LoggerFactory.getLogger(XlsxUtil.class);

    /**
     * 生成xlsx下载文件
     * 
     * @param mValues
     * @param filePath
     * @throws Exception
     */

    public static void generate(Map mValues, String filePath) throws Exception {
        SXSSFWorkbook workbook = null;
        FileOutputStream out = null;
        try {

            List<Map> lstResult = (List) mValues.get("griddata");
            //ReportTemplate reportTemplate = (ReportTemplate) mValues.get("template");

            workbook = createXlsxFile(filePath, mValues.get("filename").toString());

            List<ReportColumn> reportColumns = PropertyUtil.getReportColumns(mValues.get("content").toString());

            // 打印列头
            int rowIndex = XlsxUtil.writeHeader(workbook, reportColumns);

            // 将数据查询结果写入Xlsx文件
            writeContent(workbook, lstResult, reportColumns, 0, rowIndex);

            out = new FileOutputStream(filePath);
            workbook.write(out);
            out.flush();
            out.close();
            workbook.dispose();
            lstResult = null;
        } catch (Exception e) {
            try {
                if (null != out) {
                    out.close();
                }
            } catch (IOException e2) {
                logger.error("XlsxUtil.getXlsxForDownload: close out error", e2);
            }
            try {
                if (null != workbook) {
                    workbook.dispose();
                }
            } catch (Exception e2) {
                logger.error("XlsxUtil.getXlsxForDownload: close workbook error", e2);
            }
            logger.error("XlsxUtil.getXlsxForDownload: error", e);
            throw e;
        }
    }

    /**
     * 分辨excel字节流，返回workbook对象
     * 
     * @param in
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    public static Workbook create(InputStream in) throws IOException, InvalidFormatException {
        if (!in.markSupported()) {
            in = new PushbackInputStream(in, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader(in)) {
            return new HSSFWorkbook(in);
        }
        if (POIXMLDocument.hasOOXMLHeader(in)) {
            return new XSSFWorkbook(OPCPackage.open(in));
        }
        throw new IllegalArgumentException();
    }

    /**
     * 创建Xlxs文件
     * 
     * @param filePath
     * @param firstSheetName
     * @return
     * @throws Exception
     */
    public static SXSSFWorkbook createXlsxFile(String filePath, String firstSheetName) throws Exception {
        SXSSFWorkbook workbook = null;
        try {
            File xlsxFile = new File(filePath);
            if (!xlsxFile.exists()) {
                // 判断路径是否存在
                File parent = xlsxFile.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                // 生成文件
                xlsxFile.createNewFile();
                // 不自动写入文件
                workbook = new SXSSFWorkbook(-1);
                // 添加一个sheet
                workbook.createSheet(firstSheetName);
            }
            return workbook;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 生成Xlsx文件表头
     * 
     * @param workbook
     * @param headers
     * @return
     * @throws Exception
     */
    public static int writeHeader(SXSSFWorkbook workbook, List<ReportColumn> headers) throws Exception {
        return writeHeader(workbook, headers, 0, 0);
    }

    /**
     * 生成Xlsx文件表头
     * 
     * @param workbook
     * @param headers
     * @param iSheetIndex
     * @param RowIndex
     * @return
     * @throws Exception
     */
    public static int writeHeader(SXSSFWorkbook workbook, List<ReportColumn> headers, int iSheetIndex, int RowIndex) throws Exception {
        try {
            if (null == headers || headers.isEmpty()) {
                throw new Exception("XlsxUtil.writeHeader: header read error");
            } else {
                Sheet sheet = workbook.getSheetAt(iSheetIndex);
                // 获取写入表头的行位置
                Row row = sheet.createRow(RowIndex);
                Cell cell = null;
                // 输出csv表头
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setWrapText(true);
                for (int i = 0; i < headers.size(); i++) {
                    cell = row.createCell(i);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(headers.get(i).getHeader());
                }
            }
            return RowIndex + 1;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 生成CSV文件内容
     * 
     * @param workbook
     * @param datas
     * @param lstaTD
     * @param straOutPutPath
     * @param userBean
     * @param baGroupsumflag
     * @param bShowHeader
     * @param iSheetIndex
     * @param rowIndex
     * @throws Exception
     */
    public static void writeContent(SXSSFWorkbook workbook, List<Map> datas, List<ReportColumn> reportColumns, int iSheetIndex, Integer rowIndex) throws Exception {
        try {
            Sheet sheet = workbook.getSheetAt(iSheetIndex);
            Row row = null;
            Cell cell = null;

            Object oCellValue = null;
            // 写入文件内容
            for (int i = 0; i < datas.size(); i++) {
                // 写入下一行
                row = sheet.createRow(rowIndex + i);
                ReportColumn reportColumn = null;
                for (int n = 0; n < reportColumns.size(); n++) {
                    cell = row.createCell(n);
                    reportColumn = reportColumns.get(n);
                    // 如果导出 内容以数字 方式显示
                    if (oCellValue instanceof Number && !reportColumn.getType().equalsIgnoreCase("string")) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(((Number) oCellValue).doubleValue());
                    } else {
                        // 如果导出内容以字符方式显示，按照format格式化
                        oCellValue = formatValue(datas.get(i).get(reportColumn.getField()), reportColumn);
                        cell.setCellValue(String.valueOf(oCellValue));
                    }
                }
                if (i % Constant.CONST_EXPORT_SELECT_DATA_LIMIT == 0) {
                    ((SXSSFSheet) sheet).flushRows();
                }
            }
            // 将生成数据写入xlxs文件
            ((SXSSFSheet) sheet).flushRows();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * 根据format中定义的字段类型格式化显示字符
     * 
     * @param oBean
     * @param td
     * @param userBean
     * @param baGroupsumflag
     * @return
     * @throws Exception
     */
    public static String formatValue(Object value, ReportColumn reportColumn) throws Exception {
        String retValue = "";
        if (null == value) {
        } else if (value instanceof Date) {
            if (reportColumn.getType().equalsIgnoreCase("date")) {
                if (!CommonUtil.isNullorEmpty(reportColumn.getFormat())) {
                    retValue = Formatter.formatDate(reportColumn.getFormat(), value);
                } else {
                    retValue = Formatter.formatDate(Formatter.DATE_FORMAT1, value);
                }
            }
        } else if (value instanceof Number) {
            if (!CommonUtil.isNullorEmpty(reportColumn.getFormat())) {
                retValue = Formatter.formatDecimal(reportColumn.getFormat(), value);
            } else {
                retValue = Formatter.formatDecimal(Formatter.DECIMAL_FORMAT8, value);
            }
        } else {
            retValue = value.toString();
        }
        return retValue;
    }

    /**
     * 根据输入流 获取WorkBook
     * 
     * @param is
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream is) throws Exception {
        return new XSSFWorkbook(is);
    }

    /**
     * 获取指定页
     * 
     * @param wb
     * @param index
     * @return
     */
    public static Sheet getSheet(Workbook wb, int sheetIndex) {
        return wb.getSheetAt(sheetIndex);
    }

    /**
     * 获取指定页
     * 
     * @param sheet
     * @param index
     * @return
     */
    public static Row getSheet(Sheet sheet, int rowIndex) {
        return sheet.getRow(rowIndex);
    }

    /**
     * 获取指定行
     * 
     * @param wb
     * @param sheetIndex
     * @param rowIndex
     * @return
     */
    public static Row getRow(Workbook wb, int sheetIndex, int rowIndex) {
        return wb.getSheetAt(sheetIndex).getRow(rowIndex);
    }

    /**
     * 获取单元格
     * 
     * @param row
     * @param cellIndex
     * @return
     */
    public static Cell getCell(Row row, int cellIndex) {
        return row.getCell(cellIndex);
    }

    /**
     * 获取单元格
     * 
     * @param wb
     * @param sheetIndex
     * @param rowIndex
     * @param cellIndex
     * @return
     */
    public static Cell getCell(Workbook wb, int sheetIndex, int rowIndex, int cellIndex) throws Exception {
        try {
            return wb.getSheetAt(sheetIndex).getRow(rowIndex).getCell(cellIndex);
        } catch (NullPointerException e) {
            return null;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     * 
     * @param cell
     * @param reportColumn
     * @return
     * @throws Exception
     */
    public static Object getCellValue(Cell cell, ReportColumn reportColumn,int rowIndex) throws Exception {
        if ("true".equals(reportColumn.getIskey()) && CommonUtil.isNullorEmpty(cell)) {
            throw new Exception("第" + (rowIndex + 1) + "行,'" + reportColumn.getHeader() + "'不能为空");
        }
        if (!CommonUtil.isNullorEmpty(cell)) {
            cell.setCellType(Cell.CELL_TYPE_STRING);
            String cellValue = cell.getStringCellValue();
            if (("number").equals(reportColumn.getType())) {
                try {
                    return Double.valueOf(cellValue);
                } catch (Exception e) {
                    throw new Exception("第" + (cell.getRowIndex() + 1) + "行,'" + reportColumn.getHeader() + "'格式不正确");
                }
            }
            return cellValue;
        }
        return "";
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     * 
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");// 去掉多余的0
            s = s.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 官方测试用例
     * 
     * @param args
     * @throws Throwable
     */
    public static void main(String[] args) throws Throwable {
        SXSSFWorkbook wb = new SXSSFWorkbook(-1); // turn off auto-flushing and
                                                  // accumulate all rows in
                                                  // memory
        Sheet sh = wb.createSheet();
        for (int rownum = 0; rownum < 1000; rownum++) {
            Row row = sh.createRow(rownum);
            for (int cellnum = 0; cellnum < 10; cellnum++) {
                Cell cell = row.createCell(cellnum);
                String address = new CellReference(cell).formatAsString();
                cell.setCellValue(address);
            }

            // manually control how rows are flushed to disk
            if (rownum % 100 == 0) {
                ((SXSSFSheet) sh).flushRows(100); // retain 100 last rows and
                                                  // flush all others

                // ((SXSSFSheet)sh).flushRows() is a shortcut for
                // ((SXSSFSheet)sh).flushRows(0),
                // this method flushes all rows
            }

        }

        FileOutputStream out = new FileOutputStream("D:/sxssf.xlsx");
        wb.write(out);
        out.close();

        // dispose of temporary files backing this workbook on disk
        wb.dispose();
    }

}
