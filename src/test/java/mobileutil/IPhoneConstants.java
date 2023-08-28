package mobileutil;

public class IPhoneConstants {
    public class Common {
        public static final String type_xpath = "xpath";
        public static final String type_name = "name";
        public static final String type_id = "id";
        public static final String type_class = "classname";
        public static final String type_accessibilityId = "accessibilityId";
        public static final String type_predicateString = "predicateString";
        public static final String type_iOSClassChain = "iOSClassChain";
    }


    public static class PayRange {

        public static final String signOut = "value=\"Sign out\"";
        public static final String iconClose = "Close";

        public static final String btnLoginWithEmail = "Login with Email";

        public static final String iconPlusAddFunds = "Show Funding Sheet";
        public static final String sliderAddFunds = "**/XCUIElementTypeSlider[`value == \"0.00\"`]";
        public static final String lblAddedCreditCard = "label BEGINSWITH[c] 'Card'";
        public static final String lblTotalAmountToAdd = "type == 'XCUIElementTypeStaticText'  AND name BEGINSWITH[c] '$'";
        public static final String lblAmountToFund = "**/XCUIElementTypeStaticText[`name BEGINSWITH \"$\"`]";
        public static final String hamburgerMenu = "home_icon";
        public static final String menuOptionSettings = "Settings";
        public static final String lblClearKeyChain = "label == \"Clear Keychain\"";
        public static final String btnCloseFundingPopUp = "Close Funding Sheet";
        public static final String btnBackFromLoginScreen = "**/XCUIElementTypeStaticText[`label == \" Back\"`]";
        public static final String btnDoneFromAddFundsScreen = "label == \"Done\"";
        public static final String lblAddedToYourAccount = "label == \"was added to your account.\"";

        public static final String menuOptionRegisterDevice = "value=\"Register Device\"";
        public static final String lblDeviceInformation = "label == \"Device Information\"";
        public static final String titleRegisterDevice = "label == \"Register Device\"";

        public static final String txtDeviceSerialPlaceHolder = "name == \"Serial Number\" AND type=\"XCUIElementTypeStaticText\"";
        public static final String txtDeviceSerialNumber = "**/XCUIElementTypeTextField[1]";
        public static final String txtPINPlaceHolder = "name == \"PIN\" AND type=\"XCUIElementTypeStaticText\"";
        public static final String txtMachineIDPlaceHolder = "name == \"Machine ID\" AND type=\"XCUIElementTypeStaticText\"";
        public static final String txtPositionPlaceHolder = "name == \"Position\" AND type=\"XCUIElementTypeStaticText\"";
        public static final String txtPIN = "**/XCUIElementTypeTextField[2]";
        public static final String txtMachineIDofDeviceScreen = "**/XCUIElementTypeTextField[3]";
        public static final String txtPositionOfDeviceScreen = "**/XCUIElementTypeTextField[4]";
        public static final String iconCameraForDeviceSerialTextBox = "**/XCUIElementTypeStaticText[`label == \"photo_camera\"`][1]";
        public static final String iconCameraForMachineIDTextBox = "**/XCUIElementTypeStaticText[`label == \"photo_camera\"`][2]";
        public static final String iconCameraForPositionTextBox = "**/XCUIElementTypeStaticText[`label == \"photo_camera\"`][3]";
        public static final String btnNext = "NEXT";
        public static final String btnDoneOfKeyBoard_accessibility = "Done";
        public static final String btnCancelFromDropDownPopUp = "label == \"CANCEL\"";
        public static final String btnHomeDeviceRegisterStep1 = "Home";
        public static final String dropDownCategory = "**/XCUIElementTypeStaticText[`label == \"keyboard_arrow_down\"`][2]";
        public static final String getDropDownCategorySelectedValue = "**/XCUIElementTypeTextField[2]";
        public static final String dropDownSUBCategory = "**/XCUIElementTypeStaticText[`label == \"keyboard_arrow_down\"`][3]";
        public static final String optionsListFromDropDown = "//XCUIElementTypeOther[@name='banner']/following-sibling::XCUIElementTypeOther/XCUIElementTypeOther";
        public static final String btnNewUser = "guest_view_button_newuser";


