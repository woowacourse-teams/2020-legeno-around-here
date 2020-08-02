package wooteco.team.ittabi.legenoaroundhere.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Image extends BaseEntity {

    private String name;

    private String url;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    protected Image() {
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
    public String toString() {
        return "Image{" +
            "name='" + name + '\'' +
            ", url='" + url + '\'' +
            ", post=" + post +
            '}';
    }
}
