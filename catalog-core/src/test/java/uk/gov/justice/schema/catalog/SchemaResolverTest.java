package uk.gov.justice.schema.catalog;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.util.UrlConverter;

import java.net.URL;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class SchemaResolverTest {

    @Spy @SuppressWarnings("unused")
    private UrlConverter urlConverter = new UrlConverter();

    @InjectMocks
    private SchemaResolver schemaResolver;

    @Test
    public void shouldResolveTheLocationToAUrlUsingTheCatalogUrlAndBaseLocation() throws Exception {

        final URL catalogUrl = new URL("file:/src/main/schema.json");

        final String fileLocation = "some/path/to.json";
        final Optional<String> baseLocation = of("base/location/");

        final URL resolvedUrl = schemaResolver.resolve(catalogUrl, fileLocation, baseLocation);

        assertThat(resolvedUrl.toString(), is("file:/src/main/base/location/some/path/to.json"));
    }

    @Test
    public void shouldResolveTheLocationToAUrlUsingTheCatalogUrlAndAnEmptyBaseLocation() throws Exception {

        final URL catalogUrl = new URL("file:/src/main/schema.json");

        final String fileLocation = "some/path/to.json";
        final Optional<String> baseLocation = empty();

        final URL resolvedUrl = schemaResolver.resolve(catalogUrl, fileLocation, baseLocation);

        assertThat(resolvedUrl.toString(), is("file:/src/main/some/path/to.json"));
    }
}