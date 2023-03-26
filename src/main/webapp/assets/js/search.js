$(document).ready(function() {

	$("#faq_search_input").focus();
	$("#faq_search_input").livesearch({
		searchCallback: function(term) {
			//browser compatibility                                 
			if ($.browser.msie || $.browser.webkit) {
				$("#faq_search_input").keydown(function(e) {
					if (e.keyCode == 8)
						$('.gbox').html("").show();
					else if (e.keyCode == 13)
						e.preventDefault();
				});
			} else {
				$("#faq_search_input").keypress(function(e) {
					if (e.keyCode == 8)
						$('.gbox').html("").show();
					else if (e.keyCode == 13)
						e.preventDefault();
				});
			}
			//end browser compatibility
			if (term.length >= 2) {
				$.ajax({
					type: "POST",
					url: "<?php echo base_url(); ?>site/search",
					data: "keyword=" + term,
					success: function(response) {
						$('.gbox').html(response).show();
					}
				});
			} else if (term.length == 1)
				$('.gbox').html("").show();
		},

		innerText: "",
		queryDelay: 250,
		minimumSearchLength: 2
	});
});
