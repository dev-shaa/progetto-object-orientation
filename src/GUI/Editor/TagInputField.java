package GUI.Editor;

import java.util.ArrayList;

import Entities.Tag;
import GUI.Utilities.JTermsField;

public class TagInputField extends JTermsField {

    public TagInputField(String separator) {
        super(separator);
    }

    public ArrayList<Tag> getTags() {
        ArrayList<String> tagsString = getTerms();

        if (tagsString == null)
            return null;

        ArrayList<Tag> tags = new ArrayList<>(tagsString.size());

        for (String string : tagsString)
            tags.add(new Tag(string));

        return tags;
    }

}
