package com.example.taskManagement;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Calendar;

public class Task implements Serializable {
    private String title;
    private String description;
    private Calendar deadline;
    private long deadline_int;
    private String imgUri;
    private String docUri;
    private Boolean isDone = false;

    private String ownerId;

    public Task() {
        deadline = Calendar.getInstance();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Exclude public Calendar getDeadline() {
        return deadline;
    }

    @Exclude public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public String getDocUri() {
        return docUri;
    }

    public void setDocUri(String docUri) {
        this.docUri = docUri;
    }

    public long getDeadline_int() {
        this.deadline_int = deadline.getTimeInMillis();
        return deadline_int;
    }

    public void setDeadline_int(long deadline_int) {
        this.deadline_int = deadline_int;
        deadline.setTimeInMillis(deadline_int);
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", img='" + imgUri + '\'' +
                ", docUri='" + docUri + '\'' +
                ", isDone=" + isDone +
                '}';
    }


}
