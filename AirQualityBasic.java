package com.ic.executable;





	import java.io.BufferedReader;
	import java.io.BufferedWriter;
	import java.io.File;
	import java.io.FileReader;
	import java.io.FileWriter;
	import java.io.IOException;
	import java.io.InputStreamReader;
	import java.io.OutputStreamWriter;
	import java.net.HttpURLConnection;
	import java.net.Socket;
	import java.net.URL;
	import java.util.List;
	import java.util.Timer;
	import java.util.TimerTask;

	import com.google.gson.Gson;
	import com.google.gson.GsonBuilder;

	public class AirQualityBasic {



		static String callSign="your call"; //your amateur radio callsign
		static int passCode=00000;		//your aprs password

		static String apiKey="your api key";//your api key for World Air Quality Index (WAQI) feed for your location

		static String serverName="some aprs server name"; //your aprs server eg aunz.aprs2.net
		static int port=14580;

		static String coordinatesOfIcon="0000.00S\\00000.00E"; //where the icon is to appear on aprs.fi map APRS coordinates

		static String iconName="AirQ"; //the icon name on aprs

		static String fileTelem = new File("TelemNumber").getAbsolutePath();//incremental aprs telemetry beacon text file must start with a number cannot be blank.


		//raw data from JSON

		static String icon="E"; //sun behind clouds
		static int i;//aprs telemetry increment number
		static int aqiNumber;
		static String aqiHuman;
		static String airQuality;
		 static List<Double> coordinates;
		 static String rating;
		 static String p10_rating;
		 static String p25_rating;
		 static String ozone_rating;
		 static String no2_rating;
		 static String time;

		static String name;
		static String dew;
		static String h;
		static String No2;
		static String O3;
		static String w;
		static String pm10;
		static String pm25;
		static String t;

//a timer
		public static void main(String[] args) throws IOException {
			Timer thirtyMin = new Timer(); // main timer
			ThirtyMinScheduledTask thirtyMinTimer = new ThirtyMinScheduledTask(); // Instantiate SheduledTask class
			thirtyMin.schedule(thirtyMinTimer, 0, 1800*1000); // run the program every thirty mins 1800000 milli secs 1800/60= 30 mins


		}

	public static void JsonAirQuality() throws IOException{


		//constructors
			Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
			Aqi aqi = new Aqi();

		    Data data = new Data();



		//fill variables and make datasets to match the original json structure


		    aqi.setData(data);


		   URL URL = new URL("https://api.waqi.info/feed/geo: -00.00000;000.0000/?token="+apiKey); //to use this get the air quality feed coordinates for your local air quality station

		   HttpURLConnection conn = (HttpURLConnection) URL.openConnection();
	        conn.setRequestMethod("GET");

		    InputStreamReader reader = new InputStreamReader(conn.getInputStream()); //read JSON stream
		    Aqi aqiObject = gson.fromJson(reader, Aqi.class);
		    	reader.close();

				aqiNumber=Integer.parseInt(aqiObject.data.getAqi());
		        aqiHuman=ReadableAqi(Integer.parseInt(aqiObject.data.getAqi()));


		       name= aqiObject.data.city.getName();
		       airQuality= aqiObject.data.getAqi();
		       time= aqiObject.data.time.getS();
		        coordinates= aqiObject.data.city.getGeo();

		        rating= ReadableAqi(Integer.parseInt(aqiObject.data.getAqi()));
		        p10_rating=P10_scale(Integer.parseInt(aqiObject.data.iaqi.pm10.getV()));
		        p25_rating=P25_scale(Integer.parseInt(aqiObject.data.iaqi.pm25.getV()));
		        ozone_rating=Ozone_scale(Float.parseFloat(aqiObject.data.iaqi.o3.getV()));
		        no2_rating=No2_scale(Float.parseFloat(aqiObject.data.iaqi.no2.getV()));

		        if (aqiObject.data.iaqi.dew.getV()!= "null") {
		        	dew=aqiObject.data.iaqi.dew.getV();
		 	        } else dew="0";

		        //humidity
		        if (aqiObject.data.iaqi.h.getV()!= "null") {
		        h=aqiObject.data.iaqi.h.getV();
		        } else h="0";

		        //nitrous oxide
		        if (aqiObject.data.iaqi.no2.getV()!= "null") {
		        No2=aqiObject.data.iaqi.no2.getV();
		        } else No2="0";

		        //ozone
		        if (aqiObject.data.iaqi.o3.getV()!= "null") {
		        O3=aqiObject.data.iaqi.o3.getV();
		        } else O3="0";

		        //wind velocity
		        if (aqiObject.data.iaqi.w.getV()!= "null") {
		        w=aqiObject.data.iaqi.w.getV();
		        } else w="0";

		        //particle size 10 uM
		        if (aqiObject.data.iaqi.pm10.getV()!= "null") {
		        pm10=aqiObject.data.iaqi.pm10.getV();
		        } else pm10="0";

		        //particle size 2.5 uM
		        if (aqiObject.data.iaqi.pm25.getV()!= "null") {
		        pm25=aqiObject.data.iaqi.pm25.getV();
		        } else pm25="0";

		        //temp
		        if (aqiObject.data.iaqi.t.getV()!= "null") {
		        t=aqiObject.data.iaqi.t.getV();
		        } else t="0";




		        System.out.println("Air quality= "+aqiObject.data.getAqi());
			       System.out.println("Name= "+aqiObject.data.city.getName());
			       System.out.println("Coordinates= "+aqiObject.data.city.getGeo());
			       System.out.println("Rating= "+ReadableAqi(Integer.parseInt(aqiObject.data.getAqi())));
			       System.out.println("Time= "+aqiObject.data.time.getS());

	}

// the JSON results human readable scales

	public static String ReadableAqi(int aqiRating) {
		String rating="";

		if (aqiRating >=0 && aqiRating <=50) {
			rating="Good";

		}

		if (aqiRating >=51 && aqiRating <=100) {
			rating="Moderate";

		}

		if (aqiRating >=101 && aqiRating <=150) {
			rating="Unhealthy for sensitive people";

		}

		if (aqiRating >=151 && aqiRating <=200) {
			rating="Unhealthy";

		}

		if (aqiRating >=201 && aqiRating <=300) {
			rating="Very Unhealthy";

		}

		if (aqiRating >=301 && aqiRating <=500) {
			rating="Hazardous";

		}

			return rating;
	}


	public static String P10_scale(int p10Rating) {
		String rating="";

		if (p10Rating >=0 && p10Rating <=54) {
			rating="<font color='GREEN'>Good</font>";

		}

		if (p10Rating >=55 && p10Rating <=154) {
			rating="<font color='GREEN'>Moderate</font>";

		}

		if (p10Rating >=155 && p10Rating <=254) {
			rating="<font color='Yellow'>Unhealthy for sensitive people</font>";

		}

		if (p10Rating >=255 && p10Rating <=354) {
			rating="<font color='Red'>Unhealthy</font>";

		}

		if (p10Rating >=355 && p10Rating <=424) {
			rating="<font color='Purple'>Very Unhealthy</font>";

		}



			return rating;
	}


	public static String P25_scale(int p25Rating) {
		String rating="";

		if (p25Rating >=0 && p25Rating <=15) {
			rating="<font color='GREEN'>Good</font>";

		}

		if (p25Rating >=16 && p25Rating <=40) {
			rating="<font color='GREEN'>Moderate</font>";

		}

		if (p25Rating >=41 && p25Rating <=65) {
			rating="<font color='Yellow'>Unhealthy for sensitive people</font>";

		}

		if (p25Rating >=66 && p25Rating <=150) {
			rating="<font color='Red'>Unhealthy</font>";

		}

		if (p25Rating >=151 && p25Rating <=250) {
			rating="<font color='Purple'>Very Unhealthy</font>";

		}



			return rating;
	}


	public static String Ozone_scale(float ozRating) {
		String rating="";

		if (ozRating >=0 && ozRating <=50.9) {
			rating="<font color='GREEN'>Good</font>";

		}

		if (ozRating >=51 && ozRating <=100.9) {
			rating="<font color='Yellow'>Moderate</font>";

		}

		if (ozRating >=101 && ozRating <=160.9) {
			rating="<font color='Orange'>Unhealthy for sensitive people</font>";

		}

		if (ozRating >=161 && ozRating <=200.9) {
			rating="<font color='Red'>Unhealthy</font>";

		}

		if (ozRating >=201 && ozRating <=300.9) {
			rating="<font color='Purple'>Very Unhealthy</font>";

		}

		if (ozRating >=301 && ozRating <=500) {
			rating="<font color='Brown'>Hazardous</font>";

		}



		return rating;
	}

	public static String No2_scale(float no2Rating) {
		String rating="";

		if (no2Rating >=0 && no2Rating <=4.9) {
			rating="<font color='GREEN'>Good</font>";

		}

		if (no2Rating >=5 && no2Rating <=19.9) {
			rating="<font color='GREEN'>Moderate</font>";

		}

		if (no2Rating >=20 && no2Rating <=49.9) {
			rating="<font color='Yellow'>Unhealthy for sensitive people</font>";

		}

		if (no2Rating >=50 && no2Rating <=99.9) {
			rating="<font color='Red'>Unhealthy</font>";

		}

		if (no2Rating >=100 && no2Rating <=200) {
			rating="<font color='Purple'>Very Unhealthy</font>";

		}



			return rating;
	}

//aprs beacons

	public static void AprsBeacons(){
		try
		      {
			//APRS logon string
			String logOn="user "+ callSign+" pass "+passCode+" ver \"manual login\"";
			//object beacon
			String object=iconName+">APRS,TCPIP*:!"+coordinatesOfIcon+icon+"Air quality ug/m3= "+aqiNumber+" which is "+aqiHuman+" ";

			//telemetry beacon with beacon number
			String telem=iconName+">APRS,TCPIP*:T#"+TelemetryBeaconNumberReader()+","+aqiNumber+","+w+","+pm10+","+pm25+","+O3+"";
			String telemLabels=iconName+" APDW17,TCPIP* :PARM.Air_Qa,Wind_v,Par10,Pa2.5,Ozon";
			String telemUnits=iconName+" APDW17,TCPIP* :UNIT.uG/M3,km/hr,uG/m3,uG/m3,ppm";
			//logon to the APRS server
		System.out.println("Connecting to: " + serverName + " on port: " + port);
		 Socket client = new Socket(serverName, port);
		  System.out.println("Just connected to: "+ client.getRemoteSocketAddress());
		    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));

		        out.newLine();
			out.flush();
		 System.out.println("Server says: " + in.readLine());
		      out.write(logOn);
			out.newLine();
			out.flush();
		System.out.println("I says: " + logOn);
			   System.out.println("Server says: " + in.readLine());


		out.write(object);
		out.newLine();
		out.flush();
		System.out.println("I says: " + object);
		System.out.println("Server says: " + in.readLine());


		out.write(telem);
		out.newLine();
		out.flush();
		System.out.println("I says: " + telem);
		System.out.println("Server says: " + in.readLine());

		out.write(telemLabels);
		out.newLine();
		out.flush();
		System.out.println("I says: " + telemLabels);
		System.out.println("Server says: " + in.readLine());

		out.write(telemUnits);
		out.newLine();
		out.flush();
		System.out.println("I says: " + telemUnits);
		System.out.println("Server says: " + in.readLine());
		client.close();

		      }catch(IOException e)
		      {
		         e.printStackTrace();
		      }
		TelemetryBeaconNumberWriter(); //write a new telemetry beacon number
		      }


	//aprs needs a special beacon number

	public static int TelemetryBeaconNumberReader(){
		   //the file needs a 1 to start off
		try{
		FileReader fileReader = new FileReader(fileTelem);
		 BufferedReader bufferedReader = new BufferedReader(fileReader);

		    i = Integer.parseInt(bufferedReader.readLine());
		//System.out.println("Reader number= "+i);

		bufferedReader.close();

		    } catch (IOException e) {e.printStackTrace();}
		return i;

		}


	@SuppressWarnings("deprecation")
	public static void TelemetryBeaconNumberWriter(){

		//telemetry frame sequential number writer
		try {

		      FileWriter fileWriter =new FileWriter(fileTelem);
		BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);

		 //telemetry frame numbers reset to 1
		  i=i+1;
		if (i>=255)
		i=1;

		//System.out.println("Writer number= "+i);



		            bufferedWriter.write(new Integer(i).toString());

		            bufferedWriter.newLine();

		           bufferedWriter.close();


		        }
		        catch(IOException ex) {
		            System.out.println(
		                "Error writing to file '"
		                + fileTelem + "'");

		        }
		}


