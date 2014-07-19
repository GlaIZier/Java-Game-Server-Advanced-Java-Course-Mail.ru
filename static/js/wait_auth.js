var Refresh = Refresh || (function() {

	return {
			 start: function(interval) {
					  setInterval(function(){document.input.submit();}, interval); 
			       }  	
		   };

} () );

