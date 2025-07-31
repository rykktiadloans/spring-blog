package org.rl.apiService.model;

import jakarta.persistence.*;
import lombok.*;
import org.rl.shared.model.responses.ResourceResponse;

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
     * Create a ResourceResponse based on the object
     * @return Analogous ResourceResponse
     */
    public ResourceResponse toResponse() {
        return new ResourceResponse(this.getId(), this.getName());
    }
}
