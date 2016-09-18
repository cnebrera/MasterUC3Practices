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
			// TODO 1 - Include here your code to create a new request for a remote connection
			

			// TODO 2.1 - Include here your code to create the listener 'open' on the request
    		
    		
			// TODO 2.2 - Include here your code to create the listener 'onMessage' on the request
    		
    		
			// TODO 2.3 - Include here your code to create the listener 'onClose' on the request
    		
    		
			// TODO 2.4 - Include here your code to create the listener 'onError' on the request
    		
		    
			
			// TODO 3 - Include here your code to subscribe using the request
		    
			
		    
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
			// TODO 4 - Include here your code to be unsubscribed
		    
    		
    		// Set isConnected to false
    		this.isConnected = false ;
    		
		    // Set the error message as empty
		    instance.dashboard.setErrorMessage("") ;
    	}
    }
    
    return RemoteConnection ;
    
}()) ;