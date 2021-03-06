package org.recap.model.marc;

import lombok.Getter;
import lombok.Setter;
import org.marc4j.marc.Record;

import java.util.List;

/**
 * Created by chenchulakshmig on 14/10/16.
 */
@Setter
@Getter
public class BibMarcRecord {
    private Record bibRecord;
    private List<HoldingsMarcRecord> holdingsMarcRecords;

}
