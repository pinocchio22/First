package kr.ac.daejin.restaurantnews.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class Post implements Parcelable {
    private String content = "";
    private List<String> like = null;
    private String name = "";
    private String publisher = "";
    private Timestamp timestamp = Timestamp.now();
    private String title = "";

    public Post() {

    }

    public Post(String publisher, String name, String title, String content, Timestamp timestamp, List<String> like) {
        this.publisher = publisher;
        this.name = name;
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.like = like;
    }

    protected Post(Parcel in) {
        content = in.readString();
        like = in.readArrayList(ArrayList.class.getClassLoader());
        name = in.readString();
        publisher = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
        title = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeList(like);
        dest.writeString(name);
        dest.writeString(publisher);
        dest.writeParcelable(timestamp, flags);
        dest.writeString(title);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getLike() {
        return like;
    }

    public void setLike(List<String> like) {
        this.like = like;
    }
}
