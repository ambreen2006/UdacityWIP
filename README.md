# MoviesAndFriends

sync package

MoviesProvider: 
- Abstracts the logic to connect and interpret MovieDB API's.
- Any class that needs to fetch new content from the MovieDB server should use this class.
- It implements IMoviesProvider interface. If a new movie source is created, it should implement that interface and house API and connection logic.
- Uses ClientHttpConnection for http request to the movie DB server.
- Uses LocalDataStore to populate movies list and update page related meta data.
- It discard duplicate request for same page or page number request that exceeds the available pages for the given filter (sort)
- If the request fails for some reason, it throws SyncException with the appropriate information.
- If network connection is not available, requests are not dispatched.
- FUTURE_IMPROVEMENT: Currently it just logs any exception or lack of network connectivity. I will display useful information to the user in the next iteration.

ClientHttpConnection:
- Class abstracting the logic for http communication. Currently only implements Get request. It is reusable by any project.
- Implements IClientHttpConnection

LocalDataStore
- Abstracts logic for movies information structure.
- Implements IDataStore interface. Any other class which needs to store specific data structures should implement this interface. 
- Inner class MoviesList contains data for the latest page request and the list of all movies up to the last page fetched.

DataStoreFactory
- Creates a singleton LocalDataStore

MovieData
- UDT specifying fields returned the MovieDB api for a movie.

BackgroundService
- Subclass of IntentService.
- It bridges the UI and the MoviesProvider class. 
- It dispatches fetch requests to MoviesProvider which in turn uses ClientHttpConnection to retrieve data, and store it in LocalDataStore.
- Once the task finishes, it broadcast back the status of that request.

SyncException
- Custom exception which relates to syncing with the server.

Constants
- All non-ui constants used in the project.

ThumbnailsGridFragment
- Contains the grid view and display poster images for the movies result.
- Images are populated using Picasso lib.
- Uses BackgroundService class to request list of movies.
- Uses LocalDataStore to directly obtain movie info for each movie index associated with the selected filter
- Responds to user selection of sort/filter by dispatching content request.
- Responds to user selection of movie by starting the DetailActivity activity.
- Sets up the menu for changing the sort selection.

DetailActivity
- Displays detailed meta-data for the movie selected by the user.
- Fetches movie image using Picasso lib.

SplashScreenActivity
- Shows attribution to the moviedb org.
- FUTURE_IMPROVEMENT: Make attribution and app info available to the user through menu.

MoviesUnitTest (non-android tests)
- Uses JUnit mock tests for basic testing. Mocks HttpClientConnection and App Context.

MovieDB Key:
1. Create a file in the project:
 app/cred.properties

2. Add the following
moviedb_key=Your-key

Where Your-Key is your MovieDB key without quotes.