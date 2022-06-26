# KrakenClient

I have created a spring boot application which starts by running main class KrakenClientApplication.class file. Everything is embedded into it, Using a application.properties to load security header key and few other things into application. Application run the requirements, process the site outages data and then shut down automatically.

It is Maven project, should self check out all the libraries and work out of the box. Logging on the console so guide what is happening on the applications

The main application after initializing the spring container should kick start the post construct method on KrakenSiteOutageHandlerServic class file which has the site id argument hardcoded in it.

 # Few Of Many Important Loggings Statements
  1. Kick Starting the site outage reporting process...
  2. Starting to fetch all Outages ..
  3. Site outage data for id norwich-pear-tree successfully sent.
  4. Site outage reporting for id: norwich-pear-tree successfully completed...

Unit tests are added has necessarily 

Sneek peak   - 	

@PostConstruct
	private void kickStartSiteOutageProcess() {
		processSiteOutages("norwich-pear-tree");
	}
