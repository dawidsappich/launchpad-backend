package de.cdiag.launchpadbackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @NotNull @NotBlank
    @Column(unique = true)
    private String username;

    @NotNull @NotBlank
    private String password;

    @Column(updatable = false)
    private LocalDateTime created;

    @NotNull
    private LocalDateTime modified;

    @OneToOne(cascade = CascadeType.ALL)
    private Launchpad launchpad;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @PrePersist
    public void created() {
        this.created = LocalDateTime.now();
        this.modified = LocalDateTime.now();
    }

    @PreUpdate
    public void modified() {
        this.modified = LocalDateTime.now();
    }

}
