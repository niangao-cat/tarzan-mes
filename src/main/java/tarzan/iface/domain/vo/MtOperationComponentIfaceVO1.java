package tarzan.iface.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/7/11 13:49
 */
public class MtOperationComponentIfaceVO1 implements Serializable {

    private static final long serialVersionUID = 1942520986334015748L;
    private String bomId;
    private String bomComponentId;
    private String bomCode;
    private String bomType;
    private String bomAlternate;
    private Long lineNum;

    public String getBomId() {
        return bomId;
    }

    public void setBomId(String bomId) {
        this.bomId = bomId;
    }

    public String getBomComponentId() {
        return bomComponentId;
    }

    public void setBomComponentId(String bomComponentId) {
        this.bomComponentId = bomComponentId;
    }

    public String getBomCode() {
        return bomCode;
    }

    public void setBomCode(String bomCode) {
        this.bomCode = bomCode;
    }

    public String getBomType() {
        return bomType;
    }

    public void setBomType(String bomType) {
        this.bomType = bomType;
    }

    public String getBomAlternate() {
        return bomAlternate;
    }

    public void setBomAlternate(String bomAlternate) {
        this.bomAlternate = bomAlternate;
    }

    public Long getLineNum() {
        return lineNum;
    }

    public void setLineNum(Long lineNum) {
        this.lineNum = lineNum;
    }
}
