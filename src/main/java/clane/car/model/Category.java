package clane.car.model;

import clane.car.common.IdGenerator;
import clane.car.common.Identifier;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * Car catalogue category class
 *
 * @author Babatope Festus
 */
@Entity
@Table(name = "cats")
public class Category implements Serializable {

    /**
     * System generated id for category
     */
    @Id
    @Column(nullable = false, length = 8)
    private String id;
    /**
     * Category name
     */
    @Column(nullable = false, length = 35)
    private String name;

    public Category() {
        this("");
    }

    public Category(String name) {
        this.name = name;
    }

    @PrePersist
    private void addPrimaryKey() throws Exception {
        if (getId() == null) {
            this.setId(IdGenerator.getInstance().generate(Identifier.Type.MIN));
        }
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (id != null) {
            sb.append(id);
        }
        if (name != null) {
            sb.append(name);
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        } else {
            return this.getId().equals(((Category) o).getId())
                    && this.getName().toLowerCase().equalsIgnoreCase(((Category) o).getName());
        }
    }
}
