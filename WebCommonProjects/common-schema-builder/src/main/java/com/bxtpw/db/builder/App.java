package com.bxtpw.db.builder;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 程序入口
 */
public class App {

    private int startIndex = 1;

    /**
     * <pre>
     * -f 文件路径
     * -s sheet的名称
     *
     * 示例： -f
     * </pre>
     *
     * @param args
     * @throws Exception
     * @author 夏集球
     * @time 2015年6月15日 上午11:44:12
     * @since 0.1
     */
    public static void main(String[] args) throws Exception {
        args = new String[]{//
                "-fC:\\Users\\Arvin\\Desktop\\服务商系统相关设计\\数据库和业务划分数据字典\\1-个人中心业务表.xls",//
                "-s数据字典",//
                "-eH:\\projects\\IdeaProjects\\BxtpwProjects\\common-api-services\\schema"//
        };
        String schemaFilename = "user-space-schema.sql";
        App app = new App();
        app.parseArgs(args);
        // 获取表对象
        List<Table> tables = app.parseTables();
        app.writeTablesToSQL(schemaFilename, tables);
    }

    /**
     * 写入文件
     *
     * @param tables
     * @author 夏集球
     * @time 2015年6月13日 下午4:52:13
     * @since 0.1
     */
    public void writeTablesToSQL(String schemaFilename, List<Table> tables) throws Exception {
        String name = schemaFilename;
        String path = this.exportFolder + File.separator + name;
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (Table table : tables) {
            String tableBuildSQL = TableSqlBuilder.buildTableSQL(table);
            writer.write(tableBuildSQL);
        }
        writer.close();
    }

    /**
     * 解析数据表
     *
     * @return
     * @author 夏集球
     * @time 2015年6月15日 上午11:56:17
     * @since 0.1
     */
    private List<Table> parseTables() throws Exception {
        Workbook wb;
        if (this.isXssf) {
            wb = new XSSFWorkbook(new FileInputStream(this.sourceFile));
        } else {
            wb = new HSSFWorkbook(new FileInputStream(this.sourceFile));
        }
        Sheet sheet = wb.getSheet(this.sheetName);
        if (null == sheet) {
            sheet = wb.getSheetAt(0);
        }
        int begRowIndex = sheet.getFirstRowNum();
        int lastRowIndex = sheet.getLastRowNum();
        int tableOrder = 1;

        List<Table> tables = new ArrayList<>();

        for (int i = begRowIndex; i < lastRowIndex; ++i) {
            Row row = sheet.getRow(i);
            if (isBlankRow(row)) {  // 空行略过
                continue;
            }
            if (isTableRow(row)) {  // 如果是表行
                Cell tableNameCell = row.getCell(startIndex);
                String tableName = tableNameCell.getStringCellValue();
                String tableCNName = tableName.replaceFirst("^[a-z_]+\\(", "").replaceAll("\\)", "").replaceFirst("-[a-zA-Z_]+$", "");
                String tableENName = tableName.replace("(" + tableCNName + ")", "").replaceFirst("-[a-zA-Z_]+$", "");
                String domainName = tableName.replaceFirst("^.*-", "");

                Table table = new Table(tableOrder++, tableCNName, tableENName, domainName);
                tables.add(table);

                // 解析字段
                int j;  // 计数器
                List<TableField> tableFields = new ArrayList<TableField>();
                table.setFields(tableFields);

                int tableFieldOrder = 1;
                for (j = i + 1; j < lastRowIndex; ++j) {
                    Row fieldRow = sheet.getRow(j);
                    if (isBlankRow(fieldRow) || isTableRow(fieldRow)) {  // 空行略过
                        i = j - 1;
                        break;
                    }
                    Cell fieldNameCell = fieldRow.getCell(startIndex + 0);
                    Cell fieldDescCell = fieldRow.getCell(startIndex + 1);
                    Cell fieldTypeCell = fieldRow.getCell(startIndex + 2);
                    Cell fieldNullCell = fieldRow.getCell(startIndex + 3);
                    Cell fieldPKCell = fieldRow.getCell(startIndex + 4);
                    Cell fieldDefaultCell = fieldRow.getCell(startIndex + 5);
                    Cell fieldAutoIncreCell = fieldRow.getCell(startIndex + 6);
                    Cell fieldUniqueCell = fieldRow.getCell(startIndex + 7);

                    tableFields.add(new TableField(//
                            tableFieldOrder++, //
                            fieldNameCell.toString(), //
                            fieldDescCell.toString(), //
                            fieldTypeCell.toString(), //
                            fieldNullCell.toString(), //
                            fieldDefaultCell.toString(), //
                            getBoolean(fieldPKCell.toString()), //
                            getBoolean(fieldAutoIncreCell.toString()),//
                            getBoolean(fieldUniqueCell.toString())));
                }
                i = j;
            }
        }
        return tables;
    }

    /**
     * 获取boolean值
     *
     * @param value
     * @return
     * @author 夏集球
     * @time 2015年6月13日 下午4:55:34
     * @since 0.1
     */
    public static final boolean getBoolean(String value) {
        return "YES".equals(value);
    }

    /**
     * 是否是空行
     *
     * @param row
     * @return
     * @author 夏集球
     * @time 2015年6月13日 下午4:32:59
     * @since 0.1
     */
    private boolean isBlankRow(Row row) {
        if (null == row) return true;
        Cell cell = row.getCell(startIndex);
        if (null == cell) return true;
        String value = cell.getStringCellValue();
        return null == value || "".equals(value.trim());
    }

    /**
     * 判断指定的row是不是表的row
     *
     * @param row
     * @return
     * @author 夏集球
     * @time 2015年6月13日 下午4:27:22
     * @since 0.1
     */
    private boolean isTableRow(Row row) {
        Cell cell = row.getCell(2);
        String value = cell.getStringCellValue();
        return null == value || "".equals(value.trim());
    }

    /**
     * 参数解析
     *
     * @param args
     * @author 夏集球
     * @time 2015年6月15日 上午11:49:57
     * @since 0.1
     */
    private void parseArgs(String[] args) {
        for (String arg : args) {
            String cmd = arg.substring(0, 2);
            String val = arg.substring(2);
            switch (cmd) {
                case "-f": // Excel完整路径
                case "-F":
                    if (".".equals(val)) {
                        val = System.getProperty("user.dir") + File.separator;
                    } else if (val.indexOf(":") < 0) {
                        val = System.getProperty("user.dir") + File.separator + val;
                    }
                    this.sourceFile = val;
                    this.isXssf = this.sourceFile.endsWith("xlsx") || this.sourceFile.endsWith("XLSX");
                    break;
                case "-e": // SQL文件输出路径
                case "-E":
                    if (".".equals(val)) {
                        val = System.getProperty("user.dir") + File.separator;
                    } else if (val.indexOf(":") < 0) {
                        val = System.getProperty("user.dir") + File.separator + val;
                    }
                    this.exportFolder = val;
                    break;
                case "-s":
                case "-S":
                    this.sheetName = val;
                    break;
                default:
                    break;
            }
        }
        // 检查参数
        if (null == this.sourceFile) {
            System.out.println("您必须输入参数: -f(Excel完整路径)");
            System.exit(1);
        }
    }

    /**
     * 源文件路径
     */
    private String sourceFile;

    /**
     * 工作簿名称
     */
    private String sheetName = "数据字典";

    /**
     * 保存结果路径
     */
    private String exportFolder = "";

    /**
     * 新版的Excel表格
     */
    private boolean isXssf = false;
}
