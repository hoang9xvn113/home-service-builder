package com.poc.home.internal.search;

import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.spi.model.index.contributor.ModelIndexerWriterContributor;
import com.liferay.portal.search.spi.model.registrar.ModelSearchRegistrarHelper;
import com.liferay.portal.search.spi.model.result.contributor.ModelSummaryContributor;
import com.poc.home.internal.search.constants.SearchField;
import com.poc.home.model.HomeEntry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        service = {com.poc.home.internal.search.HomeEntrySearchRegistar.class}
)
public class HomeEntrySearchRegistar {

    @Reference(
            target = "(indexer.class.name=com.poc.home.model.HomeEntry)"
    )
    protected ModelSummaryContributor modelSummaryContributor;

    @Reference(
            target = "(indexer.class.name=com.poc.home.model.HomeEntry)"
    )
    protected ModelIndexerWriterContributor modelIndexerWriterContributor;

    private ModelSearchRegistrarHelper modelSearchRegistrarHelper;

    private ServiceRegistration<?> serviceRegistration;

    private SearchField searchField;

    @Activate
    protected void active(BundleContext bundleContext) {
        serviceRegistration = modelSearchRegistrarHelper.register(
                HomeEntry.class, bundleContext,
                modelSearchDefinition -> {
                    modelSearchDefinition.setDefaultSelectedFieldNames(
                            Field.COMPANY_ID, Field.ENTRY_CLASS_PK,
                            Field.ENTRY_CLASS_NAME, Field.GROUP_ID,
                            Field.UUID, SearchField.ADDRESS,
                            SearchField.COLOR, SearchField.FLOOR,
                            SearchField.ROOM, SearchField.SQUARE,
                            SearchField.OWNER

                    );

                    modelSearchDefinition.setModelIndexWriteContributor(modelIndexerWriterContributor);

                    modelSearchDefinition.setModelSummaryContributor(modelSummaryContributor);
                }

        );
    }


}
