
package clane.car.common;

import java.io.File;
import java.net.URL;

/**
 * Immutable class for generating file url
 *
 */
public final class Files {

    private final File file;

    /**
     * Creates new instance with a specified file
     *
     * @param fil Input file
     */
    public Files(File fil) {
        this.file = fil;
    }

    public URL toUrl() {
        URL url = null;
        try {
            url = this.file.toURI().toURL();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return url;
    }

    public String toUrlString() {
        if (this.toUrl() != null) {
            return this.toUrl().toString();
        } else {
            return "";
        }
    }
}
