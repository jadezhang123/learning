package own.jadezhang.learning.apple.domain.base;

import own.jadezhang.common.domain.BaseDomain;

/**
 * Created by Zhang Junwei on 2016/10/16.
 */
public class Article extends BaseDomain<Long>{
    private Long uid;
    private String name;
    private String comment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
