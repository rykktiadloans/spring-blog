package org.rl.apiService.model;

import jakarta.persistence.*;
import lombok.*;
import org.rl.shared.model.responses.PostResponse;
import org.rl.shared.model.PostState;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "content", length = 4096, nullable = false)
    private String content;

    @Column(name = "creation_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private PostState state;

    @OneToMany(mappedBy = "post")
    private List<Resource> resources;

    public PostResponse toResponse() {
        return new PostResponse(
                this.getId(), this.getTitle(), this.getContent(),
                this.getCreationDate(), this.getState());
    }
}
