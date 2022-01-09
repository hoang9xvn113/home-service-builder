package com.poc.home.internal.search.indexer.contributor;


import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;
import com.poc.home.internal.search.constants.SearchField;
import com.poc.home.model.HomeEntry;
import org.osgi.service.component.annotations.Component;

@Component(
        immediate = true,
        property = "indexer.class.name=com.poc.home.model.HomeEntry",
        service = ModelDocumentContributor.class
)
public class HomeEntryModelDocumentContributor implements  ModelDocumentContributor<HomeEntry> {

    @Override
    public void contribute(Document document, HomeEntry baseModel) {
        document.addText(SearchField.ADDRESS, baseModel.getAddress());
        document.addNumber(SearchField.FLOOR, baseModel.getFloor());
        document.addNumber(SearchField.SQUARE, baseModel.getSquare());
        document.addNumber(SearchField.ROOM, baseModel.getRoom());
        document.addText(SearchField.COLOR, baseModel.getColor());
    }
}
