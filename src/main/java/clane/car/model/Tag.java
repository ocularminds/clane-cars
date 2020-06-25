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
 * Car tag class for tagging car
 *
 * @author Babatope Festus
 */
@Entity
@Table(name = "tags")
public class Tag implements Serializable {

    @Id
    @Column(nullable = false, length = 10)
    private String id;

    @Column(nullable = false, length = 25)
    private String name;

    public Tag() {
        this.name = "";
    }

    public Tag(final String name) {
        this.name = name;
    }

    @PrePersist
    private void addPrimaryKey() throws Exception {
        if (getId() == null) {
            this.setId(IdGenerator.getInstance().generate(Identifier.Type.SHORT));
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
        if (!(o instanceof Tag)) {
            return false;
        } else {
            return this.getId().equals(((Tag) o).getId())
                    && this.getName().toLowerCase().equalsIgnoreCase(((Tag) o).getName());
        }
    }
}
