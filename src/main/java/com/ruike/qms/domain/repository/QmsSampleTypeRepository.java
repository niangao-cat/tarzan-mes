package com.ruike.qms.domain.repository;

import com.ruike.qms.api.dto.QmsSampleSchemeDTO;
import com.ruike.qms.api.dto.QmsSampleTypeQueryDTO;
import com.ruike.qms.api.dto.QmsSampleTypeReturnDTO;
import com.ruike.qms.api.dto.QmsSampleTypeSaveDTO;
import com.ruike.qms.domain.entity.QmsSampleType;
import io.choerodon.core.domain.Page;
import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.hzero.mybatis.base.BaseRepository;

/**
 * 抽样类型管理资源库
 *
 * @author han.zhang03@hand-china.com 2020-04-21 21:33:44
 */
public interface QmsSampleTypeRepository extends BaseRepository<QmsSampleType> {

    /**
     * @Description 抽样类型管理查询
     * @param tenantId
     * @param dto
     * @param pageRequest
     * @return com.ruike.qms.api.dto.QmsSampleTypeReturnDTO
     * @Date 2020-05-07 09:49
     * @Author han.zhang
     */
    Page<QmsSampleTypeReturnDTO> selectSampleType(Long tenantId, QmsSampleTypeQueryDTO dto, PageRequest pageRequest);

    /**
     * @Description 抽样类型管理新增和修改的保存
     * @param tenantId
     * @param dto
     * @return java.lang.String
     * @Date 2020-05-07 10:40
     * @Author han.zhang
     */
    String saveSampleTypeForUi(Long tenantId, QmsSampleTypeSaveDTO dto);
}