// the JSON feed structure in

	public static class Aqi {
		public String status;
		public Data data;
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public Data getData() {
			return data;
		}
		public void setData(Data data) {
			this.data = data;
		}



	}



	public static class Data {
		public String aqi;
		public String idx;
		public Attributions[] attributions;
		public City city;
		public String dominentpol;
		public Iaqi iaqi;
		public Time time;
		public Debug debug;
		public String getAqi() {
			return aqi;
		}
		public void setAqi(String aqi) {
			this.aqi = aqi;
		}
		public String getIdx() {
			return idx;
		}
		public void setIdx(String idx) {
			this.idx = idx;
		}
		public Attributions[] getAttributions() {
			return attributions;
		}
		public void setAttributions(Attributions[] attributions) {
			this.attributions = attributions;
		}
		public City getCity() {
			return city;
		}
		public void setCity(City city) {
			this.city = city;
		}
		public String getDominentpol() {
			return dominentpol;
		}
		public void setDominentpol(String dominentpol) {
			this.dominentpol = dominentpol;
		}
		public Iaqi getIaqi() {
			return iaqi;
		}
		public void setIaqi(Iaqi iaqi) {
			this.iaqi = iaqi;
		}
		public Time getTime() {
			return time;
		}
		public void setTime(Time time) {
			this.time = time;
		}
		public Debug getDebug() {
			return debug;
		}
		public void setDebug(Debug debug) {
			this.debug = debug;
		}



	}

	public static class Attributions {
		public String url;
		public String name;
		public String logo;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getLogo() {
			return logo;
		}
		public void setLogo(String logo) {
			this.logo = logo;
		}

	}

	public static class City {
		public List<Double> geo;
		public String name;
		public String url;
		public List<Double> getGeo() {
			return geo;
		}
		public void setGeo(List<Double> geo) {
			this.geo = geo;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}



	}

	public static class Geo {

		}


	public static class Debug {
		public String sync;

		public String getSync() {
			return sync;
		}

		public void setSync(String sync) {
			this.sync = sync;
		}
	}

	public static class Iaqi {
		public Dew dew;
		public H h;
		public No2 no2;
		public O3 o3;
		public W w;
		public Pm10 pm10;
		public Pm25 pm25;
		public T t;

		public Dew getDew() {
			return dew;
		}
		public void setDew(Dew dew) {
			this.dew = dew;
		}

		public H getH() {
			return h;
		}
		public void setH(H h) {
			this.h = h;
		}

		public No2 getNo2() {
			return no2;
		}
		public void setNo2(No2 no2) {
			this.no2 = no2;
		}

		public O3 getO3() {
			return o3;
		}
		public void setO3(O3 o3) {
			this.o3 = o3;
		}

		public W getW() {
			return w;
		}
		public void setW(W w) {
			this.w = w;
		}


		public Pm10 getPm10() {
			return pm10;
		}
		public void setPm10(Pm10 pm10) {
			this.pm10 = pm10;
		}
		public Pm25 getPm25() {
			return pm25;
		}
		public void setPm25(Pm25 pm25) {
			this.pm25 = pm25;
		}
		public T getT() {
			return t;
		}
		public void setT(T t) {
			this.t = t;
		}


	}

		public static class Dew {

			public String v;

			public String getV() {
				return v;
			}

			public void setV(String v) {
				this.v = v;
			}
		}

		public static class H {

			public String v;

			public String getV() {
				return v;
			}

			public void setV(String v) {
				this.v = v;
			}
		}

		public static class No2 {

			public String v;

			public String getV() {
				return v;
			}

			public void setV(String v) {
				this.v = v;
			}
		}

		public static class O3 {

			public String v;

			public String getV() {
				return v;
			}

			public void setV(String v) {
				this.v = v;
			}
		}

		public static class W {

			public String v;

			public String getV() {
				return v;
			}

			public void setV(String v) {
				this.v = v;
			}
		}

	public static class Pm10 {

		public String v;

		public String getV() {
			return v;
		}

		public void setV(String v) {
			this.v = v;
		}
	}

	public static class Pm25 {

		public String v;

		public String getV() {
			return v;
		}

		public void setV(String v) {
			this.v = v;
		}
	}
	public static class T {

		public String v;

		public String getV() {
			return v;
		}

		public void setV(String v) {
			this.v = v;
		}
	}


	public static class Time {
		public String s;
		public String tz;
		public String v;
		public String getS() {
			return s;
		}
		public void setS(String s) {
			this.s = s;
		}
		public String getTz() {
			return tz;
		}
		public void setTz(String tz) {
			this.tz = tz;
		}
		public String getV() {
			return v;
		}
		public void setV(String v) {
			this.v = v;
		}

	}


	//fire off the methods in the timer
		public static class ThirtyMinScheduledTask extends TimerTask {

			public void run() {

				try {
					JsonAirQuality();
					AprsBeacons();
				} catch (IOException e) {

					e.printStackTrace();
				}

			}



		}

	}






