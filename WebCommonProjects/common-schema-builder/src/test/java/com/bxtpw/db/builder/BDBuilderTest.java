package com.bxtpw.db.builder;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author 夏集球
 * 
 * @time 2015年6月13日 下午4:20:58
 * @version 0.1
 * @since 0.1
 */
public class BDBuilderTest {

    protected static final String TAB_ONE = "    ";
    protected static final String TAB_TWO = "        ";
    protected static final String TAB_THREE = "            ";
    protected static final String TAB_FOUR = "                ";
    protected static final String TAB_FIVE = "                    ";
    
    protected static final Map<String, String> foreignerMaps = new HashMap<String, String>();

    private static final String SCHEMA = "tpw";

    // private static final String EXCEL_PATH =
    // "E:\\TEC_DEPT\\百姓通谱网项目\\百姓通谱网\\数据字典\\百姓通谱网数据字典 - 副本.xls";
    //private static final String EXCEL_PATH = "E:\\TEC_DEPT\\百姓通谱网项目\\百姓通谱网\\数据字典\\百姓通谱网数据字典.xls";
    private static final String EXCEL_PATH = "C:\\Users\\Arvin\\Desktop\\百姓通谱网数据字典 (2).xls";

    private static final String SHEET_NAME = "数据字典";

    @Before
    public void ready() {
        foreignerMaps.put("creator_id", "User");
        foreignerMaps.put("publisher_id", "User");
        foreignerMaps.put("accepter_id", "User");
        foreignerMaps.put("agent_id", "User");
        foreignerMaps.put("operator_id", "User");
        foreignerMaps.put("owner_id", "User");
        foreignerMaps.put("author_id", "User");
        foreignerMaps.put("user_id", "User");
        foreignerMaps.put("role_id", "Role");
        foreignerMaps.put("visitor_id", "User");
        foreignerMaps.put("country_id", "Area");
        foreignerMaps.put("recommend_id", "User");
        foreignerMaps.put("parent_editor_id", "User");
        foreignerMaps.put("parent_surname_id", "User");
        foreignerMaps.put("partner_id", "Member");
        foreignerMaps.put("father_id", "Member");
        foreignerMaps.put("mother_id", "Member");
        foreignerMaps.put("idarea_id", "Area");
        foreignerMaps.put("birth_area_id", "Area");
        foreignerMaps.put("origin_area_id", "Area");
        foreignerMaps.put("qfparent_id", "Member");
        foreignerMaps.put("submitter_id", "User");
        foreignerMaps.put("applicant_id", "User");
        foreignerMaps.put("pf_id", "Family");
        foreignerMaps.put("sf_id", "Family");
        foreignerMaps.put("pf_start_id", "Member");
        foreignerMaps.put("sf_start_id", "Member");
        foreignerMaps.put("receiver_id", "User");
        foreignerMaps.put("send_apply_id", "FamilySendApply");
        foreignerMaps.put("approver_id", "User");
        foreignerMaps.put("deliver_id", "User");
        foreignerMaps.put("order_id", "PointOrder");
        foreignerMaps.put("uploader_id", "User");
        foreignerMaps.put("info_id", "FamilyInfo");
        foreignerMaps.put("province_id", "Area");
        foreignerMaps.put("voter_id", "User");
    }
    
    @Test
    public void testAnalyTableName() {
        String tableName = "user_auth(用户表)";

        String tableCNName = tableName.replaceFirst("^[a-z_]+\\(", "").replaceAll("\\)", "").replaceFirst("-[a-zA-Z_]+$", "");
        String tableENName = tableName.replace("(" + tableCNName + ")", "").replaceFirst("-[a-zA-Z_]+$", "");
        String domainName = tableName.replaceFirst("^.*-", "");
        if (domainName.equals(tableName)) { // 采用默认的方式
            domainName = getFirstUpperCaseString(getToFeng(tableENName));
        }
    }

    public static String getFirstUpperCaseString(String value) {
        if (null != value && !"".equals(value.trim())) {
            return String.valueOf(value.charAt(0)).toUpperCase() + value.substring(1);
        }
        return value;
    }

    public static String getToFeng(String value) {
        if (null != value && !"".equals(value.trim())) {
            String[] vals = value.split("_");
            if (vals.length > 1) {
                StringBuilder sb = new StringBuilder(vals[0]);
                for (int i = 1; i < vals.length; ++i) {
                    sb.append(getFirstUpperCaseString(vals[i]));
                }
                return sb.toString();
            }
            return value;
        }
        return value;
    }

