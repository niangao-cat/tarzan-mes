package com.ruike.hme.api.dto;

import com.ruike.hme.domain.vo.MaterialLotVO;
import com.ruike.hme.domain.vo.WipStocktakeMaterialLotWorkVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 在制品盘点条码扫描返回数据
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/8 14:14
 */
@Data
public class WipStocktakeBarcodeScanResponseDTO implements Serializable {
    private static final long serialVersionUID = 4667864558545236127L;

    @ApiModelProperty("装载对象类型")
    private String loadObjectType;
    @ApiModelProperty("装载对象编码")
    private String loadObjectCode;
    @ApiModelProperty("装载对象Id")
    private String loadObjectId;
    @ApiModelProperty("装载物料批列表")
    private List<WipStocktakeMaterialLotWorkVO> loadedMaterialLotList;
    @ApiModelProperty("不在范围内的物料Id")
    private List<String> notInRangeMaterialIds;
    @ApiModelProperty("校验未通过的物料批")
    private List<MaterialLotVO> invalidMaterialLots;
    @ApiModelProperty("警告消息列表")
    private List<String> warnings;

    public WipStocktakeBarcodeScanResponseDTO() {
    }

    public WipStocktakeBarcodeScanResponseDTO(String loadObjectType, String loadObjectCode, String loadObjectId) {
        this.loadObjectType = loadObjectType;
        this.loadObjectCode = loadObjectCode;
        this.loadObjectId = loadObjectId;
    }

    public void addWarning(String message) {
        if (StringUtils.isNotBlank(message)) {
            List<String> warnings = CollectionUtils.isEmpty(this.getWarnings()) ? new ArrayList<>() : this.getWarnings();
            warnings.add(message);
            this.setWarnings(warnings);
        }
    }
}
