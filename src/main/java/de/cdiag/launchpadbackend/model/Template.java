package de.cdiag.launchpadbackend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    private String templateName;

    private String templateDescription;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<App> applications;

    @OneToOne(mappedBy = "template")
    private Launchpad launchpad;

    public Template(String templateName, String templateDescription) {
        this.templateName = templateName;
        this.templateDescription = templateDescription;
    }
}
