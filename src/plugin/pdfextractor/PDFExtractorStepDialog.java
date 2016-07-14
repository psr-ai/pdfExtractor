package plugin.pdfextractor;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

import dataTypes.ImproperArgsException;
import extractTable.ExtractPDFTable;

import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;

public class PDFExtractorStepDialog extends BaseStepDialog implements StepDialogInterface {
			
	private static Class<?> PKG = PDFExtractorStepMeta.class; // for i18n purposes
	private PDFExtractorStepMeta input;
	
	// output field name
	private Label wlHeaderText,wlFooterText, wlPath, wlSavingPath, wlPleaseWait, wlIgnoreList, wlDate, wlTextT, wlExtDate, wlDateFormats;
	private Text wHeaderText, wFooterText , wPath, wSavingPath, wIgnoreList, wTextT, wExtDate;
	private Combo wDateFormats;
	private FormData fdlHeaderText, fdHeaderText, fdlFooterText, fdFooterText, fdlPath, fdPath, fdBrowsePath, fdSavingPath, fdlSavingPath, fdBrowseSavingPath, fdDeleteFileCheckBox, fdlPleaseWait, fdIgnoreList, fdlIgnoreList, fdlDate, fdT, fdRunDate, fdGetDate, fdlTextT, fdTextT, fdExtDate, fdlExtDate, fdlDateFormats, fdDateFormats;
	private Button browsePath, browseSavingPath, deleteFileCheckBox, wT, wRunDate, wGetDate;
	private Frame mainframe;
	public PDFExtractorStepDialog(Shell parent, Object in, TransMeta transMeta, String sname) {
		super(parent, (BaseStepMeta) in, transMeta, sname);
		input = (PDFExtractorStepMeta) in;
	}

