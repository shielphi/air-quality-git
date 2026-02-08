This Java file collects JSON data from your local air quality station with this line:

https://api.waqi.info/feed/geo: -00.00000;000.0000/?token=yourapiKey.

and puts the icon on https://aprs.fi/

You need the coordinates of your local air quality station and an api key from waqi to access your stream. For starters, look for your local station, this is a pretty public site here:
https://waqi.info/#/c/5.149/7.297/2.6z

What you want is to read about the API, your key and how to get coordinates here: https://docs.openaq.org/using-the-api/quick-start
Once you have your key and coordinates enter them into a browser with the https line 3 above.
