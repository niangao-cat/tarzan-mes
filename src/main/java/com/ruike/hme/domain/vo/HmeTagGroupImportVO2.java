package com.ruike.hme.domain.vo;

import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 数据收集组-导入VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/4 19:20
 */
@Data
public class HmeTagGroupImportVO2 extends HmeTagGroupImportVO implements Serializable {

    private static final long serialVersionUID = 6647197166484730518L;

    private List<MtExtendAttrDTO3> attrList;
}
