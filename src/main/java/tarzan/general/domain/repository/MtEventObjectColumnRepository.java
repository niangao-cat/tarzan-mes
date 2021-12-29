package tarzan.general.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.general.domain.entity.MtEventObjectColumn;
import tarzan.general.domain.vo.MtEventObjectColumnVO;

/**
 * 对象列定义资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:58:18
 */
public interface MtEventObjectColumnRepository
                extends BaseRepository<MtEventObjectColumn>, AopProxy<MtEventObjectColumnRepository> {

    /**
     * propertyLimitObjectColumnQuery-根据属性获取对象列
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<String> propertyLimitObjectColumnQuery(Long tenantId, MtEventObjectColumnVO dto);

    /**
     * objectColumnGet-获取对象列属性
     *
     * @param tenantId
     * @param objectColumnId
     * @return
     */
    MtEventObjectColumn objectColumnGet(Long tenantId, String objectColumnId);
}