	public String open() {
		Shell parent = getParent();
		Display display = parent.getDisplay();

		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
		props.setLook(shell);
		setShellImage(shell, input);

		ModifyListener lsMod = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				input.setChanged();
			}
		};
		
		SelectionAdapter lsModCheckBox = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event){
				input.setChanged();
			}
		};
		
		changed = input.hasChanged();

		FormLayout formLayout = new FormLayout();
		formLayout.marginWidth = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		shell.setLayout(formLayout);
		shell.setText(BaseMessages.getString(PKG, "Template.Shell.Title")); 

		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;
		
		// Stepname line
		wlStepname = new Label(shell, SWT.RIGHT);
		wlStepname.setText(BaseMessages.getString(PKG, "System.Label.StepName")); 
		props.setLook(wlStepname);
		fdlStepname = new FormData();
		fdlStepname.left = new FormAttachment(0, 0);
		fdlStepname.right = new FormAttachment(middle, -margin);
		fdlStepname.top = new FormAttachment(0, margin);
		wlStepname.setLayoutData(fdlStepname);
			
		wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		wStepname.setText(stepname);
		props.setLook(wStepname);
		wStepname.addModifyListener(lsMod);
		fdStepname = new FormData();
		fdStepname.left = new FormAttachment(middle, 0);
		fdStepname.top = new FormAttachment(0, margin);
		fdStepname.right = new FormAttachment(100, 0);
		wStepname.setLayoutData(fdStepname);

		// PDF File Path
		wlPath = new Label(shell, SWT.RIGHT);
		wlPath.setText(BaseMessages.getString(PKG, "Template.FieldName.DirectoryPath")); 
		props.setLook(wlPath);
		fdlPath = new FormData();
		fdlPath.left = new FormAttachment(0, 0);
		fdlPath.right = new FormAttachment(middle, -margin);
		fdlPath.top = new FormAttachment(wStepname, margin);
		wlPath.setLayoutData(fdlPath);
		
		wPath = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wPath);
		wPath.addModifyListener(lsMod);
		fdPath = new FormData();
		fdPath.left = new FormAttachment(middle, 0);
		fdPath.top = new FormAttachment(wStepname, margin);
		fdPath.right = new FormAttachment(90, 0);
		wPath.setLayoutData(fdPath);
		
		browsePath = new Button(shell, SWT.PUSH);
		browsePath.setText("Browse ...");
		fdBrowsePath = new FormData();
		
		fdBrowsePath.left = new FormAttachment(wPath, 0);
		fdBrowsePath.top = new FormAttachment(wStepname, margin);
		browsePath.setLayoutData(fdBrowsePath);
		
		browsePath.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		        FileDialog dialog = new FileDialog(mainframe, "Select File");
		        dialog.setFile("*.pdf");
		        dialog.setVisible(true);
		        dialog.setAlwaysOnTop(true);		        
		        if (dialog.getFile()!=null) wPath.setText(dialog.getDirectory() + dialog.getFile());
		      }   
		    });
		
		// PDF saving Directory
		wlSavingPath = new Label(shell, SWT.RIGHT);
		wlSavingPath.setText(BaseMessages.getString(PKG, "Template.FieldName.SavingDirectoryPath")); 
		props.setLook(wlSavingPath);
		fdlSavingPath = new FormData();
		fdlSavingPath.left = new FormAttachment(0, 0);
		fdlSavingPath.right = new FormAttachment(middle, -margin);
		fdlSavingPath.top = new FormAttachment(wPath, margin);
		wlSavingPath.setLayoutData(fdlSavingPath);
		
		wSavingPath = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wSavingPath);
		wSavingPath.addModifyListener(lsMod);
		fdSavingPath = new FormData();
		fdSavingPath.left = new FormAttachment(middle, 0);
		fdSavingPath.top = new FormAttachment(wPath, margin);
		fdSavingPath.right = new FormAttachment(90, 0);
		wSavingPath.setLayoutData(fdSavingPath);
		
		browseSavingPath = new Button(shell, SWT.PUSH);
		browseSavingPath.setText("Browse ...");
		fdBrowseSavingPath = new FormData();
		
		fdBrowseSavingPath.left = new FormAttachment(wSavingPath, 0);
		fdBrowseSavingPath.top = new FormAttachment(wPath, margin);
		browseSavingPath.setLayoutData(fdBrowseSavingPath);
		browseSavingPath.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent e) {
		        DirectoryDialog dialog = new DirectoryDialog(shell);
		        dialog.setFilterPath(input.getSavingPath());
		        if (dialog.open()!=null) {
		        	wSavingPath.setText(dialog.getFilterPath());
		        }
		        
		      }   
		    });
		
		//Date Formats
		wlDate = new Label(shell, SWT.RIGHT);
		wlDate.setText("Select Date Type");
		props.setLook(wlDate);
		fdlDate = new FormData();
		fdlDate.left = new FormAttachment(0, 0);
		fdlDate.right = new FormAttachment(middle, -margin);
		fdlDate.top = new FormAttachment(wSavingPath, margin);
		wlDate.setLayoutData(fdlDate);
		
		
		wT = new Button(shell, SWT.RADIO);
		props.setLook(wT);
		wT.setText("Release Date");
		fdT = new FormData();
		fdT.left = new FormAttachment(middle, 0);
		fdT.right = new FormAttachment(100, 0);
		fdT.top = new FormAttachment(wSavingPath, margin);
		wT.setLayoutData(fdT);
		wT.addSelectionListener(lsModCheckBox);

		wRunDate = new Button(shell, SWT.RADIO);
		props.setLook(wRunDate);
		wRunDate.setText("Job Run Date");
		fdRunDate = new FormData();
		fdRunDate.left = new FormAttachment(middle, 0);
		fdRunDate.right = new FormAttachment(100, 0);
		fdRunDate.top = new FormAttachment(wT, margin);
		wRunDate.setLayoutData(fdRunDate);
		wRunDate.addSelectionListener(lsModCheckBox);
		
		
		    
		wlTextT = new Label(shell, SWT.RIGHT);
		wlTextT.setText("Enter Text Preceding Release Date");
		props.setLook(wlTextT);
		fdlTextT = new FormData();
		fdlTextT.left = new FormAttachment(0, 0);
		fdlTextT.right = new FormAttachment(middle, -margin);
		fdlTextT.top = new FormAttachment(wRunDate, margin);
		wlTextT.setLayoutData(fdlTextT);
		
		
	    wTextT = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wTextT);
		wTextT.addModifyListener(lsMod);
		fdTextT = new FormData();
		fdTextT.left = new FormAttachment(middle, 0);
		fdTextT.right = new FormAttachment(100, 0);
		fdTextT.top = new FormAttachment(wRunDate, margin);
		wTextT.setLayoutData(fdTextT);

		
		
	    wlExtDate = new Label(shell, SWT.RIGHT);
	    wlExtDate.setText("Extracted Date");
		props.setLook(wlExtDate);
		fdlExtDate = new FormData();
		fdlExtDate.left = new FormAttachment(0, 0);
		fdlExtDate.right = new FormAttachment(middle, -margin);
		fdlExtDate.top = new FormAttachment(wTextT, margin);
		wlExtDate.setLayoutData(fdlExtDate);
		
		
	    wExtDate = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wExtDate);
		wExtDate.addModifyListener(lsMod);
		fdExtDate = new FormData();
		fdExtDate.left = new FormAttachment(middle, 0);
		fdExtDate.right = new FormAttachment(100, 0);
		fdExtDate.top = new FormAttachment(wTextT, margin);
		wExtDate.setLayoutData(fdExtDate);
		
		
		
	    wlDateFormats = new Label(shell, SWT.RIGHT);
	    wlDateFormats.setText("Select Date Format");
		props.setLook(wlDateFormats);
		fdlDateFormats = new FormData();
		fdlDateFormats.left = new FormAttachment(0, 0);
		fdlDateFormats.right = new FormAttachment(middle, -margin);
		fdlDateFormats.top = new FormAttachment(wExtDate, margin);
		wlDateFormats.setLayoutData(fdlDateFormats);
		
		
		wDateFormats = new Combo(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wDateFormats);
		wDateFormats.addModifyListener(lsMod);
		fdDateFormats = new FormData();
		fdDateFormats.left = new FormAttachment(middle, 0);
		fdDateFormats.right = new FormAttachment(100, 0);
		fdDateFormats.top = new FormAttachment(wExtDate, margin);
		wDateFormats.setLayoutData(fdDateFormats);
	    props.setLook(wDateFormats);
	    
	    wGetDate = new Button(shell, SWT.PUSH);
		props.setLook(wGetDate);
		wGetDate.setText("Get Dates");
		fdGetDate = new FormData();
		fdGetDate.left = new FormAttachment(middle, 0);
		fdGetDate.right = new FormAttachment(40, 0);
		fdGetDate.top = new FormAttachment(wDateFormats, margin);
		wGetDate.setLayoutData(fdGetDate);	
		
		// Header Text
		wlHeaderText = new Label(shell, SWT.RIGHT);
		wlHeaderText.setText(BaseMessages.getString(PKG, "Template.FieldName.Header")); 
		props.setLook(wlHeaderText);
		fdlHeaderText = new FormData();
		fdlHeaderText.left = new FormAttachment(0, 0);
		fdlHeaderText.right = new FormAttachment(middle, -margin);
		fdlHeaderText.top = new FormAttachment(wGetDate, margin);
		wlHeaderText.setLayoutData(fdlHeaderText);

		wHeaderText = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		props.setLook(wHeaderText);
		wHeaderText.addModifyListener(lsMod);
		fdHeaderText = new FormData();
		fdHeaderText.left = new FormAttachment(middle, 0);
		fdHeaderText.right = new FormAttachment(100, 0);
		fdHeaderText.top = new FormAttachment(wGetDate, margin);
		wHeaderText.setLayoutData(fdHeaderText);
		
		// Footer Text
		wlFooterText = new Label(shell, SWT.RIGHT);
		wlFooterText.setText(BaseMessages.getString(PKG, "Template.FieldName.Footer")); 
		props.setLook(wlFooterText);
		fdlFooterText = new FormData();
		fdlFooterText.left = new FormAttachment(0, 0);
		fdlFooterText.right = new FormAttachment(middle, -margin);
		fdlFooterText.top = new FormAttachment(wHeaderText, margin);
		wlFooterText.setLayoutData(fdlFooterText);

		wFooterText = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		
		props.setLook(wFooterText);
		wFooterText.addModifyListener(lsMod);
		fdFooterText = new FormData();
		fdFooterText.left = new FormAttachment(middle, 0);
		fdFooterText.right = new FormAttachment(100, 0);
		fdFooterText.top = new FormAttachment(wHeaderText, margin);
		wFooterText.setLayoutData(fdFooterText);
		
		//Ignore list for characters
		wlIgnoreList = new Label(shell, SWT.RIGHT);
		wlIgnoreList.setText(BaseMessages.getString(PKG, "Template.FieldName.IgnoreList")); 
		props.setLook(wlIgnoreList);
		fdlIgnoreList = new FormData();
		fdlIgnoreList.left = new FormAttachment(0, 0);
		fdlIgnoreList.right = new FormAttachment(middle, -margin);
		fdlIgnoreList.top = new FormAttachment(wFooterText, margin);
		wlIgnoreList.setLayoutData(fdlIgnoreList);

		wIgnoreList = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		
		props.setLook(wIgnoreList);
		wIgnoreList.addModifyListener(lsMod);
		fdIgnoreList = new FormData();
		fdIgnoreList.left = new FormAttachment(middle, 0);
		fdIgnoreList.right = new FormAttachment(100, 0);
		fdIgnoreList.top = new FormAttachment(wFooterText, margin);
		wIgnoreList.setLayoutData(fdIgnoreList);
		
					
		//deleteFile Check Box
		deleteFileCheckBox = new Button(shell, SWT.CHECK);				
		deleteFileCheckBox.setText(BaseMessages.getString(PKG, "Template.FieldName.DeleteFileCheckBox"));		
		fdDeleteFileCheckBox = new FormData();		
		fdDeleteFileCheckBox.left = new FormAttachment(middle, margin);
		fdDeleteFileCheckBox.top = new FormAttachment(wIgnoreList, margin);
		deleteFileCheckBox.setLayoutData(fdDeleteFileCheckBox);
		deleteFileCheckBox.addSelectionListener(lsModCheckBox);
						
		//Please wait label
		wlPleaseWait = new Label(shell, SWT.RIGHT);
		wlPleaseWait.setText(BaseMessages.getString(PKG, "Template.FieldName.PleaseWait"));	
		props.setLook(wlPleaseWait);
		fdlPleaseWait = new FormData();
		fdlPleaseWait.left = new FormAttachment(middle, margin);
		fdlPleaseWait.top = new FormAttachment(deleteFileCheckBox, margin);
		wlPleaseWait.setLayoutData(fdlPleaseWait);
		
		
		// OK and cancel buttons		
		wOK = new Button(shell, SWT.PUSH);
		wOK.setText(BaseMessages.getString(PKG, "System.Button.OK")); 
		wCancel = new Button(shell, SWT.PUSH);
		wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel")); 

		BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel }, margin, wlPleaseWait);
		
		// Add listeners
		
		lsCancel = new Listener() {
			public void handleEvent(Event e) {
				cancel();
			}
		};
		lsOK = new Listener() {
			public void handleEvent(Event e) {
				try {
					ok();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		};
		
		wCancel.addListener(SWT.Selection, lsCancel);
		wOK.addListener(SWT.Selection, lsOK);
		lsDef = new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				try {
					ok();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		};
		
		wT.addListener(SWT.Selection, new Listener(){
	    	public void handleEvent(Event event){
	    		if(wT.getSelection()){
	    			wTextT.setVisible(true);
	    			wlTextT.setVisible(true);
	    			wlDateFormats.setVisible(true);
	    			wDateFormats.setVisible(true);
	    			wExtDate.setVisible(true);
	    			wlExtDate.setVisible(true);
	    			wGetDate.setVisible(true);
	    		}
	    	}
	    });
		
		wRunDate.addListener(SWT.Selection, new Listener(){
	    	public void handleEvent(Event event){
	    		if(wRunDate.getSelection()){
	    			wTextT.setVisible(false);
	    			wlTextT.setVisible(false);
	    			wlDateFormats.setVisible(false);
	    			wDateFormats.setVisible(false);
	    			wExtDate.setVisible(false);
	    			wlExtDate.setVisible(false);
	    			wGetDate.setVisible(false);
	    		}
	    	}
	    });
		
		wGetDate.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				wDateFormats.removeAll();
				DateExtractorPDF pdfDate = new DateExtractorPDF();
				DownloadFile outputFile = new DownloadFile();
				String baseName = FilenameUtils.getBaseName(wPath.getText());
				String extension = FilenameUtils.getExtension(wPath.getText());
				try {
					input.setRealPath(outputFile.downloadFile(wPath.getText(), wSavingPath.getText() + "\\" + baseName + "." + extension));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logBasic(e.getMessage());
				}
				
				try {
					pdfDate.processPDFDate(input.getRealPath(), wTextT.getText());
					
					ArrayList<String[]> formats = new ArrayList<String[]>(pdfDate.getExtractedDate().getDateFormats());
					wExtDate.setText(formats.get(0)[0]);
					for (String[] format: formats){
						wDateFormats.add(format[1]);
					}
					wDateFormats.select(0);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logBasic(e.getMessage());
				}
				
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			

		});

		wStepname.addSelectionListener(lsDef);
		wPath.addSelectionListener(lsDef);
		wHeaderText.addSelectionListener(lsDef);
		wFooterText.addSelectionListener(lsDef);
		wIgnoreList.addSelectionListener(lsDef);
		wSavingPath.addSelectionListener(lsDef);
		
		// Detect X or ALT-F4 or something that kills this window...
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(ShellEvent e) {
				cancel();
			}
		});

		
		// Set the shell size, based upon previous time...
		setSize();

		getData();
		input.setChanged(changed);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return stepname;
	}
	
	// Read data and place it in the dialog
	public void getData() {
		if (input.getReleaseDateSelection().equals("t")) wT.setSelection(true);
		else {
			wRunDate.setSelection(true);
			wTextT.setVisible(false);
			wlTextT.setVisible(false);
			wlDateFormats.setVisible(false);
			wDateFormats.setVisible(false);
			wExtDate.setVisible(false);
			wlExtDate.setVisible(false);
			wGetDate.setVisible(false);
		}
		wStepname.selectAll();
		wPath.setText(input.getPath());
		if (input.getHeaderText()!=null) wHeaderText.setText(input.getHeaderText());
		else wHeaderText.setText("");
		
		if (input.getHeaderText()!=null) wFooterText.setText(input.getFooterText());
		else wFooterText.setText("");
		
		
		if (input.getSavingPath()!=null) wSavingPath.setText(input.getSavingPath());
		else wSavingPath.setText("");
		
		if (input.getIgnoreList()!=null) wIgnoreList.setText(input.getIgnoreList());
		else wIgnoreList.setText("");
		
		wTextT.setText(input.getPreLabel());
		wDateFormats.setText(input.getDateFormat());
		
	}

	private void cancel() {
		stepname = null;
		input.setChanged(changed);
		dispose();
	}
	
	// let the plugin know about the entered data
	private void ok() throws Exception {
		
		stepname = wStepname.getText(); // return value		
		DownloadFile outputFile = new DownloadFile();
		String baseName = FilenameUtils.getBaseName(wPath.getText());
		String extension = FilenameUtils.getExtension(wPath.getText());
		input.setDateFormat(wDateFormats.getText());
		input.setPath(wPath.getText());
		input.setRealPath(outputFile.downloadFile(wPath.getText(), wSavingPath.getText() + "\\" + baseName + "." + extension));
		input.setHeaderText(wHeaderText.getText());
		input.setFooterText(wFooterText.getText());
		input.setSavingPath(wSavingPath.getText());
		input.setIgnoreList(wIgnoreList.getText());
		input.setPreLabel(wTextT.getText());
				
		if (wT.getSelection()) {
			input.setReleaseDateSelection("t");
		}
		else input.setReleaseDateSelection("f");
		
		if (deleteFileCheckBox.getSelection()){
			input.setDeleteFile("t");
		}
		else input.setDeleteFile("f");
		
		
		try{
			ExtractPDFTable pdfExtractor = new ExtractPDFTable();
			ArrayList<String[]> extractedTable = pdfExtractor.processTable(input.getRealPath(), input.getHeaderText(), input.getFooterText());
			for (String comments: pdfExtractor.getCommentsOfExtraction()) {
				logBasic(comments);
			}
			String[] headers = extractedTable.get(0);
			
			String tableHeaders = headers[0];
			for (int i=1; i<headers.length; i++){
				tableHeaders = tableHeaders + "+" + headers[i];
			}
			tableHeaders = tableHeaders + "+" + "T" + "+" + "Run Date";
			input.setTableHeaders(tableHeaders);
			input.setExtractedTable(extractedTable);
		} catch (ImproperArgsException e) {
			logBasic(e.getMessage());
		}
		catch (IndexOutOfBoundsException e){
			logBasic("Cannot find specified Header or Footer " + e.toString());
		}
		catch(Exception e){
			logBasic(e.toString());
		}
		
		
		
		dispose();
	}
}
