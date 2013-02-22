package me.grison.jssg.domain;

import org.springframework.util.StringUtils;

/**
 * Just a filename representing the three differents parts of it.
 * 
 * @author <a href="mailto:a.grison@gmail.com">$Author: Alexandre Grison$</a>
 */
public class Filename {
    private final String date, name, extension;
    private final boolean isStatic;

    public Filename(String date, String name, String extension) {
        this(date, name, extension, false);
    }

    public Filename(String date, String name, String extension, boolean isStatic) {
        this.date = date;
        this.name = name;
        this.extension = extension;
        this.isStatic = isStatic;
    }

    public static Filename staticFile(String name) {
        return new Filename("", name, "", true);
    }

    public String getDate() {
        return this.date;
    }

    public String getName() {
        return this.name;
    }

    public String getExtension() {
        return this.extension;
    }

    public String toPath() {
        if (this.isStatic) {
            return this.name;
        } else if (StringUtils.hasLength(this.date)) {
            return String.format("%s/%s/index.html", this.date.replaceAll("-", "/"), this.name);
        } else if (this.name.equals("index")) {
            return "index.html";
        } else {
            return String.format("%s/index.html", this.name);
        }
    }
}
