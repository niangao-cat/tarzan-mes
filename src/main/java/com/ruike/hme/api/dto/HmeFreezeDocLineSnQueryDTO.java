package com.ruike.hme.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.util.Strings;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 条码冻结单行 通过SN查询条件
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/25 17:06
 */
@Data
public class HmeFreezeDocLineSnQueryDTO {
    @ApiModelProperty(value = "冻结单ID")
    @NotBlank
    private String freezeDocId;

    @ApiModelProperty(value = "条码code", notes = "前端传此参数，后端处理到materialLotCodes中")
    private String materialLotCode;

    @ApiModelProperty(value = "条码集合", notes = "前端勿传")
    private Set<String> materialLotCodes;

    private HmeFreezeDocLineSnQueryDTO(Builder builder) {
        setFreezeDocId(builder.freezeDocId);
        setMaterialLotCodes(builder.materialLotCodeList);
    }

    public HmeFreezeDocLineSnQueryDTO() {
    }

    public void paramInit() {
        this.setMaterialLotCodes(StringUtils.isBlank(materialLotCode) ? new HashSet<>() : new HashSet<>(Arrays.asList(Strings.split(this.materialLotCode, ','))));
    }

    public static final class Builder {
        private @NotBlank String freezeDocId;
        private Set<String> materialLotCodeList;

        public Builder() {
        }

        public Builder freezeDocId(@NotBlank String val) {
            freezeDocId = val;
            return this;
        }

        public Builder materialLotCodeList(Set<String> val) {
            materialLotCodeList = val;
            return this;
        }

        public HmeFreezeDocLineSnQueryDTO build() {
            return new HmeFreezeDocLineSnQueryDTO(this);
        }
    }
}
