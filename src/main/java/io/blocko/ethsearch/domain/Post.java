package io.blocko.ethsearch.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

import org.web3j.tuples.generated.Tuple7;
import java.math.BigInteger;

/**
 * A Post.
 */
@Entity
@Table(name = "post")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "post")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "owner_first_name")
    private String ownerFirstName;

    @Column(name = "owner_last_name")
    private String ownerLastName;

    @Size(min = 42, max = 42)
    @Column(name = "owner_account", length = 42)
    private String ownerAccount;

    //public Post(Long id, String title, String content, String ownerId, String ownerFirstName, String ownerLastName, String ownerAccount) {
    public void load(Tuple7<BigInteger, String, String, String, String, String, String> contractPost) {  
        this.id = contractPost.getValue1().longValue();
        this.title = contractPost.getValue2();
        this.content = contractPost.getValue3();
        this.ownerId = contractPost.getValue4();
        this.ownerFirstName = contractPost.getValue5();
        this.ownerLastName = contractPost.getValue6();
        this.ownerAccount = contractPost.getValue7();
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Post title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public Post content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public Post ownerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public Post ownerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
        return this;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public Post ownerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
        return this;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    public String getOwnerAccount() {
        return ownerAccount;
    }

    public Post ownerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
        return this;
    }

    public void setOwnerAccount(String ownerAccount) {
        this.ownerAccount = ownerAccount;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        if (post.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), post.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Post{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", content='" + getContent() + "'" +
            ", ownerId='" + getOwnerId() + "'" +
            ", ownerFirstName='" + getOwnerFirstName() + "'" +
            ", ownerLastName='" + getOwnerLastName() + "'" +
            ", ownerAccount='" + getOwnerAccount() + "'" +
            "}";
    }
}
