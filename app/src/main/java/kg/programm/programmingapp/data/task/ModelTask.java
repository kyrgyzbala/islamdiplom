package kg.programm.programmingapp.data.task;

import android.os.Parcel;
import android.os.Parcelable;

public class ModelTask implements Parcelable {

    private String name;
    private String description;
    private String photo;

    public ModelTask() {
    }

    public ModelTask(String name, String description, String photo) {
        this.name = name;
        this.description = description;
        this.photo = photo;
    }

    protected ModelTask(Parcel in) {
        name = in.readString();
        description = in.readString();
        photo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(photo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ModelTask> CREATOR = new Creator<ModelTask>() {
        @Override
        public ModelTask createFromParcel(Parcel in) {
            return new ModelTask(in);
        }

        @Override
        public ModelTask[] newArray(int size) {
            return new ModelTask[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