        public static final String lblAlredyHaveAnAccount = "label == \"Already have an account?\"";
        public static final String btnNoFromPopUp = "label == \"No\"";
        public static final String lblProfileTitle = "profile_title";
        public static final String btnPaymentMethod = "Payment Method";
        public static final String optionCreditDebitCard = "Credit / Debit Card";
        public static final String txtCardNumber = "**/XCUIElementTypeTextField[`label == \"Card Number Input\"`][1]";
        public static final String lblCreditCardWithNumber = "Card 4242";
        public static final String txtCardExpiryDate = "**/XCUIElementTypeTextField[`label == \"Card expiration date\"`][2]";
        public static final String txtCardCVNumber = "**/XCUIElementTypeTextField[`label == \"Card Security Code\"`][3]";
        public static final String txtCardPostalCode = "**/XCUIElementTypeTextField[`label == \"Text input\"`][4]";
        public static final String btnSubmitFunds = "Submit";
        public static final String optionPrepaidUSD = "PayRange Prepaid USD";
        public static final String btnDeleteCard = "Delete";
        public static final String btnYes = "Yes";
        public static final String btnNo = "No";
        public static final String listViewoFDevices = "laundry_list_view";
        public static final String menuOptionSignOutBtn = "**/XCUIElementTypeStaticText[`label == \"Sign out\"`]";

        public static final String btnAddMockMachine = "//XCUIElementTypeSegmentedControl[@visible=\"true\"]//XCUIElementTypeButton";
        public static final String lblKeyChainValue = "label == \"3+ set\"";
        public static final String rdbForceQuitOnBackg = "//XCUIElementTypeStaticText[@value=\"Force Quit on Background\"]/following-sibling::XCUIElementTypeSwitch";
        public static final String rdbForceDeferTransaction = "//XCUIElementTypeStaticText[@value=\"Force Defer transaction\"]/following-sibling::XCUIElementTypeSwitch";
        public static final String rdbForceCrash = "//XCUIElementTypeStaticText[@value=\"Force Crash\"]/following-sibling::XCUIElementTypeSwitch";
        public static final String rdbEnableAutoSwipe = "//XCUIElementTypeStaticText[@value=\"Enable auto swipe\"]/following-sibling::XCUIElementTypeSwitch";
        public static final String rdbEnable3DotLogging = "//XCUIElementTypeStaticText[@value=\"Enable 3dot Logging\"]/following-sibling::XCUIElementTypeSwitch";


        public static final String menuOptionTransactions = "**/XCUIElementTypeStaticText[`label == \"View Transactions\"`]";
        public static final String transactionsData = "(//XCUIElementTypeStaticText[contains(@label,\"Funding\")])[1]/parent::XCUIElementTypeCell/XCUIElementTypeStaticText";

        public static final String btnBackOfMenu_name = "Home";
        public static final String txtMachineName_predicate = "label == \"$MachineName\"";
        public static final String txtProdMachineName_predicate = "label CONTAINS \"ARCVending\"";

        public static final String optionSelectionItems_chain = "**/XCUIElementTypeButton[`label == \"Tap to Select Item\"`]";
        public static final String optionSelectionItems_name = "Touchless Selection";
        public static final String labelA_name = "A";
        public static final String label1_name = "1";
        public static final String sliderToPay_accessibility = "Tap to Pay";
        public static final String btnOkayPostPayment_predicate = "label == \"Okay\"";
        public static final String lblStartedByMe_predicate = "label == \"Started by me\"";
        public static final String lblStartedByMeCount_name = "section_items_count";
        public static final String lblFlipToBack_accessibility = "flip_to_back";
        public static final String btnRefreshDevices_accessibility = "action_refresh";
        public static final String lblFPFOffer_predicate = "label == \"$3.00 OFF\"";
        //		public static final String lblFPFOffer_predicate="type == 'XCUIElementTypeStaticText' AND value BEGINSWITH[c] 'Your first purchase for' AND visible == 1";
        public static final String creditOption$3_predicate = "type == 'XCUIElementTypeStaticText' AND value BEGINSWITH[c] '#Amount' AND visible == 1";
        public static final String dropDownCredit_name = "credit_options_layout";
        public static final String btnFlipToFront_name = "flip_to_front";
        public static final String devicesList_name = "item_conatiner";
        public static final String menuOptionContactUs_name = "Contact Us";
        public static final String contactUsCategoryOptions_chain = "**/XCUIElementTypeStaticText[`name == \"category_name\" AND visible == 1`]";
        public static final String contactUsCategories = "PurchaseFundingLogin / AccountMachineOther";

