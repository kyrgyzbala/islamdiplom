package kg.programm.programmingapp.data.lectures;

public class ModelLecture {

    private int order;
    private String name;
    private String description;
    private String link;
    private String thumbnail;
    private int category;

    public ModelLecture() {
    }

    public ModelLecture(int order, String name, String description, String link, String thumbnail) {
        this.order = order;
        this.name = name;
        this.description = description;
        this.link = link;
        this.thumbnail = thumbnail;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
