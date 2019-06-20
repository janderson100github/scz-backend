package credit.core.utl;

import org.jsoup.safety.Whitelist;

public class DescriptionUtil {

    public static Whitelist allowedWhiteList() {
        return Whitelist.basic()
                .addTags("div", "a", "label", "h1", "h2", "h3", "h4")
                .addAttributes("label", "style", "class")
                .addAttributes("span", "style", "class")
                .addAttributes("div", "style", "class")
                .addAttributes("h1", "style", "class")
                .addAttributes("h2", "style", "class")
                .addAttributes("h3", "style", "class")
                .addAttributes("h4", "style", "class")
                .addAttributes("a", "href");
    }
}
