# MoviesAndFriends


Sync Layer:

Main responsibilities are divided among:

1.  MoviesProvider with the implementation details and knowledge of the MovieDB API.
2. Data is stored in an instance of LocalDataStore and shared across the app.
3. IntentService fetches movies request in the background. 

UI elements interact with the IntentService for requesting new pages from the MovieDB server and does not directly call the MoviesProvider methods. Once data is available, any UI class that needs access directly gets it from the singleton LocalDataStore object.

[LocalDataStore] <~> [ClientHttpConnection] <—> [MoviesProvider] <—> [BackgroundService]  …. { UI Elements } <~> [LocalDataStore] 
                                                                           |

MoviesProvider class also managers which page to request next and when to ignore the request because all pages are downloaded.

LocalDataStore groups all the movies data and meta data related to the pages such as already downloaded page and total number of pages. Data structures used in it are thread safe.  However, since MoviesProvider could be invoked for a page request in any order, it is up to the user of the class to ensure that no page is requested twice.
This serialization is automatically provided by the user of the IntentService, which process requests one at a time. 

UI Layer

1. MainActivity contains ThumbnailsGridFragment fragment.
2. ThumbnailsGridFragment contains the logic to display thumbnails for each movie in a Grid. It requests new pages before the end of the scroll is reached by dispatching intent service background request. The GridView adapter in this fragment uses Picasso library to fetch an image.
3. When a column is clicked DetailActivity is launched. It is passed the index and selected filter (sort)
4. It then extract the corresponding movie information from the singleton LocalDataStore object and fetches the backdrop image using Picasso API.
5. SplashScreenActivity dispatches pre-fetch request and loads MainActivity after a timeout. I will replace that with wait until the callback from BackgroundService returns.

Error Handling:

Errors are packaged into SyncException if it's related to network connectivity and displayed to the user in a short toast.

UnitTests

Uses JUnit mock tests for basic testing. Mocks HttpClientConnection and App Context.

MovieDB Key

1. Create a file in the project:
app/cred.properties 
2. Add the following
moviedb_key=Your-key

Where Your-Key is your MovieDB key without quotes.
