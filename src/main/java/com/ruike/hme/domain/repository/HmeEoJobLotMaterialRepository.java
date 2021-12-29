package com.ruike.hme.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.ruike.hme.api.dto.HmeEoJobLotMaterialDTO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEoJobLotMaterial;
import com.ruike.hme.domain.vo.HmeEoJobLotMaterialVO;
import com.ruike.hme.domain.vo.HmeEoJobMaterialVO;
import com.ruike.hme.domain.vo.HmeEoJobSnVO2;

/**
 * 工序作业平台-批次物料投料资源库
 *
 * @author liyuan.lv@hand-china.com 2020-03-21 17:55:01
 */
public interface HmeEoJobLotMaterialRepository extends BaseRepository<HmeEoJobLotMaterial>, AopProxy<HmeEoJobLotMaterialRepository> {

    /**
     * 初始化批次物料投料数据
     *
     * @param tenantId     租户ID
     * @param bomComponentId  组件ID
     * @param materialId   物料ID
     * @param componentQty 组件数量
     * @param dto          数据
     * @return HmeEoJobLotMaterialVO
     * @author liyuan.lv@hand-china.com 2020-03-21 17:55:01
     */
    HmeEoJobLotMaterialVO initLotMaterial(Long tenantId,
                                          String bomComponentId,
                                          String materialId,
                                          BigDecimal componentQty,
                                          HmeEoJobSnVO2 dto);

    /**
     * 初始化批次物料投料数据
     *
     * @param tenantId     租户ID
     * @param materialId   物料ID
     * @param componentQty 组件数量
     * @param dto          数据
     * @return HmeEoJobLotMaterialVO
     * @author liyuan.lv@hand-china.com 2020-03-21 17:55:01
     */
    List<HmeEoJobLotMaterialVO> initLotMaterialList(Long tenantId,
                                                    String materialId,
                                                    BigDecimal componentQty,
                                                    HmeEoJobSnVO2 dto);

    /**
     *
     * @Description 初始化批次物料投料数据
     *
     * @author yuchao.wang
     * @date 2021/1/2 23:35
     * @param tenantId     租户ID
     * @param materialId   物料ID
     * @param componentQty 组件数量
     * @param dto          数据
     * @return void
     *
     */
    void initLotMaterialWithoutQuery(Long tenantId, String materialId, BigDecimal componentQty, HmeEoJobSnVO2 dto);

    /**
     * 获取当前工位的批次物料投料数据
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return List<HmeEoJobLotMaterialVO>
     * @author liyuan.lv@hand-china.com 2020-03-21 17:55:01
     */
    List<HmeEoJobLotMaterialVO> matchedJobLotMaterialQuery(Long tenantId,
                                                           HmeEoJobMaterialVO dto,
                                                           List<HmeEoJobLotMaterial> lotMaterialList);

    /**
     * 批次物料投料扫描
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return List<HmeEoJobLotMaterialVO>
     * @author liyuan.lv@hand-china.com 2020-03-21 17:55:01
     */
    List<HmeEoJobLotMaterialVO> releaseScan(Long tenantId, HmeEoJobMaterialVO dto);

    /**
     * 删除批次物料
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return
     * @author penglin.sui@hand-china.com 2020-08-29 16:19
     */
    List<HmeEoJobLotMaterialVO> deleteLotMaterial(Long tenantId , HmeEoJobMaterialVO dto);

    /**
     * 更新是否投料标识
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return
     * @author penglin.sui@hand-china.com 2020-08-31 16:24
     */
    HmeEoJobLotMaterialVO updateIsReleased(Long tenantId , HmeEoJobLotMaterialVO dto);

    /**
     * 批量更新是否投料标识
     *
     * @param tenantId 租户ID
     * @param dtoList      查询参数
     * @return
     * @author penglin.sui@hand-china.com 2020-09-03 10:17
     */
    List<HmeEoJobLotMaterialVO> batchUpdateIsReleased(Long tenantId , List<HmeEoJobLotMaterialVO> dtoList);

    /**
     * 更新条码投料量
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return
     * @author penglin.sui@hand-china.com 2020-08-31 16:24
     */
    HmeEoJobLotMaterialVO updateReleaseQty(Long tenantId , HmeEoJobLotMaterialVO dto);
}
