package org.rl.apiService.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.rl.shared.model.responses.PostResponse;
import org.rl.shared.model.PostState;
import org.rl.shared.model.responses.SimplePostResponse;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * An entity corresponding to a post
 */
@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldNameConstants
public class Post {
    /**
     * Maximum allowed length of the post title
     */
    public static final int TITLE_LENGTH = 256;

    /**
     * Maximum allowed length of the post contents
     */
    public static final int CONTENT_LENGTH = 8192;

    /**
     * Maximum allowed length of summary
     */
    public static final int SUMMARY_LENGTH = 50;

    /**
     * Id of the post
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Title of the post
     */
    @Column(name = "title", length = TITLE_LENGTH, nullable = false, unique = true)
    private String title;

    /**
     * Content of the post
     */
    @Column(name = "content", length = CONTENT_LENGTH, nullable = false)
    private String content;

    /**
     * Post Summary
     */
    @Column(name = "summary", length = SUMMARY_LENGTH, nullable = false)
    private String summary = "summary";

    /**
     * Creation date of the post. Should only be updated when a post is transitioned from Draft to Published
     */
    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDate;

    /**
     * State of the post
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private PostState state;

    /**
     * Set of resources used in a post
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "post_resources",
            joinColumns = { @JoinColumn(name = "post_id") },
            inverseJoinColumns = { @JoinColumn(name = "resource_id") }
    )
    private Set<Resource> resources = new HashSet<>();

    /**
     * Create a {@link PostResponse} based on the contents of the post
     * @return A corresponding post response
     */
    public PostResponse toResponse() {
        return new PostResponse(
                this.getId(), this.getTitle(), this.getContent(), this.getSummary(),
                this.getCreationDate(), this.getState());
    }

    /**
     * Create a {@link SimplePostResponse} based ont he contents of the post
     * @return A corresponding simplified post response
     */
    public SimplePostResponse toSimpleResponse() {
        return new SimplePostResponse(this.getId(), this.getTitle());
    }
}
