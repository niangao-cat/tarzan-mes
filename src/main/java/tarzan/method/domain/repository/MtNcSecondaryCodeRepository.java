package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtNcSecondaryCode;

/**
 * 次级不良代码资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
public interface MtNcSecondaryCodeRepository
                extends BaseRepository<MtNcSecondaryCode>, AopProxy<MtNcSecondaryCodeRepository> {

    /**
     * 根据不良代码获取所有需要关闭的不良代码清单/sen.luo 2018-04-01
     *
     * @param tenantId
     * @param ncCodeId
     * @return
     */
    List<MtNcSecondaryCode> ncCodeLimitRequiredSecondaryCodeQuery(Long tenantId, String ncCodeId);

    /**
     * 根据不良代码获取次级代码/sen.luo 2018-04-01
     *
     * @param tenantId
     * @param ncCodeId
     * @return
     */
    List<MtNcSecondaryCode> ncSecondaryCodeQuery(Long tenantId, String ncCodeId);
}
