package com.ruike.hme.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import com.ruike.hme.domain.entity.HmeException;
import com.ruike.hme.api.dto.HmeExceptionDTO;

/**
 * 异常维护基础数据头表资源库
 *
 * @author liyuan.lv@hand-china.com 2020-05-09 10:51:25
 */
public interface HmeExceptionRepository extends BaseRepository<HmeException>, AopProxy<HmeExceptionRepository> {
    /**
     * 查询异常项
     * @param tenantId
     * @param dto
     * @return
     */
    List<HmeException> exceptionUiQuery(Long tenantId, HmeExceptionDTO dto);

    /**
     * 异常项属性更新
     * @param tenantId
     * @param dto
     * @return
     */
    HmeException exceptionBasicPropertyUpdate(Long tenantId, HmeException dto);
}
