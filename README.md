This Java file collects JSON data from your local air quality station with this line:

https://api.waqi.info/feed/geo: -00.00000;000.0000/?token=yourapiKey.  

I'm in the Southern hemisphere hence the '-' 

You need the coordinates of your local air quality station and an api key from waqi to access your stream. For starters, look for your local station on this pretty interactive map, here:
https://waqi.info/#/c/5.149/7.297/2.6z

What you want is to read about the API, your key and how to get coordinates here: https://docs.openaq.org/using-the-api/quick-start
Once you have your key and coordinates enter them into a browser with the https line 2 above.

The program puts an icon and data on https://aprs.fi/

WAQI use decimal coordinates and APRS use aprs style coordinates both coordinates have to be entered as variables in the code. APRS beacons need a new number for each beacon, hence the programe writes to a Telemnumber txt file. 

