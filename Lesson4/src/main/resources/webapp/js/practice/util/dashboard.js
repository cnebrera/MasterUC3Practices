/**
 * Dashboard prototype class
 */
var Dashboard = (function () 
{
    /**
     * Public constructor
     * @param common	 with the common utilities
     * @param statistics with the statistics
     */
    function Dashboard(common, statistics)
    {
    	this.common		   		  = common ;
    	this.statistics    		  = statistics ;
    	
    	/** Interval id for the intrument */
    	this.instrumentIntervalId = '' ;
    }
    
    /**
     * Add a new price to the dashboard
     * @param instrument with the new instrument
     */
    Dashboard.prototype.addPrice = function(instrument)
    {
    	var fixedPrice = this.fixPrice(instrument.price, 4) ;
    	
    	if (this.common.getJQueryRef(instrument.instrumentName).length)
    	{
    		this.common.getJQueryRef(instrument.instrumentName + "_price").text("Price: "   + fixedPrice) ;
    		this.common.getJQueryRef(instrument.instrumentName + "_volume").text("Volume: " + instrument.volume) ;
    	}
    	else
    	{
    		// Generate a new DIV with all the content
    		var labelInstrumentName = this.labelGenerator(instrument.instrumentName, "_instrumentName", instrument.instrumentName) ;
    		var labelState 			= this.labelGenerator(instrument.instrumentName, "_state", "Open") ;
    		var labelPrice 			= this.labelGenerator(instrument.instrumentName, "_price", "Price: "   + fixedPrice) ;
    		var labelVolume 		= this.labelGenerator(instrument.instrumentName, "_volume", "Volume: " + instrument.volume) ;
    		
    		var instrumentDivStart	= "<div id='" 			+ instrument.instrumentName + "' class='instrumentDiv' data-market='" + instrument.market 		  + 
    		   							 "' data-product='" + instrument.product 		+ "' data-instrument='" 			 	  + instrument.instrumentName + "'>" ;
    		var instrumentDivFinish = "</div>" ;
    		
    		this.common.getJQueryRef("prices").append(instrumentDivStart + labelInstrumentName + labelState + labelPrice + labelVolume + instrumentDivFinish) ;
    	}
    	
    	// Add new message to the statistics
    	this.statistics.addNewMessage() ;
    }

    /**
     * Generate a new label that will be appended to the instrument
     * @param instrumentName with the instrument name
     * @param labelSuffix    with the label suffix
     * @param value			 with the value
     */
    Dashboard.prototype.labelGenerator = function(instrumentName, labelSuffix, value)
    {
    	return "<label id='" + instrumentName + labelSuffix + "'>" + value + "</label><br/>" ;
    }

    /**
     * Fix the price
     * @param price 		with the price
     * @param decimalNumber with the decimal number
     */
    Dashboard.prototype.fixPrice = function(price, decimalNumber)
    {
    	var exp = 10 ^ decimalNumber ;
    	return parseFloat(Math.round(price * exp) / exp).toFixed(decimalNumber) ;
    }
    
    /**
     * @param intervalId with the interval id
     */
    Dashboard.prototype.setIntervalId = function(intervalId)
    {
    	this.instrumentIntervalId = intervalId ;
    }
    
    Dashboard.prototype.startButtonClick = function()
    {
    	var transportType = $("#transportType option:selected").text() ;
    	startClient(transportType) ;
    }

    Dashboard.prototype.stopButtonClick = function()
    {
    	// Firstly, stop the Dashboard update
    	window.clearInterval(this.instrumentIntervalId) ;
    	
    	// Secondly, do other tasks
    	stopClient() ;
    }
    
    /**
     * @param errorMessage with the error message
     */
    Dashboard.prototype.setErrorMessage = function(errorMessage)
    {
    	this.common.getJQueryRef("errorMessage").text(errorMessage) ;
    }

    return Dashboard ;
    
}()) ;