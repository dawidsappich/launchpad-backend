package de.cdiag.launchpadbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull @NotBlank
    private String templateName;

    private String templateDescription;

    @ManyToMany
    private Set<App> applications;

    @OneToOne(mappedBy = "template")
    private Launchpad launchpad;
}
