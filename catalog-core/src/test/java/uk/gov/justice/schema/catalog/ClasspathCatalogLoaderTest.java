package uk.gov.justice.schema.catalog;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import uk.gov.justice.schema.catalog.domain.CatalogWrapper;
import uk.gov.justice.schema.catalog.util.ClasspathResourceLoader;

import java.io.IOException;
import java.net.URI;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClasspathCatalogLoaderTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ClasspathResourceLoader classpathResourceLoader;

    @InjectMocks
    private ClasspathCatalogLoader classpathCatalogLoader;


    @Test
    public void shouldThrowExceptionIfLoadingFileThrowsIOException() throws Exception {

        final IOException ioException = new IOException("Ooops");

        final URI uri = new URI("file://src/code/my-file.txt");


        when(classpathResourceLoader.getResources(ClasspathCatalogLoader.class, "json/schema/schema_catalog.json")).thenReturn(singletonList(uri));
        when(objectMapper.readValue(uri.toURL(), CatalogWrapper.class)).thenThrow(ioException);

        try {
            classpathCatalogLoader.getCatalogs();
            fail();
        } catch (final SchemaCatalogException expected) {
            assertThat(expected.getCause(), is(ioException));
            assertThat(expected.getMessage(), is("Failed to convert to json loaded from 'file://src/code/my-file.txt' to a Catalog pojo"));
        }
    }

    @Test
    public void shouldThrowExceptionIfLoadingResourcesThrowsIOException() throws Exception {

        final IOException ioException = new IOException("Ooops");

        when(classpathResourceLoader.getResources(ClasspathCatalogLoader.class, "json/schema/schema_catalog.json")).thenThrow(ioException);

        try {
            classpathCatalogLoader.getCatalogs();
            fail();
        } catch (final Exception expected) {
            assertThat(expected.getCause(), is(ioException));
            assertThat(expected.getMessage(), startsWith("Failed to load the catalogs from the classpath for location 'json/schema/schema_catalog.json'"));

        }
    }
}