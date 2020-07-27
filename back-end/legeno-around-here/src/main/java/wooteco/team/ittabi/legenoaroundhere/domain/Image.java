package wooteco.team.ittabi.legenoaroundhere.domain;

import java.util.Objects;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class Image {

    private String name;

    private String url;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Image() {
    }

    public Image(String name, String url, Post post) {
        this.name = name;
        this.url = url;
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Post getPost() {
        return post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Image image = (Image) o;
        return Objects.equals(name, image.name) &&
            Objects.equals(url, image.url) &&
            Objects.equals(post, image.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url, post);
    }

    @Override
    public String toString() {
        return "Image{" +
            "name='" + name + '\'' +
            ", url='" + url + '\'' +
            ", post=" + post +
            '}';
    }
}
