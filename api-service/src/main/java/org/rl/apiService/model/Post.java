package org.rl.apiService.model;

import jakarta.persistence.*;
import lombok.*;
import org.rl.shared.model.responses.PostResponse;
import org.rl.shared.model.PostState;

import java.time.LocalDateTime;
import java.util.List;

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
public class Post {
    /**
     * Id of the post
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Title of the post
     */
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    /**
     * Content of the post
     */
    @Column(name = "content", length = 4096, nullable = false)
    private String content;

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
     * Create a {@link PostResponse} based on the contents of the post
     * @return A corresponding post response
     */
    public PostResponse toResponse() {
        return new PostResponse(
                this.getId(), this.getTitle(), this.getContent(),
                this.getCreationDate(), this.getState());
    }
}
