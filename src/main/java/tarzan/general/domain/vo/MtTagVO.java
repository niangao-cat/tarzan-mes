package tarzan.general.domain.vo;

import java.io.Serializable;

/**
 * @author Leeloing
 * @date 2019/7/2 16:03
 */
public class MtTagVO implements Serializable {

    private static final long serialVersionUID = 7348930987729210976L;
    private String tagCode;
    private String tagGroupCode;

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    public String getTagGroupCode() {
        return tagGroupCode;
    }

    public void setTagGroupCode(String tagGroupCode) {
        this.tagGroupCode = tagGroupCode;
    }
}