        public static final String topics_name = "label";
        public static final String otherCatTopics = "I have a question about the PayRange AppI have a suggestion or commentMy issue is not listed above";
        public static final String machineCatTopics = "App won't find or connect to machineThere is a problem with the machineThe machine needs to be restockedI have a product request for vending machine";
        public static final String loginAccountCatTopics = "I forgot my passwordI can't loginI want to change my email or phone number";
        public static final String fundingCatTopics = "I accidentally loaded too much moneyI need my balance refunded back to my cardI funded with the wrong currencyI had a balance, but it's now goneMy card keeps declining or I can't register a cardI want to change or update my card on fileI don't know my PINMy card got double charged";
        public static final String purchaseCatTopics = "I was charged, but did not receive my productI was charged twiceI was charged but the machine didn't startI didn't get my discount or promotionI see a charge on my transaction summary that I didn't make";
        public static final String optionCategory_chain = "**/XCUIElementTypeStaticText[`name == \"category_name\" AND label==\"@Cat\" AND visible == 1`]";
        public static final String lblTransactionAlert_predicate = "label == \"Please select a transaction to report a problem\"";
        public static final String txtSubject_name = "subjectField";
        public static final String menuOptionHelp_name = "Help";
        public static final String helpScreenText_chain = "**/XCUIElementTypeOther[`name == \"help_text\"`]/**/XCUIElementTypeStaticText";
        public static final String helpScreenContent = "HelpCenterTell us about your issue so we can help you more quicklyGetting StartedFunding SourcesRefundsNew UserDeactivateConnecting to the machineCompatible phonesAge VerificationFAQs and training for PayRange App, Device Installation, and PayRange Management PortalGetting StartedFrequently Asked QuestionsDevice Installation and RegistrationWeekly Sales Report (Financials)Manage ConsoleRewards ProgramsFeature Release NotesAge Verification OperatorsGeneral QuestionsPayRangeLINKSSUPPORTCONTACT\uF09A\uF099\uF08C© PayRange. Theme by Start typing your search…Consumer SupportOperator SupportGeneral InformationLotus ThemesAbout UsTermsSecurityCommunityStatusPrivacysupport@payrange.com";
        public static final String lblAboutPayRange_predicate = "label == \"About PayRange\"";

        public static final String logoAboutScreen_chain = "**/XCUIElementTypeOther[`name == \"about_us_text\"`]/**/XCUIElementTypeImage";
        public static final String aboutPageText_chain = "**/XCUIElementTypeOther[`name == \"about_us_text\"`]/**/XCUIElementTypeStaticText";
        public static final String aboutScreenContent = "PayRange is focused on eliminating payment as the barrier. Doing transactions on machines such as parking meters, vending machines, laundry washers and dryers using bills and coins has traditionally been a painful experience. We want to solve this user need and provide a seamless experiences while simultaneously addressing operator's constraints. First it must work for the operator, or there will be little deployment. Second, it must be simple or there won't be many using it.Our exceptionally talented team is working to break the payment barrier by bringing cost-effective, easy-to-use mobile payments to the other 95% of machines that include vending, parking meters, luggage carts, transit ticketing, laundromats, amusement, and more. Please visit us at to learn more.http://www.payrange.com";

