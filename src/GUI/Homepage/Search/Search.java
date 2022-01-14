package GUI.Homepage.Search;

import java.util.Date;

import Entities.Category;
import Entities.Tag;

// TODO: commenta

public class Search {
    private Date from;
    private Date to;
    private Tag[] tags;
    private Category[] categories;
    // TODO: aggiungi autori

    public Search(Date from, Date to, Tag[] tags, Category[] categories) {
        setFrom(from);
        setTo(to);
        setTags(tags);
        setCategories(categories);
    }

    public Category[] getCategories() {
        return categories;
    }

    public void setCategories(Category[] categories) {
        this.categories = categories;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Tag[] getTags() {
        return tags;
    }

    public void setTags(Tag[] tags) {
        this.tags = tags;
    }
}
