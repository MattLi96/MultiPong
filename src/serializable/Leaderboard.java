package serializable;

import serializable.Score;

public class Leaderboard extends Score{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7683903530289737107L;	
	
	public Leaderboard(int numPlayers, Polygon polygon) {
		super(polygon);
		// TODO Auto-generated constructor stub
	}
	
	/////////////////////////////////////////////////////////////////////////////////////
	// Ignore! -Neil                                                                   //
	/////////////////////////////////////////////////////////////////////////////////////
	
	/*public static void main(String[] args) throws AuthenticationException, MalformedURLException, IOException, ServiceException, OAuthException {

		Boolean USE_RSA_SIGNING = false;
		String CLIENT_ID = "523743008688.apps.googleusercontent.com";
		String CLIENT_SECRET = "DyrdTwZ1JLmf486av1k_MC6n";
		String SCOPE = "https://spreadsheets.google.com/feeds";
		
		GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
		oauthParameters.setOAuthConsumerKey(CLIENT_ID);
	
		OAuthSigner signer;
	    if (USE_RSA_SIGNING) {
	      signer = new OAuthRsaSha1Signer(CLIENT_SECRET);
	    } else {
	      oauthParameters.setOAuthConsumerSecret(CLIENT_SECRET);
	      signer = new OAuthHmacSha1Signer();
	    }
		GoogleOAuthHelper oauthHelper = new GoogleOAuthHelper(signer);
		
		oauthParameters.setScope(SCOPE);
	    
	    SpreadsheetService service = new SpreadsheetService("Leaderboards");
	    URL LEADERBOARDS_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/public/basic");

	    // Make a request to the API and get all spreadsheets.
	    SpreadsheetFeed feed = service.getFeed(LEADERBOARDS_FEED_URL, SpreadsheetFeed.class);
	    List<SpreadsheetEntry> spreadsheets = feed.getEntries();

	    // Iterate through all of the spreadsheets returned
	    for (SpreadsheetEntry spreadsheet : spreadsheets) {
	      // Print the title of this spreadsheet to the screen
	      System.out.println(spreadsheet.getTitle().getPlainText());
        
        
     }
     
}*/
	
}