package pojo;

public class CreatedUserResponse {

    private int code;
    private String meta;
    private CreatedUserData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public CreatedUserData getData() {
        return data;
    }

    public void setData(CreatedUserData data) {
        this.data = data;
    }


}