        public static final String msgSignUpError_predicate = "label == \"There is already an email account which uses this address. Press Continue to sign-in to this account.\"";
        public static final String btnCancel_name = "Cancel";

        public static final String btnContinue_name = "Continue";
        public static final String iconSpinnerArrow_id = "spinner_arrow";
        public static final String credit_options_layout_name = "credit_options_layout";
        public static final String dropDownCreditOptions_chain = "**/XCUIElementTypeOther[`name == \"selected_credit_textview\"`]/XCUIElementTypeStaticText";
        public static final String lblPleaseFundYourAccount_aID = "Please fund your account.";
        public static final String popUpPleaseFundAccount_chain = "**/XCUIElementTypeStaticText[`name BEGINSWITH \"You do not have enough balance to make a transaction.\"`]";
        public static final String btnOkayOfTransactionPopUp_name = "Okay";
        public static final String lblGiftCard_predicate = "label == \"Store Gift Cards\"";
        public static final String cardBestBuy_chain = "**/XCUIElementTypeOther[`label == \"Best Buy\"`]";
        public static final String txtGiftCardNumber_chain = "**/XCUIElementTypeOther[`label == \"main\"`]/XCUIElementTypeTextField[1]";
        public static final String txtGiftCardPin_chain = "**/XCUIElementTypeOther[`label == \"main\"`]/XCUIElementTypeTextField[2]";
        public static final String btnCheckBalance_name = "Check balance";
        public static final String btnConfirmTransfer_aID = "Confirm transfer";
        public static final String btnContinue_aID = "Continue »";
        public static final String btnBack_name = "Back";
        public static final String msgExpirationDateValidation_name = "Please enter valid card details and try again.";
        public static final String msgPostalCodeValidationError_name = "Postal code validation failed";
        public static final String lblTermsAndConditions_name = "Terms and Conditions";
        public static final String iconCloseWhite_name = "icon close white";
        public static final String lblValidEmail_name = "Please enter a valid email address.";
        public static final String transactionRow_name = "transaction_row";
        public static final String popUpHeaderTransaction_predicate = "type == 'XCUIElementTypeStaticText' AND name==\"screen_title\" AND value =='Transaction Details' AND visible == 1";
        public static final String labelAmountOfPopUp_name = "transaction_amount";
        public static final String labelDeviceName_name = "header1_details";
        public static final String labelPostDate_chain = "**/XCUIElementTypeStaticText[`label CONTAINS \"Post Date\"`]";
        public static final String lblTransaction_chain = "**/XCUIElementTypeStaticText[`label CONTAINS \"Transaction Date\"`]";
        public static final String lblPurchasePrice_chain = "**/XCUIElementTypeStaticText[`label CONTAINS \"Purchase Price\"`]";
        public static final String btnReportAProblem_name = "Report a Problem";
        public static final String txtEmailRecipientOfEmailScreen_predicate = "type == 'XCUIElementTypeTextField' AND name==\"toField\" AND value CONTAINS 'support@payrange.com' AND visible == 1";
        public static final String lblSubject_predicate = "type == 'XCUIElementTypeTextView' AND name==\"subjectField\" AND value CONTAINS 'Problem with transaction' AND visible == 1";
        public static final String lblMailBody_predicate = "type == 'XCUIElementTypeTextField' AND name==\"Message Body\" AND value CONTAINS 'Please describe your problem:'  AND value CONTAINS 'Transaction ID:' AND value CONTAINS 'Session ID:' AND visible == 1";
        public static final String lblPwdAlert = "label ==\"Password must be a minimum of 6 characters and cannot contain 6 repeating characters.\"";
        public static final String dropDownOptionsList_chain = "**/XCUIElementTypeScrollView[`visible==1`]/XCUIElementTypeOther/XCUIElementTypeStaticText";
        public static final String funding_chain = "**/XCUIElementTypeStaticText[`label BEGINSWITH \"Funding\"`][1]";
        public static final String signIn_AccId = "guest_view_button_signin";
    }

}
