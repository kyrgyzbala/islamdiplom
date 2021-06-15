package kg.programm.programmingapp.data.test;

import org.jetbrains.annotations.NotNull;

public class ModelTest {

    private int id;
    private String name;
    private String description;
    private int questions;
    private String icon;

    public ModelTest() {
    }

    public ModelTest(int id, String name, String description, int questions, String icon) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.questions = questions;
        this.icon = icon;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuestions() {
        return questions;
    }

    public void setQuestions(int questions) {
        this.questions = questions;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @NotNull
    @Override
    public String toString() {
        return "ModelTest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", questions=" + questions +
                ", icon='" + icon + '\'' +
                '}';
    }
}

