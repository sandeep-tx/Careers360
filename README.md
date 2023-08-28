Careers 360
=====================

This is a single framework in which we are executing Web, API and Mobile testcases.

# You can use the below commands in order to execute TCs on different devices

<h4>For Android: mvn clean verify "-Dcucumber.options=--tags @SmokeProdIOS" -Ddevice_detail="b2323_13 "
-Dscreen_shot=true -Dbatch_type=smoke -Denvironment=wash </h4>
<p>--tags signifies which TC you want to execute</p>
<p>device_detail tells about the device name followed by _ which is the Android version</p>
<p>example: b2323_13 b2323 is the device name and 13 is the android version</p>
<p>screen_shot possible values true/false. It will tell the code to take screenshot or not</p>
<p>batch_type is a cosmetic field to show value in report's dashboard.</p>
<p>environment possible values: prod, wash</p>

<h4>For IOS: mvn clean verify "-Dcucumber.options=--tags @SmokeProdIOS" -Dudid=4298b44ada9ec5c49cb3e7d0c7967cf32ab19251
-Dscreen_shot=true -Dbatch_type=smoke -Denvironment=wash
</h4>
<p>--tags signifies which TC you want to execute</p>
<p>udid tells the IOS device name </p>
<p>screen_shot possible values true/false. It will tell the code to take screenshot or not</p>
<p>batch_type is a cosmetic field to show value in report's dashboard.</p>
<p>environment possible values: prod, wash</p>
