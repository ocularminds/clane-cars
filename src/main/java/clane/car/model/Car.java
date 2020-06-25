package clane.car.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Car model of the car features
 *
 * @author Babatope Festus
 */
@Entity
@Table(name = "cars")
@DynamicUpdate
public class Car implements Serializable {

    /**
     * System generated primary key id
     */
    @Id
    @Column(nullable = false, length = 22)
    private String id;
    /**
     * Car name
     */
    @NotNull(message = "Name cannot be null")
    //@Min()
    @Column(nullable = false, length = 26)
    private String name;

    /**
     * Summarized details of the car look
     */
    @Column(name = "details", nullable = false, length = 45)
    private String description;

    /**
     * Sequential engine number
     */
    @NotNull(message = "Engine number cannot be null")
    @Column(name = "engine_num", nullable = false, length = 5)
    private String number;

    /**
     * Car catalogue category
     */
    @ManyToOne
    @JoinColumn(name = "category", nullable = true)
    private Category category;

    /**
     * Car tags
     */
    @ManyToMany
    @JoinTable(name = "car_tags",
            joinColumns = @JoinColumn(name = "car", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag", referencedColumnName = "id")
    )
    private Set<Tag> tags;

    @OneToMany(mappedBy = "car")
    private List<Image> images;

    /**
     * Date and time when the car record was created.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date record;

    public Car() {
        this("");
    }

    public Car(final String name) {
        this.name = name;
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

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the number
     */
    public String getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return the tags
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    /**
     * @return the images
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * @param images the images to set
     */
    public void setImages(List<Image> images) {
        this.images = images;
    }

    /**
     * @return the record
     */
    public Date getRecord() {
        return record;
    }

    /**
     * @param record the record to set
     */
    public void setRecord(Date record) {
        this.record = record;
    }

    public void addTag(Tag tag) {
        if (this.tags == null || this.tags.isEmpty()) {
            this.tags = new HashSet<>();
        }
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        if (this.tags == null || this.tags.isEmpty()) {
            return;
        }
        this.tags.remove(tag);
    }

    public void addImage(Image image) {
        if (this.images == null || this.images.isEmpty()) {
            this.images = new ArrayList<>();
        }
        image.setCar(this);
        this.images.add(image);
    }

    public void removeImage(Image image) {
        if (this.images == null || this.images.isEmpty()) {
            return;
        }
        this.images.remove(image);
    }

}
