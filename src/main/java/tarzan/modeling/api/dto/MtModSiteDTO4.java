package tarzan.modeling.api.dto;

import io.swagger.annotations.ApiModelProperty;
import io.tarzan.common.api.dto.MtExtendAttrDTO3;
import tarzan.modeling.domain.vo.MtModSiteManufacturingVO;
import tarzan.modeling.domain.vo.MtModSiteScheduleVO;
import tarzan.modeling.domain.vo.MtModSiteVO5;

import java.io.Serializable;
import java.util.List;

/**
 * @author yuan.yuan@hand-chin.com 2019/8/9
 */
public class MtModSiteDTO4 implements Serializable {
    private static final long serialVersionUID = -1771157560969962381L;

    @ApiModelProperty("站点基础属性")
    private MtModSiteVO5 site;

    @ApiModelProperty("站点生产属性")
    private MtModSiteManufacturingVO siteManufacturing;

    @ApiModelProperty("站点计划属性")
    private MtModSiteScheduleVO siteSchedule;

    @ApiModelProperty("站点扩展属性")
    private List<MtExtendAttrDTO3> siteAttrs;

    public MtModSiteVO5 getSite() {
        return site;
    }

    public void setSite(MtModSiteVO5 site) {
        this.site = site;
    }

    public MtModSiteManufacturingVO getSiteManufacturing() {
        return siteManufacturing;
    }

    public void setSiteManufacturing(MtModSiteManufacturingVO siteManufacturing) {
        this.siteManufacturing = siteManufacturing;
    }

    public MtModSiteScheduleVO getSiteSchedule() {
        return siteSchedule;
    }

    public void setSiteSchedule(MtModSiteScheduleVO siteSchedule) {
        this.siteSchedule = siteSchedule;
    }

    public List<MtExtendAttrDTO3> getSiteAttrs() {
        return siteAttrs;
    }

    public void setSiteAttrs(List<MtExtendAttrDTO3> siteAttrs) {
        this.siteAttrs = siteAttrs;
    }



}
