package archenoah.lib.tool.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;

/**
 * This specializes {@link FileDownloader} in a way, such that both the file
 * name and content can be determined on-demand, i.e. when the user has clicked
 * the component.
 */
public class OnDemandFileDownloader extends FileDownloader {

    /**
     * Provide both the {@link StreamSource} and the filename in an on-demand
     * way.
     */
    public static abstract class OnDemandStreamResource implements StreamSource {
        public abstract String getFilename();
        /**
         * 
         * @return e.g. "application/zip" or null to default to "application/octet-stream"
         */
        public abstract byte[] getOutput();
        public abstract String getContentType();
        
        @Override
        public InputStream getStream() {
          return new ByteArrayInputStream(getOutput());
        }
        
    }

    private static final long serialVersionUID = 1L;
    private final OnDemandStreamResource onDemandStreamResource;

    public OnDemandFileDownloader(OnDemandStreamResource onDemandStreamResource) {
        super(new StreamResource(onDemandStreamResource, ""));
        this.onDemandStreamResource = checkNotNull(onDemandStreamResource, "The given on-demand stream resource may never be null!");
    }

    @Override
    public boolean isOverrideContentType() {
        return false;
    }
    
    @Override
    public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws IOException {
        
        String mime = onDemandStreamResource.getContentType() != null ?
            onDemandStreamResource.getContentType() : "application/octet-stream";
        
        getResource().setMIMEType(mime);
        getResource().setCacheTime(-1);
        getResource().setFilename(onDemandStreamResource.getFilename());
        return super.handleConnectorRequest(request, response, path);
    }

    private StreamResource getResource() {
        return (StreamResource) this.getResource("dl");
    }
    
    private OnDemandStreamResource checkNotNull(OnDemandStreamResource resource, String message) {
        if (resource == null) {
            throw new IllegalArgumentException(message);
        }
        return resource;
    }
    
}

