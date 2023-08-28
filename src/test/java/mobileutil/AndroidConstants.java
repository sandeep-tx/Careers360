package mobileutil;

public class AndroidConstants {
    public class Common {
        public static final String type_xpath = "xpath";
        public static final String type_name = "name";
        public static final String type_id = "id";
        public static final String type_class = "classname";
        public static final String type_uiSelector = "uiSelector";
        public static final String type_accessibilityId = "accessibilityId";
    }

    public static class PayRange {

        public static final String optionAboutPayRange = "new UiSelector().text(\"About PayRange\")";
        public static final String menuOptionTransactions = "new UiSelector().text(\"View Transactions\")";
        public static final String menuOptionMaintenance = "new UiSelector().text(\"Maintenance\")";
        public static final String btnBack = "com.payrange.payrange:id/btn_back";
        public static final String lblAlredyHaveAnAccount = "//android.widget.TextView[@text=\"Already have an account?\"]";
        public static final String linkForgotPassword = "com.payrange.payrange:id/btnForgotPassword";

        public static final String btnBackOfProfilePage = "//android.widget.ImageView[@content-desc=\"Back\"]";
        public static final String lblMyProfile = "//android.widget.TextView[@text=\"My Profile\"]";
        public static final String txtUserName = "com.payrange.payrange:id/text_username";
        public static final String txtPhoneNumber = "com.payrange.payrange:id/text_mobile_number";
        public static final String txtEmailOfProfilePage = "com.payrange.payrange:id/login_username";
        public static final String logoEmail = "com.payrange.payrange:id/login_icon";
        public static final String btnSaveOfProfilePage = "com.payrange.payrange:id/profile_btn_save";
        public static final String btnSaveOfAddFunds_UI = "new UiSelector().text(\"Save\")";
        public static final String btnChangePassword = "com.payrange.payrange:id/profile_btn_change_password";
        public static final String btnDeactivate = "com.payrange.payrange:id/deactivate";
        public static final String settingsOption = "//android.widget.TextView[@text='Settings']";
        public static final String settingsLessThanIcon = "//android.widget.TextView[@text='Settings']/following-sibling::android.widget.ImageView";
        public static final String contactUsOption = "//android.widget.TextView[@text='Contact Us']";
        public static final String contactUsLessThanIcon = "//android.widget.TextView[@text='Contact Us']/following-sibling::android.widget.ImageView";
        public static final String helpOption = "//android.widget.TextView[@text='Help']";
        public static final String helpLessThanIcon = "//android.widget.TextView[@text='Help']/following-sibling::android.widget.ImageView";
        public static final String lblLoginPopUpHeader = "com.payrange.payrange:id/sheet_title";
        public static final String btnFacebookLogin = "com.payrange.payrange:id/btn_facebook";
        public static final String btnGoogleLogin = "com.payrange.payrange:id/btn_google";
        public static final String btnAppleLogin = "com.payrange.payrange:id/btn_apple";
        public static final String lblDontHaveAnAccount = "//android.widget.TextView[@text=\"Don't have an account?\"]";
        public static final String lblBySigningIn = "com.payrange.payrange:id/toc_text";
        public static final String linkTermsAndConditions = "com.payrange.payrange:id/toc_link";
        public static final String linkClickHere = "com.payrange.payrange:id/btn_switch_mode";
        public static final String btnPopUpClose = "com.payrange.payrange:id/plus_and_close_icon";
        public static final String btnLoginWithPhone = "com.payrange.payrange:id/btn_login_with_phone";

        public static final String headingDeletePopUp = "new UiSelector().text(\"Are you sure want to delete the Payment Method?\")";
        public static final String headingUserPreferences = "new UiSelector().text(\"User Preferences\")";
        public static final String headingMore = "new UiSelector().text(\"More\")";
        public static final String lblDefaultCurrency = "new UiSelector().textContains(\"Dollars\")";
        public static final String lblManageBluetooth = "new UiSelector().textContains(\"Bluetooth\")";
        public static final String lblAlternateConnectionMethod = "new UiSelector().text(\"Alternate Connection Method\")";
        public static final String lblUserImprovementProgram = "new UiSelector().text(\"User Improvement Program\")";
        public static final String btnNextWebView = "//*[text()=\"NEXT\"]";

        public static final String transactionsData = "(//*[contains(@text,\"Funding\")]//parent::android.widget.LinearLayout//parent::android.widget.LinearLayout)//parent::android.widget.LinearLayout[1]//child::android.widget.TextView";

        public static final String txtMachineName = "new UiSelector().text(\"$MachineName\")";
        public static final String optionSelectionItems = "com.payrange.payrange:id/tl_center_text";
        public static final String labelA = "key_0";
        public static final String label1 = "key_1";
        public static final String btnSubmitOfVendingOption = "submitBtn";

        public static final String latestTransactionHeader = "(//*[@resource-id=\"com.payrange.payrange:id/header1_details\"])[1]";
        public static final String latestTransactionAmount = "(//*[@resource-id=\"com.payrange.payrange:id/transaction_amount\"])[1]";

