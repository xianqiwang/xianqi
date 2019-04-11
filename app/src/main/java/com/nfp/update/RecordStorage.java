package com.nfp.update;
import java.io.Serializable;
public class RecordStorage implements Serializable {

    private static final long serialVersionUID = 1L;
    private String settime;
    private String createtime;
    private String imeinumber;
    private int id;

    public String getCreatetime () {
        return createtime;
    }

    public void setCreatetime (String createtime) {
        this.createtime = createtime;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getSettime () {

        return settime;
    }

    public void setSettime (String settime) {
        this.settime = settime;
    }

    public String getImeinumber () {

        return imeinumber;
    }

    public void setImeinumber (String imeinumber) {
        this.imeinumber = imeinumber;
    }
}
