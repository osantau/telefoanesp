var SEARCH_BOX_DEFAULT_TEXT = 'Enter the keywords:';

var AJAX_PENDING_TIMER;
var CURRENT_PAGE = 1;
var CURRENT_LIMIT = 5;

function init() {

	var sTextBox   = $("#search_val");

	sTextBox.focus(function() {
		if(this.value == SEARCH_BOX_DEFAULT_TEXT) {
			this.value = '';
		}
	});
	sTextBox.blur(function() {
		if(this.value == '') {
			this.value = SEARCH_BOX_DEFAULT_TEXT;
		}
	});
	sTextBox.blur();


	sTextBox.keyup(function() {
		var q    = $("#search_val").val();
		if( q == SEARCH_BOX_DEFAULT_TEXT || q == '' || q == undefined || q.length<=3) {
			HideLiveSearch();
		}
		else {
			clearTimeout(AJAX_PENDING_TIMER);
			CURRENT_PAGE = 1;
			AJAX_PENDING_TIMER = setTimeout("PerformLiveSearch()",300);
		}
		
	});
	
	$("#livesearch_result_close_link").click(function() {
		HideLiveSearch();
	});

}

function NextPage(p) {
	if(p['has_next']) {
		AJAX_IS_RUNNING = false;
		CURRENT_PAGE++;
		PerformLiveSearch();
	}
}
function PrevPage(p) {
	if(p['has_prev']) {
		AJAX_IS_RUNNING = false;
		CURRENT_PAGE--;
		PerformLiveSearch();
	}
}

function ShowLoaders() {
	$('#ajaxloader').fadeIn('fast');

	if( $('#livesearch').css('display') == 'block' ) {
		var h = $('#livesearch').height() - 5;
		var w = $('#livesearch').width() - 45;
		$('#loader_div').width(w);
		$('#loader_div').height(h);
		$('#loader_div p').css('margin-top',(h/2)+20);
		$('#loader_div').fadeIn('fast');
	}
}

function HideLoaders() {
	$('#ajaxloader').fadeOut('fast');
	$('#loader_div').hide();
}

var AJAX_IS_RUNNING = false;
function PerformLiveSearch() {

	if(AJAX_IS_RUNNING == true) {
		return;
	}

	var query      = $("#search_val");
	var q_val      = (query.val() && query.val() != SEARCH_BOX_DEFAULT_TEXT) ? query.val() : '';	

	if(q_val == '') {
		return;
	}
	AJAX_IS_RUNNING = true;

	$.ajax({
		url:        './search',
		data: {
			query: q_val,
			output: 'json',
			page: CURRENT_PAGE,
			limit: CURRENT_LIMIT
		},
		type:       'GET',
		timeout:    '5000',
		dataType:   'json',
		beforeSend: function() {
			// Before send request
			// Show the loader
			AJAX_IS_RUNNING = true;
			ShowLoaders();
		},
		complete: function() {
			// When Sent request
			// Hide the loader
			AJAX_IS_RUNNING = false;
			HideLoaders();
		},
		success: function(data, textStatus) {
			AJAX_IS_RUNNING = false;
			HideLoaders();
			$('#livesearch').slideDown();

			// clear the results
			$(".livesearch_results dd").remove();
			var resultsNav = $('.livesearch_results dt');

			if( data['results'].length ) {

				// add the new results (in reverse since the
				// append inserts right after the header and not
				// at the end of the result list
				var current = resultsNav;
				for(i=data['results'].length;i>0;i--) {
					current.after(
						DisplayResult(data['results'][i-1])
					);
				}
			}
			else {
				resultsNav.after('<dd id=""><p>No articles found with these search terms</p></dd>');
			}
			
			// Pagination at the bottom of LiveSearch panel
			Pagination(data['paging'],".livesearch_navbody");

		},
		
		// We're gonna hide everything when get error
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			AJAX_IS_RUNNING = false;
			HideLoaders();
			HideLiveSearch();
		}
	});

}

function DisplayResult(row) {
	var output = '<dd id="">';
	output += '<a href="' + row['url'] + '">';
	output += '<p>';
	output += '<b>' + row['title'] + '</b>';
	output += row['summary'];
	output += '</p></a></dd>';
	return output;
}
function Pagination(p,selector) {

	$(selector + " *").remove();

	if(p['start_idx'] != undefined) {
		var html = '<span class="livesearch_legend">' + p['start_idx'] + ' - ' + p['end_idx'] + ' of ' + p['total'] + ' Results</span>';
		html += '<a class="livesearch_next" href="javascript:void(0);" title="Next 5 Results"><em>Next</em></a>';
		html += '<a class="livesearch_prev" href="javascript:void(0);" title="Previous 5 Results"><em>Previous</em></a>';
		html += '<div class="clearfix">&nbsp;</div>';

		$(selector).append(html);
	}
	else {
		var html = '<span class="kbls_legend">0 Results</span>';
		html += '<a class="livesearch_next" href="javascript:void(0);" title="Next 5 Results"><em>Next</em></a>';
		html += '<a class="livesearch_prev" href="javascript:void(0);" title="Previous 5 Results"><em>Previous</em></a>';
		html += '<div class="clearfix">&nbsp;</div>';

		$(selector).append(html);
	}

	$(selector + " .livesearch_next").click(function() {
		NextPage(p);
	});
	$(selector + " .livesearch_prev").click(function() {
		PrevPage(p);
	});

}

function HideLiveSearch() {
	$('#livesearch').slideUp();
	$('#ajaxloader').fadeOut('fast');
}

$(document).ready(function() {
	init();
});