        public static final String popUpHeaderTransaction = "new UiSelector().text(\"Transaction\")";
        public static final String findFundingTransaction = "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().textContains(\"Funding\").instance(2))";
        public static final String labelDescriptionOfPopUp = "new UiSelector().text(\"Description\")";
        public static final String labelAmountOfPopUp = "new UiSelector().text(\"Amount\")";
        public static final String labelTransactionDetails_id = "com.payrange.payrange:id/transaction_details";
        public static final String transactionRow_UI = "new UiSelector().resourceId(\"com.payrange.payrange:id/transaction_row\").instance(0)";
        public static final String labelPricePopUp = "new UiSelector().textContains(\"$\")";
        public static final String btnOkOfPopUp = "new UiSelector().text(\"OK\")";
        public static final String btnReportAProblemOfPopUp = "new UiSelector().text(\"REPORT A PROBLEM\")";
        public static final String dropDownCreditOptions = "com.payrange.payrange:id/selected_credit_textview";
        public static final String creditOptionsItemList = "new UiSelector().resourceId(\"com.payrange.payrange:id/list_item_text_view\")";
        public static final String cardFrontLayout = "com.payrange.payrange:id/info_message_view";
        //Maintenance Page
        public static final String btnRefreshForMaintenancePage = "Refresh";
        public static final String tabCollectionForMaintenancePage = "Collection";
        public static final String tabFirmwareForMaintenancePage = "Firmware";
        public static final String tabDiagnosticsForMaintenancePage = "Diagnostics";
        public static final String nearByDevicesListForMaintenancePage = "new UiSelector().resourceId(\"com.payrange.payrange:id/machine_details\")";
        public static final String lblNearByDevices = "com.payrange.payrange:id/nearby_devices";
        public static final String lblSelectedForMaintenanceScreen = "com.payrange.payrange:id/selected_devices";
        public static final String btnUploadTransactionsOfMaintenanceScreen = "com.payrange.payrange:id/start_collection_btn";
        public static final String btnCancelTransactionsOfMaintenanceScreen = "com.payrange.payrange:id/cancel_collection_btn";

        public static final String imgDeviceImageOfMaintenanceScreen = "new UiSelector().resourceId(\"com.payrange.payrange:id/machine_image\")";
        public static final String tileMachineDetails = "new UiSelector().resourceId(\"com.payrange.payrange:id/machine_details\")";
        public static final String checkBoxOfMaintenanceScreen = "new UiSelector().resourceId(\"com.payrange.payrange:id/check_box\")";
        public static final String btnCancelPayment = "com.payrange.payrange:id/slider_processing";
        public static final String lblCancellingPayment = "new UiSelector().textContains(\"Cancelling\")";
        public static final String lblPleaseFundYourAccount = "new UiSelector().text(\"Please fund your account.\")";
        public static final String popUpPleaseFundAccount = "new UiSelector().text(\"You do not have enough balance to make a transaction.\")";
        public static final String checkBoxOfCollectionsTab = "new UiSelector().resourceId(\"com.payrange.payrange:id/check_box\")";
        public static final String statusColourIndicatorForMaintenanceTab = "new UiSelector().resourceId(\"com.payrange.payrange:id/status\")";
        public static final String btnRetryMaintenanceScreen = "com.payrange.payrange:id/retry_collection_btn";
        public static final String btnDoneMaintenanceScreen = "com.payrange.payrange:id/done_collection_btn";
        public static final String lableSuccessfullDevicesMaintenanceScreen = "new UiSelector().textContains(\"Successful\")";
        public static final String labelStatusMessage = "//android.widget.TextView[@text=\"Needs Attention\"]|//android.widget.TextView[@text=\"Good\"]";
        public static final String lblSelectMachineName = "new UiSelector().text(\"Select a machine\")";
        public static final String iconSpinnerArrow = "com.payrange.payrange:id/spinner_arrow";
        public static final String btnOkayOfTransactionPopUp = "com.payrange.payrange:id/positive_button";
        public static final String lblAvailableMachinesCount = "com.payrange.payrange:id/section_items_count";
        public static final String iconProfile = "new UiSelector().resourceId(\"com.payrange.payrange:id/profile_layout\").childSelector(new UiSelector().className(\"android.widget.ImageView\"))";
        public static final String txtCurrentPassword = "com.payrange.payrange:id/current_password";
        public static final String txtNewPassword = "com.payrange.payrange:id/new_password";
        public static final String txtConfirmPassword = "com.payrange.payrange:id/confirm_password";
        public static final String btnSaveOfChangePwdScreen = "com.payrange.payrange:id/save";

