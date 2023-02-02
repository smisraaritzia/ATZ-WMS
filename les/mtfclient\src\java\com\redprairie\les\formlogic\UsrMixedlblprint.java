/**************************************************************
* USER  JIRA        DATE       Description          
* ----  -----       --------   ------------          
* RC    ARTZIA-7298  01/25/23  Mixed SKU ASN label Print               
*
*
*
*Form to able to print the Mixed Case ASN label 
*/

package com.redprairie.les.formlogic;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.redprairie.moca.MocaException;
import com.redprairie.moca.MocaResults;
import com.redprairie.moca.NotFoundException;
import static com.redprairie.mtf.MtfConstants.*;
import com.redprairie.mtf.CMtfUtil;
import com.redprairie.mtf.MtfConstants.EFlow;

import com.redprairie.mtf.foundation.presentation.ACommand;
import com.redprairie.mtf.foundation.presentation.AFormLogic;
import com.redprairie.mtf.foundation.presentation.ATimerAction;
import com.redprairie.mtf.foundation.presentation.CWidgetActionAdapter;
import com.redprairie.mtf.foundation.presentation.CVirtualKey;

import com.redprairie.mtf.foundation.presentation.ICommand;
import com.redprairie.mtf.foundation.presentation.IContainer;
import com.redprairie.mtf.foundation.presentation.IDisplay;
import com.redprairie.mtf.foundation.presentation.IEntryField;
import com.redprairie.mtf.foundation.presentation.IForm;
import com.redprairie.mtf.foundation.presentation.IFormSegment;
import com.redprairie.mtf.foundation.presentation.IStaticText;
import com.redprairie.mtf.foundation.presentation.ITextSelection;
import com.redprairie.mtf.foundation.presentation.ITimerAction;
import com.redprairie.mtf.foundation.presentation.IVirtualKey;
import com.redprairie.mtf.foundation.presentation.IWidgetActionValidator;
import com.redprairie.mtf.foundation.presentation.IInteractiveWidget;
import com.redprairie.wmd.mtfutil.CCarMovMaxUsers;
import com.redprairie.wmd.mtfutil.CVehLodLimit;

/*
import static com.redprairie.mtf.Mcsgendef.*;
import static com.redprairie.mtf.Salgendef.*;
import static com.redprairie.wmd.Dcsgendef.*;
import static com.redprairie.wmd.RfErr.*;
import static com.redprairie.wmd.RfFormat.*;
*/

import org.apache.log4j.Logger;
import com.redprairie.wmd.mtfutil.CWmdMtfUtil;
import com.redprairie.wmd.WMDErrors;
import com.redprairie.wmd.WMDConstants;

import com.redprairie.mtf.exceptions.XInvalidRequest;
import com.redprairie.mtf.exceptions.XFormAlreadyOnStack;
import com.redprairie.mtf.exceptions.XFailedRequest;
import com.redprairie.mtf.exceptions.XInvalidArg;
import com.redprairie.mtf.exceptions.XInvalidState;


public class UsrMixedlblprint extends AFormLogic {


	// Instance Variables
    //Entry Fields
	public boolean 					formReturn;
	public IWidgetActionValidator	actATZMixedCaselblPrint;
	/*public IWidgetActionValidator	actDestID;
	public IEntryField				efDestID;*/
	public IWidgetActionValidator	actCaseID;
	public IEntryField				efCaseID;
	/*public IWidgetActionValidator	actSrcID;
	public IEntryField				efSrcID;*/
	public IEntryField				efCtnMsg1;
	public IEntryField				efCtnMsg2;
	public IEntryField				efCtnMsg3;
	public IFormSegment				segDef;
	
	//Setting F1 Key
	public ICommand					cmdFkeyGoBack;
	
	//Setting up F6 Key and function variables
    /*public ACommand                 cmdFkeyDone;*/
	private int 					uc_dest_pol_group;
	private int 					uc_src_pol_group;
	private int				    	uc_from_dest;
	
	
	
	
	public CWmdMtfUtil wmdMtf = (CWmdMtfUtil) session.getGlobalObjMap().get("WMDMTF");

	
    public UsrMixedlblprint(IDisplay _display) throws Exception {

        super(_display);
		//log.info("construct - display add entry field widgets");
        // Create form and default segment
		Logger.getLogger(getClass()).debug("Construction of display and adding entry field widgets");

        frmMain = display.createForm("USR_ATZ_MIXED_CASE_LBL_PRINT");
		actATZMixedCaselblPrint = this.new ATZMixedCaselblPrintActions();
        frmMain.addWidgetAction(actATZMixedCaselblPrint);

        segDef = frmMain.createSegment("segDef", false);
		
		frmMain.setTitle("ttlUsrMixedlblprint");

        // Create widgets on the form, entry fields

		efCaseID = segDef.createEntryField("uc_case_id", "lblUcCaseId");
		actCaseID = this.new CaseIdActions();
		efCaseID.addWidgetAction(actCaseID);


        // Create and bind function keys

        frmMain.unbind(frmMain.getCancelCommand());
        cmdFkeyGoBack = this.new FkeyBackCommand();
        cmdFkeyGoBack.setVisible(false);
        frmMain.bind(cmdFkeyGoBack);
        cmdFkeyGoBack.bind(VK_FKEY_BACK);
		
		
		// Message Fields - contains error message for display
		efCtnMsg1 = segDef.createEntryField("uc_msg1");
		efCtnMsg2 = segDef.createEntryField("uc_msg2");
		efCtnMsg3 = segDef.createEntryField("uc_msg3");
    }

