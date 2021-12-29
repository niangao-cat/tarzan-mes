package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtAssembleGroupControl;
import tarzan.method.domain.vo.MtAssembleGroupControlVO;
import tarzan.method.domain.vo.MtAssembleGroupControlVO1;
import tarzan.method.domain.vo.MtAssembleGroupControlVO2;

/**
 * 装配组控制，标识具体装配控制下对装配组的控制，装配组可安装的工作单元资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:14:22
 */
public interface MtAssembleGroupControlRepository
                extends BaseRepository<MtAssembleGroupControl>, AopProxy<MtAssembleGroupControlRepository> {

    /**
     * 根据工作单元获取可使用装配组
     * 
     * @param tenantId
     * @param condition
     */
    List<String> wkcLimitAvailableAssembleGroupQuery(Long tenantId, MtAssembleGroupControlVO condition);

    /**
     * 新增更新装配组控制
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    String assembleGroupControlUpdate(Long tenantId, MtAssembleGroupControl dto);

    /**
     * propertyLimitAssembleGroupControlPropertyQuery-根据属性获取装配组控制信息
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtAssembleGroupControlVO2> propertyLimitAssembleGroupControlPropertyQuery(Long tenantId,
                                                                                   MtAssembleGroupControlVO1 dto);
}
