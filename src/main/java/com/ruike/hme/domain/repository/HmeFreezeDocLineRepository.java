package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeFreezeCosLoadRepresentationDTO;
import com.ruike.hme.api.dto.HmeFreezeDocLineSnQueryDTO;
import com.ruike.hme.domain.entity.HmeFreezeDocLine;
import com.ruike.hme.domain.vo.HmeFreezeDocLineSnUnfreezeVO;
import com.ruike.hme.domain.vo.HmeFreezeDocLineVO;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;

import java.util.List;

/**
 * 条码冻结单行资源库
 *
 * @author yonghui.zhu@hand-china.com 2021-02-22 15:44:41
 */
public interface HmeFreezeDocLineRepository extends BaseRepository<HmeFreezeDocLine> {
    /**
     * 查询展示数据
     *
     * @param tenantId    租户
     * @param freezeDocId 头ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/23 10:35:05
     */
    List<HmeFreezeDocLineVO> listGet(Long tenantId, String freezeDocId);

    /**
     * 按单据ID查询
     *
     * @param tenantId    租户
     * @param freezeDocId 头ID
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/23 10:35:05
     */
    List<HmeFreezeDocLine> byDocId(Long tenantId, String freezeDocId);

    /**
     * 按条件查询
     *
     * @param tenantId 租户
     * @param dto      查询条件
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocLineVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/23 10:35:05
     */
    Page<HmeFreezeDocLineVO> byCondition(Long tenantId, HmeFreezeDocLineSnQueryDTO dto, PageRequest pageRequest);

    /**
     * 保存
     *
     * @param line 数据
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 10:43:55
     */
    void save(HmeFreezeDocLine line);

    /**
     * 保存
     *
     * @param lineList 数据
     * @return int
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/24 10:43:55
     */
    @Override
    int batchSave(List<HmeFreezeDocLine> lineList);

    /**
     * sn解冻判定结果列表查询
     *
     * @param tenantId       租户
     * @param materialLotIds SN
     * @return java.util.List<com.ruike.hme.domain.vo.HmeFreezeDocLineSnUnfreezeVO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/2/25 10:14:00
     */
    List<HmeFreezeDocLineSnUnfreezeVO> unfreezeSnGet(Long tenantId,
                                                     List<String> materialLotIds);

    /**
     * 查询COS装载冻结信息
     *
     * @param tenantId      租户
     * @param materialLotId 物料批
     * @param pageRequest   分页参数
     * @return java.util.List<com.ruike.hme.api.dto.HmeFreezeCosLoadRepresentationDTO>
     * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/3/17 02:21:01
     */
    Page<HmeFreezeCosLoadRepresentationDTO> cosLoadGet(Long tenantId,
                                                       String materialLotId,
                                                       PageRequest pageRequest);
}
