package plugin.pdfextractor;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;


public class PDFExtractorStep extends BaseStep implements StepInterface {

//    public static void main(String[] args) throws Exception{
//		String[] headers = new PrintTextLocations().pdfExtractor("C:\\Users\\Prabhjot\\Desktop\\not working\\jolts.pdf", 6, "Job openings levels and rates by industry and region, seasonally", "Job openings are the number of job openings on the last business day of the month", true)[1];
//		
//		for (int i=0; i<headers.length; i++){
//			System.out.println(headers[i]);
//		}
//		
//	}
	
	private PDFExtractorStepData data;
	private PDFExtractorStepMeta meta;
	
	public PDFExtractorStep(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t, Trans dis) {
		super(s, stepDataInterface, c, t, dis);
	}

	public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
		meta = (PDFExtractorStepMeta) smi;
		
		data = (PDFExtractorStepData) sdi;

		Object[] r = getRow(); // get row, blocks when needed!
		if (r == null) // no more input to be expected...
		{
			setOutputDone();
			return false;
		}
		
		if (first) {
			first = false;
			
			data.outputRowMeta = (RowMetaInterface) getInputRowMeta().clone();
			meta.getFields(data.outputRowMeta, getStepname(), null, null, this);
			if (meta.getErrorMessage() != null) {
				logError("Unexpected error : " + meta.getErrorMessage());;
				setErrors(1);
				stopAll();
				setOutputDone();
				return false;
			}
			
			
			logBasic("template step initialized successfully");

		}
		
		try {
			
			String headerText = "";
			if (meta.getHeaderText()!=null) headerText = meta.getHeaderText();
			String footerText = "";
			if (meta.getFooterText()!=null) footerText = meta.getFooterText();
			
			
			
			ArrayList<String[]> mytesttable = new ArrayList<String[]>(meta.getExtractedTable());
			
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
			
			Date dateobj = new Date();
			
			DateExtractorPDF pdfDate = new DateExtractorPDF();
			String releaseDate;
			if (!meta.getPreLabel().replace(" ", "").isEmpty()) {
				
				if (meta.getDateFormat()!=null) {
					pdfDate.processPDFDate(meta.getRealPath(), meta.getPreLabel(), meta.getDateFormat());
					if (pdfDate.getExtractedDate()!=null) releaseDate = pdfDate.getExtractedDate().getFormattedExtractedDate();
					else {
						System.out.println("Couldn't find the date in pdf, running on today's Date-Time...");
						logBasic("Couldn't find the date in pdf, running on today's Date-Time...");
						releaseDate = df.format(dateobj);
					}
				}
				
				else {
					pdfDate.processPDFDate(meta.getRealPath(), meta.getPreLabel());
					if (pdfDate.getExtractedDate()!=null) {
						ArrayList<String[]> formats = new ArrayList<String[]>(pdfDate.getExtractedDate().getDateFormats());
						meta.setDateFormat(formats.get(0)[1]);
						pdfDate.processPDFDate(meta.getRealPath(), meta.getPreLabel(), meta.getDateFormat());
						releaseDate = pdfDate.getExtractedDate().getFormattedExtractedDate();
					}
					else {
						System.out.println("Couldn't find the date in pdf, running on today's Date-Time...");
						logBasic("Couldn't find the date in pdf, running on today's Date-Time...");
						releaseDate = df.format(dateobj);
					}					
				}				
			}
			
			else releaseDate = df.format(dateobj);
					
			
			for (int i=1; i<mytesttable.size(); i++){
				Object[] outputRow = RowDataUtil.createResizedCopy(r, data.outputRowMeta.size());
				int cellCtr = 0;
				for (int j=0; j<mytesttable.get(i).length; j++){
					outputRow[getInputRowMeta().size() + j] = mytesttable.get(i)[j];
					cellCtr++;
				}
				outputRow[getInputRowMeta().size() + cellCtr] = releaseDate;
				outputRow[getInputRowMeta().size() + cellCtr + 1] = df.format(dateobj);
				putRow(data.outputRowMeta, outputRow);
				outputRow = null;
			}
			
			if (meta.getDeleteFile().equals("t")){
				File file = new File(meta.getRealPath());
				if(file.delete()){
	    			logBasic(file.getName() + " is deleted!");
	    		}else{
	    			logBasic("Delete operation is failed.");
	    		}
			}
		}
		catch(Exception e){
			logBasic(e.toString());
		}		
		
		
		 // copy row to possible alternate rowset(s)

		if (checkFeedback(getLinesRead())) {
			logBasic("Linenr " + getLinesRead()); // Some basic logging
		}

		return true;
	}

	public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (PDFExtractorStepMeta) smi;
		data = (PDFExtractorStepData) sdi;

		return super.init(smi, sdi);
	}

	public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
		meta = (PDFExtractorStepMeta) smi;
		data = (PDFExtractorStepData) sdi;

		super.dispose(smi, sdi);
	}

	//
	// Run is were the action happens!
	public void run() {
		logBasic("Starting to run...");
		try {
			while (processRow(meta, data) && !isStopped())
				;
		} catch (Exception e) {
			logError("Unexpected error : " + e.toString());
			logError(Const.getStackTracker(e));
			setErrors(1);
			stopAll();
		} finally {
			dispose(meta, data);
			logBasic("Finished, processing " + getLinesRead() + " rows");
			markStop();
		}
	}

}
