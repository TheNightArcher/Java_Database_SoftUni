package bg.softuni.gamestore.models.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(name = "trailer_id", nullable = false)
    private String trailerId;

    @Column(name = "thumbnail_URL",nullable = false)
    private String thumbnailUrl;

    @Column(nullable = false)
    private float size;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "release_date",nullable = false)
    private LocalDate releaseDate;


    public Game(){}

    public Game(String title, String trailerId, String thumbnailUrl, float size, BigDecimal price, LocalDate releaseDate) {
        this.title = title;
        this.trailerId = trailerId;
        this.thumbnailUrl = thumbnailUrl;
        this.size = size;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
}