    /**
     * Display and run the form 
     */
	 
    public void run() throws XFormAlreadyOnStack, XInvalidRequest, XFailedRequest {
		//log.trace("run-interact");
		frmMain.interact();
	}
	
	
	    /**
     * Defines extended ACommand for FkeyBack 
     */
    private class FkeyBackCommand extends ACommand {

        /**
         * Prime constructor.
         */
        public FkeyBackCommand() {
            super("cmdFkeyBack", "FkeyBack",  FKEY_BACK_CAPTION, '1');
        }
	
	        /**
         * Performs the actions for the function key.
         *
         * @param _container
         *           Container
         */
        public void execute(IContainer _container) throws ClassNotFoundException, NullPointerException, XFormAlreadyOnStack, XInvalidRequest, XFailedRequest, MocaException {

            AFormLogic     newFrm     = null;
           
            newFrm = display.createFormLogic("CHANGE_MODE_UNDIR");
            newFrm.run();
        }

        private static final long serialVersionUID = 0L;
		
	}
	


    /** 
	* This class contains the entry/exit actions for the efDestID field
	*/
	private class CaseIdActions extends CWidgetActionAdapter {
		
		
		public boolean onFieldExit(IInteractiveWidget _ef) throws Exception {
			
			AFormLogic 	newFrm 		= null;
			MocaResults rs			= session.getGlobalMocaResults();
			int			retStatus	= WMDErrors.eOK;
			String		mocaErrStr	= "";
			String 		dlg 		= "";
			
			uc_from_dest    =  '0';
			
			//Validate the Case IDLEntity
			if(CMtfUtil.length(efCaseID) == 1) {
				
				
				try {
					
					Logger.getLogger(getClass()).debug("Check Case ID");
					rs = null;
					retStatus = WMDErrors.eOK;
					mocaErrStr = "";
					rs = session.executeDSQL("generate usr mixed case asn label where phy_subnum = '" + efCaseID.getText() + 
												"' and wh_id = '" + display.getVariable("global.wh_id") + "'  catch(-1403,510) "  );
						
				}
				catch(MocaException e) {
					retStatus = e.getErrorCode();
					mocaErrStr = e.getLocalizedMessage();
				}
				
				// if invalid identifier, throw error and clear Case ID field
				if(retStatus != WMDErrors.eOK) {
					frmMain.displayErrorMessage();
					efCaseID.setFocus();
					return false;
				}
				
				if(rs == null && retStatus != WMDErrors.eOK) {
					
					efCaseID.setFocus();
				}
			
			    else if(rs != null && retStatus == WMDErrors.eOK) {
					rs.next();
					frmMain.promptMessageAnyKey("Label(s) Printed Successfully");
					efCaseID.clear();
					efCaseID.setFocus();
					frmMain.resetMessageLine();
										
				}
			
			}
			return true;
		}
		
		
		
		
	}


    /**
     * This class contains the entry/exit actions for the form (frmMain) 
     */
    private class ATZMixedCaselblPrintActions extends CWidgetActionAdapter {

        public boolean onFormEntry(IForm _frm) throws Exception {

            AFormLogic       newFrm     = null;
            MocaResults      rs         = session.getGlobalMocaResults();
            int              retStatus  = WMDErrors.eOK;
			boolean		     isOverriding = false;
			
			// Clear form fields
			frmMain.clearForm();
			// Set initial state
			//cmdFkeyDone.setEnabled(true);
			
			//reset inital values, set values for form start
			efCaseID.setEnabled(true);
			efCaseID.setVisible(true);
			efCaseID.setEntryRequired(true);
			
			efCtnMsg1.setEntryRequired(false);
			efCtnMsg1.setEnabled(false);
			efCtnMsg1.setVisible(false);

			efCtnMsg2.setEntryRequired(false);
			efCtnMsg2.setEnabled(false);
			efCtnMsg2.setVisible(false);

			efCtnMsg3.setEntryRequired(false);
			efCtnMsg3.setEnabled(false);
			efCtnMsg3.setVisible(false);		

			setMessage("", "", "");
			
			String mocaErrStr = "";
			
			efCaseID.clear();	
			efCaseID.setFocus();

            return true; 
			
	    }	

        public boolean onFormExit(IForm _frm) throws Exception {
			  
			frmMain.resetMessageLine();
			return false;
		}
		
		private void setMessage(String msg1, String msg2, String msg3) {
			
			boolean show = false;
			
			if (msg1 != "" || msg2 != "" || msg3 != "") {
				// showing message
				show = true;
			}
			
			//Sets up display/error messages for RF screen
			efCtnMsg1.setText(msg1);
			efCtnMsg2.setText(msg2);
			efCtnMsg3.setText(msg3);
			
			efCtnMsg1.setVisible(show);
			efCtnMsg2.setVisible(show);
			efCtnMsg3.setVisible(show);		

			frmMain.refresh();
		}
		
		private void displayError() {
			
			setMessage("", "", "");
			display.beep();
			frmMain.displayErrorMessage();
			frmMain.resetMessageLine();
		}
		
		
		
    }

  
}

