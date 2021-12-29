package tarzan.iface.domain.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Leeloing
 * @date 2019/7/11 13:59
 */
public class MtOperationComponentIfaceVO3 implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1893253172988278835L;
    private String plantCode;
    private String routerObjectType;
    private String routerCode;
    private String routerAlternate;
    private String bomObjectType;
    private String bomCode;
    private String bomAlternate;

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getRouterObjectType() {
        return routerObjectType;
    }

    public void setRouterObjectType(String routerObjectType) {
        this.routerObjectType = routerObjectType;
    }

    public String getRouterCode() {
        return routerCode;
    }

    public void setRouterCode(String routerCode) {
        this.routerCode = routerCode;
    }

    public String getRouterAlternate() {
        return routerAlternate;
    }

    public void setRouterAlternate(String routerAlternate) {
        this.routerAlternate = routerAlternate;
    }

    public String getBomObjectType() {
        return bomObjectType;
    }

    public void setBomObjectType(String bomObjectType) {
        this.bomObjectType = bomObjectType;
    }

    public String getBomCode() {
        return bomCode;
    }

    public void setBomCode(String bomCode) {
        this.bomCode = bomCode;
    }

    public String getBomAlternate() {
        return bomAlternate;
    }

    public void setBomAlternate(String bomAlternate) {
        this.bomAlternate = bomAlternate;
    }

    public MtOperationComponentIfaceVO3(String plantCode, String routerObjectType, String routerCode, String routerAlternate,
                    String bomObjectType, String bomCode, String bomAlternate) {
        this.plantCode = plantCode;
        this.routerObjectType = routerObjectType;
        this.routerCode = routerCode;
        this.routerAlternate = routerAlternate;
        this.bomObjectType = bomObjectType;
        this.bomCode = bomCode;
        this.bomAlternate = bomAlternate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MtOperationComponentIfaceVO3 mtOperationComponentIfaceVO3 = (MtOperationComponentIfaceVO3) o;
        return Objects.equals(plantCode, mtOperationComponentIfaceVO3.plantCode)
                && Objects.equals(routerObjectType, mtOperationComponentIfaceVO3.routerObjectType)
                && Objects.equals(routerCode, mtOperationComponentIfaceVO3.routerCode)
                && Objects.equals(routerAlternate, mtOperationComponentIfaceVO3.routerAlternate)
                && Objects.equals(bomObjectType, mtOperationComponentIfaceVO3.bomObjectType)
                && Objects.equals(bomCode, mtOperationComponentIfaceVO3.bomCode)
                && Objects.equals(bomAlternate, mtOperationComponentIfaceVO3.bomAlternate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plantCode, routerObjectType, routerCode, routerAlternate, bomObjectType, bomCode,
                bomAlternate);
    }
}
