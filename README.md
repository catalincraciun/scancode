ScanCode (ICHack '17)
=====================
[![Build Status](https://travis-ci.org/catalincraciun/scancode.svg?branch=master)](https://travis-ci.org/catalincraciun/scancode) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/catalincraciun/scancode/blob/master/LICENSE)

The idea
--------
We wanted to build a platform to bring fancy personalised QR codes to everyone.
The API has two main features:
1. It can generate a QR code holding some data of your choice
2. It can take a raw image (like the image from your camera), scan the QR code and send you back the embedded data
</br>
In order to prove the interaction flow, we have created a sample client iOS app that can request the API for a QR code and then display the information from other scanned QR codes.

How to build and run
----------
For running the API you need to have Maven installed. You can either use an IDE like Intellij or run it using command line:
```
cd scancode_api
mvn spring-boot:run
```

Technologies used
-----------------
The API is mainly based aroung Spring Boot framework. Other than that we have coded our own tools for image processing on raw pixels. Scanning codes in images turned out to be pretty difficult, but not impossible, since the image might be rotated and everything gets distorted. In the end, we managed to get it working unexpectedly accurate.

The prize
---------
ScanCode won 'Most Viable Hack' prize awarded by Bloomberg! :wine_glass:
![alt text](https://github.com/catalincraciun/scancode/blob/master/resources/bloomberg_prize.jpg)
