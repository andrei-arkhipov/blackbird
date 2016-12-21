var globalFilter = {

	typingTimer : null,
	doneTypingInterval : 900,
	button : null,

	init : function(inputSelector, buttonSelector, interval) {
		var self = this;
		self.button = $(buttonSelector);
		if (interval > 0) {
			self.doneTypingInterval = interval;
		}

		$(inputSelector).live('keyup',function() {
			clearTimeout(self.typingTimer);
			if ($(inputSelector).val) {
				self.typingTimer = setTimeout(function() {
					self.doneTyping();
				}, self.doneTypingInterval);
			}
		});
	},

	doneTyping : function() {
		console.log("Le click ");
		this.button.click();
	},
	
	focusElement : function(selector){
		var input = document.getElementById(selector);
		input.select();
		tmpStr = $(input).val();
		$(input).val('');
		$(input).val(tmpStr);
	}
};