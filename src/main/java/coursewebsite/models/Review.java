package coursewebsite.models;

public class Review {
    private final int rating;
    private final String comment;

    // Constructeur de base, avec note + commentaire
    Review(int rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    // Surcharge du constructeur, commentaire facultatif
    public Review(int rating) {
        this.rating = rating;
        this.comment = "";
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
}
