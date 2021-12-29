package com.ruike.hme.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.ruike.hme.api.dto.HmeEoJobTimeMaterialDTO;
import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeEoJobTimeMaterial;
import com.ruike.hme.domain.vo.HmeEoJobSnVO2;
import com.ruike.hme.domain.vo.HmeEoJobTimeMaterialVO;
import com.ruike.hme.domain.vo.HmeEoJobMaterialVO;

/**
 * 工序作业平台-时效投料资源库
 *
 * @author liyuan.lv@hand-china.com 2020-03-22 17:08:55
 */
public interface HmeEoJobTimeMaterialRepository extends BaseRepository<HmeEoJobTimeMaterial>, AopProxy<HmeEoJobTimeMaterialRepository> {

    /**
     * 初始化时效物料
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param availableTime 有效时间
     * @param componentQty 组件数量
     * @param dto  初始化参数
     * @author liyuan.lv@hand-china.com 20.7.15 03:05:40
     * @return com.ruike.hme.domain.vo.HmeEoJobTimeMaterialVO
     */
    HmeEoJobTimeMaterialVO initTimeMaterial(Long tenantId,
                                            String bomComponentId,
                                            String materialId,
                                            String availableTime,
                                            BigDecimal componentQty,
                                            HmeEoJobSnVO2 dto);

    /**
     * 初始化时效物料
     *
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param availableTime 有效时间
     * @param componentQty 组件数量
     * @param dto  初始化参数
     * @author penglin.sui@hand-china.com 20.9.14 15 03:05:40
     * @return com.ruike.hme.domain.vo.HmeEoJobTimeMaterialVO
     */
    public List<HmeEoJobTimeMaterialVO> initTimeMaterialList(Long tenantId,
                                                             String materialId,
                                                             String availableTime,
                                                             BigDecimal componentQty,
                                                             HmeEoJobSnVO2 dto);

    /**
     *
     * @Description 初始化时效物料投料数据
     *
     * @author yuchao.wang
     * @date 2021/1/2 23:53
     * @param tenantId 租户ID
     * @param materialId 物料ID
     * @param availableTime 有效时间
     * @param componentQty 组件数量
     * @param dto  初始化参数
     * @return void
     *
     */
    void initTimeMaterialWithoutQuery(Long tenantId, String materialId, String availableTime, BigDecimal componentQty, HmeEoJobSnVO2 dto);

    /**
     * 时效物料数据查询
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return List<HmeEoJobTimeMaterialVO>
     */
    List<HmeEoJobTimeMaterialVO> matchedJobTimeMaterialQuery(Long tenantId, HmeEoJobMaterialVO dto, List<HmeEoJobTimeMaterial> timeMaterialList);

    /**
     * 投料扫描
     *
     * @param tenantId 租户ID
     * @param dto      扫描参数
     */
    List<HmeEoJobTimeMaterialVO> releaseScan(Long tenantId, HmeEoJobMaterialVO dto);

    /**
     * 删除批次物料
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return
     * @author penglin.sui@hand-china.com 2020-08-29 16:19
     */
    List<HmeEoJobTimeMaterialVO> deleteTimeMaterial(Long tenantId , HmeEoJobMaterialVO dto);

    /**
     * 更新投料标识
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return
     * @author penglin.sui@hand-china.com 2020-08-31 16:24
     */
    HmeEoJobTimeMaterialVO updateIsReleased(Long tenantId , HmeEoJobTimeMaterialVO dto);

    /**
     * 批量更新投料标识
     *
     * @param tenantId 租户ID
     * @param dtoList      查询参数
     * @return
     * @author penglin.sui@hand-china.com 2020-09-03 10:25
     */
    List<HmeEoJobTimeMaterialVO> batchUpdateIsReleased(Long tenantId , List<HmeEoJobTimeMaterialVO> dtoList);

    /**
     * 更新条码投料量
     *
     * @param tenantId 租户ID
     * @param dto      查询参数
     * @return
     * @author penglin.sui@hand-china.com 2020-09-14 16:24
     */
    HmeEoJobTimeMaterialVO updateReleaseQty(Long tenantId , HmeEoJobTimeMaterialVO dto);
}
