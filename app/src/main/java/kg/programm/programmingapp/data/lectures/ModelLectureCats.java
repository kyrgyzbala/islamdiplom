package kg.programm.programmingapp.data.lectures;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelLectureCats implements Parcelable {
    private int id;
    private String name;
    private String icon;

    public ModelLectureCats() {
    }

    public ModelLectureCats(int id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    protected ModelLectureCats(Parcel in) {
        id = in.readInt();
        name = in.readString();
        icon = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(icon);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelLectureCats> CREATOR = new Creator<ModelLectureCats>() {
        @Override
        public ModelLectureCats createFromParcel(Parcel in) {
            return new ModelLectureCats(in);
        }

        @Override
        public ModelLectureCats[] newArray(int size) {
            return new ModelLectureCats[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
