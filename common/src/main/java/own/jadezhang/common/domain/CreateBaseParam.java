package own.jadezhang.common.domain;

public abstract class CreateBaseParam<T> extends BaseParam<T> {
    /**
     *字段常量——创建人
     */
    public static final String F_Creator="creator";
    /**
     *字段常量——创建时间
     */
    public static final String F_CreateDate="createDate";
    /**
     *字段常量——修改人
     */
    public static final String F_LastModifier="lastModifier";
    /**
     *字段常量——修改时间
     */
    public static final String F_LastModDate="lastModDate";
    /**
     *字段常量——状态
     */
    public static final String F_Status="status";

    private String creator; //创建人
    private Long createDate; //创建时间
    private String lastModifier; //修改人
    private Long lastModDate; //修改时间
    private Integer status; //状态

    /**
     * @return creator 创建人
     */
    public String getCreator(){
        return this.creator;
    }
    /**
     * @param creator 创建人
     */
    public void setCreator(String creator){
        this.creator = creator;
    }
    /**
     * @return createDate 创建时间
     */
    public Long getCreateDate(){
        return this.createDate;
    }
    /**
     * @param createDate 创建时间
     */
    public void setCreateDate(Long createDate){
        this.createDate = createDate;
    }
    /**
     * @return lastModifier 修改人
     */
    public String getLastModifier(){
        return this.lastModifier;
    }
    /**
     * @param lastModifier 修改人
     */
    public void setLastModifier(String lastModifier){
        this.lastModifier = lastModifier;
    }
    /**
     * @return lastModDate 修改时间
     */
    public Long getLastModDate(){
        return this.lastModDate;
    }
    /**
     * @param lastModDate 修改时间
     */
    public void setLastModDate(Long lastModDate){
        this.lastModDate = lastModDate;
    }
    /**
     * @return status 状态
     */
    public Integer getStatus(){
        return this.status;
    }
    /**
     * @param status 状态
     */
    public void setStatus(Integer status){
        this.status = status;
    }
}
