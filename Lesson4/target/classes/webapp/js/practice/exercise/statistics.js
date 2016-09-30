/**
 * Statistics prototype class
 */
var Statistics = (function () 
{
    /**
     * Public constructor
     * @param common with the common utilities
     */
    function Statistics(common)
    {
    	this.common			   = common ;
    	
    	this.totalMessages     = 0 ;
    	this.startTime 	   	   = 0 ;
    	this.messagesPerSecond = 0 ;
    	this.elapsedTime       = 0 ;
    }

    /**
     * Reset the statistics 
     */
    Statistics.prototype.resetStatistics = function()
    {
    	this.totalMessages = 0 ;
    	this.startTime 	   = new Date().getTime() ; 
    	
    	this.displayStatistics() ;
    }
    
    /**
     * Update the statistics values
     */
    Statistics.prototype.updateStatistics = function()
    {
    	var now 		 	   = new Date().getTime() ;
    	this.elapsedTime 	   = (now - this.startTime) / 1000 ;
    	this.messagesPerSecond = this.totalMessages / this.elapsedTime ;
    		
    	this.displayStatistics() ;
    }
    
    /**
     * Display the statistics in the dashboard
     */
    Statistics.prototype.displayStatistics = function()
    {
    	common.getJQueryRef("messagesPerSecond").text(this.messagesPerSecond.toFixed(0) + " msg/s") ;
    	common.getJQueryRef("totalTime").text(this.elapsedTime.toFixed(0) + " time") ;
    	common.getJQueryRef("totalMessages").text(this.totalMessages + " msgs") ;
    }

    /**
     * Add a new message
     */
    Statistics.prototype.addNewMessage = function()
    {
    	this.totalMessages ++ ;
    }
    
    return Statistics ;
    
}()) ;