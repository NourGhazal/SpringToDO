package de.sellwerk.backend.persistence;

import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String status;
    private LocalDateTime createdAt;

    public TodoEntity() {
    }

    public TodoEntity(final Long id, final String title, final String status, final LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TodoEntity that = (TodoEntity) o;
        return Objects.equals(status, that.status) && Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(createdAt,
                                                                                                                                         that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, status, createdAt);
    }

    @Override
    public String toString() {
        return "TodoEntity{" +
               "id='" + id + '\'' +
               ", title='" + title + '\'' +
               ", status=" + status +
               ", createdAt='" + createdAt + '\'' +
               '}';
    }
}
