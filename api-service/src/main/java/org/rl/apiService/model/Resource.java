package org.rl.apiService.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.rl.shared.model.responses.ResourceResponse;

import java.util.HashSet;
import java.util.Set;

/**
 * An entity corresponding to a resource, such as a file
 */
@Entity
@Table(name = "resources")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@FieldNameConstants
public class Resource {
    /**
     * Id of the resource
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Name of the resource (its name)
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Set of posts that use this resource
     */
    @ManyToMany(mappedBy = Post.Fields.resources)
    private Set<Post> posts = new HashSet<>();

    public ResourceResponse toResponse() {
        return new ResourceResponse(this.getId(), this.getName());
    }
}
