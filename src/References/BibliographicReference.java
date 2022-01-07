// PLACEHOLDER

import java.util.Date;

public class BibliographicReference {
    public String name;
    public String author;
    public Date pubblicationDate;

    public BibliographicReference(String name, String author, Date pubblicationDate) {
        this.name = name;
        this.author = author;
        this.pubblicationDate = new Date(); // FIXME: imposta la data
    }

    public String getFormattedDetails() {
        return "Nome:\t" + this.name + "\nAutore:\t" + this.author + "\nData:\t" + this.pubblicationDate.toString();
    }
}
