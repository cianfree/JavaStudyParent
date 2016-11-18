package Database;

import java.util.Date;

/**
 * @author Arvin
 * @time 2016/11/9 14:22
 */
public class Book {

    private int id;

    private String name;

    /** 所属出版社ID */
    private int pressId;

    /** 年级， 1-12, 详见ModelConstants.GRADE_* 定义的常量 */
    private int grade;

    /** 属于第几册， 上中下， 1-上册，2-下册，3-全册， 参考ModelConstants.VOLUME_* */
    private int volume;

    /** 小封面， BS2的完整路径 */
    private String cover;

    private String bigCover;

    /** 状态， 参考 ModelConstants.STATUS_* */
    private int status;
    /** 创建时间 */
    private Date createdTime;
    /** 条形码 */
    private String barcode;
    /** 修改次数 */
    private int modifyTimes = 0;
    /** 版本号 */
    private int version = 0;

    public Book() {
    }

    public Book(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPressId() {
        return pressId;
    }

    public void setPressId(int pressId) {
        this.pressId = pressId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBigCover() {
        return bigCover;
    }

    public void setBigCover(String bigCover) {
        this.bigCover = bigCover;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getModifyTimes() {
        return modifyTimes;
    }

    public void setModifyTimes(int modifyTimes) {
        this.modifyTimes = modifyTimes;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
