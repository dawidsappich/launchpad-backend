package de.cdiag.launchpadbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"applications"})
@JsonIgnoreProperties({"launchpad"})
@Entity
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
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
