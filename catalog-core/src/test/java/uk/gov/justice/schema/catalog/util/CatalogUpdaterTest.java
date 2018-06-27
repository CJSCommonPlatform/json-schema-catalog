package uk.gov.justice.schema.catalog.util;


import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

import uk.gov.justice.schema.catalog.CatalogUpdater;
import uk.gov.justice.schema.catalog.JsonSchemaFileLoader;
import uk.gov.justice.schema.catalog.RawCatalog;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

@RunWith(MockitoJUnitRunner.class)
public class CatalogUpdaterTest {

    @Mock Logger logger;
    @Mock
    private JsonSchemaFileLoader jsonSchemaFileLoader;

    @InjectMocks
    private RawCatalog rawCatalog;

    @InjectMocks private CatalogUpdater catalogUpdater;

    @Test
    public void shouldCacheFileWithReferenceToAnotherSchema(){

        final Map<String, String> schemaIdsToRawJsonSchemaCache = new HashMap<>();
        final Path basePath = Paths.get((""));

        schemaIdsToRawJsonSchemaCache.put("http://justice.gov.uk/standards/address.json", "json schema" );

        rawCatalog.initialize();
        Collection<Path> paths = new ArrayList<>();

        try {
            final File aliasJson = new File(this.getClass().getClassLoader().getResource("json/schema/standards/example.events.alias.json").toURI());
            final File personJson = new File(this.getClass().getClassLoader().getResource("json/schema/standards/example.events.person-updated.json").toURI());
            paths.add(Paths.get(aliasJson.toURI()));
            paths.add(Paths.get(personJson.toURI()));
            catalogUpdater.updtateRawCatalog(schemaIdsToRawJsonSchemaCache, basePath, paths);
            assertThat(schemaIdsToRawJsonSchemaCache.size(), greaterThan(1));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }

}
