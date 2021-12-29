package tarzan.general.api.dto;

import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.swagger.annotations.ApiModelProperty;
import tarzan.general.domain.entity.MtEventObjectTypeRel;

import java.io.Serializable;

public class MtEventObjectTypeRelDTO2 extends MtEventObjectTypeRel implements Serializable {

    private static final long serialVersionUID = -6461207653145339298L;

    public String getObjectTypeCode() {
        return objectTypeCode;
    }

    public void setObjectTypeCode(String objectTypeCode) {
        this.objectTypeCode = objectTypeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ApiModelProperty(value = "对象类型编码")
    private String objectTypeCode;
    @ApiModelProperty(value = "对象类型描述")
    @MultiLanguageField
    private String description;
}
