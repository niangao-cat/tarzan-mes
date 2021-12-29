package tarzan.inventory.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname MtInvJournalDTO4
 * @Description 库存日记账报表
 * @Date 2020/10/19 11:40
 * @Author yuchao.wang
 */
@Data
public class MtInvJournalDTO4 extends MtInvJournalDTO {
    private static final long serialVersionUID = 4389041860152125339L;

    @ApiModelProperty(value = "货位ID")
    private String locatorId;

    @ApiModelProperty(value = "仓库ID")
    private String warehouseId;

    @ApiModelProperty(value = "事件类型ID")
    private String eventTypeId;
}