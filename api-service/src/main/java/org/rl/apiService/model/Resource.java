package org.rl.apiService.model;

import jakarta.persistence.*;
import lombok.*;
import org.rl.shared.model.responses.ResourceResponse;

@Entity
@Table(name = "resources")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public ResourceResponse toResponse() {
        return new ResourceResponse(this.getId(), this.getName());
    }
}
