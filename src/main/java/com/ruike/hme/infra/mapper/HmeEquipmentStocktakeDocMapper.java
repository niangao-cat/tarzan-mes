package com.ruike.hme.infra.mapper;

import com.ruike.hme.api.dto.query.HmeEquipmentStocktakeDocQuery;
import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeDoc;
import com.ruike.hme.domain.vo.HmeEquipmentStocktakeExportVO;
import io.choerodon.mybatis.common.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备盘点单Mapper
 *
 * @author yonghui.zhu@hand-china.com 2021-03-31 09:32:46
 */
public interface HmeEquipmentStocktakeDocMapper extends BaseMapper<HmeEquipmentStocktakeDoc> {

    /**
     * 查询展示数据
     *
     * @param query 查询
     * @return java.util.List<com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 10:10:07
     */
    List<HmeEquipmentStocktakeDocRepresentation> selectRepresentationList(@Param("query") HmeEquipmentStocktakeDocQuery query);

    /**
     * 查询导出列表
     *
     * @param query 导出条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentStocktakeExportVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/20 02:39:36
     */
    List<HmeEquipmentStocktakeExportVO> selectExportList(@Param("query") HmeEquipmentStocktakeDocQuery query);
}
