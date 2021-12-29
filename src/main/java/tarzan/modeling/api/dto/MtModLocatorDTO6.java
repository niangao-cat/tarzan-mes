package tarzan.modeling.api.dto;

import java.io.Serializable;
import java.util.List;

/**
 * LOV 查询使用DTO
 * 
 * @author benjamin
 */
public class MtModLocatorDTO6 implements Serializable {
    private static final long serialVersionUID = -1488808725110386598L;

    private List<String> locatorIdList;
    private String locatorCode;
    private String locatorName;

    public List<String> getLocatorIdList() {
        return locatorIdList;
    }

    public void setLocatorIdList(List<String> locatorIdList) {
        this.locatorIdList = locatorIdList;
    }

    public String getLocatorCode() {
        return locatorCode;
    }

    public void setLocatorCode(String locatorCode) {
        this.locatorCode = locatorCode;
    }

    public String getLocatorName() {
        return locatorName;
    }

    public void setLocatorName(String locatorName) {
        this.locatorName = locatorName;
    }
}
