package com.poc.home.internal.search.indexer.contributor;

import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.search.batch.BatchIndexingActionable;
import com.liferay.portal.search.batch.DynamicQueryBatchIndexingActionableFactory;
import com.liferay.portal.search.spi.model.index.contributor.ModelIndexerWriterContributor;
import com.liferay.portal.search.spi.model.index.contributor.helper.IndexerWriterMode;
import com.liferay.portal.search.spi.model.index.contributor.helper.ModelIndexerWriterDocumentHelper;
import com.poc.home.model.HomeEntry;
import com.poc.home.service.HomeEntryLocalService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
        immediate = true,
        property = "indexer.class.name=com.poc.home.model.HomeEntry",
        service = ModelIndexerWriterContributor.class
)
public class HomeEntryModelIndexerWriterContributor implements ModelIndexerWriterContributor<HomeEntry> {

    @Reference
    private DynamicQueryBatchIndexingActionableFactory dynamicQueryBatchIndexingActionableFactory;

    @Reference
    private HomeEntryLocalService homeEntryLocalService;

    @Override
    public void customize(BatchIndexingActionable batchIndexingActionable, ModelIndexerWriterDocumentHelper modelIndexerWriterDocumentHelper) {
        batchIndexingActionable.setPerformActionMethod((HomeEntry homeEntry) -> {
            Document document = modelIndexerWriterDocumentHelper.getDocument(homeEntry);

            batchIndexingActionable.addDocuments(document);
        });
    }

    @Override
    public BatchIndexingActionable getBatchIndexingActionable() {
        return dynamicQueryBatchIndexingActionableFactory.
                getBatchIndexingActionable(homeEntryLocalService.getIndexableActionableDynamicQuery());
    }

    @Override
    public long getCompanyId(HomeEntry baseModel) {
        return baseModel.getCompanyId();
    }

    @Override
    public IndexerWriterMode getIndexerWriterMode(HomeEntry baseModel) {
        return IndexerWriterMode.UPDATE;
    }

    @Override
    public void modelIndexed(HomeEntry baseModel) {
        ModelIndexerWriterContributor.super.modelIndexed(baseModel);
    }
}
