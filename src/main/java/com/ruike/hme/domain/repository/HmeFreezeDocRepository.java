package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeFreezeDocQueryDTO;
import com.ruike.hme.domain.entity.HmeFreezeDoc;
import com.ruike.hme.domain.entity.HmeMaterialLotLoad;
import com.ruike.hme.domain.vo.HmeFreezeDocCreateSnVO;
import com.ruike.hme.domain.vo.HmeFreezeDocTrxVO;
import com.ruike.hme.domain.vo.HmeFreezeDocVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 条码冻结单资源库
 *
 * @author yonghui.zhu@hand-china.com 2021-02-22 15:44:42
 */
public interface HmeFreezeDocRepository extends BaseRepository<HmeFreezeDoc> {

    /**
     * 查询展示字段列表
     *
     * @param tenantId    租户
     * @param dto         查询条件
     * @param pageRequest 分页参数
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/22 04:30:25
     */
    Page<HmeFreezeDocVO> pageList(Long tenantId,
                                  HmeFreezeDocQueryDTO dto,
                                  PageRequest pageRequest);

    /**
     * 根据ID查询
     *
     * @param tenantId    租户
     * @param freezeDocId 冻结单ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/22 04:30:25
     */
    HmeFreezeDocVO byId(Long tenantId, String freezeDocId);

    /**
     * 查询SN列表
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/22 04:30:25
     */
    List<HmeFreezeDocCreateSnVO> snList(Long tenantId,
                                        HmeFreezeDocQueryDTO dto);

    /**
     * 根据查询条件确定要冻结的条码
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @author chaonan.hu chaonan.hu@hand-china.com 2021/7/3 04:38:58
     * @return java.util.List<java.lang.String>
     */
    List<String> selectFreezeMaterialLotIdList(Long tenantId, HmeFreezeDocQueryDTO dto);

    /**
     * 查询冻结物料批事务列表
     *
     * @param tenantId       租户
     * @param materialLotIds 条码
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocTrxVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 02:38:14
     */
    List<HmeFreezeDocTrxVO> materialLotFreezeTrxListGet(Long tenantId,
                                                        List<String> materialLotIds);

    /**
     * 查询冻结物料批事务列表
     *
     * @param tenantId       租户
     * @param materialLotIds 条码
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocTrxVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 02:38:14
     */
    List<HmeFreezeDocTrxVO> materialLotUnfreezeTrxListGet(Long tenantId,
                                                          List<String> materialLotIds);

    /**
     * 保存
     *
     * @param doc 数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 10:43:55
     */
    void save(HmeFreezeDoc doc);

    /**
     * 查询关联的装载信息
     *
     * @param tenantId       租户
     * @param dto            条件
     * @param materialLotIds 条码
     * @return java.util.List<com.ruike.hme.domain.entity.HmeMaterialLotLoad>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/17 11:24:46
     */
    List<HmeMaterialLotLoad> selectRelationLoadList(Long tenantId,
                                                    HmeFreezeDocQueryDTO dto,
                                                    Iterable<String> materialLotIds);
}
