package tarzan.material.domain.vo;

import java.io.Serializable;

/**
 * @author peng.yuan
 * @ClassName MtPfepInventoryVO8
 * @description
 * @date 2019年10月11日 15:23
 */
public class MtPfepInventoryVO8 implements Serializable {
    private static final long serialVersionUID = -3552312676819683868L;

    private String tableName;// 表名
    private String keyId;// 主键ID

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}
