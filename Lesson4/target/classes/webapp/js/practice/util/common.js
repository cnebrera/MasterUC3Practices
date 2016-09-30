/**
 * Common prototype class
 */
var Common = (function () 
{
    /**
     * Public constructor
     */
    function Common()
    {
    	// Empty constructor
    }
    
    /**
     * Get the JQuery reference
     * @param stringName with the string name
     */
    Common.prototype.getJQueryRef = function(stringName)
    {
    	return $('#' + stringName) ;
    }
    
    return Common ;
    
}()) ;