// Initialize all the prototype libraries
var common	   = new Common() ;
var statistics = new Statistics(common) ;
var dashboard  = new Dashboard(common, statistics) ;

// Initialize the exercise
var remoteConnection  = new RemoteConnection(dashboard) ;



/**
 * Start the client with the transport type
 * @param transportType with the instrument name
 */
function startClient(transportType)
{
	// Remove all the current prices if exists
	common.getJQueryRef("prices").html('') ;
	
	// Start the remote connection
	remoteConnection.start(transportType) ;

	// Reset statistics
	statistics.resetStatistics() ;
	
	// Start a new interval for the instrument name
	var intervalId = setInterval(function()
								 {
									statistics.updateStatistics();
								 }, 1000) ;
	
	dashboard.setIntervalId(intervalId) ;
}

/**
 * Stop the client by the instrument name
 */
function stopClient()
{
	// Remove all the current prices if exists
	common.getJQueryRef("prices").html('') ;
	
	// Secondly, stop the remote connection
	remoteConnection.stop() ;
}