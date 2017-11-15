package uk.gov.justice.schema.catalog.generation;

import static java.util.stream.Collectors.toList;

import uk.gov.justice.schema.catalog.domain.Catalog;
import uk.gov.justice.schema.catalog.domain.Group;
import uk.gov.justice.schema.catalog.domain.Schema;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CatalogObjectGenerator {

    private final SchemaDefParser schemaDefParser;

    public CatalogObjectGenerator(final SchemaDefParser schemaDefParser) {
        this.schemaDefParser = schemaDefParser;
    }

    public Catalog generate(final String catalogName, final List<URL> schemaFiles) {

        final List<SchemaDef> schemaDefs = schemaFiles.stream()
                .map(schemaDefParser::parse)
                .collect(toList());

        final List<Group> groups = asGroups(schemaDefs);

        return new Catalog(catalogName, groups);
    }

    private List<Group> asGroups(final List<SchemaDef> schemaDefs) {

        final Map<String, Group> groups = new HashMap<>();

        schemaDefs.forEach(schemaDef -> createGroups(schemaDef, groups));

        return new ArrayList<>(groups.values());
    }

    private void createGroups(final SchemaDef schemaDef, final Map<String, Group> groups) {

        final String groupName = schemaDef.getGroupName();
        if (!groups.containsKey(groupName)) {

            final Group group = new Group(
                    schemaDef.getBaseLocation(),
                    groupName,
                    new ArrayList<>());

            groups.put(groupName, group);
        }

        final Group group = groups.get(groupName);

        final List<Schema> schemas = group.getSchema();

        schemas.add(new Schema(schemaDef.getId().toString(), schemaDef.getLocation()));
    }
}