    @Test
    public void test() throws Exception {
        Workbook wb = new HSSFWorkbook(new FileInputStream(EXCEL_PATH));
        Sheet sheet = wb.getSheet(SHEET_NAME);
        int begRowIndex = sheet.getFirstRowNum();
        int lastRowIndex = sheet.getLastRowNum();
        int tableOrder = 1;

        List<Table> tables = new ArrayList<Table>();

        for (int i = begRowIndex; i < lastRowIndex; ++i) {
            Row row = sheet.getRow(i);
            if (isBlankRow(row)) {  // 空行略过
                continue;
            }
            if (isTableRow(row)) {  // 如果是表行
                Cell tableNameCell = row.getCell(1);
                String tableName = tableNameCell.getStringCellValue();
                String tableCNName = tableName.replaceFirst("^[a-z_]+\\(", "").replaceAll("\\)", "").replaceFirst("-[a-zA-Z_]+$", "");
                String tableENName = tableName.replace("(" + tableCNName + ")", "").replaceFirst("-[a-zA-Z_]+$", "");
                String domainName = tableName.replaceFirst("^.*-", "");
                if (domainName.equals(tableName)) { // 采用默认的方式
                    domainName = getFirstUpperCaseString(getToFeng(tableENName));
                }

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
                    Cell fieldNameCell = fieldRow.getCell(1);
                    Cell fieldDescCell = fieldRow.getCell(2);
                    Cell fieldTypeCell = fieldRow.getCell(3);
                    Cell fieldNullCell = fieldRow.getCell(4);
                    Cell fieldPKCell = fieldRow.getCell(5);
                    Cell fieldDefaultCell = fieldRow.getCell(6);
                    Cell fieldAutoIncreCell = fieldRow.getCell(7);
                    Cell fieldUniqueCell = fieldRow.getCell(8);
                    Cell refTableCell = fieldRow.getCell(9);
                    Cell refFieldCell = fieldRow.getCell(10);
                    Cell javaFieldCell = fieldRow.getCell(11);
                    Cell propertyFieldCell = fieldRow.getCell(12);

                    tableFields.add(new TableField(//
                            tableFieldOrder++, //
                            fieldNameCell.toString(), //
                            fieldDescCell.toString(), //
                            fieldTypeCell.toString(), //
                            fieldNullCell.toString(), //
                            fieldDefaultCell == null ? null : fieldDefaultCell.toString(), //
                            getBoolean(fieldPKCell == null ? "NO" : fieldPKCell.toString()), //
                            getBoolean(fieldAutoIncreCell == null ? "NO" : fieldAutoIncreCell.toString()),//
                            getBoolean(fieldUniqueCell == null ? "NO" : fieldUniqueCell.toString()), //
                            refTableCell == null ? null : refTableCell.toString(),//
                            refFieldCell == null ? null : refFieldCell.toString(),//
                            javaFieldCell == null ? null : javaFieldCell.toString(),//
                            propertyFieldCell == null ? null : propertyFieldCell.toString()//
                            ));
                }
                i = j;
            }
        }
        String filename = "C:\\Users\\Arvin\\Desktop\\bxtpw-schema-ach.sql";
        String domainFilename = "C:\\Users\\Arvin\\Desktop\\bxtpw-domain.properties";
        String generatorConfigFilename = "C:\\Users\\Arvin\\Desktop\\bxtpw-generator-config.properties";
        writeTablesToSQL(tables, filename, domainFilename, generatorConfigFilename);
    }

    /**
     * 获取boolean值
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午4:55:34
     * @since 0.1
     * @param value
     * @return
     */
    public static final boolean getBoolean(String value) {
        return "YES".equals(value);
    }

    /**
     * 是否是空行
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午4:32:59
     * @since 0.1
     * @param row
     * @return
     */
    private boolean isBlankRow(Row row) {
        Cell cell = row.getCell(1);
        String value = cell.getStringCellValue();
        return null == value || "".equals(value.trim());
    }

    /**
     * 判断指定的row是不是表的row
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午4:27:22
     * @since 0.1
     * @param row
     * @return
     */
    private boolean isTableRow(Row row) {
        Cell cell = row.getCell(2);
        String value = cell.getStringCellValue();
        return null == value || "".equals(value.trim());
    }

    /**
     * 写入文件
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午4:52:13
     * @since 0.1
     * @param tables
     * @param filename
     */
    public static final void writeTablesToSQL(List<Table> tables, String filename, String domainFilename, String generatorFilename) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        BufferedWriter domainNameWriter = new BufferedWriter(new FileWriter(domainFilename));
        BufferedWriter generatorConfigWriter = new BufferedWriter(new FileWriter(generatorFilename));
        for (Table table : tables) {
            String tableBuildSQL = buildTableSQL(table);
            writer.write(tableBuildSQL);
            domainNameWriter.write(table.getDomainName() + "=" + table.getEnname() + "\n");
            generatorConfigWriter.write(buildGenerateConfig(table) + "\n\n");
        }
        writer.close();
        domainNameWriter.close();
        generatorConfigWriter.close();
    }

    /**
     * 构建MyBatis Generator 的配置
     * 
     * @author 夏集球
     * @time 2015年7月7日 下午8:49:21
     * @since 0.1
     * @param table
     * @return
     */
    public static final String buildGenerateConfig(Table table) {
        StringBuilder sb = new StringBuilder();
        sb.append(TAB_TWO).append("\n");
        sb.append(TAB_TWO).append("<table\n");
        sb.append(TAB_THREE).append("schema=\"").append(SCHEMA).append("\"\n");
        sb.append(TAB_THREE).append("tableName=\"").append(table.getEnname()).append("\"\n");
        sb.append(TAB_THREE).append("domainObjectName=\"").append(table.getDomainName()).append("\"\n");
        sb.append(TAB_THREE).append("enableCountByExample=\"false\"\n");
        sb.append(TAB_THREE).append("enableDeleteByPrimaryKey=\"false\"\n");
        sb.append(TAB_THREE).append("enableDeleteByExample=\"false\"\n");
        sb.append(TAB_THREE).append("enableSelectByExample=\"false\"\n");
        sb.append(TAB_THREE).append("enableUpdateByExample=\"false\">\n");
        List<TableField> fields = table.getFields();
        for (TableField field : fields) {
            if (field.isEnum() || isForeignerKey(field)) {   // 枚举属性，外键
                String javaType = getJavaType(field);
                String property = getJavaProperty(field);
                if (javaType.equals("Parent") || javaType.equals("Target")) {
                    javaType = table.getDomainName();
                }
                if (foreignerMaps.containsKey(field.getName())) {
                    javaType = foreignerMaps.get(field.getName());
                }
                sb.append(TAB_THREE).append("<columnOverride column=\"")//
                        .append(field.getName()).append("\" property=\"").append(property)//
                        .append("\" javaType=\"").append(javaType).append("\"/>\n");
            }
        }
        sb.append(TAB_TWO).append("</table>");
        return sb.toString();
    }

    private static boolean isForeignerKey(TableField field) {
        return field.getName().endsWith("_id") //
                || field.getName().equals("point_account")//
                || field.getName().equals("auto_code");
    }

    /**
     * 获取枚举类型的java类名
     * 
     * @author 夏集球
     * @time 2015年7月7日 下午9:18:14
     * @since 0.1
     * @param field
     * @return
     */
    public static String getJavaType(TableField field) {
        if (!isBlank(field.getJavaType())) {
            return field.getJavaType();
        }
        if (field.isEnum()) {
            return getFirstUpperCaseString(getToFeng(field.getName()));
        } else {
            return getFirstUpperCaseString(getToFeng(field.getName()).replaceAll("Id$", ""));
        }
    }

    public static String getJavaProperty(TableField field) {
        if (!isBlank(field.getProperty())) {
            return field.getProperty();
        }
        System.out.println(getToFeng(field.getName()));
        return getToFeng(field.getName())//
                .replaceAll("Id$", "");
    }

    /**
     * <pre>
     * -- 删除表：员工表
     * DROP TABLE IF EXISTS employee;
     * -- 创建表：员工表
     * CREATE TABLE employee ( 
     *     id char(36) NOT NULL COMMENT '唯一主键：user(id), 直接取user表的ID一致',
     *     emergency_name varchar(25) NULL COMMENT '紧急联系人姓名',
     *     emergency_phone char(11) NULL COMMENT '紧急联系人电话号码',
     *     department_id int(2) NULL COMMENT '所属部门外键ID：department(id)',
     *     position_id int(2) NULL COMMENT '所属职位外键ID：position(id)',
     *     position_category_id int(2) NULL COMMENT '职位类型外键ID：position_category(id)',
     *     creator_id char(36) NULL COMMENT '创建者ID：user(id)',
     *     idarea_id int(11) NULL COMMENT '证件地址ID：common(id)',
     *     idarea_details varchar(50) NULL COMMENT '证件地址详情',
     *     PRIMARY KEY(id)
     * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工表';
     * </pre>
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午5:03:36
     * @since 0.1
     * @param table
     * @return
     */
    public static final String buildTableSQL(Table table) {
        StringBuilder sb = new StringBuilder();
        String nextLine = "\n";
        sb.append("-- 删除表：").append(table.getCnname()).append(nextLine);
        sb.append("DROP TABLE IF EXISTS `").append(table.getEnname()).append("`;").append(nextLine);
        sb.append("-- 创建表：").append(table.getCnname()).append(nextLine);
        sb.append("CREATE TABLE `").append(table.getEnname()).append("` (").append(nextLine);

        int len = table.getFields().size();
        for (int i = 0; i < len; ++i) {
            TableField field = table.getFields().get(i);
            sb.append(buildTableFileSQL(field));
            if (i != len - 1) {
                sb.append(",").append(nextLine);
            }
        }

        // 获取外键属性
        List<TableField> foreignerFields = table.getForeignerFields();
        if (foreignerFields != null || table.hasPK()) {
            if (foreignerFields != null) {
                sb.append(",").append(nextLine);
                int size = foreignerFields.size();
                for (int i = 0; i < size; ++i) {
                    TableField field = foreignerFields.get(i);
                    sb.append("    ").append("FOREIGN KEY(`").append(field.getName()).append("`) REFERENCES `").append(field.getRefTable()).append("`(`")
                            .append(field.getRefField()).append("`)");
                    if (i != size - 1) {
                        sb.append(",").append(nextLine);
                    }
                }
            }

            if (table.hasPK()) {    // 有主键
                sb.append(",").append(nextLine);
                sb.append("    ").append("PRIMARY KEY(`").append(table.getPKS().replaceAll(",", "`,`")).append("`)").append(nextLine);
            }
        } else {
            sb.append(nextLine);
        }
        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='").append(table.getCnname()).append("';").append(nextLine);
        sb.append("\n");
        return sb.toString();
    }

    /**
     * 创建字段的SQL， name type unique isnull autoIncre comment,
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午5:14:34
     * @since 0.1
     * @param field
     * @return
     */
    private static String buildTableFileSQL(TableField field) {
        StringBuilder sb = new StringBuilder("   ");
        String blank = " ";
        String unique = "UNIQUE";
        String autoIncrement = "AUTO_INCREMENT";
        sb.append("`").append(field.getName()).append("`").append(blank);
        sb.append(field.getType()).append(blank);
        if (field.isUnique()) { // 唯一
            sb.append(unique).append(blank);
        }
        if ("NOT NULL".equalsIgnoreCase(field.getIsnull())) {
            sb.append(field.getIsnull()).append(blank);
        }
        if (field.isAutoIncre()) {  // 自增
            sb.append(autoIncrement).append(blank);
        }
        if (isNotBlank(field.getDefaultVal())) {    // 默认值
            if (isNotNumber(field.getDefaultVal())) {   // 如果不是数字
                if ("NULL".equalsIgnoreCase(field.getDefaultVal())) {
                    sb.append("DEFAULT ").append(field.getDefaultVal()).append(blank);
                } else {
                    sb.append("DEFAULT '").append(field.getDefaultVal()).append("'").append(blank);
                }
            } else {
                sb.append("DEFAULT ").append(field.getDefaultVal()).append(blank);
            }
        }
        if (isNotBlank(field.getDesc())) {
            sb.append("COMMENT '").append(getCleanComment(field.getDesc())).append("'");
        }
        sb.append(",");
        return sb.toString().replaceAll(" +,", ",").replaceFirst(",$", "");
    }

    /**
     * 获取注释
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午5:51:25
     * @since 0.1
     * @param desc
     * @return
     */
    public static final String getCleanComment(String comment) {
        return isNotBlank(comment) ? comment.replaceAll("'", "\"") : comment;
    }

    /**
     * 判断是不是数字字符串
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午5:26:50
     * @since 0.1
     * @param defaultVal
     * @return
     */
    public static final boolean isNotNumber(String value) {
        return !value.matches("^[0-9]*\\.?[0-9]*$");
    }

    @Test
    public void testIsNotNumber() {
        System.out.println(isNotNumber("asdad"));
        System.out.println(isNotNumber("151321"));
        System.out.println(isNotNumber("0"));
        System.out.println(isNotNumber("0.2"));
    }

    /**
     * 是否为空
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午5:23:40
     * @since 0.1
     * @param value
     * @return
     */
    public static final boolean isBlank(String value) {
        return null == value || "".equals(value);
    }

    /**
     * 非空判断
     * 
     * @author 夏集球
     * @time 2015年6月13日 下午5:24:07
     * @since 0.1
     * @param value
     * @return
     */
    public static final boolean isNotBlank(String value) {
        return !isBlank(value);
    }
}
