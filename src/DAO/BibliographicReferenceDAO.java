package DAO;

import Entities.*;
import Entities.References.*;
import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;
import java.util.ArrayList;

// PLACEHOLDER

public class BibliographicReferenceDAO {

    public void removeReference(BibliographicReference reference) {

    }

    public BibliographicReference[] getReferences(Category category) {
        // TODO: DEBUG:
        ArrayList<BibliographicReference> references = new ArrayList<>(2);
        Book book = new Book("Libro");
        book.setDOI("DOI1234");
        book.setLanguage(ReferenceLanguage.Italiano);

        book.addAuthor(new Author("Mario", "Rossi"));
        book.addAuthor(new Author("Ciro", "Esposito"));

        ArrayList<String> tags = new ArrayList<String>(2);
        tags.add("tag1");
        tags.add("tag2");
        book.setTags(tags);

        Video video = new Video("Video", "https://youtu.be/dQw4w9WgXcQ");
        video.setDuration(212f);
        video.setLanguage(ReferenceLanguage.Inglese);
        video.setFrameRate(24);
        video.setWidth(640);
        video.setHeight(320);

        references.add(book);
        references.add(video);

        return references.toArray(new BibliographicReference[references.size()]);
    }
}
