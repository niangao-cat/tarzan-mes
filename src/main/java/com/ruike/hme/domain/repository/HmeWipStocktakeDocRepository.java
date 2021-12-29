package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.*;
import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO;
import com.ruike.hme.domain.vo.HmeWipStocktakeDocVO3;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeWipStocktakeDoc;
import tarzan.modeling.domain.entity.MtModArea;
import tarzan.modeling.domain.entity.MtModProductionLine;
import tarzan.modeling.domain.entity.MtModWorkcell;

import java.util.List;
import java.util.Map;

/**
 * 在制盘点单资源库
 *
 * @author chaonan.hu@hand-china.com 2021-03-03 13:48:57
 */
public interface HmeWipStocktakeDocRepository extends BaseRepository<HmeWipStocktakeDoc> {


    /**
     * 部门LOV查询
     * 
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/3 15:27:32
     * @return java.util.List<tarzan.modeling.domain.entity.MtModArea>
     */
    Page<HmeWipStocktakeDocDTO2> departmentLovQuery(Long tenantId, HmeWipStocktakeDocDTO2 mtModArea, PageRequest pageRequest);

    /**
     * 产线LOV查询
     * 
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/3 14:47:27
     * @return tarzan.modeling.domain.entity.MtModProductionLine
     */
    Page<HmeWipStocktakeDocDTO3> prodLineLovQuery(Long tenantId, HmeWipStocktakeDocDTO3 mtModProductionLine, PageRequest pageRequest);

    /**
     * 工序LOV查询
     *
     * @param tenantId 租户ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/3 15:13:04
     * @return java.util.List<tarzan.modeling.domain.entity.MtModWorkcell>
     */
    Page<HmeWipStocktakeDocDTO4> processLovQuery(Long tenantId, HmeWipStocktakeDocDTO4 mtModWorkcell, PageRequest pageRequest);

    /**
     * 创建盘点单以及盘点氛围
     *
     * @param tenantId 租户ID
     * @param dto 创建信息
     * @param cosFlag 盘点单类型
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/5 05:19:46
     * @return com.ruike.hme.api.dto.HmeWipStocktakeDocDTO6
     */
    HmeWipStocktakeDocDTO6 createStocktakeDoc(Long tenantId, HmeWipStocktakeDocDTO6 dto, String cosFlag);

    /**
     * 更新盘点单头信息
     *
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/7 05:40:01
     * @return com.ruike.hme.domain.entity.HmeWipStocktakeDoc
     */
    HmeWipStocktakeDoc updateStocktakeDoc(Long tenantId, HmeWipStocktakeDocDTO9 dto);

    /**
     * 批量删除盘点范围数据
     *
     * @param tenantId 租户ID
     * @param stocktakeRangeIdList 盘点范围数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/7 06:11:59
     * @return void
     */
    void deleteStocktakeRange(Long tenantId, List<String> stocktakeRangeIdList);

    /**
     * 新增盘点范围
     *
     * @param tenantId 租户ID
     * @param dto 盘点范围数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/8 01:43:57
     * @return com.ruike.hme.api.dto.HmeWipStocktakeDocDTO11
     */
    HmeWipStocktakeDocDTO11 addStocktakeRange(Long tenantId,  HmeWipStocktakeDocDTO11 dto);

    /**
     * 盘点单下达
     *
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/8 03:40:29
     * @return void
     */
    void releasedWipStocktake(Long tenantId, HmeWipStocktakeDocVO dto);

    /**
     * 盘点单完成
     *
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/8 06:04:39
     * @return void
     */
    void completedWipStocktake(Long tenantId, HmeWipStocktakeDocVO dto);

    /**
     * 关闭盘点单
     *
     * @param tenantId 租户ID
     * @param dto 盘点单信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/3/9 09:45:30
     * @return void
     */
    void closedWipStocktake(Long tenantId, HmeWipStocktakeDocVO dto);

    /**
     * 根据eoId查询对应的返修条码
     *
     * @param tenantId 租户ID
     * @param eoIdList eoId集合
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/9/23 10:18:33
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    Map<String, String> getRepairMaterialLotByEo(Long tenantId, List<String> eoIdList);
}
