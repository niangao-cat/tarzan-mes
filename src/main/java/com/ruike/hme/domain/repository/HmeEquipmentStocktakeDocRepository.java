package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.query.HmeEquipmentStocktakeDocQuery;
import com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation;
import com.ruike.hme.domain.entity.HmeEquipmentStocktakeDoc;
import com.ruike.hme.domain.vo.HmeEquipmentStocktakeExportVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.apache.ibatis.annotations.Param;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 设备盘点单资源库
 *
 * @author yonghui.zhu@hand-china.com 2021-03-31 09:32:46
 */
public interface HmeEquipmentStocktakeDocRepository extends BaseRepository<HmeEquipmentStocktakeDoc> {

    /**
     * 保存
     *
     * @param entity 实体
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 09:40:13
     */
    void save(HmeEquipmentStocktakeDoc entity);

    /**
     * 根据ID查询
     *
     * @param stocktakeId 盘点单
     * @param tenantId    租户
     * @return com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 09:40:13
     */
    HmeEquipmentStocktakeDocRepresentation byId(String stocktakeId, Long tenantId);

    /**
     * 根据单号查询
     *
     * @param stocktakeNum 盘点单
     * @param tenantId     租户
     * @return com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 09:40:13
     */
    HmeEquipmentStocktakeDocRepresentation byDocNum(String stocktakeNum, Long tenantId);

    /**
     * 查询展示数据
     *
     * @param tenantId    租户
     * @param query       查询
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.hme.api.dto.representation.HmeEquipmentStocktakeDocRepresentation>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/31 10:10:07
     */
    Page<HmeEquipmentStocktakeDocRepresentation> page(Long tenantId,
                                                      HmeEquipmentStocktakeDocQuery query,
                                                      PageRequest pageRequest);

    /**
     * 导出
     *
     * @param tenantId    租户
     * @param query 导出条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeEquipmentStocktakeExportVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/20 02:39:36
     */
    List<HmeEquipmentStocktakeExportVO> export(Long tenantId, @Param("query") HmeEquipmentStocktakeDocQuery query);
}
