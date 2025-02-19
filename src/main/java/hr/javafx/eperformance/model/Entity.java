package hr.javafx.eperformance.model;

/**
 * This class is used to represent the entity.
 * It is used to represent the entity with the id.
 * All classes that represent the entity should extend this class.
 */

public abstract class Entity {

    private Long id;

    protected Entity(Long id) {
        this.id = id;
    }

    protected Entity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
