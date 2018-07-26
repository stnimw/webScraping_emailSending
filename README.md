# webScraping_emailSending
This is a simple demo as follows.

1. Web scraping with HtmlUnit.
2. Sending email with org.apache.commons.mail.
3. Extracting data with Jsoup.

## Tasks

The main task is sending a notification via email when user's appointment time is close.

1. Get the service progress from a hospital website (Cathay General Hospital).
2. Detect if the current number is close to user's number.
   For example, if user's number is 5, send notification when current number is 4.
3. Send email.

## Requirements

 This demo is build with with Maven 3.5.4 and Java 1.7.

## How To Start

1. Import as a maven project.
2. Edit email setting in "mail.properties".
3. Customize function input and run Test.