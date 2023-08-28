package pojo;

import java.util.List;

public class CreatedUserResponseWithWrongData {

    private int code;
    private String meta;
    private List<CreatedUserDataWithWrongData> data;

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

    public List<CreatedUserDataWithWrongData> getData() {
        return data;
    }

    public void setData(List<CreatedUserDataWithWrongData> data) {
        this.data = data;
    }


}
