package plugin.pdfextractor;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

public class PDFExtractorStepData extends BaseStepData implements StepDataInterface {

	public RowMetaInterface outputRowMeta;
	
    public PDFExtractorStepData()
	{
		super();
	}
}
	
