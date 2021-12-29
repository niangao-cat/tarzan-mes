package com.ruike.hme.domain.repository;

import com.ruike.hme.api.dto.HmeTagDaqAttrDTO;
import com.ruike.hme.domain.vo.HmeTagDaqAttrVO;
import com.ruike.hme.domain.vo.HmeTagDaqAttrVO2;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.BaseRepository;
import com.ruike.hme.domain.entity.HmeTagDaqAttr;

/**
 * 数据项数据采集扩展属性表资源库
 *
 * @author chaonan.hu@hand-china.com 2020-07-21 09:52:44
 */
public interface HmeTagDaqAttrRepository extends BaseRepository<HmeTagDaqAttr> {

    /**
     * 数据采集Lov数据查询
     *
     * @param tenantId 租户ID
     * @param dto 查询条件
     * @param pageRequest 分页信息
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/21 10:44:20
     * @return io.choerodon.core.domain.Page<org.hzero.boot.platform.lov.dto.LovValueDTO>
     */
    Page<LovValueDTO> dataCollectionLovQuery(Long tenantId, HmeTagDaqAttrDTO dto, PageRequest pageRequest);

    /**
     * 创建或者更新数据采集扩展属性
     *
     * @param tenantId 租户ID
     * @param dto 保存数据
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/21 11:13:12
     * @return com.ruike.hme.domain.entity.HmeTagDaqAttr
     */
    HmeTagDaqAttr createOrUpdate(Long tenantId, HmeTagDaqAttr dto);

    /**
     * 查询数据项数据采集扩展属性
     *
     * @param tenantId 租户ID
     * @param tagId 数据项ID
     * @author chaonan.hu chaonan.hu@hand-china.com 2020/7/21 11:56:41
     * @return com.ruike.hme.domain.vo.HmeTagDaqAttrVO2
     */
    HmeTagDaqAttrVO2 query(Long tenantId, String tagId);
}
