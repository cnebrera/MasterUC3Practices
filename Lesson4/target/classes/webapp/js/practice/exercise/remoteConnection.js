/**
 * RemoteConnection prototype class
 */
var RemoteConnection = (function () 
{
	var instance 		 = {} ;

    /**
     * Public constructor
     */
    function RemoteConnection(dashboard)
    {
    	instance.dashboard = dashboard ;
    	
    	this.socket 	   = atmosphere ;
    	this.isConnected   = false ;
    }
    
    /**
     * Start a new remote connection
     * @param transportType with the transport type
     */
    RemoteConnection.prototype.start = function(transportType)
    {
    	if (this.isConnected)
    	{
    		instance.dashboard.setErrorMessage("The remote connection is already opened. Close before start a new one") ;
    	}
    	else
    	{
		    var request = { url: document.location.toString() + 'prices/' + transportType,
		    				contentType : "application/json",
		    				transport : transportType,
		    				fallbackTransport: 'long-polling'} ;

		    request.onOpen = function(response)
		    {
		    	instance.dashboard.setErrorMessage("Atmosphere connected using " + response.transport) ;
		    };

		    request.onMessage = function (response)
		    {
		    	var instrument = JSON.parse(response.responseBody) ;
				instance.dashboard.addPrice(instrument) ;
		    };

		    request.onClose = function(response)
		    {
		    	instance.dashboard.setErrorMessage("Atmosphere disconnected from " + response.transport) ;
		    };

		    request.onError = function(response)
		    {
		    	instance.dashboard.setErrorMessage("Sorry, but there is some problem with your socket or the server is down " + response.transport) ;
		    };
		    
		    // Subscribe to this topic
		    this.socket.subscribe(request);
		    
		    // Set isConnected to true
		    this.isConnected = true ;
		    
		    // Set the error message as empty
		    instance.dashboard.setErrorMessage("") ;
    	}
    }
    
    /**
     * Stop the current remote connection
     */
    RemoteConnection.prototype.stop = function()
    {
    	if (!this.isConnected)
    	{
    		instance.dashboard.setErrorMessage("The remote connection is already closed. Open before close a new one.") ;
    	}
    	else
    	{
		    // Unsubscribe to this topic
    		this.socket.unsubscribe() ;
    		
    		// Set isConnected to false
    		this.isConnected = false ;
    		
		    // Set the error message as empty
		    instance.dashboard.setErrorMessage("") ;
    	}
    }
    
    return RemoteConnection ;
    
}()) ;