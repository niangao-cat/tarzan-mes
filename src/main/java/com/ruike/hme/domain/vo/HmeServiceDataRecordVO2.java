package com.ruike.hme.domain.vo;

import com.ruike.hme.domain.entity.HmeServiceDataRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sanfeng.zhang@hand-china.com 2020/9/3 19:05
 */
@Data
public class HmeServiceDataRecordVO2 extends HmeServiceDataRecord implements Serializable {

    private static final long serialVersionUID = 6408933615153535763L;

    @ApiModelProperty(value = "数据组描述")
    private String tagGroupDescription;

    @ApiModelProperty(value = "附件名称")
    private String attachmentName;

    @ApiModelProperty(value = "Id信息")
    private List<String> tagIdList;

    @ApiModelProperty(value = "描述信息")
    private List<String> tagDescList;

    private List<HmeServiceDataRecordVO3> tagList;
}
