package udacity.com.popularmovies.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Created by gubbave on 10/5/2016.
 */
public class TrailerInfo extends BaseModel {

    @SerializedName("id")
    private String id;

    @SerializedName("iso_639_1")
    private String iso_639_1;

    @SerializedName("iso_3166_1")
    private String iso_3166_1;

    @SerializedName("key")
    private String key;

    @SerializedName("name")
    private String name;

    @SerializedName("site")
    private String site;

    @SerializedName("size")
    private String size;

    @SerializedName("type")
    private String type;

    public TrailerInfo() {  }

    protected TrailerInfo(Parcel in) {
        super(in);
        id = in.readString();
        iso_639_1 = in.readString();
        iso_3166_1 = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readString();
        type = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static final Creator<TrailerInfo> CREATOR = new Creator<TrailerInfo>() {
        @Override
        public TrailerInfo createFromParcel(Parcel in) {
            return new TrailerInfo(in);
        }

        @Override
        public TrailerInfo[] newArray(int size) {
            return new TrailerInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(iso_3166_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(size);
        dest.writeString(type);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TrailerInfo that = (TrailerInfo) obj;
        return Objects.equals(id, that.id) &&
                Objects.equals(iso_639_1, that.iso_639_1) &&
                Objects.equals(iso_3166_1, that.iso_3166_1) &&
                Objects.equals(key, that.key) &&
                Objects.equals(name, that.name) &&
                Objects.equals(site, that.site) &&
                Objects.equals(size, that.size) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, iso_639_1, iso_3166_1, key, name, site, size, type);
    }
}
