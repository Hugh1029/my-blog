package an.answering.vo;

import java.io.Serializable;

/**
 * Created by HP on 2017/8/18.
 */
public class Menu implements Serializable{

    private static final long serialVersionUID = 5671205360962741098L;

    private String name;
    private String url;

    public Menu(String name,String url){
        this.name = name;
        this.url = url;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
