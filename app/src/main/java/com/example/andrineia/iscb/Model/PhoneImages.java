package com.example.andrineia.iscb.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneImages
{
    /**
     * url : https://www.91-img.com/gallery_images_uploads/9/5/95483e778ba595de71ba90fe06675dcf8b9f9d0a.jpg
     * mobile_id : 3
     * id : 3
     */

    private String url;
    private int mobile_id;
    private int id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getMobile_id() {
        return mobile_id;
    }

    public void setMobile_id(int mobile_id) {
        this.mobile_id = mobile_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void decode(JSONObject json) throws JSONException {
        if (json != null) {
            if (json.length() > 0) {
                this.id = json.optInt("id");
                this.mobile_id = json.optInt("mobile_id");
                this.url = json.optString("url");
            }
        }
    }
}

