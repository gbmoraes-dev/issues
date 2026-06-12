package com.gbmoraes.issues.adapters.outbound.persistence.user;

import com.gbmoraes.issues.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserJpaEntity {

  @Id
  private String id;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;

  protected UserJpaEntity() {}

  public UserJpaEntity(String id, String email, String name, String password) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.password = password;
  }

  public User toDomain() {
    return new User(id, name, email, password);
  }

  public static UserJpaEntity fromDomain(User user) {
    return new UserJpaEntity(
      user.id(),
      user.email(),
      user.name(),
      user.password()
    );
  }

  public String getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }
}
