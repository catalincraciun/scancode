The API
=======

Documentation
-------------
We have tried to document the code for public methods using JavaDoc.

Limitations
-----------
* The API can support only 8.589.934.590 generated codes. To increase that number changes to the code are needed. You can still generate a code for every human on Earth.
* It might not work well in bad light since it uses color detection

Automation Testing
------------------
ScanCode is fully integrated with Travis-CI and current unit tests include:
* API Key security testing (it will make a request with an invalid key to make sure it is not accepted)
* Raw code scanning (it scan the code from a number of raw images and compare the output with the expected one)
* Generated code scanning (it will generate a number of raw images using the API and then it will pass those images to the code scanning method)
