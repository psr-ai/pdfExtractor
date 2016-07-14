package plugin.pdfextractor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.*;
import org.pentaho.di.core.database.DatabaseMeta; 
import org.pentaho.di.core.exception.*;
import org.pentaho.di.core.row.*;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.*;
import org.pentaho.di.trans.*;
import org.pentaho.di.trans.step.*;
import org.w3c.dom.Node;

import dataTypes.ImproperArgsException;
import extractTable.ExtractPDFTable;

public class PDFExtractorStepMeta extends BaseStepMeta implements StepMetaInterface {

	private static Class<?> PKG = PDFExtractorStepMeta.class; // for i18n purposes
	private String headerText;
	private String footerText;
	private String path;
	private String tableHeaders;
	private String savingPath;
	private String deleteFile;
	private String realPath;
	private String ignoreList;
	private String dateFormat;
	private String preLabel;
	private String releaseDateSelection;
	private ArrayList<String[]> extractedTable;
	private String errorMessage;
		
	public String getErrorMessage() {
		return errorMessage;
	}



	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}



	public ArrayList<String[]> getExtractedTable() {
		return extractedTable;
	}



	public void setExtractedTable(ArrayList<String[]> extractedTable) {
		this.extractedTable = extractedTable;
	}



	public String getReleaseDateSelection() {
		return releaseDateSelection;
	}



	public void setReleaseDateSelection(String releaseDateSelection) {
		this.releaseDateSelection = releaseDateSelection;
	}



	public String getPreLabel() {
		return preLabel;
	}



	public void setPreLabel(String preLabel) {
		this.preLabel = preLabel;
	}

	public String getDateFormat() {
		return dateFormat;
	}



	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}



	public String getIgnoreList() {
		return ignoreList;
	}



	public void setIgnoreList(String ignoreList) {
		this.ignoreList = ignoreList;
	}



	public String getRealPath() {
		return realPath;
	}



	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}



	public String getDeleteFile() {
		return deleteFile;
	}



	public void setDeleteFile(String deleteFile) {
		this.deleteFile = deleteFile;
	}



	public String getSavingPath() {
		return savingPath;
	}



	public void setSavingPath(String savingPath) {
		this.savingPath = savingPath;
	}


	public String getTableHeaders() {
		return tableHeaders;
	}



	public void setTableHeaders(String tableHeaders) {
		this.tableHeaders = tableHeaders;
	}



	public String getPath() {
		return path;
	}

	

	public void setPath(String path) {
		this.path = path;
	}



	public PDFExtractorStepMeta() throws IOException {
		super();		
	}
	
	
	
	public String getHeaderText() {
		return headerText;
	}



	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}



	public String getFooterText() {
		return footerText;
	}



	public void setFooterText(String footerText) {
		this.footerText = footerText;
	}

	public String getXML() throws KettleValueException {
		String retval = "";
		retval += "		<headertext>" + getHeaderText() + "</headertext>" + Const.CR;
		retval += "		<footertext>" + getFooterText() + "</footertext>" + Const.CR;
		retval += "		<path>" + getPath() + "</path>" + Const.CR;
		retval += "		<headers>" + getTableHeaders() + "</headers>" + Const.CR;
		retval += "		<savingpath>" + getSavingPath() + "</savingpath>" + Const.CR;
		retval += "		<deletefile>" + getDeleteFile() + "</deletefile>" + Const.CR;
		retval += "		<realpath>" + getRealPath() + "</realpath>" + Const.CR;
		retval += "		<ignorelist>" + getIgnoreList() + "</ignorelist>" + Const.CR;
		retval += "		<dateformat>" + getDateFormat() + "</dateformat>" + Const.CR;
		retval += "		<prelabel>" + getPreLabel() + "</prelabel>" + Const.CR;
		retval += "		<releasedateselection>" + getReleaseDateSelection() + "</releasedateselection>" + Const.CR;
		return retval;
	}

	@SuppressWarnings("deprecation")
	public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info, StepMeta nextStep, VariableSpace space) {
		DownloadFile outputFile = new DownloadFile();
		String baseName = FilenameUtils.getBaseName(getPath());
		String extension = FilenameUtils.getExtension(getPath());
		try {
			setRealPath(outputFile.downloadFile(getPath(), getSavingPath() + "\\" + baseName + "." + extension));
		} catch (java.net.UnknownHostException e) {
			logBasic(e.getMessage());
			logBasic("URL cannot be defined.");
		}
		catch (Exception e1) {
			e1.printStackTrace();
			logBasic(e1.getMessage());
		}
				
		if (getHeaderText()==null) setHeaderText("");
		if (getFooterText()==null) setFooterText("");
		
		try {
			ExtractPDFTable pdfExtractor = new ExtractPDFTable();
			extractedTable = new ArrayList<String[]>(pdfExtractor.processTable(getRealPath(), getHeaderText(), getFooterText()));
			
			for (String comments: pdfExtractor.getCommentsOfExtraction()) {
				logBasic(comments);
			}
			
			String[] headers = extractedTable.get(0);
			if (headers[0].equals("")) tableHeaders = "col0";
			else tableHeaders = headers[0];
			int count = 1;
			for (int i=1; i<headers.length; i++){				
				if (headers[i].replace(" ", "").isEmpty()) {
					tableHeaders = tableHeaders + "+" + "col" + String.valueOf(count);
					count++;
				}
				else tableHeaders = tableHeaders + "+" + headers[i];
			}
			tableHeaders = tableHeaders + "+" + "T" + "+" + "RunDate";
		} catch(ImproperArgsException e) {
			logBasic(e.getMessage());
			setErrorMessage(e.getMessage());
		}
		catch (java.io.FileNotFoundException e) {
			logBasic(e.getMessage());
			logBasic("Couldn't find the specified file");
			setErrorMessage(e.getMessage());
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			logBasic(e.getMessage());
			setErrorMessage(e.getMessage());
			e.printStackTrace();			
		}
		
		String[] myhead = tableHeaders.split("\\+");
		
				
		// append the outputField to the output
		for (int i=0; i<myhead.length; i++){
			ValueMetaInterface v = new ValueMeta();
			
			v.setName(myhead[i]);
			v.setType(ValueMeta.TYPE_STRING);
			v.setTrimType(ValueMeta.TRIM_TYPE_BOTH);
			v.setOrigin(origin);

			r.addValueMeta(v);
		}
		if (preLabel == null) preLabel = ""; 
			
	}

	public Object clone() {
		Object retval = super.clone();
		return retval;
	}

	public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleXMLException {

		try {
			setPath((XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "path"))));
			setHeaderText(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "headertext")));
			setFooterText(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "footertext")));
			setTableHeaders(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "headers")));
			setSavingPath(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "savingpath")));
			setDeleteFile(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "deletefile")));
			setRealPath(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "realpath")));
			setIgnoreList(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "ignorelist")));
			setDateFormat(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "dateformat")));
			setPreLabel(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "prelabel")));
			setReleaseDateSelection(XMLHandler.getNodeValue(XMLHandler.getSubNode(stepnode, "releasedateselection")));
		} catch (Exception e) {
			throw new KettleXMLException("Template Plugin Unable to read step info from XML node", e);
		}

	}

	public void setDefault() {
		path = "http://www.census.gov/retail/mrts/www/data/pdf/ec_current.pdf";
		headerText = "Table 1. Estimated Quarterly U.S. Retail Sales: Total and E-commerce1 (Estimates are based on data from the Monthly Retail Trade Survey and administrative records. Unless otherwise specified, all estimates are revised based on the 2014 Annual Retail Trade Survey and the final results from the 2012 Economic Census.)";
		footerText = "";
		tableHeaders = "";
		savingPath = "C:\\Windows\\Temp";
		deleteFile = "f";
		realPath = "";
		ignoreList = "";
		dateFormat = "";
		preLabel = "";
		releaseDateSelection = "t";
		
	}

	public void check(List<CheckResultInterface> remarks, TransMeta transmeta, StepMeta stepMeta, RowMetaInterface prev, String input[], String output[], RowMetaInterface info) {
		CheckResult cr;

		// See if we have input streams leading to this step!
		if (input.length > 0) {
			cr = new CheckResult(CheckResult.TYPE_RESULT_OK, "Step is receiving info from other steps.", stepMeta);
			remarks.add(cr);
		} else {
			cr = new CheckResult(CheckResult.TYPE_RESULT_ERROR, "No input received from other steps!", stepMeta);
			remarks.add(cr);
		}	
    	
	}

	public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta, TransMeta transMeta, String name) {
		return new PDFExtractorStepDialog(shell, meta, transMeta, name);
	}

	public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int cnr, TransMeta transMeta, Trans disp) {
		return new PDFExtractorStep(stepMeta, stepDataInterface, cnr, transMeta, disp);
	}

	public StepDataInterface getStepData() {
		return new PDFExtractorStepData();
	}

	public void readRep(Repository rep, ObjectId id_step, List<DatabaseMeta> databases, Map<String, Counter> counters) throws KettleException {
		try
		{
			path = rep.getStepAttributeString(id_step, "path"); //$NON-NLS-1$
			headerText  = rep.getStepAttributeString(id_step, "headertext"); //$NON-NLS-1$
			footerText  = rep.getStepAttributeString(id_step, "footertext"); //$NON-NLS-1$
			tableHeaders = rep.getStepAttributeString(id_step, "tableheaders"); //$NON-NLS-1$
			savingPath = rep.getStepAttributeString(id_step, "savingpath"); //$NON-NLS-1$
			deleteFile = rep.getStepAttributeString(id_step, "deletefile"); //$NON-NLS-1$
			realPath = rep.getStepAttributeString(id_step, "realpath"); //$NON-NLS-1$
			ignoreList = rep.getStepAttributeString(id_step, "ignorelist"); //$NON-NLS-1$
			dateFormat = rep.getStepAttributeString(id_step, "dateFormat"); //$NON-NLS-1$
			preLabel = rep.getStepAttributeString(id_step, "prelabel"); //$NON-NLS-1$
			releaseDateSelection = rep.getStepAttributeString(id_step, "releasedateselection"); //$NON-NLS-1$
		}
		catch(Exception e)
		{
			throw new KettleException(BaseMessages.getString(PKG, "TemplateStep.Exception.UnexpectedErrorInReadingStepInfo"), e);
		}
	}

	public void saveRep(Repository rep, ObjectId id_transformation, ObjectId id_step) throws KettleException
	{
		try
		{
			rep.saveStepAttribute(id_transformation, id_step, "path", path); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "headertext", headerText); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "footertext", footerText); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "tableheaders", tableHeaders); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "savingpath", savingPath); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "deletefile", deleteFile); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "realpath", realPath); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "ignorelist", ignoreList); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "dateformat", dateFormat); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "prelabel", preLabel); //$NON-NLS-1$
			rep.saveStepAttribute(id_transformation, id_step, "releasedateselection", releaseDateSelection); //$NON-NLS-1$
		}
		catch(Exception e)
		{
			throw new KettleException(BaseMessages.getString(PKG, "TemplateStep.Exception.UnableToSaveStepInfoToRepository")+id_step, e); 
		}
	}
}