        public static final String listViewoFDevices = "com.payrange.payrange:id/laundry_list_view";
        public static final String lblMachineTimer = "com.payrange.payrange:id/running_timer";
        public static final String lblStartedByMe = "com.payrange.payrange:id/section_title";
        public static final String lblStartedByMeCount = "com.payrange.payrange:id/section_items_count";
        public static final String lblRunningTimer_UI = "new UiSelector().resourceId(\\\"com.payrange.payrange:id/running_timer\\\"";
        public static final String helpScreenContent = "HelpCenterTell us about your issue so we can help you more quicklyConsumer SupportGetting StartedFunding Sources";
        //        public static final String helpScreenContent = "What is PayRange?You can use your PayRange App to make transactions on range of machines such as vending machines, amusement machines, laundry washers and dryers etc.How does PayRange work?Step 1: Launch the PayRange App . Swipe left or right to find the machine you would like to pay at.Step 2: Swipe up the machine card and the payment will be sent to the machine.Step 3: You will see the confirmation on your screen once the payment is completed.Can I view my past PayRange transactions?Yes. You can see the history of your transactions by going into \"View Transactions\" from main menu.How do I know if PayRange is accepted by a machine?Once you are in-front of the machine and open your app, you will be able to locate the specific machine by swiping left of right. If you don't see it, you will not be able to use PayRange on that specific machine.";
        public static final String aboutScreenContent = "PayRange is focused on eliminating payment as the barrier. Doing transactions on machines such as parking meters, vending machines, laundry washers and dryers using bills and coins has traditionally been a painful experience. We want to solve this user need and provide a seamless experiences while simultaneously addressing operator's constraints. First it must work for the operator, or there will be little deployment. Second, it must be simple or there won’t be many using it.Our exceptionally talented team is working to break the payment barrier by bringing cost-effective, easy-to-use mobile payments to the other 95% of machines that include vending, parking meters, luggage carts, transit ticketing, laundromats, amusement, and more.Please visit us at http://www.payrange.com to learn more.";
        public static final String helpScreenText_class = "android.widget.TextView";
        public static final String aboutPageText_class = "android.widget.TextView";
        public static final String logoAboutScreen_UI = "new UiSelector().resourceId(\"android:id/content\").childSelector(new UiSelector().className(\"android.widget.ImageView\"))";
        public static final String reportTransactionSubject = "Problem with transaction";
        public static final String txtEmailRecipientOfEmailScreen_UI = "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().descriptionContains(\"Recipients:\"))";

        public static final String noMachineHeader_id = "com.payrange.payrange:id/noMachinesHeader";
        public static final String btnScanAgain_id = "com.payrange.payrange:id/scanAgain";
        public static final String btnOkay_id = "com.payrange.payrange:id/dialog_ok";

        public static final String lblEmailNotRegistered_UI = "new UiSelector().text(\"This email is not registered. Please try another or create new account.\")";

        public static final String lblInvalidEmail_UI = "new UiSelector().text(\"Please enter a valid email address.\")";

        public static final String lblInvalidPwd_UI = "new UiSelector().text(\"Password must be a minimum of 6 characters and cannot contain 6 repeating characters.\")";
        public static final String lblInvalidLogin_UI = "new UiSelector().text(\"You have entered invalid login credentials. Please try again.\")";
        public static final String lblInvalidLoginEmail_UI = "new UiSelector().text(\"Please enter a valid email address.\")";
        public static final String lblInternetErrorMsg_UI = "new UiSelector().text(\"Server unreachable at this time. Please try again.\")";
        public static final String popUpHeader_UI = "new UiSelector().text(\"Create an Account\")";


        public static final String tabMarketPlace_UI = "new UiSelector().text(\"MARKETPLACE\")";
        public static final String optionPayRangeCredit_UI = "new UiSelector().resourceId(\"IS_OFFERWALL\")";
        public static final String optionRedbox_UI = "new UiSelector().resourceId(\"REDBOX\")";

        public static final String bkDevicesList_ID = "com.payrange.payrange:id/machine_name";

        public static final String lblMachineName_ID = "com.payrange.payrange:id/tvMachineName";
        public static final String imgBkDeviceLayout_ID = "com.payrange.payrange:id/machine_image";
        public static final String lblFirstPurchaseFree_UI = "new UiSelector().text(\"First Purchase Free.\")";
        public static final String lbl15Seconds_UI = "new UiSelector().text(\"15 seconds to your first purchase.\")";
        public static final String btnSignUpWithGoogle = "new UiSelector().text(\"Sign Up with Google\")";
        public static final String btnSignUpWithEmailInstead = "new UiSelector().text(\"Sign Up using Email instead\")";

        public static final String lblBySigningUp_ID = "com.payrange.payrange:id/module_toc";

        public static final String menuOptionCurrency_UI = "new UiSelector().textContains(\"Dollars\")";
        public static final String menuOptionSelectCurrency_UI = "new UiSelector().text(\"Select Currency\")";
        public static final String optionUSDollars_UI = "new UiSelector().text(\"US Dollars\") ";
        public static final String optionCanadianDollars_UI = "new UiSelector().text(\"Canadian Dollars\")";
        public static final String OneLable_ui = "new UiSelector().text(\"1\")";
        public static final String aLable_ui = "new UiSelector().text(\"A\")";
    }

}
