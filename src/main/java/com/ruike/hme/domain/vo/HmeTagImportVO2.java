package com.ruike.hme.domain.vo;

import io.tarzan.common.domain.vo.MtExtendVO5;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 数据收集项-导入VO
 *
 * @author sanfeng.zhang@hand-china.com 2020/8/4 15:41
 */
@Data
public class HmeTagImportVO2 extends HmeTagImportVO implements Serializable {

    private static final long serialVersionUID = -3428462294311768094L;

    private List<MtExtendVO5> attrList;
}
