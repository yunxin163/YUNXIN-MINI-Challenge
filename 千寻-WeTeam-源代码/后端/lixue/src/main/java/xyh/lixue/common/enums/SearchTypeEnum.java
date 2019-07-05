package xyh.lixue.common.enums;

/**
 * @author XiangYida
 * @version 2019/5/7 16:50
 */
public enum SearchTypeEnum {
    TITLE("title"),
    KNOWLEDGEPOINT("knowledgePoint");

    private String type;

    public String getType(){
        return this.type;
    }

    SearchTypeEnum(String type){
        this.type=type;
    }
}
